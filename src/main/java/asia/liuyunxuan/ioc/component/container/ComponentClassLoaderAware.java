package asia.liuyunxuan.ioc.component.container;

/**
 * 实现此接口的bean在初始化时会被注入ClassLoader的引用。
 * <p>
 * 这提供了一种方式让bean能够访问加载它的ClassLoader，从而可以：
 * <ul>
 *   <li>加载其他类</li>
 *   <li>获取类路径资源</li>
 *   <li>执行类加载相关的操作</li>
 * </ul>
 * <p>
 * 通常在需要动态加载类或访问类路径资源的场景下使用此接口。
 * 
 * @author Liu YunXuan
 * @see ClassLoader
 * @see Aware
 */
public interface ComponentClassLoaderAware extends Aware{

    void setBeanClassLoader(ClassLoader classLoader);

}


    