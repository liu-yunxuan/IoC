package asia.liuyunxuan.ioc.beans.factory.xml;

import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.PropertyValue;
import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;
import asia.liuyunxuan.ioc.beans.factory.config.BeanReference;
import asia.liuyunxuan.ioc.beans.factory.support.AbstractBeanDefinitionReader;
import asia.liuyunxuan.ioc.beans.factory.support.BeanDefinitionRegistry;
import asia.liuyunxuan.ioc.context.annotation.ClassPathBeanDefinitionScanner;
import asia.liuyunxuan.ioc.core.io.Resource;
import asia.liuyunxuan.ioc.core.io.ResourceLoader;


import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.Arrays;


public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            try (InputStream inputStream = resource.getInputStream()) {
                doLoadBeanDefinitions(inputStream);
            }
        } catch (Exception e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(String... locations) throws BeansException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    protected void doLoadBeanDefinitions(InputStream inputStream) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

        BeanDefinition currentBean = null;
        String currentBeanName = null;
        String currentClassName;

        while (reader.hasNext()) {
            int eventType = reader.next();

            // 处理开始标签
            if (eventType == XMLStreamReader.START_ELEMENT) {
                String tagName = reader.getLocalName();

                // 处理bean标签
                if ("bean".equals(tagName)) {
                    // 解析bean属性
                    String id = getAttribute(reader, "id");
                    String name = getAttribute(reader, "name");
                    String initMethod = getAttribute(reader,"init-method");
                    String destroyMethodName = getAttribute(reader,"destroy-method");
                    currentClassName = getAttribute(reader, "class");
                    String beanScope = getAttribute(reader,"scope");
                    // 生成bean名称
                    currentBeanName = generateBeanName(id, name, currentClassName);

                    // 创建Bean定义
                    Class<?> clazz = Class.forName(currentClassName);
                    currentBean = new BeanDefinition(clazz);
                    currentBean.setInitMethodName(initMethod);
                    currentBean.setDestroyMethodName(destroyMethodName);
                    if (beanScope != null && !beanScope.isEmpty()) {
                        currentBean.setScope(beanScope);
                    }
                }

                // 处理property标签
                else if ("property".equals(tagName) && currentBean != null) {
                    String propName = getAttribute(reader, "name");
                    String value = getAttribute(reader, "value");
                    String ref = getAttribute(reader, "ref");

                    Object propValue = (ref != null && !ref.isEmpty()) ?
                            new BeanReference(ref) : value;

                    currentBean.getPropertyValues().addPropertyValue(
                            new PropertyValue(propName, propValue));
                }

                // 处理component-scan标签
                else if ("component-scan".equals(tagName)) {
                    String scanPath = getAttribute(reader, "base-package");

                    // Java原生空值检查
                    if (scanPath == null || scanPath.trim().isEmpty()) {
                        throw new BeansException("The value of base-package attribute cannot be empty or null");
                    }
                    scanPackage(scanPath);
                }
            }

            // 处理结束标签
            else if (eventType == XMLStreamReader.END_ELEMENT) {
                String tagName = reader.getLocalName();

                // 完成bean定义处理
                if ("bean".equals(tagName) && currentBean != null) {
                    if (getRegistry().containsBeanDefinition(currentBeanName)) {
                        throw new BeansException("Duplicate bean definition: " + currentBeanName);
                    }
                    getRegistry().registerBeanDefinition(currentBeanName, currentBean);
                    currentBean = null;
                    currentBeanName = null;
                }
            }


        }
        reader.close();
    }

    // 原生属性获取方法
    private String getAttribute(XMLStreamReader reader, String attrName) {
        return reader.getAttributeValue(null, attrName);
    }

    // Bean名称生成逻辑
    private String generateBeanName(String id, String name, String className) {
        // 优先级：id > name > 类名
        if (id != null && !id.isEmpty()) return id;

        if (name != null && !name.isEmpty()) {
            // 处理逗号分隔的多个名称
            int commaIndex = name.indexOf(',');
            return (commaIndex != -1) ?
                    name.substring(0, commaIndex).trim() : name;
        }

        // 类名转小写开头
        if (!className.isEmpty()) {
            String simpleName = className.substring(className.lastIndexOf('.') + 1);
            return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
        }

        throw new BeansException("Invalid bean definition - missing class name");
    }

    // 修改后的scanPackage方法
    private void scanPackage(String scanPath) {
        // 使用Java原生方法分割字符串
        String[] basePackages = Arrays.stream(scanPath.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        scanner.doScan(basePackages);
    }
}
