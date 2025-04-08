package asia.liuyunxuan.ioc.component.container.support;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.container.config.ComponentDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * JDK反射方式实例化Bean的策略实现类。
 * <p>
 * 该类通过Java反射API实现Bean的实例化，是最基础的实例化策略。
 * 它直接使用Class对象的getDeclaredConstructor方法获取构造函数，
 * 然后通过newInstance方法创建实例。
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {

    public SimpleInstantiationStrategy() {
    }


    /**
     * 实例化Bean对象。
     *
     * @param componentDefinition Bean的定义信息
     * @param beanName Bean的名称
     * @param ctor 指定的构造函数，如果为null则使用默认构造函数
     * @param args 构造函数参数，如果使用默认构造函数则为null
     * @return 实例化的Bean对象
     * @throws ComponentException 当实例化过程中发生异常时抛出
     */
    @Override
    public Object instantiate(ComponentDefinition componentDefinition, String beanName, Constructor<?> ctor, Object[] args) throws ComponentException {
        Class<?> clazz = componentDefinition.getBeanClass();
        try {
            if (null != ctor) {
                return clazz.getDeclaredConstructor(ctor.getParameterTypes()).newInstance(args);
            } else {
                return clazz.getDeclaredConstructor().newInstance();
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ComponentException("无法实例化[" + clazz.getName() + "]", e);
        }
    }
}
