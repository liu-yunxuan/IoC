package asia.liuyunxuan.ioc.aspect;

import org.aopalliance.aop.Advice;

/**
 * 顾问接口，AOP中的核心接口之一，定义了获取通知的规范。
 * <p>
 * 每个Advisor代表一个切面，它持有一个Advice（通知）实例，用于在特定连接点
 * 实现横切关注点的增强逻辑。Advisor在AOP框架中起到以下作用：
 * <ul>
 *     <li>将通知（Advice）与切入点（JoinPointSelector）组合</li>
 *     <li>定义切面的基本单元</li>
 *     <li>提供通知和切入点的访问方法</li>
 * </ul>
 *
 * @author liuyunxuan
 * @see org.aopalliance.aop.Advice
 * @see PointcutAspectAdvisor
 * @since 1.0
 */
public interface AspectAdvisor {

    /**
     * 获取该顾问持有的通知
     *
     * @return 返回该顾问对应的Advice实例
     */
    Advice getAdvice();

}
