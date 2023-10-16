package com.example.hdvideoplayer;

import android.graphics.Bitmap;

import com.example.hdvideoplayer.view.CustomVideoPlayer;

public interface VideoCallback {
    void onAudioOnlyBtnClick(CustomVideoPlayer videoPlayer, boolean z);

    void onBuffering(int i);

    void onCompletion(CustomVideoPlayer videoPlayer);

    void onError(CustomVideoPlayer videoPlayer, Exception exception);

    void onPauseBtnClick(CustomVideoPlayer videoPlayer, boolean z);

    void onPaused(CustomVideoPlayer videoPlayer);

    void onPlayNextClick(CustomVideoPlayer videoPlayer);

    void onPlayPrevClick(CustomVideoPlayer videoPlayer);

    void onPlayForward(CustomVideoPlayer videoPlayer);

    void onPlayBackward(CustomVideoPlayer videoPlayer);

    void onPrepared(CustomVideoPlayer videoPlayer);

    void onPreparing(CustomVideoPlayer videoPlayer);

    void onRotLockBtnClick(CustomVideoPlayer videoPlayer, boolean z);

    void onStarted(CustomVideoPlayer videoPlayer);

    void onToggleControls(CustomVideoPlayer videoPlayer, boolean z);

    void onVFloatBtnClick(CustomVideoPlayer videoPlayer, boolean z);

    void onZoom(CustomVideoPlayer videoPlayer, boolean z);

    void onRequestedOrientation(boolean z);

    void onTakeScreenShort(boolean z, Bitmap bitmap);
    void Eq(int sessionid);

    void videoSize(CustomVideoPlayer videoPlayer, float f, float f2, float f3, float f4, float f5);
}
