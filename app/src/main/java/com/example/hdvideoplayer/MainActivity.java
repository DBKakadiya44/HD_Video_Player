package com.example.hdvideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
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
import com.example.hdvideoplayer.model.MusicFile;
import com.example.hdvideoplayer.musicplayer.MusicPlayerActivity;
import com.example.hdvideoplayer.videodownload.VideoDownloaderActivity;
import com.example.hdvideoplayer.videoplayer.VideoPlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //video player


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