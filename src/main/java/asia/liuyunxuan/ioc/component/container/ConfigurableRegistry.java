package asia.liuyunxuan.ioc.component.container;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.container.config.AutowireCapableComponentProvider;
import asia.liuyunxuan.ioc.component.container.config.ComponentDefinition;
import asia.liuyunxuan.ioc.component.container.config.BeanPostProcessor;
import asia.liuyunxuan.ioc.component.container.config.ConfigurableProvider;

/**
 * 提供了最完整的bean工厂配置机制。
 * <p>
 * 这个接口扩展了ListableBeanFactory接口，同时整合了AutowireCapableBeanFactory和
 * ConfigurableBeanFactory的功能，是一个综合性的配置接口。它主要提供了：
 * <ul>
 *     <li>配置bean工厂的能力</li>
 *     <li>枚举bean实例的能力</li>
 *     <li>自动装配bean的能力</li>
 *     <li>管理bean生命周期的能力</li>
 * </ul>
 *
 * @see ComponentRegistry
 * @see AutowireCapableComponentProvider
 * @see ConfigurableProvider
 */
public interface ConfigurableRegistry extends ComponentRegistry, AutowireCapableComponentProvider, ConfigurableProvider {

    /**
     * 根据bean的名称获取bean的定义信息
     *
     * @param beanName bean的名称
     * @return bean的定义信息
     * @throws ComponentException 如果找不到指定名称的bean定义
     */
    ComponentDefinition getBeanDefinition(String beanName) throws ComponentException;

    /**
     * 预实例化所有单例bean
     *
     * @throws ComponentException 如果预实例化过程中发生错误
     */
    void preInstantiateSingletons() throws ComponentException;

    /**
     * 添加bean的后置处理器
     *
     * @param beanPostProcessor 要添加的bean后置处理器
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}