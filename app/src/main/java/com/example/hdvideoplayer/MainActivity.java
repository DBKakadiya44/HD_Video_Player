package com.example.hdvideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.example.hdvideoplayer.databinding.ActivityMainBinding;
import com.example.hdvideoplayer.musicplayer.MusicPlayerActivity;
import com.example.hdvideoplayer.videodownload.VideoDownloaderActivity;
import com.example.hdvideoplayer.videoplayer.VideoPlayerActivity;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    public static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //video player
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        return;
                    }
                }

                Intent intent = new Intent(MainActivity.this , VideoPlayerActivity.class);
                startActivity(intent);
            }
        });

        binding.imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //music player
                Intent intent = new Intent(MainActivity.this , MusicPlayerActivity.class);
                startActivity(intent);

            }
        });

        binding.imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //downloader
                Intent intent = new Intent(MainActivity.this , VideoDownloaderActivity.class);
                startActivity(intent);

            }
        });

        binding.ivBack.setOnClickListener(view -> onBackPressed());

    }
}