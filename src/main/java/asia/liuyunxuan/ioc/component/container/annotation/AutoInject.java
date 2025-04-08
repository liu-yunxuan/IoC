package asia.liuyunxuan.ioc.component.container.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动装配注解，用于标记需要由IoC容器自动注入的依赖。
 * <p>
 * 该注解可以应用在以下位置：
 * <ul>
 *     <li>构造函数 - 标记一个构造函数用于依赖注入</li>
 *     <li>字段 - 直接将依赖注入到字段中</li>
 *     <li>方法 - 通常是setter方法，用于设置依赖</li>
 * </ul>
 * <p>
 * 使用示例：
 * <pre>
 * public class UserService {
 *     &#064;AutoInject
 *     private UserRepository userRepository;
 *     
 *     &#064;AutoInject
 *     public void setEmailService(EmailService emailService) {
 *         this.emailService = emailService;
 *     }
 * }
 * </pre>
 * <p>
 * 当标记了@Autowired注解后，IoC容器会在创建Bean时自动注入相应的依赖。
 * 如果存在多个候选Bean，可以配合{@link ComponentScope}注解使用来指定具体的实现。
 *
 * @see ComponentScope
 * @see Value
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD})
public @interface AutoInject {
}
