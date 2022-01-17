package com.ipad.ktvphone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ipad.ktvphone.api.HttpResultSubscriber;
import com.ipad.ktvphone.api.HttpServiceIml;
import com.ipad.ktvphone.base.BaseActivity;
import com.ipad.ktvphone.entity.MusicBo;
import com.ipad.ktvphone.entity.VersionBO;
import com.ipad.ktvphone.entity.event.HideSearchEvent;
import com.ipad.ktvphone.entity.event.SearchMusicEvent;
import com.ipad.ktvphone.ui.HomeFragment;
import com.ipad.ktvphone.ui.SearchFragment;
import com.ipad.ktvphone.utils.UpdateUtils;
import com.ipad.ktvphone.weight.OnDoubleClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private EditText editText;

    private SearchFragment searchDialog;

    private FrameLayout searchFragment;
    private TextView versionName;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        editText = findViewById(R.id.edit_view);
        searchFragment = findViewById(R.id.search_fragment);
        versionName = findViewById(R.id.version_name);
        versionName.setText("当前版本：v" + AppUtils.getAppVersionName());
        versionName.setOnTouchListener(new OnDoubleClickListener(() -> {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }));
        findViewById(R.id.logo).setOnTouchListener(new OnDoubleClickListener(this::checkUpdate));
        setListener();

        searchDialog = new SearchFragment();
        FragmentUtils.add(getSupportFragmentManager(), new HomeFragment(), R.id.container_fragment);
//        RootUtils.upgradeRootPermission(getPackageCodePath());
        startHeartbeat();
    }

    @Override
    protected int getLayout() {
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_main;
    }


    private void setListener() {
        editText.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMusic(editText.getText().toString());
                handled = true;
            }
            return handled;
        });
    }


    /**
     * 搜索歌曲
     */
    private void searchMusic(String keyWord) {
        HttpServiceIml.searchMusic(0, keyWord).subscribe(new HttpResultSubscriber<List<MusicBo>>() {
            @Override
            public void onSuccess(List<MusicBo> musicBos) {
                if (musicBos == null || musicBos.size() == 0) {
                    return;
                }
                searchFragment.setVisibility(View.VISIBLE);
                FragmentUtils.add(getSupportFragmentManager(), searchDialog, R.id.search_fragment);
                EventBus.getDefault().post(new SearchMusicEvent(keyWord, musicBos));
            }

            @Override
            public void onFiled(String message) {
                showToast(message);
            }
        });
    }


    /**
     * 建立心跳连接
     */
    private void startHeartbeat() {
        Observable.interval(0, 2, TimeUnit.MINUTES)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        requestHeart();
                    }
                });
    }


    /**
     * 检测更新
     */
    private void checkUpdate() {
        HttpServiceIml.checkUpdate().subscribe(new HttpResultSubscriber<VersionBO>() {
            @Override
            public void onSuccess(VersionBO versionBO) {
                new UpdateUtils().checkUpdate(MainActivity.this, versionBO);
            }

            @Override
            public void onFiled(String message) {
                ToastUtils.showShort(message);
            }
        });
    }


    private void requestHeart() {
        HttpServiceIml.postHeartbeat().subscribe(new HttpResultSubscriber<VersionBO>() {
            @Override
            public void onSuccess(VersionBO s) {
            }

            @Override
            public void onFiled(String message) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HideSearchEvent event) {
        searchFragment.setVisibility(View.GONE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}