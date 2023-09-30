package com.example.hdvideoplayer.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.databinding.ActivityMusicPlayerBinding;
import com.example.hdvideoplayer.model.MusicFile;
import com.example.hdvideoplayer.model.Video;
import com.example.hdvideoplayer.musicplayer.adapter.MusicAdapter;
import com.example.hdvideoplayer.videoplayer.VideoPlayerActivity;
import com.example.hdvideoplayer.videoplayer.adapter.FolderAdapter;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity {

    ActivityMusicPlayerBinding binding;
    public static List<MusicFile> musicFiles = new ArrayList<>();
    List<Video> videos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getAllMusic(getApplicationContext());

        MusicAdapter adapter = new MusicAdapter(MusicPlayerActivity.this,musicFiles);
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

    public static List<MusicFile> getAllMusic(Context context) {


        // Set up the content resolver
        ContentResolver contentResolver = context.getContentResolver();

        // Define the projection (columns to retrieve) for the music files
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA  // Path to the actual music file
        };

        // Define the selection (optional)
        String selection = null;
        String[] selectionArgs = null;

        // Define the sort order (optional)
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        // Get a cursor to the music files
        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                // Create a MusicFile object and add it to the list
                MusicFile musicFile = new MusicFile(id, title, artist, album, duration, data);
                musicFiles.add(musicFile);

                Log.d("ASAS", "getAllMusic: Musiccc = "+musicFiles);
            }
            cursor.close();
        }

        return musicFiles;
    }
}