package asia.liuyunxuan.ioc.component.container.config;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.container.ConfigurableRegistry;

/**
 * Bean工厂后处理器接口，允许自定义修改应用上下文中的Bean定义信息。
 * <p>
 * 该接口作为Spring IoC容器的扩展点之一，主要用途包括：
 * <ul>
 *     <li>在Bean实例化之前修改其定义信息（ComponentDefinition）</li>
 *     <li>修改Bean工厂的配置元数据</li>
 *     <li>条件化地注册或移除Bean定义</li>
 *     <li>修改Bean的作用域、属性值等配置信息</li>
 * </ul>
 * <p>
 * 工作流程：
 * <ol>
 *     <li>Spring IoC容器加载所有Bean定义信息</li>
 *     <li>调用所有BeanFactoryPostProcessor的实现类</li>
 *     <li>根据处理后的Bean定义信息实例化Bean对象</li>
 * </ol>
 * <p>
 * 注意：BeanFactoryPostProcessor在Bean实例化之前执行，
 * 因此它只能修改Bean的配置元数据，而不能修改Bean实例。
 *
 * @see ConfigurableRegistry
 * @see ComponentDefinition
 * @see ComponentException
 */
public interface ComponentProviderPostProcessor {

    /**
     * 在所有的BeanDefinition加载完成后，Bean实例化之前执行的操作
     * <p>
     * 通过该方法，可以修改BeanDefinition的属性值或者增加其他定制化的配置信息。
     * 这个阶段Bean还未实例化，但所有的BeanDefinition已经加载到BeanFactory中。
     *
     * @param beanFactory 可配置的Bean工厂，提供查询和修改Bean定义的能力
     * @throws ComponentException 当处理过程中出现错误时抛出
     */
    void postProcessBeanFactory(ConfigurableRegistry beanFactory) throws ComponentException;

}
