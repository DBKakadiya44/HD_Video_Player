package com.example.hdvideoplayer;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;
import java.lang.reflect.Method;


public class NotificationBackground extends Service implements OnAudioFocusChangeListener {
    private AudioManager audioManager;
    private String audioPath;
    private Boolean isCompleted;
    private Boolean isInterrupted = Boolean.valueOf(false);
    private Boolean isPaused;
    private MediaPlayer mediaPlayer;
    private int position;
    private SharedPreferences prefs;
    private final BroadcastReceiver receiver = new C05202();
    private Notification status;


    class C05191 implements OnCompletionListener {
        C05191() {
        }

        public void onCompletion(MediaPlayer mediaPlayer) {
            if (NotificationBackground.this.mediaPlayer.isLooping()) {
                NotificationBackground.this.isPaused = Boolean.valueOf(true);
                NotificationBackground.this.isCompleted = Boolean.valueOf(true);
                NotificationBackground.this.showNotification();
            }
        }
    }

    class C05202 extends BroadcastReceiver {
        C05202() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("android.media.AUDIO_BECOMING_NOISY") && NotificationBackground.this.mediaPlayer != null && NotificationBackground.this.mediaPlayer.isPlaying()) {
                NotificationBackground.this.pauseState();
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        unregisterReceiver(this.receiver);
        this.audioManager.abandonAudioFocus(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (mediaPlayer != null) {

                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    stopForeground(true);
                    stopSelf();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    public int onStartCommand(Intent intent, int i, int i2) {
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("stopservice"));
        if (intent.getAction().equals(AudioServiceConstants.ACTION.STARTFOREGROUND_ACTION)) {
            if (this.mediaPlayer != null) {
                this.mediaPlayer.stop();
                this.mediaPlayer.release();
                stopForeground(true);
            } else {
                this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
                this.audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                if (this.audioManager != null) {
                    this.audioManager.requestAudioFocus(this, 3, 1);
                }
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.media.AUDIO_BECOMING_NOISY");
                registerReceiver(this.receiver, intentFilter);
            }
            this.audioPath = (String) intent.getExtras().get("audioPath");
            this.position = intent.getIntExtra("Duration", 0);
            this.mediaPlayer = MediaPlayer.create(this, Uri.fromFile(new File(this.audioPath)));
            this.isPaused = Boolean.valueOf(false);
            this.isCompleted = Boolean.valueOf(false);
            this.mediaPlayer.seekTo(this.position);
            this.mediaPlayer.start();
            if (this.prefs.getBoolean("isRepeatOn", true)) {
                this.mediaPlayer.setLooping(true);
            } else {
                this.mediaPlayer.setLooping(false);
            }
            showNotification();
        } else if (intent.getAction().equals(AudioServiceConstants.ACTION.PLAY_ACTION)) {
            if (this.mediaPlayer == null) {
                stopForeground(true);
                stopSelf();
            } else if (this.mediaPlayer.isPlaying()) {
                this.isCompleted = Boolean.valueOf(false);
                pauseState();
            } else {
                this.isCompleted = Boolean.valueOf(false);
                playState();
            }
        } else if (intent.getAction().equals(AudioServiceConstants.ACTION.REPEAT)) {
            if (this.mediaPlayer != null) {
                if (this.mediaPlayer.isLooping()) {
                    this.prefs.edit().putBoolean("isRepeatOn", false).apply();
                    Toast.makeText(this, "Repeat OFF", Toast.LENGTH_SHORT).show();
                    this.mediaPlayer.setLooping(false);
                } else {
                    this.prefs.edit().putBoolean("isRepeatOn", true).apply();
                    Toast.makeText(this, "Repeat ON", Toast.LENGTH_SHORT).show();
                    this.mediaPlayer.setLooping(true);
                }
                showNotification();
            } else {
                stopForeground(true);
                stopSelf();
            }
        } else if (intent.getAction().equals(AudioServiceConstants.ACTION.JUMP_TO_START)) {
            if (this.mediaPlayer != null) {
                this.mediaPlayer.seekTo(0);
                if (this.mediaPlayer.isPlaying()) {
                    this.mediaPlayer.pause();
                    this.mediaPlayer.start();
                }
            } else {
                stopForeground(true);
                stopSelf();
            }
        } else if (intent.getAction().equals(AudioServiceConstants.ACTION.STOPFOREGROUND_ACTION)) {
            if (this.mediaPlayer != null) {
                this.mediaPlayer.stop();
                this.mediaPlayer.release();
            }
            stopForeground(true);
            stopSelf();
        } else if (intent.getAction().equals(AudioServiceConstants.ACTION.VIDEO_RESUME)) {
            try {
                if (this.mediaPlayer != null) {
                    try {
                        Object service = getSystemService("statusbar");
                        Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
                        if (VERSION.SDK_INT <= 16) {
                            Method collapse = statusbarManager.getMethod("collapse");
                            collapse.setAccessible(true);
                            collapse.invoke(service);
                        } else {
                            Method collapse2 = statusbarManager.getMethod("collapsePanels");
                            collapse2.setAccessible(true);
                            collapse2.invoke(service);
                        }
                    } catch (Exception ex) {
                    }
                    if (this.mediaPlayer.isPlaying()) {
                        this.position = this.mediaPlayer.getCurrentPosition();
                        this.mediaPlayer.stop();
                        this.mediaPlayer.release();
                    }
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.media.AUDIO_BECOMING_NOISY");
                    registerReceiver(this.receiver, intentFilter);
                    intent = new Intent(this, Preview.class);
                    intent.putExtra("AudioPath", this.audioPath);
                    intent.putExtra("duration", this.position);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                stopForeground(true);
                stopSelf();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        return START_NOT_STICKY;
    }

    private void showNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.small_notification_bar);
        RemoteViews remoteViews2 = new RemoteViews(getPackageName(),  R.layout.big_notification_bar_expanded);
        remoteViews.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        Intent intent = new Intent(this, NotificationBackground.class);
        intent.setAction(AudioServiceConstants.ACTION.PLAY_ACTION);
        PendingIntent service = PendingIntent.getService(this, 0, intent, 0);
        Intent intent2 = new Intent(this, NotificationBackground.class);
        intent2.setAction(AudioServiceConstants.ACTION.REPEAT);
        PendingIntent service2 = PendingIntent.getService(this, 0, intent2, 0);
        Intent intent3 = new Intent(this, NotificationBackground.class);
        intent3.setAction(AudioServiceConstants.ACTION.JUMP_TO_START);
        PendingIntent service3 = PendingIntent.getService(this, 0, intent3, 0);
        Intent intent4 = new Intent(this, NotificationBackground.class);
        intent4.setAction(AudioServiceConstants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent service4 = PendingIntent.getService(this, 0, intent4, 0);
        Intent intent5 = new Intent(this, NotificationBackground.class);
        intent5.setAction(AudioServiceConstants.ACTION.VIDEO_RESUME);
        PendingIntent service5 = PendingIntent.getService(this, 0, intent5, 0);
        remoteViews.setOnClickPendingIntent(R.id.status_bar_play, service);
        remoteViews2.setOnClickPendingIntent(R.id.status_bar_play, service);
        remoteViews.setOnClickPendingIntent(R.id.status_bar_repeat, service2);
        remoteViews2.setOnClickPendingIntent(R.id.status_bar_repeat, service2);
        remoteViews2.setOnClickPendingIntent(R.id.skip_to_start, service3);
        remoteViews.setOnClickPendingIntent(R.id.status_bar_collapse, service4);
        remoteViews2.setOnClickPendingIntent(R.id.status_bar_collapse, service4);
        remoteViews.setOnClickPendingIntent(R.id.status_bar_icon, service5);
        remoteViews2.setOnClickPendingIntent(R.id.status_bar_album_art, service5);
        Bitmap createVideoThumbnail = ThumbnailUtils.createVideoThumbnail(this.audioPath, 3);
        CharSequence lastPathSegment = Uri.parse(this.audioPath).getLastPathSegment();
        remoteViews.setImageViewBitmap(R.id.status_bar_icon, createVideoThumbnail);
        remoteViews2.setImageViewBitmap(R.id.status_bar_album_art, createVideoThumbnail);
        remoteViews.setTextViewText(R.id.status_bar_track_name, lastPathSegment);
        remoteViews2.setTextViewText(R.id.status_bar_track_name, lastPathSegment);
        remoteViews.setTextViewText(R.id.status_bar_artist_name, "Video Player");

        remoteViews2.setTextViewText(R.id.status_bar_album_name, "Video Player");
        remoteViews2.setTextViewText(R.id.duration, Util.getDurationString((long) this.mediaPlayer.getDuration(), false));
        if (this.isPaused.booleanValue()) {
            remoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play);
            remoteViews2.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play);
        } else {
            remoteViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause);
            remoteViews2.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause);
        }
        if (this.prefs.getBoolean("isRepeatOn", true)) {
            remoteViews.setImageViewResource(R.id.status_bar_repeat, R.drawable.ic_repeat_on);
            remoteViews2.setImageViewResource(R.id.status_bar_repeat, R.drawable.ic_repeat_on);
        } else {
            remoteViews.setImageViewResource(R.id.status_bar_repeat, R.drawable.ic_repeat_off);
            remoteViews2.setImageViewResource(R.id.status_bar_repeat, R.drawable.ic_repeat_off);
        }
        if (VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("101", "Audio Controls", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Needed to Control audio bestvideoplayer");
            notificationChannel.setSound(null, null);
            notificationChannel.setVibrationPattern(null);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
            this.status = new Builder(this, "101").build();
        } else {
            this.status = new Builder(this).build();
        }
        this.status.contentView = remoteViews;
        this.status.bigContentView = remoteViews2;
        this.status.flags = 2;
        this.status.icon = R.drawable.ic_audio_only;
        startForeground(101, this.status);
        this.mediaPlayer.setOnCompletionListener(new C05191());
    }


    public void onAudioFocusChange(int i) {
        if (this.mediaPlayer == null) {
            return;
        }
        if (i != 1) {
            switch (i) {
                case -3:
                    if (this.mediaPlayer.isPlaying() && this.isCompleted.booleanValue()) {
                        this.mediaPlayer.setVolume(0.1f, 0.1f);
                        return;
                    }
                    return;
                case -2:
                    if (this.mediaPlayer.isPlaying() && this.isCompleted.booleanValue()) {
                        pauseState();
                        this.isInterrupted = Boolean.valueOf(true);
                        return;
                    }
                    return;
                case -1:
                    if (this.mediaPlayer.isPlaying() && this.isCompleted.booleanValue()) {
                        pauseState();
                        this.isInterrupted = Boolean.valueOf(true);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
        this.mediaPlayer.setVolume(1.0f, 1.0f);
        if (this.mediaPlayer.isPlaying() && this.isInterrupted.booleanValue() && this.isPaused.booleanValue()) {
            playState();
            this.isInterrupted = Boolean.valueOf(false);
        }
    }

    private void pauseState() {
        this.mediaPlayer.pause();
        this.isPaused = Boolean.valueOf(true);
        showNotification();
    }

    private void playState() {
        this.mediaPlayer.start();
        this.isPaused = Boolean.valueOf(false);
        showNotification();
    }
}
