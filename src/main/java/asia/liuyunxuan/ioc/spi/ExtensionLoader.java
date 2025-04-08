package asia.liuyunxuan.ioc.spi;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI扩展加载器，用于实现Java的服务提供者接口(Service Provider Interface)机制。
 * 
 * <p>该类负责从指定路径加载并管理SPI扩展实现，支持系统级和用户自定义的扩展点。
 * 通过此机制，框架可以在不修改核心代码的情况下实现功能扩展，遵循开闭原则。
 * 扩展实现通过配置文件进行注册，配置文件位于类路径下的META-INF目录中。
 *
 * @author liuyunxuan
 * @since 1.0
 */
public class ExtensionLoader{

    /**
     * 系统SPI扩展配置文件路径前缀
     */
    private static final String SYS_EXTENSION_LOADER_DIR_PREFIX = "META-INF/services/";

    /**
     * 用户自定义SPI扩展配置文件路径前缀
     */
    private static final String DIY_EXTENSION_LOADER_DIR_PREFIX = "META-INF/users/";

    /**
     * 所有支持的SPI配置文件路径前缀数组
     */
    private static final String[] prefix = {SYS_EXTENSION_LOADER_DIR_PREFIX, DIY_EXTENSION_LOADER_DIR_PREFIX};

    /**
     * 扩展类缓存，存储扩展名到具体实现类的映射
     * key: 扩展名称，value: 对应的实现类
     */
    @SuppressWarnings("rawtypes")
    private static final Map<String, Class> extensionClassCache = new ConcurrentHashMap<>();
    
    /**
     * 接口到其所有实现类的映射缓存
     * key: 接口全限定名，value: 该接口所有实现类的映射(扩展名->实现类)
     */
    @SuppressWarnings("rawtypes")
    private static final Map<String, Map<String,Class>> extensionClassCaches = new ConcurrentHashMap<>();

    /**
     * 单例对象缓存，存储已实例化的扩展对象
     * key: 扩展名称，value: 扩展实例
     */
    private static final Map<String, Object> singletonsObject = new ConcurrentHashMap<>();

    /**
     * ExtensionLoader单例实例
     */
    private static final ExtensionLoader extensionLoader;

    static {
        extensionLoader = new ExtensionLoader();
    }

    /**
     * 获取ExtensionLoader单例实例
     *
     * @return ExtensionLoader单例
     */
    public static ExtensionLoader getInstance(){
        return extensionLoader;
    }

    /**
     * 私有构造函数，防止外部实例化
     */
    private ExtensionLoader(){

    }

    /**
     * 根据扩展名获取对应的扩展实例
     *
     * @param name 扩展名称
     * @param <V> 扩展类型
     * @return 扩展实例
     * @throws RuntimeException 如果实例化过程中发生异常
     */
    @SuppressWarnings("unchecked")
    public <V> V get(String name) {
        if (!singletonsObject.containsKey(name)) {
            try {
                singletonsObject.put(name, extensionClassCache.get(name).newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return (V) singletonsObject.get(name);
    }

    /**
     * 获取指定接口的所有扩展实现
     *
     * @param interfaceClass 接口类
     * @param <T> 接口类型
     * @return 扩展名到扩展实例的映射
     * @throws IllegalArgumentException 如果参数为null或不是接口
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> Map<String, T> gets(Class<T> interfaceClass) {
        if (interfaceClass == null) {
            throw new IllegalArgumentException("Interface class cannot be null");
        }

        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("Class " + interfaceClass.getName() + " is not an interface");
        }

        Map<String, Class> classMap = extensionClassCaches.get(interfaceClass.getName());
        if (classMap == null) {
            return new HashMap<>();
        }

        Map<String, T> result = new HashMap<>();
        for (Map.Entry<String, Class> entry : classMap.entrySet()) {
            String key = entry.getKey();
            Class<?> clazz = entry.getValue();
            if (!singletonsObject.containsKey(key)) {
                try {
                    singletonsObject.put(key, clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    continue;
                }
            }
            result.put(key, (T) singletonsObject.get(key));
        }
        return result;
    }

    /**
     * 加载指定接口的所有扩展实现
     * 
     * <p>从配置文件中读取接口的实现类信息并缓存，配置文件格式为：扩展名=实现类全限定名
     *
     * @param clazz 接口类
     * @throws IOException 如果读取配置文件时发生IO异常
     * @throws ClassNotFoundException 如果找不到指定的实现类
     * @throws IllegalArgumentException 如果参数为null
     */
    @SuppressWarnings("rawtypes")
    public void loadExtension(Class<?> clazz) throws IOException, ClassNotFoundException {
        if (clazz == null) {
            throw new IllegalArgumentException("class 没找到");
        }
        ClassLoader classLoader = this.getClass().getClassLoader();
        Map<String, Class> classMap = new HashMap<>();
        // 从系统SPI以及用户SPI中找bean
        for (String prefix : prefix) {
            String spiFilePath = prefix + clazz.getName();
            Enumeration<URL> enumeration = classLoader.getResources(spiFilePath);
            if (!enumeration.hasMoreElements()) {
                continue;
            }
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                InputStreamReader inputStreamReader;
                inputStreamReader = new InputStreamReader(url.openStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] lineArr = line.split("=");
                    String key = lineArr[0];
                    String name = lineArr[1];
                    final Class<?> aClass = Class.forName(name);
                    extensionClassCache.put(key, aClass);
                    classMap.put(key, aClass);
                }
            }
        }
        extensionClassCaches.put(clazz.getName(),classMap);
    }

}
