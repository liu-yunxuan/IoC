package asia.liuyunxuan.ioc.utils;

import asia.liuyunxuan.ioc.beans.BeansException;

import java.lang.reflect.Field;

public class BeanUtils {
    public static void setFieldValue(Object bean, String fieldName, Object value) {
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

    private static void makeAccessible(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }
}
