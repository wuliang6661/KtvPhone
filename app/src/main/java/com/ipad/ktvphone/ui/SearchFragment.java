package com.ipad.ktvphone.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ipad.ktvphone.R;
import com.ipad.ktvphone.base.BaseFragment;
import com.ipad.ktvphone.entity.MusicBo;
import com.ipad.ktvphone.entity.event.HideSearchEvent;
import com.ipad.ktvphone.entity.event.SearchMusicEvent;
import com.ipad.ktvphone.utils.CreateOrderUtils;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGRecycleViewAdapter;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFragment extends BaseFragment {

    private List<MusicBo> musicBos;
    private View dialogView;
    private RecyclerView searchMusicList;
    private String keyWord;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogView = inflater.inflate(R.layout.dialog_search_layout, container, false);
        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);
        initView();
    }


    private void initView() {
        View dialogBgView = dialogView.findViewById(R.id.bg_view);
        searchMusicList = dialogView.findViewById(R.id.search_music_list);
        TextView tvAllSearchMusic = dialogView.findViewById(R.id.all_search_music);

        searchMusicList.setLayoutManager(new LinearLayoutManager(getActivity()));
        dialogBgView.setOnClickListener(v -> EventBus.getDefault().post(new HideSearchEvent()));
        tvAllSearchMusic.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 0);
            bundle.putString("keyWord", keyWord);
            gotoActivity(MusicListActivity.class, bundle, false);
            EventBus.getDefault().post(new HideSearchEvent());
        });
    }


    private void setAdapter() {
        LGRecycleViewAdapter<MusicBo> adapter = new LGRecycleViewAdapter<MusicBo>(musicBos) {

            @Override
            public int getItemCount() {
                return Math.min(musicBos.size(), 5);
            }

            @Override
            public int getLayoutId(int viewType) {
                return R.layout.item_search_music;
            }

            @Override
            public void convert(LGViewHolder holder, MusicBo musicBo, int position) {
                holder.setImageUrl(getActivity(), R.id.music_img, musicBo.song_cover);
                holder.setText(R.id.music_name, musicBo.song_name);
                holder.setText(R.id.music_person, musicBo.singer_name);
                holder.itemView.setOnClickListener(v -> CreateOrderUtils.createOrder(musicBo));
            }
        };
        searchMusicList.setAdapter(adapter);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SearchMusicEvent event) {
        this.musicBos = event.musicBos;
        keyWord = event.keyWord;
        setAdapter();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
