package asia.liuyunxuan.ioc.kernel.io;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * ResourceLoader接口的默认实现，提供了基本的资源加载策略。
 * 
 * <p>该实现能够处理以下类型的资源：
 * <ul>
 *   <li>类路径资源 - 以"classpath:"前缀开头的资源</li>
 *   <li>URL资源 - 符合URL规范的资源路径</li>
 *   <li>文件系统资源 - 默认情况下，不符合上述条件的资源被视为文件系统资源</li>
 * </ul>
 *
 * <p>资源加载的优先级为：首先检查是否为类路径资源，然后尝试作为URL加载，
 * 最后作为文件系统资源处理。
 *
 * @author liuyunxuan
 * @since 1.0
 * @see ResourceLoader
 * @see ClassPathResource
 * @see UrlResource
 * @see FileSystemResource
 */
public class DefaultResourceLoader implements ResourceLoader {

    /**
     * 根据资源位置获取相应的资源对象。
     * 
     * <p>资源加载策略：
     * <ul>
     *   <li>如果位置以"classpath:"开头，返回ClassPathResource</li>
     *   <li>如果位置是一个合法的URL，返回UrlResource</li>
     *   <li>否则，返回FileSystemResource</li>
     * </ul>
     *
     * @param location 资源位置
     * @return 对应的资源对象
     * @see ClassPathResource
     * @see UrlResource
     * @see FileSystemResource
     */
    @Override
    public Resource getResource(String location) {
        if (location == null) {
            throw new IllegalArgumentException("Location must not be null");
        }
        
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        }
        else {
            try {
                // 尝试作为URL解析
                URL url = new URL(location);
                return new UrlResource(url);
            } catch (MalformedURLException e) {
                // 不是URL，作为文件系统资源处理
                return new FileSystemResource(location);
            }
        }
    }

}
