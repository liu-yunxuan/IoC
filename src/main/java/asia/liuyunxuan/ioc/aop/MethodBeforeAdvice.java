package asia.liuyunxuan.ioc.aop;

import java.lang.reflect.Method;

/**
 * 方法前置通知接口，定义了在目标方法执行前实施增强的具体行为。
 * <p>
 * 该接口是{@link BeforeAdvice}的具体实现规范，提供了访问目标方法的完整信息，包括：
 * <ul>
 *     <li>目标方法的反射信息</li>
 *     <li>方法的参数列表</li>
 *     <li>目标对象的引用</li>
 * </ul>
 * 
 * 常见的应用场景包括：
 * <ul>
 *     <li>方法调用参数的合法性验证</li>
 *     <li>安全检查和权限验证</li>
 *     <li>业务规则的预处理</li>
 *     <li>日志记录和性能监控</li>
 * </ul>
 *
 * @author liuyunxuan
 * @see BeforeAdvice
 * @see org.aopalliance.aop.Advice
 * @since 1.0
 */
public interface MethodBeforeAdvice extends BeforeAdvice {

    /**
     * 在目标方法执行之前调用的通知方法。
     * <p>
     * 该方法在目标方法调用之前被调用，可以访问目标方法的完整上下文信息，
     * 但不能改变目标方法的执行流程（除非抛出异常）。
     *
     * @param method 即将执行的目标方法，提供方法的反射信息
     * @param args   传递给目标方法的参数数组，可以读取但不应修改参数值
     * @param target 目标对象，即被代理的原始对象
     * @throws Throwable 如果通知执行过程中发生异常，异常会传播给调用者
     */
    void before(Method method, Object[] args, Object target) throws Throwable;

}
