package cn.edu.hebtu.software.sharemateclient;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseUI;

public class EMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        EaseUI.getInstance().init(this,null);
        EMClient.getInstance().setDebugMode(true);
    }
}
