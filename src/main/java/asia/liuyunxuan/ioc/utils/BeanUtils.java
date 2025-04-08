package asia.liuyunxuan.ioc.utils;

import asia.liuyunxuan.ioc.beans.BeansException;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Bean操作工具类，提供对Bean属性的反射操作功能。
 * 
 * <p>该工具类主要用于在IoC容器中进行Bean属性的动态设置，支持对私有字段的访问。
 * 提供了对Bean字段的查找、访问控制修改以及值设置等功能。
 *
 * @author liuyunxuan
 * @since 1.0
 */
public class BeanUtils {
    /**
     * 设置Bean对象指定字段的值。
     * 
     * <p>该方法通过反射机制，设置目标对象指定字段的值。支持设置私有字段，
     * 会自动处理字段的访问权限。如果字段不存在，将抛出异常。
     *
     * @param bean 目标Bean对象
     * @param fieldName 要设置的字段名
     * @param value 要设置的值
     * @throws BeansException 如果字段不存在或无法设置值时抛出
     * @throws IllegalArgumentException 如果bean或fieldName为null
     */
    public static void setFieldValue(Object bean, String fieldName, Object value) {
        Objects.requireNonNull(bean, "Bean must not be null");
        Objects.requireNonNull(fieldName, "Field name must not be null");
        Class<?> clazz = bean.getClass();
        Field field = findField(clazz, fieldName);
        if (field == null) {
            throw new BeansException("Could not find field [" + fieldName + "] on class " + clazz.getName());
        }
        makeAccessible(field);
        try {
            field.set(bean, value);
        } catch (IllegalAccessException ex) {
            throw new BeansException("Unable to set field [" + fieldName + "] on class " + clazz.getName(), ex);
        }
    }

    /**
     * 在指定类及其父类中查找指定名称的字段。
     *
     * @param clazz 要查找的类
     * @param fieldName 字段名称
     * @return 找到的Field对象，如果未找到返回null
     */
    private static Field findField(Class<?> clazz, String fieldName) {
        while (clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 确保字段可访问。
     * 
     * <p>如果字段不可访问，则修改其访问权限为可访问。
     *
     * @param field 要处理的字段
     */
    private static void makeAccessible(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }
}
