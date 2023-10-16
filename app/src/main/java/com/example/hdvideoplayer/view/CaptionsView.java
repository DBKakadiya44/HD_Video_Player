package com.example.hdvideoplayer.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

public class CaptionsView extends AppCompatTextView implements Runnable {
    private static final String LINE_BREAK = "<br/>";
    private CaptionsViewLoadListener captionsViewLoadListener;
    private CMime mimeType;
    private MediaPlayer player;
    private TreeMap<Long, Line> track;

    public enum CMime {
        SUBRIP,
        WEBVTT
    }

    public interface CaptionsViewLoadListener {
        void onCaptionLoadFailed(Throwable th, @Nullable String str, int i);

        void onCaptionLoadSuccess(@Nullable String str, int i);
    }

    static class Line {
        final long from;
        String text;
        final long to;

        Line(long j, long j2, String str) {
            this.from = j;
            this.to = j2;
            this.text = str;
        }

        Line(long j, long j2) {
            this.from = j;
            this.to = j2;
        }

        void setText(String str) {
            this.text = str;
        }
    }

    private enum TrackParseState {
        NEW_TRACK,
        PARSED_CUE,
        PARSED_TIME
    }

    public CaptionsView(Context context) {
        super(context);
    }

    public CaptionsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CaptionsView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void run() {
        if (!(this.player == null || this.track == null)) {
            int currentPosition = this.player.getCurrentPosition() / 1000;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(getTimedText((long) this.player.getCurrentPosition()));
            Log.e("updatecounter", "get - " + Html.fromHtml(stringBuilder.toString()));
            setText(Html.fromHtml(stringBuilder.toString()));
        }
//        postDelayed(this, 50);
    }

    public String getTimedText(long paramLong) {
        String str = "";
        Iterator localIterator = this.track.entrySet().iterator();
        while (localIterator.hasNext()) {
            Entry localEntry = (Entry) localIterator.next();
            /*if (paramLong < ((Long) localEntry.getKey()).longValue()) {
                return str;
            }*/
            if (paramLong < ((Line) localEntry.getValue()).to) {
                str = ((Line) localEntry.getValue()).text;
                return str;
            } else {
                return str;
            }

        }
        return str;
    }


    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        postDelayed(this, 300);
        setShadowLayer(6.0f, 6.0f, 6.0f, ViewCompat.MEASURED_STATE_MASK);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this);
    }

    public void setPlayer(MediaPlayer mediaPlayer) {
        this.player = mediaPlayer;
    }

    public void setCaptionsViewLoadListener(CaptionsViewLoadListener captionsViewLoadListener) {
        this.captionsViewLoadListener = captionsViewLoadListener;
    }

    public void setCaptionsSource(@RawRes int i, CMime cMime) {
        this.mimeType = cMime;
        this.track = getSubtitleFile(i);
    }

    public void setCaptionsSource(@Nullable Uri uri, CMime cMime) {
        this.mimeType = cMime;
        if (uri == null) {
            this.track = new TreeMap();
            return;
        }
      /*  if (HelperMethods.isRemotePath(uri) ) {
            try {
                getSubtitleFile(new URL(uri.toString()));
            }catch (MalformedURLException e) {
                e.printStackTrace();
                if (this.captionsViewLoadListener != null) {
                    this.captionsViewLoadListener.onCaptionLoadFailed(e, uri.toString(), 0);
                }
            }
        } else {*/
        this.track = getSubtitleFile(uri.toString());
        /*}*/
    }

    private static TreeMap<Long, Line> parse(InputStream inputStream, CMime cMime) throws IOException {
        if (cMime == CMime.SUBRIP) {
            return parseSrt(inputStream);
        }
        if (cMime == CMime.WEBVTT) {
            return parseVtt(inputStream);
        }
        return parseSrt(inputStream);
    }

    private static TreeMap<Long, Line> parseSrt(InputStream inputStream) throws IOException {
        LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(inputStream, "UTF-8"));
        TreeMap<Long, Line> lineTreeMap = new TreeMap();
        StringBuilder stringBuilder = new StringBuilder();
        TrackParseState trackParseState = TrackParseState.NEW_TRACK;
        loop0:
        while (true) {
            String readLine;
            Line line = null;
            while (true) {
                readLine = lineNumberReader.readLine();
                if (readLine == null) {
                    break loop0;
                }
                if (trackParseState == TrackParseState.NEW_TRACK) {
                    if (!readLine.isEmpty()) {
                        if (isInteger(readLine)) {
                            trackParseState = TrackParseState.PARSED_CUE;
                            if (line != null && stringBuilder.length() > 0) {
                                break;
                            }
                        } else if (stringBuilder.length() > 0) {
                            stringBuilder.append(readLine);
                            stringBuilder.append(LINE_BREAK);
                        }
                    }
                }
                if (trackParseState == TrackParseState.PARSED_CUE) {
                    String[] split = readLine.split("-->");
                    if (split.length == 2) {
                        Line line2 = new Line(parseSrt(split[0]), parseSrt(split[1]));
                        line = line2;
                        trackParseState = TrackParseState.PARSED_TIME;
                    }
                }
                if (trackParseState == TrackParseState.PARSED_TIME) {
                    if (readLine.isEmpty()) {
                        trackParseState = TrackParseState.NEW_TRACK;
                    } else {
                        stringBuilder.append(readLine);
                        stringBuilder.append(LINE_BREAK);
                    }
                }
            }
            readLine = stringBuilder.toString();
            Log.e("readdline....", "" + readLine);
            line.setText(readLine.substring(0, readLine.length() - LINE_BREAK.length()));
            addTrack(lineTreeMap, line);
            stringBuilder.setLength(0);
        }
        Line line = null;
        if (line != null && stringBuilder.length() > 0) {
            String stringBuilder2 = stringBuilder.toString();
            line.setText(stringBuilder2.substring(0, stringBuilder2.length() - LINE_BREAK.length()));
            addTrack(lineTreeMap, line);
        }
        return lineTreeMap;
    }

    private static void addTrack(TreeMap<Long, Line> treeMap, Line line) {
        treeMap.put(Long.valueOf(line.from), line);
    }

    private static boolean isInteger(String str) {
        if (str.isEmpty()) {
            return false;
        }
        int i = 0;
        while (i < str.length()) {
            if (i == 0 && str.charAt(i) == '-') {
                if (str.length() == 1) {
                    return false;
                }
            } else if (Character.digit(str.charAt(i), 10) < 0) {
                return false;
            }
            i++;
        }
        return true;
    }

    private static long parseSrt(String str) {
        String[] strings = str.split(":");
        String[] split = strings[2].split(",");
        return (((((Long.parseLong(strings[0].trim()) * 60) * 60) * 1000) + ((Long.parseLong(split[1].trim()) * 60) * 1000)) + (Long.parseLong(split[0].trim()) * 1000)) + Long.parseLong(split[1].trim());
    }

    private static TreeMap<Long, Line> parseVtt(InputStream inputStream) throws IOException {
        LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(inputStream, "UTF-8"));
        TreeMap<Long, Line> lineTreeMap = new TreeMap();
        lineNumberReader.readLine();
        lineNumberReader.readLine();
        while (true) {
            String readLine = lineNumberReader.readLine();
            if (readLine == null) {
                return lineTreeMap;
            }
            StringBuilder stringBuilder;
            String[] split;
            StringBuilder stringBuilder2 = new StringBuilder();
            while (true) {
                String readLine2 = lineNumberReader.readLine();
                if (readLine2 == null || readLine2.trim().equals("")) {
                    stringBuilder = new StringBuilder(stringBuilder2.substring(0, stringBuilder2.length() - LINE_BREAK.length()));
                    split = readLine.split(" --> ");
                    if (split.length == 2) {
                        long parseVtt = parseVtt(split[0]);
                        lineTreeMap.put(Long.valueOf(parseVtt), new Line(parseVtt, parseVtt(split[1]), stringBuilder.toString()));
                    }
                } else {
                    stringBuilder2.append(readLine2);
                    stringBuilder2.append(LINE_BREAK);
                }
            }

        }
    }

    private static long parseVtt(String str) {
        String[] strings = str.split(":");
        if ((strings.length == 3 ? 1 : null) != null) {
            String[] split = strings[2].split("\\.");
            return (((((Long.parseLong(strings[0].trim()) * 60) * 60) * 1000) + ((Long.parseLong(strings[1].trim()) * 60) * 1000)) + (Long.parseLong(split[0].trim()) * 1000)) + Long.parseLong(split[1].trim());
        }
        String[] split = strings[1].split("\\.");
        return (((Long.parseLong(strings[0].trim()) * 60) * 1000) + (Long.parseLong(split[0].trim()) * 1000)) + Long.parseLong(split[1].trim());
    }

    private TreeMap<Long, Line> getSubtitleFile(String str) {
        InputStream fileInputStream;
        Throwable e;
        try {
            fileInputStream = new FileInputStream(new File(str));
            try {
                TreeMap<Long, Line> parse = parse(fileInputStream, this.mimeType);
                if (this.captionsViewLoadListener != null) {
                    this.captionsViewLoadListener.onCaptionLoadSuccess(str, 0);
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception str2) {
                        str2.printStackTrace();
                    }
                }
                return parse;
            } catch (Exception e2) {
                e = e2;
                try {
                    if (this.captionsViewLoadListener != null) {
                        this.captionsViewLoadListener.onCaptionLoadFailed(e, null, 0);
                    }
                    e.printStackTrace();
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception str22) {
                            str22.printStackTrace();
                        }
                    }
                    return null;
                } catch (Throwable th) {

                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
        } catch (Exception e4) {
            e = e4;
            fileInputStream = null;
            if (this.captionsViewLoadListener != null) {
                this.captionsViewLoadListener.onCaptionLoadFailed(e, e4.getMessage(), 0);
            }
            e.printStackTrace();
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        } catch (Throwable th2) {

            fileInputStream = null;
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            throw th2;
        }
    }

    private TreeMap<Long, Line> getSubtitleFile(int i) {
        InputStream openRawResource;
        Throwable e;
        try {
            openRawResource = getResources().openRawResource(i);
            try {
                TreeMap<Long, Line> parse = parse(openRawResource, this.mimeType);
                if (this.captionsViewLoadListener != null) {
                    this.captionsViewLoadListener.onCaptionLoadSuccess(null, i);
                }
                if (openRawResource != null) {
                    try {
                        openRawResource.close();
                    } catch (Exception i2) {
                        i2.printStackTrace();
                    }
                }
                return parse;
            } catch (Exception e2) {
                e = e2;
                try {
                    if (this.captionsViewLoadListener != null) {
                        this.captionsViewLoadListener.onCaptionLoadFailed(e, null, i);
                    }
                    e.printStackTrace();
                    if (openRawResource != null) {
                        try {
                            openRawResource.close();
                        } catch (Exception i22) {
                            i22.printStackTrace();
                        }
                    }
                    return null;
                } catch (Throwable th) {

                    if (openRawResource != null) {
                        try {
                            openRawResource.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
        } catch (Exception e4) {
            e = e4;
            openRawResource = null;
            if (this.captionsViewLoadListener != null) {
                this.captionsViewLoadListener.onCaptionLoadFailed(e, null, 0);
            }
            e.printStackTrace();
            if (openRawResource != null) {
                try {
                    openRawResource.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        } catch (Throwable th2) {

            openRawResource = null;
            if (openRawResource != null) {

                try {
                    openRawResource.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            throw th2;
        }
    }


}
