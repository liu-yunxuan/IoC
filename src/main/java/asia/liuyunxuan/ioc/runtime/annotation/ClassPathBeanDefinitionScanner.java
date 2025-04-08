package asia.liuyunxuan.ioc.runtime.annotation;


import asia.liuyunxuan.ioc.annotation.Injectable;
import asia.liuyunxuan.ioc.component.container.annotation.AutoInjectAnnotationComponentPostProcessor;
import asia.liuyunxuan.ioc.component.container.config.ComponentDefinition;
import asia.liuyunxuan.ioc.component.container.support.ComponentDefinitionRegistry;

import java.util.Set;


/**
 * 类路径Bean定义扫描器，用于扫描并注册被注解标记的组件。
 * <p>
 * 该类继承自ClassPathScanningCandidateComponentProvider，在其基础扫描功能之上增加了：
 * <ul>
 *     <li>自动注册扫描到的Bean定义到容器</li>
 *     <li>处理Bean的作用域注解（@Scope）</li>
 *     <li>处理组件名称的生成和注册</li>
 *     <li>自动注册AutowiredAnnotationBeanPostProcessor用于处理依赖注入</li>
 * </ul>
 * @author liuyunxuan
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private final ComponentDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(ComponentDefinitionRegistry registry) {
        this.registry = registry;
    }

    /**
     * 执行扫描操作，扫描指定的基础包并注册发现的组件。
     *
     * @param basePackages 要扫描的基础包路径数组
     */
    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<ComponentDefinition> candidates = findCandidateComponents(basePackage);
            for (ComponentDefinition componentDefinition : candidates) {
                // 解析 Bean 的作用域 singleton、prototype
                String beanScope = resolveBeanScope(componentDefinition);
                if (beanScope != null && !beanScope.isEmpty()) {
                    componentDefinition.setScope(beanScope);
                }
                registry.registerBeanDefinition(determineBeanName(componentDefinition), componentDefinition);
            }
        }
        registry.registerBeanDefinition("asia.liuyunxuan.ioc.component.container.annotation.AutoInjectAnnotationComponentPostProcessor", new ComponentDefinition(AutoInjectAnnotationComponentPostProcessor.class));
    }

    /**
     * 解析Bean的作用域。
     *
     * @param componentDefinition 要解析的Bean定义
     * @return Bean的作用域，如果未指定则返回空字符串
     */
    private String resolveBeanScope(ComponentDefinition componentDefinition) {
        Class<?> beanClass = componentDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (null != scope) return scope.value();
        return " ";
    }

    /**
     * 确定Bean的名称。
     * <p>
     * 如果@Component注解指定了value，则使用该值作为Bean名称；
     * 否则使用类名的首字母小写形式作为Bean名称。
     * </p>
     * @param componentDefinition 要确定名称的Bean定义
     * @return 确定的Bean名称
     */
    private String determineBeanName(ComponentDefinition componentDefinition) {
        Class<?> beanClass = componentDefinition.getBeanClass();
        Injectable injectable = beanClass.getAnnotation(Injectable.class);
        String value = injectable.value();
        if (value == null || value.isEmpty()) {
            String simpleName = beanClass.getSimpleName();
            if (!simpleName.isEmpty()) {
                value = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
            } else {
                value = simpleName;
            }
        }
        return value;
    }

}
