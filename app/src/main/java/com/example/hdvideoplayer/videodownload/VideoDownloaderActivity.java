package com.example.hdvideoplayer.videodownload;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hdvideoplayer.MainActivity;
import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.databinding.ActivityVideoDownloaderBinding;
import com.example.hdvideoplayer.facebook.FacebookActivity;
import com.example.hdvideoplayer.instagram.InstagramActivity;
import com.example.hdvideoplayer.mycollection.MyCollectionActivity;
import com.example.hdvideoplayer.videoplayer.VideoPlayerActivity;
import com.example.hdvideoplayer.whatsapp.WhatsappActivity;

public class VideoDownloaderActivity extends AppCompatActivity {
    ActivityVideoDownloaderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoDownloaderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoDownloaderActivity.this , InstagramActivity.class);
                startActivity(intent);
            }
        });
        binding.imageView14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoDownloaderActivity.this , FacebookActivity.class);
                startActivity(intent);
            }
        });
        binding.imageView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoDownloaderActivity.this , WhatsappActivity.class);
                startActivity(intent);
            }
        });
        binding.imageView16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoDownloaderActivity.this , MyCollectionActivity.class);
                startActivity(intent);
            }
        });

        binding.ivBack.setOnClickListener(view -> onBackPressed());

    }
}