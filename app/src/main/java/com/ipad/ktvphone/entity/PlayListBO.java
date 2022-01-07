package com.ipad.ktvphone.entity;

import java.util.List;

//歌单
public class PlayListBO {


    public List<Data1Bean> data1;
    public List<Data1Bean> data2;
    public List<Data1Bean> data3;

    public static class Data1Bean {
        public String songlist_id;
        public String songlist_name;
        public String songlist_cover;
    }
}
