package com.ipad.ktvphone.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.blankj.utilcode.util.ToastUtils;


public class BaseFragment extends Fragment {


    private SVProgressHUD svProgressHUD;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        svProgressHUD = new SVProgressHUD(getActivity());
    }

    /**
     * 常用的跳转方法
     */
    public void gotoActivity(Class<?> cls, boolean isFinish) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
        if (isFinish) {
            getActivity().finish();
        }
    }

    public void gotoActivity(Class<?> cls, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtras(bundle);
        startActivity(intent);
        if (isFinish) {
            getActivity().finish();
        }
    }


    /**
     * 显示加载进度弹窗
     */
    protected void showProgress() {
        svProgressHUD.showWithStatus("加载中...", SVProgressHUD.SVProgressHUDMaskType.BlackCancel);
    }

    /**
     * 停止弹窗
     */
    protected void stopProgress() {
        if (svProgressHUD.isShowing()) {
            svProgressHUD.dismiss();
        }
    }


    public void showToast(String message) {
        ToastUtils.showShort(message);
    }
}
