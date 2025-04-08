package asia.liuyunxuan.ioc.component.container;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.PropertyValue;
import asia.liuyunxuan.ioc.component.PropertyValues;
import asia.liuyunxuan.ioc.component.container.config.ComponentDefinition;
import asia.liuyunxuan.ioc.component.container.config.ComponentProviderPostProcessor;
import asia.liuyunxuan.ioc.kernel.io.DefaultResourceLoader;
import asia.liuyunxuan.ioc.kernel.io.Resource;
import asia.liuyunxuan.ioc.common.StringValueResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * 属性占位符配置器，用于处理bean定义中的占位符。
 * <p>
 * 这个类实现了BeanFactoryPostProcessor接口，在bean实例化之前，
 * 负责处理bean定义中的占位符。它的主要功能包括：
 * <ul>
 *     <li>加载属性文件</li>
 *     <li>解析bean定义中的占位符</li>
 *     <li>用属性文件中的实际值替换占位符</li>
 * </ul>
 * <p>
 * 使用示例：
 * <pre>
 * ${database.url}
 * ${mail.host}
 * </pre>
 * 
 * @see ComponentProviderPostProcessor
 * @see ConfigurableRegistry
 */
public class PropertyPlaceholderConfigurer implements ComponentProviderPostProcessor {


    /**
     * 默认的占位符前缀
     */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /**
     * 默认的占位符后缀
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    /**
     * 属性文件的位置
     */
    private String location;

    /**
     * 处理bean工厂中的bean定义，替换其属性值中的占位符
     *
     * @param beanFactory 要处理的bean工厂
     * @throws ComponentException 如果处理过程中发生错误
     */
    @Override
    public void postProcessBeanFactory(ConfigurableRegistry beanFactory) throws ComponentException {
        try {
            // 加载属性文件
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);

            // 占位符替换属性值
            Properties properties = new Properties();
            properties.load(resource.getInputStream());

            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanName : beanDefinitionNames) {
                ComponentDefinition componentDefinition = beanFactory.getBeanDefinition(beanName);

                PropertyValues propertyValues = componentDefinition.getPropertyValues();
                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    Object value = propertyValue.getValue();
                    if (!(value instanceof String)) continue;
                    value = resolvePlaceholder((String) value, properties);
                    propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), value));
                }
            }

            // 向容器中添加字符串解析器，供解析@Value注解使用
            StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(properties);
            beanFactory.addEmbeddedValueResolver(valueResolver);

        } catch (IOException e) {
            throw new ComponentException("Could not load properties", e);
        }
    }

    /**
     * 解析字符串中的占位符
     *
     * @param value 包含占位符的字符串
     * @param properties 包含实际值的属性集
     * @return 解析后的字符串
     */
    private String resolvePlaceholder(String value, Properties properties) {
        StringBuilder buffer = new StringBuilder(value);
        int startIdx = value.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        int stopIdx = value.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
        if (startIdx != -1 && stopIdx != -1 && startIdx < stopIdx) {
            String propKey = value.substring(startIdx + 2, stopIdx);
            String propVal = properties.getProperty(propKey);
            buffer.replace(startIdx, stopIdx + 1, propVal);
        }
        return buffer.toString();
    }

    /**
     * 设置属性文件的位置
     *
     * @param location 属性文件的资源位置
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 用于解析字符串值中占位符的解析器实现
     */
    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private final Properties properties;

        public PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String resolveStringValue(String strVal) {
            return PropertyPlaceholderConfigurer.this.resolvePlaceholder(strVal, properties);
        }

    }

}
