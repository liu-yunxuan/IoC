package asia.liuyunxuan.ioc.beans.factory;


import asia.liuyunxuan.ioc.beans.BeansException;

/**
 * 对象工厂接口，用于延迟加载对象实例。
 * <p>
 * 这个接口定义了一个简单的工厂模式，主要用于：
 * <ul>
 *     <li>延迟实例化对象</li>
 *     <li>解决循环依赖问题</li>
 *     <li>提供对象创建的抽象层</li>
 * </ul>
 * <p>
 * 实现这个接口的类负责在getObject()方法被调用时创建对象实例。
 *
 * @param <T> 工厂创建的对象类型
 */
public interface ObjectFactory<T> {

    /**
     * 返回此工厂创建的对象实例
     *
     * @return 对象实例
     * @throws BeansException 如果对象创建失败
     */
    T getObject() throws BeansException;

}
