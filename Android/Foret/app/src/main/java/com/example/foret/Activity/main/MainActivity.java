package com.example.foret.Activity.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.foret.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView mBottomNV;
    DrawerLayout drawer;
    long pressedTime = 0;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.mainLayout);
        NavigationView navigationView = findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);

        mBottomNV = findViewById(R.id.nav_view);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                BottomNavigate(menuItem.getItemId());

                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.navigation_1);
    }

    private void BottomNavigate(int id) {  // BottomNavigation 페이지 변경
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        //테스트 라인 61번...
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {

            if (id == R.id.navigation_1) { // 메인 화면
                fragment = new MainFragment();
            } else if (id == R.id.navigation_2) {   // 익명 게시판 화면
                fragment = new com.example.foret.Activity.main.BoardFragment();
            } else if  (id == R.id.navigation_3) {  // 추천/검색 화면
                fragment = new SearchFragment();
            } else if  (id == R.id.navigation_4) {  // 채팅 화면
                fragment = new ChatFragment();
            } else if  (id == R.id.navigation_5) {  // 알림 화면
                fragment = new NoticeFragment();
            }

            fragmentTransaction.add(R.id.content_layout, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();


    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.mainLayout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
            return;
        }
        if(pressedTime == 0) {
            toast = Toast.makeText(MainActivity.this, "한 번 더 누르면 종료됩니다.",
                    Toast.LENGTH_SHORT);
            toast.show();
            pressedTime = System.currentTimeMillis();
        } else {
            int seconds = (int)(System.currentTimeMillis() - pressedTime);
            if(seconds > 2000) {
                toast = Toast.makeText(MainActivity.this, "한 번 더 누르면 종료됩니다.",
                        Toast.LENGTH_SHORT);
                toast.show();
                pressedTime = 0;
            } else {
                super.onBackPressed();
                toast.cancel();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the side_nav_icon_menu;
        // 수행 표시줄에 항목이 있는 경우 이 항목이 추가됨.
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 여기서 작업 표시줄 항목 클릭 처리
        // AndroidManifest.xml에서 상위 활동을 지정하는 경우 작업 표시줄에서 Home/Up 버튼 클릭을 자동으로 처리
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.nav :
                drawer = findViewById(R.id.mainLayout);
                drawer.openDrawer(GravityCompat.END);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // 여기에서 탐색 보기 항목 클릭 처리
        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.mainLayout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}