package asia.liuyunxuan.ioc.aspect.framework;

import asia.liuyunxuan.ioc.aspect.AdvisedSupport;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 基于CGLIB的AOP代理实现。
 * <p>
 * 该实现使用CGLIB动态生成目标类的子类作为代理，通过方法拦截实现AOP功能。
 * 相比JDK动态代理，CGLIB可以代理没有实现接口的类，但会带来一定的性能开销。
 */
public class Cglib2AopProxy implements AopProxy{
    private AdvisedSupport advised;

    /**
     * 使用给定的通知支持创建CGLIB代理。
     *
     * @param advised 包含目标对象和通知的配置信息
     */
    public Cglib2AopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    /**
     * 默认构造函数。
     */
    public Cglib2AopProxy() {
        this.advised = null;
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
     * 创建CGLIB代理实例。
     * <p>
     * 使用CGLIB的Enhancer创建目标类的子类，并设置方法拦截器。
     *
     * @return 代理对象实例
     */
    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advised.getTargetSource().getTarget().getClass());
        enhancer.setInterfaces(advised.getTargetSource().getTargetClass());
        enhancer.setCallback(new DynamicAdvisedInterceptor(advised));
        return enhancer.create();
    }

    private static class DynamicAdvisedInterceptor implements MethodInterceptor {

        private final AdvisedSupport advised;

        public DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(advised.getTargetSource().getTarget(), method, objects, methodProxy);
            if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
                return advised.getMethodInterceptor().invoke(methodInvocation);
            }
            return methodInvocation.proceed();
        }
    }

    private static class CglibMethodInvocation extends ReflectiveMethodInvocation {

        private final MethodProxy methodProxy;

        public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
            super(target, method, arguments);
            this.methodProxy = methodProxy;
        }

        @Override
        public Object proceed() throws Throwable {
            return this.methodProxy.invoke(this.target, this.arguments);
        }

    }
}
