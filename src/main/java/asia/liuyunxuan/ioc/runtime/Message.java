package asia.liuyunxuan.ioc.runtime;

import java.util.EventObject;

/**
 * 应用事件的基类，所有应用内事件都应该继承此类。
 * 
 * <p>这是一个标准的观察者设计模式实现，用于在应用程序中实现松耦合的事件处理机制。
 * 事件可以由任何对象发布，并由实现了ApplicationListener接口的Bean处理。
 * 
 * <p>此类继承自Java标准库的EventObject，提供了事件源对象的引用。事件源通常是
 * 发布事件的对象，例如ApplicationContext实例。
 *
 * @author liuyunxuan
 * @see MessageSubscriber
 * @see MessagePublisher
 * @see java.util.EventObject
 * @since 1.0
 */
public abstract class Message extends EventObject {

    /** 序列化版本ID */
    private static final long serialVersionUID = 1L;

    /**
     * 创建一个新的ApplicationEvent。
     *
     * @param source 事件源对象，不能为null
     */
    public Message(Object source) {
        super(source);
    }

}
