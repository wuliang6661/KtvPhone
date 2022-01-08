package com.ipad.ktvphone.entity.event;

import com.ipad.ktvphone.entity.MusicBo;

import java.util.List;

public class SearchMusicEvent {

    public SearchMusicEvent(String keyWord, List<MusicBo> musicBos) {
        this.keyWord = keyWord;
        this.musicBos = musicBos;
    }

    public String keyWord;

    public List<MusicBo> musicBos;
}
