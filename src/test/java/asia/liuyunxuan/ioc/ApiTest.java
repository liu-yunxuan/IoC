package asia.liuyunxuan.ioc;

import asia.liuyunxuan.ioc.bean.StudentService;
import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;
import asia.liuyunxuan.ioc.beans.factory.support.DefaultListableBeanFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ApiTest {
    @Test
    public void test_BeanFactory() {
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 3. 注入bean
        BeanDefinition beanDefinition = new BeanDefinition(StudentService.class);
        beanFactory.registerBeanDefinition("studentService", beanDefinition);

        // 4.获取bean
        StudentService studentService = (StudentService) beanFactory.getBean("studentService", "test");
        studentService.queryStudent();
    }

    @Test
    public void test_cglib() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(StudentService.class);
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
        Object obj = enhancer.create(new Class[]{String.class}, new Object[]{"test"});
        System.out.println(obj);
    }

    @Test
    public void test_newInstance() throws IllegalAccessException, InstantiationException {
        StudentService studentService = StudentService.class.newInstance();
        System.out.println(studentService);
    }

    @Test
    public void test_constructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<StudentService> studentServiceClass = StudentService.class;
        Constructor<StudentService> declaredConstructor = studentServiceClass.getDeclaredConstructor(String.class);
        StudentService studentService = declaredConstructor.newInstance("test");
        System.out.println(studentService);
    }

    @Test
    public void test_parameterTypes() throws Exception {
        Class<StudentService> beanClass = StudentService.class;
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        
        Constructor<?> constructor = declaredConstructors[0];
        Constructor<StudentService> declaredConstructor = beanClass.getDeclaredConstructor(constructor.getParameterTypes());
        StudentService studentService = declaredConstructor.newInstance("test");
        System.out.println(studentService);
    }
}
