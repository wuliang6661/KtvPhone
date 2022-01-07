package com.ipad.ktvphone.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ipad.ktvphone.R;
import com.ipad.ktvphone.api.HttpResultSubscriber;
import com.ipad.ktvphone.api.HttpServiceIml;
import com.ipad.ktvphone.base.BaseFragment;
import com.ipad.ktvphone.entity.PlayListBO;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGRecycleViewAdapter;
import com.ipad.ktvphone.weight.lgrecycleadapter.LGViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        setRankingAdapter();
        getPlayList();
    }


    private void initView() {
        timeRecycle = rootView.findViewById(R.id.time_recycle);
        rankList = rootView.findViewById(R.id.rank_list);
        dataList1 = rootView.findViewById(R.id.data_list1);
        dataList2 = rootView.findViewById(R.id.data_list2);
        dataList3 = rootView.findViewById(R.id.data_list3);
        timeRecycle.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        timeRecycle.setNestedScrollingEnabled(false);
        rankList.setLayoutManager(new LinearLayoutManager(getActivity()));

        dataList1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        dataList2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        dataList3.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
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


    private void setRankingAdapter() {
        List<String> sss = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            sss.add("1");
        }
        LGRecycleViewAdapter<String> adapter = new LGRecycleViewAdapter<String>(sss) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.item_ranking_music;
            }

            @Override
            public void convert(LGViewHolder holder, String s, int position) {

            }
        };
        rankList.setAdapter(adapter);
    }


    private void setRecommendAdapter(PlayListBO listBO) {
        dataList1.setAdapter(new RecommendAdapter(listBO.data1));
        dataList2.setAdapter(new RecommendAdapter(listBO.data2));
        dataList3.setAdapter(new RecommendAdapter(listBO.data3));
    }
}
