package com.example.hdvideoplayer.videoplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hdvideoplayer.Preview;
import com.example.hdvideoplayer.databinding.ActivityVideoPlayerBinding;
import com.example.hdvideoplayer.model.Favorite;
import com.example.hdvideoplayer.model.Video;
import com.example.hdvideoplayer.model.VideoModel;
import com.example.hdvideoplayer.videoplayer.adapter.FavoriteAdapter;
import com.example.hdvideoplayer.videoplayer.adapter.FolderAdapter;
import com.example.hdvideoplayer.videoplayer.adapter.RecentAdapter;
import com.example.hdvideoplayer.videoplayer.adapter.VideoAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoPlayerActivity extends AppCompatActivity {
    ActivityVideoPlayerBinding binding;
    public static List<Video> videos = new ArrayList<>();
    public static List<Favorite> favorites = new ArrayList<>();
    ArrayList<VideoModel> al_images = new ArrayList<>();

    boolean boolean_folder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        videos.clear();

        ContentResolver contentResolver = getContentResolver();
        Uri videouri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
        //String sortorder = MediaStore.Video.Media.YEAR + " ASC";
        Cursor cursor = contentResolver.query(videouri, projection, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                String size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));

                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                Uri uri = ContentUris.withAppendedId(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, id);
                String thumbnail = uri.toString();
                Video video = new Video(title, path, thumbnail,size,time);
                videos.add(video);
            }
        }

        FolderAdapter adapter = new FolderAdapter(VideoPlayerActivity.this,videos);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(VideoPlayerActivity.this);
        binding.rvContent.setLayoutManager(manager);
        binding.rvContent.setAdapter(adapter);

        binding.llVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearline();
                binding.lineVideo.setVisibility(View.VISIBLE);

                FolderAdapter adapter = new FolderAdapter(VideoPlayerActivity.this,videos);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(VideoPlayerActivity.this);
                binding.rvContent.setLayoutManager(manager);
                binding.rvContent.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        binding.llFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clearline();

                binding.lineFolder.setVisibility(View.VISIBLE);

                getVideoFolders();

                VideoAdapter adapter = new VideoAdapter(VideoPlayerActivity.this,al_images);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(VideoPlayerActivity.this);
                binding.rvContent.setLayoutManager(manager);
                binding.rvContent.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });

        binding.llRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clearline();
                binding.lineRecent.setVisibility(View.VISIBLE);

                RecentAdapter adapter = new RecentAdapter(VideoPlayerActivity.this, Preview.recentlist);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(VideoPlayerActivity.this);
                binding.rvContent.setLayoutManager(manager);
                binding.rvContent.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        binding.llFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearline();
                binding.lineFavorite.setVisibility(View.VISIBLE);

                FavoriteAdapter adapter = new FavoriteAdapter(VideoPlayerActivity.this, favorites);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(VideoPlayerActivity.this);
                binding.rvContent.setLayoutManager(manager);
                binding.rvContent.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();

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

    public ArrayList<VideoModel> getVideoFolders() {
        al_images.clear();

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name, column_index_title,column_index_size, column_index_time;

        String absolutePathOfImage = null;
        String absoluteTitle = null;
        String size = null;
        String time = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};

        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_title = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        column_index_size = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE);
        column_index_time = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            absoluteTitle = cursor.getString(column_index_title);
            size = cursor.getString(column_index_size);
            time = cursor.getString(column_index_time);

            for (int i = 0; i < al_images.size(); i++) {
                if (al_images.get(i).getFolderName().equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true;
                    int_position = i;
                    break;
                } else {
                    boolean_folder = false;
                }
            }

            if (boolean_folder) {
                ArrayList<String> al_path = new ArrayList<>();
                ArrayList<String> al_title = new ArrayList<>();
                ArrayList<String> al_size = new ArrayList<>();
                ArrayList<String> al_time = new ArrayList<>();

                al_path.addAll(al_images.get(int_position).getVideoPath());
                al_title.addAll(al_images.get(int_position).getTitle());
                al_size.addAll(al_images.get(int_position).getSize());
                al_time.addAll(al_images.get(int_position).getTime());

                al_path.add(absolutePathOfImage);
                al_title.add(absoluteTitle);
                al_size.add(size);
                al_time.add(time);

                al_images.get(int_position).setVideoPath(al_path);
                al_images.get(int_position).setTitle(al_title);
                al_images.get(int_position).setSize(al_size);
                al_images.get(int_position).setTime(al_time);
            }
            else {
                ArrayList<String> al_path = new ArrayList<>();
                ArrayList<String> al_title = new ArrayList<>();
                ArrayList<String> al_size = new ArrayList<>();
                ArrayList<String> al_time = new ArrayList<>();

                al_path.add(absolutePathOfImage);
                al_title.add(absoluteTitle);
                al_size.addAll(Collections.singleton(size));
                al_time.addAll(Collections.singleton(time));

                VideoModel obj_model = new VideoModel();

                obj_model.setFolderName(cursor.getString(column_index_folder_name));
                obj_model.setVideoPath(al_path);
                obj_model.setTitle(al_title);
                obj_model.setSize(al_size);
                obj_model.setTime(al_time);

                al_images.add(obj_model);
            }
        }
        return al_images;
    }

}