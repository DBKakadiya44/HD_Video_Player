package com.example.hdvideoplayer.model;

public class Favorite
{
    String title,path,size,time;

    public Favorite(String title, String path, String size, String time) {
        this.title = title;
        this.path = path;
        this.size = size;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
