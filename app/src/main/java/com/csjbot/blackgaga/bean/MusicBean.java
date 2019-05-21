package com.csjbot.blackgaga.bean;

import java.io.Serializable;

/**
 * 音乐
 * Created by jingwc on 2017/12/13.
 */

public class MusicBean implements Serializable {

    public static final String service = "MUSIC";

    // 专辑
    private String album;

    // 专辑图标
    private String albumIcon;

    // 演唱人
    private String artist;

    // 分类
    private String category;

    // id
    private String id;

    // 音乐播放地址
    private String musicUrl;

    // 歌曲名称
    private String songName;

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumIcon() {
        return albumIcon;
    }

    public void setAlbumIcon(String albumIcon) {
        this.albumIcon = albumIcon;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}
