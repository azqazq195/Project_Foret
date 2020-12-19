package com.example.foret_app_prototype.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.chat.ChatFragment;
import com.example.foret_app_prototype.activity.free.FreeFragment;
import com.example.foret_app_prototype.activity.home.HomeFragment;
import com.example.foret_app_prototype.activity.login.LoginActivity;
import com.example.foret_app_prototype.activity.login.SessionManager;
import com.example.foret_app_prototype.activity.menu.AppGuideActivity;
import com.example.foret_app_prototype.activity.menu.AppNoticeActivity;
import com.example.foret_app_prototype.activity.menu.MyInfoActivity;
import com.example.foret_app_prototype.activity.notify.NotifyFragment;
import com.example.foret_app_prototype.activity.notify.Token;
import com.example.foret_app_prototype.activity.search.SearchFragment;
import com.example.foret_app_prototype.model.MemberDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
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

import java.util.HashMap;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    BottomNavigationView nav_bottom;
    HomeFragment homeFragment;
    FreeFragment freeFragment;
    SearchFragment searchFragment;
    ChatFragment chatFragment;
    NotifyFragment notifyFragment;
    DrawerLayout container;
    long pressedTime = 0;
    NavigationView nav_drawer;
    LinearLayout containerLayout;
    MemberDTO memberDTO;
    AsyncHttpClient client;
    HttpResponse response;
    String url = "http://34.72.240.24:8085/foret/search/member.do";
    // String url = "http://192.168.0.180:8085/foret/search/member.do";
    TextView button_out, drawer_text1, drawer_text2, drawer_text3, drawer_text4;
    ImageView button_out2, button_drawcancel, profile;
    Intent intent;

    SessionManager sessionManager;
    FirebaseAuth mAuth;
    FirebaseUser currntuser;
    Context context;
    String message;
    String mUID;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MemberDTO getMemberDTO() {
        return memberDTO;
    }

    public void setMemberDTO(MemberDTO memberDTO) {
        this.memberDTO = memberDTO;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 상태바 색상 변경
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.foret4));

        mAuth = FirebaseAuth.getInstance();

        context = this;
        homeFragment = new HomeFragment(context);
        freeFragment = new FreeFragment(context);
        searchFragment = new SearchFragment(context);
        chatFragment = new ChatFragment(context);
        notifyFragment = new NotifyFragment(context);

        nav_bottom = findViewById(R.id.nav_bottom);
        container = findViewById(R.id.container);
        containerLayout = findViewById(R.id.containerLayout);
        nav_drawer = findViewById(R.id.nav_drawer);
        button_out = findViewById(R.id.button_out); // 햄버거 로그아웃버튼
        button_out2 = findViewById(R.id.button_out2); // 햄버거 로그아웃버튼
        button_drawcancel = findViewById(R.id.button_drawcancel); // 햄버거 닫기
        drawer_text1 = findViewById(R.id.drawer_text1); // 회원닉네임
        drawer_text2 = findViewById(R.id.drawer_text2); // 이메일
        drawer_text3 = findViewById(R.id.drawer_text3); // 멤머 아이디
        drawer_text4 = findViewById(R.id.drawer_text4); // 가입일
        profile = findViewById(R.id.drawer_profile); // 햄버거메뉴에 들어갈 프로필사진

        nav_bottom.setOnNavigationItemSelectedListener(this);
        nav_drawer.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.containerLayout, freeFragment).commit();
        }

        client = new AsyncHttpClient();
        response = new HttpResponse();
        sessionManager = new SessionManager(this);

        // 아이디 가져오기
        id = getIntent().getStringExtra("id");
        if (id == null || id.equals("")) {
            id = "" + sessionManager.getSession();
        }

        final int DEFAULT_TIME = 40 * 1000;
        client.setConnectTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);
        client.setTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(url, params, response);

        button_out.setOnClickListener(this);
        button_out2.setOnClickListener(this);
        button_drawcancel.setOnClickListener(this);

    }

    // 안드로이드 푸쉬알림을 위한 추가
    public void updateToken(String token) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token(token);
        ref.child(mUID).setValue(mToken);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { // 네비게이션 드로어 메뉴, 바텀네비게이션 메뉴 둘다 이벤트 처리
        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, homeFragment).commit();
                break;
            case R.id.navigation_freeboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, freeFragment).commit();
                break;
            case R.id.navigation_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, searchFragment).commit();
                break;
            case R.id.navigation_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, chatFragment).commit();
                break;
            case R.id.navigation_notify:
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, notifyFragment).commit();
                break;
            case R.id.drawer_notice: // 햄버거 공지사항 버튼
                intent = new Intent(this, AppNoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.drawer_myinfo: // 햄버거 내정보
                intent = new Intent(this, MyInfoActivity.class);
                intent.putExtra("memberDTO", memberDTO);
                startActivity(intent);
                break;
            case R.id.drawer_help: // 햄버거 도움말
                // Toast.makeText(this, "레이아웃으로 도움말 설명할거 레이아웃 5장", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AppGuideActivity.class));
                break;
            case R.id.drawer_foret: // 햄버거 foret ->그냥 넣어놈
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (container.isDrawerOpen(GravityCompat.END)) {
            container.closeDrawer(GravityCompat.END);
            return;
        }
        if (pressedTime == 0) {
            Toast.makeText(MainActivity.this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        } else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);
            if (seconds > 2000) {
                Toast.makeText(MainActivity.this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                pressedTime = 0;
            } else {
                super.onBackPressed();
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_out: // 로그아웃
                logOutDialog();
                break;
            case R.id.button_out2: // 로그아웃
                logOutDialog();
                break;
            case R.id.button_drawcancel: // 햄버거 닫기
                container.closeDrawer(GravityCompat.END);
                break;
        }
    }

    private void logOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 로그아웃 눌렀을 때 실행되야하는 이벤트->로그아웃처리

                // 세션 제거
                sessionManager.removeSession();

                // 파이어 베이스 로그아웃 만들기
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                auth.signOut();
                updateuserActiveStatusOff();
                Toast.makeText(context, "로그아웃 됨", Toast.LENGTH_LONG).show();

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                MainActivity.this.finish();


            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    class HttpResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            Gson gson = new Gson();

            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if (RT.equals("OK")) {

                    JSONArray member = json.getJSONArray("member");
                    JSONObject temp = member.getJSONObject(0);
                    memberDTO = gson.fromJson(temp.toString(), MemberDTO.class);

                    setMemberDTO(memberDTO);
                    memberDTO.setPhoto(temp.getString("photo"));
                    // Bundle bundle = new Bundle();
                    // bundle.putSerializable("membetDTO",memberDTO);
                    // searchFragment = new SearchFragment(memberDTO);
                    // Log.e("[test]","포토 루트?"+memberDTO.getPhoto());

                    LoginActivity loginActivity = (LoginActivity) LoginActivity.loginActivity;
                    // 세션에 담아서 로그인 페이지로
                    Log.e("[test]","세션 담기전 DTO?"+memberDTO+toString());
                    SessionManager sessionManager = new SessionManager(loginActivity);
                    sessionManager.saveSession(memberDTO);

                    // 데이터 셋팅 HERE ----------------
                    // Toast.makeText(MainActivity.this, memberDTO.toString(),
                    // Toast.LENGTH_SHORT).show();

                    fillTextView(R.id.drawer_text1, memberDTO.getNickname());
                    fillTextView(R.id.drawer_text2, memberDTO.getEmail());
                    fillTextView(R.id.drawer_text3, memberDTO.getId() + "");
                    fillTextView(R.id.drawer_text4, memberDTO.getReg_date());
                    try {
                        // 사진이 있을 떄
                        intoImage(context, memberDTO.getPhoto(), R.id.drawer_profile);
                    } catch (Exception e) {
                        // 사진이 없을떄
                        intoImage(context, "", R.id.drawer_profile);
                    }

                    // 파이어 베이스 로그인 상태 만들기
                    updateuserActiveStatusOn();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(MainActivity.this, "에러", Toast.LENGTH_SHORT).show();
            Log.e("[test]", "리스펀스 페일 진입");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 데이터 다시 받아오기
        final int DEFAULT_TIME = 40 * 1000;
        client.setConnectTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);
        client.setTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(url, params, response);

        currntuser = FirebaseAuth.getInstance().getCurrentUser();
        Log.e("[test]", FirebaseAuth.getInstance().getCurrentUser() + "");
        if (currntuser == null) {
            // 로그인 아닌상태
            Toast.makeText(context, "파이어베이스 로그아웃 상태..", Toast.LENGTH_LONG).show();
        } else {
            mUID = currntuser.getUid();
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        Log.e("[test]", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    String token = task.getResult();
                    updateToken(token);
                }
            });

            SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Current_USERID", mUID);
            editor.apply();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //  if (FirebaseAuth.getInstance().getCurrentUser() != null)
        //updateuserActiveStatusOff();
    }

    // 내상태 온라인 만들기
    private void updateuserActiveStatusOn() {
        FirebaseUser currentUseruser = FirebaseAuth.getInstance().getCurrentUser();
        final String userUid = currentUseruser.getUid();
        DatabaseReference userAcitive = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
        HashMap<String, Object> onlineStatus = new HashMap<>();
        onlineStatus.put("onlineStatus", "online");
        onlineStatus.put("listlogined_date", "현재 접속중");
        onlineStatus.put("id", memberDTO.getId());
        onlineStatus.put("nickname", memberDTO.getNickname()); // 닉네임 최신화
        onlineStatus.put("user_id", memberDTO.getPassword()); // 비밀번호 최신화
        userAcitive.updateChildren(onlineStatus);
    }

    // 내상태 오프라인 상태 만들기
    private void updateuserActiveStatusOff() {
        FirebaseUser currentUseruser = FirebaseAuth.getInstance().getCurrentUser();
        final String userUid = currentUseruser.getUid();
        DatabaseReference userAcitive = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
        HashMap<String, Object> onlineStatus = new HashMap<>();
        onlineStatus.put("onlineStatus", "offline");

        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.KOREAN);
        cal.setTimeInMillis(Long.parseLong(String.valueOf(System.currentTimeMillis())));
        String dateTime = DateFormat.format("yy/MM/dd hh:mm aa", cal).toString();

        onlineStatus.put("listlogined_date", "Last Seen at : " + dateTime);
        userAcitive.updateChildren(onlineStatus);
    }

    public void fillTextView(int id, String text) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(text);
    }

    private void intoImage(Context context, String message, int profile) {
        ImageView iv = (ImageView) findViewById(profile);
        if (message.equals("") || message == null) {
            Glide.with(context).load(R.drawable.icon4).into(iv);
        }
        Glide.with(context).load(message).fallback(R.drawable.icon2).into(iv);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateuserActiveStatusOff();
    }
}