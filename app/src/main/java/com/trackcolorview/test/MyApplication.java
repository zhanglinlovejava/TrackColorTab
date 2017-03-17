package com.trackcolorview.test;

import android.app.Application;
import android.content.Context;

import com.trackcolorview.test.util.ScreenUtil;

/**
 * Created by Colin.Zhang on 2017/3/16.
 */

public class MyApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ScreenUtil.init(mContext);
    }

    public static Context getContext() {
        return mContext;
    }
}
