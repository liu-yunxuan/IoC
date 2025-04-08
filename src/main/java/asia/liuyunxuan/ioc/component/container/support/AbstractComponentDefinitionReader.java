package asia.liuyunxuan.ioc.component.container.support;


import asia.liuyunxuan.ioc.kernel.io.DefaultResourceLoader;
import asia.liuyunxuan.ioc.kernel.io.ResourceLoader;

/**
 * Bean定义读取器的抽象基类，为Bean定义的加载提供基础设施。
 * 
 * <p>该类实现了{@link ComponentDefinitionReader}接口，为具体的Bean定义读取器提供了通用的功能支持。
 * 主要包括：
 * <ul>
 *     <li>Bean定义注册表的引用维护</li>
 *     <li>资源加载器的管理</li>
 * </ul>
 * 
 * <p>子类需要实现具体的Bean定义加载逻辑，例如XML配置文件的解析等。通过继承该类，
 * 子类可以专注于特定格式的配置文件解析，而无需关心资源加载和注册表维护等基础设施。
 *
 * @see ComponentDefinitionReader
 * @see ComponentDefinitionRegistry
 * @see ResourceLoader
 */
public abstract class AbstractComponentDefinitionReader implements ComponentDefinitionReader {

    /**
     * Bean定义注册表，用于注册解析出的Bean定义
     */
    private final ComponentDefinitionRegistry registry;

    /**
     * 资源加载器，用于加载Bean定义配置资源
     */
    private final ResourceLoader resourceLoader;

    /**
     * 使用指定的Bean定义注册表构造读取器。
     * 内部会创建一个默认的资源加载器{@link DefaultResourceLoader}。
     *
     * @param registry Bean定义注册表
     */
    protected AbstractComponentDefinitionReader(ComponentDefinitionRegistry registry) {
        this(registry, new DefaultResourceLoader());
    }

    /**
     * 使用指定的Bean定义注册表和资源加载器构造读取器。
     *
     * @param registry Bean定义注册表
     * @param resourceLoader 资源加载器
     */
    public AbstractComponentDefinitionReader(ComponentDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    /**
     * 获取Bean定义注册表。
     *
     * @return Bean定义注册表
     */
    @Override
    public ComponentDefinitionRegistry getRegistry() {
        return registry;
    }

    /**
     * 获取资源加载器。
     *
     * @return 资源加载器
     */
    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

}
