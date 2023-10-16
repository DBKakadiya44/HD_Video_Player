package com.example.hdvideoplayer;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.widget.RemoteViews;


public class DumyNotification extends Service {
    private boolean isFloatActive = false;
    private boolean isMainActive = false;
    private Notification status;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
       String sintent  = intent.getAction();
        i = intent.hashCode();
        if (i != 648572790) {
            if (i != 1304631444) {
                if (i != 1359049833) {
                    if (i == 1854583297) {
                        if (sintent.equals(AudioServiceConstants.ACTION.FLOAT_VID_STARTFOREGROUND)) {
                            switch (i) {
                                case 0:
                                    this.isMainActive = true;
                                    if (this.isFloatActive) {
                                        showNotification();
                                        break;
                                    }
                                    break;
                                case 1:
                                    this.isFloatActive = true;
                                    if (this.isMainActive ) {
                                        showNotification();
                                        break;
                                    }
                                    break;
                                case 2:
                                    this.isMainActive = false;
                                    if (this.isFloatActive) {
                                        stopForeground();
                                        break;
                                    }
                                    break;
                                case 3:
                                    this.isFloatActive = false;
                                    if (this.isMainActive ) {
                                        stopForeground();
                                        break;
                                    }
                                    break;
                                default:
                                    break;
                            }
                            return START_NOT_STICKY;
                        }
                    }
                } else if (sintent.equals(AudioServiceConstants.ACTION.FLOAT_VID_STOPFOREGROUND)) {
                    switch (i) {
                        case 0:
                            this.isMainActive = true;
                            if (this.isFloatActive) {
                                showNotification();
                                break;
                            }
                            break;
                        case 1:
                            this.isFloatActive = true;
                            if (this.isMainActive ) {
                                showNotification();
                                break;
                            }
                            break;
                        case 2:
                            this.isMainActive = false;
                            if (this.isFloatActive) {
                                stopForeground();
                                break;
                            }
                            break;
                        case 3:
                            this.isFloatActive = false;
                            if (this.isMainActive ) {
                                stopForeground();
                                break;
                            }
                            break;
                        default:
                            break;
                    }
                    return START_NOT_STICKY;
                }
            } else if (sintent.equals(AudioServiceConstants.ACTION.MAIN_ACT_STARTFOREGROUND)) {

                switch (i) {
                    case 0:
                        this.isMainActive = true;
                        if (this.isFloatActive) {
                            showNotification();
                            break;
                        }
                        break;
                    case 1:
                        this.isFloatActive = true;
                        if (this.isMainActive ) {
                            showNotification();
                            break;
                        }
                        break;
                    case 2:
                        this.isMainActive = false;
                        if (this.isFloatActive) {
                            stopForeground();
                            break;
                        }
                        break;
                    case 3:
                        this.isFloatActive = false;
                        if (this.isMainActive ) {
                            stopForeground();
                            break;
                        }
                        break;
                    default:
                        break;
                }
                return START_NOT_STICKY;
            }
        } else if (sintent.equals(AudioServiceConstants.ACTION.MAIN_ACT_STOPFOREGROUND)) {

            switch (i) {
                case 0:
                    this.isMainActive = true;
                    if (this.isFloatActive) {
                        showNotification();
                        break;
                    }
                    break;
                case 1:
                    this.isFloatActive = true;
                    if (this.isMainActive ) {
                        showNotification();
                        break;
                    }
                    break;
                case 2:
                    this.isMainActive = false;
                    if (this.isFloatActive ) {
                        stopForeground();
                        break;
                    }
                    break;
                case 3:
                    this.isFloatActive = false;
                    if (this.isMainActive ) {
                        stopForeground();
                        break;
                    }
                    break;
                default:
                    break;
            }
            return START_NOT_STICKY;
        }

        switch (i) {
            case 0:
                this.isMainActive = true;
                if (this.isFloatActive) {
                    showNotification();
                    break;
                }
                break;
            case 1:
                this.isFloatActive = true;
                if (this.isMainActive) {
                    showNotification();
                    break;
                }
                break;
            case 2:
                this.isMainActive = false;
                if (this.isFloatActive ) {
                    stopForeground();
                    break;
                }
                break;
            case 3:
                this.isFloatActive = false;
                if (this.isMainActive) {
                    stopForeground();
                    break;
                }
                break;
            default:
                break;
        }
        return START_NOT_STICKY;
    }

    private void showNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.dummy_notification_bar);
        if (VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("102", "Video Player", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Needed to keep freeplayer bestvideoplayer alive");
            notificationChannel.setSound(null, null);
            notificationChannel.setVibrationPattern(null);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
            this.status = new Builder(this, "102").build();
        } else {
            this.status = new Builder(this).build();
        }
        this.status.contentView = remoteViews;
        this.status.flags = 2;
        this.status.icon = R.drawable.ic_audio_only;
        startForeground(102, this.status);
    }

    private void stopForeground() {
        stopForeground(true);
        stopSelf();
    }
}
