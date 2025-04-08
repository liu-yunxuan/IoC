package asia.liuyunxuan.ioc.component.container.support;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.container.config.ComponentDefinition;

import java.lang.reflect.Constructor;

/**
 * Bean实例化策略接口。
 * <p>
 * 定义了创建Bean实例的策略接口，不同的实现类可以提供不同的实例化方式，
 * 例如通过JDK的反射机制、CGLib等。这种策略模式的设计使得Bean的实例化过程
 * 可以灵活切换和扩展。
 */
public interface InstantiationStrategy {
    /**
     * 实例化Bean对象的方法。
     *
     * @param componentDefinition Bean的定义信息
     * @param beanName Bean的名称
     * @param ctor 指定的构造函数，如果为null则使用默认构造函数
     * @param args 构造函数参数，如果使用默认构造函数则为null
     * @return 实例化的Bean对象
     * @throws ComponentException 当实例化过程中发生异常时抛出
     */
    Object instantiate(ComponentDefinition componentDefinition, String beanName, Constructor<?> ctor, Object[] args) throws ComponentException;
}
