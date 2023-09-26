package com.example.hdvideoplayer.grantpermission;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.databinding.ActivityPermissionBinding;
import com.example.hdvideoplayer.start.StartActivity;
import com.example.hdvideoplayer.startprivacy.StartPrivacyActivity;

public class PermissionActivity extends AppCompatActivity {

    ActivityPermissionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.grantpermision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PermissionActivity.this , StartActivity.class);
                startActivity(intent);
            }
        });

    }
}