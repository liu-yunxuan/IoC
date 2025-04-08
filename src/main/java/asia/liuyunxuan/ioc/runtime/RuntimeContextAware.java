package asia.liuyunxuan.ioc.runtime;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.container.Aware;

/**
 * 实现此接口的Bean可以获得对创建它们的ApplicationContext的引用。
 * 
 * <p>该接口是Aware接口家族的一部分，允许Bean与容器进行交互。
 * 实现此接口的Bean将在Bean属性设置之后、初始化回调之前，
 * 被通知所属的ApplicationContext。
 * 
 * <p>ApplicationContextAware的典型用例包括：
 * <ul>
 * <li>以编程方式获取其他Bean</li>
 * <li>发布应用事件</li>
 * <li>解析消息</li>
 * <li>访问资源</li>
 * </ul>
 *
 * @author liuyunxuan
 * @see Aware
 * @see RuntimeContext
 * @since 1.0
 */
public interface RuntimeContextAware extends Aware {

    /**
     * 设置此Bean运行的ApplicationContext。
     * <p>此方法将在Bean的属性填充之后、初始化回调（如InitializingBean的afterPropertiesSet
     * 或自定义init-method）之前被调用。
     *
     * @param runtimeContext 此Bean运行的ApplicationContext
     * @throws ComponentException 如果应用上下文感知过程中出现错误
     */
    void setApplicationContext(RuntimeContext runtimeContext) throws ComponentException;

}
    