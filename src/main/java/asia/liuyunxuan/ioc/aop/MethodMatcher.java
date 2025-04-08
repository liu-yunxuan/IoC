package asia.liuyunxuan.ioc.aop;

import java.lang.reflect.Method;

/**
 * 方法匹配器接口，用于判断目标方法是否匹配切点表达式。
 * <p>
 * 在AOP中，该接口与{@link ClassFilter}配合使用，共同构成了切点的完整匹配功能。
 * MethodMatcher负责方法级别的匹配判断，具有以下特点：
 * <ul>
 *     <li>提供方法级别的细粒度控制</li>
 *     <li>可以基于方法名、参数类型、注解等条件匹配</li>
 *     <li>支持动态和静态两种匹配方式</li>
 * </ul>
 *
 * @author liuyunxuan
 * @see Pointcut
 * @see ClassFilter
 * @since 1.0
 */
public interface MethodMatcher {
    /**
     * 判断给定的方法是否匹配切点表达式。
     * <p>
     * 该方法在创建AOP代理时被调用，用于确定哪些方法需要被代理。
     * 匹配过程会考虑方法的各个特征，包括：
     * <ul>
     *     <li>方法名称和签名</li>
     *     <li>方法的声明类型</li>
     *     <li>方法的修饰符</li>
     *     <li>方法上的注解</li>
     * </ul>
     *
     * @param method      待匹配的方法，提供方法的反射信息
     * @param targetClass 方法所属的目标类，用于上下文匹配判断
     * @return 如果方法匹配返回true，否则返回false
     */
    boolean matches(Method method, Class<?> targetClass);
}
