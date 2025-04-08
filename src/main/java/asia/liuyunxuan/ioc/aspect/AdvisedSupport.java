package asia.liuyunxuan.ioc.aspect;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * AOP代理配置的核心支持类，持有目标对象、拦截器和方法匹配器等AOP代理所需的全部配置信息。
 * <p>
 * 该类在AOP中扮演着重要角色，为代理工厂提供创建代理对象所需的全部信息。主要职责包括：
 * <ul>
 *     <li>管理代理方式的选择（JDK动态代理或CGLIB代理）</li>
 *     <li>持有和管理目标对象（{@link TargetSource}）</li>
 *     <li>配置方法拦截器（{@link MethodInterceptor}）</li>
 *     <li>设置方法匹配器（{@link MethodMatcher}）用于切点判断</li>
 * </ul>
 * 
 * @author liuyunxuan
 * @see TargetSource
 * @see MethodInterceptor
 * @see MethodMatcher
 * @since 1.0
 */
public class AdvisedSupport {
    /** 
     * 标记是否使用CGLIB代理，true表示使用CGLIB代理，false表示使用JDK动态代理
     */
    private boolean proxyTargetClass = false;

    /** 
     * 被代理的目标对象，封装了目标对象及其类型信息
     */
    private TargetSource targetSource;

    /** 
     * 方法拦截器，用于在目标方法执行前后实施增强
     */
    private MethodInterceptor methodInterceptor;

    /** 
     * 方法匹配器，用于判断目标方法是否符合切点表达式定义的匹配条件
     */
    private MethodMatcher methodMatcher;

    /**
     * 获取是否使用CGLIB代理的标记
     *
     * @return 如果返回true表示使用CGLIB代理，false表示使用JDK动态代理
     */
    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    /**
     * 设置是否使用CGLIB代理
     *
     * @param proxyTargetClass true表示使用CGLIB代理，false表示使用JDK动态代理
     */
    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    /**
     * 获取被代理的目标对象源
     *
     * @return 返回封装了目标对象的TargetSource实例
     */
    public TargetSource getTargetSource() {
        return targetSource;
    }

    /**
     * 设置被代理的目标对象源
     *
     * @param targetSource 封装了目标对象的TargetSource实例
     */
    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    /**
     * 获取方法拦截器
     *
     * @return 返回用于方法拦截的MethodInterceptor实例
     */
    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    /**
     * 设置方法拦截器
     *
     * @param methodInterceptor 用于方法拦截的MethodInterceptor实例
     */
    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    /**
     * 获取方法匹配器
     *
     * @return 返回用于匹配方法的MethodMatcher实例
     */
    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    /**
     * 设置方法匹配器
     *
     * @param methodMatcher 用于匹配方法的MethodMatcher实例
     */
    public void setMethodMatcher(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }
}
