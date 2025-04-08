package asia.liuyunxuan.ioc.beans.factory.support;

import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FactoryBean注册表的支持类。
 * <p>
 * 该抽象类继承自DefaultSingletonBeanRegistry，为FactoryBean的注册和管理提供基础设施。
 * 它维护了FactoryBean创建的单例对象的缓存，并处理FactoryBean的生命周期。
 */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    /**
     * Cache of singleton objects created by FactoryBeans: FactoryBean name --> object
     */
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    /**
     * 获取FactoryBean创建的单例对象的缓存。
     *
     * @param beanName Bean的名称
     * @return 缓存的对象，如果不存在则返回null
     */
    protected Object getCachedObjectForFactoryBean(String beanName) {
        Object object = this.factoryBeanObjectCache.get(beanName);
        return (object != NULL_OBJECT ? object : null);
    }

    /**
     * 从FactoryBean获取对象。
     * <p>
     * 如果FactoryBean配置为单例，则会缓存创建的对象。
     *
     * @param factory FactoryBean实例
     * @param beanName Bean的名称
     * @return 创建的对象
     */
    protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName) {
        if (factory.isSingleton()) {
            Object object = this.factoryBeanObjectCache.get(beanName);
            if (object == null) {
                object = doGetObjectFromFactoryBean(factory, beanName);
                this.factoryBeanObjectCache.put(beanName, (object != null ? object : NULL_OBJECT));
            }
            return (object != NULL_OBJECT ? object : null);
        } else {
            return doGetObjectFromFactoryBean(factory, beanName);
        }
    }

    /**
     * 执行从FactoryBean获取对象的实际逻辑。
     *
     * @param factory FactoryBean实例
     * @param beanName Bean的名称
     * @return 创建的对象
     * @throws BeansException 当创建对象过程中发生异常时抛出
     */
    private Object doGetObjectFromFactoryBean(final FactoryBean<?> factory, final String beanName){
        try {
            return factory.getObject();
        } catch (Exception e) {
            throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", e);
        }
    }

}
