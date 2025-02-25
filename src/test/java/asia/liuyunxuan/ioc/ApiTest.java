package asia.liuyunxuan.ioc;

import asia.liuyunxuan.ioc.bean.StudentService;
import asia.liuyunxuan.ioc.bean.UserDao;
import asia.liuyunxuan.ioc.bean.UserService;
import asia.liuyunxuan.ioc.beans.PropertyValue;
import asia.liuyunxuan.ioc.beans.PropertyValues;
import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;
import asia.liuyunxuan.ioc.beans.factory.config.BeanReference;
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

        // 2. UserDao 注册
        beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

        // 3. UserService 设置属性[id、userDao]
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("id", "10001"));
        propertyValues.addPropertyValue(new PropertyValue("userDao",new BeanReference("userDao")));

        // 4. UserService 注入bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // 5. UserService 获取bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
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
