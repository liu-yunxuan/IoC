package asia.liuyunxuan.ioc.context.event;

import asia.liuyunxuan.ioc.context.ApplicationContext;

/**
 * 当ApplicationContext完成初始化或刷新时发布的事件。
 * 
 * <p>这个事件表明所有的bean都已经被成功加载，后处理器都已经被激活，
 * 所有单例bean都已经被实例化，并且ApplicationContext已经可以使用了。
 * 
 * <p>如果ApplicationContext支持多次刷新，每次刷新都会发布一个新的
 * ContextRefreshedEvent事件。
 *
 * @author liuyunxuan
 * @see ApplicationContext
 * @see ApplicationContextEvent
 * @since 1.0
 */
public class ContextRefreshedEvent extends ApplicationContextEvent {

    /** 序列化版本ID */
    private static final long serialVersionUID = 1L;

    /**
     * 创建一个新的ContextRefreshedEvent实例。
     *
     * @param source 事件源（必须是ApplicationContext），不能为null
     */
    public ContextRefreshedEvent(Object source) {
        super(source);
    }
}
