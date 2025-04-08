package asia.liuyunxuan.ioc.context.event;

import asia.liuyunxuan.ioc.context.ApplicationContext;

/**
 * 当ApplicationContext被关闭时发布的事件。
 * 
 * <p>这个事件表明所有单例bean都将被销毁。发布此事件后，
 * 所有的bean都将被销毁，除非手动重新刷新上下文。
 * 
 * <p>关闭事件的发布标志着容器生命周期的结束，之后如果没有
 * 重新刷新，该上下文将不能再被使用。
 *
 * @author liuyunxuan
 * @see ApplicationContext
 * @see ApplicationContextEvent
 * @since 1.0
 */
public class ContextClosedEvent extends ApplicationContextEvent {

    /** 序列化版本ID */
    private static final long serialVersionUID = 1L;

    /**
     * 创建一个新的ContextClosedEvent实例。
     *
     * @param source 事件源（必须是ApplicationContext），不能为null
     */
    public ContextClosedEvent(Object source) {
        super(source);
    }
}
