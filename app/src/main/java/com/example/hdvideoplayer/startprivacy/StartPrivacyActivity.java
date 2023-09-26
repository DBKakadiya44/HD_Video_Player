package com.example.hdvideoplayer.startprivacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.hdvideoplayer.R;
import com.example.hdvideoplayer.databinding.ActivityStartPrivacyBinding;
import com.example.hdvideoplayer.grantpermission.PermissionActivity;

public class StartPrivacyActivity extends AppCompatActivity {

    ActivityStartPrivacyBinding binding;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartPrivacyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.imageView2.setVisibility(View.INVISIBLE);
                binding.imageView3.setVisibility(View.VISIBLE);
                check = 1;
            }
        });

        binding.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.imageView2.setVisibility(View.VISIBLE);
                binding.imageView3.setVisibility(View.INVISIBLE);
                check = 0;
            }
        });

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check==1){
                    Intent intent = new Intent(StartPrivacyActivity.this , PermissionActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(StartPrivacyActivity.this, "Accept Terms", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}