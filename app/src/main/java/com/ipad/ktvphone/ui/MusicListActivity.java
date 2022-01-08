package com.ipad.ktvphone.ui;

import android.graphics.Color;
import android.os.Bundle;
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
import com.ipad.ktvphone.weight.lgrecycleadapter.LGRecycleViewAdapter;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MusicListActivity extends BaseActivity {

    private EditText editText;
    private RecyclerView musicList;

    List<MusicBo> musicData;

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
        String keyWord = getIntent().getExtras().getString("keyWord");
        editText.setText(keyWord);
        searchMusic(keyWord);
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
        HttpServiceIml.searchMusic(1, keyWord).subscribe(new HttpResultSubscriber<List<MusicBo>>() {
            @Override
            public void onSuccess(List<MusicBo> musicBos) {
                musicData = musicBos;
                setAdapter();
            }

            @Override
            public void onFiled(String message) {
                showToast(message);
            }
        });
    }


    private void setAdapter() {
        LGRecycleViewAdapter<MusicBo> adapter = new LGRecycleViewAdapter<MusicBo>(musicData) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.item_music;
            }

            @Override
            public void convert(LGViewHolder holder, MusicBo musicBo, int position) {
                TextView positionNum = (TextView) holder.getView(R.id.list_num);
                positionNum.setText(getNum(position));
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
            }
        };
        musicList.setAdapter(adapter);
    }


    private String getNum(int position) {
        int num = position + 1;
        return num < 10 ? "0" + num : num + "";
    }
}
