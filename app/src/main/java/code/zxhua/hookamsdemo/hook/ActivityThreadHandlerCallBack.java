package code.zxhua.hookamsdemo.hook;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by Zxhua on 2018/3/27 0027.
 */

public class ActivityThreadHandlerCallBack implements Handler.Callback {

    private Handler mH;

    public ActivityThreadHandlerCallBack(Handler mH) {
        this.mH = mH;
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.e(HookExtension.TAG, msg.toString());
        switch (msg.what) {
            case 100:
                handleLaunchActivity(msg);
                break;
        }
        //替换完成后， 调用ActivityThread的mH的handleMessage方法继续系统流程
        mH.handleMessage(msg);
        return true;
    }

    //换回目标Intent
    private void handleLaunchActivity(Message msg) {
        Object obj = msg.obj;
        try {
            Field intent = obj.getClass().getDeclaredField("intent");
            intent.setAccessible(true);
            Intent rawIntent = (Intent) intent.get(obj);
            Intent targetIntent = rawIntent.getParcelableExtra(HookExtension.EXTRA_TARGET_ITENT);
            if (targetIntent != null)
                rawIntent.setComponent(targetIntent.getComponent());
        } catch (Exception e) {
            throw new RuntimeException("hook lauch activity failure", e);
        }
    }
}
