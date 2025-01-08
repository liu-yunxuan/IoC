package asia.liuyunxuan.ioc;

import asia.liuyunxuan.ioc.bean.StudentService;
import org.junit.Test;

public class ApiTest {
    @Test
    public void test_BeanFactory() {
        // 1.初始化 BeanFactory
        BeanFactory beanFactory = new BeanFactory();

        // 2.注册 bean
        BeanDefinition beanDefinition = new BeanDefinition(new StudentService());
        beanFactory.registerBeanDefinition("studentService", beanDefinition);

        // 3.获取 bean
        StudentService userService = (StudentService) beanFactory.getBean("studentService");
        userService.queryStudent();
    }
}
