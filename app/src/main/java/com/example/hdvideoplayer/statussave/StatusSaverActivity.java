package com.example.hdvideoplayer.statussave;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.databinding.ActivityStatusSaverBinding;

public class StatusSaverActivity extends AppCompatActivity {
    ActivityStatusSaverBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatusSaverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
}