package com.ipad.ktvphone.ui;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ipad.ktvphone.R;
import com.ipad.ktvphone.entity.MusicBo;
import com.ipad.ktvphone.utils.CreateOrderUtils;

/**
 * 支付成功弹窗
 */
public class PayRefundDialog extends PopupWindow {

    private final Activity activity;
    private final TextView count_down_time;
    private TextView restart_pay;

    private CountDownTimer timer;

    public PayRefundDialog(Activity activity, MusicBo musicBo) {
        this.activity = activity;

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_pay_refund, null);
        count_down_time = dialogView.findViewById(R.id.count_down_time);
        ImageView close_img = dialogView.findViewById(R.id.close_img);
        restart_pay = dialogView.findViewById(R.id.restart_pay);

        this.setBackgroundDrawable(new ColorDrawable(0));
        this.setContentView(dialogView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(SizeUtils.dp2px(381));
        //设置PopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setOnDismissListener(() -> {
            if (timer != null) {
                timer.cancel();
            }
            backgroundAlpha(1.0f);
        });
        close_img.setOnClickListener(v -> {
            dismiss();
            backgroundAlpha(1f);
        });
        restart_pay.setOnClickListener(v -> {
            dismiss();
            CreateOrderUtils.createOrder(musicBo);
        });
        startTimer();
    }


    /**
     * 启动一个秒数的倒计时
     */
    private void startTimer() {
        timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                count_down_time.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                dismiss();
            }
        };
        timer.start();
    }


    public void show() {
        backgroundAlpha(0.5f);
        showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    /**
     * 设置添加屏幕的背景透明度
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

}
