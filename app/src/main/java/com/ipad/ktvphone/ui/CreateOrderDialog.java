package com.ipad.ktvphone.ui;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.SizeUtils;
import com.ipad.ktvphone.R;
import com.ipad.ktvphone.entity.MusicBo;

public class CreateOrderDialog extends PopupWindow {

    private Activity activity;
    private MusicBo musicBo;

    private View dialogView;

    public CreateOrderDialog(Activity activity, MusicBo musicBo) {
        super(activity);
        this.activity = activity;
        this.musicBo = musicBo;

        dialogView = LayoutInflater.from(activity).inflate(R.layout.create_order_dialog, null);
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




    /**
     * 设置添加屏幕的背景透明度
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
}
