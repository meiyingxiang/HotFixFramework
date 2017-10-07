package com.frank_ghost.hotfix;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import dalvik.system.DexFile;

/**
 * Created by admin on 2017/10/3.
 */
public class DexManager {
    private Context context;
    private static DexManager ourInstance = new DexManager();

    public static DexManager getInstance() {
        return ourInstance;
    }

    private DexManager() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void loadFile(File file) {
        try {
            DexFile dexFile = DexFile.loadDex(file.getAbsolutePath(),
                    new File(context.getCacheDir(), "opt").getAbsolutePath(), Context.MODE_PRIVATE);
            //得到class,修复好的method
            Enumeration<String> entries = dexFile.entries();
            if (entries != null) {
                while (entries.hasMoreElements()) {
                    //拿到全类名
                    String className = entries.nextElement();
                    Class aClass = dexFile.loadClass(className, context.getClassLoader());
                    if (aClass != null) {
                        fixClass(aClass);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fixClass(Class realClass) {
        Method[] methods = realClass.getDeclaredMethods();
        if (methods != null) {
            for (Method rightMeth : methods) {
                Replace annotation = rightMeth.getAnnotation(Replace.class);
                if (annotation == null) {
                    continue;
                }
                //找到修复好的Method  找到出BUG的方法
                String wrongClazz = annotation.clazz();
                String wrongMethodName = annotation.method();
                try {
                    Class aClass = Class.forName(wrongClazz);
                    Method wrongMethod = aClass.getDeclaredMethod(wrongMethodName, rightMeth.getParameterTypes());
                    if (Build.VERSION.SDK_INT <= 18) {
                        replace(Build.VERSION.SDK_INT, wrongMethod, rightMeth);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public native void replace(int sdk, Method wrongMethod, Method rightMeth);
}
