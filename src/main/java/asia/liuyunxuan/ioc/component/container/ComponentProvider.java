package asia.liuyunxuan.ioc.component.container;

import asia.liuyunxuan.ioc.component.ComponentException;

/**
 * IoC容器的根接口，定义了获取bean实例的基本方法。
 * <p>
 * BeanFactory作为最基本的IoC容器，提供了配置框架和基本功能。它主要的功能包括：
 * <ul>
 *     <li>通过名称获取bean实例</li>
 *     <li>支持带参数的bean实例获取</li>
 *     <li>支持按类型获取bean实例</li>
 * </ul>
 * <p>
 * 实现此接口的类需要能够加载、实例化、组装和提供bean实例。
 *
 * @see ComponentRegistry
 * @see HierarchicalProvider
 * @see ConfigurableRegistry
 */
public interface ComponentProvider {
    /**
     * 根据bean的名称获取在IoC容器中的bean实例
     *
     * @param name 要检索的bean的名称
     * @return bean实例
     * @throws ComponentException 如果无法获取bean
     */
    Object getBean(String name) throws ComponentException;

    /**
     * 根据bean的名称和构造参数获取在IoC容器中的bean实例
     *
     * @param name 要检索的bean的名称
     * @param args 构造函数的参数
     * @return bean实例
     * @throws ComponentException 如果无法获取bean
     */
    Object getBean(String name, Object... args) throws ComponentException;

    /**
     * 根据bean的名称和类型获取在IoC容器中的bean实例
     *
     * @param name bean的名称
     * @param requiredType 期望的bean类型
     * @param <T> bean的类型
     * @return bean实例
     * @throws ComponentException 如果无法获取bean或类型不匹配
     */
    <T> T getBean(String name, Class<T> requiredType) throws ComponentException;

    /**
     * 根据bean的类型获取在IoC容器中的bean实例
     *
     * @param requiredType 期望的bean类型
     * @param <T> bean的类型
     * @return bean实例
     * @throws ComponentException 如果无法获取bean或找到多个匹配的bean
     */
    <T> T getBean(Class<T> requiredType) throws ComponentException;
}
