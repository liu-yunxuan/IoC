package asia.liuyunxuan.ioc.aop.aspectj;

import asia.liuyunxuan.ioc.aop.ClassFilter;
import asia.liuyunxuan.ioc.aop.MethodMatcher;
import asia.liuyunxuan.ioc.aop.Pointcut;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * AspectJ表达式切点解析器，用于解析和匹配AspectJ风格的切点表达式。
 * 
 * <p>该类实现了{@link Pointcut}、{@link ClassFilter}和{@link MethodMatcher}接口，
 * 提供了完整的切点解析和匹配功能。主要用于：
 * <ul>
 *     <li>解析AspectJ表达式语法</li>
 *     <li>匹配目标类和方法</li>
 *     <li>支持execution切点指示符</li>
 * </ul>
 * 
 * <p>支持的表达式语法示例：
 * <pre>
 * execution(* com.example.service.*.*(..))
 * execution(public * *(..))
 * execution(* save*(..))
 * </pre>
 * 
 * <p>表达式语法说明：
 * <ul>
 *     <li>execution：用于匹配方法执行</li>
 *     <li>第一个*：表示返回值类型</li>
 *     <li>包名：表示目标类所在的包</li>
 *     <li>第二个*：表示类名</li>
 *     <li>第三个*：表示方法名</li>
 *     <li>(..)：表示方法参数</li>
 * </ul>
 *
 * @see Pointcut
 * @see ClassFilter
 * @see MethodMatcher
 */
public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {
    /**
     * 支持的切点原语集合。
     * 当前仅支持execution原语，用于匹配方法执行点。
     */
    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
    }

    /**
     * AspectJ切点表达式对象，用于实际的切点匹配。
     */
    private final PointcutExpression pointcutExpression;

    /**
     * 创建一个AspectJ表达式切点解析器。
     *
     * @param expression AspectJ切点表达式，例如："execution(* com.example.service.*.*(..))"。
     */
    public AspectJExpressionPointcut(String expression) {
        PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, this.getClass().getClassLoader());
        pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    /**
     * 判断给定的类是否匹配切点表达式。
     *
     * @param clazz 待匹配的目标类
     * @return 如果类匹配切点表达式返回true，否则返回false
     */
    @Override
    public boolean matches(Class<?> clazz) {
        return pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    /**
     * 判断给定的方法是否匹配切点表达式。
     *
     * @param method 待匹配的方法
     * @param targetClass 方法所属的目标类
     * @return 如果方法匹配切点表达式返回true，否则返回false
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
    }

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }
}
