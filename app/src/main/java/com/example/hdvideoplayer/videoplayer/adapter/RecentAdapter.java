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
import com.example.hdvideoplayer.model.Recent;
import com.example.hdvideoplayer.videoplayer.VideoPlayerActivity;
import java.util.ArrayList;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {

    VideoPlayerActivity videoPlayerActivity;
    ArrayList<Recent> recent;
    public RecentAdapter(VideoPlayerActivity videoPlayerActivity, ArrayList<Recent> recent) {
        this.videoPlayerActivity = videoPlayerActivity;
        this.recent = recent;
    }
    @NonNull
    @Override
    public RecentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(videoPlayerActivity).inflate(R.layout.item_content , parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.title.setText(recent.get(position).getTitle());
        holder.size.setText(recent.get(position).getPath());

            Glide.with(holder.itemView.getContext()).
                    load(recent.get(position).getPath()).
                    placeholder(R.drawable.videolable).
                    centerCrop().
                    into(holder.profile);

//        Glide.with(holder.itemView.getContext())
//                .load(recent.get(position))
//                // Optional: Crop the image to fit the ImageView
//                .into(holder.profile);

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = String.valueOf(recent.get(position).getTitle());
                String path = String.valueOf(recent.get(position).getPath());

                PopupMenu popupMenu = new PopupMenu(videoPlayerActivity, holder.menu);

                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if(menuItem.getItemId()==R.id.AddFavorite)
                        {
                            Favorite favorite = new Favorite(title,path);

                            for(int i=0 ; i<VideoPlayerActivity.favorites.size() ; i++)
                            {
                                if(VideoPlayerActivity.favorites.get(i).getTitle().equals(title) && VideoPlayerActivity.favorites.get(i).getPath().equals(path))
                                {
                                    VideoPlayerActivity.favorites.remove(i);
                                }
                            }
                            VideoPlayerActivity.favorites.add(favorite);

                            //VideoPlayerActivity.favorites.add(favorite);

                            Toast.makeText(videoPlayerActivity, "Added Favourite", Toast.LENGTH_SHORT).show();
                        }
                        if (menuItem.getItemId()==R.id.Delete)
                        {
                            Toast.makeText(videoPlayerActivity, "Delete", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();

            }
        });

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), Preview.class);
                intent.putExtra("path", recent.get(position).getPath());
                intent.putExtra("title", recent.get(position).getTitle());
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = recent.get(position).getTitle();
                String path = String.valueOf(recent.get(position).getPath());

                PopupMenu popupMenu = new PopupMenu(videoPlayerActivity, holder.menu);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if(menuItem.getItemId()==R.id.AddFavorite)
                        {
                            Favorite favorite = new Favorite(title,path);
                            VideoPlayerActivity.favorites.add(favorite);

                            Toast.makeText(videoPlayerActivity, "Added Favourite", Toast.LENGTH_SHORT).show();
                        }
                        if (menuItem.getItemId()==R.id.Delete)
                        {
                            Toast.makeText(videoPlayerActivity, "Delete", Toast.LENGTH_SHORT).show();
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
        return recent.size();
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

}
