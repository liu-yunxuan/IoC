package asia.liuyunxuan.ioc.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * URL资源实现，用于访问通过URL可达的资源。
 * 
 * <p>该实现支持访问各种URL协议的资源，如HTTP、HTTPS、FTP等。
 * 它使用Java标准的URL和URLConnection API来获取资源。
 * 
 * <p>对于HTTP连接，该实现会在发生异常时正确关闭连接，防止资源泄漏。
 *
 * @author liuyunxuan
 * @since 1.0
 * @see Resource
 * @see java.net.URL
 * @see java.net.URLConnection
 */
public class UrlResource implements Resource {

    /**
     * 资源的URL
     */
    private final URL url;

    /**
     * 通过URL对象创建URL资源。
     *
     * @param url 资源的URL，不能为null
     * @throws IllegalArgumentException 如果URL为null
     */
    public UrlResource(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("URL must not be null");
        }
        this.url = url;
    }
    
    /**
     * 通过URL字符串创建URL资源。
     *
     * @param url URL字符串，不能为null
     * @throws IllegalArgumentException 如果URL字符串为null
     * @throws MalformedURLException 如果URL格式不正确
     */
    public UrlResource(String url) throws MalformedURLException {
        if (url == null) {
            throw new IllegalArgumentException("URL must not be null");
        }
        this.url = new URL(url);
    }

    /**
     * 获取URL资源的输入流。
     * 
     * <p>对于HTTP连接，会在发生异常时自动关闭连接。
     *
     * @return 资源的输入流
     * @throws IOException 如果无法打开连接或获取输入流
     */
    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection con = this.url.openConnection();
        try {
            return con.getInputStream();
        }
        catch (IOException ex) {
            // 对于HTTP连接，确保在发生异常时关闭连接
            if (con instanceof HttpURLConnection) {
                ((HttpURLConnection) con).disconnect();
            }
            throw ex;
        }
    }
    
    /**
     * 获取资源的URL。
     *
     * @return 资源的URL
     */
    public URL getURL() {
        return this.url;
    }
    
    /**
     * 返回此资源的字符串表示形式。
     *
     * @return 资源的URL字符串
     */
    @Override
    public String toString() {
        return "URL [" + this.url + "]"; 
    }

}
