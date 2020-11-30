package com.example.foret.Activity.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.foret.R;

public class VideoViewActivity extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        videoView = findViewById(R.id.videoView2);

        MediaController mc = new MediaController(this);
        videoView.setMediaController(mc);

        String uri = getIntent().getStringExtra("video");

        videoView.setVideoURI(Uri.parse(uri));
        videoView.start();

    }
}