package asia.liuyunxuan.ioc.component.container.support;


import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.kernel.io.Resource;
import asia.liuyunxuan.ioc.kernel.io.ResourceLoader;

/**
 * Bean定义读取器接口。
 * <p>
 * 该接口定义了读取Bean定义信息的抽象行为。实现类负责从不同来源（如XML、注解等）
 * 读取Bean定义信息，并将其注册到容器中。它是Spring容器加载Bean定义的核心接口。
 */
public interface ComponentDefinitionReader {

    /**
     * 获取Bean定义注册表。
     *
     * @return Bean定义注册表实例
     */
    ComponentDefinitionRegistry getRegistry();

    /**
     * 获取资源加载器。
     *
     * @return 资源加载器实例
     */
    ResourceLoader getResourceLoader();

    /**
     * 从指定的资源加载Bean定义。
     *
     * @param resource 资源对象
     * @throws ComponentException 当加载过程中发生错误时抛出
     */
    void loadBeanDefinitions(Resource resource) throws ComponentException;

    /**
     * 从多个资源中加载Bean定义。
     *
     * @param resources 资源对象数组
     * @throws ComponentException 当加载过程中发生错误时抛出
     */
    void loadBeanDefinitions(Resource... resources) throws ComponentException;

    /**
     * 从指定位置加载Bean定义。
     *
     * @param location 资源位置
     * @throws ComponentException 当加载过程中发生错误时抛出
     */
    void loadBeanDefinitions(String location) throws ComponentException;

    /**
     * 从多个位置加载Bean定义。
     *
     * @param locations 资源位置数组
     * @throws ComponentException 当加载过程中发生错误时抛出
     */
    void loadBeanDefinitions(String... locations) throws ComponentException;

}
