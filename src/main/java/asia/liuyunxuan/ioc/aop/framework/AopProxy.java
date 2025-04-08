package asia.liuyunxuan.ioc.aop.framework;

import asia.liuyunxuan.ioc.aop.AdvisedSupport;

/**
 * AOP代理的核心接口，定义了代理对象的创建和配置方法。
 * <p>
 * 该接口的实现类负责生成目标对象的代理实例，通常包括：
 * <ul>
 *     <li>JDK动态代理实现 - 基于接口的代理</li>
 *     <li>CGLIB代理实现 - 基于类的代理</li>
 * </ul>
 * 代理对象将拦截对目标对象的方法调用，并在调用前后执行配置的通知（Advice）。
 */
public interface AopProxy {

    /**
     * 获取代理对象
     * @return 代理对象
     */
    Object getProxy();
    
    /**
     * 设置被代理的对象及其通知
     * @param advised 被代理的对象及其通知
     */
    void setAdvised(AdvisedSupport advised);
}
