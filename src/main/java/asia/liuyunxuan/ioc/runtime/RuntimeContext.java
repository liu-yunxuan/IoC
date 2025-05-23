package asia.liuyunxuan.ioc.runtime;

import asia.liuyunxuan.ioc.component.container.ComponentProvider;
import asia.liuyunxuan.ioc.component.container.ComponentRegistry;
import asia.liuyunxuan.ioc.component.container.HierarchicalProvider;
import asia.liuyunxuan.ioc.kernel.io.ResourceLoader;
import asia.liuyunxuan.ioc.runtime.support.ClassPathXmlContext;

/**
 * 应用上下文接口，是IoC容器的核心接口，提供了高级容器功能。
 * 
 * <p>ApplicationContext是整个框架的中央接口，它继承了多个接口，提供了完整的应用程序配置框架和
 * 基本功能。它负责实例化、配置和组装Bean，同时也负责管理Bean的生命周期。
 * 
 * <p>与普通的BeanFactory相比，ApplicationContext提供了更多的企业级功能：
 * <ul>
 * <li>支持国际化消息解析</li>
 * <li>支持事件发布</li>
 * <li>支持特定上下文的资源加载</li>
 * <li>支持层次性上下文，允许每个上下文专注于特定层，如web层</li>
 * </ul>
 * 
 * <p>ApplicationContext通常是只读的，但可以通过ConfigurableApplicationContext接口
 * 进行配置和刷新。
 *
 * @author liuyunxuan
 * @see ConfigurableContext
 * @see ComponentProvider
 * @see ClassPathXmlContext
 * @since 1.0
 */
public interface RuntimeContext extends ComponentRegistry, HierarchicalProvider, ResourceLoader, MessagePublisher {
    // 核心方法已由继承的接口提供
}
