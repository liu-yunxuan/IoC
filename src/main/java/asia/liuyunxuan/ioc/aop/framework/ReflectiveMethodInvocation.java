package asia.liuyunxuan.ioc.aop.framework;

import org.aopalliance.intercept.MethodInvocation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * 基于反射的方法调用实现。
 * <p>
 * 该类封装了目标方法的调用信息，包括目标对象、方法和参数，
 * 并通过反射机制执行实际的方法调用。作为AOP调用链中的基础调用实现。
 */
public class ReflectiveMethodInvocation implements MethodInvocation {
    /** 目标对象 */
    protected final Object target;
    /** 要调用的方法 */
    protected final Method method;
    /** 方法调用的参数 */
    protected final Object[] arguments;

    /**
     * 创建方法调用实例。
     *
     * @param target 目标对象
     * @param method 要调用的方法
     * @param arguments 方法参数
     */
    public ReflectiveMethodInvocation(Object target, Method method, Object[] arguments) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
    }

    /**
     * 获取要调用的方法。
     *
     * @return 方法对象
     */
    @Override
    public Method getMethod() {
        return method;
    }

    /**
     * 获取方法调用的参数。
     *
     * @return 参数数组
     */
    @Override
    public Object[] getArguments() {
        return arguments;
    }

    /**
     * 执行方法调用。
     * <p>
     * 使用反射机制调用目标对象的方法。
     *
     * @return 方法调用的结果
     * @throws Throwable 如果方法调用过程中发生异常
     */
    @Override
    public Object proceed() throws Throwable {
        return method.invoke(target, arguments);
    }

    /**
     * 获取目标对象。
     *
     * @return 目标对象实例
     */
    @Override
    public Object getThis() {
        return target;
    }

    /**
     * 获取方法的静态部分。
     *
     * @return 方法对象作为可访问对象
     */
    @Override
    public AccessibleObject getStaticPart() {
        return method;
    }

}
