package com.example.demomusicplayerver101.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Song implements Serializable {
    @SerializedName("songName")
    private String songName;
    @SerializedName("artist")
    private String artist;
    @SerializedName("image")
    private String image;
    @SerializedName("songPath")
    private String songPath;

    public Song() {
    }

    public Song(String songName, String artist, String image, String songPath) {
        this.songName = songName;
        this.artist = artist;
        this.image = image;
        this.songPath = songPath;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }
}
