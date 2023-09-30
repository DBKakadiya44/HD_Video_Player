package com.example.hdvideoplayer.instagram;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.example.hdvideoplayer.databinding.ActivityInstagramBinding;

public class InstagramActivity extends AppCompatActivity {
    ActivityInstagramBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstagramBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        binding.indownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String link = String.valueOf(binding.inedittext.getText());
//
//                String downloadUrl = link; // Replace with your media URL
//                String destinationDirectory = Environment.DIRECTORY_DOWNLOADS;
//
//                Random random = new Random();
//                int min = 1; // Minimum value (inclusive)
//                int max = 100000; // Maximum value (exclusive)
//                int randomNumber = random.nextInt(max - min) + min;
//
//                String fileName = "mediafile"+randomNumber+".mp3"; // Replace with your desired file name
//
//                downloadMedia(getApplicationContext(), downloadUrl, destinationDirectory, fileName);
//
//            }
//        });


    }

    public static void downloadMedia(Context context, String downloadUrl, String destinationDirectory, String fileName) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(downloadUrl);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(destinationDirectory, fileName);

        downloadManager.enqueue(request);
    }

}