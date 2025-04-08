package asia.liuyunxuan.ioc.component.container.support;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.container.config.ComponentDefinition;

/**
 * Bean定义注册表接口。
 * <p>
 * 该接口定义了管理BeanDefinition的注册中心的基本操作。实现类负责
 * 存储和管理BeanDefinition信息，是容器管理Bean定义的核心接口。
 */
public interface ComponentDefinitionRegistry {

    /**
     * 注册一个新的BeanDefinition。
     *
     * @param beanName Bean的名称
     * @param componentDefinition Bean的定义信息
     */
    void registerBeanDefinition(String beanName, ComponentDefinition componentDefinition);


    /**
     * 获取指定名称的BeanDefinition。
     *
     * @param beanName Bean的名称
     * @return Bean的定义信息
     * @throws ComponentException 当找不到指定的BeanDefinition时抛出
     */
    ComponentDefinition getBeanDefinition(String beanName) throws ComponentException;


    /**
     * 判断是否包含指定名称的BeanDefinition。
     *
     * @param beanName Bean的名称
     * @return 如果包含返回true，否则返回false
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 获取所有已注册的Bean的名称。
     *
     * @return 已注册的所有Bean的名称数组
     */
    String[] getBeanDefinitionNames();

}
