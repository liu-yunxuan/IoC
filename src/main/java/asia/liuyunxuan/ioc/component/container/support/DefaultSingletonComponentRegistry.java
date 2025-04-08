package asia.liuyunxuan.ioc.component.container.support;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.container.DisposableComponent;
import asia.liuyunxuan.ioc.component.container.ObjectFactory;
import asia.liuyunxuan.ioc.component.container.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的单例Bean注册表实现类。
 * <p>
 * 该类实现了SingletonBeanRegistry接口，提供了单例Bean的注册和获取功能。
 * 它通过三级缓存机制来解决循环依赖问题：
 * <ul>
 * <li>一级缓存（singletonObjects）：用于存储完全初始化好的Bean</li>
 * <li>二级缓存（earlySingletonObjects）：用于存储原始的Bean对象</li>
 * <li>三级缓存（singletonFactories）：用于存储Bean的工厂对象</li>
 * </ul>
 */
public class DefaultSingletonComponentRegistry implements SingletonBeanRegistry {

    protected static final Object NULL_OBJECT = new Object();

    // 一级缓存，普通对象
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    // 二级缓存，提前暴漏对象，没有完全实例化的对象
    protected final Map<String, Object> earlySingletonObjects = new HashMap<>();

    // 三级缓存，存放代理对象
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();

    private final Map<String, DisposableComponent> disposableBeans = new LinkedHashMap<>();

    /**
     * 获取单例Bean。
     * <p>
     * 该方法实现了三级缓存机制，用于解决循环依赖问题：
     * 1. 先从一级缓存singletonObjects中获取
     * 2. 如果没有，再从二级缓存earlySingletonObjects中获取
     * 3. 如果还没有，则尝试从三级缓存singletonFactories中获取并放入二级缓存
     *
     * @param beanName Bean的名称
     * @return 单例Bean实例，如果不存在则返回null
     */
    @Override
    public Object getSingleton(String beanName) {
        Object singletonObject = singletonObjects.get(beanName);
        if (null == singletonObject) {
            singletonObject = earlySingletonObjects.get(beanName);
            // 判断二级缓存中是否有对象，这个对象就是代理对象，因为只有代理对象才会放到三级缓存中
            if (null == singletonObject) {
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    // 把三级缓存中的代理对象中的真实对象获取出来，放入二级缓存中
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    /**
     * 注册单例Bean。
     *
     * @param beanName Bean的名称
     * @param singletonObject Bean实例
     */
    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }

    /**
     * 添加单例工厂。
     * <p>
     * 将单例工厂添加到三级缓存中，用于解决循环依赖。
     *
     * @param beanName Bean的名称
     * @param singletonFactory 创建Bean的工厂
     */
    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory){
        if (!this.singletonObjects.containsKey(beanName)) {
            this.singletonFactories.put(beanName, singletonFactory);
            this.earlySingletonObjects.remove(beanName);
        }
    }


    /**
     * 注册需要销毁的Bean。
     *
     * @param beanName Bean的名称
     * @param bean 需要销毁的Bean实例
     */
    public void registerDisposableBean(String beanName, DisposableComponent bean) {
        disposableBeans.put(beanName, bean);
    }

    /**
     * 销毁所有单例Bean。
     * <p>
     * 按照销毁顺序逆序调用Bean的销毁方法。
     */

    public void destroySingletons() {
        Set<String> keySet = this.disposableBeans.keySet();
        String[] disposableBeanNames = keySet.toArray(new String[0]);

        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
            String beanName = disposableBeanNames[i];
            DisposableComponent disposableComponent = disposableBeans.remove(beanName);
            if (disposableComponent != null) {
                try {
                    disposableComponent.destroy();
                } catch (Exception e) {
                    System.err.println("Error destroying bean with name: " + beanName);
                    throw new ComponentException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
                }
            }
        }
    }

}
