package asia.liuyunxuan.ioc.context;

/**
 * 应用事件发布者接口，定义了发布应用事件的能力。
 * 
 * <p>这是观察者设计模式的一部分，用于在应用程序中实现事件处理机制。
 * ApplicationContext接口继承了此接口，因此所有的应用上下文都可以
 * 作为事件发布者。
 * 
 * <p>事件可以是任何扩展自ApplicationEvent的类型，将被分发给所有
 * 匹配的ApplicationListener。
 *
 * @author liuyunxuan
 * @see ApplicationEvent
 * @see ApplicationListener
 * @since 1.0
 */
public interface ApplicationEventPublisher {

    /**
     * 发布应用事件。
     * <p>事件将被分发给所有匹配的ApplicationListener。
     *
     * @param event 要发布的应用事件，不能为null
     */
    void publishEvent(ApplicationEvent event);

}
