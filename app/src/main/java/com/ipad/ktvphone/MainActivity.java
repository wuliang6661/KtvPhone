package com.ipad.ktvphone;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    private void requestMsg(){
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