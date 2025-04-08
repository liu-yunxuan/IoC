package asia.liuyunxuan.ioc.component.container.annotation;

import java.lang.annotation.*;


/**
 * 限定符注解，用于在自动装配时指定具体的目标Bean。
 * <p>
 * 当一个类型存在多个Bean实例时，可以使用@ComponentScope注解来指定具体要注入哪个Bean。
 * 这个注解通常与{@link AutoInject}注解配合使用，用于消除自动装配的歧义性。
 * <p>
 * 该注解可以应用在以下位置：
 * <ul>
 *     <li>字段 - 指定要注入的具体Bean</li>
 *     <li>方法 - 指定方法参数注入的具体Bean</li>
 *     <li>参数 - 在构造函数或方法参数上指定具体Bean</li>
 *     <li>类型 - 在类型级别指定限定符</li>
 *     <li>注解 - 用于创建自定义限定符注解</li>
 * </ul>
 * <p>
 * 使用示例：
 * <pre>
 * &#064;AutoInject
 * &#064;ComponentScope("mainDataSource")
 * private DataSource dataSource;
 * 
 * &#064;AutoInject
 * public void setMessageService(@ComponentScope("emailService") MessageService service) {
 *     this.messageService = service;
 * }
 * </pre>
 *
 * @see AutoInject
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ComponentScope {

    String value() default "";

}
