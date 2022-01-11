package com.ipad.ktvphone.utils;

import android.app.Activity;
import android.view.Gravity;

import com.blankj.utilcode.util.ToastUtils;
import com.ipad.ktvphone.api.HttpResultSubscriber;
import com.ipad.ktvphone.api.HttpServiceIml;
import com.ipad.ktvphone.entity.MusicBo;
import com.ipad.ktvphone.entity.OrderBO;
import com.ipad.ktvphone.ui.CreateOrderDialog;

public class CreateOrderUtils {


    /**
     * 创建订单
     *
     * @param musicBo
     */
    public static void createOrder(MusicBo musicBo) {
        ProgressUtils progressUtils = new ProgressUtils();
        progressUtils.showProgress();
        HttpServiceIml.createPayOrder(musicBo).subscribe(new HttpResultSubscriber<OrderBO>() {
            @Override
            public void onSuccess(OrderBO s) {
                progressUtils.stopProgress();
                showPayDialog(s);
            }

            @Override
            public void onFiled(String message) {
                progressUtils.stopProgress();
                ToastUtils.showShort(message);
            }
        });
    }


    private static void showPayDialog(OrderBO orderBO) {
        Activity activity = AppManager.getAppManager().curremtActivity();
        CreateOrderDialog dialog = new CreateOrderDialog(activity, orderBO);
        dialog.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

}
