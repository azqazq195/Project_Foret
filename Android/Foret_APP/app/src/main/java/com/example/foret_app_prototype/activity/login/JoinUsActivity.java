package com.example.foret_app_prototype.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;

public class JoinUsActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout layout1, layout2, layout3, layout4, layout5, layout6;
    EditText editText1, editText2, editText3, editText4, editText5, editText6;
    TextView textView5, textView7;
    Button button2;
    ImageButton button1;

    // 레이아웃 애니메이션
    Animation animation = new AlphaAnimation(0, 1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_us);

        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);
        layout5 = findViewById(R.id.layout5);
        layout6 = findViewById(R.id.layout6);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);
        textView5 = findViewById(R.id.textView5);
        textView7 = findViewById(R.id.textView7);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

//        editText1.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                switch (keyCode){
//                    case KeyEvent.KEYCODE_ENTER:
//                        checkId();
//                }
//                return true;
//            }
//        });


        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        animation.setDuration(1000); // 레이아웃 애니메이션

        editText1.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1 : // x 버튼
                finish();
                break;
            case R.id.button2 : // 회원 가입
                if(checkId()) {
                    Intent intent = new Intent(this, GuideActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    private boolean checkId() {
        String name = editText1.getText().toString().trim();
        if(name.equals("")) {
            Toast.makeText(this, "이름를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        layout2.setVisibility(View.VISIBLE);
        layout2.setAnimation(animation);
        editText2.requestFocus();

        String nickname = editText2.getText().toString().trim();
        if(nickname.equals("")) {
            Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        layout3.setVisibility(View.VISIBLE);
        layout3.setAnimation(animation);
        editText3.requestFocus();

        String birth = editText3.getText().toString().trim();
        if(birth.equals("")) {
            Toast.makeText(this, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        layout4.setVisibility(View.VISIBLE);
        layout4.setAnimation(animation);
        editText4.requestFocus();

        String email = editText4.getText().toString().trim();
        if(email.equals("")) {
            Toast.makeText(this, "이메일을을 입력해주세요.", Toast.LENGTH_SHORT).show();
           return false;
        }
        textView5.setVisibility(View.VISIBLE);

        layout5.setVisibility(View.VISIBLE);
        layout5.setAnimation(animation);
        editText5.requestFocus();

        String pw = editText5.getText().toString().trim();
        if(pw.equals("")) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        editText6.setVisibility(View.VISIBLE);
        layout5.setAnimation(animation);
        editText6.requestFocus();

        String pw2= editText6.getText().toString().trim();
        if (pw2.equals("")) {
            textView7.setText("비밀번호를 입력해주세요.");
            textView7.setVisibility(View.VISIBLE);
            return false;
        } else if(!pw.equals(pw2)) {
            textView7.setText("비밀번호가 다릅니다.");
            textView7.setVisibility(View.VISIBLE);
            return false;
        } else {
            textView7.setText("비밀번호가 일치합니다.");
            textView7.setVisibility(View.VISIBLE);
        }
        return true;
    }

//    @Override
//    public boolean onKey(View v, int keyCode, KeyEvent event) {
//        switch (v.getId()) {
//            case R.id.editText1:
//                if(event.KEYCODE_ENTER == 66) {
//                    checkId();
//                }
//                break;
//            case R.id.editText2:
//                if(event.KEYCODE_ENTER == 66) {
//                    checkId();
//                }
//                break;
//            case R.id.editText3:
//                if(event.KEYCODE_ENTER == 66) {
//                    checkId();
//                }
//                break;
//            case R.id.editText4:
//                if(event.KEYCODE_ENTER == 66) {
//                    checkId();
//                }
//                break;
//            case R.id.editText5:
//                if(event.KEYCODE_ENTER == 66) {
//                    checkId();
//                }
//                break;
//            case R.id.editText6:
//                if(event.KEYCODE_ENTER == 66) {
//                    checkId();
//                }
//                break;
//        }
//        return true;
//    }
}