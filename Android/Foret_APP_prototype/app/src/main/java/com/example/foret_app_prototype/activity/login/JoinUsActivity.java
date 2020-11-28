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

    Button button;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_us);
        button = findViewById(R.id.button);
        imageButton = findViewById(R.id.imageButton);

        button.setOnClickListener(this);
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button :
                Intent intent = new Intent(this, MainActivity.class);
                break;
            case R.id.imageButton :
                finish();
                break;
        }
    }
}