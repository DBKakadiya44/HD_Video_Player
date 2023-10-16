package com.example.hdvideoplayer;

import android.content.Context;
import android.preference.PreferenceManager;

public class PrefUtils {
    public static final String PREF_VIDEOLIST = "pref_videolist";

    public static String getVideoList(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_VIDEOLIST, "");
    }

    public static void setVideoList(Context context, String str) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_VIDEOLIST, str).commit();
    }


}
