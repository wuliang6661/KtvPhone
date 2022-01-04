package com.ipad.ktvphone;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.FragmentUtils;
import com.ipad.ktvphone.api.HttpResultSubscriber;
import com.ipad.ktvphone.api.HttpServiceIml;
import com.ipad.ktvphone.entity.VersionBO;
import com.ipad.ktvphone.ui.HomeFragment;
import com.ipad.ktvphone.utils.RootUtils;
import com.ipad.ktvphone.utils.UpdateUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        FragmentUtils.replace(getSupportFragmentManager(), new HomeFragment(), R.id.container_fragment);
        RootUtils.upgradeRootPermission(getPackageCodePath());
        startHeartbeat();
    }


    /**
     * 建立心跳连接
     */
    private void startHeartbeat() {
        Observable.interval(0, 5000, TimeUnit.MILLISECONDS)
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


    private void requestHeart() {
        HttpServiceIml.postHeartbeat().subscribe(new HttpResultSubscriber<VersionBO>() {
            @Override
            public void onSuccess(VersionBO s) {
                new UpdateUtils().checkUpdate(MainActivity.this, s);
            }

            @Override
            public void onFiled(String message) {

            }
        });
    }


    private void requestMsg() {
//        Observable.interval(0, retryInterval, TimeUnit.MILLISECONDS)
//                .take(retryCount)
//                .observeOn(Schedulers.io())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .flatMap((Function<Long, DsmObservable<CMSResultBean<QueryOrderResultBean>>>) aLong -> cmsService.queryCreateOrderResult(id))
//                .takeUntil(rsp -> {
//                    QueryOrderResultBean item = rsp.item;
//                    return item.status != 1;
//                })
//                .subscribe(new Observer<>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        add(d);
//                    }
//
//                    @Override
//                    public void onNext(@NonNull CMSResultBean<QueryOrderResultBean> rsp) {
//                        QueryOrderResultBean item = rsp.item;
//                        if (item.status == 2 && !TextUtils.isEmpty(item.id)) {
//                            createOrderSuccess(item.id);
//                            mView.cancelProgressDialog();
//                        } else if (!TextUtils.isEmpty(item.errMsg)) {
//                            mView.cancelProgressDialog();
//                            if (item.errorCode == 12114) {   //存在未支付的订单
//                                mView.existDrugOrder(item.errMsg);
//                                return;
//                            } else if (item.errorCode == 10020) {   //药品状态存在变化/下架/库存不足
//                                mView.showToastMessage("药品状态存在变化");
//                                getSkuList();
//                                return;
//                            }
//                            mView.showToastMessage(item.errMsg);
//                        }
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        mView.cancelProgressDialog();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        mView.cancelProgressDialog();
//                    }
//                });
    }
}