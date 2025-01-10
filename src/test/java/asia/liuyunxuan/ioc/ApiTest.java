package asia.liuyunxuan.ioc;

import asia.liuyunxuan.ioc.bean.StudentService;
import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;
import asia.liuyunxuan.ioc.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

public class ApiTest {
    @Test
    public void test_BeanFactory() {
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2.注册 bean
        BeanDefinition beanDefinition = new BeanDefinition(StudentService.class);
        beanFactory.registerBeanDefinition("studentService", beanDefinition);

        // 3.第一次获取 bean
        StudentService userService = (StudentService) beanFactory.getBean("studentService");
        userService.queryStudent();

        // 4.第二次获取 bean from Singleton
        StudentService userService_singleton = (StudentService) beanFactory.getBean("studentService");
        userService_singleton.queryStudent();
    }
}
