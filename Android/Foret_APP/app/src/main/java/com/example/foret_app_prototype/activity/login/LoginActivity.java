package com.example.foret_app_prototype.activity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.helper.ProgressDialogHelper;
import com.example.foret_app_prototype.model.MemberDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean saveLoginData;
    private String email;
    private String pwd;

    AsyncHttpClient client;
    HttpResponse response;
    String url = "http://34.72.240.24:8085/foret/search/member_login.do";
    //String url = "http://192.168.219.100:8085/foret/search/member.do";
    Button button0;
    TextView button3, button4;
    EditText emailEditText, passwordEditText;

    FirebaseAuth mAuth;
    Context context;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button0 = findViewById(R.id.button0);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        emailEditText = findViewById(R.id.editText1);
        passwordEditText = findViewById(R.id.editText2);
        context = this;
        client = new AsyncHttpClient();
        final int DEFAULT_TIME = 20*1000;
        client.setConnectTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);
        client.setTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);
        response = new HttpResponse();

        button0.setOnClickListener(this); //로그인
        button3.setOnClickListener(this); //비밀번호 찾기
        button4.setOnClickListener(this); //회원가입

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SessionManager sessionManager = new SessionManager(this);
        int userID = sessionManager.getSession();

        if(userID != -1) {
            moveToMainActivity();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button0 :
                //파이어 베이스용
                email = emailEditText.getText().toString().trim();
                pwd = passwordEditText.getText().toString().trim();

                RequestParams params = new RequestParams();
                params.put("email", emailEditText.getText().toString().trim());
                params.put("password", passwordEditText.getText().toString().trim());
                ProgressDialogHelper.getInstance().getProgressbar(this, "로그인 진행중");
                client.post(url, params, response);
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

    private void moveToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("email",email);
        intent.putExtra("pwd",pwd);
        startActivity(intent);
        finish();
    }

    class HttpResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            Gson gson = new Gson();
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if(RT.equals("OK")) {
                    //파이어 베이스
                    joinedMember(email,pwd);

                    JSONArray member = json.getJSONArray("member");
                    JSONObject temp = member.getJSONObject(0);
                    MemberDTO memberDTO = gson.fromJson(temp.toString(), MemberDTO.class);
                    // 세션에 담아서 로그인 페이지로
                    SessionManager sessionManager = new SessionManager(LoginActivity.this);
                    sessionManager.saveSession(memberDTO);
                    Log.e("[test]","성공진입/"+statusCode);
                    ProgressDialogHelper.getInstance().removeProgressbar();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show();
            Log.e("[test]",error.getMessage()+"/"+statusCode);
        }
    }

    //파이어 베이스 로그인
    public void joinedMember(String member_email, String member_id) {
        Log.d("TAG", "signInWithEmail:진입");
        Log.d("TAG", "member_email:"+member_email);
        Log.d("TAG", "member_id:"+member_id);
        mAuth.signInWithEmailAndPassword(member_email, member_id).
                addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithEmail:success");
                            user = mAuth.getCurrentUser();
                            moveToMainActivity();
                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}