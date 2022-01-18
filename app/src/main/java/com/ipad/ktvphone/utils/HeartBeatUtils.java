package com.ipad.ktvphone.utils;

import com.ipad.ktvphone.api.HttpResultSubscriber;
import com.ipad.ktvphone.api.HttpServiceIml;
import com.ipad.ktvphone.entity.VersionBO;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HeartBeatUtils {


    private static HeartBeatUtils utils;
    private static Subscription subscription;

    public static HeartBeatUtils getInstance() {
        if (utils == null) {
            utils = new HeartBeatUtils();
        }
        return utils;
    }


    public void start() {
        if (subscription != null) {
            return;
        }
        subscription = Observable.interval(0, 2, TimeUnit.MINUTES)
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
            }

            @Override
            public void onFiled(String message) {

            }
        });
    }
}
