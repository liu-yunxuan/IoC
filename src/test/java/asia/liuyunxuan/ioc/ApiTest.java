package asia.liuyunxuan.ioc;

import asia.liuyunxuan.ioc.aspect.*;
import asia.liuyunxuan.ioc.aspect.aspectj.AspectJExpressionJoinPointSelector;
import asia.liuyunxuan.ioc.aspect.framework.Cglib2AopProxy;
import asia.liuyunxuan.ioc.aspect.framework.JdkDynamicAopProxy;
import asia.liuyunxuan.ioc.aspect.framework.ReflectiveMethodInvocation;
import asia.liuyunxuan.ioc.bean.Student2Service;
import asia.liuyunxuan.ioc.bean.StudentService;
import asia.liuyunxuan.ioc.bean.UserDao;
import asia.liuyunxuan.ioc.bean.UserService;
import asia.liuyunxuan.ioc.common.MyComponentProviderPostProcessor;
import asia.liuyunxuan.ioc.component.PropertyValue;
import asia.liuyunxuan.ioc.component.PropertyValues;
import asia.liuyunxuan.ioc.component.container.config.ComponentDefinition;
import asia.liuyunxuan.ioc.component.container.config.ComponentReference;
import asia.liuyunxuan.ioc.component.container.support.DefaultRegistry;
import asia.liuyunxuan.ioc.component.container.support.InstantiationStrategy;
import asia.liuyunxuan.ioc.component.container.xml.XmlComponentDefinitionReader;
import asia.liuyunxuan.ioc.common.MyBeanPostProcessor;
import asia.liuyunxuan.ioc.runtime.support.ClassPathXmlContext;
import asia.liuyunxuan.ioc.kernel.io.DefaultResourceLoader;
import asia.liuyunxuan.ioc.kernel.io.Resource;
import asia.liuyunxuan.ioc.dependence.Husband;
import asia.liuyunxuan.ioc.dependence.Wife;
import asia.liuyunxuan.ioc.event.CustomEvent;
import asia.liuyunxuan.ioc.proxy.IUserService;
import asia.liuyunxuan.ioc.extension.ExtensionLoader;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.*;
import java.util.Map;
import java.util.stream.Collectors;

public class ApiTest {
    @Test
    public void test_BeanFactory() {
        // 1.初始化 ComponentProvider
        DefaultRegistry beanFactory = new DefaultRegistry();

        // 2. UserDao 注册
        beanFactory.registerBeanDefinition("userDao", new ComponentDefinition(UserDao.class));

        // 3. UserService 设置属性[id、userDao]
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("id", "10001"));
        propertyValues.addPropertyValue(new PropertyValue("userDao",new ComponentReference("userDao")));

        // 4. UserService 注入bean
        ComponentDefinition componentDefinition = new ComponentDefinition(UserService.class, propertyValues);
        beanFactory.registerBeanDefinition("userService", componentDefinition);

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
        // 1.初始化 ComponentProvider
        DefaultRegistry beanFactory = new DefaultRegistry();

        // 2. 读取配置文件&注册Bean
        XmlComponentDefinitionReader reader = new XmlComponentDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");

        // 3. ComponentDefinition 加载完成 & Bean实例化之前，修改 ComponentDefinition 的属性值
        MyComponentProviderPostProcessor beanFactoryPostProcessor = new MyComponentProviderPostProcessor();
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
        ClassPathXmlContext applicationContext = new ClassPathXmlContext("classpath:spring.xml");
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
        // 1.初始化 ComponentProvider
        ClassPathXmlContext applicationContext = new ClassPathXmlContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();

        // 2. 调用代理方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }

    @Test
    public void test_event() {
        ClassPathXmlContext applicationContext = new ClassPathXmlContext("classpath:spring.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, 1019129009086763L, "成功了！"));

        applicationContext.registerShutdownHook();
    }


    @Test
    public void test_dynamic() {
        // 目标对象
        IStudentService userService =new Student2Service();
        // 组装代理信息
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setMethodInterceptor(new StudentServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionJoinPointSelector("execution(* asia.liuyunxuan.ioc.aspect.IStudentService.*(..))"));

        // 代理对象(JdkDynamicAopProxy)
        IStudentService proxy_jdk = (IStudentService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        // 测试调用
        System.out.println("测试结果：" + proxy_jdk.selectUser());

        // 代理对象(Cglib2AopProxy)
        IStudentService proxy_cglib = (IStudentService) new Cglib2AopProxy(advisedSupport).getProxy();
        // 测试调用
        System.out.println("测试结果：" + proxy_cglib.register("花花"));
    }


    @Test
    public void test_proxy_method() {
        // 目标对象(可以替换成任何的目标对象)
        Object targetObj = new Student2Service();

        // AOP 代理
        IStudentService proxy = (IStudentService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), targetObj.getClass().getInterfaces(), new InvocationHandler() {
            // 方法匹配器
            final MethodMatcher methodMatcher = new AspectJExpressionJoinPointSelector("execution(* asia.liuyunxuan.ioc.aspect.IStudentService.*(..))");

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (methodMatcher.matches(method, targetObj.getClass())) {
                    // 方法拦截器
                    MethodInterceptor methodInterceptor = invocation -> {
                        long start = System.currentTimeMillis();
                        try {
                            return invocation.proceed();
                        } finally {
                            System.out.println("监控 - Begin By AOP");
                            System.out.println("方法名称：" + invocation.getMethod().getName());
                            System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
                            System.out.println("监控 - End\r\n");
                        }
                    };
                    // 反射调用
                    return methodInterceptor.invoke(new ReflectiveMethodInvocation(targetObj, method, args));
                }
                return method.invoke(targetObj, args);
            }
        });

        String result = proxy.selectUser();
        System.out.println("测试结果：" + result);

    }


    @Test
    public void test_aop() {
        ClassPathXmlContext applicationContext = new ClassPathXmlContext("classpath:spring.xml");
        IStudentService studentService = applicationContext.getBean("student2Service", IStudentService.class);
        System.out.println("测试结果：" + studentService.selectUser());
    }


    @Test
    public void test_property() {
        ClassPathXmlContext applicationContext = new ClassPathXmlContext("classpath:spring-property.xml");
        IStudentService studentService = applicationContext.getBean("studentService", IStudentService.class);
        System.out.println("测试结果：" + studentService);
    }


    @Test
    public void test_scan() {
        ClassPathXmlContext applicationContext = new ClassPathXmlContext("classpath:spring-scan.xml");
        IStudentService studentService = applicationContext.getBean("studentService", IStudentService.class);
        System.out.println("测试结果：" + studentService.selectUser());
    }

    @Test
    public void test_scan_annotation() {
        ClassPathXmlContext applicationContext = new ClassPathXmlContext("classpath:spring.xml");
        IStudentService studentService = applicationContext.getBean("studentService", IStudentService.class);
        System.out.println("测试结果：" + studentService.selectUser());
    }

    @Test
    public void test_autoProxy() {
        ClassPathXmlContext applicationContext = new ClassPathXmlContext("classpath:spring.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }

    @Test
    public void test_circular() {
        ClassPathXmlContext applicationContext = new ClassPathXmlContext("classpath:spring.xml");
        Husband husband = applicationContext.getBean("husband", Husband.class);
        Wife wife = applicationContext.getBean("wife", Wife.class);
        System.out.println("老公的媳妇：" + husband.queryWife());
        System.out.println("媳妇的老公：" + wife.queryHusband());
    }

    @Test
    public void test_spi() throws IOException, ClassNotFoundException {
        ExtensionLoader extensionLoader = ExtensionLoader.getInstance();
        extensionLoader.loadExtension(InstantiationStrategy.class);
        Map<String, InstantiationStrategy> gets = extensionLoader.gets(InstantiationStrategy.class);
        gets.forEach((k, v) -> {
            System.out.println(k);
            System.out.println(v);
        });
    }

}
