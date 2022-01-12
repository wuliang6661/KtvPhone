package com.ipad.ktvphone.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ipad.ktvphone.R;
import com.ipad.ktvphone.api.HttpResultSubscriber;
import com.ipad.ktvphone.api.HttpServiceIml;
import com.ipad.ktvphone.base.BaseActivity;
import com.ipad.ktvphone.entity.MusicBo;
import com.ipad.ktvphone.utils.CreateOrderUtils;
import com.ipad.ktvphone.utils.MusicPlayUtils;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGRecycleViewAdapter;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 排行榜
 */
public class RankingActivity extends BaseActivity {


    private EditText editText;
    private RecyclerView musicList;

    List<MusicBo> musicData;


    LGRecycleViewAdapter<MusicBo> adapter;

    @Override
    protected int getLayout() {
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.act_ranking_music;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editText = findViewById(R.id.edit_view);
        musicList = findViewById(R.id.music_list);
        findViewById(R.id.go_back).setOnClickListener(v -> finish());
        musicList.setLayoutManager(new LinearLayoutManager(this));
        setListener();

        getTopSongs();
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
        showProgress();
        HttpServiceIml.searchMusic(0, keyWord).subscribe(new HttpResultSubscriber<List<MusicBo>>() {
            @Override
            public void onSuccess(List<MusicBo> musicBos) {
                stopProgress();
                musicData = musicBos;
                setAdapter();
            }

            @Override
            public void onFiled(String message) {
                stopProgress();
                showToast(message);
            }
        });
    }



    /**
     * 获取歌单内部歌曲列表
     */
    private void getTopSongs() {
        showProgress();
        HttpServiceIml.getTopSongs(0).subscribe(new HttpResultSubscriber<List<MusicBo>>() {
            @Override
            public void onSuccess(List<MusicBo> s) {
                stopProgress();
                musicData = s;
                setAdapter();
            }

            @Override
            public void onFiled(String message) {
                stopProgress();
                showToast(message);
            }
        });
    }



    private void setAdapter() {
        adapter = new LGRecycleViewAdapter<MusicBo>(musicData) {
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
                        positionNum.setBackground(ContextCompat.getDrawable(RankingActivity.this, R.drawable.slide_da5d6f_conner_2_5_dp));
                        break;
                    case 1:
                        positionNum.setBackground(ContextCompat.getDrawable(RankingActivity.this, R.drawable.slide_55c0bb_conner_2_5_dp));
                        break;
                    case 2:
                        positionNum.setBackground(ContextCompat.getDrawable(RankingActivity.this, R.drawable.slide_8980ce_conner_2_5_dp));
                        break;
                    default:
                        positionNum.setTextColor(Color.parseColor("#8B8B8B"));
                        positionNum.setBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                }
                holder.setText(R.id.music_name, musicBo.song_name);
                holder.setText(R.id.music_person, musicBo.singer_name);
                holder.setText(R.id.play_num, musicBo.play_count);
                Glide.with(RankingActivity.this).load(musicBo.song_cover)
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
        musicList.setAdapter(adapter);
    }


    private String getNum(int position) {
        int num = position + 1;
        return num < 10 ? "0" + num : num + "";
    }
}