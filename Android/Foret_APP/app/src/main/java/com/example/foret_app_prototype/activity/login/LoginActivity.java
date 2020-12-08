package com.example.foret_app_prototype.activity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.helper.ProgressDialogHelper;
import com.example.foret_app_prototype.model.MemberDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static LoginActivity loginActivity;

    private boolean saveLoginData;
    private String email;
    private String pwd;
    String id;
    AsyncHttpClient client;
    HttpResponse response;
    String url = "http://34.72.240.24:8085/foret/search/member_login.do";
    Button button0;
    TextView button3, button4;
    EditText emailEditText, passwordEditText;

    FirebaseAuth mAuth;
    Context context;
    FirebaseUser user;
    String deviceToken;
    String myUid ;
    String myPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loginActivity = LoginActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 상태바 색상 변경
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.foret4));

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

                Log.e("[test]",email+"/"+pwd);

                RequestParams params = new RequestParams();
                params.put("email", email);
                params.put("password", pwd);
                ProgressDialogHelper.getInstance().getProgressbar(this, "로그인 진행중");
                client.post(url, params, response);
                break;
            case R.id.button3 :
                if(emailEditText.getText().toString().trim().equals("")||emailEditText.getText().toString().trim() ==null){
                    Toast.makeText(this,"찾으실 이메일을 입력해주세요.",Toast.LENGTH_LONG).show();
                    break;
                }
                ProgressDialogHelper.getInstance().getProgressbar(context,"잠시만 기다려주세요.");
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("[test]", "Fetching FCM registration token failed", task.getException());
                                    return;
                                }
                                deviceToken = task.getResult();
                                Log.e("[test]", "deviceToken?" + deviceToken);


                                if(deviceToken != null){
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot ds : snapshot.getChildren()){
                                                Log.e("[test]", "ds ref? ?" + ds.getRef());
                                                if(ds.child(ds.getKey()).getValue().equals(deviceToken)){
                                                    //내 토큰 찻음
                                                    myUid = ds.getKey();
                                                    Log.e("[test]", "ds.getKey()).getValue()?" + ds.child(ds.getKey()).getValue());
                                                }
                                                if(myUid!=null){
                                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Users");

                                                    ref2.child(myUid).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for(DataSnapshot ds : snapshot.getChildren()){
                                                                Log.e("[test]", "ds 22 의 ref??" + ds.getRef());
                                                                myPw = ds.child("user_id").getValue()+"";
                                                                Log.e("[test]", "myPw?" + myPw);

                                                                passwordEditText.setText(myPw);
                                                                Toast.makeText(context,"찾으신 이메일의 비밀번호가 입력되었습니다. 로그인 후 패스워드를 변경해주세요.",Toast.LENGTH_LONG).show();
                                                                ProgressDialogHelper.getInstance().removeProgressbar();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Toast.makeText(context,"등록된 이메일이 없습니다. 포레를 시작해주세요.",Toast.LENGTH_LONG).show();
                                                            ProgressDialogHelper.getInstance().removeProgressbar();
                                                        }
                                                    });

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(context,"회원가입시 등록된 디바이스를 이용해주세요",Toast.LENGTH_LONG).show();
                                            ProgressDialogHelper.getInstance().removeProgressbar();
                                        }
                                    });


                                }
                            }
                        });




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
        intent.putExtra("id",id);
        startActivity(intent);
        finish();
    }

    class HttpResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                Log.e("[test]",RT);
                Log.e("[test]",json.getString("id"));

                if(RT.equals("OK")) {
                    //파이어 베이스
                    joinedMember(email,pwd);
                    id = json.getString("id");

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