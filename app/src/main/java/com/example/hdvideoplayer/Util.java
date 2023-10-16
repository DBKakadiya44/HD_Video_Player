package com.example.hdvideoplayer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.AttrRes;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Util {
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static String getDurationString(long j, boolean z) {
        Locale locale2 = null;
        String str2 = null;
        Object[] objArr2 = new Object[0];
        try {
            long toHours = TimeUnit.MILLISECONDS.toHours(j);
            long toMinutes = TimeUnit.MILLISECONDS.toMinutes(j);
            j = TimeUnit.MILLISECONDS.toSeconds(j);
            if (toHours > 0) {
                Locale locale = Locale.getDefault();
                String str = "%s%02d:%02d:%02d";
                Object[] objArr = new Object[4];
                objArr[0] = z ? "-" : "";
                objArr[1] = Long.valueOf(toHours);
                objArr[2] = Long.valueOf(toMinutes - TimeUnit.HOURS.toMinutes(toHours));
                objArr[3] = Long.valueOf(j - TimeUnit.MINUTES.toSeconds(toMinutes));
                return String.format(locale, str, objArr);
            }
            locale2 = Locale.getDefault();
            str2 = "%s%02d:%02d";
            objArr2 = new Object[3];
            objArr2[0] = z ? "-" : "";
            objArr2[1] = Long.valueOf(toMinutes);
            objArr2[2] = Long.valueOf(j - TimeUnit.MINUTES.toSeconds(toMinutes));
            return String.format(locale2, str2, objArr2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isColorDark(int i) {
        return 1.0d - ((((((double) Color.red(i)) * 0.299d) + (((double) Color.green(i)) * 0.587d)) + (((double) Color.blue(i)) * 0.114d)) / 255.0d) >= 0.5d;
    }

    public static int adjustAlpha(int i, float f) {
        return Color.argb(Math.round(((float) Color.alpha(i)) * f), Color.red(i), Color.green(i), Color.blue(i));
    }

    public static int resolveColor(Context context, @AttrRes int i) {
        return resolveColor(context, i, 0);
    }

    private static int resolveColor(Context context, @AttrRes int i, int i2) {
        try {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{i});
            try {
                i = typedArray.getColor(0, i2);
                return typedArray.getColor(0, i2);
            } finally {
                typedArray.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Drawable resolveDrawable(Context context, @AttrRes int i) {
        return resolveDrawable(context, i, null);
    }

    private static Drawable resolveDrawable(Context context, @AttrRes int i, Drawable drawable) {
        Drawable drawable1 = null;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{i});
        try {
            Drawable drawable2 = typedArray.getDrawable(0);
            if (i == 0 && drawable2 != null) {
                drawable1 = drawable2;
            }
            typedArray.recycle();
            return drawable1;
        } catch (Throwable th) {
            typedArray.recycle();
        }
        return null;
    }

    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = null;
        String secondsString = null;
        String minutesString = null;
        try {
            finalTimerString = "";
            secondsString = "";
            int hours = (int) (milliseconds / 3600000);
            int minutes = ((int) (milliseconds % 3600000)) / 60000;
            int seconds = (int) (((milliseconds % 3600000) % 60000L) / 1000);
            if (hours > 0) {
                finalTimerString = hours + ":";
            }
            if (seconds < 10) {
                secondsString = "0" + seconds;
            } else {
                secondsString = "" + seconds;
            }
            minutesString = "" + minutes;
            if (finalTimerString.length() > 0 && minutes < 10) {
                minutesString = "0" + minutes;
            }
            return finalTimerString + minutesString + ":" + secondsString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public static String getFileSize(long size) {
        try {
            if (size <= 0)
                return "0";

            final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
            int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

            return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convert(long miliSeconds) {
        try {
            int hrs = (int) TimeUnit.MILLISECONDS.toHours(miliSeconds) % 24;
            int min = (int) TimeUnit.MILLISECONDS.toMinutes(miliSeconds) % 60;
            int sec = (int) TimeUnit.MILLISECONDS.toSeconds(miliSeconds) % 60;
            return String.format("%02d:%02d:%02d", hrs, min, sec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
