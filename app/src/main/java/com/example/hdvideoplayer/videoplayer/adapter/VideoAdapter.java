package com.example.hdvideoplayer.videoplayer.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.allfolderpreview.AllFolderPreviewActivity;
import com.example.hdvideoplayer.model.VideoModel;
import com.example.hdvideoplayer.videoplayer.VideoPlayerActivity;
import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    VideoPlayerActivity videoPlayerActivity;
    ArrayList<VideoModel> alImages;

    public VideoAdapter(VideoPlayerActivity videoPlayerActivity, ArrayList<VideoModel> alImages) {
        this.videoPlayerActivity = videoPlayerActivity;
        this.alImages = alImages;
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(videoPlayerActivity).inflate(R.layout.item_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.title.setText("" + alImages.get(position).getFolderName());

        holder.round.setVisibility(View.INVISIBLE);
        holder.folder.setVisibility(View.VISIBLE);

        Glide.with(holder.itemView.getContext()).
                load(alImages.get(position).getVideoPath()).
                placeholder(R.drawable.videolable).
                centerCrop().
                into(holder.profile);

        holder.size.setText(alImages.get(position).getVideoPath().size() + " Videos");

        holder.time.setVisibility(View.INVISIBLE);
        holder.menu.setVisibility(View.INVISIBLE);

        holder.folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(videoPlayerActivity, AllFolderPreviewActivity.class);
                intent.putExtra("name", alImages.get(position).getFolderName());
                intent.putExtra("list", alImages.get(position).getVideoPath());
                intent.putExtra("title", alImages.get(position).getTitle());
                videoPlayerActivity.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return alImages.size();
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
