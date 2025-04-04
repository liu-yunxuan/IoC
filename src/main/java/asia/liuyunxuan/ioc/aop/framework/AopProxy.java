package asia.liuyunxuan.ioc.aop.framework;

import asia.liuyunxuan.ioc.aop.AdvisedSupport;

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
