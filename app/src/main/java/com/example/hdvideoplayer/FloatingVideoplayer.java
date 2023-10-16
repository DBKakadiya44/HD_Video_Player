package com.example.hdvideoplayer;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;



public class FloatingVideoplayer extends Service implements OnAudioFocusChangeListener, OnPreparedListener, Callback {
    private TextView backText;
    private ImageView backward;
    private ImageView closeButton;
    private Intent dummyNotif;
    private TextView duration;
    private ImageView forward;
    private TextView forwawrdText;
    private ImageView fullScreen;
    private boolean is1080pVideo = false;
    private boolean isControlShown = false;
    private boolean isInterrupted = false;
    private AudioManager mAudioManager;
    private View mFloatingView;
    private RelativeLayout mRelativeLayout;
    private WindowManager mWindowManager;
    private MediaPlayer mediaPlayer;
    private Runnable mediaRunnable;
    private ImageView minusBtn;
    private Button playButton;
    private ImageView plusBtn;
    private int position;
    private SharedPreferences prefs;
    private TextView progress;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("android.media.AUDIO_BECOMING_NOISY") && FloatingVideoplayer.this.mediaPlayer != null && FloatingVideoplayer.this.mediaPlayer.isPlaying()) {
                FloatingVideoplayer.this.pauseVideo();
            }
        }
    };
    private ImageView repeat;
    private float scaleFactor;
    private SurfaceHolder surfaceHolder;
    private Runnable textRunnable;
    private final Handler timerHandler = new Handler();
    private final Handler toggleHandler = new Handler();
    private Runnable toggleRunnable;
    private final Runnable updater = new Runnable() {
        public void run() {
            FloatingVideoplayer.this.progress.setText(Util.getDurationString((long) FloatingVideoplayer.this.mediaPlayer.getCurrentPosition(), false));
            FloatingVideoplayer.this.timerHandler.postDelayed(this, 1000);
        }
    };
    private float videoHeight;
    private String videoPath;
    private float videoWidth;

    class close_button_call implements OnClickListener {
        close_button_call() {
        }

        public void onClick(View view) {
            FloatingVideoplayer.this.stopSelf();
        }
    }

    class play_button_call implements OnClickListener {
        play_button_call() {
        }

        public void onClick(View view) {
            if (view.getId() == R.id.play_btn) {
                if (FloatingVideoplayer.this.mediaPlayer.isPlaying()) {
                    FloatingVideoplayer.this.pauseVideo();
                    FloatingVideoplayer.this.pauseVideo();
                } else {
                    FloatingVideoplayer.this.playVideo();
                }
                FloatingVideoplayer.this.toggleHandler.removeCallbacksAndMessages(null);
                FloatingVideoplayer.this.toggleHandler.postDelayed(FloatingVideoplayer.this.toggleRunnable, 2000);
            }
        }
    }

    class full_screen_button_call implements OnClickListener {
        full_screen_button_call() {
        }

        public void onClick(View view) {
            try {
                FloatingVideoplayer.this.position = FloatingVideoplayer.this.mediaPlayer.getCurrentPosition();
                Intent intent = new Intent(FloatingVideoplayer.this, Preview.class);
                intent.putExtra("AudioPath", FloatingVideoplayer.this.videoPath);
                intent.putExtra("duration", FloatingVideoplayer.this.position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                FloatingVideoplayer.this.startActivity(intent);
                FloatingVideoplayer.this.stopSelf();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class forword_button_call implements OnClickListener {
        forword_button_call() {
        }

        public void onClick(View view) {
            FloatingVideoplayer.this.mediaPlayer.seekTo(FloatingVideoplayer.this.mediaPlayer.getCurrentPosition() + 10000);
            FloatingVideoplayer.this.toggleHandler.removeCallbacksAndMessages(null);
            FloatingVideoplayer.this.toggleHandler.postDelayed(FloatingVideoplayer.this.toggleRunnable, 2000);
            FloatingVideoplayer.this.backText.setVisibility(View.GONE);
            FloatingVideoplayer.this.forwawrdText.setVisibility(View.VISIBLE);
            FloatingVideoplayer.this.toggleHandler.postDelayed(FloatingVideoplayer.this.textRunnable, 1000);
        }
    }

    class backword_button_call implements OnClickListener {
        backword_button_call() {
        }

        public void onClick(View view) {
            FloatingVideoplayer.this.mediaPlayer.seekTo(FloatingVideoplayer.this.mediaPlayer.getCurrentPosition() - 10000);
            FloatingVideoplayer.this.toggleHandler.removeCallbacksAndMessages(null);
            FloatingVideoplayer.this.toggleHandler.postDelayed(FloatingVideoplayer.this.toggleRunnable, 2000);
            FloatingVideoplayer.this.forwawrdText.setVisibility(View.GONE);
            FloatingVideoplayer.this.backText.setVisibility(View.VISIBLE);
            FloatingVideoplayer.this.toggleHandler.postDelayed(FloatingVideoplayer.this.textRunnable, 1000);
        }
    }

    class repeat_button_call implements OnClickListener {
        repeat_button_call() {
        }

        public void onClick(View view) {
            FloatingVideoplayer.this.toggleHandler.removeCallbacksAndMessages(null);
            FloatingVideoplayer.this.toggleHandler.postDelayed(FloatingVideoplayer.this.toggleRunnable, 2000);
            if (FloatingVideoplayer.this.mediaPlayer.isLooping()) {
                Toast.makeText(FloatingVideoplayer.this, "Repeat OFF", Toast.LENGTH_LONG).show();
                FloatingVideoplayer.this.mediaPlayer.setLooping(false);
                FloatingVideoplayer.this.repeat.setImageResource(R.drawable.ic_repeat_off);
                return;
            }
            Toast.makeText(FloatingVideoplayer.this, "Repeat ON", Toast.LENGTH_LONG).show();
            FloatingVideoplayer.this.mediaPlayer.setLooping(true);
            FloatingVideoplayer.this.repeat.setImageResource(R.drawable.ic_repeat_on);
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            try {
                stopSelf();
                onDestroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int i, int i2) {
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("floatingstop"));
        this.dummyNotif = new Intent(this, DumyNotification.class);
        this.dummyNotif.setAction(AudioServiceConstants.ACTION.FLOAT_VID_STARTFOREGROUND);
        startService(this.dummyNotif);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.media.AUDIO_BECOMING_NOISY");
        registerReceiver(this.receiver, intentFilter);
        this.videoPath = (String) intent.getExtras().get("audioPath");
        this.position = intent.getIntExtra("Duration", 0);
        this.videoWidth = intent.getFloatExtra("Width", 0.0f);
        this.videoHeight = intent.getFloatExtra("Height", 0.0f);
        this.is1080pVideo = intent.getBooleanExtra("is1080p", false);
        final LayoutParams layoutParams = new LayoutParams(-2, -2, VERSION.SDK_INT < 26 ? 2002 : 2038, 136, -3);
        layoutParams.gravity = 51;
        layoutParams.x = 0;
        layoutParams.y = 100;
        this.mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (this.mFloatingView != null) {
            if (this.mediaPlayer != null) {
                this.mediaPlayer.stop();
                this.mediaPlayer.release();
                this.mediaPlayer = null;
            }
            if (mAudioManager != null)
                this.mAudioManager.abandonAudioFocus(this);
            this.timerHandler.removeCallbacks(this.updater);
            this.toggleHandler.removeCallbacksAndMessages(null);
            this.mWindowManager.removeView(this.mFloatingView);
        } else {
            this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        }
        this.mFloatingView = LayoutInflater.from(this).inflate(R.layout.video_floating_view, null);
        this.scaleFactor = this.prefs.getFloat("ScaleFact", 1.0f);
        this.mRelativeLayout = (RelativeLayout) this.mFloatingView.findViewById(R.id.relative_lyt);
        this.mRelativeLayout.getLayoutParams().width = Math.round(this.videoWidth * this.scaleFactor);
        this.mRelativeLayout.getLayoutParams().height = Math.round(this.videoHeight * this.scaleFactor);
        SurfaceView surfaceView = (SurfaceView) this.mFloatingView.findViewById(R.id.surf_view);
        this.mWindowManager.addView(this.mFloatingView, layoutParams);
        this.surfaceHolder = surfaceView.getHolder();
        this.surfaceHolder.addCallback(this);
        this.closeButton = (ImageView) this.mFloatingView.findViewById(R.id.close_btn);
        this.playButton = (Button) this.mFloatingView.findViewById(R.id.play_btn);
        this.fullScreen = (ImageView) this.mFloatingView.findViewById(R.id.full_view);
        this.plusBtn = (ImageView) this.mFloatingView.findViewById(R.id.plus);
        this.minusBtn = (ImageView) this.mFloatingView.findViewById(R.id.minus);
        this.forward = (ImageView) this.mFloatingView.findViewById(R.id.forward);
        this.repeat = (ImageView) this.mFloatingView.findViewById(R.id.repeat);
        this.backward = (ImageView) this.mFloatingView.findViewById(R.id.backward);
        this.forwawrdText = (TextView) this.mFloatingView.findViewById(R.id.forward_text);
        this.backText = (TextView) this.mFloatingView.findViewById(R.id.back_text);
        this.duration = (TextView) this.mFloatingView.findViewById(R.id.duration);
        this.progress = (TextView) this.mFloatingView.findViewById(R.id.progress);
        if (this.scaleFactor == 0.6F) {
            this.minusBtn.setVisibility(View.GONE);
        }
        if (this.scaleFactor >= 1.0F) {
            FloatingVideoplayer.this.scaleFactor = 1.0f;
            this.plusBtn.setVisibility(View.GONE);
        }
        this.plusBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FloatingVideoplayer.this.scaleFactor = FloatingVideoplayer.this.scaleFactor + 0.1f;
                if (FloatingVideoplayer.this.scaleFactor >= 1.0F) {
                    FloatingVideoplayer.this.plusBtn.setVisibility(View.GONE);
                    FloatingVideoplayer.this.forward.setVisibility(View.VISIBLE);
                    FloatingVideoplayer.this.backward.setVisibility(View.VISIBLE);
                    FloatingVideoplayer.this.repeat.setVisibility(View.VISIBLE);
                    FloatingVideoplayer.this.scaleFactor = 1.0f;
                } else {
                    FloatingVideoplayer.this.minusBtn.setVisibility(View.VISIBLE);
                }
                if (FloatingVideoplayer.this.scaleFactor >= 0.7f) {
                    FloatingVideoplayer.this.duration.setVisibility(View.VISIBLE);
                    FloatingVideoplayer.this.progress.setVisibility(View.VISIBLE);
                }
                FloatingVideoplayer.this.prefs.edit().putFloat("ScaleFact", FloatingVideoplayer.this.scaleFactor).apply();
                FloatingVideoplayer.this.mRelativeLayout.getLayoutParams().width = Math.round(FloatingVideoplayer.this.videoWidth * FloatingVideoplayer.this.scaleFactor);
                FloatingVideoplayer.this.mRelativeLayout.getLayoutParams().height = Math.round(FloatingVideoplayer.this.videoHeight * FloatingVideoplayer.this.scaleFactor);
                FloatingVideoplayer.this.mWindowManager.updateViewLayout(FloatingVideoplayer.this.mFloatingView, layoutParams);
                FloatingVideoplayer.this.toggleHandler.removeCallbacksAndMessages(null);
                FloatingVideoplayer.this.toggleHandler.postDelayed(FloatingVideoplayer.this.toggleRunnable, 2000);
            }
        });
        this.minusBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FloatingVideoplayer.this.scaleFactor = FloatingVideoplayer.this.scaleFactor - 0.1f;
                if (FloatingVideoplayer.this.scaleFactor <= 0.6F) {
                    FloatingVideoplayer.this.minusBtn.setVisibility(View.GONE);
                    FloatingVideoplayer.this.scaleFactor = 0.6f;
                } else {
                    FloatingVideoplayer.this.plusBtn.setVisibility(View.VISIBLE);
                }
                if (FloatingVideoplayer.this.scaleFactor == 0.6F) {
                    FloatingVideoplayer.this.duration.setVisibility(View.GONE);
                    FloatingVideoplayer.this.progress.setVisibility(View.GONE);
                }
                FloatingVideoplayer.this.prefs.edit().putFloat("ScaleFact", FloatingVideoplayer.this.scaleFactor).apply();
                FloatingVideoplayer.this.backward.setVisibility(View.GONE);
                FloatingVideoplayer.this.forward.setVisibility(View.GONE);
                FloatingVideoplayer.this.repeat.setVisibility(View.GONE);
                FloatingVideoplayer.this.mRelativeLayout.getLayoutParams().width = Math.round(FloatingVideoplayer.this.videoWidth * FloatingVideoplayer.this.scaleFactor);
                FloatingVideoplayer.this.mRelativeLayout.getLayoutParams().height = Math.round(FloatingVideoplayer.this.videoHeight * FloatingVideoplayer.this.scaleFactor);
                FloatingVideoplayer.this.mWindowManager.updateViewLayout(FloatingVideoplayer.this.mFloatingView, layoutParams);
                FloatingVideoplayer.this.toggleHandler.removeCallbacksAndMessages(null);
                FloatingVideoplayer.this.toggleHandler.postDelayed(FloatingVideoplayer.this.toggleRunnable, 2000);
            }
        });
        this.closeButton.setOnClickListener(new close_button_call());
        this.playButton.setOnClickListener(new play_button_call());
        this.fullScreen.setOnClickListener(new full_screen_button_call());
        this.forward.setOnClickListener(new forword_button_call());
        this.backward.setOnClickListener(new backword_button_call());
        this.repeat.setOnClickListener(new repeat_button_call());
        this.mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new OnTouchListener() {
            private float initialTouchX;
            private float initialTouchY;
            private int initialX;
            private int initialY;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        this.initialX = layoutParams.x;
                        this.initialY = layoutParams.y;
                        this.initialTouchX = motionEvent.getRawX();
                        this.initialTouchY = motionEvent.getRawY();
                        return true;
                    case 1:
                        int i = (int) (motionEvent.getRawX() - this.initialTouchX);
                        int j = (int) (motionEvent.getRawY() - this.initialTouchY);
                        if ((i < 10) || (j < 10)) {
                            if (FloatingVideoplayer.this.isControlShown) {
                                FloatingVideoplayer.this.hideControls();
                            } else {
                                FloatingVideoplayer.this.showControls();
                            }
                            FloatingVideoplayer.this.toggleHandler.removeCallbacksAndMessages(null);
                            FloatingVideoplayer.this.toggleHandler.postDelayed(FloatingVideoplayer.this.toggleRunnable, 2000L);
                            FloatingVideoplayer.this.isControlShown = FloatingVideoplayer.this.isControlShown ^ true;

                        }
                        return true;
                    case 2:
                        layoutParams.x = this.initialX + ((int) (motionEvent.getRawX() - this.initialTouchX));
                        layoutParams.y = this.initialY + ((int) (motionEvent.getRawY() - this.initialTouchY));
                        FloatingVideoplayer.this.hideControls();
                        FloatingVideoplayer.this.mWindowManager.updateViewLayout(FloatingVideoplayer.this.mFloatingView, layoutParams);
                        return true;
                    default:
                        return false;
                }
            }
        });
        this.toggleRunnable = new Runnable() {
            public void run() {
                FloatingVideoplayer.this.isControlShown = FloatingVideoplayer.this.isControlShown ^ true;
                FloatingVideoplayer.this.hideControls();
            }
        };
        this.textRunnable = new Runnable() {
            public void run() {
                FloatingVideoplayer.this.forwawrdText.setVisibility(View.GONE);
                FloatingVideoplayer.this.backText.setVisibility(View.GONE);
            }
        };
        this.mediaRunnable = new Runnable() {

            class media_player_call implements OnCompletionListener {
                media_player_call() {
                }

                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (FloatingVideoplayer.this.mediaPlayer.isLooping()) {
                        FloatingVideoplayer.this.playButton.setBackgroundResource(R.drawable.ic_play);
                        FloatingVideoplayer.this.isControlShown = true;
                        FloatingVideoplayer.this.showControls();
                    }
                }
            }

            @SuppressLint("WrongConstant")
            public void run() {
                FloatingVideoplayer.this.mediaPlayer.seekTo(FloatingVideoplayer.this.position);
                FloatingVideoplayer.this.mediaPlayer.start();
                TextView access$600 = FloatingVideoplayer.this.duration;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Util.getDurationString((long) FloatingVideoplayer.this.mediaPlayer.getDuration(), false));
                stringBuilder.append("  /  ");
                access$600.setText(stringBuilder.toString());
                FloatingVideoplayer.this.showControls();
                FloatingVideoplayer.this.timerHandler.post(FloatingVideoplayer.this.updater);
                FloatingVideoplayer.this.toggleHandler.removeCallbacksAndMessages(null);
                FloatingVideoplayer.this.toggleHandler.postDelayed(FloatingVideoplayer.this.toggleRunnable, 2000);
                FloatingVideoplayer.this.mediaPlayer.setOnCompletionListener(new media_player_call());
                FloatingVideoplayer.this.mAudioManager = (AudioManager) FloatingVideoplayer.this.getSystemService(AUDIO_SERVICE);
                if (FloatingVideoplayer.this.mAudioManager != null) {
                    FloatingVideoplayer.this.mAudioManager.requestAudioFocus(FloatingVideoplayer.this, 3, 1);
                }
            }
        };
        return START_NOT_STICKY;
    }

    private void showControls() {
        this.closeButton.setVisibility(View.VISIBLE);
        this.playButton.setVisibility(View.VISIBLE);
        this.fullScreen.setVisibility(View.VISIBLE);
        if (this.scaleFactor > 0.6f) {
            this.minusBtn.setVisibility(View.VISIBLE);
        }
        if (this.scaleFactor >= 0.7f) {
            this.duration.setVisibility(View.VISIBLE);
            this.progress.setVisibility(View.VISIBLE);
        }
        if (this.scaleFactor < 1.0f) {
            this.plusBtn.setVisibility(View.VISIBLE);
        }
        if (this.scaleFactor == 1.0f) {
            this.forward.setVisibility(View.VISIBLE);
            this.backward.setVisibility(View.VISIBLE);
            this.repeat.setVisibility(View.VISIBLE);
        }
    }

    private void hideControls() {
        this.closeButton.setVisibility(View.GONE);
        this.playButton.setVisibility(View.GONE);
        this.fullScreen.setVisibility(View.GONE);
        this.plusBtn.setVisibility(View.GONE);
        this.minusBtn.setVisibility(View.GONE);
        this.forward.setVisibility(View.GONE);
        this.backward.setVisibility(View.GONE);
        this.repeat.setVisibility(View.GONE);
        this.duration.setVisibility(View.GONE);
        this.progress.setVisibility(View.GONE);
        this.forwawrdText.setVisibility(View.GONE);
        this.backText.setVisibility(View.GONE);
    }

    private void pauseVideo() {
        this.mediaPlayer.pause();
        this.playButton.setBackgroundResource(R.drawable.ic_play);
    }

    private void playVideo() {
        this.mediaPlayer.start();
        this.playButton.setBackgroundResource(R.drawable.ic_pause);
    }

    public void onAudioFocusChange(int i) {
        if (i != 1) {
            switch (i) {
                case -3:
                    if (this.mediaPlayer.isPlaying()) {
                        this.mediaPlayer.setVolume(0.1f, 0.1f);
                        return;
                    }
                    return;
                case -2:
                    if (this.mediaPlayer.isPlaying()) {
                        pauseVideo();
                        this.isInterrupted = true;
                        return;
                    }
                    return;
                case -1:
                    if (this.mediaPlayer.isPlaying()) {
                        pauseVideo();
                        this.isInterrupted = true;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
        this.mediaPlayer.setVolume(1.0f, 1.0f);
        if (this.mediaPlayer.isPlaying() && this.isInterrupted) {
            playVideo();
            this.isInterrupted = false;
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mediaPlayer = MediaPlayer.create(this, Uri.fromFile(new File(this.videoPath)));
        this.mediaPlayer.setAudioStreamType(3);
        this.mediaPlayer.setDisplay(this.surfaceHolder);
        this.mediaPlayer.setOnPreparedListener(this);
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        if (this.is1080pVideo) {
            this.toggleHandler.postDelayed(this.mediaRunnable, 1000);
        } else {
            this.toggleHandler.postDelayed(this.mediaRunnable, 500);
        }
    }

    public void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
            unregisterReceiver(this.receiver);
            this.mAudioManager.abandonAudioFocus(this);
            this.timerHandler.removeCallbacks(this.updater);
            this.toggleHandler.removeCallbacksAndMessages(null);
            this.dummyNotif.setAction(AudioServiceConstants.ACTION.FLOAT_VID_STOPFOREGROUND);
            startService(this.dummyNotif);
            if (this.mediaPlayer != null) {
                this.mediaPlayer.release();
                this.mediaPlayer = null;
            }
            if (this.mFloatingView != null) {
                this.mWindowManager.removeView(this.mFloatingView);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
