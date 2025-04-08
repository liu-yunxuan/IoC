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


/**
 * XML配置文件解析器，负责读取XML配置文件并将其转换为BeanDefinition对象。
 * 该类是Spring风格IoC容器的核心组件之一，用于解析XML配置文件中的Bean定义。
 *
 * <p>支持以下XML配置特性：
 * <ul>
 *     <li>标准的Spring XML Bean定义格式</li>
 *     <li>Bean的属性注入（值注入和引用注入）</li>
 *     <li>组件自动扫描（component-scan）</li>
 *     <li>生命周期方法配置（init-method, destroy-method）</li>
 *     <li>作用域配置（scope）</li>
 * </ul>
 *
 * <p>示例配置：
 * XML Bean 定义的示例：
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;beans&gt;
 *     &lt;bean id="userService" class="com.example.UserService"&gt;
 *         &lt;property name="userDao" ref="userDao"/&gt;
 *         &lt;property name="maxUsers" value="100"/&gt;
 *     &lt;/bean&gt;
 *     &lt;component-scan base-package="com.example"/&gt;
 * &lt;/beans&gt;
 * </pre>
 *
 * @see BeanDefinitionRegistry
 * @see ResourceLoader
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    /**
     * 从指定的Resource加载Bean定义。
     *
     * @param resource 包含Bean定义的资源
     * @throws BeansException 当解析XML文档出现IO异常时
     */
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

    /**
     * 从多个Resource中加载Bean定义。
     *
     * @param resources 包含Bean定义的资源数组
     * @throws BeansException 当解析XML文档出现异常时
     */
    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    /**
     * 从指定位置加载Bean定义。位置字符串可以是类路径、文件系统路径或URL。
     *
     * @param location Bean定义资源的位置
     * @throws BeansException 当解析XML文档出现异常时
     */
    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    /**
     * 从多个位置加载Bean定义。
     *
     * @param locations Bean定义资源的位置数组
     * @throws BeansException 当解析XML文档出现异常时
     */
    @Override
    public void loadBeanDefinitions(String... locations) throws BeansException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    /**
     * 执行XML配置文件的解析，将XML配置转换为BeanDefinition。
     * 支持解析bean标签、property标签和component-scan标签。
     *
     * @param inputStream XML配置文件的输入流
     * @throws Exception 解析过程中可能发生的异常
     */
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

    /**
     * 获取XML元素的属性值
     * @param reader XML流读取器
     * @param attrName 属性名
     * @return 属性值，如果属性不存在则返回null
     */
    private String getAttribute(XMLStreamReader reader, String attrName) {
        return reader.getAttributeValue(null, attrName);
    }

    /**
     * 生成Bean的名称
     * 按照优先级：id > name > 类名（首字母小写）
     *
     * @param id Bean的id属性值
     * @param name Bean的name属性值
     * @param className Bean的类名
     * @return 生成的Bean名称
     * @throws BeansException 如果无法生成有效的Bean名称
     */
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

    /**
     * 扫描指定包路径下的组件
     * @param scanPath 要扫描的包路径，支持逗号分隔的多个路径
     */
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
