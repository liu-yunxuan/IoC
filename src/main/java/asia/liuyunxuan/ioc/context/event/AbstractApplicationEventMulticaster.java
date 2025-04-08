package asia.liuyunxuan.ioc.context.event;

import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.factory.BeanFactory;
import asia.liuyunxuan.ioc.beans.factory.BeanFactoryAware;
import asia.liuyunxuan.ioc.context.ApplicationEvent;
import asia.liuyunxuan.ioc.context.ApplicationListener;
import asia.liuyunxuan.ioc.utils.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * 应用事件广播器的抽象实现，提供了基础的事件监听器管理和事件分发功能。
 * 
 * <p>此类实现了ApplicationEventMulticaster接口，提供了监听器注册表的管理
 * 以及事件类型匹配的核心逻辑。它使用反射机制来确定监听器是否支持特定类型的事件。
 * 
 * <p>此类还实现了BeanFactoryAware接口，允许访问BeanFactory，以便在需要时
 * 获取其他Bean。
 * 
 * <p>子类需要实现multicastEvent方法来定义具体的事件分发策略。
 *
 * @author liuyunxuan
 * @see ApplicationEventMulticaster
 * @see ApplicationListener
 * @see ApplicationEvent
 * @see BeanFactoryAware
 * @since 1.0
 */
public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    /**
     * 应用监听器集合，存储所有注册的事件监听器。
     * 使用LinkedHashSet保持注册顺序并确保唯一性。
     */
    public final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    /**
     * 添加一个应用监听器到注册表中。
     * 
     * @param listener 要添加的监听器，不能为null
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    /**
     * 从注册表中移除一个应用监听器。
     * 
     * @param listener 要移除的监听器，不能为null
     */
    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    /**
     * 设置BeanFactory，由BeanFactoryAware接口定义。
     * 此方法被标记为final，确保子类不能覆盖此行为。
     * 
     * @param beanFactory 要设置的BeanFactory，不能为null
     */
    @Override
    public final void setBeanFactory(BeanFactory beanFactory) {
        /*
          持有的BeanFactory引用，可用于获取其他Bean。
         */
    }


    /**
     * 获取支持给定事件的所有监听器。
     * <p>此方法遍历所有注册的监听器，并使用supportsEvent方法
     * 检查每个监听器是否支持给定的事件类型。
     * 
     * @param event 要处理的事件，不能为null
     * @return 支持给定事件的监听器集合
     */
    @SuppressWarnings("rawtypes")
    protected Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
        LinkedList<ApplicationListener> allListeners = new LinkedList<>();
        for (ApplicationListener<ApplicationEvent> listener : applicationListeners) {
            if (supportsEvent(listener, event)) allListeners.add(listener);
        }
        return allListeners;
    }


    /**
     * 检查给定的监听器是否支持给定的事件类型。
     * <p>此方法使用反射机制来确定监听器声明的泛型类型，并检查
     * 事件是否是该类型的实例或子类型。
     * <p>此方法处理了CGLIB代理类的特殊情况，确保能够正确获取
     * 目标类的泛型信息。
     * 
     * @param applicationListener 要检查的监听器，不能为null
     * @param event 要检查的事件，不能为null
     * @return 如果监听器支持给定事件类型，则返回true
     * @throws BeansException 如果无法解析事件类名
     */
    @SuppressWarnings("rawtypes")
    protected boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event) {
        Class<? extends ApplicationListener> listenerClass = applicationListener.getClass();

        // 按照 CglibSubclassingInstantiationStrategy、SimpleInstantiationStrategy 不同的实例化类型，需要判断后获取目标 class
        Class<?> targetClass = ClassUtils.isCglibProxyClass(listenerClass) ? listenerClass.getSuperclass() : listenerClass;
        Type genericInterface = targetClass.getGenericInterfaces()[0];

        Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
        String className = actualTypeArgument.getTypeName();
        Class<?> eventClassName;
        try {
            eventClassName = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong event class name: " + className);
        }
        // 判定此 eventClassName 对象所表示的类或接口与指定的 event.getClass() 参数所表示的类或接口是否相同，或是否是其超类或超接口。
        // isAssignableFrom是用来判断子类和父类的关系的，或者接口的实现类和接口的关系的，默认所有的类的终极父类都是Object。如果A.isAssignableFrom(B)结果是true，证明B可以转换成为A,也就是A可以由B转换而来。
        return eventClassName.isAssignableFrom(event.getClass());
    }

}
