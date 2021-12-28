package com.ipad.ktvphone.api;

import com.ipad.ktvphone.entity.BaseResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wuliang on 2017/3/9.
 * <p>
 * 此处存放后台服务器的所有接口数据
 */

public interface HttpService {

    String URL = "http://api.platform.yinghezhong.com/";   //测试服
//    String URL = "http://mapi.open.yinghezhong.com/";  //正式环境


    /**
     * 心跳连接
     */
    @FormUrlEncoded
    @POST("/on_demand_songs/api/v1/heartbeat")
    Observable<BaseResult<String>> postHeartbeat(@Field("data") String data, @Field("time") long time);


    /**
     * 查询正在播放的歌曲信息
     */
    @GET("/on_demand_songs/api/v1/get_play_song")
    Observable<BaseResult<String>> getPlaySong();


    /**
     * 获取每个小时的排位状态
     */
    @GET("/on_demand_songs/api/v1/get_queue_status")
    Observable<BaseResult<String>> getQueueStatus();


    /**
     * 获取歌曲列表1
     */
    @GET("/on_demand_songs/api/v1/get_songs_list")
    Observable<BaseResult<String>> getSongsList();


    /**
     * 获取歌曲列表2
     */
    @GET("/on_demand_songs/api/v1/get_songlist_list")
    Observable<BaseResult<String>> getSongListList();

    /**
     * 获取点歌弹窗内容
     */
    @GET("/on_demand_songs/api/v1/get_popups_info")
    Observable<BaseResult<String>> getPopupsInfo();

    /**
     * 点歌表单提交
     */
    @FormUrlEncoded
    @POST("/on_demand_songs/api/v1/on_demand_song_commit")
    Observable<BaseResult<String>> onDemandSongCommit(@Field("song_id") String song_id);


    /**
     * 获取支付结果
     *
     * 1、“点歌表单提交”接口返回二维码展示到前端后调用此接口来获取用户支付是否成功结果；
     * 2、此接口等待时间较长，超时后默认支付失败
     */
    @GET("/on_demand_songs/api/v1/get_pay_result")
    Observable<BaseResult<String>> getPayResult();


}
