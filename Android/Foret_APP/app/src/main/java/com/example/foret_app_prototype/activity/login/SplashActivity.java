package com.example.foret_app_prototype.activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.foret_app_prototype.R;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout splash;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash = findViewById(R.id.splash);

        splash.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash :
                intent = new Intent(this, LoginActivity.class);
                break;
        }
        startActivity(intent);
    }
}