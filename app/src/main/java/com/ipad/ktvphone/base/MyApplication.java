package com.ipad.ktvphone.base;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;

import androidx.multidex.MultiDex;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * 作者 by wuliang 时间 16/10/26.
 * <p>
 * 程序的application
 */

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    public static SPUtils spUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        CustomActivityOnCrash.install(this);
        Utils.init(this);
        spUtils = SPUtils.getInstance("ktv_phone");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
