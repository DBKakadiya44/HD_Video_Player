package com.example.hdvideoplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bikomobile.donutprogress.DonutProgress;
import com.bumptech.glide.Glide;
import com.example.hdvideoplayer.databinding.ActivityPreviewBinding;
import com.example.hdvideoplayer.model.Recent;
import com.example.hdvideoplayer.model.VideoModel;
import com.example.hdvideoplayer.view.CustomVideoPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Preview extends AppCompatActivity implements VideoCallback, AudioManager.OnAudioFocusChangeListener  {

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE_FLOATVIEW = 5469;
    private static final String[] PERMISSIONS = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private long adDelayFullScreen;
    private long adDelayOnPaused;
    private Intent audioIntent;
    private int currentPosition;
    private Uri data;
    private Dialog dialog;
    private Intent dummyNotif;
    public static ArrayList<VideoModel> imageList;
    private boolean is1080pVideo = false;
    private boolean isAboutToExit = false;
    private boolean isAdDialActive = false;
    private boolean isInterrupted = false;
    CountDownTimer mCountDownTimer;
    private boolean isLandScape = false;
    private boolean isVidPathFromOtherApp = false;
    private AudioManager mAudioManager;
    private int nTimes = 0;
    private long onPauseTime;
    private float origHeight;
    private float origWidth;
    private CustomVideoPlayer player;
    public FrameLayout pnlFlash;
    private SharedPreferences prefs;
    private final BroadcastReceiver receiver = new player_pause();
    private MediaMetadataRetriever retriever;
    private Intent serviceIntent;
    private String stringUri;
    private final Handler toggleHandler = new Handler();
    private Runnable toggleRunnable;
    private Integer vidPos;
    private String videoTitle = "Video Player.mp4";
    private boolean wasPreviousVideoHD;
    private Button zoomOk;
    private boolean isPanelShown;

    int coundown;
    RelativeLayout hidden_panel;
    RecyclerView play_list_pot;
    private Runnable zoomOkRunnable;
    public static Activity activity;
    RecentDatabaseAdapter databaseAdapter;
    String videoPath;
    String size;



    //    private SimpleExoPlayer player = null;
    ActivityPreviewBinding binding;
    public static ArrayList<Recent> recentlist = new ArrayList();
    Recent recent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//
//        videoPath = getIntent().getStringExtra("path");
//        String title = getIntent().getStringExtra("title");
//        size = getIntent().getStringExtra("size");
//        String time = getIntent().getStringExtra("time");
//
//        initializePlayer(videoPath);
//
//        recent = new Recent(title, videoPath, size,time);
//
//        for (int i = 0; i < recentlist.size(); i++) {
//            if (recentlist.get(i).getTitle().equals(title) && recentlist.get(i).getPath().equals(videoPath)) {
//                recentlist.remove(i);
//            }
//        }
//        recentlist.add(recent);


        playerInit();

        try {
            setContentView((int) R.layout.activity_main);
            activity = Preview.this;
            databaseAdapter = new RecentDatabaseAdapter(Preview.this);
            try {

                if (isMyServiceRunning(FloatingVideoplayer.class)) {
                    Intent intent = new Intent("floatingstop");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }
                if (isMyServiceRunning(NotificationBackground.class)) {
                    Intent intent = new Intent("stopservice");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            init();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#8D000000"));

            }
            if (Build.VERSION.SDK_INT < 23 || utils.hasPermissions(Preview.this, PERMISSIONS)) {
                if (imageList != null) {
                    playerInit();
                  /*  new Thread(new Runnable() {
                        @Override
                        public void run() {


                        }
                    }).start();*/
                }
            } else {
                ActivityCompat.requestPermissions(Preview.this, PERMISSIONS, 10);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void init() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.media.AUDIO_BECOMING_NOISY");
            registerReceiver(this.receiver, intentFilter);
            this.mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            if (this.mAudioManager != null) {
                this.mAudioManager.requestAudioFocus(this, 3, 1);
            }
            this.isAboutToExit = false;

            player = (CustomVideoPlayer) findViewById(R.id.player);
            zoomOk = (Button) findViewById(R.id.zoom_btn);
            hidden_panel = (RelativeLayout) findViewById(R.id.hidden_panel);
            play_list_pot = findViewById(R.id.play_list_pot);
            hidden_panel.setVisibility(View.INVISIBLE);
            isPanelShown = false;
            pnlFlash = (FrameLayout) findViewById(R.id.pnlFlash);
            findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AllScreenClick();
                }
            });
            this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
            this.wasPreviousVideoHD = this.prefs.getBoolean("previousVideo", false);
            this.player.enableSwipeGestures(getWindow());
            this.toggleRunnable = new togglezoom();
            this.zoomOkRunnable = new zoom();
            addListeners();
            player.getBack().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    class togglezoom implements Runnable {
        togglezoom() {
        }

        public void run() {
        }
    }

    class zoom implements Runnable {
        zoom() {
        }

        public void run() {
            Preview.this.zoomOk.setVisibility(View.GONE);
        }
    }


    class Permisson implements DialogInterface.OnClickListener {
        Permisson() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            ActivityCompat.requestPermissions(Preview.this, Preview.PERMISSIONS, 10);
        }
    }

    class zoom_finsh implements View.OnClickListener {
        zoom_finsh() {
        }

        public void onClick(View view) {
            Preview.this.zoomOk.setVisibility(View.GONE);
            Preview.this.player.onZooming(false);
            Toast.makeText(Preview.this, "Zooming finished", Toast.LENGTH_SHORT).show();
        }
    }

    class player_toolbar implements View.OnClickListener {
        player_toolbar() {
        }

        public void onClick(View view) {

            Preview.this.onBackPressed();
        }
    }

    @Override
    public void onBuffering(int i) {

    }

    @Override
    public void onCompletion(CustomVideoPlayer videoPlayer) {

        try {
            if (!player.mLoop)
                if (imageList != null && imageList.size() != 0) {
                    this.vidPos = Integer.valueOf(this.vidPos.intValue() + 1);
                    int totalsize = imageList.size();
                    if (vidPos <= (totalsize - 1)) {
                        try {
                            Dialog dialog = new Dialog(activity);
                            dialog.setContentView(R.layout.video_progrss_dialog);
                            Window window = dialog.getWindow();
                            WindowManager.LayoutParams wlp = window.getAttributes();
                            wlp.gravity = Gravity.CENTER;
                            wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                            window.setAttributes(wlp);
                            dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                            dialog.setCancelable(false);
                            LinearLayout layout_with = dialog.findViewById(R.id.layout_with);


                            int orientation = this.getResources().getConfiguration().orientation;
                            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                                LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int) getResources().getDimension(com.intuit.sdp.R.dimen._140sdp));
                                layout_with.setLayoutParams(params);
                                layout_with.setGravity(Gravity.CENTER);

                            } else {
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(com.intuit.sdp.R.dimen._250sdp),(int) getResources().getDimension(com.intuit.sdp.R.dimen._140sdp));
                                layout_with.setLayoutParams(params);
                                layout_with.setGravity(Gravity.CENTER);

                            }




                            ImageView video_thumb_list = dialog.findViewById(R.id.video_thumb_list);
                            TextView list_video_duration = dialog.findViewById(R.id.list_video_duration);
                            TextView list_video_name = dialog.findViewById(R.id.list_video_name);
                            DonutProgress donut_progress = dialog.findViewById(R.id.donut_progress);
                            list_video_name.setText((CharSequence) imageList.get(vidPos).getTitle());
                            list_video_duration.setText((CharSequence) imageList.get(vidPos).getSize());
                            Glide.with(activity).load(imageList.get(vidPos).getVideoPath()).into(video_thumb_list);
                            dialog.findViewById(R.id.replay_button).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    vidPos = Integer.valueOf(vidPos.intValue() - 1);
                                    changeVideo();


                                }
                            });

                            dialog.findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();

                                    changeVideo();


                                }
                            });
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            dialog.show();


                            coundown = 0;
                            donut_progress.setProgress(coundown);
                            mCountDownTimer = new CountDownTimer(5000, 1000) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    coundown++;
                                    donut_progress.setProgress((int) coundown * 100 / (5000 / 1000));

                                }

                                @Override
                                public void onFinish() {
                                    coundown++;
                                    donut_progress.setProgress(100);
                                    dialog.dismiss();

                                    changeVideo();

                                }
                            };
                            mCountDownTimer.start();
                        } catch (Exception e) {

                        }


                        return;
                    } else {
                        onBackPressed();
                    }
                    this.vidPos = totalsize - 1;
                } else {
                    onBackPressed();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class player_pause extends BroadcastReceiver {
        player_pause() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("android.media.AUDIO_BECOMING_NOISY") && Preview.this.player.isPlaying()) {
                Preview.this.player.pause();
            }
        }
    }

    private void playerInit() {

        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.media.AUDIO_BECOMING_NOISY");
            registerReceiver(this.receiver, intentFilter);
            this.mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            if (this.mAudioManager != null) {
                this.mAudioManager.requestAudioFocus(this, 3, 1);
            }
            this.isAboutToExit = false;

//            String stringExtra = getIntent().getStringExtra("AudioPath");
            String stringExtra2 = getIntent().getStringExtra("path");
//            String stringExtra3 = getIntent().getStringExtra("FloatVidPath");

//            Log.d("AudioPath", "" + stringExtra);
//            Log.d("path", "aaaaaaaaaaaaa" + stringExtra2);
//            Log.d("FloatVidPath", "" + stringExtra3);
//            if (vidPos == null) {
//                try {
//                    if (imageList != null) {
//                        for (int i = 0; i < imageList.size(); i++) {
//                            String path = videoPath;
//                            if (path.equals(stringExtra)) {
//                                vidPos = i;
//                                break;
//                            }
//
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            listAdapter = new PlayerVideoListAdapter(imageList,activity);
//            play_list_pot.setAdapter(listAdapter);
//            listAdapter.setOnItemClickListener(new PlayerVideoListAdapter.onItemClickListener() {
//                @Override
//                public void onItemClickListener(int postion) {
//                    vidPos = postion;
//                    AllScreenClick();
//                    changeVideo();
//
//                }
//            });
            /*if (stringExtra != null) {
                this.currentPosition = getIntent().getIntExtra("duration", 0);
                this.stringUri = stringExtra;
            } else*/ if (stringExtra2 != null) {
                this.vidPos = Integer.valueOf(getIntent().getIntExtra("position", 0));
                this.stringUri = stringExtra2;
                if (utils.recent_chek(stringUri, Preview.this) == true) {
                } else {
                    databaseAdapter.insertData(stringUri, size);
                }
                prevNxtBtnvisib();
            }/* else if (stringExtra3 != null) {
                this.stringUri = stringExtra3;
            }*/ else {
                this.isVidPathFromOtherApp = true;
                if ("android.intent.action.VIEW".equals(getIntent().getAction())) {
                    this.data = getIntent().getData();
                    stringUri = UriUtils.getPathFromUri(this,data);
                }
                if (this.stringUri == null) {
                    this.stringUri = this.data.getPath();
                }
            }
            this.adDelayFullScreen = this.prefs.getLong("adDelayFullScreen", 0);
            if (this.adDelayFullScreen == 0) {
                this.prefs.edit().putLong("adDelayFullScreen", System.currentTimeMillis()).apply();
                this.adDelayFullScreen = this.prefs.getLong("adDelayFullScreen", 0);
            }
            this.adDelayOnPaused = this.prefs.getLong("adDelayOnPaused", 0);
            if (this.adDelayOnPaused == 0) {
                this.prefs.edit().putLong("adDelayOnPaused", System.currentTimeMillis()).apply();
                this.adDelayOnPaused = this.prefs.getLong("adDelayOnPaused", 0);
            }
            this.player.setCallback(this);
            this.player.setLoadingStyle(9);
            player.hideControls();
            this.player.setSource(Uri.fromFile(new File(this.stringUri)));
            this.videoTitle = Uri.parse(this.stringUri).getLastPathSegment();
            this.retriever = new MediaMetadataRetriever();
            HDorNot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void HDorNot() {
        try {
            this.retriever.setDataSource(this.stringUri);
            this.is1080pVideo = ((float) this.retriever.getFrameAtTime().getWidth()) > 1800.0f;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void prevNxtBtnvisib() {


        try {
            if (imageList != null) {
                if (this.vidPos.intValue() == 0) {
                    this.player.onNoPrevVideo();
                }
                if (this.vidPos.intValue() == this.imageList.size() - 1) {
                    this.player.onNoNextVideo();
                }
                this.player.fromOurGallery(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 10) {
            if (iArr.length > 0 && iArr[0] == 0) {
                playerInit();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Storage permission");
                builder.setMessage("This permission is needed to play videos");
                builder.setPositiveButton("Grant", new Permisson());
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                builder.show();
                alertDialog.getButton(-1).setTextSize(2, 1099956224);
            } else {
                Toast.makeText(getBaseContext(), "Permission not granted. Kindly grant permission from settings.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addListeners() {
        this.zoomOk.setOnClickListener(new zoom_finsh());
        this.player.getToolbar().setNavigationOnClickListener(new player_toolbar());
    }

    public void onAudioFocusChange(int i) {
        try {
            if (i != 1) {
                switch (i) {
                    case -3:
                        if (this.player.isPlaying()) {
                            this.player.setVolume(0.1f, 0.1f);
                            return;
                        }
                        return;
                    case -2:
                        if (this.player.isPlaying()) {
                            this.player.pause();
                            this.isInterrupted = true;
                            return;
                        }
                        return;
                    case -1:
                        if (this.player.isPlaying()) {
                            this.player.pause();
                            this.isInterrupted = true;
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
            if (player != null) {
                if (player.isPrepared()) {
                    this.player.setVolume(1.0f, 1.0f);
                    if (this.player.isPlaying() == false && this.isInterrupted != false && this.isAdDialActive == false) {
                        this.player.start();
                        this.isInterrupted = false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isMyServiceRunning(Class<?> cls) {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void floatView() {
        if (this.is1080pVideo) {
            this.prefs.edit().putBoolean("previousVideo", true).apply();
        } else {
            this.prefs.edit().putBoolean("previousVideo", false).apply();
        }
        this.currentPosition = this.player.getCurrentPosition();
        this.serviceIntent.putExtra("audioPath", this.stringUri);
        this.serviceIntent.putExtra("Width", this.origWidth);
        this.serviceIntent.putExtra("Height", this.origHeight);
        this.serviceIntent.putExtra("Duration", this.currentPosition);
        this.serviceIntent.putExtra("is1080p", this.is1080pVideo);
        startService(this.serviceIntent);
        exit();

    }

//    public void onPause() {
//        if (!this.isAboutToExit) {
////            this.currentPosition = this.player.getCurrentPosition();
//            this.player.pause();
//            unRegister();
//            this.onPauseTime = System.currentTimeMillis();
//        }
//        super.onPause();
//    }

    @SuppressLint("WrongConstant")
    public void onResume() {
        super.onResume();
        try {
            player.onResume();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.media.AUDIO_BECOMING_NOISY");
            registerReceiver(this.receiver, intentFilter);
            this.mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            if (this.mAudioManager != null) {
                this.mAudioManager.requestAudioFocus(this, 3, 1);
            }
            this.isAboutToExit = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        try {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        }catch (Exception e){
            finish();
        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }

    private void exit() {
        try {
            this.isAboutToExit = true;
            unRegister();

            this.player.stop();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unRegister() {
        try {
            unregisterReceiver(this.receiver);
            this.mAudioManager.abandonAudioFocus(this);
            this.toggleHandler.removeCallbacksAndMessages(null);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        try {
            super.onDestroy();
            this.dummyNotif.setAction(AudioServiceConstants.ACTION.MAIN_ACT_STOPFOREGROUND);
            startService(this.dummyNotif);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dialogBuildOverlay(String str, String str2, final int i) {

        try {
            Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_pop_window);
            dialog.findViewById(R.id.cancle_b).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                }
            });
            dialog.findViewById(R.id.allow_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("package:");
                    stringBuilder.append(Preview.this.getPackageName());
                    startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse(stringBuilder.toString())), i);
                }
            });
            dialog.show();
        } catch (Exception e) {

        }


    }

    private void dialogBuildSettings(String str, String str2) {
        try {
            Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_settings);
            dialog.findViewById(R.id.switch_on).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", Preview.this.getPackageName(), null));
                    Preview.this.startActivityForResult(intent, 101);
                }
            });
            dialog.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void errorAlertDialog() {

        try {
            Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_error);

            dialog.findViewById(R.id.allow_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Preview.this.exit();

                }
            });
            dialog.show();
        } catch (Exception e) {

        }

    }

    private void hideSystemUI() {
        this.player.setSystemUiVisibility(3846);
        getWindow().addFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void showSystemUI() {
        this.player.setSystemUiVisibility(1792);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().addFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT);
    }

    public void onPreparing(CustomVideoPlayer videoPlayer) {
        this.dummyNotif = new Intent(this, DumyNotification.class);
        this.serviceIntent = new Intent(this, FloatingVideoplayer.class);
        this.audioIntent = new Intent(this, NotificationBackground.class);
    }

    public void onPrepared(CustomVideoPlayer videoPlayer) {
        this.toggleHandler.postDelayed(this.toggleRunnable, 50);
        if (this.currentPosition != 0) {
            videoPlayer.seekTo(this.currentPosition);
            currentPosition = 0;
        }
        videoPlayer.setmTitle(this.videoTitle);
        this.dummyNotif.setAction(AudioServiceConstants.ACTION.MAIN_ACT_STARTFOREGROUND);
        startService(this.dummyNotif);
    }

    public void onError(CustomVideoPlayer videoPlayer, Exception exception) {
        this.nTimes += 1;
        if (this.nTimes != 1) {
            return;
        }
        if (isMyServiceRunning(FloatingVideoplayer.class) && (this.is1080pVideo || this.wasPreviousVideoHD)) {
            stopService(this.serviceIntent);
            Toast.makeText(this, "Try again !!", Toast.LENGTH_SHORT).show();
            exit();
        } else if (this.is1080pVideo) {
            Toast.makeText(this, "Your device won't support HD videos !!", Toast.LENGTH_SHORT).show();
            exit();
        } else {
            if (this.isVidPathFromOtherApp) {
                getPackageManager().clearPackagePreferredActivities(getPackageName());
            }
            errorAlertDialog();
        }
    }

    @Override
    public void onPauseBtnClick(CustomVideoPlayer videoPlayer, boolean z) {

    }

    @Override
    public void onPaused(CustomVideoPlayer videoPlayer) {

    }

    public void onToggleControls(CustomVideoPlayer videoPlayer, boolean z) {

        if (z) {

            showSystemUI();
            return;
        }

        hideSystemUI();
    }

    public void onZoom(CustomVideoPlayer videoPlayer, boolean z) {
        this.toggleHandler.removeCallbacksAndMessages(null);
        if (z) {
            this.zoomOk.setVisibility(View.VISIBLE);
            this.toggleHandler.postDelayed(this.zoomOkRunnable, 2000);
            return;
        }
        this.zoomOk.setVisibility(View.GONE);
    }


    public void videoSize(CustomVideoPlayer paramVideoPlayer, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {

        this.origWidth = paramFloat1;
        this.origHeight = paramFloat2;
        boolean bool = true;
        if (paramFloat3 > paramFloat4) {
            bool = true;
        } else {
            bool = false;
        }
        this.isLandScape = bool;

    }

    public void onVFloatBtnClick(CustomVideoPlayer videoPlayer, boolean z) {
        if (z) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(getApplicationContext())) {
                floatView();
            } else {
                dialogBuildOverlay("Overlay permission needed", "Enable overlay permission to enjoy the float view feature", ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE_FLOATVIEW);
            }
        }
    }

    public void onAudioOnlyBtnClick(CustomVideoPlayer videoPlayer, boolean z) {
        if (z) {
            if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                player.stop();
                this.currentPosition = videoPlayer.getCurrentPosition();
                this.audioIntent.setAction(AudioServiceConstants.ACTION.STARTFOREGROUND_ACTION);
                this.audioIntent.putExtra("audioPath", this.stringUri);
                this.audioIntent.putExtra("Duration", this.currentPosition);
                startService(this.audioIntent);
                exit();
                return;
            }
            dialogBuildSettings("Unblock notifications", "Please unblock notifications to enjoy this feature");
        }

    }

    @Override
    public void onRequestedOrientation(boolean z) {
        if (z) {
            player.isLandScape = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            player.setLayoutOrei(true);

        } else {
            player.isLandScape = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            player.setLayoutOrei(false);

        }
    }

    public void onRotLockBtnClick(CustomVideoPlayer videoPlayer, boolean z) {


    }

    @Override
    public void onStarted(CustomVideoPlayer videoPlayer) {

    }

    public void onPlayNextClick(CustomVideoPlayer videoPlayer) {
        try {
            if (imageList != null && imageList.size() != 0) {
                this.vidPos = Integer.valueOf(this.vidPos.intValue() + 1);
                int totalsize = imageList.size();
                if (vidPos <= (totalsize - 1)) {

                    changeVideo();
                    return;
                } else {
                    onBackPressed();
                }
                this.vidPos = totalsize - 1;
            } else {
                onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onPlayPrevClick(CustomVideoPlayer videoPlayer) {
        try {
            if (imageList != null && imageList.size() != 0) {
                this.vidPos = Integer.valueOf(this.vidPos.intValue() - 1);
                if (vidPos >= 0) {
                    changeVideo();
                    return;
                }
                vidPos = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlayForward(CustomVideoPlayer videoPlayer) {
        try {
            boolean play;
            if (player.isPlaying()) {
                play = true;
            } else {
                play = false;
            }
            if (player != null) {
                long totalduration = player.getDuration();
                long currentDuration = player.getCurrentPosition();

                if (currentDuration >= totalduration) {
                    onPlayNextClick(videoPlayer);
                } else {
                    player.pause();
                    currentDuration += 10000;
                    player.seekTo((int) currentDuration);
                    player.getmSeeker().setProgress((int) currentDuration);
                    if (play) {
                        player.start();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlayBackward(CustomVideoPlayer videoPlayer) {
        boolean play;
        if (player.isPlaying()) {
            play = true;
        } else {
            play = false;
        }
        if (player != null) {
            long totalduration = player.getDuration();
            long currentDuration = player.getCurrentPosition();
            if (currentDuration >= 0) {
                if (currentDuration <= totalduration) {
                    player.pause();
                    currentDuration -= 10000;
                    player.seekTo((int) currentDuration);
                    player.getmSeeker().setProgress((int) currentDuration);
                    if (play) {
                        player.start();
                    }
                } else {

                }
            }

        }
    }


    public void changeVideo() {

//        try {
//            if (imageList != null) {
//                player.stop();
//                this.player.reset();
//                this.stringUri = videoPath;
//                if (utils.recent_chek(stringUri, Preview.this) == true) {
//                } else {
//                    databaseAdapter.insertData(stringUri, size);
//                }
//
//                this.player.setSource(Uri.fromFile(new File(this.stringUri)));
//                this.videoTitle = Uri.parse(this.stringUri).getLastPathSegment();
//                this.player.setmTitle(this.videoTitle);
//                this.player.seekTo(0);
//                this.player.start();
//                HDorNot();
//                prevNxtBtnvisib();
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }


    protected void onActivityResult(int i, int i2, Intent intent) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (i == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE_FLOATVIEW) {
                if (Settings.canDrawOverlays(this)) {
                    floatView();
                }
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    private class SaveFile extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... sUrl) {
            Bitmap bm;
            InputStream in;

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/" + getResources().getString(R.string.app_name));
            myDir.mkdirs();
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "Image-" + n + ".jpg";
            File file = new File(myDir, fname);
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(Preview.this, "Capture Saved Successfully", Toast.LENGTH_SHORT).show();

        }
    }

    Bitmap saveBitmap;

    @Override
    public void onTakeScreenShort(boolean z, final Bitmap bitmap) {
        try {
            saveBitmap = bitmap;
            player.pause();
            pnlFlash.setVisibility(View.VISIBLE);
            AlphaAnimation fade = new AlphaAnimation(1, 0);
            fade.setDuration(250);
            fade.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation anim) {
                    pnlFlash.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

            });



            MediaPlayer mPlayer2 = MediaPlayer.create(this, R.raw.shutter);
            mPlayer2.start();
            pnlFlash.startAnimation(fade);
            FileOutputStream fos;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    ContentResolver contentResolver = getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    String fname = "Image-" + n + ".jpg";
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,fname);
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");

                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES);
                    Uri img = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                    fos = (FileOutputStream)contentResolver.openOutputStream(Objects.requireNonNull(img));
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                    Objects.requireNonNull(fos);
                    Toast.makeText(Preview.this, "Capture Saved Successfully", Toast.LENGTH_SHORT).show();

                }catch (Exception e){

                }


            }else {
                new SaveFile().execute();

            }

            if (!player.isPlaying()) {
                player.start();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }





    }

    @Override
    public void Eq(int sessionid) {
        CallAllVideo();
    }

    public void AllScreenClick(){
        if (hidden_panel.getVisibility()==View.VISIBLE){
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                Animation bottomDown = AnimationUtils.loadAnimation(this,
                        R.anim.bottom_down);

                hidden_panel.startAnimation(bottomDown);
                hidden_panel.setVisibility(View.INVISIBLE);
                isPanelShown = false;
            } else {
                Animation bottomDown = AnimationUtils.loadAnimation(this,
                        R.anim.left);

                hidden_panel.startAnimation(bottomDown);
                hidden_panel.setVisibility(View.INVISIBLE);
                isPanelShown = false;
            }
        }

    }

    public void CallAllVideo(){
        int orientation = this.getResources().getConfiguration().orientation;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height/2);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins(0,0,0,0);
            hidden_panel.setLayoutParams(params);
            hidden_panel.setGravity(Gravity.BOTTOM);
            Potrait();
        } else {
            RelativeLayout.LayoutParams params =new RelativeLayout.LayoutParams(width/2,height);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(0,30,0,0);
            hidden_panel.setLayoutParams(params);
            Landscope();
        }
    }

    public void Potrait(){
        if(!isPanelShown) {
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_up);
            hidden_panel.startAnimation(bottomUp);
            hidden_panel.setVisibility(View.VISIBLE);
            isPanelShown = true;
        }
        else {
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);
            hidden_panel.startAnimation(bottomDown);
            hidden_panel.setVisibility(View.INVISIBLE);
            isPanelShown = false;
        }

    }

    public void Landscope(){
        if(!isPanelShown) {
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.right);
            hidden_panel.startAnimation(bottomUp);
            hidden_panel.setVisibility(View.VISIBLE);
            isPanelShown = true;
        }
        else {
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.left);
            hidden_panel.startAnimation(bottomDown);
            hidden_panel.setVisibility(View.INVISIBLE);
            isPanelShown = false;
        }
    }

//    private void initializePlayer(String videoPath) {
//        if (player == null) {
//            // Create a SimpleExoPlayer instance.
//            player = new SimpleExoPlayer.Builder(this).build();
//
//            // Connect the player to the PlayerView.
//            binding.playerView.setPlayer(player);
//
//            // Set up a DefaultDataSourceFactory with a user agent
//            String userAgent = "YourUserAgent"; // Replace with your user agent
//            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
//
//            // Create a media item.
//            MediaItem mediaItem = MediaItem.fromUri(videoPath);
//
//            // Create a ProgressiveMediaSource using the media item and data source factory.
//            ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
//                    .createMediaSource(mediaItem);
//
//            // Prepare the player with the media source.
//            player.setMediaSource(mediaSource);
//
//            // Prepare the player and set playWhenReady to true to start playback immediately.
//            player.prepare();
//            player.setPlayWhenReady(true);
//
//            // Set the initial volume. (0.5f is 50% volume)
//            player.setVolume(1.0f);
//
//            // Set up a listener for volume control
//            binding.playerView.setOnTouchListener(new View.OnTouchListener() {
//                private final GestureDetector gestureDetector = new GestureDetector(
//                        binding.playerView.getContext(),
//                        new GestureDetector.SimpleOnGestureListener() {
//                            @Override
//                            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                                // Calculate the change in Y direction and adjust volume accordingly
//                                float deltaY = e2.getY() - e1.getY();
//                                float maxVolume = 1.0f;
//
//                                float volumeDelta = deltaY / binding.playerView.getHeight() * maxVolume;
//                                float newVolume = player.getVolume() - volumeDelta;
//
//                                // Ensure the volume stays within the valid range (0.0 to 1.0)
//                                newVolume = Math.max(0.0f, Math.min(newVolume, 1.0f));
//
//                                // Set the new volume
//                                player.setVolume(newVolume);
//
//                                return true;
//                            }
//                        });
//
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    gestureDetector.onTouchEvent(event);
//                    return true;
//                }
//            });
//        }
//    }


//    private void initializePlayer(String videoPath) {
//        player = new SimpleExoPlayer.Builder(this).build();
//
//        // Create a media item.
//        MediaItem mediaItem = new MediaItem.Builder()
//                .setUri(videoPath)
//                .setMimeType(MimeTypes.APPLICATION_MP4)
//                .build();
//
//        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(
//                new DefaultDataSource.Factory(this)
//        ).createMediaSource(mediaItem);
//
//        player.setMediaSource(mediaSource);
//        player.setPlayWhenReady(true); // Start playing when the ExoPlayer is set up.
//        player.seekTo(0, 0L); // Start from the beginning.
//        player.prepare(); // Change the state from idle.
//
//        binding.playerView.setPlayer(player);
//    }
}