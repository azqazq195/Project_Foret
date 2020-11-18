package com.example.foret.Activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foret.R;

public class SplashActivity extends AppCompatActivity {

    // 스플래시 스크린 속도 ms
    private static int SPLASH_SCREEN = 3500;

    Animation bottomAnim;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 위에 상태바 제거
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);

        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        textView = findViewById(R.id.textView);

        textView.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(com.example.foret.Activity.login.SplashActivity.this, com.example.foret.Activity.login.LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}