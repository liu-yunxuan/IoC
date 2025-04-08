package asia.liuyunxuan.ioc.aop;

import asia.liuyunxuan.ioc.utils.ClassUtils;

/**
 * 目标源类，用于封装和管理AOP代理的目标对象。
 * <p>
 * 该类在AOP代理系统中扮演着重要角色：
 * <ul>
 *     <li>封装和管理实际的目标对象</li>
 *     <li>提供目标对象的类型信息</li>
 *     <li>处理目标对象的生命周期</li>
 *     <li>支持不同类型的代理（JDK动态代理和CGLIB代理）</li>
 * </ul>
 * 
 * 在AOP代理创建和方法调用过程中，都需要通过该类来访问目标对象。主要用途包括：
 * <ul>
 *     <li>代理对象的创建过程</li>
 *     <li>方法调用时获取目标对象</li>
 *     <li>获取目标对象的接口信息</li>
 * </ul>
 *
 * @author liuyunxuan
 * @see AdvisedSupport
 * @since 1.0
 */
public class TargetSource {
    /**
     * 被代理的目标对象
     */
    private final Object target;

    /**
     * 构造一个目标源实例
     *
     * @param target 需要被代理的目标对象
     */
    public TargetSource(Object target) {
        this.target = target;
    }

    /**
     * 获取目标对象实现的接口数组。
     * <p>
     * 该方法会智能处理不同类型的目标对象：
     * <ul>
     *     <li>对于普通类，返回其实现的所有接口</li>
     *     <li>对于CGLIB代理类，返回其父类实现的接口</li>
     * </ul>
     *
     * @return 目标对象实现的接口数组，如果没有实现任何接口则返回空数组
     */
    public Class<?>[] getTargetClass() {
        Class<?> clazz = this.target.getClass();
        clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;
        return clazz.getInterfaces();
    }


    /**
     * 获取被代理的目标对象。
     * <p>
     * 该方法返回实际的目标对象实例，用于：
     * <ul>
     *     <li>创建代理对象时获取目标类型信息</li>
     *     <li>方法调用时获取实际的目标对象</li>
     *     <li>在通知中访问目标对象状态</li>
     * </ul>
     *
     * @return 返回被封装的目标对象实例
     */
    public Object getTarget(){
        return this.target;
    }
}
