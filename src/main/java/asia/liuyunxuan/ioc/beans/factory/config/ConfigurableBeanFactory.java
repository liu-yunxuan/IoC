package asia.liuyunxuan.ioc.beans.factory.config;


import asia.liuyunxuan.ioc.beans.factory.HierarchicalBeanFactory;
import asia.liuyunxuan.ioc.utils.StringValueResolver;

/**
 * 可配置的Bean工厂接口，提供配置Bean工厂的各种能力。
 * 定义了Bean的作用域常量，并提供了配置Bean后处理器、嵌入值解析器等功能。
 * 继承了HierarchicalBeanFactory和SingletonBeanRegistry接口，具备层级性和单例Bean注册能力。
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    /** 单例作用域标识符 */
    String SCOPE_SINGLETON = "singleton";

    /** 原型作用域标识符 */
    String SCOPE_PROTOTYPE = "prototype";

    /**
     * 添加Bean后处理器
     * @param beanPostProcessor 要添加的Bean后处理器
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * 销毁所有单例Bean
     */
    void destroySingletons();

    /**
     * 添加嵌入值解析器
     * @param valueResolver 要添加的值解析器
     */
    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    /**
     * 解析嵌入的值，如注解属性中的占位符
     * @param value 要解析的值
     * @return 解析后的值
     */
    String resolveEmbeddedValue(String value);
}
