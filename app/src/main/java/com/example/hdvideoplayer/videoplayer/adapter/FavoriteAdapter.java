package com.example.hdvideoplayer.videoplayer.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hdvideoplayer.Preview;
import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.model.Favorite;
import com.example.hdvideoplayer.videoplayer.VideoPlayerActivity;

import java.io.File;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    VideoPlayerActivity videoPlayerActivity;
    List<Favorite> favorites;

    public FavoriteAdapter(VideoPlayerActivity favouriteActivity, List<Favorite> favorites) {
        this.videoPlayerActivity = favouriteActivity;
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(videoPlayerActivity).inflate(R.layout.item_content, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.title.setText("" + favorites.get(position).getTitle());

        holder.round.setVisibility(View.VISIBLE);
        holder.folder.setVisibility(View.INVISIBLE);
        holder.time.setVisibility(View.VISIBLE);

        long durationMilliseconds = Long.parseLong(favorites.get(position).getTime());
        String formattedDuration = millisecondsToTime(durationMilliseconds);
        holder.time.setText(formattedDuration);

        Glide.with(holder.itemView.getContext()).
                load(favorites.get(position).getPath()).
                placeholder(R.drawable.videolable).
                centerCrop().
                into(holder.profile);

        long fileSizeInBytes = Long.parseLong(favorites.get(position).getSize());
        double fileSizeInMB = (double) fileSizeInBytes / (1024 * 1024);
        String fileSizeFormatted = String.format("%.2f MB", fileSizeInMB);

        holder.size.setText(""+fileSizeFormatted);

        holder.round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), Preview.class);
                intent.putExtra("path", favorites.get(position).getPath());
                intent.putExtra("title", favorites.get(position).getTitle());
                intent.putExtra("time",favorites.get(position).getTime());
                intent.putExtra("size",favorites.get(position).getSize());
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = favorites.get(position).getTitle();
                String path = String.valueOf(favorites.get(position).getPath());

                PopupMenu popupMenu = new PopupMenu(videoPlayerActivity, holder.menu);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu2, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.RemoveFavorite) {

                            favorites.remove(position);

                            notifyDataSetChanged();

                            Toast.makeText(videoPlayerActivity, "Remove Favourite", Toast.LENGTH_SHORT).show();
                        }
                        if (menuItem.getItemId() == R.id.Delete) {

                            File videoFile = new File(favorites.get(position).getPath());

                            if (videoFile.exists()) {
                                // Delete the file
                                boolean deleted = videoFile.delete();

                                if (deleted) {
                                    favorites.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(videoPlayerActivity, "Delete", Toast.LENGTH_SHORT).show();
                                    ;
                                    // File successfully deleted
                                    return true;
                                } else {
                                    // Failed to delete the file
                                    return false;
                                }
                            } else {
                                // File does not exist
                                return false;
                            }

                        }
                        return true;
                    }
                });
                popupMenu.show();

            }
        });
    }

    @Override
    public int getItemCount() {

        return favorites.size();

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
    public static String millisecondsToTime(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

        // Format the time as HH:MM:SS
        return String.format("%02d:%02d", minutes, seconds);
    }
}
