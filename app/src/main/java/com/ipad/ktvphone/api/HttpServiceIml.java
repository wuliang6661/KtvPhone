package com.ipad.ktvphone.api;

import com.ipad.ktvphone.entity.PlayListBO;
import com.ipad.ktvphone.entity.VersionBO;

import rx.Observable;

/**
 * Created by wuliang on 2017/4/19.
 * <p>
 * 所有网络请求方法
 */

public class HttpServiceIml {

    static HttpService service;

    /**
     * 获取代理对象
     *
     * @return
     */
    public static HttpService getService() {
        if (service == null)
            service = ApiManager.getInstance().configRetrofit(HttpService.class, HttpService.URL);
        return service;
    }


    /**
     * 心跳连接
     */
    public static Observable<VersionBO> postHeartbeat() {
        return getService().postHeartbeat("online", System.currentTimeMillis()).compose(RxResultHelper.httpRusult());
    }


    /**
     * 查询正在播放的歌曲信息
     */
    public static Observable<String> getPlaySong() {
        return getService().getPlaySong().compose(RxResultHelper.httpRusult());
    }


    /**
     * 获取每个小时的排位状态
     */
    public static Observable<String> getQueueStatus() {
        return getService().getQueueStatus().compose(RxResultHelper.httpRusult());
    }


    /**
     * 获取歌曲列表1
     */
    public static Observable<String> getSongsList() {
        return getService().getSongsList().compose(RxResultHelper.httpRusult());
    }


    /**
     * 获取歌曲列表2
     */
    public static Observable<PlayListBO> getSongListList() {
        return getService().getSongListList().compose(RxResultHelper.httpRusult());
    }

    /**
     * 获取点歌弹窗内容
     */
    public static Observable<String> getPopupsInfo() {
        return getService().getPopupsInfo().compose(RxResultHelper.httpRusult());
    }

    /**
     * 点歌表单提交
     */
    public static Observable<String> onDemandSongCommit(String song_id) {
        return getService().onDemandSongCommit(song_id).compose(RxResultHelper.httpRusult());
    }


    /**
     * 获取支付结果
     * <p>
     * 1、“点歌表单提交”接口返回二维码展示到前端后调用此接口来获取用户支付是否成功结果；
     * 2、此接口等待时间较长，超时后默认支付失败
     */
    public static Observable<String> getPayResult() {
        return getService().getPayResult().compose(RxResultHelper.httpRusult());
    }

}
