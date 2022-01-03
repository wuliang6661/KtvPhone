package com.ipad.ktvphone.ui;

import com.ipad.ktvphone.R;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGRecycleViewAdapter;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGViewHolder;

import java.util.List;

public class RecommendAdapter extends LGRecycleViewAdapter<String> {


    public RecommendAdapter(List<String> dataList) {
        super(dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_recommont_music;
    }

    @Override
    public void convert(LGViewHolder holder, String s, int position) {

    }
}
