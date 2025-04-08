package asia.liuyunxuan.ioc.aspect;

import java.lang.reflect.Method;

public class Student2ServiceBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println("拦截方法：" + method.getName());
    }

}