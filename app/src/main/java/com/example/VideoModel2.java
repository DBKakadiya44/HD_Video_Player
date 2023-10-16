package com.example;

import java.io.Serializable;

public class VideoModel2 implements Serializable {
    public String ImageName;
    public String Imagepath;
    public String addeddate;
    public String albumid;
    public String albumname;
    public String albumtype;
    public String createddate;
    public String duration;
    public long durationmilisecond;
    public String resolution;
    public int rotate;
    public int type;

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String videoid;

    public int getViewtype() {
        return viewtype;
    }

    public void setViewtype(int viewtype) {
        this.viewtype = viewtype;
    }

    public int viewtype;

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getImageName() {
        return this.ImageName;
    }

    public void setImageName(String str) {
        this.ImageName = str;
    }

    public long getDurationmilisecond() {
        return this.durationmilisecond;
    }

    public void setDurationmilisecond(long j) {
        this.durationmilisecond = j;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public String getResolution() {
        return this.resolution;
    }

    public void setResolution(String str) {
        this.resolution = str;
    }

    public String getAlbumtype() {
        return this.albumtype;
    }

    public void setAlbumtype(String str) {
        this.albumtype = str;
    }

    public String getAlbumid() {
        return this.albumid;
    }

    public void setAlbumid(String str) {
        this.albumid = str;
    }

    public String getAlbumname() {
        return this.albumname;
    }

    public void setAlbumname(String str) {
        this.albumname = str;
    }

    public String getCreateddate() {
        return this.createddate;
    }

    public void setCreateddate(String str) {
        this.createddate = str;
    }

    public String getAddeddate() {
        return this.addeddate;
    }

    public void setAddeddate(String str) {
        this.addeddate = str;
    }

    public int getRotate() {
        return this.rotate;
    }

    public void setRotate(int i) {
        this.rotate = i;
    }

    public String getImagepath() {
        return this.Imagepath;
    }

    public void setImagepath(String str) {
        this.Imagepath = str;
    }
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof VideoModel2)) {
            return false;
        }
        return getImagepath().equals(((VideoModel2) obj).getImagepath());
    }
}
