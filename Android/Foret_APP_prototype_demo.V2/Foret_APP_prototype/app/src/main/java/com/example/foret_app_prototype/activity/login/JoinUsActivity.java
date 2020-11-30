package com.example.foret_app_prototype.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;

public class JoinUsActivity extends AppCompatActivity implements View.OnClickListener {

    Button button2;
    ImageButton button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_us);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1 :
                finish();
                break;
            case R.id.button2 :
                Intent intent = new Intent(this, GuideActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}