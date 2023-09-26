package com.example.hdvideoplayer.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hdvideoplayer.MainActivity;
import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.databinding.ActivityStartBinding;
import com.example.hdvideoplayer.grantpermission.PermissionActivity;

public class StartActivity extends AppCompatActivity {
    ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this , MainActivity.class);
                startActivity(intent);
            }
        });

        binding.imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////share


            }
        });

        binding.imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///rate app



            }
        });

        binding.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////privacy


            }
        });

    }
}