package com.example.hdvideoplayer.allfolderpreview.adapter;

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
import com.example.hdvideoplayer.allfolderpreview.AllFolderPreviewActivity;
import com.example.hdvideoplayer.model.Favorite;
import com.example.hdvideoplayer.videoplayer.VideoPlayerActivity;

import java.util.ArrayList;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder>
{
    AllFolderPreviewActivity allFolderPreviewActivity;
    ArrayList<String> list;
    ArrayList<String> title;

    public ListsAdapter(AllFolderPreviewActivity allFolderPreviewActivity, ArrayList<String> list, ArrayList<String> title) {
        this.allFolderPreviewActivity = allFolderPreviewActivity;
        this.list = list;
        this.title=title;
    }

    @NonNull
    @Override
    public ListsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(allFolderPreviewActivity).inflate(R.layout.item_content,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.round.setVisibility(View.VISIBLE);
        holder.profile.setImageResource(R.drawable.videolable);
        holder.folder.setVisibility(View.INVISIBLE);
        holder.time.setVisibility(View.INVISIBLE);
        holder.size.setText(""+list.get(position));
        holder.title.setText(""+title.get(position));

        Glide.with(holder.itemView.getContext()).
                load(list.get(position)).
                placeholder(R.drawable.videolable).
                centerCrop().
                into(holder.profile);

        holder.round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), Preview.class);
                intent.putExtra("path", list.get(position));
                intent.putExtra("title", title.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titlet = title.get(position);
                String path = String.valueOf(list.get(position));

                PopupMenu popupMenu = new PopupMenu(allFolderPreviewActivity, holder.menu);

                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if(menuItem.getItemId()==R.id.AddFavorite)
                        {
                            Favorite favorite = new Favorite(titlet,path);

                            for(int i = 0; i< VideoPlayerActivity.favorites.size() ; i++)
                            {
                                if(VideoPlayerActivity.favorites.get(i).getTitle().equals(title) && VideoPlayerActivity.favorites.get(i).getPath().equals(path))
                                {
                                    VideoPlayerActivity.favorites.remove(i);
                                }
                            }
                            VideoPlayerActivity.favorites.add(favorite);

                            //VideoPlayerActivity.favorites.add(favorite);

                            Toast.makeText(allFolderPreviewActivity, "Added Favourite", Toast.LENGTH_SHORT).show();
                        }
                        if (menuItem.getItemId()==R.id.Delete)
                        {
                            Toast.makeText(allFolderPreviewActivity, "Delete", Toast.LENGTH_SHORT).show();
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
        return list.size();
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
