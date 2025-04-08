package asia.liuyunxuan.ioc.runtime;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.runtime.support.AbstractContext;
import asia.liuyunxuan.ioc.runtime.support.ClassPathXmlContext;

/**
 * 可配置的应用上下文接口，扩展了ApplicationContext接口，
 * 添加了配置和生命周期管理功能。
 * 
 * <p>该接口提供了刷新和关闭应用上下文的能力，是所有应用上下文实现类
 * 必须实现的接口。它定义了应用上下文的标准生命周期方法。
 * 
 * <p>ApplicationContext的典型实现类都实现了这个接口。
 * 使用这个接口可以以编程方式控制容器的启动、刷新和关闭，
 * 而不仅仅是依赖于JVM关闭钩子。
 *
 * @author liuyunxuan
 * @see RuntimeContext
 * @see AbstractContext
 * @see ClassPathXmlContext
 * @since 1.0
 */
public interface ConfigurableContext extends RuntimeContext {

    /**
     * 刷新容器。
     * <p>加载或刷新配置，可能是XML文件、属性文件或关系数据库模式。
     * <p>由于这是一个启动方法，如果失败应该销毁已创建的单例，以避免悬空资源。
     * 换句话说，在调用该方法之后，要么全部实例化，要么完全不实例化。
     *
     * @throws ComponentException 如果bean创建失败
     */
    void refresh() throws ComponentException;

    /**
     * 注册一个JVM关闭钩子，在JVM关闭时关闭这个上下文，
     * 除非它当时已经关闭。
     * <p>这个方法可以在上下文创建后立即调用，以避免在应用程序
     * 意外关闭时忘记关闭上下文。
     */
    void registerShutdownHook();

    /**
     * 关闭此应用上下文，释放实现可能持有的所有资源和锁。
     * <p>这包括销毁所有缓存的单例bean。
     * <p>注意：不会关闭父上下文（如果有的话）。
     */
    void close();

}
