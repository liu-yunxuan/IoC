package asia.liuyunxuan.ioc.runtime.message;

import asia.liuyunxuan.ioc.runtime.RuntimeContext;

/**
 * 应用上下文事件的基类，所有与ApplicationContext相关的事件都应继承此类。
 * 
 * <p>此类提供了一个方便的方法来获取发布事件的ApplicationContext。
 * 事件源（source）必须是发布事件的ApplicationContext实例。
 * 
 * <p>典型的子类包括ContextRefreshedEvent和ContextClosedEvent，分别表示
 * 上下文刷新完成和上下文关闭的事件。
 *
 * @author liuyunxuan
 * @see RuntimeContext
 * @see ContextRefreshedEvent
 * @see ContextClosedEvent
 * @since 1.0
 */
public class Message extends asia.liuyunxuan.ioc.runtime.Message {

    /** 序列化版本ID */
    private static final long serialVersionUID = 1L;

    /**
     * 创建一个新的ApplicationContextEvent。
     *
     * @param source 事件源（必须是ApplicationContext），不能为null
     */
    public Message(Object source) {
        super(source);
    }

    /**
     * 获取发布此事件的ApplicationContext。
     * <p>此方法被标记为final，确保子类不能覆盖此行为。
     *
     * @return 发布此事件的ApplicationContext
     */
    public final RuntimeContext getApplicationContext() {
        return (RuntimeContext) getSource();
    }

}
