package com.example.hdvideoplayer.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.databinding.ActivityMusicPlayerBinding;
import com.example.hdvideoplayer.model.Video;
import com.example.hdvideoplayer.videoplayer.VideoPlayerActivity;
import com.example.hdvideoplayer.videoplayer.adapter.FolderAdapter;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity {

    ActivityMusicPlayerBinding binding;
    List<Video> videos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FolderAdapter adapter = new FolderAdapter(MusicPlayerActivity.this,5,videos);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(MusicPlayerActivity.this);
        binding.rvMusic.setLayoutManager(manager);
        binding.rvMusic.setAdapter(adapter);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}