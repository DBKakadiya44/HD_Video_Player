package com.example.hdvideoplayer.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.media.TimedText;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CheckResult;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.UserMethods;
import com.example.hdvideoplayer.Util;
import com.example.hdvideoplayer.VideoCallback;
import com.example.hdvideoplayer.VideoPlayerCallback;
import com.example.hdvideoplayer.VideoProgressCallback;

import java.io.File;
import java.util.Map;



public class CustomVideoPlayer extends RelativeLayout implements MediaPlayer.OnTimedTextListener, UserMethods, SurfaceTextureListener, OnPreparedListener, OnBufferingUpdateListener, OnVideoSizeChangedListener, OnErrorListener, OnClickListener, OnSeekBarChangeListener {

    private static final int ZOOM = 2;
    private LinearLayout actionLayout;
    private ImageView alignSwitch;
    private AudioManager am;
    private ImageView aspectRatio;
    private ImageView aspectDefualt;
    private ImageView aspectDefualt1;
    private int aspectRatioNumber = 0;
    private ImageView aspectRatioPort;
    private ImageView audioOnly;
    private ImageView audioOnlyPort;
    private float bottom;
    private Context context;
    private float currentPosition;
    private float decidedX;
    private float decidedY;
    private float diff = 0.0f;
    private float diffBrightness;
    private float diffTime = -1.0f;
    private float diffVolume;
    private int direction = 0;
    private int finalBrightness;
    private float finalTime = -1.0f;
    private int finalVolume;
    private Map<String, String> headers;
    private Runnable hideAspTxtRunnable = new Runebalprogress_1();

    private View include;
    private int initialGesture;
    private float initialX;
    private float initialY;
    private boolean isAspectChanged = false;
    private boolean isDefaultPos = true;
    private boolean isFingerOnSeek = false;
    private boolean isFromOurGallery = false;
    public boolean isLandScape = false;
    private boolean isLeftHandHold = false;
    private boolean isNightMdON = false;
    private boolean isNoNextVideo = false;
    private boolean isNoPrevVideo = false;
    private boolean isONzooming = false;
    private boolean isRotationLocked = false;
    private boolean isSeekScroll = false;
    private boolean isZomedView = false;
    private PointF last = new PointF();
    private float[] f19m;
    private boolean mAutoPlay = true;
    private ProgressBar mBottomProgressBar;
    private boolean mBottomProgressBarVisibility = false;
    private VideoCallback mCallback;
    private View mClickFrame;
    private View mContainerView;
    private boolean mControlsDisabled = false;
    private LinearLayout mControlsFrame;
    private LinearLayout llTop;
    private ImageView previewImageview;
    private FrameLayout previewFrameLayout;
    private Handler mHandler;
    private int mHideControlsDuration = 5000;
    private boolean mHideControlsOnPlay = false;
    private ImageView mImageView;
    private int mInitialPosition = -1;
    private int mInitialTextureHeight;
    private int mInitialTextureWidth;
    private boolean mIsPrepared;
    private TextView mLabelDuration;
    private TextView mLabelPosition;
    private LinearLayout mLinearLayout;
    private int mLoadingStyle = 5;
    public boolean mLoop = false;
    private boolean mVolume = false;
    public MediaPlayer mPlayer;
    private TextView mPositionTextView;
    private ProgressBar mProgressBar;
    private VideoProgressCallback mProgressCallback;
    private View mProgressFrame;
    private ScaleGestureDetector mScaleDetector;
    private LinearLayout mScreenRotLayout;
    private SeekBar mSeeker;
    private boolean mShowToolbar = true;
    private boolean mShowTotalDuration = true;
    private boolean mShowSubTitle = true;
    private Uri mSource;
    private CaptionsView mSubView;
    private int mSubViewTextColor;
    private int mSubViewTextSize;
    private Surface mSurface;
    private boolean mSurfaceAvailable;
    private boolean mSwipeEnabled = false;
    private LinearLayout mSwitchLayout;
    public SubtitleView captionView;
    public LinearLayout layout_equilizer;
    private TextureView mTextureView;
    private String mTitle;
    private Toolbar mToolbar;
    private ImageView  back_button;
    private View mToolbarFrame;
    private final Runnable mUpdateCounters = new Runebalprogress_2();
    private boolean mWasPlaying;
    private Window mWindow;
    private Matrix matrix = new Matrix();
    private int maxBrightness = 16;
    private float maxScale = 5.0f;
    private int maxVolume;
    private float minScale = 1.0f;
    private int mode = 0;
    private int nTimes;
    private ImageView nightMode;
    private ImageView nightModePort;
    private View nightView;
    private float origHeight;
    private float origWidth;
    private ImageView playNext;
    private ImageView playPause;
    private ImageView playPrevious;
    private ImageView playForward;
    private ImageView playBackward;
    private ImageView repeat;
    private ImageView repeatPort;
    private float right;
    private ImageView rotationUnlock;
    private ImageView rotationUnlockPort;
    private float saveScale = 1.0f;
    private float scaleHeight;
    private float scaleWidth;
    private PointF start = new PointF();
    private int startBrightness = 0;
    private int startVolume;
    private long then = 0;
    private TextView toolbarTitle;
    private ImageView btnScreenshort, btnSubtitle, btnVolume, btnEqualizer, btnLock, btnUnLock;
    public LinearLayout lockLayout;
    private ImageView videoFloat;
    private ImageView videoFloatPort;
    private float viewFloatHeight;
    private float viewFloatWidth;
    private int viewVisibility;
    private int xoff = 0;
    private int yoff = 0;
    private float zoomXoff = 0.0f;
    private float zoomYoff = 0.0f;


    public TextView ok;


    static final int MAX_SLIDERS = 8;



    public SeekBar getmSeeker() {
        return mSeeker;
    }


    class Animation_1 extends AnimatorListenerAdapter {
        Animation_1() {
        }

        public void onAnimationEnd(Animator animator) {
            if (CustomVideoPlayer.this.mControlsFrame != null) {
                CustomVideoPlayer.this.mControlsFrame.setVisibility(GONE);
                llTop.setVisibility(GONE);
            }
        }
    }

    /* renamed from: videoplayer.freeplayer.hdplayer.videoplayer.VideoPlayer$3 */
    class Animation_2 extends AnimatorListenerAdapter {
        Animation_2() {
        }

        public void onAnimationEnd(Animator animator) {
            if (CustomVideoPlayer.this.mToolbarFrame != null) {
                CustomVideoPlayer.this.mToolbarFrame.setVisibility(GONE);
            }
        }
    }


    class Runebalprogress_1 implements Runnable {
        Runebalprogress_1() {
        }

        public void run() {
            CustomVideoPlayer.this.actionLayout.setVisibility(GONE);
            CustomVideoPlayer.this.mPositionTextView.setVisibility(GONE);
            if (!CustomVideoPlayer.this.isPlaying()) {
                CustomVideoPlayer.this.showPlayPauseBtnSets();
            }
        }
    }

    class Runebalprogress_2 implements Runnable {
        Runebalprogress_2() {
        }

        public void run() {
            if (!(CustomVideoPlayer.this.mHandler == null || !CustomVideoPlayer.this.mIsPrepared || CustomVideoPlayer.this.mSeeker == null)) {
                if (CustomVideoPlayer.this.mPlayer != null) {
                    long currentPosition = (long) CustomVideoPlayer.this.mPlayer.getCurrentPosition();
                    long duration = (long) CustomVideoPlayer.this.mPlayer.getDuration();
                    if (currentPosition > duration) {
                        currentPosition = duration;
                    }
//                    captionView.setVisibility(VISIBLE);
//                    captionView.setText(mSubView.getTimedText(currentPosition));

                    CustomVideoPlayer.this.mLabelPosition.setText(Util.getDurationString(currentPosition, false));
                    if (CustomVideoPlayer.this.mShowTotalDuration) {
                        CustomVideoPlayer.this.mLabelDuration.setText(Util.getDurationString(duration, false));
                    } else {
                        CustomVideoPlayer.this.mLabelDuration.setText(Util.getDurationString(duration - currentPosition, true));
                    }
                    int i = (int) currentPosition;
                    int i2 = (int) duration;

                    CustomVideoPlayer.this.mSeeker.setProgress(i);
                    CustomVideoPlayer.this.mSeeker.setMax(i2);
                    CustomVideoPlayer.this.mBottomProgressBar.setProgress(i);
                    CustomVideoPlayer.this.mBottomProgressBar.setMax(i2);
                    if (CustomVideoPlayer.this.mProgressCallback != null) {
                        CustomVideoPlayer.this.mProgressCallback.onVideoProgressUpdate(i, i2);
                    }
                    if (CustomVideoPlayer.this.mHandler != null) {
                        CustomVideoPlayer.this.mHandler.postDelayed(this, 100);
                    }

                }
            }
        }
    }


    private class ZoomOnTouchListeners implements OnTouchListener {

        private class ScaleListener extends SimpleOnScaleGestureListener {
            private ScaleListener() {
            }

            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                if (CustomVideoPlayer.this.isControlsShown()) {
                    CustomVideoPlayer.this.hideControls();
                }
                CustomVideoPlayer.this.mode = 2;
                return true;
            }


            public boolean onScale(ScaleGestureDetector paramScaleGestureDetector) {
                if (!mLockUnlock) {
                    float f1 = paramScaleGestureDetector.getScaleFactor();
                    float f2 = CustomVideoPlayer.this.saveScale;
                    CustomVideoPlayer.this.saveScale = CustomVideoPlayer.this.saveScale * f1;
                    if (CustomVideoPlayer.this.saveScale > CustomVideoPlayer.this.maxScale) {
                        CustomVideoPlayer.this.saveScale = CustomVideoPlayer.this.maxScale;
                        f1 = CustomVideoPlayer.this.maxScale / f2;
                    } else if (CustomVideoPlayer.this.saveScale < CustomVideoPlayer.this.minScale) {
                        CustomVideoPlayer.this.saveScale = CustomVideoPlayer.this.minScale;
                        f1 = CustomVideoPlayer.this.minScale / f2;
                    }
                    TextView localTextView = CustomVideoPlayer.this.mPositionTextView;
                    StringBuilder localStringBuilder = new StringBuilder();
                    localStringBuilder.append(Math.round((CustomVideoPlayer.this.saveScale - 1.0F) * 100.0F / (CustomVideoPlayer.this.maxScale - 1.0F)));
                    localStringBuilder.append(" %");
                    aspectRatioNumber = 5;
                    if (Math.round((CustomVideoPlayer.this.saveScale - 1.0F) * 100.0F / (CustomVideoPlayer.this.maxScale - 1.0F)) < 1) {
                        aspectRatioNumber = 0;
                        adjustAspectRatio(mInitialTextureWidth, mInitialTextureHeight, mPlayer.getVideoWidth(), mPlayer.getVideoHeight());
                    }
                    localTextView.setText(localStringBuilder.toString());
                    CustomVideoPlayer.this.nTimes = CustomVideoPlayer.this.nTimes + 1;
                    if (CustomVideoPlayer.this.nTimes == 1) {
                        CustomVideoPlayer.this.actionLayout.setVisibility(VISIBLE);
                        CustomVideoPlayer.this.mPositionTextView.setVisibility(VISIBLE);
                    }
                    CustomVideoPlayer.this.right = CustomVideoPlayer.this.scaleWidth - ((float) CustomVideoPlayer.this.getWidth());
                    CustomVideoPlayer.this.bottom = CustomVideoPlayer.this.scaleHeight - ((float) CustomVideoPlayer.this.getHeight());
                    if ((CustomVideoPlayer.this.origWidth * CustomVideoPlayer.this.saveScale > CustomVideoPlayer.this.getWidth()) && (CustomVideoPlayer.this.origHeight * CustomVideoPlayer.this.saveScale > CustomVideoPlayer.this.getHeight())) {
                        CustomVideoPlayer.this.matrix.postScale(f1, f1, paramScaleGestureDetector.getFocusX(), paramScaleGestureDetector.getFocusY());
                        if (f1 < 1.0F) {
                            CustomVideoPlayer.this.matrix.getValues(CustomVideoPlayer.this.f19m);
                            f1 = CustomVideoPlayer.this.f19m[2];
                            f2 = CustomVideoPlayer.this.f19m[5];
                            if (f1 < -CustomVideoPlayer.this.right) {
                                CustomVideoPlayer.this.zoomXoff = -(CustomVideoPlayer.this.right + f1);
                                CustomVideoPlayer.this.zoomYoff = 0.0f;
                            } else if (f1 > 0.0F) {
                                CustomVideoPlayer.this.zoomXoff = -f1;
                                CustomVideoPlayer.this.zoomYoff = 0.0f;
                            }
                            if (f2 < -CustomVideoPlayer.this.bottom) {
                                CustomVideoPlayer.this.zoomXoff = 0.0f;
                                CustomVideoPlayer.this.zoomYoff = -(CustomVideoPlayer.this.bottom + f2);
                            } else if (f2 > 0.0F) {
                                CustomVideoPlayer.this.zoomXoff = 0.0f;
                                CustomVideoPlayer.this.zoomYoff = -f2;
                            }
//                        VideoPlayer.this.matrix.postTranslate(VideoPlayer.this.zoomXoff, VideoPlayer.this.zoomYoff);
                            return true;
                        }
                    } else {
                        CustomVideoPlayer.this.matrix.postScale(f1, f1, CustomVideoPlayer.this.getWidth() / 2, CustomVideoPlayer.this.getHeight() / 2);
                        if (f1 < 1.0F) {
                            CustomVideoPlayer.this.matrix.getValues(CustomVideoPlayer.this.f19m);
                            f1 = CustomVideoPlayer.this.f19m[2];
                            f2 = CustomVideoPlayer.this.f19m[5];
                            if (Math.round(CustomVideoPlayer.this.origWidth * CustomVideoPlayer.this.saveScale) < CustomVideoPlayer.this.getWidth()) {
                                if (f2 < -CustomVideoPlayer.this.bottom) {
                                    CustomVideoPlayer.this.zoomXoff = 0.0f;
                                    CustomVideoPlayer.this.zoomYoff = -(CustomVideoPlayer.this.bottom + f2);
                                } else if (f2 > 0.0F) {
                                    CustomVideoPlayer.this.zoomXoff = 0.0f;
                                    CustomVideoPlayer.this.zoomYoff = -f2;
                                }
                            } else if (f1 < -CustomVideoPlayer.this.right) {
                                CustomVideoPlayer.this.zoomXoff = -(CustomVideoPlayer.this.right + f1);
                                CustomVideoPlayer.this.zoomYoff = 0.0F;
                            } else if (f1 > 0.0F) {
                                CustomVideoPlayer.this.zoomXoff = -f1;
                                CustomVideoPlayer.this.zoomYoff = 0.0F;
                            }
//                        VideoPlayer.this.matrix.postTranslate(VideoPlayer.this.zoomXoff, VideoPlayer.this.zoomYoff);
                        }
                    }
                    return true;
                } else {
                    showLock();
                    hideLock();

                }
                return true;
            }

        }

        public ZoomOnTouchListeners() {
            CustomVideoPlayer.this.f19m = new float[9];
            CustomVideoPlayer.this.mScaleDetector = new ScaleGestureDetector(CustomVideoPlayer.this.context, new ScaleListener());
        }

        public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
            if (!mLockUnlock) {

                CustomVideoPlayer.this.mScaleDetector.onTouchEvent(paramMotionEvent);
                CustomVideoPlayer.this.matrix.getValues(CustomVideoPlayer.this.f19m);
                float f4 = CustomVideoPlayer.this.f19m[2];
                float f5 = CustomVideoPlayer.this.f19m[5];
                PointF pointF = new PointF(paramMotionEvent.getX(), paramMotionEvent.getY());
                int i = paramMotionEvent.getActionMasked();
                int j = 0;
                switch (i) {
                    case 3:
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        hideZOOMtext();
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        CustomVideoPlayer.this.last.set(paramMotionEvent.getX(), paramMotionEvent.getY());
                        CustomVideoPlayer.this.start.set(CustomVideoPlayer.this.last);
                        showZoomText();
                        CustomVideoPlayer.this.mode = ZOOM;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float f2;
                        float f1;
                        if ((CustomVideoPlayer.this.mode != 2) && (!CustomVideoPlayer.this.isONzooming)) {
                            if (CustomVideoPlayer.this.initialGesture == 0) {
                                f2 = paramMotionEvent.getX() - CustomVideoPlayer.this.initialX;
                                f1 = paramMotionEvent.getY() - CustomVideoPlayer.this.initialY;
                            } else {
                                f2 = paramMotionEvent.getX() - CustomVideoPlayer.this.decidedX;
                                f1 = paramMotionEvent.getY() - CustomVideoPlayer.this.decidedY;
                            }
                            if ((CustomVideoPlayer.this.initialGesture == 0) && (Math.abs(f2) > 100.0F)) {
//                                VideoPlayer.this.hideControls();
                                CustomVideoPlayer.this.initialGesture = 1;
                                CustomVideoPlayer.this.decidedX = paramMotionEvent.getX();
                                CustomVideoPlayer.this.decidedY = paramMotionEvent.getY();
                                if (f2 > 0.0F) {
                                    CustomVideoPlayer.this.direction = 1;
                                } else {
                                    CustomVideoPlayer.this.direction = 2;
                                }
                                onBeforeMove();
                            } else if ((CustomVideoPlayer.this.initialGesture == 0) && (Math.abs(f1) > 100.0F)) {
//                                VideoPlayer.this.hideControls();
                                CustomVideoPlayer.this.initialGesture = 2;
                                CustomVideoPlayer.this.decidedX = paramMotionEvent.getX();
                                CustomVideoPlayer.this.decidedY = paramMotionEvent.getY();
                                if (f1 > 0.0F) {
                                    CustomVideoPlayer.this.direction = 3;
                                } else {
                                    CustomVideoPlayer.this.direction = 4;
                                }
                                onBeforeMove();
                            }
                            if (CustomVideoPlayer.this.initialGesture == 1) {
                                if (f2 > 0.0F) {
                                    CustomVideoPlayer.this.direction = 1;
                                    CustomVideoPlayer.this.diff = f2;
                                } else {
                                    CustomVideoPlayer.this.direction = 2;
                                    CustomVideoPlayer.this.diff = -f2;
                                }
                                onMove();
                                break;
                            } else if (CustomVideoPlayer.this.initialGesture == 2) {
                                if (f1 > 0.0F) {
                                    CustomVideoPlayer.this.direction = 3;
                                    CustomVideoPlayer.this.diff = f1;
                                } else {
                                    CustomVideoPlayer.this.direction = 4;
                                    CustomVideoPlayer.this.diff = -f1;
                                }
                                onMove();
                                break;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        CustomVideoPlayer.this.mode = 0;
                        if ((CustomVideoPlayer.this.initialGesture == 0) && (!CustomVideoPlayer.this.isONzooming) && (System.currentTimeMillis() - CustomVideoPlayer.this.then < 150L)) {
                            performClick();
                        } else if ((CustomVideoPlayer.this.initialGesture == 0) && (CustomVideoPlayer.this.isONzooming)) {
                            CustomVideoPlayer.this.mCallback.onZoom(CustomVideoPlayer.this, true);
                            CustomVideoPlayer.this.isRotationLocked = true;
                            CustomVideoPlayer.this.mCallback.onRotLockBtnClick(CustomVideoPlayer.this, true);
                            CustomVideoPlayer.this.rotationUnlock.setImageResource(R.drawable.ic_rotate);
                            CustomVideoPlayer.this.rotationUnlockPort.setImageResource(R.drawable.ic_rotate);
                        } else if (CustomVideoPlayer.this.initialGesture > 0) {
                            onAfterMove();
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        CustomVideoPlayer.this.then = System.currentTimeMillis();
                        CustomVideoPlayer.this.initialX = paramMotionEvent.getX();
                        CustomVideoPlayer.this.initialY = paramMotionEvent.getY();
                        CustomVideoPlayer.this.initialGesture = 0;
                        CustomVideoPlayer.this.last.set(CustomVideoPlayer.this.initialX, CustomVideoPlayer.this.initialY);
                        CustomVideoPlayer.this.start.set(CustomVideoPlayer.this.last);
                        CustomVideoPlayer.this.mode = 1;
                        break;
                }
                CustomVideoPlayer.this.mTextureView.setTransform(CustomVideoPlayer.this.matrix);
                CustomVideoPlayer.this.mTextureView.invalidate();
                return true;
            } else {
                showLock();
                hideLock();
            }
            return false;
        }

        public void performClick() {
            CustomVideoPlayer.this.toggleControls();
            if (CustomVideoPlayer.this.initialX >= ((float) (CustomVideoPlayer.this.mInitialTextureWidth / 2))) {
                CustomVideoPlayer.this.isLeftHandHold = false;
            } else {
                CustomVideoPlayer.this.isLeftHandHold = true;
            }
            CustomVideoPlayer.this.LinearLayoutPos();
        }

        public void showZoomText() {
            if (CustomVideoPlayer.this.isControlsShown()) {
                CustomVideoPlayer.this.hideControls();
            }
            CustomVideoPlayer.this.nTimes = 0;
        }

        public void hideZOOMtext() {
            CustomVideoPlayer.this.actionLayout.setVisibility(GONE);
            CustomVideoPlayer.this.mPositionTextView.setVisibility(GONE);
        }

        public void onBeforeMove() {
            if (CustomVideoPlayer.this.mSwipeEnabled) {
                CustomVideoPlayer.this.nTimes = 0;
                if (CustomVideoPlayer.this.direction != 2) {
                    if (CustomVideoPlayer.this.direction != 1) {
                        CustomVideoPlayer.this.isSeekScroll = false;
                        return;
                    }
                }
                CustomVideoPlayer.this.isSeekScroll = true;
                CustomVideoPlayer.this.mWasPlaying = CustomVideoPlayer.this.isPlaying();
                CustomVideoPlayer.this.mPlayer.pause();
                CustomVideoPlayer.this.currentPosition = (float) CustomVideoPlayer.this.mPlayer.getCurrentPosition();
                CustomVideoPlayer.this.actionLayout.setVisibility(VISIBLE);
                CustomVideoPlayer.this.mPositionTextView.setVisibility(VISIBLE);
            }
        }

        public void onMove() {
            if (!CustomVideoPlayer.this.mSwipeEnabled) {
                return;
            }
            String localObject;
            if ((CustomVideoPlayer.this.direction != 2) && (CustomVideoPlayer.this.direction != 1)) {
                CustomVideoPlayer.this.finalTime = -1.0f;
                if ((CustomVideoPlayer.this.initialX < CustomVideoPlayer.this.mInitialTextureWidth / 2) && (CustomVideoPlayer.this.mWindow != null)) {
                    if (CustomVideoPlayer.this.initialX < CustomVideoPlayer.this.mInitialTextureWidth / 2) {
                        CustomVideoPlayer.this.diffBrightness = (((float) CustomVideoPlayer.this.maxBrightness) * CustomVideoPlayer.this.diff) / (((float) CustomVideoPlayer.this.mInitialTextureHeight) / 2.0f);
                        if (CustomVideoPlayer.this.direction == 3) {
                            CustomVideoPlayer.this.diffBrightness = -CustomVideoPlayer.this.diffBrightness;
                        }
                        CustomVideoPlayer.this.finalBrightness = CustomVideoPlayer.this.startBrightness + ((int) CustomVideoPlayer.this.diffBrightness);
                        if (CustomVideoPlayer.this.finalBrightness < 1) {
                            CustomVideoPlayer.this.finalBrightness = 1;
                        } else if (CustomVideoPlayer.this.finalBrightness > CustomVideoPlayer.this.maxBrightness) {
                            CustomVideoPlayer.this.finalBrightness = CustomVideoPlayer.this.maxBrightness;
                        }
                        localObject = String.format(CustomVideoPlayer.this.getResources().getString(R.string.brightness), new Object[]{Integer.valueOf(CustomVideoPlayer.this.finalBrightness - 1)});
                        CustomVideoPlayer.this.nTimes = CustomVideoPlayer.this.nTimes + 1;
                        if (CustomVideoPlayer.this.nTimes >= 2) {
                            CustomVideoPlayer.this.mPositionTextView.setText((CharSequence) localObject);
                            if (CustomVideoPlayer.this.finalBrightness <= 7) {
                                CustomVideoPlayer.this.mImageView.setImageResource(R.drawable.ic_brightness);
                            } else {
                                CustomVideoPlayer.this.mImageView.setImageResource(R.drawable.ic_brightness);
                            }
                            if (CustomVideoPlayer.this.nTimes == 2) {
                                CustomVideoPlayer.this.actionLayout.setVisibility(VISIBLE);
                                CustomVideoPlayer.this.mPositionTextView.setVisibility(VISIBLE);
                                CustomVideoPlayer.this.mImageView.setVisibility(VISIBLE);
                            }
                            WindowManager.LayoutParams layoutParams = CustomVideoPlayer.this.mWindow.getAttributes();
                            layoutParams.screenBrightness = (CustomVideoPlayer.this.finalBrightness / 16.0F);
                            CustomVideoPlayer.this.mWindow.setAttributes(layoutParams);
                        }
                    }
                } else {
                    CustomVideoPlayer.this.diffVolume = (((float) CustomVideoPlayer.this.maxVolume) * CustomVideoPlayer.this.diff) / (((float) CustomVideoPlayer.this.mInitialTextureHeight) / 2.0f);
                    if (CustomVideoPlayer.this.direction == 3) {
                        CustomVideoPlayer.this.diffVolume = -CustomVideoPlayer.this.diffVolume;
                    }
                    CustomVideoPlayer.this.finalVolume = CustomVideoPlayer.this.startVolume + ((int) CustomVideoPlayer.this.diffVolume);
                    if (CustomVideoPlayer.this.finalVolume < 0) {
                        CustomVideoPlayer.this.finalVolume = 0;
                    } else if (CustomVideoPlayer.this.finalVolume > CustomVideoPlayer.this.maxVolume) {
                        CustomVideoPlayer.this.finalVolume = CustomVideoPlayer.this.maxVolume;
                    }
                    localObject = String.format(CustomVideoPlayer.this.getResources().getString(R.string.volume), new Object[]{Integer.valueOf(CustomVideoPlayer.this.finalVolume)});
                    CustomVideoPlayer.this.nTimes = CustomVideoPlayer.this.nTimes + 1;
                    if (CustomVideoPlayer.this.nTimes >= 2) {
                        CustomVideoPlayer.this.mPositionTextView.setText((CharSequence) localObject);
                        if (CustomVideoPlayer.this.finalVolume == 0) {
                            CustomVideoPlayer.this.btnVolume.setImageDrawable(getResources().getDrawable(R.drawable.ic_mute));
                            CustomVideoPlayer.this.mImageView.setImageResource(R.drawable.ic_mute);
                        } else if (CustomVideoPlayer.this.finalVolume <= 7) {
//                            VideoPlayer.this.mImageView.setBackgroundResource(R.drawable.less_volume);
                        } else {
                            CustomVideoPlayer.this.btnVolume.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_on));
                            CustomVideoPlayer.this.mImageView.setImageResource(R.drawable.ic_volume_on);
                        }
                        if (CustomVideoPlayer.this.nTimes == 2) {
                            CustomVideoPlayer.this.actionLayout.setVisibility(VISIBLE);
                            CustomVideoPlayer.this.mPositionTextView.setVisibility(VISIBLE);
                            CustomVideoPlayer.this.mImageView.setVisibility(VISIBLE);
                        }
                        CustomVideoPlayer.this.am.setStreamVolume(3, CustomVideoPlayer.this.finalVolume, AudioManager.FLAG_SHOW_UI);
                    }
                }
            } else {
                if (CustomVideoPlayer.this.mPlayer.getDuration() <= 6000) {
                    CustomVideoPlayer.this.diffTime = (((float) CustomVideoPlayer.this.mPlayer.getDuration()) * CustomVideoPlayer.this.diff) / ((float) CustomVideoPlayer.this.mInitialTextureWidth);
                } else if (CustomVideoPlayer.this.initialY <= CustomVideoPlayer.this.mInitialTextureHeight / 2) {
                    CustomVideoPlayer.this.diffTime = (CustomVideoPlayer.this.diff * 120000.0f) / ((float) CustomVideoPlayer.this.mInitialTextureWidth);
                } else if (CustomVideoPlayer.this.initialY > CustomVideoPlayer.this.mInitialTextureHeight / 2) {
                    CustomVideoPlayer.this.diffTime = (CustomVideoPlayer.this.diff * 60000.0f) / ((float) CustomVideoPlayer.this.mInitialTextureWidth);
                }
                if (CustomVideoPlayer.this.direction == 2) {
                    CustomVideoPlayer.this.diffTime = CustomVideoPlayer.this.diffTime * -1.0f;
                }
                CustomVideoPlayer.this.finalTime = CustomVideoPlayer.this.currentPosition + CustomVideoPlayer.this.diffTime;
                if (CustomVideoPlayer.this.finalTime < 0.0F) {
                    CustomVideoPlayer.this.finalTime = 0.0f;
                } else if (CustomVideoPlayer.this.finalTime > CustomVideoPlayer.this.mPlayer.getDuration()) {
                    CustomVideoPlayer.this.finalTime = (float) CustomVideoPlayer.this.mPlayer.getDuration();
                }

                CustomVideoPlayer.this.diffTime = CustomVideoPlayer.this.finalTime - CustomVideoPlayer.this.currentPosition;
                TextView localTextView = CustomVideoPlayer.this.mPositionTextView;
                StringBuilder localStringBuilder = new StringBuilder();
                localStringBuilder.append(Util.getDurationString((long) CustomVideoPlayer.this.finalTime, false));
                localStringBuilder.append(" [");
                if (CustomVideoPlayer.this.direction == 0) {
                    localObject = "-";
                } else {
                    localObject = "+";
                }
                localStringBuilder.append((String) localObject);
                localStringBuilder.append(Util.getDurationString((long) Math.abs(CustomVideoPlayer.this.diffTime), false));
                localStringBuilder.append("]");
                localTextView.setText(localStringBuilder.toString());
                CustomVideoPlayer.this.nTimes = CustomVideoPlayer.this.nTimes + 1;
                if (CustomVideoPlayer.this.nTimes >= 15) {
                    CustomVideoPlayer.this.seekTo((int) CustomVideoPlayer.this.finalTime);
                    CustomVideoPlayer.this.nTimes = 0;
                }
                CustomVideoPlayer.this.mSeeker.setProgress((int) CustomVideoPlayer.this.finalTime);
            }
        }



        public void onAfterMove() {
            if (CustomVideoPlayer.this.initialX < ((float) (CustomVideoPlayer.this.mInitialTextureWidth / 2)) && CustomVideoPlayer.this.mWindow != null && !CustomVideoPlayer.this.isSeekScroll) {
                CustomVideoPlayer.this.startBrightness = (int) (CustomVideoPlayer.this.mWindow.getAttributes().screenBrightness * 16.0f);
                CustomVideoPlayer.this.startBrightness = CustomVideoPlayer.this.finalBrightness;
            } else if (!(CustomVideoPlayer.this.initialX < ((float) (CustomVideoPlayer.this.mInitialTextureWidth / 2)) || CustomVideoPlayer.this.mWindow == null || CustomVideoPlayer.this.isSeekScroll)) {
                CustomVideoPlayer.this.startVolume = CustomVideoPlayer.this.finalVolume;
            }
            if (CustomVideoPlayer.this.finalTime >= 0.0f && CustomVideoPlayer.this.mSwipeEnabled) {
                CustomVideoPlayer.this.seekTo((int) CustomVideoPlayer.this.finalTime);
                if (CustomVideoPlayer.this.mWasPlaying) {
                    CustomVideoPlayer.this.mPlayer.start();
                }
            }
            if (!CustomVideoPlayer.this.isPlaying()) {
                CustomVideoPlayer.this.showControls();
            }
            CustomVideoPlayer.this.actionLayout.setVisibility(GONE);
            CustomVideoPlayer.this.mPositionTextView.setVisibility(GONE);
            CustomVideoPlayer.this.mImageView.setVisibility(GONE);
        }
    }


    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public void setButtonDrawable(int i, @NonNull Drawable drawable) {
    }

    public CustomVideoPlayer(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public CustomVideoPlayer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        init(context, attributeSet);
    }

    public CustomVideoPlayer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.context = context;
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.context = context;
        retriever = new MediaMetadataRetriever();
        if (attributeSet != null) {
            TypedArray theme = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.PlayerVideoPlayer, 0, 0);
            try {
                String string = theme.getString(R.styleable.PlayerVideoPlayer_source);
                if (!(string == null || string.trim().isEmpty())) {
                    this.mSource = Uri.parse(string);
                }
                string = theme.getString(R.styleable.PlayerVideoPlayer_title);
                if (!(string == null || string.trim().isEmpty())) {
                    this.mTitle = string;
                }
                this.mLoadingStyle = theme.getInt(R.styleable.PlayerVideoPlayer_loadingstyle, 0);
                this.mHideControlsDuration = theme.getInteger(R.styleable.PlayerVideoPlayer_hidecontrolduration, this.mHideControlsDuration);
                this.mHideControlsOnPlay = theme.getBoolean(R.styleable.PlayerVideoPlayer_hidecontrolonplay, false);
                this.mAutoPlay = theme.getBoolean(R.styleable.PlayerVideoPlayer_autoplay, true);
                this.mLoop = theme.getBoolean(R.styleable.PlayerVideoPlayer_loop, false);
                this.mShowTotalDuration = theme.getBoolean(R.styleable.PlayerVideoPlayer_showtotalduration, true);
                this.mBottomProgressBarVisibility = theme.getBoolean(R.styleable.PlayerVideoPlayer_bottomprogressvarvisibility, false);
                this.mSwipeEnabled = theme.getBoolean(R.styleable.PlayerVideoPlayer_swipeenable, false);
                this.mShowToolbar = theme.getBoolean(R.styleable.PlayerVideoPlayer_showtoolbar, true);
                this.mControlsDisabled = theme.getBoolean(R.styleable.PlayerVideoPlayer_subviewtextsize, false);
                this.mSubViewTextSize = theme.getDimensionPixelSize(R.styleable.PlayerVideoPlayer_subviewtextsize, getResources().getDimensionPixelSize(R.dimen.subtitle_size));
                this.mSubViewTextColor = theme.getColor(R.styleable.PlayerVideoPlayer_subviewtextcolor, ContextCompat.getColor(context, R.color.subtitle_color));
            } catch (Exception context2) {
                context2.printStackTrace();
            } catch (Throwable th) {
                theme.recycle();
            }
            theme.recycle();
        } else {
            this.mSubViewTextSize = getResources().getDimensionPixelSize(R.dimen.subtitle_size);
            this.mSubViewTextColor = ContextCompat.getColor(context, R.color.subtitle_color);
        }
        this.mCallback = new VideoPlayerCallback();

    }
    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    public void setSource(@NonNull Uri uri) {
        this.mSource = uri;
        if (this.mPlayer != null) {
            prepare();
        }
    }

    public void setSource(@NonNull Uri uri, @NonNull Map<String, String> map) {
        this.headers = map;
        setSource(uri);
    }

    public void setCallback(@NonNull VideoCallback videoCallback) {
        this.mCallback = videoCallback;
    }

    public void setCaptionLoadListener(@Nullable CaptionsView.CaptionsViewLoadListener captionsViewLoadListener) {
        this.mSubView.setCaptionsViewLoadListener(captionsViewLoadListener);
    }

    @Override
    public void setCaptions(int i, CaptionsView.CMime cMime) {

    }

    public void setProgressCallback(@NonNull VideoProgressCallback videoProgressCallback) {
        this.mProgressCallback = videoProgressCallback;
    }

    public void setmTitle(String str) {
        this.toolbarTitle.setText(str);
    }

    public void extendControlShow() {
        if (!mLockUnlock) {
            if (this.mHandler != null) {
                this.mHandler.removeCallbacks(this.hideControlsRunnable);
                this.mHandler.postDelayed(this.hideControlsRunnable, mHideControlsDuration);
            }
        }
    }

    private Runnable hideControlsRunnable = new Runnable() {
        @Override
        public void run() {
            CustomVideoPlayer.this.hideControls();
        }
    };

    public void disableToolbar() {
        this.mShowToolbar = false;
    }

    public void enableToolbar() {
        this.mShowToolbar = true;
    }

    public void onZooming(boolean z) {
        this.isONzooming = z;
        this.isZomedView = (z ^ true);
        adjustAspectRatio(this.mInitialTextureWidth, this.mInitialTextureHeight, this.mPlayer.getVideoWidth(), this.mPlayer.getVideoHeight());
    }

    public void onNoPrevVideo() {
        this.playPrevious.setVisibility(GONE);
        this.isNoPrevVideo = true;
    }

    public void onNoNextVideo() {
        this.playNext.setVisibility(GONE);
        this.isNoNextVideo = true;
    }

    public void fromOurGallery(boolean z) {
        this.isFromOurGallery = z;
    }

    public void hidePlayPauseBtnSets() {
        this.playPause.setVisibility(GONE);
        this.playNext.setVisibility(GONE);
        this.playPrevious.setVisibility(GONE);
        this.playForward.setVisibility(GONE);
        this.playBackward.setVisibility(GONE);
        aspectRatio.setVisibility(GONE);
        rotationUnlockPort.setVisibility(GONE);

        repeat.setVisibility(GONE);
        nightMode.setVisibility(GONE);
        videoFloat.setVisibility(GONE);
        audioOnly.setVisibility(GONE);
    }

    public void showPlayPauseBtnSets() {
        this.playPause.setVisibility(VISIBLE);
        this.playForward.setVisibility(VISIBLE);
        this.playBackward.setVisibility(VISIBLE);
        this.playNext.setVisibility(VISIBLE);
        this.playPrevious.setVisibility(VISIBLE);
        aspectRatio.setVisibility(VISIBLE);
        rotationUnlockPort.setVisibility(VISIBLE);
        repeat.setVisibility(VISIBLE);
        nightMode.setVisibility(VISIBLE);
        videoFloat.setVisibility(VISIBLE);
        audioOnly.setVisibility(VISIBLE);

    }

    public void setHideControlsOnPlay(boolean z) {
        this.mHideControlsOnPlay = z;
    }

    public void setAutoPlay(boolean z) {
        this.mAutoPlay = z;
    }

    public void enableSwipeGestures() {
        this.mSwipeEnabled = true;
    }

    public void enableSwipeGestures(@NonNull Window window) {
        this.mSwipeEnabled = true;
        this.mWindow = window;
    }

    public void disableSwipeGestures() {
        this.mSwipeEnabled = false;
    }

    public void showToolbar() {
        this.mShowToolbar = true;
    }

    public void hideToolbar() {
        this.mShowToolbar = false;
        hideToolbarWithAnimation();
    }

    public void setInitialPosition(@IntRange(from = 0, to = 2147483647L) int i) {
        this.mInitialPosition = i;
    }

    @Override
    public void setLoadingStyle(int i) {

    }

    public int totalDuaration;

    private void prepare() {
        try {

            this.mHandler = new Handler();
            this.mPlayer = new MediaPlayer();
            if (!(!this.mSurfaceAvailable || this.mSource == null || this.mPlayer == null)) {


                if (!this.mIsPrepared) {
                    try {
                        if (mPlayer.isPlaying()) {
                            pause();
                        }
                        this.mPlayer.setOnPreparedListener(this);
                        this.mPlayer.setOnBufferingUpdateListener(this);
                        //                    this.mPlayer.setOnCompletionListener(this);
                        this.mPlayer.setOnVideoSizeChangedListener(this);
                        this.mPlayer.setOnErrorListener(this);
                        this.mPlayer.setAudioStreamType(3);
                        this.am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

                        this.mCallback.onPreparing(this);
                        this.mPlayer.setSurface(this.mSurface);
                        if (!this.mSource.getScheme().equals("http")) {
                            if (!this.mSource.getScheme().equals("https")) {
                                retriever.setDataSource(getContext(), mSource);
                                String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                if (duration != null) {
                                    totalDuaration = Integer.valueOf(duration);
                                } else {
                                    totalDuaration = 0;
                                }
                                this.mPlayer.setDataSource(getContext(), this.mSource, this.headers);

                                loadCaption("first");

                                this.mPlayer.prepareAsync();

                                this.maxVolume = this.am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                                this.startVolume = this.am.getStreamVolume(AudioManager.STREAM_MUSIC);
                            }
                        }

                        try {
                            mPlayer.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
                                @Override
                                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                                    int orientation = -1;


                                 /*   if (PreferenceManager.getInstance().isNightModeEnabled()) {
                                        if (width < height) {
                                            orientation = 0;
                                            isLandScape = false;
                                            // mCallback.onRequestedOrientation(true);
                                        } else {
                                            orientation = 1;
                                            isLandScape = true;
                                            //  mCallback.onRequestedOrientation(false);
                                        }
                                    } else {

                                    }*/
                                    if (width < height) {
                                        orientation = 0;
                                        isLandScape = false;
                                        mCallback.onRequestedOrientation(true);
                                    } else {
                                        orientation = 1;
                                        isLandScape = true;
                                        mCallback.onRequestedOrientation(false);
                                    }

                                }
                            });


                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        throwError(e);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCaption(String srtPath) {
        String path = "";
        try {
            if (srtPath.equals("")) {
                path = mSource.getPath();
                if (path.contains(".")) {
                    if (null != path && path.length() > 0) {
                        int endIndex = path.lastIndexOf(".");
                        if (endIndex != -1) {
                            path = path.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
                            path = path + ".srt";
                        }
                    }
                }
            } else {
                path = srtPath;
            }

            // path = path.replace(".mp4", ".srt");
            if (new File(path).exists()) {
                captionView.setPlayer(mPlayer);
                captionView.setSubSource(Uri.parse(path), SubtitleView.CMime.SUBRIP);

//                setCaptions(Uri.parse(path), CaptionsView.CMime.SUBRIP);
                mPlayer.addTimedTextSource(path, MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);
                int textTrackIndex = findTrackIndexFor(
                        MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT, mPlayer.getTrackInfo());
                if (textTrackIndex >= 0) {
                    if (mShowSubTitle) {
                        captionView.setVisible(true);
                        captionView.setVisibility(VISIBLE);
                    } else {
                        captionView.setVisible(false);
                        captionView.setVisibility(GONE);
                    }
                    captionView.setText("Subtitle Loading...");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        mPlayer.clearOnSubtitleDataListener();
                    }
                    mPlayer.selectTrack(textTrackIndex);
                } else {
                    captionView.setVisible(false);
                    captionView.setVisibility(GONE);
                    Log.w("test", "Cannot find text track!");
                }

                mPlayer.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
                    @Override
                    public void onTimedText(MediaPlayer mp, TimedText timedText) {
                        if (mShowSubTitle) {
                            if (timedText != null) {
                                Log.d("testsubtitleview", "subtitle: " + timedText.getText());

                                captionView.setVisibility(VISIBLE);
                                captionView.setVisible(true);
//                                captionView.setText(timedText.getText());
                            } else {
                                captionView.setVisible(false);
                                captionView.setVisibility(GONE);
                            }
                        } else {
                            captionView.setVisible(false);
                            captionView.setVisibility(GONE);
                        }
                    }
                });
            } else {
                mShowSubTitle = false;
                captionView.setVisible(false);
                captionView.setVisibility(GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            captionView.setVisible(false);
            captionView.setVisibility(GONE);
        }
    }


    @Override
    public void onTimedText(MediaPlayer mp, TimedText timedText) {
        Log.d("Vishal", "subtitle: " + timedText.getText());

        try {
            if (mShowSubTitle)
                if (timedText != null) {
                    Log.d("testsubtitleview", "subtitle: " + timedText.getText());
                    captionView.setVisible(true);
                    captionView.setVisibility(VISIBLE);
                    captionView.setText(timedText.getText());
                } else {
                    captionView.setVisible(false);
                    captionView.setVisibility(GONE);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int findTrackIndexFor(int mediaTrackType, MediaPlayer.TrackInfo[] trackInfo) {
        int index = -1;
        for (int i = 0; i < trackInfo.length; i++) {
            if (trackInfo[i].getTrackType() == mediaTrackType) {
                return i;
            }
        }
        return index;
    }

    private void setControlsEnabled(boolean z) {
        if (this.mSeeker != null) {
            this.mSeeker.setEnabled(z);
            this.mLabelPosition.setEnabled(z);
            this.mLabelPosition.setAlpha(z ? 1.0f : 0.4f);
            this.mClickFrame.setEnabled(z);
        }
    }

    public boolean mLockUnlock = false;

    public void showControls() {
        try {
            if (!mLockUnlock) {

                this.mCallback.onToggleControls(this, true);

                if (!(this.mControlsDisabled || isControlsShown())) {
                    if (this.mSeeker != null) {
                        this.mSeeker.setEnabled(true);
                        showViews();
                        showLock();
                        this.mControlsFrame.animate().cancel();
                        this.mControlsFrame.setAlpha(0.0f);
                        this.mControlsFrame.setVisibility(VISIBLE);
                        this.mControlsFrame.animate().alpha(1.0f).translationY(0.0f).setListener(null).setInterpolator(new DecelerateInterpolator()).start();
                        this.llTop.setVisibility(VISIBLE);


                        if (this.mBottomProgressBarVisibility) {
                            this.mBottomProgressBar.animate().cancel();
                            this.mBottomProgressBar.setAlpha(1.0f);
                            this.mBottomProgressBar.animate().alpha(0.0f).start();
                        }
                        if (this.mShowToolbar) {
                            this.mToolbarFrame.setBackgroundResource(R.drawable.topbar_background);
                            this.mToolbarFrame.animate().cancel();
                            this.mToolbarFrame.setAlpha(0.0f);
                            this.mToolbarFrame.setVisibility(VISIBLE);
                            this.mToolbarFrame.animate().alpha(1.0f).setListener(null).setInterpolator(new DecelerateInterpolator()).start();
                        }
                        rotationUnlock.setVisibility(VISIBLE);
                    }
                }
                if (this.mHandler != null) {
                    this.mHandler.removeCallbacks(this.hideControlsRunnable);
                }
                if (mPlayer.isPlaying()) {
                    extendControlShow();
                } else {

                }
            } else {
                showLock();
                hideLock();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void setLayoutOrei(boolean orei) {
        if (orei) {
            /*Potrait*/
            isLandScape = false;
            showViews();
        } else {
            isLandScape = true;
            showViews();
            /*Landscap*/

        }
    }

    public void hideControls() {
        try {
            if (!mLockUnlock) {

                if (!(this.mControlsDisabled || !isControlsShown() || this.mSeeker == null)) {
                    if (!this.isFingerOnSeek) {
                        this.mCallback.onToggleControls(this, false);
                        this.mSeeker.setEnabled(false);
                        hideViews();
                        this.mControlsFrame.animate().cancel();
                        this.mControlsFrame.setAlpha(1.0f);
                        this.mControlsFrame.setTranslationY(0.0f);
                        this.mControlsFrame.setVisibility(VISIBLE);
                        this.mControlsFrame.animate().alpha(0.0f).translationY((float) this.mControlsFrame.getHeight()).setInterpolator(new DecelerateInterpolator()).setListener(new Animation_1()).start();
                        this.llTop.setVisibility(VISIBLE);

                        final View view = (View) this.captionView.getParent();
                        view.animate().cancel();
                        view.animate().translationY((float) this.mControlsFrame.getHeight()).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animator) {
                                view.setTranslationY(0.0f);
                            }
                        }).start();
                        if (this.mBottomProgressBarVisibility) {
                            this.mBottomProgressBar.animate().cancel();
                            this.mBottomProgressBar.setAlpha(0.0f);
                            this.mBottomProgressBar.animate().alpha(1.0f).start();
                        }
                        if (this.mToolbarFrame.getVisibility() == VISIBLE) {
                            this.mToolbarFrame.setVisibility(GONE);
                        }
                        rotationUnlock.setVisibility(GONE);
                    }
                }

            } else {
                btnUnLock.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideToolbarWithAnimation() {
        if (this.mToolbarFrame.getVisibility() == VISIBLE) {
            this.mToolbarFrame.animate().cancel();
            this.mToolbarFrame.setAlpha(1.0f);
            this.mToolbarFrame.setVisibility(GONE);
            this.mToolbarFrame.animate().alpha(0.0f).setInterpolator(new DecelerateInterpolator()).setListener(new Animation_2()).start();
        }
    }

    @CheckResult
    public boolean isControlsShown() {
        return (this.mControlsDisabled || this.mControlsFrame == null || this.mControlsFrame.getAlpha() <= 0.5f) ? false : true;
    }

    public void toggleControls() {
        if (!this.mControlsDisabled) {
            if (isControlsShown()) {
                hideControls();
                return;
            }
            showControls();
        }
    }

    public void setBottomProgressBarVisibility(boolean z) {
        this.mBottomProgressBarVisibility = z;
        if (z) {
            this.mBottomProgressBar.setVisibility(VISIBLE);
        } else {
            this.mBottomProgressBar.setVisibility(GONE);
        }
    }

    public void setHideControlsDuration(int i) {
        this.mHideControlsDuration = i;
    }

    public int getHideControlsDuration() {
        return this.mHideControlsDuration;
    }

    public void enableControls() {
        this.mControlsDisabled = false;
        this.mClickFrame.setClickable(true);
        this.mClickFrame.setOnTouchListener(new ZoomOnTouchListeners());
    }

    public void disableControls() {
        this.mControlsDisabled = true;
        this.mControlsFrame.setVisibility(GONE);
        this.llTop.setVisibility(GONE);
        hidePlayPauseBtnSets();
        this.mToolbarFrame.setVisibility(GONE);
        this.mClickFrame.setOnTouchListener(null);
        this.mClickFrame.setClickable(false);
    }

    public boolean isPrepared() {
        return this.mPlayer != null && this.mIsPrepared;
    }

    public boolean isPlaying() {
        return this.mPlayer != null && this.mPlayer.isPlaying();
    }

    public int getCurrentPosition() {
        if (this.mPlayer == null) {
            return -1;
        }
        return this.mPlayer.getCurrentPosition();
    }

    public int getDuration() {
        if (this.mPlayer == null) {
            return -1;
        }
        return this.mPlayer.getDuration();
    }

    public void start() {
        if (this.mPlayer != null) {
            this.playPause.setImageResource(R.drawable.ic_pause);

            this.mPlayer.start();
            this.mCallback.onStarted(this);
            if (this.mHandler == null) {
                this.mHandler = new Handler();
            }
            showControls();
            this.mHandler.post(this.mUpdateCounters);
        }
    }


    public void seekTo(@IntRange(from = 0, to = 2147483647L) int i) {
        if (this.mPlayer != null) {
            this.mPlayer.seekTo(i);
        }
    }

    public TextureView getmTextureView() {
        return mTextureView;
    }

    public void setVolume(@FloatRange(from = 0.0d, to = 1.0d) float f, @FloatRange(from = 0.0d, to = 1.0d) float f2) {
        if (this.mPlayer != null) {
            if (this.mIsPrepared) {
                this.mPlayer.setVolume(f, f2);
                return;
            }
        }
        throw new IllegalStateException("You cannot use setVolume(float, float) until the bestvideoplayer is prepared.");
    }

    private MediaMetadataRetriever retriever;

    public void onResume() {
        if (this.mPlayer != null || !isPlaying()) {
            this.playPause.setImageResource(R.drawable.ic_pause);
            this.mPlayer.start();
            mTextureView.postDelayed(capturePreview, 1000);
//            hideControls();
            showControls();
            mTextureView.removeCallbacks(capturePreview);
            this.mHandler.post(this.mUpdateCounters);

        }
    }

    public void pause() {
        if (this.mPlayer != null || isPlaying()) {
            this.playPause.setImageResource(R.drawable.ic_play);
            this.mPlayer.pause();
            this.mCallback.onPaused(this);
            if (this.mHandler != null) {
                this.mHandler.removeCallbacks(this.hideControlsRunnable);
                this.mHandler.removeCallbacks(this.mUpdateCounters);
            }
        }
    }

    public void stop() {

        if (this.mPlayer != null) {
        }
        try {
            this.mPlayer.stop();
            if (this.mHandler != null) {
                this.mHandler.removeCallbacks(this.hideControlsRunnable);
                this.mHandler.removeCallbacks(this.mUpdateCounters);
                this.playPause.setImageResource(R.drawable.ic_play);
            }
            return;
        } catch (Throwable localThrowable) {
            throw localThrowable;
        }
    }

    public void reset() {
        if (this.mPlayer != null) {
            this.mIsPrepared = false;
            this.mPlayer.reset();
            if (this.mHandler != null) {
                this.mHandler.removeCallbacks(this.hideControlsRunnable);
            }
        }
    }

    public void release() {
        this.mIsPrepared = false;
        if (this.mPlayer != null) {
            try {
                mPlayer.stop();
                this.mPlayer.release();
                this.mPlayer = null;
                if (this.mHandler != null) {
                    this.mHandler.removeCallbacks(this.mUpdateCounters);
                    this.mHandler = null;
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void setCaptions(Uri uri, CaptionsView.CMime cMime) {

        this.mSubView.setCaptionsSource(uri, cMime);
    }


    public void removeCaptions() {
        setCaptions(null, null);
    }

    public Toolbar getToolbar() {
        return this.mToolbar;
    }

    public ImageView getBack() {
        return this.back_button;
    }
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        this.mInitialTextureWidth = i;
        this.mInitialTextureHeight = i2;
        this.mSurfaceAvailable = true;
        this.mSurface = new Surface(surfaceTexture);
        if (this.mIsPrepared) {
            this.mPlayer.setSurface(this.mSurface);
        } else {
            prepare();
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        this.mInitialTextureWidth = i;
        this.mInitialTextureHeight = i2;
//        hideControls();
        if (!isPlaying() && this.mIsPrepared) {
            this.mPlayer.setVolume(0.0f, 0.0f);
            this.mPlayer.start();
            this.mPlayer.pause();
            this.playPause.setImageResource(R.drawable.ic_play);
            this.mPlayer.setVolume(1.0f, 1.0f);
        }
        adjustAspectRatio(i, i2, this.mPlayer.getVideoWidth(), this.mPlayer.getVideoHeight());
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        this.mSurfaceAvailable = false;
        this.mSurface = null;
        return false;
    }

    @TargetApi(16)
    public void onPrepared(MediaPlayer mediaPlayer) {

        this.mPlayer.setOnVideoSizeChangedListener(this);
        this.mProgressBar.setVisibility(INVISIBLE);
        this.mIsPrepared = true;
        if (this.mCallback != null) {
            this.mCallback.onPrepared(this);
        }
        this.mLabelPosition.setText(Util.getDurationString(0, false));
        this.mLabelDuration.setText(Util.getDurationString((long) mediaPlayer.getDuration(), false));
        this.mSeeker.setProgress(0);
        this.mSeeker.setMax(mediaPlayer.getDuration());
        setControlsEnabled(true);
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    if (mHandler != null) {
                        mHandler.removeCallbacks(mUpdateCounters);
                    }
                    int i = mSeeker.getMax();
                    mSeeker.setProgress(i);
                    mBottomProgressBar.setProgress(i);
                    if (!mLoop) {
                        playPause.setImageResource(R.drawable.ic_play);
                        if (!isONzooming) {
                            //                        showControls();
                        }
                    } else {
                        start();
                    }
                    if (mCallback != null) {
                        mCallback.onCompletion(CustomVideoPlayer.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (this.mAutoPlay) {
            if (this.mControlsDisabled && this.mHideControlsOnPlay) {
                this.mHandler.postDelayed(this.hideControlsRunnable, 500);
            }
            start();
            if (this.mInitialPosition > 0) {
                seekTo(this.mInitialPosition);
                this.mInitialPosition = -1;
                return;
            }
            return;
        }

        this.mPlayer.start();
        this.mPlayer.pause();


    }

    public void onBufferingUpdate(MediaPlayer paramMediaPlayer, int paramInt) {
        if (mCallback != null) {
            this.mCallback.onBuffering(paramInt);
        }
        if (this.mSeeker != null) {
            if (paramInt == 100) {
                this.mSeeker.setSecondaryProgress(0);
                this.mBottomProgressBar.setSecondaryProgress(0);
                return;
            }
            float f = paramInt / 100.0F;
            paramInt = (int) (this.mSeeker.getMax() * f);
            this.mSeeker.setSecondaryProgress(paramInt);
            this.mBottomProgressBar.setSecondaryProgress(paramInt);
        }
    }

    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
        adjustAspectRatio(this.mInitialTextureWidth, this.mInitialTextureHeight, i, i2);
    }

    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        if (i == -38) {
            return false;
        }

        return false;
    }

    public boolean seekbarTracking = false;

    protected void onFinishInflate() {
        super.onFinishInflate();
        setKeepScreenOn(true);

        try {

            this.am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            LayoutInflater from = LayoutInflater.from(getContext());
            View inflate = from.inflate(R.layout.include_surface, this, false);
            addView(inflate);
            this.mTextureView = (TextureView) inflate.findViewById(R.id.textureview);
            this.mTextureView.setSurfaceTextureListener(this);
            this.nightView = inflate.findViewById(R.id.night_view);
            this.mProgressFrame = from.inflate(R.layout.include_progress, this, false);
            this.mProgressBar = mProgressFrame.findViewById(R.id.spin_kit);
            this.mBottomProgressBar = (ProgressBar) this.mProgressFrame.findViewById(R.id.progressBarBottom);
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(androidx.appcompat.R.attr.colorAccent, typedValue, true);
            setLoadingStyle(this.mLoadingStyle);
            this.mPositionTextView = (TextView) this.mProgressFrame.findViewById(R.id.position_textview);
            this.mPositionTextView.setShadowLayer(3.0f, 3.0f, 3.0f, ViewCompat.MEASURED_STATE_MASK);
            this.mImageView = (ImageView) this.mProgressFrame.findViewById(R.id.action_image);
            this.actionLayout = (LinearLayout) this.mProgressFrame.findViewById(R.id.action_layout);
            addView(this.mProgressFrame);
            this.mClickFrame = new FrameLayout(getContext());
            ((FrameLayout) this.mClickFrame).setForeground(Util.resolveDrawable(getContext(), com.airbnb.lottie.R.attr.selectableItemBackground));
            addView(this.mClickFrame, new ViewGroup.LayoutParams(-1, -1));
            this.mContainerView = from.inflate(R.layout.container_view, this, false);
            ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -2);
            ((LayoutParams) layoutParams).addRule(15);
            ((LayoutParams) layoutParams).addRule(14);
            addView(this.mContainerView, layoutParams);
            this.llTop = this.mContainerView.findViewById(R.id.llTop);

            this.mControlsFrame = this.mContainerView.findViewById(R.id.seeek_controls);

            this.previewImageview = this.mControlsFrame.findViewById(R.id.imageView);
            this.previewFrameLayout = this.mControlsFrame.findViewById(R.id.previewFrameLayout);
            this.aspectRatioPort = (ImageView) this.mControlsFrame.findViewById(R.id.aspect_ratio);
            this.aspectRatioPort.setOnClickListener(this);
            this.videoFloatPort = (ImageView) this.mControlsFrame.findViewById(R.id.video_float);
            this.videoFloatPort.setOnClickListener(this);
            this.audioOnlyPort = (ImageView) this.mControlsFrame.findViewById(R.id.audio_only);
            this.audioOnlyPort.setOnClickListener(this);
            this.rotationUnlockPort = (ImageView) this.mControlsFrame.findViewById(R.id.screen_rotn);
            this.rotationUnlockPort.setOnClickListener(this);
            this.repeatPort = (ImageView) this.mControlsFrame.findViewById(R.id.repeat);
            this.repeatPort.setOnClickListener(this);
            this.nightModePort = (ImageView) this.mControlsFrame.findViewById(R.id.night_mode_toggler);
            this.nightModePort.setOnClickListener(this);
            this.include = this.mControlsFrame.findViewById(R.id.include_portrait);
            aspectDefualt1 = (ImageView) this.include.findViewById(R.id.aspect_defualt);
            this.aspectDefualt1.setOnClickListener(this);
            this.mToolbarFrame = this.mContainerView.findViewById(R.id.toolbar);
            this.mToolbar = (Toolbar) this.mToolbarFrame.findViewById(R.id.toolbar);
            this.back_button = (ImageView) this.mToolbarFrame.findViewById(R.id.back_button);
            this.toolbarTitle = (TextView) this.mToolbarFrame.findViewById(R.id.toolbar_title);
            this.btnScreenshort = (ImageView) this.mToolbarFrame.findViewById(R.id.btnScreenshort);
            this.btnScreenshort.setOnClickListener(this);
            this.btnSubtitle = (ImageView) this.mToolbarFrame.findViewById(R.id.btnSubtitle);
            this.btnSubtitle.setOnClickListener(this);

            this.btnLock = (ImageView) this.mContainerView.findViewById(R.id.btnLock);
            this.btnLock.setOnClickListener(this);
            this.btnUnLock = (ImageView) this.mContainerView.findViewById(R.id.btnUnLock);
            this.btnUnLock.setOnClickListener(this);
            this.lockLayout = (LinearLayout) mContainerView.findViewById(R.id.lockLayout);
            this.btnEqualizer = (ImageView) this.mToolbarFrame.findViewById(R.id.btnEqualizer);
            this.btnEqualizer.setOnClickListener(this);
            this.toolbarTitle.setText(this.mTitle);
            this.mToolbarFrame.setVisibility(this.mShowToolbar ? VISIBLE : GONE);
            this.playPause = (ImageView) this.mContainerView.findViewById(R.id.play_pause);
            this.playPause.setOnClickListener(this);
            this.playNext = (ImageView) this.mContainerView.findViewById(R.id.play_next);
            this.playNext.setOnClickListener(this);
            this.playPrevious = (ImageView) this.mContainerView.findViewById(R.id.play_prev);
            this.playPrevious.setOnClickListener(this);
            this.playBackward = (ImageView) this.mContainerView.findViewById(R.id.play_backward);
            this.playBackward.setOnClickListener(this);
            this.playForward = (ImageView) this.mContainerView.findViewById(R.id.play_forward);
            this.playForward.setOnClickListener(this);
            this.mLinearLayout = (LinearLayout) this.mContainerView.findViewById(R.id.linear_layout);
            this.aspectRatio = (ImageView) this.mContainerView.findViewById(R.id.aspect_ratio);
            this.aspectRatio.setOnClickListener(this);
            this.aspectDefualt = (ImageView) this.mLinearLayout.findViewById(R.id.aspect_defualt);
            this.aspectDefualt.setOnClickListener(this);
            this.videoFloat = (ImageView) this.mContainerView.findViewById(R.id.video_float);
            this.videoFloat.setOnClickListener(this);
            this.audioOnly = (ImageView) this.mContainerView.findViewById(R.id.audio_only);
            this.audioOnly.setOnClickListener(this);
            this.mScreenRotLayout = (LinearLayout) this.mContainerView.findViewById(R.id.screen_rot_layout);
            this.rotationUnlock = (ImageView) this.mContainerView.findViewById(R.id.screen_rotn);
            this.rotationUnlock.setOnClickListener(this);
            this.btnVolume = (ImageView) this.mContainerView.findViewById(R.id.btnVolume);
            this.btnVolume.setOnClickListener(this);
            this.repeat = (ImageView) this.mContainerView.findViewById(R.id.repeat);
            this.repeat.setOnClickListener(this);
            this.nightMode = (ImageView) this.mContainerView.findViewById(R.id.night_mode_toggler);
            this.nightMode.setOnClickListener(this);

            this.mSwitchLayout = (LinearLayout) this.mContainerView.findViewById(R.id.switch_layout);
            this.captionView = (SubtitleView) this.mContainerView.findViewById(R.id.captionView);
            layoutParams = new LayoutParams(-1, -2);
            ((LayoutParams) layoutParams).addRule(2, R.id.include_relativelayout);
            ((LayoutParams) layoutParams).alignWithParent = true;
            ((LayoutParams) layoutParams).alignWithParent = true;
            captionView.setLayoutParams(layoutParams);
            this.alignSwitch = (ImageView) this.mSwitchLayout.findViewById(R.id.align_switch);
            this.alignSwitch.setOnClickListener(this);
            this.layout_equilizer = (LinearLayout) this.mContainerView.findViewById(R.id.layout_equilizer);


            View inflate2 = from.inflate(R.layout.include_subtitle, this, false);
            layoutParams = new LayoutParams(-1, -2);
            ((LayoutParams) layoutParams).addRule(2, R.id.include_relativelayout);
            ((LayoutParams) layoutParams).alignWithParent = true;
            this.mSubView = (CaptionsView) inflate2.findViewById(R.id.subs_box);
            this.mSubView.setPlayer(this.mPlayer);
            this.mSubView.setTextSize(0, (float) this.mSubViewTextSize);
            this.mSubView.setTextColor(this.mSubViewTextColor);
            addView(inflate2, layoutParams);
            this.mSeeker = (SeekBar) this.mControlsFrame.findViewById(R.id.seeker);

            this.mSeeker.setOnSeekBarChangeListener(this);
            this.mLabelPosition = (TextView) this.mControlsFrame.findViewById(R.id.position);
            this.mLabelPosition.setText(Util.getDurationString(0, false));
            this.mLabelDuration = (TextView) this.mControlsFrame.findViewById(R.id.duration);
            this.mLabelDuration.setText(Util.getDurationString(0, true));
            this.mLabelDuration.setOnClickListener(this);
            if (this.mControlsDisabled) {
                disableControls();
            } else {
                enableControls();
            }
            setBottomProgressBarVisibility(this.mBottomProgressBarVisibility);
            setControlsEnabled(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
//        prepare();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btnScreenshort) {
            if (mTextureView != null) {
                mCallback.onTakeScreenShort(true, mTextureView.getBitmap());
            }

        }
        if (view.getId() == R.id.btnVolume) {
            if (!mVolume) {
                mVolume = true;
                am.setStreamVolume(3, 0, AudioManager.FLAG_SHOW_UI);
                btnVolume.setImageDrawable(getResources().getDrawable(R.drawable.ic_mute));
            } else {
                mVolume = false;
                if (finalVolume == 0) {
                    am.setStreamVolume(3, maxVolume, AudioManager.FLAG_SHOW_UI);
                } else {
                    am.setStreamVolume(3, finalVolume, AudioManager.FLAG_SHOW_UI);
                }

                btnVolume.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_on));
            }

        }
        if (view.getId() == R.id.btnSubtitle) {
            try {
                File file = new File(mSource.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (view.getId() == R.id.btnLock) {
            hideControls();
            mLockUnlock = true;
            lockLayout.setVisibility(VISIBLE);
        }
        if (view.getId() == R.id.btnUnLock) {
            mLockUnlock = false;
            lockLayout.setVisibility(GONE);
            showControls();
            btnUnLock.setImageDrawable(getResources().getDrawable(R.drawable.ic_hidden));
            lockLayout.setVisibility(GONE);

        }
        if (view.getId() == R.id.btnEqualizer) {
            this.mCallback.Eq(mPlayer.getAudioSessionId());


        }
        if (view.getId() == R.id.duration) {
//            extendControlShow();
            this.mShowTotalDuration ^= true;
        } else if (view.getId() != R.id.position) {
            if (view.getId() == R.id.play_pause) {
//                extendControlShow();
                if (this.mPlayer.isPlaying()) {
                    this.mCallback.onPauseBtnClick(this, true);
                    this.playPause.setImageResource(R.drawable.ic_pause);
                    pause();

                    return;
                }
                this.playPause.setImageResource(R.drawable.ic_play);

                start();

            } else if (view.getId() == R.id.play_next) {
//                extendControlShow();

                this.mCallback.onPlayNextClick(this);
                this.playPrevious.setVisibility(VISIBLE);
                this.isNoPrevVideo = false;
                this.isZomedView = false;
                this.aspectRatioNumber = 0;
                adjustAspectRatio(this.mInitialTextureWidth, this.mInitialTextureHeight, this.mPlayer.getVideoWidth(), this.mPlayer.getVideoHeight());
            } else if (view.getId() == R.id.play_prev) {
//                extendControlShow();
                this.mCallback.onPlayPrevClick(this);
                this.playNext.setVisibility(VISIBLE);
                this.isNoNextVideo = false;
                this.isZomedView = false;
                this.aspectRatioNumber = 0;
                adjustAspectRatio(this.mInitialTextureWidth, this.mInitialTextureHeight, this.mPlayer.getVideoWidth(), this.mPlayer.getVideoHeight());
            } else if (view.getId() == R.id.play_forward) {
//                extendControlShow();

                boolean play;
                if (mPlayer.isPlaying()) {
                    play = true;
                } else {
                    play = false;
                }
                if (mPlayer != null) {
                    long totalduration = mPlayer.getDuration();
                    long currentDuration = mPlayer.getCurrentPosition();

                    if (currentDuration >= totalduration) {
                        mCallback.onPlayNextClick(CustomVideoPlayer.this);
                    } else {
                        mPlayer.pause();
                        currentDuration += 10000;
                        mPlayer.seekTo((int) currentDuration);
                        mSeeker.setProgress((int) currentDuration);
                        CustomVideoPlayer.this.mLabelPosition.setText(Util.getDurationString(currentDuration, false));
                        if (play) {
                            mPlayer.start();
                        }
                    }
                }
//                this.mCallback.onPlayForward(this);
                this.isNoNextVideo = false;
                this.isZomedView = false;
                this.aspectRatioNumber = 0;
                adjustAspectRatio(this.mInitialTextureWidth, this.mInitialTextureHeight, this.mPlayer.getVideoWidth(), this.mPlayer.getVideoHeight());
            } else if (view.getId() == R.id.play_backward) {
//                extendControlShow();

                boolean play;
                if (mPlayer.isPlaying()) {
                    play = true;
                } else {
                    play = false;
                }
                if (mPlayer != null) {
                    long totalduration = mPlayer.getDuration();
                    long currentDuration = mPlayer.getCurrentPosition();
                    if (currentDuration >= 0) {
                        if (currentDuration <= totalduration) {
                            mPlayer.pause();
                            currentDuration -= 10000;
                            mPlayer.seekTo((int) currentDuration);
                            mSeeker.setProgress((int) currentDuration);
                            CustomVideoPlayer.this.mLabelPosition.setText(Util.getDurationString(currentDuration, false));
                            if (play) {
                                mPlayer.start();
                            }
                        } else {

                        }
                    }

                }
//                this.mCallback.onPlayBackward(this);
                this.isNoNextVideo = false;
                this.isZomedView = false;
                this.aspectRatioNumber = 0;
                adjustAspectRatio(this.mInitialTextureWidth, this.mInitialTextureHeight, this.mPlayer.getVideoWidth(), this.mPlayer.getVideoHeight());
            } else if (view.getId() == R.id.repeat) {
//                extendControlShow();
                this.mLoop ^= true;
                view = this.repeat;
                boolean z = this.mLoop;
                int i = R.drawable.ic_repeat;
                repeat.setImageResource(z ? R.drawable.ic_repeat_on : R.drawable.ic_repeat);
                view = this.repeatPort;
                if (this.mLoop) {
                    i = R.drawable.ic_repeat_on;
                }
                repeatPort.setImageResource(i);
                this.mLoop = this.mLoop;
                Toast.makeText(this.context, this.mLoop ? "Repeat ON" : "Repeat OFF", Toast.LENGTH_SHORT).show();
            } else if (view.getId() == R.id.aspect_ratio) {
//                extendControlShow();
                this.isZomedView = false;
                this.isAspectChanged = true;
                this.aspectRatioNumber += 1;
                if (this.aspectRatioNumber == 5) {
                    this.aspectRatioNumber = 0;
                }
                adjustAspectRatio(this.mInitialTextureWidth, this.mInitialTextureHeight, this.mPlayer.getVideoWidth(), this.mPlayer.getVideoHeight());
            } else if (view.getId() == R.id.aspect_defualt) {
//                extendControlShow();
                this.isZomedView = false;
                this.isAspectChanged = true;
                aspectRatioNumber = 5;
                adjustAspectRatio(this.mInitialTextureWidth, this.mInitialTextureHeight, this.mPlayer.getVideoWidth(), this.mPlayer.getVideoHeight());
            } else if (view.getId() == R.id.video_float) {
                this.mCallback.onVFloatBtnClick(this, true);
            } else if (view.getId() == R.id.audio_only) {
                noti();


            } else if (view.getId() == R.id.screen_rotn) {

                this.isZomedView = false;
                this.isRotationLocked ^= true;
                int orientation = this.getResources().getConfiguration().orientation;
                if (this.isRotationLocked) {
                    this.rotationUnlock.setImageResource(R.drawable.ic_rotate);
                    this.rotationUnlockPort.setImageResource(R.drawable.ic_rotate);
//                    this.mCallback.onRotLockBtnClick(this, true);
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        isLandScape = true;
                        mCallback.onRequestedOrientation(false);
                    } else {
                        isLandScape = false;
                        mCallback.onRequestedOrientation(true);
                    }

                } else {
                    this.rotationUnlock.setImageResource(R.drawable.ic_rotate);
                    this.rotationUnlockPort.setImageResource(R.drawable.ic_rotate);
//                    this.mCallback.onRotLockBtnClick(this, false);
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        isLandScape = true;
                        mCallback.onRequestedOrientation(false);
                    } else {
                        isLandScape = false;
                        mCallback.onRequestedOrientation(true);
                    }
                }
            } else if (view.getId() == R.id.night_mode_toggler) {
                if (this.isNightMdON) {
                    isNightMdON = false;
                    this.nightMode.setImageResource(R.drawable.ic_night_mode);
                    this.nightModePort.setImageResource(R.drawable.ic_night_mode);
                    this.nightView.setVisibility(GONE);
                } else {
                    isNightMdON = true;
                    this.nightMode.setImageResource(R.drawable.ic_night_mode);
                    this.nightModePort.setImageResource(R.drawable.ic_night_mode);
                    this.nightView.setVisibility(VISIBLE);
                }
                Toast.makeText(this.context, this.isNightMdON ? "Night Mode ON" : "Night Mode OFF", Toast.LENGTH_SHORT).show();
            } else if (view.getId() == R.id.align_switch) {
//                extendControlShow();
                this.isDefaultPos ^= true;
            }
        }
    }







    public Runnable hideLockHandler = new Runnable() {
        @Override
        public void run() {
            if (mLockUnlock) {
                btnUnLock.setVisibility(GONE);
            }
        }
    };

    public void hideLock() {

        mHandler.postDelayed(hideLockHandler, 3000);

    }

    public void showLock() {
        mHandler.removeCallbacks(hideLockHandler);
        btnUnLock.setVisibility(VISIBLE);
        mHandler.postDelayed(hideLockHandler, 3000);
    }

    public void hideViews() {
        hidePlayPauseBtnSets();
        this.mLinearLayout.setVisibility(GONE);
        this.mScreenRotLayout.setVisibility(GONE);
        this.mSwitchLayout.setVisibility(GONE);
        this.include.setVisibility(GONE);
        openTopIconInVisible();
        llTop.setVisibility(VISIBLE);

    }

    public void showViews() {

        if (!mLockUnlock) {
            showPlayPauseBtnSets();

            this.include.setVisibility(GONE);
            this.mLinearLayout.setVisibility(GONE);
            this.mScreenRotLayout.setVisibility(GONE);
            this.mSwitchLayout.setVisibility(GONE);
            btnLock.setVisibility(VISIBLE);
            llTop.setVisibility(VISIBLE);

        } else {
            btnUnLock.setVisibility(VISIBLE);
        }
    }

    boolean openIcon = false;

    public void openTopIconVisible() {
        openIcon = true;
        repeat.setVisibility(VISIBLE);
        nightMode.setVisibility(VISIBLE);
        videoFloat.setVisibility(VISIBLE);
        audioOnly.setVisibility(VISIBLE);
    }

    public void openTopIconInVisible() {
        openIcon = false;
        repeat.setVisibility(GONE);
        nightMode.setVisibility(GONE);
        videoFloat.setVisibility(GONE);
        audioOnly.setVisibility(GONE);
    }


    public void LinearLayoutPos() {
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.addRule(15);
        if (this.isLeftHandHold) {
            layoutParams.addRule(0, 0);
            if (this.isDefaultPos) {
                layoutParams.addRule(1, R.id.screen_rot_layout);
            } else {
                layoutParams.addRule(1, R.id.linear_layout);
            }
        } else {
            layoutParams.addRule(1, 0);
            if (this.isDefaultPos) {
                layoutParams.addRule(0, R.id.linear_layout);
            } else {
                layoutParams.addRule(0, R.id.screen_rot_layout);
            }
        }
        this.mSwitchLayout.setLayoutParams(layoutParams);
    }

    int pos = 0;

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {

        pos = i;
        if (z) {
            seekTo(i);
            this.mPositionTextView.setText(Util.getDurationString((long) i, false));

        }

    }


    public void onStartTrackingTouch(SeekBar seekBar) {
        this.isFingerOnSeek = true;
        this.mWasPlaying = isPlaying();
        if (this.mWasPlaying) {
            this.mPlayer.pause();
        }
        showPreviewImage();
        showPlayPauseBtnSets();
//        hidePlayPauseBtnSets();
        this.actionLayout.setVisibility(VISIBLE);
        this.mPositionTextView.setVisibility(VISIBLE);
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        this.isFingerOnSeek = false;
        if (this.mWasPlaying) {
            this.mPlayer.start();
        } else {
            showPlayPauseBtnSets();
        }

        hidePreviewImage();
        this.actionLayout.setVisibility(GONE);
        this.mPositionTextView.setVisibility(GONE);
        extendControlShow();
    }

    Bitmap bitmap;
    private Runnable capturePreview = new Runnable() {
        @Override
        public void run() {
            bitmap = mTextureView.getBitmap();
            // Run again after approximately 1 second.
            showPreviewImage();
            mTextureView.postDelayed(this, 100);
        }
    };

    public void showPreviewImage() {
        if (mTextureView != null) {
//            Bitmap bitmap = mTextureView.getBitmap();
            if (bitmap != null) {
                previewFrameLayout.setVisibility(VISIBLE);
                previewImageview.setImageBitmap(bitmap);

            }
        }
    }

    public void hidePreviewImage() {
        if (previewFrameLayout.getVisibility() == VISIBLE) {
            previewFrameLayout.setVisibility(GONE);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MediaPlayer mediaPlayer = this.mPlayer;
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        release();
        this.mSeeker = null;
        this.mLabelPosition = null;
        this.mLabelDuration = null;
        this.mControlsFrame = null;
        this.mClickFrame = null;
        this.mProgressFrame = null;
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mUpdateCounters);
            this.mHandler = null;
        }
    }

    private static void LOG(String str, Object... objArr) {
        if (objArr != null && objArr.length > 0) {
            String.format(str, objArr);
        }
    }

    private void adjustAspectRatio(int i, int i2, int i3, int i4) {
        int i5 = i;
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        if (this.isZomedView) {
            matrix.postTranslate(zoomXoff, this.zoomYoff);
            this.mTextureView.setTransform(this.matrix);
            return;
        }
        double d;
        CharSequence charSequence;
        int i9;
        boolean z = true;
        if (this.aspectRatioNumber == 1) {
            d = i8 > i7 ? 1.7777777777777777d : 0.5625d;
            charSequence = " 16 : 9 ";
        } else if (this.aspectRatioNumber == 2) {
            d = i8 > i7 ? 2.0d : 0.5d;
            charSequence = " 18 : 9 ";
        } else if (this.aspectRatioNumber == 3) {
            d = i8 > i7 ? 2.111111111111111d : 0.47368421052631576d;
            charSequence = " 19 : 9 ";
        } else if (this.aspectRatioNumber == 4) {
            d = i8 > i7 ? 1.6666666666666667d : 0.6d;
            charSequence = " 5 : 3 ";
        } else {
            d = ((double) i8) / ((double) i7);
            charSequence = "Default";
            this.aspectRatioNumber = 0;
        }
        if (this.isAspectChanged) {
            this.mPositionTextView.setText(charSequence);
            this.actionLayout.setVisibility(VISIBLE);
            this.mPositionTextView.setVisibility(VISIBLE);
//            hidePlayPauseBtnSets();
            showPlayPauseBtnSets();
            this.mHandler.removeCallbacks(this.hideAspTxtRunnable);
            this.mHandler.postDelayed(this.hideAspTxtRunnable, 2000);
            this.isAspectChanged = false;
        }
        if (this.isRotationLocked && !this.isONzooming) {
            this.mCallback.onRotLockBtnClick(this, false);
            this.isRotationLocked = false;
            this.rotationUnlock.setImageResource(R.drawable.ic_rotate);
            this.rotationUnlockPort.setImageResource(R.drawable.ic_rotate);
        }
        double d2 = ((double) i5) * d;
        int i10 = (int) d2;
        if (i6 > i10) {
            i9 = i10;
            i10 = i5;
        } else {
            i10 = (int) (((double) i6) / d);
            i9 = i6;
        }
        if (i5 <= i6) {
            z = false;
        }
//        this.isLandScape = z;
        this.xoff = (i5 - i10) / 2;
        this.yoff = (i6 - i9) / 2;
        float f = (float) i10;
        this.origWidth = f;
        float f2 = (float) i9;
        this.origHeight = f2;
        this.saveScale = 1.0f;
        this.mTextureView.getTransform(this.matrix);
        float f3 = (float) i5;
        float f4 = (float) i6;
        this.matrix.setScale(f / f3, f2 / f4);
        this.matrix.postTranslate((float) this.xoff, (float) this.yoff);
        this.mTextureView.setTransform(this.matrix);
        this.mCallback.onZoom(this, false);
        if (i7 > i8) {
            if (i5 > i6) {
                this.viewFloatWidth = f4;
                this.viewFloatHeight = (float) (((double) i6) * d);
            } else {
                this.viewFloatWidth = f3;
                this.viewFloatHeight = (float) d2;
            }
        } else if (i5 > i6) {
            this.viewFloatWidth = 0.6f * f4;
            this.viewFloatHeight = (float) ((((double) i6) * d) * 0.6000000238418579d);
        } else {
            this.viewFloatWidth = 0.6f * f3;
            this.viewFloatHeight = (float) (d2 * 0.6000000238418579d);
        }
        this.mCallback.videoSize(this, this.viewFloatWidth, this.viewFloatHeight, f3, f4, (float) i7);
    }

    private void throwError(Exception exception) {
        if (this.mCallback != null) {
            this.mCallback.onError(this, exception);
            return;
        }
        throw new RuntimeException(exception);
    }

    public void setLoop(boolean z) {
        this.mLoop = z;
    }


    public void noti(){
        this.mCallback.onAudioOnlyBtnClick(this, true);

    }







}
