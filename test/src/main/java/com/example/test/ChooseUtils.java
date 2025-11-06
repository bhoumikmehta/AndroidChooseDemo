package com.example.test;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhenxi on 2021/7/5
 * <p>
 * 源码参考:
 * http://androidxref.com/9.0.0_r3/s?refs=ClassD&project=art
 */
public class ChooseUtils {


    private static Method getInstancesOfClassesMethod;
    private static Class<?> VMDebugClazz = null;

    private static void init() {
        try {
            if (getInstancesOfClassesMethod != null) {
                return;
            }
            VMDebugClazz = Class.forName("dalvik.system.VMDebug");
            if (Build.VERSION.SDK_INT >= 28) {
                getInstancesOfClassesMethod = VMDebugClazz.getDeclaredMethod("getInstancesOfClasses",
                        Class[].class, Boolean.TYPE);
            }
        } catch (Throwable e) {
            if (VMDebugClazz != null) {
                Method[] declaredMethods = VMDebugClazz.getDeclaredMethods();
                for (Method method : declaredMethods) {
                    Log.e("Zhenxi","VMDebugClazz method -> " + method.toString());
                }
            }
        }
    }

    /**
     * 根据Class获取当前进程全部的实例
     *
     * @param clazz 需要查找的Class
     * @return 当前进程的全部实例。
     */
    public static <T> ArrayList<T> choose(Class<T> clazz) {
        return choose(clazz, false);
    }

    /**
     * 根据Class获取当前进程全部的实例
     *
     * @param clazz      需要查找的Class
     * @param assignable 是否包含子类的实例
     * @return 当前进程的全部实例。
     */
    @TargetApi(28)
    public static <T> ArrayList<T> choose(Class<T> clazz, boolean assignable) {
        ArrayList<T> resut = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                try {
                    init();
                    if (getInstancesOfClassesMethod == null) {
                        resut = new ArrayList<>((List<T>) Arrays.asList(ChooseJvmZ(clazz)));
                        return resut;
                    }
                    Object[][] instancesOfClasses = getInstancesOfClasses(new Class[]{clazz}, assignable);
                    if (instancesOfClasses != null) {
                        resut = new ArrayList<>();
                        for (Object[] instancesOfClass : instancesOfClasses) {
                            List<T> objects = (List<T>) Arrays.asList(instancesOfClass);
                            resut.addAll(objects);
                        }
                    }
                } catch (Throwable e) {
                    Log.e("Zhenxi","ChooseUtils choose error ", e);
                    return resut;
                }
            }
            if (resut == null || resut.size() == 0) {
                try {
                    Object[] objects = ChooseJvmZ(clazz);
                    if (objects != null && objects.length > 0) {
                        //CLog.e("choose get obj size " +
                        // objects.length + " " + Arrays.toString(objects));
                        resut = new ArrayList<>((List<T>) Arrays.asList(objects));
                    } else {
                        Log.e("Zhenxi","choose get obj fail ChooseJvm == null "+clazz.getName());
                    }
                } catch (Exception e) {
                    Log.e("Zhenxi","ChooseJvm error  "+e.toString());
                }
            }
        } catch (Throwable e) {
            Log.e("Zhenxi","choose get obj choose error " + e.getMessage());
            return null;
        }
        return resut;
    }

    @TargetApi(28)
    private static Object[][] getInstancesOfClasses(Class<?>[] classes,
                                                    boolean assignable) {
        try {
            if (getInstancesOfClassesMethod == null) {
                return null;
            }
            return (Object[][]) getInstancesOfClassesMethod.invoke(
                    null, new Object[]{classes, assignable});
        } catch (Throwable e) {
            return null;
        }
    }

    private native static Object[] ChooseJvmZ(Class<?> clazz);

}
