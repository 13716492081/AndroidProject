package com.example.ballneko_dellg5.picview;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    /**
     * 获取全局上下文*/
    public static Context getContext() {
        return context;
    }
}
