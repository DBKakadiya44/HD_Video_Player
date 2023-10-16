package com.example.hdvideoplayer.grantpermission;

import static com.example.hdvideoplayer.splash.SplashActivity.editor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hdvideoplayer.databinding.ActivityPermissionBinding;
import com.example.hdvideoplayer.start.StartActivity;

public class PermissionActivity extends AppCompatActivity {

    ActivityPermissionBinding binding;
    public static final int PERMISSION_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.grantpermision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Request READ_EXTERNAL_STORAGE permission
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    } else {
                        // Permission is already granted, so start the main activity
                        startMainActivity();
                    }
                } else {
                    // For devices below Android M, no need to request permissions, so start the main activity
                    startMainActivity();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        // You have MANAGE_EXTERNAL_STORAGE permission, so start the main activity
                        startMainActivity();
                    } else {
                        // Request MANAGE_EXTERNAL_STORAGE permission
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, PERMISSION_REQUEST_CODE);
                    }
                } else {
                    // For devices below Android 11, no need to request MANAGE_EXTERNAL_STORAGE permission
                    startMainActivity();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start the main activity
                startMainActivity();
            } else {
                // Permission denied, handle accordingly (e.g., show a message or exit the app)
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // MANAGE_EXTERNAL_STORAGE permission granted, start the main activity
                    startMainActivity();
                } else {
                    // Permission denied, handle accordingly (e.g., show a message or exit the app)
                }
            }
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(PermissionActivity.this, StartActivity.class);
        startActivity(intent);

        editor.putInt("login",1);
        editor.commit();

        finish(); // Finish this activity after starting the main activity
    }

}