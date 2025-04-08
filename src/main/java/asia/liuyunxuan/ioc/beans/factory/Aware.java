package asia.liuyunxuan.ioc.beans.factory;

/**
 * 标记型接口，用于表示bean需要被容器通知特定的框架对象或信息。
 * <p>
 * 实现此接口的bean在初始化时会收到一个回调，允许它们获得框架对象的引用。
 * 例如，{@link BeanFactoryAware}允许bean获得其BeanFactory的引用，
 * {@link BeanClassLoaderAware}允许bean获得其ClassLoader的引用。
 * <p>
 * 这种方式实现了Spring风格的回调注入，使bean能够与IoC容器进行交互。
 *
 * @author Liu YunXuan
 * @see BeanFactoryAware
 * @see BeanClassLoaderAware
 * @see BeanNameAware
 */
public interface Aware {
}
                             