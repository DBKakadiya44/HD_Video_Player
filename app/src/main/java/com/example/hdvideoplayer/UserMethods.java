package com.example.hdvideoplayer;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Window;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.appcompat.widget.Toolbar;

import com.example.hdvideoplayer.view.CaptionsView;

import java.util.Map;



public interface UserMethods {
    void disableControls();

    void disableSwipeGestures();

    void enableControls();

    void enableSwipeGestures();

    void enableSwipeGestures(@NonNull Window window);

    int getCurrentPosition();

    int getDuration();

    int getHideControlsDuration();

    Toolbar getToolbar();

    void hideControls();

    void hideToolbar();

    boolean isControlsShown();

    boolean isPlaying();

    boolean isPrepared();

    void pause();

    void release();

    void removeCaptions();

    void reset();

    void seekTo(@IntRange(from = 0, to = 2147483647L) int i);

    void setAutoPlay(boolean z);

    void setBottomProgressBarVisibility(boolean z);

    void setButtonDrawable(int i, @NonNull Drawable drawable);

    void setCallback(@NonNull VideoCallback videoCallback);

    void setCaptionLoadListener(@Nullable CaptionsView.CaptionsViewLoadListener captionsViewLoadListener);

    void setCaptions(@RawRes int i, CaptionsView.CMime cMime);

    void setCaptions(Uri uri, CaptionsView.CMime cMime);

    void setHideControlsDuration(int i);

    void setHideControlsOnPlay(boolean z);

    void setInitialPosition(@IntRange(from = 0, to = 2147483647L) int i);

    void setLoadingStyle(int i);

    void setLoop(boolean z);

    void setProgressCallback(@NonNull VideoProgressCallback videoProgressCallback);

    void setSource(@NonNull Uri uri);

    void setSource(@NonNull Uri uri, @NonNull Map<String, String> map);

    void setVolume(@FloatRange(from = 0.0d, to = 1.0d) float f, @FloatRange(from = 0.0d, to = 1.0d) float f2);

    void showControls();

    void showToolbar();

    void start();

    void stop();

    void toggleControls();
}
