package asia.liuyunxuan.ioc.dependence;

import asia.liuyunxuan.ioc.component.container.FactoryComponent;

import java.lang.reflect.Proxy;

/**
 * 代理类
 */
public class HusbandMother implements FactoryComponent<IMother> {

    @Override
    public IMother getObject() {
        return (IMother) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IMother.class}, (proxy, method, args) -> "婚后媳妇妈妈的职责被婆婆代理了！" + method.getName());
    }

    @Override
    public Class<?> getObjectType() {
        return IMother.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
