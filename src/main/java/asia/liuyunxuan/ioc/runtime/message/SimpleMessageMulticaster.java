package asia.liuyunxuan.ioc.runtime.message;

import asia.liuyunxuan.ioc.component.container.ComponentProvider;
import asia.liuyunxuan.ioc.runtime.Message;
import asia.liuyunxuan.ioc.runtime.MessageSubscriber;

/**
 * ApplicationEventMulticaster接口的简单实现，提供基本的事件广播功能。
 * 
 * <p>此类继承自AbstractApplicationEventMulticaster，实现了事件分发的核心逻辑。
 * 它以同步方式将事件分发给所有匹配的监听器，即在当前线程中直接调用监听器的
 * onApplicationEvent方法。
 * 
 * <p>此实现不提供任何事件过滤或自定义执行策略，适用于简单场景或作为自定义
 * 事件广播器的基础。
 *
 * @author liuyunxuan
 * @see AbstractMessageMulticaster
 * @see MessageMulticaster
 * @see MessageSubscriber
 * @see Message
 * @since 1.0
 */
public class SimpleMessageMulticaster extends AbstractMessageMulticaster {

    /**
     * 创建一个新的SimpleApplicationEventMulticaster实例。
     * 
     * @param componentProvider 要设置的BeanFactory，不能为null
     */
    public SimpleMessageMulticaster(ComponentProvider componentProvider) {
        setBeanFactory(componentProvider);
    }

    /**
     * 将给定的应用事件广播到所有匹配的监听器。
     * <p>此实现以同步方式调用每个监听器的onApplicationEvent方法，
     * 在当前线程中顺序执行所有监听器。
     * 
     * @param event 要广播的事件，不能为null
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void multicastEvent(final Message event) {
        for (final MessageSubscriber listener : getApplicationListeners(event)) {
            listener.onApplicationEvent(event);
        }
    }

}
