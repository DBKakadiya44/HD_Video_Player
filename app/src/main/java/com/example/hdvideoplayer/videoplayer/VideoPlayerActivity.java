package com.example.hdvideoplayer.videoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import com.example.hdvideoplayer.databinding.ActivityVideoPlayerBinding;
import com.example.hdvideoplayer.model.Video;
import com.example.hdvideoplayer.videoplayer.adapter.FolderAdapter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class VideoPlayerActivity extends AppCompatActivity {
    ActivityVideoPlayerBinding binding;
    public static List<Video> videos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ContentResolver contentResolver = getContentResolver();
        Uri videouri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID};
        //String sortorder = MediaStore.Video.Media.YEAR + " ASC";
        Cursor cursor = contentResolver.query(videouri, projection, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                Uri uri = ContentUris.withAppendedId(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, id);
                String thumbnail = uri.toString();
                Video video = new Video(title, path, thumbnail);
                videos.add(video);
            }
        }

        FolderAdapter adapter = new FolderAdapter(VideoPlayerActivity.this, 1,videos);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(VideoPlayerActivity.this);
        binding.rvContent.setLayoutManager(manager);
        binding.rvContent.setAdapter(adapter);

        binding.llVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearline();
                binding.lineVideo.setVisibility(View.VISIBLE);

                FolderAdapter adapter = new FolderAdapter(VideoPlayerActivity.this, 1,videos);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(VideoPlayerActivity.this);
                binding.rvContent.setLayoutManager(manager);
                binding.rvContent.setAdapter(adapter);
            }
        });

        binding.llFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearline();
                binding.lineFolder.setVisibility(View.VISIBLE);

                FolderAdapter adapter = new FolderAdapter(VideoPlayerActivity.this, 2,videos);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(VideoPlayerActivity.this);
                binding.rvContent.setLayoutManager(manager);
                binding.rvContent.setAdapter(adapter);
            }
        });
        binding.llRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearline();
                binding.lineRecent.setVisibility(View.VISIBLE);

//                FolderAdapter adapter = new FolderAdapter(VideoPlayerActivity.this,3);
//                RecyclerView.LayoutManager manager = new LinearLayoutManager(VideoPlayerActivity.this);
//                binding.rvContent.setLayoutManager(manager);
//                binding.rvContent.setAdapter(adapter);
            }
        });
        binding.llFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearline();
                binding.lineFavorite.setVisibility(View.VISIBLE);

//                FolderAdapter adapter = new FolderAdapter(VideoPlayerActivity.this,4);
//                RecyclerView.LayoutManager manager = new LinearLayoutManager(VideoPlayerActivity.this);
//                binding.rvContent.setLayoutManager(manager);
//                binding.rvContent.setAdapter(adapter);
            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void clearline() {
        binding.lineVideo.setVisibility(View.INVISIBLE);
        binding.lineFavorite.setVisibility(View.INVISIBLE);
        binding.lineFolder.setVisibility(View.INVISIBLE);
        binding.lineRecent.setVisibility(View.INVISIBLE);
    }

}