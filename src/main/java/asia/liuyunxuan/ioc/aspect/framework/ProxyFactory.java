package asia.liuyunxuan.ioc.aspect.framework;

import asia.liuyunxuan.ioc.aspect.AdvisedSupport;
import asia.liuyunxuan.ioc.extension.ExtensionLoader;

import java.io.IOException;

/**
 * AOP代理工厂，负责创建和管理AOP代理实例。
 * <p>
 * 该工厂类支持两种代理方式：
 * <ul>
 *     <li>JDK动态代理 - 基于接口的代理实现</li>
 *     <li>CGLIB代理 - 基于类的代理实现</li>
 * </ul>
 * 代理类型的选择基于目标对象的配置（是否强制使用CGLIB）以及可选的显式代理名称指定。
 */
public class ProxyFactory {

    private final AdvisedSupport advisedSupport;

    public ProxyFactory(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }
    static {
        try {
            ExtensionLoader.getInstance().loadExtension(AopProxy.class);
        } catch (IOException | ClassNotFoundException e) {
            
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取代理对象，使用默认的代理策略。
     * <p>
     * 默认策略下，如果目标类配置了强制使用CGLIB（proxyTargetClass为true），
     * 则使用CGLIB代理；否则使用JDK动态代理。
     *
     * @return 代理对象实例
     */
    public Object getProxy() {
        return getProxy(null);
    }

    /**
     * 获取指定代理策略的代理对象。
     *
     * @param proxyName 代理策略名称，可以是"jdk"或"cglib"。如果为null，则使用默认策略
     * @return 代理对象实例
     */
    public Object getProxy(String proxyName) {
        AopProxy aopProxy = createAopProxy(proxyName);
        return aopProxy.getProxy();
    }
    
    /**
     * 创建AOP代理对象。
     *
     * @param proxyName 代理策略名称，可以是"jdk"或"cglib"。如果为null，则根据配置决定
     * @return AOP代理实例
     */
    private AopProxy createAopProxy(String proxyName) {
        AopProxy proxy;
        
        if (proxyName != null && !proxyName.isEmpty()) {
            proxy = ExtensionLoader.getInstance().get(proxyName);
        } else if (advisedSupport.isProxyTargetClass()) {
            // 默认代理策略
            proxy = ExtensionLoader.getInstance().get("cglib");
        } else {
            proxy = ExtensionLoader.getInstance().get("jdk");
        }
        
        // 使用接口方法设置AdvisedSupport，不需要类型判断和强制转换
        proxy.setAdvised(this.advisedSupport);
        return proxy;
    }
}
