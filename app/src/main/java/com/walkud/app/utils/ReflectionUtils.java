package com.walkud.app.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射工具类
 * kotlin 反射超级慢，这里使用Java来处理反射
 * Created by Zhuliya on 2018/11/8
 */
public class ReflectionUtils {
    public static Class<?>[] getClassTypes(Object[] args) {
        if (null == args || args.length == 0) {
            return null;
        }
        Class<?>[] argClasses = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            argClasses[i] = args[i].getClass();
        }
        return argClasses;
    }

    public static Object newInstance(String className, Object[] args, Class<?>[] argClasses) {
        Object instance = null;
        try {
            Class classObj = Class.forName(className);
            Constructor cons = classObj.getConstructor(argClasses);
            instance = cons.newInstance(args);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public static Object getStaticField(String className, String fieldName) {
        Object fieldValue = null;
        try {
            Class classObj = Class.forName(className);
            Field field = classObj.getDeclaredField(fieldName);
            field.setAccessible(true);
            fieldValue = field.get(null); // note: Field.get(Object obj), If this field is static, the object argument is ignored.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }

    public static boolean setStaticField(String className, String fieldName, Object value) {
        boolean result = false;
        try {
            Class classObj = Class.forName(className);
            Field field = classObj.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
            result = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object getField(Object owner, String fieldName) {
        Object fieldValue = null;
        try {
            Class classObj = owner.getClass();
            Field field = classObj.getDeclaredField(fieldName);
            field.setAccessible(true);
            fieldValue = field.get(owner);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }

    public static Object getField(Object owner, String className, String fieldName) {
        Object fieldValue = null;
        try {
            Class classObj = Class.forName(className);
            Field field = classObj.getDeclaredField(fieldName);
            field.setAccessible(true);
            fieldValue = field.get(owner);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }

    public static boolean setField(Object owner, String fieldName, Object value) {
        boolean result = false;
        try {
            Class classObj = owner.getClass();
            Field field = classObj.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(owner, value);
            result = true;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean setField(Object owner, String className, String fieldName, Object value) {
        boolean result = false;
        try {
            Class classObj = Class.forName(className);
            Field field = classObj.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(owner, value);
            result = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object invokeStaticMethod(String className, String methodName, Object[] args, Class<?>[] argClasses) {
        Object result = null;
        try {
            Class classObj = Class.forName(className);
            Method method = classObj.getDeclaredMethod(methodName, argClasses);
            method.setAccessible(true);
            result = method.invoke(null, args);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object invokeMethod(Object owner, String methodName, Object[] args, Class<?>[] argClasses) {
        Object result = null;
        try {
            Class classObj = owner.getClass();
            Method method = classObj.getDeclaredMethod(methodName, argClasses);
            method.setAccessible(true);
            result = method.invoke(owner, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object invokeMethod(Object owner, String className, String methodName, Object[] args, Class<?>[] argClasses) {
        Object result = null;
        try {
            Class classObj = Class.forName(className);
            Method method = classObj.getDeclaredMethod(methodName, argClasses);
            method.setAccessible(true);
            result = method.invoke(owner, args);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object newArrayInstance(String className, int len) {
        Object result = null;
        try {
            Class classObj = Class.forName(className);
            result = Array.newInstance(classObj, len);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object getObjectInArray(Object array, int index) {
        return Array.get(array, index);
    }

    public static void setObjectInArray(Object array, int index, Object value) {
        Array.set(array, index, value);
    }


    /**
     * 调用静态方法
     *
     * @param className
     * @param methodName
     * @param paramTypes
     * @param paramValues
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Object invokeStaticMethod(String className, String methodName, Class[] paramTypes,
                                            Object[] paramValues) {

        return invokeMethod(null, className, methodName, paramTypes, paramValues);

    }

    /**
     * 调用成员方法
     *
     * @param target
     * @param className
     * @param methodName
     * @param paramTypes
     * @param paramValues
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Object invokeMethod(Object target,
                                      String className,
                                      String methodName,
                                      Class[] paramTypes,
                                      Object[] paramValues) {

        try {
            Class clazz = Class.forName(className);
            Method method = clazz.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method.invoke(target, paramValues);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取字段对象
     *
     * @param target
     * @param clazz
     * @param fieldName
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Object getFieldObject(Object target, Class clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // try supper for Miui, Miui has a class named MiuiPhoneWindow
            try {
                Field field = clazz.getSuperclass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(target);
            } catch (Exception superE) {
                e.printStackTrace();
                superE.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取成员属性
     *
     * @param target
     * @param className
     * @param fieldName
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Object getFieldObject(Object target, String className, String fieldName) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
            return getFieldObject(target, clazz, fieldName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取静态字段对象
     *
     * @param className
     * @param fieldName
     * @return
     */
    public static Object getStaticFieldObject(String className, String fieldName) {

        return getFieldObject(null, className, fieldName);
    }

    /**
     * 设置成员属性
     *
     * @param target
     * @param className
     * @param fieldName
     * @param fieldValue
     */
    @SuppressWarnings("rawtypes")
    public static void setFieldObject(Object target,
                                      String className,
                                      String fieldName,
                                      Object fieldValue) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, fieldValue);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // try supper for Miui, Miui has a class named MiuiPhoneWindow
            try {
                Field field = clazz.getSuperclass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, fieldValue);
            } catch (Exception superE) {
                e.printStackTrace();
                superE.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (ClassNotFoundException e) {
        }
    }

    /**
     * 设置静态成员属性
     *
     * @param className
     * @param fieldName
     * @param fieldValue
     */
    public static void setStaticOjbect(String className, String fieldName, Object fieldValue) {
        setFieldObject(null, className, fieldName, fieldValue);
    }


    /**
     * 获取父类泛型类型
     *
     * @param clazz
     * @param index
     * @return
     */
    public static Class getSuperClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 获取父类泛型实例
     *
     * @param o
     * @param i
     * @param <T>
     * @return
     */
    public static <T> T getSuperClassGenricType(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (ClassCastException e) {
        }
        return null;
    }

    /**
     * 获取泛型类型
     *
     * @param object
     * @return
     */
    public static Type[] getParameterizedTypes(Object object) {
        Type superclassType = object.getClass().getGenericSuperclass();
        if (!ParameterizedType.class.isAssignableFrom(superclassType.getClass())) {
            return null;
        }
        return ((ParameterizedType) superclassType).getActualTypeArguments();
    }
}
