package code.zxhua.hookamsdemo.hook;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import code.zxhua.hookamsdemo.App;
import code.zxhua.hookamsdemo.TempActivity;

/**
 * Created by Zxhua on 2018/3/23 0023.
 */

public class IActivityManagerHandler implements InvocationHandler {

    private static final String TAG = "IActiviyManagerhandler";
    //要代理的目标对象
    Object mBase;

    public IActivityManagerHandler(Object mBase) {
        this.mBase = mBase;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("startActivity".equals(method.getName())) {
            Log.e(TAG, "startActivity hook 成功");
            Intent rawIntent = null;
            int intentIndex = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    intentIndex = i;
                    break;
                }
            }
            rawIntent = (Intent) args[intentIndex];
            //是否为未注册Activity
            boolean unregister = rawIntent.getBooleanExtra(HookExtension.UNREGISTERED, false);
            if (unregister) {
                //创建坑位Intent
                Intent tempIntent = new Intent();
                //获取包名
                String packageName = App.mC.getPackageName();
                //替换坑位Activity
                ComponentName componentName = new ComponentName(packageName, TempActivity.class.getCanonicalName());
                tempIntent.setComponent(componentName);
                //把原始intent存起来
                tempIntent.putExtra(HookExtension.EXTRA_TARGET_ITENT, rawIntent);
                //替换Intent
                args[intentIndex] = tempIntent;
                return method.invoke(mBase, args);
            }
        }

        return method.invoke(mBase, args);
    }
}
