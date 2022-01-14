package com.ipad.ktvphone.api;

import com.blankj.utilcode.util.AppUtils;
import com.ipad.ktvphone.entity.MusicBo;
import com.ipad.ktvphone.entity.OrderBO;
import com.ipad.ktvphone.entity.PayResultBo;
import com.ipad.ktvphone.entity.PlayListBO;
import com.ipad.ktvphone.entity.VersionBO;

import java.util.List;
import java.util.UUID;

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
        return getService().postHeartbeat("online", System.currentTimeMillis(), AppUtils.getAppVersionCode()).compose(RxResultHelper.httpRusult());
    }


    /**
     * 查询正在播放的歌曲信息
     */
    public static Observable<MusicBo> getPlaySong() {
        return getService().getPlaySong().compose(RxResultHelper.httpRusult());
    }


    /**
     * 搜索歌曲
     */
    public static Observable<List<MusicBo>> searchMusic(int limit, String keyWord) {
        return getService().searchMusic(limit, 20, keyWord).compose(RxResultHelper.httpRusult());
    }


    /**
     * 获取歌单内部歌曲
     */
    public static Observable<List<MusicBo>> getSongsList(int limit, String keyWord) {
        return getService().getSongsList(limit, 20, keyWord).compose(RxResultHelper.httpRusult());
    }


    /**
     * 获取歌单列表
     */
    public static Observable<PlayListBO> getSongListList() {
        return getService().getSongListList().compose(RxResultHelper.httpRusult());
    }

    /**
     * 获取排行榜歌曲
     */
    public static Observable<List<MusicBo>> getTopSongs(int limit, int from) {
        return getService().getTopSongs(limit, 20).compose(RxResultHelper.httpRusult());
    }


    /**
     * 生成支付订单
     */
    public static Observable<OrderBO> createPayOrder(MusicBo musicBo) {
        return getService().onDemandSongCommit(UUID.randomUUID().toString(),
                musicBo.song_id,
                musicBo.song_name,
                musicBo.song_cover,
                musicBo.singer_name,
                musicBo.play_url).compose(RxResultHelper.httpRusult());
    }


    /**
     * 获取支付结果
     * <p>
     * 1、“点歌表单提交”接口返回二维码展示到前端后调用此接口来获取用户支付是否成功结果；
     * 2、此接口等待时间较长，超时后默认支付失败
     */
    public static Observable<PayResultBo> getPayResult(String orderId) {
        return getService().getPayResult(orderId).compose(RxResultHelper.httpRusult());
    }

}
