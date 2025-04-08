package asia.liuyunxuan.ioc.aop;

/**
 * 切点接口，是AOP中的核心抽象，用于定义在何处应用通知。
 * <p>
 * 切点是AOP中的关键概念，它定义了通知应该在哪些连接点上执行。一个完整的切点定义包含：
 * <ul>
 *     <li>类型匹配：通过{@link ClassFilter}实现</li>
 *     <li>方法匹配：通过{@link MethodMatcher}实现</li>
 *     <li>运行时匹配：支持动态切点判断</li>
 * </ul>
 * 
 * 切点与通知（Advice）和增强器（Advisor）紧密协作：
 * <ul>
 *     <li>切点决定通知的织入位置</li>
 *     <li>通知定义了具体地增强逻辑</li>
 *     <li>增强器将切点和通知组合在一起</li>
 * </ul>
 *
 * @author liuyunxuan
 * @see ClassFilter
 * @see MethodMatcher
 * @see PointcutAdvisor
 * @since 1.0
 */
public interface Pointcut {

    /**
     * 获取类型匹配器
     *
     * @return 返回用于类型匹配的ClassFilter实例
     */
    ClassFilter getClassFilter();

    /**
     * 获取方法匹配器
     *
     * @return 返回用于方法匹配的MethodMatcher实例
     */
    MethodMatcher getMethodMatcher();
}
