package com.ipad.ktvphone.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ipad.ktvphone.R;
import com.ipad.ktvphone.api.HttpResultSubscriber;
import com.ipad.ktvphone.api.HttpServiceIml;
import com.ipad.ktvphone.base.BaseFragment;
import com.ipad.ktvphone.base.MyApplication;
import com.ipad.ktvphone.entity.MusicBo;
import com.ipad.ktvphone.entity.PlayListBO;
import com.ipad.ktvphone.utils.CreateOrderUtils;
import com.ipad.ktvphone.utils.MusicPlayUtils;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGRecycleViewAdapter;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页播放的fragment
 */
public class HomeFragment extends BaseFragment {

    private RecyclerView timeRecycle;
    private RecyclerView rankList;
    private View rootView;

    private RecyclerView dataList1;
    private RecyclerView dataList2;
    private RecyclerView dataList3;

    private FrameLayout playingMusicBg;
    private ImageView playingMusicImg;
    private TextView playingMusicName;
    private TextView playingMusicPerson;

    LGRecycleViewAdapter<MusicBo> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fra_home, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initView();
        setTimeAdapter();
        getPlayList();
        lunxunPlaying();
        getTopSongs();
    }


    private void initView() {
        timeRecycle = rootView.findViewById(R.id.time_recycle);
        rankList = rootView.findViewById(R.id.rank_list);
        dataList1 = rootView.findViewById(R.id.data_list1);
        dataList2 = rootView.findViewById(R.id.data_list2);
        dataList3 = rootView.findViewById(R.id.data_list3);
        playingMusicBg = rootView.findViewById(R.id.playing_music_bg);
        playingMusicImg = rootView.findViewById(R.id.playing_music_img);
        playingMusicName = rootView.findViewById(R.id.playing_music_name);
        playingMusicPerson = rootView.findViewById(R.id.playing_music_person);
        rootView.findViewById(R.id.all_rank).setOnClickListener(v -> gotoActivity(RankingActivity.class, false));
        timeRecycle.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        timeRecycle.setNestedScrollingEnabled(false);
        rankList.setLayoutManager(new LinearLayoutManager(getActivity()));

        dataList1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        dataList2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        dataList3.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }


    private void lunxunPlaying() {
        Observable.interval(0, 2, TimeUnit.SECONDS)
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
                        getPlayMusic();
                        long localRefreshTime = MyApplication.spUtils.getLong("localRefreshTime");
                        if (System.currentTimeMillis() - localRefreshTime >= (2 * 60 * 60 * 1000)) {
                            MyApplication.spUtils.put("localRefreshTime", System.currentTimeMillis());
                            getPlayList();
                            getTopSongs();
                        }
                    }
                });
    }


    /**
     * 获取歌单列表
     */
    private void getPlayList() {
        HttpServiceIml.getSongListList().subscribe(new HttpResultSubscriber<PlayListBO>() {
            @Override
            public void onSuccess(PlayListBO s) {
                setRecommendAdapter(s);
            }

            @Override
            public void onFiled(String message) {
                showToast(message);
            }
        });
    }

    /**
     * 获取正在播放的歌曲
     */
    private void getPlayMusic() {
        HttpServiceIml.getPlaySong().subscribe(new HttpResultSubscriber<MusicBo>() {
            @Override
            public void onSuccess(MusicBo musicBo) {
                if (musicBo == null || StringUtils.isEmpty(musicBo.song_name)) {
                    playingMusicName.setText("歌曲名：暂无");
                    playingMusicPerson.setText("歌手：暂无");
                    playingMusicImg.setImageResource(R.mipmap.default_img);
                } else {
                    Glide.with(getActivity()).load(musicBo.song_cover)
                            .placeholder(R.mipmap.default_img)
                            .error(R.mipmap.default_img)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(playingMusicImg);
                    playingMusicName.setText("歌曲名：" + musicBo.song_name);
                    playingMusicPerson.setText("歌手：" + musicBo.singer_name);
                    if (playingMusicBg.getAnimation() == null || !playingMusicBg.getAnimation().hasStarted()) {
                        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
                        playingMusicBg.startAnimation(animation);
                    }
                }
            }

            @Override
            public void onFiled(String message) {
                showToast(message);
            }
        });
    }


    /**
     * 获取排行榜歌曲
     */
    private void getTopSongs() {
        HttpServiceIml.getTopSongs(0).subscribe(new HttpResultSubscriber<List<MusicBo>>() {
            @Override
            public void onSuccess(List<MusicBo> musicBos) {
                setRankingAdapter(musicBos);
            }

            @Override
            public void onFiled(String message) {
                showToast(message);
            }
        });
    }


    private void setTimeAdapter() {
        List<String> sss = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            sss.add("1");
        }
        LGRecycleViewAdapter<String> adapter = new LGRecycleViewAdapter<String>(sss) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.item_time;
            }

            @Override
            public void convert(LGViewHolder holder, String s, int position) {

            }
        };
        timeRecycle.setAdapter(adapter);
    }


    private void setRankingAdapter(List<MusicBo> musicBos) {
        adapter = new LGRecycleViewAdapter<MusicBo>(musicBos) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.item_ranking_music;
            }

            @Override
            public void convert(LGViewHolder holder, MusicBo musicBo, int position) {
                TextView positionNum = (TextView) holder.getView(R.id.list_num);
                positionNum.setText(getNum(position));
                positionNum.setTextColor(Color.parseColor("#ffffff"));
                switch (position) {
                    case 0:
                        positionNum.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.slide_da5d6f_conner_2_5_dp));
                        break;
                    case 1:
                        positionNum.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.slide_55c0bb_conner_2_5_dp));
                        break;
                    case 2:
                        positionNum.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.slide_8980ce_conner_2_5_dp));
                        break;
                    default:
                        positionNum.setTextColor(Color.parseColor("#8B8B8B"));
                        positionNum.setBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                }
                holder.setText(R.id.music_name, musicBo.song_name);
                holder.setText(R.id.music_person, musicBo.singer_name);
                holder.setText(R.id.play_num, musicBo.play_count);
                Glide.with(getActivity()).load(musicBo.song_cover)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into((ImageView) holder.getView(R.id.music_img));
                holder.getView(R.id.create_order).setOnClickListener(v -> CreateOrderUtils.createOrder(musicBo));
                if (MusicPlayUtils.getInstance().getPlayingMusic() != null &&
                        MusicPlayUtils.getInstance().getPlayingMusic().song_id.equals(musicBo.song_id)) {
                    holder.setImageResurce(R.id.music_play_img, R.mipmap.stop_music);
                } else {
                    holder.setImageResurce(R.id.music_play_img, R.mipmap.music_start);
                }
                holder.getView(R.id.play_music).setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(View v) {
                        if (MusicPlayUtils.getInstance().getPlayingMusic() != null &&
                                MusicPlayUtils.getInstance().getPlayingMusic().song_id.equals(musicBo.song_id)) {
                            MusicPlayUtils.getInstance().stopPlay();
                        } else {
                            MusicPlayUtils.getInstance().startPlay(musicBo, new MusicPlayUtils.OnMusicFinishListener() {
                                @Override
                                public void onFinish(MusicBo musicBo) {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        rankList.setAdapter(adapter);
    }


    private void setRecommendAdapter(PlayListBO listBO) {
        dataList1.setAdapter(new RecommendAdapter(listBO.data1));
        dataList2.setAdapter(new RecommendAdapter(listBO.data2));
        dataList3.setAdapter(new RecommendAdapter(listBO.data3));
    }


    private String getNum(int position) {
        int num = position + 1;
        return num < 10 ? "0" + num : num + "";
    }
}
