package asia.liuyunxuan.ioc.component.container;

/**
 * 实现此接口的bean在销毁前会收到回调。
 * <p>
 * 当一个bean实现了这个接口，在容器关闭时，会调用其destroy方法，
 * 使bean有机会释放它所持有的资源。这通常用于：
 * <ul>
 *     <li>关闭数据库连接</li>
 *     <li>关闭网络连接</li>
 *     <li>清理缓存</li>
 *     <li>释放其他系统资源</li>
 * </ul>
 */
public interface DisposableComponent {

    /**
     * 在bean被销毁时由容器调用
     *
     * @throws Exception 如果销毁过程中发生错误
     */
    void destroy() throws Exception;

}
