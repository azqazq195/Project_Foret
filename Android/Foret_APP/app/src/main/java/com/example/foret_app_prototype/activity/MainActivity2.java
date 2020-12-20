package com.example.foret_app_prototype.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.chat.ChatFragment;
import com.example.foret_app_prototype.activity.free.FreeFragment;
import com.example.foret_app_prototype.activity.home.HomeFragment;
import com.example.foret_app_prototype.activity.login.LoginActivity;
import com.example.foret_app_prototype.activity.login.SessionManager;
import com.example.foret_app_prototype.activity.notify.NotifyFragment;
import com.example.foret_app_prototype.activity.search.SearchFragment;
import com.example.foret_app_prototype.model.MemberDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity2 extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    BottomNavigationView nav_bottom;
    HomeFragment homeFragment;
    FreeFragment freeFragment;
    SearchFragment searchFragment;
    ChatFragment chatFragment;
    NotifyFragment notifyFragment;

    DrawerLayout container;
    NavigationView nav_drawer;
    LinearLayout containerLayout;

    TextView button_out, drawer_text1, drawer_text2, drawer_text3, drawer_text4;
    ImageView button_out2, button_drawcancel, profile;
    Intent intent;


    AsyncHttpClient client;
    HttpResponse response;
    String url = "http://54.180.219.200:8085/get/member";

    long pressedTime = 0;

    SessionManager sessionManager;
    FirebaseAuth mAuth;
    FirebaseUser currntuser;
    Context context;
    String message;
    String mUID;
    String id;
    MemberDTO memberDTO;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
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

        // drawer
        nav_drawer = findViewById(R.id.nav_drawer);
        button_out = findViewById(R.id.button_out);                 // 햄버거 로그아웃버튼
        button_out2 = findViewById(R.id.button_out2);               // 햄버거 로그아웃버튼
        button_drawcancel = findViewById(R.id.button_drawcancel);   // 햄버거 닫기
        drawer_text1 = findViewById(R.id.drawer_text1);             // 회원닉네임
        drawer_text2 = findViewById(R.id.drawer_text2);             // 이메일
        drawer_text3 = findViewById(R.id.drawer_text3);             // 멤머 아이디
        drawer_text4 = findViewById(R.id.drawer_text4);             // 가입일
        profile = findViewById(R.id.drawer_profile);                // 햄버거메뉴에 들어갈 프로필사진

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

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    class HttpResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);

            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if (RT.equals("OK")) {

                    JSONArray member = json.getJSONArray("member");
                    JSONObject temp = member.getJSONObject(0);


                    memberDTO = new MemberDTO();
                    memberDTO.setId(Integer.parseInt(temp.getString("id")));
                    memberDTO.setName(temp.getString("name"));
                    memberDTO.setEmail(temp.getString("email"));
                    memberDTO.setPassword(temp.getString("password"));
                    memberDTO.setNickname(temp.getString("nickname"));
                    memberDTO.setBirth(temp.getString("birth"));
                    memberDTO.setReg_date(temp.getString("reg_date"));
                    memberDTO.setDeviceToken(temp.getString("deviceToken"));

                    if (!temp.getString("photo").equals("0")) {
                        memberDTO.setPhoto(temp.getString("photo"));
                    }
                    if (!temp.getString("tag").equals("0")) {
                        List<String> tempList = new ArrayList<>();
                        JSONArray tempArray = temp.getJSONArray("tag");
                        for (int i = 0; i < tempArray.length(); i++) {
                            tempList.add(tempArray.getString(i));
                        }
                        memberDTO.setTag(tempList);
                    }

                    if (!temp.getString("region_si").equals("0")) {
                        List<String> tempList = new ArrayList<>();
                        JSONArray tempArray = temp.getJSONArray("region_si");
                        for (int i = 0; i < tempArray.length(); i++) {
                            tempList.add(tempArray.getString(i));
                        }
                        memberDTO.setRegion_si(tempList);
                    }
                    if (!temp.getString("region_gu").equals("0")) {
                        List<String> tempList = new ArrayList<>();
                        JSONArray tempArray = temp.getJSONArray("region_gu");
                        for (int i = 0; i < tempArray.length(); i++) {
                            tempList.add(tempArray.getString(i));
                        }
                        memberDTO.setRegion_gu(tempList);
                    }
                    if (!temp.getString("like_board").equals("0")) {
                        List<String> tempList = new ArrayList<>();
                        JSONArray tempArray = temp.getJSONArray("like_board");
                        for (int i = 0; i < tempArray.length(); i++) {
                            tempList.add(tempArray.getString(i));
                        }
                        memberDTO.setLike_board(tempList);
                    }
                    if (!temp.getString("like_comment").equals("0")) {
                        List<String> tempList = new ArrayList<>();
                        JSONArray tempArray = temp.getJSONArray("like_comment");
                        for (int i = 0; i < tempArray.length(); i++) {
                            tempList.add(tempArray.getString(i));
                        }
                        memberDTO.setLike_comment(tempList);
                    }
                    if (!temp.getString("foret_id").equals("0")) {
                        List<String> tempList = new ArrayList<>();
                        JSONArray tempArray = temp.getJSONArray("foret_id");
                        for (int i = 0; i < tempArray.length(); i++) {
                            tempList.add(tempArray.getString(i));
                        }
                        memberDTO.setForet_id(tempList);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            LoginActivity loginActivity = (LoginActivity) LoginActivity.loginActivity;
            // 세션에 담아서 로그인 페이지로
            SessionManager sessionManager = new SessionManager(loginActivity);
            sessionManager.saveSession(memberDTO);

            // 데이터 셋팅 HERE ----------------
            // Toast.makeText(MainActivity.this, memberDTO.toString(),
            // Toast.LENGTH_SHORT).show();
//            drawer_text1.setText(memberDTO.getNickname());
//            drawer_text2.setText(memberDTO.getEmail());
//            drawer_text3.setText(String.valueOf(memberDTO.getId()));
//            drawer_text4.setText(memberDTO.getReg_date());
//            try {
//                // 사진이 있을 떄
//                intoImage(context, memberDTO.getPhoto(), R.id.drawer_profile);
//            } catch (Exception e) {
//                // 사진이 없을떄
//                intoImage(context, "", R.id.drawer_profile);
//            }
            // 파이어 베이스 로그인 상태 만들기
            updateuserActiveStatusOn();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(MainActivity2.this, "에러", Toast.LENGTH_SHORT).show();
            Log.e("[test]", "리스펀스 페일 진입");
        }
    }

    private void intoImage(Context context, String message, int profile) {
        ImageView iv = (ImageView) findViewById(profile);
        if (message == null) {
            Glide.with(context).load(R.drawable.icon4).into(iv);
        }
        Glide.with(context).load(message).fallback(R.drawable.icon2).into(iv);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateuserActiveStatusOff();
    }
}
