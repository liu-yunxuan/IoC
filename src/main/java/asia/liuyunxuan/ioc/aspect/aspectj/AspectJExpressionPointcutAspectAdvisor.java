package asia.liuyunxuan.ioc.aspect.aspectj;


import asia.liuyunxuan.ioc.aspect.JoinPointSelector;
import asia.liuyunxuan.ioc.aspect.PointcutAspectAdvisor;
import org.aopalliance.aop.Advice;

/**
 * AspectJ表达式切面通知器，将切点表达式和通知组合在一起。
 * 
 * <p>该类实现了{@link PointcutAspectAdvisor}接口，作为切面的核心组件，负责：
 * <ul>
 *     <li>管理切点表达式：通过AspectJ表达式定义切点</li>
 *     <li>持有通知对象：封装了在切点处执行的增强逻辑</li>
 *     <li>提供切面组装：将切点和通知组合成一个完整的切面</li>
 * </ul>
 * 
 * <p>使用示例：
 * <pre>
 * AspectJExpressionPointcutAspectAdvisor advisor = new AspectJExpressionPointcutAspectAdvisor();
 * advisor.setExpression("execution(* com.example.service.*.*(..))");
 * advisor.setAdvice(new MethodBeforeAdvice(...));
 * </pre>
 *
 * <p>工作流程：
 * <ol>
 *     <li>设置AspectJ切点表达式</li>
 *     <li>配置通知对象（前置、后置、环绕等）</li>
 *     <li>在代理对象中使用该通知器判断和执行增强</li>
 * </ol>
 *
 * @see PointcutAspectAdvisor
 * @see AspectJExpressionJoinPointSelector
 * @see Advice
 */
public class AspectJExpressionPointcutAspectAdvisor implements PointcutAspectAdvisor {

    /**
     * 切点对象，用于存储和解析AspectJ表达式
     */
    private AspectJExpressionJoinPointSelector pointcut;

    /**
     * 通知对象，封装了要在切点处执行的增强逻辑
     */
    private Advice advice;

    /**
     * AspectJ切点表达式
     */
    private String expression;

    public void setExpression(String expression){
        this.expression = expression;
    }

    @Override
    public JoinPointSelector getPointcut() {
        if (null == pointcut) {
            pointcut = new AspectJExpressionJoinPointSelector(expression);
        }
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice){
        this.advice = advice;
    }

}
