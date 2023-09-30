package com.example.hdvideoplayer.videoplayer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.example.hdvideoplayer.MainActivity;
import com.example.hdvideoplayer.Preview;
import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.model.Favorite;
import com.example.hdvideoplayer.model.Recent;
import com.example.hdvideoplayer.model.Video;
import com.example.hdvideoplayer.videoplayer.VideoPlayerActivity;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    Context context;
    List<Video> videos;
    public FolderAdapter(Context context,List<Video> videos) {
        this.context = context;
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

                String title = videos.get(position).getTitle();
                String path = String.valueOf(videos.get(position).getPath());

                PopupMenu popupMenu = new PopupMenu(context, holder.menu);

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

                            Toast.makeText(context, "Added Favourite", Toast.LENGTH_SHORT).show();
                        }
                        if (menuItem.getItemId()==R.id.Delete)
                        {
                            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();

            }
        });

        holder.title.setText(videos.get(position).getTitle());
        holder.size.setText(videos.get(position).getPath());

        Glide.with(holder.itemView.getContext()).
                load(videos.get(position).getPath()).
                placeholder(R.drawable.videolable).
                centerCrop().
                into(holder.profile);

//            Glide.with(holder.itemView.getContext())
//                    .load(videos.get(position).getPath())
//                    // Optional: Crop the image to fit the ImageView
//                    .into(holder.profile);

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), Preview.class);
                intent.putExtra("path", videos.get(position).getPath());
                intent.putExtra("title", videos.get(position).getTitle());
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
            return videos.size();
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
