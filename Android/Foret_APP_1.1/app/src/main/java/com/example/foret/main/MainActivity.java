package com.example.foret.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foret.R;
import com.example.foret.login.SessionManager;
import com.example.foret.model.MemberDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.bumptech.glide.Glide;
import com.example.foret.login.LoginActivity;
import com.example.foret.login.SessionManager;
import com.example.foret.model.MemberDTO;
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
import cz.msebera.android.httpclient.HttpResponse;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    SessionManager sessionManager;
    FirebaseAuth mAuth;
    AsyncHttpClient client;
    HttpResponse response;

    MemberDTO memberDTO;

    DrawerLayout container;
    NavigationView nav_drawer;

    String url = "http://54.180.219.200:8085/get/member";

    HomeFragment homeFragment;
    FreeFragment freeFragment;
    SearchFragment searchFragment;
    ChatFragment chatFragment;
    NotifyFragment notifyFragment;

    BottomNavigationView nav_bottom;

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

        viewSetting();

        client = new AsyncHttpClient();
        response = new HttpResponse();
        sessionManager = new SessionManager(this);
        RequestParams params = new RequestParams();
        params.put("id", sessionManager.getSession());
        client.post(url, params, response);

        homeFragment = new HomeFragment(this);
        freeFragment = new FreeFragment();
        searchFragment = new SearchFragment();
        chatFragment = new ChatFragment();
        notifyFragment = new NotifyFragment();

        nav_bottom = findViewById(R.id.nav_bottom);

        nav_bottom.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RequestParams params = new RequestParams();
        params.put("id", sessionManager.getSession());
        client.post(url, params, response);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateuserActiveStatusOff();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { // 네비게이션 드로어 메뉴, 바텀네비게이션 메뉴 둘다 이벤트 처리
        switch (item.getItemId()) {
            case R.id.navigation_home:
                Log.e("[BOTTOM_NAV]", "clicked : homeFragment");
                // Bundle bundle = new Bundle();
                // bundle.putSerializable("memberDTO", memberDTO);
                // homeFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, homeFragment).commit();
                break;
            case R.id.navigation_freeboard:
                Log.e("[BOTTOM_NAV]", "clicked : freeFragment");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, freeFragment).commit();
                break;
            case R.id.navigation_search:
                Log.e("[BOTTOM_NAV]", "clicked : searchFragment");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, searchFragment).commit();
                break;
            case R.id.navigation_chat:
                Log.e("[BOTTOM_NAV]", "clicked : chatFragment");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, chatFragment).commit();
                break;
            case R.id.navigation_notify:
                Log.e("[BOTTOM_NAV]", "clicked : notifyFragment");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, notifyFragment).commit();
                break;
            // case R.id.drawer_notice: // 햄버거 공지사항 버튼
            // intent = new Intent(this, AppNoticeActivity.class);
            // startActivity(intent);
            // break;
            // case R.id.drawer_myinfo: // 햄버거 내정보
            // intent = new Intent(this, MyInfoActivity.class);
            // intent.putExtra("memberDTO", memberDTO);
            // startActivity(intent);
            // break;
            // case R.id.drawer_help: // 햄버거 도움말
            // // Toast.makeText(this, "레이아웃으로 도움말 설명할거 레이아웃 5장",
            // Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(this, AppGuideActivity.class));
            // break;
            // case R.id.drawer_foret: // 햄버거 foret ->그냥 넣어놈
            // break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_out: // 로그아웃
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
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // 서버요청처리
    class HttpResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.e("[MainActivity]", "onSuccess");
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

                    textView1.setText(String.valueOf(memberDTO.getId()));
                    textView2.setText(memberDTO.getName());
                    textView3.setText(memberDTO.getEmail());
                    textView4.setText(memberDTO.getPassword());
                    textView5.setText(memberDTO.getNickname());
                    textView6.setText(memberDTO.getBirth());
                    textView7.setText(memberDTO.getReg_date());
                    textView8.setText(memberDTO.getDeviceToken());

                    Log.e("[SUNMI]", memberDTO.getNickname());
                    // 햄버거 메뉴 데이터 세팅
                    setNavigationView(memberDTO);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 파이어 베이스 로그인 상태 만들기
            updateuserActiveStatusOn();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(MainActivity.this, "에러", Toast.LENGTH_SHORT).show();
            Log.e("[MainActivity]", "onFailure");
        }
    }

    private void setNavigationView(MemberDTO memberDTO) {
        View nav_header = nav_drawer.getHeaderView(0);
        TextView button_out = (TextView) nav_header.findViewById(R.id.button_out);
        TextView drawer_text1 = (TextView) nav_header.findViewById(R.id.drawer_text1);
        TextView drawer_text2 = (TextView) nav_header.findViewById(R.id.drawer_text2);
        TextView drawer_text3 = (TextView) nav_header.findViewById(R.id.drawer_text3);
        TextView drawer_text4 = (TextView) nav_header.findViewById(R.id.drawer_text4);
        CircleImageView drawer_profile = (CircleImageView) nav_header.findViewById(R.id.drawer_profile);

        drawer_text1.setText(memberDTO.getNickname());
        drawer_text2.setText(memberDTO.getEmail());
        drawer_text3.setText("member ID : " + memberDTO.getId());
        drawer_text4.setText("가입일 : " + memberDTO.getReg_date());
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

}