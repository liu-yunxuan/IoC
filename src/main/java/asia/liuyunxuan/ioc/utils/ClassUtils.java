package asia.liuyunxuan.ioc.utils;

/**
 * 类操作工具类，提供类加载器获取和代理类判断等功能。
 * 
 * <p>该工具类主要用于处理类加载器相关操作，以及判断类是否为CGLIB生成的代理类。
 * 提供了获取默认类加载器和检测CGLIB代理类的方法。
 *
 * @author liuyunxuan
 * @since 1.0
 */
public class ClassUtils {

    /**
     * 获取默认的类加载器。
     * 
     * <p>按以下顺序获取类加载器：
     * <ol>
     *   <li>线程上下文类加载器</li>
     *   <li>当前类的类加载器</li>
     * </ol>
     *
     * @return 默认的类加载器，如果无法获取则可能返回null
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }

    /**
     * 判断给定的类是否为CGLIB生成的代理类。
     *
     * @param clazz 要检查的类
     * @return 如果是CGLIB代理类返回true，否则返回false
     */
    public static boolean isCglibProxyClass(Class<?> clazz) {
        return (clazz != null && isCglibProxyClassName(clazz.getName()));
    }

    /**
     * 判断给定的类名是否为CGLIB代理类名。
     * 
     * <p>CGLIB代理类的类名中包含"$$"字符串。
     *
     * @param className 要检查的类名
     * @return 如果是CGLIB代理类名返回true，否则返回false
     */
    public static boolean isCglibProxyClassName(String className) {
        return (className != null && className.contains("$$"));
    }
}
