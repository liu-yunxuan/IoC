package asia.liuyunxuan.ioc.dependence;

import asia.liuyunxuan.ioc.aspect.MethodBeforeAdvice;
import java.lang.reflect.Method;

public class SpouseAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println("关怀小两口(切面)：" + method);
    }

}
