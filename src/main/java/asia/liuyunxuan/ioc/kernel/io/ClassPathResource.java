package asia.liuyunxuan.ioc.kernel.io;

import asia.liuyunxuan.ioc.common.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 类路径资源实现，用于访问类路径下的资源。
 * 
 * <p>该实现使用ClassLoader加载类路径上的资源，支持从当前线程上下文类加载器、
 * 指定的类加载器或系统类加载器中获取资源。
 * 
 * <p>类路径资源的路径应该是相对于类路径根目录的相对路径，不需要以斜杠开头。
 * 例如，"asia/liuyunxuan/ioc/component.xml"表示类路径下的该文件。
 *
 * @author liuyunxuan
 * @since 1.0
 * @see Resource
 * @see ClassLoader#getResourceAsStream(String)
 */
public class ClassPathResource implements Resource {

    /**
     * 资源在类路径中的路径
     */
    private final String path;

    /**
     * 用于加载资源的类加载器
     */
    private final ClassLoader classLoader;

    /**
     * 使用默认类加载器创建类路径资源。
     *
     * @param path 类路径中的资源路径
     */
    public ClassPathResource(String path) {
        this(path, null);
    }

    /**
     * 使用指定的类加载器创建类路径资源。
     *
     * @param path 类路径中的资源路径
     * @param classLoader 用于加载资源的类加载器，如果为null则使用默认类加载器
     */
    public ClassPathResource(String path, ClassLoader classLoader) {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null");
        }
        this.path = path;
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }

    /**
     * 获取类路径资源的输入流。
     *
     * @return 资源的输入流
     * @throws IOException 如果资源不存在或无法打开
     */
    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = classLoader.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException(
                    this.path + " cannot be opened because it does not exist");
        }
        return is;
    }
    
    /**
     * 获取资源的类路径。
     *
     * @return 资源的类路径
     */
    public String getPath() {
        return this.path;
    }
    
    /**
     * 获取用于加载资源的类加载器。
     *
     * @return 类加载器
     */
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }
}
