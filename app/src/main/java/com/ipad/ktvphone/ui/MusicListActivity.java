package com.ipad.ktvphone.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
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
import com.ipad.ktvphone.weight.OnRecyclerViewScrollListener;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGRecycleViewAdapter;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MusicListActivity extends BaseActivity {

    private EditText editText;
    private RecyclerView musicList;

    List<MusicBo> musicData;
    //1 为排行榜搜索， 0 是输入框搜索
    private int type;

    private String songlist_id;

    LGRecycleViewAdapter<MusicBo> adapter;

    private int from = 0;

    @Override
    protected int getLayout() {
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.act_music_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editText = findViewById(R.id.edit_view);
        musicList = findViewById(R.id.music_list);
        findViewById(R.id.go_back).setOnClickListener(v -> finish());
        musicList.setLayoutManager(new LinearLayoutManager(this));
        setListener();
        type = getIntent().getExtras().getInt("type");
        musicData = new ArrayList<>();
        if (type == 0) {
            String keyWord = getIntent().getExtras().getString("keyWord");
            editText.setText(keyWord);
            searchMusic(keyWord);
        } else {
            songlist_id = getIntent().getExtras().getString("songlist_id");
            getSongList(songlist_id);
        }
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
                    from = 0;
                    type = 0;
                    searchMusic(editText.getText().toString());
                }
            }
        });
        editText.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                from = 0;
                type = 0;
                searchMusic(editText.getText().toString());
                handled = true;
            }
            return handled;
        });
        musicList.addOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onBottom() {
                from++;
                if (type == 0) {
                    searchMusic(editText.getText().toString());
                } else {
                    getSongList(songlist_id);
                }
            }
        });
    }

    /**
     * 搜索歌曲
     */
    private void searchMusic(String keyWord) {
        showProgress();
        HttpServiceIml.searchMusic(from, keyWord).subscribe(new HttpResultSubscriber<List<MusicBo>>() {
            @Override
            public void onSuccess(List<MusicBo> musicBos) {
                stopProgress();
                if (from == 0) {
                    musicData.clear();
                }
                if (from != 0 && musicBos.isEmpty()) {
                    from--;
                }
                musicData.addAll(musicBos);
                setAdapter();
            }

            @Override
            public void onFiled(String message) {
                if (from != 0) {
                    from--;
                }
                stopProgress();
                showToast(message);
            }
        });
    }


    /**
     * 获取歌单内部歌曲列表
     */
    private void getSongList(String songlist_id) {
        HttpServiceIml.getSongsList(from, songlist_id).subscribe(new HttpResultSubscriber<List<MusicBo>>() {
            @Override
            public void onSuccess(List<MusicBo> s) {
                if (from == 0) {
                    musicData.clear();
                }
                if (from != 0 && s.isEmpty()) {
                    from--;
                }
                musicData.addAll(s);
                setAdapter();
            }

            @Override
            public void onFiled(String message) {
                if (from != 0) {
                    from--;
                }
                showToast(message);
            }
        });
    }


    private void setAdapter() {
        if (adapter != null) {
            adapter.setData(musicData);
            return;
        }
        adapter = new LGRecycleViewAdapter<MusicBo>(musicData) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.item_music;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void convert(LGViewHolder holder, MusicBo musicBo, int position) {
                TextView positionNum = (TextView) holder.getView(R.id.list_num);
                positionNum.setText(getNum(holder.getAbsoluteAdapterPosition()));
                positionNum.setTextColor(Color.parseColor("#ffffff"));
                switch (position) {
                    case 0:
                        positionNum.setBackground(ContextCompat.getDrawable(MusicListActivity.this, R.drawable.slide_da5d6f_conner_2_5_dp));
                        break;
                    case 1:
                        positionNum.setBackground(ContextCompat.getDrawable(MusicListActivity.this, R.drawable.slide_55c0bb_conner_2_5_dp));
                        break;
                    case 2:
                        positionNum.setBackground(ContextCompat.getDrawable(MusicListActivity.this, R.drawable.slide_8980ce_conner_2_5_dp));
                        break;
                    default:
                        positionNum.setTextColor(Color.parseColor("#8B8B8B"));
                        positionNum.setBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                }
                holder.setText(R.id.music_name, musicBo.song_name);
                holder.setText(R.id.music_person, musicBo.singer_name);
                Glide.with(MusicListActivity.this).load(musicBo.song_cover)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into((ImageView) holder.getView(R.id.music_img));
                holder.getView(R.id.create_order).setOnClickListener(v -> {
                    MusicPlayUtils.getInstance().stopPlay();
                    adapter.notifyDataSetChanged();
                    CreateOrderUtils.createOrder(musicBo);
                });
                if (MusicPlayUtils.getInstance().getPlayingMusic() != null &&
                        MusicPlayUtils.getInstance().getPlayingMusic().song_id.equals(musicBo.song_id)) {
                    holder.setImageResurce(R.id.music_play_img, R.mipmap.stop_music);
                } else {
                    holder.setImageResurce(R.id.music_play_img, R.mipmap.music_start);
                }
                holder.getView(R.id.play_music).setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayUtils.getInstance().stopPlay();
    }

    private String getNum(int position) {
        int num = position + 1;
        return num < 10 ? "0" + num : num + "";
    }
}
