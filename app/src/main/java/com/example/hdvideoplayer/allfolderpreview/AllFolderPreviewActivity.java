package com.example.hdvideoplayer.allfolderpreview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.allfolderpreview.adapter.ListsAdapter;
import com.example.hdvideoplayer.databinding.ActivityAllFolderPreviewBinding;
import com.example.hdvideoplayer.model.VideoModel;
import com.example.hdvideoplayer.videoplayer.VideoPlayerActivity;

import java.util.ArrayList;

public class AllFolderPreviewActivity extends AppCompatActivity {
    ActivityAllFolderPreviewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllFolderPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<String> list = getIntent().getStringArrayListExtra("list");
        ArrayList<String> title = getIntent().getStringArrayListExtra("title");
        String name = getIntent().getStringExtra("name");

        Log.d("ASASAS", "onCreate: Listttt = "+list);

        ListsAdapter adapter = new ListsAdapter(AllFolderPreviewActivity.this , list, title);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(AllFolderPreviewActivity.this);
        binding.rvallvideo.setLayoutManager(manager);
        binding.rvallvideo.setAdapter(adapter);

        binding.textView8.setText(""+name);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}