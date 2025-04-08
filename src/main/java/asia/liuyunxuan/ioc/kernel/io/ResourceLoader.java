package asia.liuyunxuan.ioc.kernel.io;

/**
 * 资源加载器接口，负责加载各种类型的资源。
 * 
 * <p>该接口定义了统一的资源加载策略，允许客户端代码以一致的方式
 * 获取不同类型的资源，而无需关心资源的具体位置和加载方式。
 * 
 * <p>实现类应当能够处理不同前缀的资源路径，如类路径资源（classpath:）、
 * 文件系统资源和URL资源等。
 *
 * @author liuyunxuan
 * @since 1.0
 * @see Resource
 * @see DefaultResourceLoader
 */
public interface ResourceLoader {

    /**
     * 类路径资源URL前缀：classpath:
     */
    String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * 根据给定的资源位置获取相应的资源。
     * 
     * <p>资源位置可以是带有特定前缀的路径，如类路径资源（classpath:）、
     * 标准URL或文件系统路径。实现类应当能够根据路径的格式选择适当的资源类型。
     *
     * @param location 资源位置，可以是绝对路径或相对路径
     * @return 对应的资源对象
     * @see Resource
     */
    Resource getResource(String location);

}
