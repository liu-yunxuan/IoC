package asia.liuyunxuan.ioc.context;

import java.util.EventListener;

/**
 * 应用事件监听器接口，用于处理应用事件。
 * 
 * <p>这是标准观察者设计模式的一部分，用于实现应用程序中的事件处理机制。
 * 所有希望监听ApplicationEvent的Bean都应该实现此接口。
 * 
 * <p>此接口是泛型化的，允许监听器只接收特定类型的事件。当事件发布时，
 * 只有支持该事件类型的监听器才会被通知。
 * 
 * <p>ApplicationListener可以通过实现Ordered接口或使用@Order注解来指定处理顺序。
 *
 * @author liuyunxuan
 * @param <E> 此监听器可以处理的ApplicationEvent类型
 * @see ApplicationEvent
 * @see ApplicationEventPublisher
 * @see java.util.EventListener
 * @since 1.0
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * 处理应用事件。
     * <p>当发布与此监听器匹配的事件时，此方法将被调用。
     *
     * @param event 要响应的事件，不会为null
     */
    void onApplicationEvent(E event);

}
