package com.example.hdvideoplayer.model;

import java.util.ArrayList;

public class VideoModel {
    String FolderName;
    ArrayList<String> VideoPath;
    ArrayList<String> title;

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public ArrayList<String> getVideoPath() {
        return VideoPath;
    }

    public void setVideoPath(ArrayList<String> videoPath) {
        VideoPath = videoPath;
    }

    public ArrayList<String> getTitle() {
        return title;
    }

    public void setTitle(ArrayList<String> title) {
        this.title = title;
    }
}