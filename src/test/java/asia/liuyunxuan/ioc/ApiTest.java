package asia.liuyunxuan.ioc;

import asia.liuyunxuan.ioc.bean.StudentService;
import asia.liuyunxuan.ioc.bean.UserDao;
import asia.liuyunxuan.ioc.bean.UserService;
import asia.liuyunxuan.ioc.beans.PropertyValue;
import asia.liuyunxuan.ioc.beans.PropertyValues;
import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;
import asia.liuyunxuan.ioc.beans.factory.config.BeanReference;
import asia.liuyunxuan.ioc.beans.factory.support.DefaultListableBeanFactory;
import asia.liuyunxuan.ioc.beans.factory.xml.XmlBeanDefinitionReader;
import asia.liuyunxuan.ioc.common.MyBeanFactoryPostProcessor;
import asia.liuyunxuan.ioc.common.MyBeanPostProcessor;
import asia.liuyunxuan.ioc.context.support.ClassPathXmlApplicationContext;
import asia.liuyunxuan.ioc.core.io.DefaultResourceLoader;
import asia.liuyunxuan.ioc.core.io.Resource;
import asia.liuyunxuan.ioc.event.CustomEvent;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;

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

    private DefaultResourceLoader resourceLoader;

    @Before
    public void init() {
        resourceLoader = new DefaultResourceLoader();
    }

    @Test
    public void test_classpath() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:test.properties");
        InputStream inputStream = resource.getInputStream();
        String content = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println(content);
    }

    @Test
    public void test_file() throws IOException {
        Resource resource = resourceLoader.getResource("src/test/resources/test.properties");
        InputStream inputStream = resource.getInputStream();
        String content = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println(content);
    }

    @Test
    public void test_url() throws IOException {
        Resource resource = resourceLoader.getResource("https://liuyunxuan.asia");
        InputStream inputStream = resource.getInputStream();
        String content = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println(content);
    }

    @Test
    public void test_BeanFactoryPostProcessorAndBeanPostProcessor(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. 读取配置文件&注册Bean
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");

        // 3. BeanDefinition 加载完成 & Bean实例化之前，修改 BeanDefinition 的属性值
        MyBeanFactoryPostProcessor beanFactoryPostProcessor = new MyBeanFactoryPostProcessor();
        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

        // 4. Bean实例化之后，修改 Bean 属性信息
        MyBeanPostProcessor beanPostProcessor = new MyBeanPostProcessor();
        beanFactory.addBeanPostProcessor(beanPostProcessor);

        // 5. 获取Bean对象调用方法
        UserService userService = beanFactory.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
    }

    @Test
    public void test_hook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("close！")));
    }

    @Test
    public void test_prototype() {
        // 1. 初始化容器
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();

        // 2. 获取Bean对象（测试作用域）
        UserService userService01 = applicationContext.getBean("userService", UserService.class);
        UserService userService02 = applicationContext.getBean("userService", UserService.class);

        // 3. 验证是否为不同实例（prototype）或相同实例（singleton）
        System.out.println("userService01 实例: " + userService01);
        System.out.println("userService02 实例: " + userService02);
        System.out.println("是否为不同实例: " + (userService01 != userService02));

        // 4. 打印哈希码（替代内存布局分析）
        System.out.println("userService01 哈希码: " + userService01.hashCode());
        System.out.println("userService02 哈希码: " + userService02.hashCode());
    }

    @Test
    public void test_factory_bean() {
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();

        // 2. 调用代理方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }

    @Test
    public void test_event() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, 1019129009086763L, "成功了！"));

        applicationContext.registerShutdownHook();
    }

}
