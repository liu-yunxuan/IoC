package asia.liuyunxuan.ioc.beans.factory.annotation;

import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.PropertyValues;
import asia.liuyunxuan.ioc.beans.factory.BeanFactory;
import asia.liuyunxuan.ioc.beans.factory.BeanFactoryAware;
import asia.liuyunxuan.ioc.beans.factory.ConfigurableListableBeanFactory;
import asia.liuyunxuan.ioc.beans.factory.config.InstantiationAwareBeanPostProcessor;
import asia.liuyunxuan.ioc.utils.BeanUtils;
import asia.liuyunxuan.ioc.utils.ClassUtils;

import java.lang.reflect.Field;

/**
 * 处理@Autowired、@Value和@Qualifier注解的Bean后处理器。
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
 *             <li>若有@Qualifier，按名称和类型注入指定Bean</li>
 *             <li>若无@Qualifier，按类型注入匹配的Bean</li>
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
 *     &#064;Autowired
 *     &#064;Qualifier("mainRepository")
 *     private UserRepository userRepository;
 * }
 * </pre>
 *
 * @see Autowired
 * @see Value
 * @see Qualifier
 * @see InstantiationAwareBeanPostProcessor
 * @see BeanFactoryAware
 */
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
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

        // 2. 处理注解 @Autowired
        for (Field field : declaredFields) {
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            if (null != autowiredAnnotation) {
                Class<?> fieldType = field.getType();
                String dependentBeanName;
                Qualifier qualifierAnnotation = field.getAnnotation(Qualifier.class);
                Object dependentBean;
                if (null != qualifierAnnotation) {
                    dependentBeanName = qualifierAnnotation.value();
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
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

}
