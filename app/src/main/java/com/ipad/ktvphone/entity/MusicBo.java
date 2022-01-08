package com.ipad.ktvphone.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicBo implements Parcelable {

    public String song_id;

    public String song_name;

    public String song_cover;

    public String singer_name;

    public String play_url;

    protected MusicBo(Parcel in) {
        song_id = in.readString();
        song_name = in.readString();
        song_cover = in.readString();
        singer_name = in.readString();
        play_url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(song_id);
        dest.writeString(song_name);
        dest.writeString(song_cover);
        dest.writeString(singer_name);
        dest.writeString(play_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MusicBo> CREATOR = new Creator<MusicBo>() {
        @Override
        public MusicBo createFromParcel(Parcel in) {
            return new MusicBo(in);
        }

        @Override
        public MusicBo[] newArray(int size) {
            return new MusicBo[size];
        }
    };
}
