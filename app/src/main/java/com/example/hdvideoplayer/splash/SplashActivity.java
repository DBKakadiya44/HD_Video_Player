package com.example.hdvideoplayer.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.SortedListAdapterCallback;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.hdvideoplayer.MainActivity;
import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.startprivacy.StartPrivacyActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, StartPrivacyActivity.class);
                startActivity(intent);
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable,3000);
    }
}