package asia.liuyunxuan.ioc.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 资源访问接口，抽象了对底层资源的访问方式。
 * 
 * <p>该接口是IoC容器资源加载的核心抽象，为各种类型的资源提供了统一的访问方式，
 * 包括类路径资源、文件系统资源和URL资源等。实现类负责处理特定类型资源的加载逻辑。
 * 
 * <p>通过此接口，框架可以以统一的方式访问不同来源的配置文件和其他资源，
 * 而不需要关心资源的具体位置和访问方式。
 *
 * @author liuyunxuan
 * @since 1.0
 */
public interface Resource {

    /**
     * 获取资源的输入流。
     * 
     * <p>每次调用可能会返回一个新的输入流，调用者负责关闭返回的输入流。
     * 
     * @return 资源的输入流
     * @throws IOException 如果无法打开输入流或资源不存在
     */
    InputStream getInputStream() throws IOException;

}
