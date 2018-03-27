package code.zxhua.hookamsdemo.hook;

import android.os.Handler;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Zxhua on 2018/3/23 0023.
 */

public class HookExtension {
    public static final String TAG = "HOOK_EXTENSION";
    public static final String EXTRA_TARGET_ITENT = "TargetIntent";
    public static final String UNREGISTERED = "unregisteredActivity";

    public static final void hookActivityManagerService() {
        try {
            Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
            if (activityManagerNative == null) return;
            // 获取gDefault字段
            Field gDefaultField = activityManagerNative.getDeclaredField("gDefault");
            gDefaultField.setAccessible(true);
            //从字段中取出对象值
            Object gDefault = gDefaultField.get(null);
            //gDefault是一个Singleton对象；需要成Singleton中取出这个单例的AMS代理
            Class<?> singleton = Class.forName("android.util.Singleton");
            Field mInstanceField = singleton.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            Object activityManager = mInstanceField.get(gDefault);
            //创建这个manager的代理对象
            Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
            Object gDefaultProxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{iActivityManagerInterface}, new IActivityManagerHandler(activityManager));
            mInstanceField.set(gDefault, gDefaultProxy);
        } catch (Exception e) {
            Log.e(TAG, "hook AMS failure " + e.getMessage());
        }
    }


    public static final void hookActivityThreadCallBack() {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            if (activityThread == null) return;
            //获取ActivityThread对象
            Method currentActivityThreadMethod = activityThread.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);
            //获取mH
            Field mHField = activityThread.getDeclaredField("mH");
            mHField.setAccessible(true);
            Handler mH = (Handler) mHField.get(currentActivityThread);
            //因为要hook handleMessage方法，而该方法是接口CallBack的方法，所以需要拿到Handler的成员变量mCallBack
            Field mCallBackField = Handler.class.getDeclaredField("mCallback");
            mCallBackField.setAccessible(true);
            mCallBackField.set(mH, new ActivityThreadHandlerCallBack(mH));
        } catch (Exception e) {
            Log.e(TAG, "hook AT failure " + e.getMessage());
        }
    }
}
