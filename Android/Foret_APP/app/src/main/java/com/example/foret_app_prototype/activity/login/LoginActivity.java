package com.example.foret_app_prototype.activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button button0;
    TextView button1, button2, button3, button4;
    EditText editText1, editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);

        button0.setOnClickListener(this); //로그인
        button1.setOnClickListener(this); //구글 로그인
        button2.setOnClickListener(this); //카카오 로그인
        button3.setOnClickListener(this); //비밀번호 찾기
        button4.setOnClickListener(this); //회원가입

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button0 :
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button1 :
                Toast.makeText(this, "구글 로그인", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2 :
                Toast.makeText(this, "카카오 로그인", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3 :
                Toast.makeText(this, "이름, 이메일 주소로 비밀번호 찾기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button4 :
                intent = new Intent(this, JoinUsActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}