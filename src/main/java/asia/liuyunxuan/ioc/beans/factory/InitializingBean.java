package asia.liuyunxuan.ioc.beans.factory;

/**
 * 实现此接口的bean在初始化时会收到回调。
 * <p>
 * 当一个bean实现了这个接口，在bean的所有属性被设置完成后，
 * 容器会调用afterPropertiesSet方法，给bean一个机会执行自定义的初始化逻辑。
 * 这通常用于：
 * <ul>
 *     <li>验证所有必需的属性是否已设置</li>
 *     <li>执行自定义的初始化逻辑</li>
 *     <li>建立特定的系统连接</li>
 *     <li>加载额外的资源</li>
 * </ul>
 */
public interface InitializingBean {

    /**
     * 在bean的所有属性被设置后由容器调用
     *
     * @throws Exception 如果初始化过程中发生错误
     */
    void afterPropertiesSet() throws Exception;

}
