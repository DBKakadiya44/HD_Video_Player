package com.example.hdvideoplayer.musicplayer.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.model.MusicFile;
import com.example.hdvideoplayer.musicplayer.MusicPlayerActivity;
import com.example.hdvideoplayer.musicplayer.PlayMusicActivity;

import java.util.List;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>
{
    MusicPlayerActivity musicPlayerActivity;
    List<MusicFile> musicFiles;

    public MusicAdapter(MusicPlayerActivity musicPlayerActivity, List<MusicFile> musicFiles) {
        this.musicPlayerActivity = musicPlayerActivity;
        this.musicFiles = musicFiles;
    }

    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(musicPlayerActivity).inflate(R.layout.item_content,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.title.setText(musicFiles.get(position).getTitle());
        holder.size.setText(musicFiles.get(position).getArtist());

        holder.profile.setImageResource(R.drawable.music_player);

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(musicPlayerActivity , PlayMusicActivity.class);
                intent.putExtra("name" , musicFiles.get(position).getTitle());
                intent.putExtra("album" , musicFiles.get(position).getAlbum());
                intent.putExtra("data" , musicFiles.get(position).getData());
                intent.putExtra("artist" , musicFiles.get(position).getArtist());
                intent.putExtra("duration" , musicFiles.get(position).getDuration());
                musicPlayerActivity.startActivity(intent);

            }
        });


        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

    }

    @Override
    public int getItemCount() {
        return musicFiles.size();
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
        PopupMenu popupMenu = new PopupMenu(musicPlayerActivity, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (view.getId() == R.id.AddFavorite) {


                    Toast.makeText(musicPlayerActivity, "Copy", Toast.LENGTH_SHORT).show();
                }

                if (view.getId() == R.id.Delete) {


                    Toast.makeText(musicPlayerActivity, "Delete", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        popupMenu.show();
    }

}
