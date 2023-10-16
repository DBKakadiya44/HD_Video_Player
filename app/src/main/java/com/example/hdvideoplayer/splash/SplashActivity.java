package com.example.hdvideoplayer.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.start.StartActivity;
import com.example.hdvideoplayer.startprivacy.StartPrivacyActivity;

public class SplashActivity extends AppCompatActivity {

    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferences = getSharedPreferences("mypref",MODE_PRIVATE);
        editor= preferences.edit();

        int login = preferences.getInt("login", 0);

        if(login==1) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable, 3500);
        }

        if(login==0) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, StartPrivacyActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable, 3500);
        }
    }
}