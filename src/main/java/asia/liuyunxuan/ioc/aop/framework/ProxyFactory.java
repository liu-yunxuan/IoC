package asia.liuyunxuan.ioc.aop.framework;

import asia.liuyunxuan.ioc.aop.AdvisedSupport;
import asia.liuyunxuan.ioc.spi.ExtensionLoader;

import java.io.IOException;

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

    public Object getProxy() {
        return getProxy(null);
    }

    public Object getProxy(String proxyName) {
        AopProxy aopProxy = createAopProxy(proxyName);
        return aopProxy.getProxy();
    }
    
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
