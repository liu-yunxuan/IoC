package asia.liuyunxuan.ioc.beans.factory;

import asia.liuyunxuan.ioc.beans.BeansException;

import java.util.Map;

/**
 * 扩展了BeanFactory接口，提供了枚举bean的能力。
 * <p>
 * 这个接口定义了访问容器中bean定义的方法，可以：
 * <ul>
 *     <li>列出容器中所有bean的名称</li>
 *     <li>按照类型查找bean</li>
 *     <li>检查容器中特定名称的bean定义</li>
 * </ul>
 * <p>
 * 这个接口通常被那些想要遍历容器配置的工具或框架使用，
 * 而不是由普通的应用代码使用。
 *
 * @see BeanFactory
 * @see ConfigurableListableBeanFactory
 */
public interface ListableBeanFactory extends BeanFactory {

    /**
     * 返回指定类型的所有bean实例
     *
     * @param type bean的类型
     * @param <T> bean类型参数
     * @return 一个Map，key是bean名称，value是bean实例
     * @throws BeansException 如果获取bean过程中发生错误
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    /**
     * 返回容器中所有bean定义的名称
     *
     * @return bean定义名称的数组
     */
    String[] getBeanDefinitionNames();

}
