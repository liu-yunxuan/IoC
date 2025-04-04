package asia.liuyunxuan.ioc.spi;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ExtensionLoader{

    // 系统SPI
    private static final String SYS_EXTENSION_LOADER_DIR_PREFIX = "META-INF/services/";

    // 用户SPI
    private static final String DIY_EXTENSION_LOADER_DIR_PREFIX = "META-INF/users/";

    private static final String[] prefixs = {SYS_EXTENSION_LOADER_DIR_PREFIX, DIY_EXTENSION_LOADER_DIR_PREFIX};

    // bean定义信息 key: 定义的key value：具体类
    private static final Map<String, Class> extensionClassCache = new ConcurrentHashMap<>();
    // bean 定义信息 key：接口 value：接口子类s
    private static final Map<String, Map<String,Class>> extensionClassCaches = new ConcurrentHashMap<>();

    // 实例化的bean
    private static final Map<String, Object> singletonsObject = new ConcurrentHashMap<>();


    private static final ExtensionLoader extensionLoader;

    static {
        extensionLoader = new ExtensionLoader();
    }

    public static ExtensionLoader getInstance(){
        return extensionLoader;
    }

    private ExtensionLoader(){

    }

    public <V> V get(String name) {
        if (!singletonsObject.containsKey(name)) {
            try {
                singletonsObject.put(name, extensionClassCache.get(name).newInstance());
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return (V) singletonsObject.get(name);
    }


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
                    e.printStackTrace();
                    continue;
                }
            }
            result.put(key, (T) singletonsObject.get(key));
        }
        return result;
    }

    public void loadExtension(Class<?> clazz) throws IOException, ClassNotFoundException {
        if (clazz == null) {
            throw new IllegalArgumentException("class 没找到");
        }
        ClassLoader classLoader = this.getClass().getClassLoader();
        Map<String, Class> classMap = new HashMap<>();
        // 从系统SPI以及用户SPI中找bean
        for (String prefix : prefixs) {
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
