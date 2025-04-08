package asia.liuyunxuan.ioc.beans.factory;

/**
 * 工厂bean接口，用于生产其他bean实例的特殊bean。
 * <p>
 * FactoryBean是一个工厂bean，它可以生产其他bean实例。这种模式的主要用途是：
 * <ul>
 *     <li>封装复杂的实例化逻辑</li>
 *     <li>支持单例或原型模式的bean创建</li>
 *     <li>提供一种编程方式创建bean的机制</li>
 * </ul>
 * <p>
 * 当在容器中查找FactoryBean时，返回的是FactoryBean创建的对象实例，
 * 而不是FactoryBean本身，除非在bean名称前加上' &amp;'前缀。
 *
 * @param <T> 这个工厂创建的bean类型
 */
public interface FactoryBean<T> {

    /**
     * 返回此工厂管理的对象实例
     *
     * @return 对象实例
     * @throws Exception 如果对象创建失败
     */
    T getObject() throws Exception;

    /**
     * 返回此工厂创建的对象类型
     *
     * @return 对象类型
     */
    Class<?> getObjectType();

    /**
     * 返回由此工厂创建的对象是否是单例
     *
     * @return 如果是单例返回true，否则返回false
     */
    boolean isSingleton();

}
