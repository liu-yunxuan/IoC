package asia.liuyunxuan.ioc.component.container.support;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.container.ConfigurableRegistry;
import asia.liuyunxuan.ioc.component.container.config.ComponentDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IoC容器的默认实现类，提供了完整的Bean定义注册和Bean实例获取功能。
 * 
 * <p>该类是Spring风格IoC容器的核心实现，它整合了以下功能：
 * <ul>
 *     <li>Bean定义的注册和管理</li>
 *     <li>Bean实例的创建和获取</li>
 *     <li>按类型查找Bean</li>
 *     <li>预实例化单例Bean</li>
 * </ul>
 * 
 * <p>作为{@link ComponentDefinitionRegistry}的实现，它提供了Bean定义的注册功能；
 * 作为{@link ConfigurableRegistry}的实现，它提供了可配置的Bean工厂功能。
 * 
 * <p>该类使用{@link ConcurrentHashMap}存储Bean定义，保证了线程安全性。
 * 它支持以下主要操作：
 * <ul>
 *     <li>通过名称和类型获取Bean</li>
 *     <li>查找指定类型的所有Bean</li>
 *     <li>检查Bean定义是否存在</li>
 *     <li>预实例化所有单例Bean</li>
 * </ul>
 *
 * @see ComponentDefinitionRegistry
 * @see ConfigurableRegistry
 * @see AbstractAutowireCapableProvider
 */
public class DefaultRegistry extends AbstractAutowireCapableProvider implements ComponentDefinitionRegistry, ConfigurableRegistry {

    /**
     * 存储Bean定义的线程安全Map
     * key为Bean的名称，value为对应的Bean定义
     */
    private final Map<String, ComponentDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    /**
     * 注册一个新的Bean定义。
     *
     * @param beanName Bean的名称
     * @param componentDefinition Bean的定义信息
     */
    @Override
    public void registerBeanDefinition(String beanName, ComponentDefinition componentDefinition) {
        beanDefinitionMap.put(beanName, componentDefinition);
    }

    /**
     * 检查是否包含指定名称的Bean定义。
     *
     * @param beanName Bean的名称
     * @return 如果包含该Bean定义则返回true
     */
    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    /**
     * 获取指定类型的所有Bean实例。
     *
     * @param type Bean的类型
     * @return 所有匹配类型的Bean实例，key为Bean的名称
     * @throws ComponentException 如果获取Bean失败
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws ComponentException {
        Map<String, T> result = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            Class beanClass = beanDefinition.getBeanClass();
            if (type.isAssignableFrom(beanClass)) {
                result.put(beanName, (T) getBean(beanName));
            }
        });
        return result;
    }

    /**
     * 获取所有已注册的Bean定义的名称。
     *
     * @return Bean定义名称的数组
     */
    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    /**
     * 获取指定名称的Bean定义。
     *
     * @param beanName Bean的名称
     * @return Bean的定义信息
     * @throws ComponentException 如果找不到对应的Bean定义
     */
    @Override
    public ComponentDefinition getBeanDefinition(String beanName) throws ComponentException {
        ComponentDefinition componentDefinition = beanDefinitionMap.get(beanName);
        if (componentDefinition == null) throw new ComponentException("No bean named '" + beanName + "' is defined");
        return componentDefinition;
    }

    /**
     * 预实例化所有单例Bean。
     * 这个方法通常在容器启动时调用，确保所有单例Bean都被正确初始化。
     *
     * @throws ComponentException 如果预实例化过程中发生错误
     */
    @Override
    public void preInstantiateSingletons() throws ComponentException {
        beanDefinitionMap.keySet().forEach(this::getBean);
    }

    /**
     * 获取指定类型的唯一Bean实例。
     * 如果找到多个匹配的Bean，则抛出异常。
     *
     * @param requiredType 需要的Bean类型
     * @return 匹配类型的Bean实例
     * @throws ComponentException 如果找不到或找到多个匹配的Bean
     */
    @SuppressWarnings("rawtypes")
    @Override
    public <T> T getBean(Class<T> requiredType) throws ComponentException {
        List<String> beanNames = new ArrayList<>();
        for (Map.Entry<String, ComponentDefinition> entry : beanDefinitionMap.entrySet()) {
            Class beanClass = entry.getValue().getBeanClass();
            if (requiredType.isAssignableFrom(beanClass)) {
                beanNames.add(entry.getKey());
            }
        }
        if (1 == beanNames.size()) {
            return getBean(beanNames.get(0), requiredType);
        }

        throw new ComponentException(requiredType + "expected single bean but found " + beanNames.size() + ": " + beanNames);
    }
}
