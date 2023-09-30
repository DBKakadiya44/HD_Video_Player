package com.example.hdvideoplayer.model;

public class Video
{
    String path,title,thumbnailUri,size;

    public Video(String title, String path, String thumbnailUri,String size) {

        this.title = title;
        this.path = path;
        this.thumbnailUri = thumbnailUri;
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(String thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
