package asia.liuyunxuan.ioc.aop;

/**
 * 切点顾问接口，继承自{@link Advisor}接口，用于将切点（{@link Pointcut}）和通知（{@link org.aopalliance.aop.Advice}）组合在一起。
 * <p>
 * 这是AOP中最常用的Advisor类型，它具有以下特点：
 * <ul>
 *     <li>通过切点精确定义通知的适用范围</li>
 *     <li>通过通知定义横切逻辑的具体实现</li>
 *     <li>将切点和通知组合成一个完整的切面</li>
 *     <li>支持灵活的切面定义和复用</li>
 * </ul>
 *
 * @author liuyunxuan
 * @see Advisor
 * @see Pointcut
 * @see org.aopalliance.aop.Advice
 * @since 1.0
 */
public interface PointcutAdvisor extends Advisor {

    /**
     * 获取该顾问持有的切点
     *
     * @return 返回定义通知适用范围的Pointcut实例
     */
    Pointcut getPointcut();

}
