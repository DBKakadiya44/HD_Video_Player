package com.example.hdvideoplayer.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.databinding.ActivityPlayMusicBinding;

public class PlayMusicActivity extends AppCompatActivity {

    ActivityPlayMusicBinding binding;
    String name, artist, album, data;
    long duration;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        name = getIntent().getStringExtra("name");
        artist = getIntent().getStringExtra("artist");
        album = getIntent().getStringExtra("album");
        data = getIntent().getStringExtra("data");
        duration = getIntent().getLongExtra("duration", 0);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // Replace "musicFilePath" with the actual path to the music file you want to play
        String musicFilePath = data;

        try {
            mediaPlayer.setDataSource(musicFilePath);
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.name.setText(name);
        binding.album.setText(album);
        binding.duration.setText("" + Math.toIntExact(duration));

        mediaPlayer.start();

        binding.play.setOnClickListener(view -> {
            mediaPlayer.start();
            binding.pause.setVisibility(View.VISIBLE);
            binding.play.setVisibility(View.INVISIBLE);
        });

        binding.pause.setOnClickListener(view -> {
            mediaPlayer.pause();
            binding.pause.setVisibility(View.INVISIBLE);
            binding.play.setVisibility(View.VISIBLE);
        });


        long durationMilliseconds = mediaPlayer.getDuration();

        String formattedDuration = millisecondsToTime(durationMilliseconds);

        binding.duration.setText(formattedDuration);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static String millisecondsToTime(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

        // Format the time as HH:MM:SS
        return String.format("%02d:%02d", minutes, seconds);
    }

}