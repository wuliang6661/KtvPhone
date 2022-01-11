package com.ipad.ktvphone.ui;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ipad.ktvphone.R;
import com.ipad.ktvphone.entity.MusicBo;
import com.ipad.ktvphone.entity.OrderBO;
import com.ipad.ktvphone.utils.ZxingUtils;

public class CreateOrderDialog extends PopupWindow {

    private Activity activity;
    private OrderBO orderBO;
    private View dialogView;

    private TextView count_down_time;
    private ImageView closeImg;
    private TextView playingMusicName;
    private TextView playingTime;
    private TextView playingPrice;
    private ImageView qrCodeImg;


    public CreateOrderDialog(Activity activity, OrderBO musicBo) {
        super(activity);
        this.activity = activity;
        this.orderBO = musicBo;

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
        this.setOnDismissListener(() -> backgroundAlpha(1f));
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
