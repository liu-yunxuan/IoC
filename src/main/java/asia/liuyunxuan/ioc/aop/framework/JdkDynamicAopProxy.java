package asia.liuyunxuan.ioc.aop.framework;

import asia.liuyunxuan.ioc.aop.AdvisedSupport;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 基于JDK动态代理的AOP实现。
 * <p>
 * 该实现通过实现{@link InvocationHandler}接口，使用JDK的{@link Proxy}类
 * 创建基于接口的代理对象。要求目标类必须实现至少一个接口。
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    private AdvisedSupport advised;

    /**
     * 使用给定的通知支持创建JDK动态代理。
     *
     * @param advised 包含目标对象和通知的配置信息
     */
    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    /**
     * 默认构造函数。
     */
    public JdkDynamicAopProxy() {
    }

    /**
     * 设置代理的通知支持。
     *
     * @param advised 包含目标对象和通知的配置信息
     */
    public void setAdvised(AdvisedSupport advised) {
        this.advised = advised;
    }

    /**
     * 创建JDK动态代理实例。
     * <p>
     * 使用{@link Proxy#newProxyInstance}创建代理对象，将当前实例作为方法调用处理器。
     *
     * @return 代理对象实例
     */
    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), advised.getTargetSource().getTargetClass(), this);
    }

    /**
     * 处理代理对象的方法调用。
     * <p>
     * 如果方法匹配切点表达式，则应用通知；否则直接调用目标方法。
     *
     * @param proxy 代理对象
     * @param method 被调用的方法
     * @param args 方法参数
     * @return 方法调用结果
     * @throws Throwable 如果方法调用过程中发生异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
            MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(), method, args));
        }
        return method.invoke(advised.getTargetSource().getTarget(), args);
    }
}
