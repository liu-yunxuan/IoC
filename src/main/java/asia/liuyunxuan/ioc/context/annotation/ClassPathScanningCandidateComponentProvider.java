package asia.liuyunxuan.ioc.context.annotation;


import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;
import asia.liuyunxuan.ioc.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 用于扫描类路径并查找候选组件的基础类。
 * <p>
 * 该类负责扫描指定包路径下的所有类，并识别被@Component注解标记的类作为候选组件。
 * 它提供了基础的组件扫描功能，可以被其他扫描器扩展以提供更多特定的功能。
 * </p>
 *
 * @author liuyunxuan
 */
public class ClassPathScanningCandidateComponentProvider {

    /**
     * 在指定的基础包路径下查找所有候选组件。
     *
     * @param basePackage 要扫描的基础包路径
     * @return 包含所有候选Bean定义的Set集合
     */
    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = basePackage.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());
                if (directory.exists()) {
                    findClassesInDirectory(directory, basePackage, candidates);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return candidates;
    }

    /**
     * 递归搜索目录中的类文件，并将符合条件的类添加到候选组件集合中。
     *
     * @param directory 要搜索的目录
     * @param packageName 当前包名
     * @param candidates 用于存储候选Bean定义的集合
     */
    private void findClassesInDirectory(File directory, String packageName, Set<BeanDefinition> candidates) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    findClassesInDirectory(file, packageName + "." + file.getName(), candidates);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (clazz.isAnnotationPresent(Component.class)) {
                            candidates.add(new BeanDefinition(clazz));
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

}
