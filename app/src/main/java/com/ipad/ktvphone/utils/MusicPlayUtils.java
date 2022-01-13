package com.ipad.ktvphone.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ipad.ktvphone.entity.MusicBo;

import java.io.IOException;

/**
 * 音乐播放功能
 */
public class MusicPlayUtils {


    private static MusicPlayUtils playUtils;

    private MediaPlayer mediaPlayer;

    private MusicBo musicBo;

    public static MusicPlayUtils getInstance() {
        if (playUtils == null) {
            playUtils = new MusicPlayUtils();
        }
        return playUtils;
    }

    private MusicPlayUtils() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }


    public void startPlay(MusicBo musicBo, OnMusicFinishListener listener) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            stopPlay();
            if (listener != null) {
                listener.onFinish(musicBo);
            }
        }
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        try {
            if (StringUtils.isEmpty(musicBo.play_url)) {
                ToastUtils.showShort("播放地址为空！");
                return;
            }
            new AudioMngHelper(AppManager.getAppManager().curremtActivity())
                    .setVoice100(musicBo.volume);
            this.musicBo = musicBo;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicBo.play_url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                LogUtils.e("播放完成");
                stopPlay();
                if (listener != null) {
                    listener.onFinish(musicBo);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public MusicBo getPlayingMusic() {
        return musicBo;
    }

    public void stopPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release(); //切记一定要release
            mediaPlayer = null;
            musicBo = null;
        }
    }

    public interface OnMusicFinishListener {

        void onFinish(MusicBo musicBo);
    }
}
