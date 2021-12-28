package com.ipad.ktvphone.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;

public class BaseFragment extends Fragment {


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


    public void showToast(String message){
        ToastUtils.showShort(message);
    }
}
