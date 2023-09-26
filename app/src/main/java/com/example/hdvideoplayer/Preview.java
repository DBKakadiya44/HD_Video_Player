package com.example.hdvideoplayer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hdvideoplayer.databinding.ActivityPreviewBinding;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.MimeTypes;

public class Preview extends AppCompatActivity {
    private SimpleExoPlayer player = null;
    ActivityPreviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String videoPath = getIntent().getStringExtra("path"); // Replace with the actual file path of your video
        initializePlayer(videoPath);
    }

    private void initializePlayer(String videoPath) {
        player = new SimpleExoPlayer.Builder(this).build();

        // Create a media item.
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(videoPath)
                .setMimeType(MimeTypes.APPLICATION_MP4)
                .build();

        // Create a media source and pass the media item.
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(
                new DefaultDataSource.Factory(this)
        ).createMediaSource(mediaItem);

        // Finally, assign this media source to the player.
        player.setMediaSource(mediaSource);
        player.setPlayWhenReady(true); // Start playing when the ExoPlayer is set up.
        player.seekTo(0, 0L); // Start from the beginning.
        player.prepare(); // Change the state from idle.

        // Do not forget to attach the player to the view.
        binding.playerView.setPlayer(player);
    }
}