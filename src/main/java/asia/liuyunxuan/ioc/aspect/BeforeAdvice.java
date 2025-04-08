package asia.liuyunxuan.ioc.aspect;

import org.aopalliance.aop.Advice;

/**
 * 前置通知接口，是AOP中的标识接口，用于标识在目标方法执行前实施的通知。
 * <p>
 * 前置通知在AOP通知类型中具有以下特点：
 * <ul>
 *     <li>在目标方法执行之前被调用</li>
 *     <li>不能阻止方法执行流程（除非抛出异常）</li>
 *     <li>不能改变目标方法的返回值</li>
 *     <li>常用于前置验证、日志记录、权限检查等场景</li>
 * </ul>
 *
 * @author liuyunxuan
 * @see MethodBeforeAdvice
 * @see org.aopalliance.aop.Advice
 * @since 1.0
 */
public interface BeforeAdvice extends Advice {

}
