package com.ipad.ktvphone.api;

import com.ipad.ktvphone.entity.BaseResult;
import com.ipad.ktvphone.entity.MusicBo;
import com.ipad.ktvphone.entity.OrderBO;
import com.ipad.ktvphone.entity.PayResultBo;
import com.ipad.ktvphone.entity.PlayListBO;
import com.ipad.ktvphone.entity.VersionBO;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wuliang on 2017/3/9.
 * <p>
 * 此处存放后台服务器的所有接口数据
 */

public interface HttpService {

    String URL = "https://dsongs.xuju.club/";   //测试服
//    String URL = "http://mapi.open.yinghezhong.com/";  //正式环境


    /**
     * 心跳连接
     */
    @FormUrlEncoded
    @POST("/on_demand_songs/api/v1/heartbeat")
    Observable<BaseResult<VersionBO>> postHeartbeat(@Field("data") String data, @Field("time") long time, @Field("version") int version);


    /**
     * 查询正在播放的歌曲信息
     */
    @GET("/on_demand_songs/api/v1/get_play_song")
    Observable<BaseResult<MusicBo>> getPlaySong();

    /**
     * 获取排行榜歌曲
     */
    @GET("/on_demand_songs/api/v1/search_top_songs")
    Observable<BaseResult<List<MusicBo>>> getTopSongs(@Query("from") int from, @Query("limit") int limit);


    /**
     * 搜索歌曲
     */
    @GET("/on_demand_songs/api/v1/search_songs")
    Observable<BaseResult<List<MusicBo>>> searchMusic(@Query("from") int from, @Query("limit") int limit, @Query("keyword") String keyword);


    /**
     * 获取歌单内部歌曲
     */
    @GET("/on_demand_songs/api/v1/get_songlist_songs")
    Observable<BaseResult<List<MusicBo>>> getSongsList(@Query("from") int from, @Query("limit") int limit, @Query("songlist_id") String songlist_id);


    /**
     * 获取歌单列表
     */
    @GET("/on_demand_songs/api/v1/get_songlist_list")
    Observable<BaseResult<PlayListBO>> getSongListList();


    /**
     * 生成支付订单
     */
    @FormUrlEncoded
    @POST("/on_demand_songs/api/v1/create_pay_order")
    Observable<BaseResult<OrderBO>> onDemandSongCommit(@Field("request_id") String request_id,
                                                       @Field("song_id") String song_id,
                                                       @Field("song_name") String song_name,
                                                       @Field("song_cover") String song_cover,
                                                       @Field("singer_name") String singer_name,
                                                       @Field("play_url") String play_url);


    /**
     * 获取支付结果
     * <p>
     * 1、“点歌表单提交”接口返回二维码展示到前端后调用此接口来获取用户支付是否成功结果；
     * 2、此接口等待时间较长，超时后默认支付失败
     */
    @GET("/on_demand_songs/api/v1/get_pay_result")
    Observable<BaseResult<PayResultBo>> getPayResult(@Query("order_id") String order_id);


}
