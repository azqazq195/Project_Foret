package com.example.foret_app_prototype.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

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
    String url = "http://192.168.55.172:8081/foret/search/member.do";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nav_bottom = findViewById(R.id.nav_bottom);
        container = findViewById(R.id.container);
        containerLayout = findViewById(R.id.containerLayout);
        homeFragment = new HomeFragment();
        freeFragment = new FreeFragment();
        searchFragment = new SearchFragment();
        chatFragment = new ChatFragment();
        notifyFragment = new NotifyFragment();
        nav_drawer = findViewById(R.id.nav_drawer);

        nav_bottom.setOnNavigationItemSelectedListener(this);
        nav_bottom.setItemIconTintList(null);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.containerLayout, homeFragment).commit();
        }

        client = new AsyncHttpClient();
        response = new HttpResponse();
        SessionManager sessionManager = new SessionManager(this);
        String email = sessionManager.getSessionEmail();
        String password = sessionManager.getSessionPassword();
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        client.post(url, params, response);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (container.isDrawerOpen(GravityCompat.END)) {
            container.closeDrawer(GravityCompat.END);
            return;
        }
        if(pressedTime == 0) {
            Toast.makeText(MainActivity.this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        } else {
            int seconds = (int)(System.currentTimeMillis() - pressedTime);
            if(seconds > 2000) {
                Toast.makeText(MainActivity.this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                pressedTime = 0;
            } else {
                super.onBackPressed();
                finish();
            }
        }
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
                    JSONArray member = json.getJSONArray("member");
                    JSONObject temp = member.getJSONObject(0);
                    memberDTO = gson.fromJson(temp.toString(), MemberDTO.class);


                    // 데이터 셋팅 HERE ----------------
                    Toast.makeText(MainActivity.this, memberDTO.toString(), Toast.LENGTH_SHORT).show();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(MainActivity.this, "에러", Toast.LENGTH_SHORT).show();
        }
    }

}