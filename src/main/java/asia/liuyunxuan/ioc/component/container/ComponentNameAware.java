package asia.liuyunxuan.ioc.component.container;

/**
 * 实现此接口的bean可以获知自己在容器中的名称。
 * <p>
 * 这是一个标记性接口，实现此接口的bean在被容器实例化后，
 * 会收到一个回调，传入该bean在容器中的名称。这对于
 * 一些需要知道自己在容器中标识的bean来说非常有用。
 *
 * @see Aware
 * @see ComponentFactoryAware
 */
public interface ComponentNameAware extends Aware {

    /**
     * 设置bean在容器中的名称
     *
     * @param name 容器中bean的名称
     */
    void setBeanName(String name);

}

