package com.ipad.ktvphone.utils;

import android.content.Context;

import com.bigkoo.svprogresshud.SVProgressHUD;

public class ProgressUtils {


    SVProgressHUD svProgressHUD;


    public ProgressUtils() {
        Context context = AppManager.getAppManager().curremtActivity();
        svProgressHUD = new SVProgressHUD(context);
    }

    public void showProgress() {
        svProgressHUD.showWithStatus("加载中...", SVProgressHUD.SVProgressHUDMaskType.BlackCancel);
    }


    /**
     * 停止弹窗
     */
    public void stopProgress() {
        if (svProgressHUD.isShowing()) {
            svProgressHUD.dismiss();
        }
    }

}
