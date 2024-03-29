package com.ipad.ktvphone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.StringUtils;
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
import com.ipad.ktvphone.utils.HeartBeatUtils;
import com.ipad.ktvphone.utils.UpdateUtils;
import com.ipad.ktvphone.weight.OnDoubleClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.Nullable;

public class MainActivity extends BaseActivity {

    private EditText editText;

    private SearchFragment searchDialog;

    private FrameLayout searchFragment;
    private TextView versionName;
    private HomeFragment homeFragment;

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
        versionName.setOnTouchListener(new OnDoubleClickListener(this::checkUpdate));
        findViewById(R.id.logo).setOnTouchListener(new OnDoubleClickListener(() -> {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }));
        setListener();

        searchDialog = new SearchFragment();
        homeFragment = new HomeFragment();
        FragmentUtils.add(getSupportFragmentManager(), homeFragment, R.id.container_fragment);
//        RootUtils.upgradeRootPermission(getPackageCodePath());
        HeartBeatUtils.getInstance().start();
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
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    searchMusic(editText.getText().toString());
                }
            }
        });
        editText.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMusic(editText.getText().toString());
                handled = true;
            }
            return handled;
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        homeFragment.refreshData();
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
     * 检测更新
     */
    private void checkUpdate() {
        HttpServiceIml.checkUpdate().subscribe(new HttpResultSubscriber<VersionBO>() {
            @Override
            public void onSuccess(VersionBO versionBO) {
                if (StringUtils.isEmpty(versionBO.getLatest_version())) {
                    ToastUtils.showShort("已是最新版本");
                    return;
                }
                new UpdateUtils().checkUpdate(MainActivity.this, versionBO);
            }

            @Override
            public void onFiled(String message) {
                ToastUtils.showShort(message);
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