package code.zxhua.hookamsdemo;

import android.app.Application;
import android.content.Context;

import code.zxhua.hookamsdemo.hook.HookExtension;

/**
 * Created by Zxhua on 2018/3/27 0027.
 */

public class App extends Application {

    public static Context mC;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mC = base;

        HookExtension.hookActivityThreadCallBack();
        HookExtension.hookActivityManagerService();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
