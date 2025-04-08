package asia.liuyunxuan.ioc.component.container.annotation;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.PropertyValues;
import asia.liuyunxuan.ioc.component.container.ComponentFactoryAware;
import asia.liuyunxuan.ioc.component.container.ComponentProvider;
import asia.liuyunxuan.ioc.component.container.ConfigurableRegistry;
import asia.liuyunxuan.ioc.component.container.config.InstantiationAwareBeanPostProcessor;
import asia.liuyunxuan.ioc.common.BeanUtils;
import asia.liuyunxuan.ioc.common.ClassUtils;

import java.lang.reflect.Field;

/**
 * 处理@AutoInject、@Value和@Qualifier注解的Bean后处理器。
 * <p>
 * 该处理器实现了以下功能：
 * <ul>
 *     <li>处理@Value注解，将配置属性值注入到相应字段</li>
 *     <li>处理@Autowired注解，实现依赖的自动装配</li>
 *     <li>处理@Qualifier注解，实现限定注入的具体Bean</li>
 * </ul>
 * <p>
 * 工作流程：
 * <ol>
 *     <li>在Bean实例化后，属性填充前被调用</li>
 *     <li>扫描Bean中的所有字段，查找相关注解</li>
 *     <li>处理@Value注解：解析属性值并注入</li>
 *     <li>处理@Autowired注解：
 *         <ul>
 *             <li>若有@ComponentScope，按名称和类型注入指定Bean</li>
 *             <li>若无@ComponentScope，按类型注入匹配的Bean</li>
 *         </ul>
 *     </li>
 * </ol>
 * <p>
 * 示例：
 * <pre>
 * public class UserService {
 *     &#064;Value("${app.name}")
 *     private String appName;
 *     
 *     &#064;AutoInject
 *     &#064;ComponentScope("mainRepository")
 *     private UserRepository userRepository;
 * }
 * </pre>
 *
 * @see AutoInject
 * @see Value
 * @see ComponentScope
 * @see InstantiationAwareBeanPostProcessor
 * @see ComponentFactoryAware
 */
public class AutoInjectAnnotationComponentPostProcessor implements InstantiationAwareBeanPostProcessor, ComponentFactoryAware {

    private ConfigurableRegistry beanFactory;

    @Override
    public void setBeanFactory(ComponentProvider componentProvider) throws ComponentException {
        this.beanFactory = (ConfigurableRegistry) componentProvider;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws ComponentException {
        // 1. 处理注解 @Value
        Class<?> clazz = bean.getClass();
        clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;

        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field field : declaredFields) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (null != valueAnnotation) {
                String value = valueAnnotation.value();
                value = beanFactory.resolveEmbeddedValue(value);
                BeanUtils.setFieldValue(bean, field.getName(), value);
            }
        }

        // 2. 处理注解 @AutoInject
        for (Field field : declaredFields) {
            AutoInject autoInjectAnnotation = field.getAnnotation(AutoInject.class);
            if (null != autoInjectAnnotation) {
                Class<?> fieldType = field.getType();
                String dependentBeanName;
                ComponentScope componentScopeAnnotation = field.getAnnotation(ComponentScope.class);
                Object dependentBean;
                if (null != componentScopeAnnotation) {
                    dependentBeanName = componentScopeAnnotation.value();
                    dependentBean = beanFactory.getBean(dependentBeanName, fieldType);
                } else {
                    dependentBean = beanFactory.getBean(fieldType);
                }
                BeanUtils.setFieldValue(bean, field.getName(), dependentBean);
            }
        }

        return pvs;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws ComponentException {
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws ComponentException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws ComponentException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws ComponentException {
        return true;
    }

}
