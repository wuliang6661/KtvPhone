package com.ipad.ktvphone.ui;

import com.ipad.ktvphone.R;
import com.ipad.ktvphone.entity.PlayListBO;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGRecycleViewAdapter;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGViewHolder;

import java.util.List;

public class RecommendAdapter extends LGRecycleViewAdapter<PlayListBO.Data1Bean> {


    public RecommendAdapter(List<PlayListBO.Data1Bean> dataList) {
        super(dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_recommont_music;
    }

    @Override
    public void convert(LGViewHolder holder, PlayListBO.Data1Bean s, int position) {
//       holder.setText(R.id.bofang_num,s.);
        holder.setText(R.id.play_list_name, s.songlist_name);
        holder.setImageUrl(holder.itemView.getContext(), R.id.music_img, s.songlist_cover);
    }
}
