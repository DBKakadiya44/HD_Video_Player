package com.example.hdvideoplayer.allfolderpreview;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hdvideoplayer.allfolderpreview.adapter.ListsAdapter;
import com.example.hdvideoplayer.databinding.ActivityAllFolderPreviewBinding;

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
        ArrayList<String> size = getIntent().getStringArrayListExtra("size");
        ArrayList<String> time = getIntent().getStringArrayListExtra("time");
        String name = getIntent().getStringExtra("name");

        Log.d("ASASAS", "onCreate: Listttt = "+list);

        ListsAdapter adapter = new ListsAdapter(AllFolderPreviewActivity.this , list, title,size,time);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(AllFolderPreviewActivity.this);
        binding.rvallvideo.setLayoutManager(manager);
        binding.rvallvideo.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        binding.textView8.setText(""+name);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}