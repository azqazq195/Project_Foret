package com.example.foret_app_prototype.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.foret_app_prototype.R;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    Button button;
    TextView textView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        button.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button :
                intent = new Intent(this, LoginActivity.class);
                break;
            case R.id.textView :
                intent = new Intent(this, JoinUsActivity.class);
                break;
        }
        startActivity(intent);
    }
}