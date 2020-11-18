package com.example.foret.Activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foret.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_sign_up);

        btn_Back = findViewById(R.id.btn_Back);
        btn_Back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}