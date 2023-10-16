
package com.example.hdvideoplayer;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.VideoModel2;
import com.example.hdvideoplayer.model.VideoModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class VideoGetService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
        getVideo();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void getVideo() {
        ArrayList arrayList2;
        Gson gson = new Gson();

        Type type2 = new TypeToken<List<VideoModel>>() {
        }.getType();
        if (!PrefUtils.getVideoList(this).equals("")) {
            arrayList2 = (ArrayList) gson.fromJson(PrefUtils.getVideoList(this), type2);
        } else {
            arrayList2 = new ArrayList();
        }
        Cursor query2 = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "title", "datetaken", "date_modified", "bucket_display_name", "bucket_id", "duration", "resolution", "_id"}, (String) null, (String[]) null, "datetaken DESC");
        if (query2 != null) {
            int columnIndex4 = query2.getColumnIndex("bucket_display_name");
            int columnIndex5 = query2.getColumnIndex("bucket_id");
            int columnIndex6 = query2.getColumnIndex("title");
            int columnIndex7 = query2.getColumnIndex("resolution");
            int columnIndex8 = query2.getColumnIndex("duration");
            int i2 = 0;
            while (i2 < query2.getCount()) {
                query2.moveToPosition(i2);

                String string5 = query2.getString(query2.getColumnIndexOrThrow("datetaken"));
                String string6 = query2.getString(query2.getColumnIndexOrThrow("date_modified"));
                String string7 = query2.getString(columnIndex6);
                String string8 = query2.getString(query2.getColumnIndexOrThrow("_data"));
                String ss = query2.getString(query2.getColumnIndexOrThrow("_id"));
                try {
                    VideoModel2 videoModel = new VideoModel2();

                    videoModel.setImagepath(string8);
                    videoModel.setImageName(string7);
                    videoModel.setVideoid(ss);
                    if (string5 == null) {
                        videoModel.setCreateddate(String.valueOf(System.currentTimeMillis()));
                    } else {
                        videoModel.setCreateddate(string5);
                    }
                    Log.d("Videolist", "" + string8);
                    if (string6 == null) {
                        videoModel.setAddeddate(String.valueOf(System.currentTimeMillis()));
                    } else {
                        videoModel.setAddeddate(string6);
                    }
                    videoModel.setAlbumname(query2.getString(columnIndex4));
                    videoModel.setAlbumid(query2.getString(columnIndex5));
                    videoModel.setAlbumtype("1");
                    videoModel.setResolution(query2.getString(columnIndex7));
                    videoModel.setDuration(utils.milliSecondsToTimer(query2.getLong(columnIndex8)));
                    try {
                        videoModel.setRotate(0);
                        if (!arrayList2.contains(videoModel)) {
                            arrayList2.add(videoModel);
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        i2++;
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                    i2++;
                }
                i2++;
            }
            PrefUtils.setVideoList(getApplicationContext(), gson.toJson((Object) arrayList2));
            query2.close();
        }

    }


    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getBaseContext().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


}



