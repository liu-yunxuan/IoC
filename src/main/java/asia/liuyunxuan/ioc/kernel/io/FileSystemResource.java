package asia.liuyunxuan.ioc.kernel.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * 文件系统资源实现，用于访问文件系统中的资源。
 * 
 * <p>该实现基于java.io.File和java.nio.file.Files API，支持访问本地文件系统中的文件。
 * 资源路径可以是绝对路径或相对路径，相对路径将相对于当前工作目录解析。
 * 
 * <p>此实现使用NIO的Files API来获取输入流，提供了更好的性能和异常处理。
 *
 * @author liuyunxuan
 * @since 1.0
 * @see Resource
 * @see java.io.File
 * @see java.nio.file.Files
 */
public class FileSystemResource implements Resource {

    /**
     * 文件对象
     */
    private final File file;

    /**
     * 文件路径
     */
    private final String path;

    /**
     * 通过File对象创建文件系统资源。
     *
     * @param file 文件对象，不能为null
     * @throws IllegalArgumentException 如果文件对象为null
     */
    public FileSystemResource(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        this.file = file;
        this.path = file.getPath();
    }

    /**
     * 通过文件路径创建文件系统资源。
     *
     * @param path 文件路径，不能为null
     * @throws IllegalArgumentException 如果路径为null
     */
    public FileSystemResource(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null");
        }
        this.file = new File(path);
        this.path = path;
    }

    /**
     * 获取文件的输入流。
     *
     * @return 文件的输入流
     * @throws IOException 如果文件不存在或无法打开
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(this.file.toPath());
    }

    /**
     * 获取文件路径。
     *
     * @return 文件的路径
     */
    public final String getPath() {
        return this.path;
    }
    
    /**
     * 获取文件对象。
     *
     * @return 文件对象
     */
    public final File getFile() {
        return this.file;
    }
    
    /**
     * 检查文件是否存在。
     *
     * @return 如果文件存在返回true，否则返回false
     */
    public boolean exists() {
        return this.file.exists();
    }
    
    /**
     * 返回此资源的字符串表示形式。
     *
     * @return 资源的文件路径字符串
     */
    @Override
    public String toString() {
        return "File [" + this.path + "]";
    }

}
