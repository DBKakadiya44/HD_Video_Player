package com.example.hdvideoplayer;

import com.example.VideoModel2;

import java.io.Serializable;
import java.util.ArrayList;

public class FolderModel implements Serializable {
    public long CoverId;
    public String albumid;
    public String albumname;
    public String albumpath;
    public int albumphotocount;
    public String albumtype;
    public ArrayList<VideoModel2> modeldata;
    public int rotate;
    public String setaddeddate;
    public int type;



    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getAlbumtype() {
        return this.albumtype;
    }

    public void setAlbumtype(String str) {
        this.albumtype = str;
    }

    public int getRotate() {
        return this.rotate;
    }

    public void setRotate(int i) {
        this.rotate = i;
    }

    public String getSetaddeddate() {
        return this.setaddeddate;
    }

    public void setSetaddeddate(String str) {
        this.setaddeddate = str;
    }

    public int getAlbumphotocount() {
        return this.albumphotocount;
    }

    public void setAlbumphotocount(int i) {
        this.albumphotocount = i;
    }

    public ArrayList<VideoModel2> getPhotosDatas() {
        return this.modeldata;
    }

    public void setPhotosDatas(ArrayList<VideoModel2> arrayList) {
        this.modeldata = arrayList;
    }

    public long getCoverId() {
        return this.CoverId;
    }

    public void setCoverId(long j) {
        this.CoverId = j;
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

    public String getAlbumpath() {
        return this.albumpath;
    }

    public void setAlbumpath(String str) {
        this.albumpath = str;
    }
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof FolderModel)) {
            return false;
        }
        return getAlbumid().equals(((FolderModel) obj).getAlbumid());
    }
}
