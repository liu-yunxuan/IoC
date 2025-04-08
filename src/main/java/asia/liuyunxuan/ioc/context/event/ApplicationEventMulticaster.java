package asia.liuyunxuan.ioc.context.event;

import asia.liuyunxuan.ioc.context.ApplicationEvent;
import asia.liuyunxuan.ioc.context.ApplicationListener;

/**
 * 应用事件广播器接口，负责管理应用事件监听器并将事件分发给它们。
 * 
 * <p>这是观察者设计模式的核心组件，用于实现事件的发布-订阅机制。
 * ApplicationContext通过委托给ApplicationEventMulticaster的实现类
 * 来处理事件的分发。
 * 
 * <p>此接口的实现类负责以下职责：
 * <ul>
 * <li>维护一个监听器注册表</li>
 * <li>根据事件类型筛选合适的监听器</li>
 * <li>将事件分发给匹配的监听器</li>
 * </ul>
 *
 * @author liuyunxuan
 * @see ApplicationEvent
 * @see ApplicationListener
 * @see AbstractApplicationEventMulticaster
 * @see SimpleApplicationEventMulticaster
 * @since 1.0
 */
public interface ApplicationEventMulticaster {

    /**
     * 添加一个应用监听器。
     * <p>所有支持的事件将被分发到此监听器。
     *
     * @param listener 要添加的监听器，不能为null
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * 移除一个应用监听器。
     *
     * @param listener 要移除的监听器，不能为null
     */
    void removeApplicationListener(ApplicationListener<?> listener);

    /**
     * 将给定的应用事件广播到适当的监听器。
     * <p>根据事件类型，只有支持该事件的监听器才会收到通知。
     *
     * @param event 要广播的事件，不能为null
     */
    void multicastEvent(ApplicationEvent event);

}
