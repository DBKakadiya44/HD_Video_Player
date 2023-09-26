package com.example.hdvideoplayer.videoplayer.adapter;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hdvideoplayer.MainActivity;
import com.example.hdvideoplayer.Preview;
import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.allfolderpreview.AllFolderPreviewActivity;
import com.example.hdvideoplayer.model.Video;
import com.example.hdvideoplayer.model.VideoModel;
import com.example.hdvideoplayer.videoplayer.VideoPlayerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    Context context;
    int i;
    List<Video> videos;
    ArrayList<VideoModel> al_images = new ArrayList<>();

    boolean boolean_folder = false;
    Map<String, List<String>> videoFolders = new HashMap<>();

    public FolderAdapter(Context context, int i, List<Video> videos) {
        this.context = context;
        this.i = i;
        this.videos = videos;
    }

    @NonNull
    @Override
    public FolderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });


        if (i == 1) {

            holder.title.setText(videos.get(position).getTitle());
            holder.size.setText(videos.get(position).getPath());

            Glide.with(holder.itemView.getContext()).
                    load(videos.get(position).getThumbnailUri()).
                    placeholder(R.drawable.videolable).
                    centerCrop().
                    into(holder.profile);

//            Glide.with(holder.itemView.getContext())
//                    .load(videos.get(position).getThumbnailUri())
//                    // Optional: Crop the image to fit the ImageView
//                    .into(holder.profile);


            holder.profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(holder.itemView.getContext(), Preview.class);
                    intent.putExtra("path", videos.get(position).getPath());
                    holder.itemView.getContext().startActivity(intent);
                }
            });

        }

        if (i == 2) {

            getVideoFolders();
            holder.title.setText("" +al_images.get(position).getFolderName());

            holder.round.setVisibility(View.INVISIBLE);
            holder.folder.setVisibility(View.VISIBLE);
            holder.size.setText("8 Videos");
            holder.time.setVisibility(View.INVISIBLE);

            holder.folder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context , AllFolderPreviewActivity.class);
                    intent.putExtra("name",al_images.get(position).getFolderName());
                    intent.putExtra("list",al_images.get(position).getVideoPath());
                    context.startActivity(intent);

                }
            });

        }

        if (i == 3) {

        }

        if (i == 4) {

        }

        if (i == 5) {
            holder.round.setVisibility(View.INVISIBLE);
            holder.folder.setVisibility(View.VISIBLE);
            holder.folder.setImageResource(R.drawable.music_player);
            holder.title.setText("Mera - Naam _ Mary mp3");
            holder.size.setText("Music");
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setText("02:51");
        }
    }

    @Override
    public int getItemCount() {
        if (i == 1) {
            return videos.size();
        } else if (i == 2) {
            return 2;
        } else {
            return 5;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView folder, profile, menu;
        CardView round;
        TextView title, size, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            round = itemView.findViewById(R.id.cardView);
            folder = itemView.findViewById(R.id.ivFolder);
            title = itemView.findViewById(R.id.textView9);
            size = itemView.findViewById(R.id.textView10);
            time = itemView.findViewById(R.id.textView11);
            profile = itemView.findViewById(R.id.profile);
            menu = itemView.findViewById(R.id.ivMenu);
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (view.getId() == R.id.Copy) {
                    Toast.makeText(context, "Copy", Toast.LENGTH_SHORT).show();
                }
                if (view.getId() == R.id.Details) {
                    Toast.makeText(context, "Details", Toast.LENGTH_SHORT).show();
                }
                if (view.getId() == R.id.Rename) {
                    Toast.makeText(context, "Rename", Toast.LENGTH_SHORT).show();
                }
                if (view.getId() == R.id.Delete) {
                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public ArrayList<VideoModel> getVideoFolders() {
        al_images.clear();

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA};

        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

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
                al_path.addAll(al_images.get(int_position).getVideoPath());
                al_path.add(absolutePathOfImage);
                al_images.get(int_position).setVideoPath(al_path);
            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfImage);
                VideoModel obj_model = new VideoModel();
                obj_model.setFolderName(cursor.getString(column_index_folder_name));
                obj_model.setVideoPath(al_path);
                al_images.add(obj_model);
            }
        }
        return al_images;
    }

}
