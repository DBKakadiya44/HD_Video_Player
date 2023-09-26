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

import com.example.hdvideoplayer.Preview;
import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.allfolderpreview.AllFolderPreviewActivity;
import com.example.hdvideoplayer.videoplayer.adapter.FolderAdapter;

import java.util.ArrayList;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder>
{
    AllFolderPreviewActivity allFolderPreviewActivity;
    ArrayList<String> list;

    public ListsAdapter(AllFolderPreviewActivity allFolderPreviewActivity, ArrayList<String> list) {
        this.allFolderPreviewActivity = allFolderPreviewActivity;
        this.list = list;
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
        holder.size.setVisibility(View.INVISIBLE);
        holder.title.setText(""+list.get(position));

        holder.round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), Preview.class);
                intent.putExtra("path", list.get(position));
                holder.itemView.getContext().startActivity(intent);
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

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(allFolderPreviewActivity, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (view.getId() == R.id.Copy) {
                    Toast.makeText(allFolderPreviewActivity, "Copy", Toast.LENGTH_SHORT).show();
                }
                if (view.getId() == R.id.Details) {
                    Toast.makeText(allFolderPreviewActivity, "Details", Toast.LENGTH_SHORT).show();
                }
                if (view.getId() == R.id.Rename) {
                    Toast.makeText(allFolderPreviewActivity, "Rename", Toast.LENGTH_SHORT).show();
                }
                if (view.getId() == R.id.Delete) {
                    Toast.makeText(allFolderPreviewActivity, "Delete", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
