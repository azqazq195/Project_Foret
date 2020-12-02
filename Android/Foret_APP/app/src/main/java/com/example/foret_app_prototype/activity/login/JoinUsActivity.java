package com.example.foret_app_prototype.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.example.foret_app_prototype.activity.client.ClientInstance;
import com.google.android.gms.common.api.Response;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class JoinUsActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout layout1, layout2, layout3, layout4, layout5, layout6;
    EditText editText1, editText2, editText3, editText4, editText5, editText6;
    TextView textView5, textView7;
    Button button2;
    ImageButton button1;

    // 레이아웃 애니메이션
    Animation animation = new AlphaAnimation(0, 1);

    //멤버 정보
    String name, nickname, birth, email, pw2;

    AsyncHttpClient client;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_us);

        activity = this;

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

        client = new AsyncHttpClient();

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
            case R.id.button1: // x 버튼
                finish();
                break;
            case R.id.button2: // 회원 가입
                if (checkId()) {
                    //서버에 전송
                   //signInMember();
                    Intent intent = new Intent(activity, GuideActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("nickname",nickname);
                    intent.putExtra("birth",birth);
                    intent.putExtra("email",email);
                    intent.putExtra("pw2",pw2);
                    startActivity(intent);
//                    finish();
                }
                break;
        }
    }

    private boolean checkId() {
        name = editText1.getText().toString().trim();
        if (name.equals("")) {
            Toast.makeText(this, "이름를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        layout2.setAnimation(animation);
        layout2.setVisibility(View.VISIBLE);

        editText2.requestFocus();

        nickname = editText2.getText().toString().trim();
        if (nickname.equals("")) {
            Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(nickname.length()>8){
            Toast.makeText(this, "닉네임은 8자까지 입력가능합니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        layout3.setVisibility(View.VISIBLE);
        layout3.setAnimation(animation);
        editText3.requestFocus();

        birth = editText3.getText().toString().trim();
        if (birth.equals("")) {
            Toast.makeText(this, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        layout4.setVisibility(View.VISIBLE);
        layout4.setAnimation(animation);
        editText4.requestFocus();

        email = editText4.getText().toString().trim();
        if (email.equals("")) {
            Toast.makeText(this, "이메일을을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        textView5.setVisibility(View.VISIBLE);

        layout5.setVisibility(View.VISIBLE);
        layout5.setAnimation(animation);
        editText5.requestFocus();

        String pw = editText5.getText().toString().trim();
        if (pw.equals("")) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        editText6.setVisibility(View.VISIBLE);
        layout5.setAnimation(animation);
        editText6.requestFocus();

        pw2 = editText6.getText().toString().trim();
        if (pw2.equals("")) {
            textView7.setText("비밀번호를 입력해주세요.");
            textView7.setVisibility(View.VISIBLE);
            return false;
        } else if (!pw.equals(pw2)) {
            textView7.setText("비밀번호가 다릅니다.");
            textView7.setVisibility(View.VISIBLE);
            return false;
        } else {
            textView7.setText("비밀번호가 일치합니다.");
            textView7.setVisibility(View.VISIBLE);
        }
        return true;
    }

    private void signInMember() {
        String url = "http://192.168.0.180:8090/app_test1/result/result_insert.do";
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("email", email);
        params.put("password", pw2);
        params.put("birth", birth);

        client.post(url, params, new Response(activity));
    }


    public class Response extends AsyncHttpResponseHandler {
        Activity activity;

        public Response(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String result = new String(responseBody);

            try {
                JSONObject json = new JSONObject(result);

                String rt = json.getString("rt");

                if (rt.equals("FAIL")) {
                    Toast.makeText(activity, "리절트 fail, 이미 사용중인 이메일입니다.", Toast.LENGTH_LONG).show();
                } else {
                    /*
                    Intent intent = new Intent(activity, GuideActivity.class);
                    startActivity(intent);
                    activity.finish();

                     */

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패, 원인 : "+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
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