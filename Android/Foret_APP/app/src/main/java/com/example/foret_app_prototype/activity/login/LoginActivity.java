package com.example.foret_app_prototype.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.model.MemberDTO;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean saveLoginData;
    private String email;
    private String pwd;

    AsyncHttpClient client;
    HttpResponse response;
    String url = "http://192.168.55.172:8081/foret/search/login_member.do";

    // 자동로그인, 로그인 유지 X
    private SharedPreferences appData;

    Button button0;
    TextView button1, button2, button3, button4;
    EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        emailEditText = findViewById(R.id.editText1);
        passwordEditText = findViewById(R.id.editText2);


        client = new AsyncHttpClient();
        response = new HttpResponse();

//        appData = getSharedPreferences("appData", MODE_PRIVATE);
//        load();
//
//        // 로그인 데이터가 있으면
//        if(saveLoginData) {
//            emailEditText.setText(email);
//            passwordEditText.setText(pwd);
//        }

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
                RequestParams params = new RequestParams();
                params.put("email", emailEditText.getText().toString().trim());
                params.put("password", passwordEditText.getText().toString().trim());
                client.post(url, params, response);
//                save();
//                intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                finish();
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

//    private void save() {
//        SharedPreferences.Editor editor = appData.edit();
//
//        editor.putBoolean("SAVE_LOGIN_DATA",true);
//        editor.putString("EMAIL", editText1.getText().toString().trim());
//        editor.putString("PWD", editText1.getText().toString().trim());
//
//        editor.apply();
//    }
//
//    private void load() {
//        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA",false);
//        email = appData.getString("EMAIL", "");
//        pwd = appData.getString("PWD", "");
//    }

    class HttpResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            Gson gson = new Gson();
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if(RT.equals("OK")) {
                    JSONArray member = json.getJSONArray("member");
                    JSONObject temp = member.getJSONObject(0);
                    MemberDTO memberDTO = gson.fromJson(temp.toString(), MemberDTO.class);
                    // 세션에 담아서 로그인 페이지로
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show();
        }
    }

}