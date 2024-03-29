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
import com.ipad.ktvphone.api.HttpResultSubscriber;
import com.ipad.ktvphone.api.HttpServiceIml;
import com.ipad.ktvphone.entity.MusicBo;
import com.ipad.ktvphone.entity.OrderBO;
import com.ipad.ktvphone.entity.PayResultBo;
import com.ipad.ktvphone.utils.ZxingUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CreateOrderDialog extends PopupWindow {

    private Activity activity;
    private OrderBO orderBO;
    private MusicBo musicBo;
    private View dialogView;

    private TextView count_down_time;
    private ImageView closeImg;
    private TextView playingMusicName;
    private TextView playingTime;
    private TextView playingPrice;
    private ImageView qrCodeImg;

    private Subscription subscription;
    private CountDownTimer timer;


    public CreateOrderDialog(Activity activity, OrderBO orderBO, MusicBo musicBo) {
        super(activity);
        this.activity = activity;
        this.orderBO = orderBO;
        this.musicBo = musicBo;

        dialogView = LayoutInflater.from(activity).inflate(R.layout.create_order_dialog, null);
        initView();
        this.setBackgroundDrawable(new ColorDrawable(0));
        this.setContentView(dialogView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(SizeUtils.dp2px(522));
        //设置PopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        // this.setBackgroundDrawable(dw);
        this.setOnDismissListener(() -> {
            if (subscription != null) {
                subscription.unsubscribe();
            }
            if (timer != null) {
                timer.cancel();
            }
            backgroundAlpha(1f);
        });
    }


    private void initView() {
        count_down_time = dialogView.findViewById(R.id.count_down_time);
        closeImg = dialogView.findViewById(R.id.close_img);
        playingMusicName = dialogView.findViewById(R.id.play_music_name);
        playingTime = dialogView.findViewById(R.id.playing_time);
        playingPrice = dialogView.findViewById(R.id.music_price);
        qrCodeImg = dialogView.findViewById(R.id.qr_code_img);

        closeImg.setOnClickListener(v -> {
            dismiss();
            backgroundAlpha(1f);
        });
        playingTime.setText(orderBO.time);
        playingMusicName.setText(orderBO.song_name);
        playingPrice.setText(orderBO.cost);
        qrCodeImg.setImageBitmap(ZxingUtils.createNoRectQRCode(orderBO.code_url, 240));
        queryPaySuress();
        startTimer();
    }


    /**
     * 轮询查询支付结果
     */
    private void queryPaySuress() {
        int timer = Integer.parseInt(orderBO.timer);
        int time_out = Integer.parseInt(orderBO.pay_time_out);
        subscription = Observable.interval(0, timer, TimeUnit.SECONDS).take(time_out / timer)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap((Func1<Long, Observable<PayResultBo>>) aLong -> HttpServiceIml.getPayResult(orderBO.order_id))
                .takeUntil(new Func1<PayResultBo, Boolean>() {
                    @Override
                    public Boolean call(PayResultBo payResultBo) {
                        return payResultBo.is_polling == 0;
                    }
                }).subscribe(new HttpResultSubscriber<PayResultBo>() {
                    @Override
                    public void onSuccess(PayResultBo payResultBo) {
                        switch (payResultBo.pay_result) {
                            case "success":
                                dismiss();
                                new PaySuressDialog(activity).show();
                                break;
                            case "fail":
                                dismiss();
                                new PayRefundDialog(activity, musicBo).show();
                                dismiss();
                                break;
                        }
                    }

                    @Override
                    public void onFiled(String message) {

                    }
                });

    }


    /**
     * 启动一个秒数的倒计时
     */
    private void startTimer() {
        timer = new CountDownTimer(Integer.parseInt(orderBO.pay_time_out) * 1000L, 1000) {
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
