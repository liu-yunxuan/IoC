package asia.liuyunxuan.ioc.beans.factory.support;

import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

/**
 * CGLib动态代理方式实现的Bean实例化策略。
 * <p>
 * 该策略使用CGLib的Enhancer来创建目标类的子类，通过这种方式实现Bean的实例化。
 * 相比于JDK反射方式，CGLib方式可以实例化没有实现接口的类，并且在某些场景下性能更好。
 * 但需要注意的是，CGLib不能代理final类。
 */
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {

    public CglibSubclassingInstantiationStrategy() {
    }


    /**
     * 使用CGLib方式实例化Bean对象。
     *
     * @param beanDefinition Bean的定义信息
     * @param beanName Bean的名称
     * @param ctor 指定的构造函数，如果为null则使用默认构造函数
     * @param args 构造函数参数，如果使用默认构造函数则为null
     * @return 实例化的Bean对象
     * @throws BeansException 当实例化过程中发生异常时抛出
     */
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args) throws BeansException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanDefinition.getBeanClass());
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
        if (null == ctor) return enhancer.create();
        return enhancer.create(ctor.getParameterTypes(), args);
    }
}
