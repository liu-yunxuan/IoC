package asia.liuyunxuan.ioc.aspect.framework.adapter;

import asia.liuyunxuan.ioc.aspect.MethodBeforeAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 方法前置通知的拦截器实现，用于在目标方法执行前调用通知方法。
 * 实现了AOP Alliance的MethodInterceptor接口，将MethodBeforeAdvice转换为对应的拦截器。
 */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {

    /**
     * 持有的前置通知
     */
    private MethodBeforeAdvice advice;

    /**
     * 默认构造函数
     */
    public MethodBeforeAdviceInterceptor() {
    }

    /**
     * 构造函数
     * @param advice 方法前置通知
     */
    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
        this.advice = advice;
    }

    /**
     * 拦截方法调用，在目标方法执行前调用前置通知
     * @param methodInvocation 方法调用对象，封装了目标方法的调用信息
     * @return 方法调用的返回值
     * @throws Throwable 如果方法调用过程中发生异常
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        this.advice.before(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
        return methodInvocation.proceed();
    }

}
