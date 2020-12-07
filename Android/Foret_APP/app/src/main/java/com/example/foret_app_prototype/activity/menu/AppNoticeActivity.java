package com.example.foret_app_prototype.activity.menu;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.adapter.notification.AdapterNotice;
import com.example.foret_app_prototype.model.ModelNotice;

import java.util.ArrayList;
import java.util.List;

public class AppNoticeActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    List<ModelNotice> list;
    AdapterNotice adapterNotice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_notice);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listView);
        list = new ArrayList<>();
        adapterNotice = new AdapterNotice(this,R.layout.item_row_notice,list);

        listView.setAdapter(adapterNotice);

        addDate();


    }

    private void addDate() {
        list.add(new ModelNotice("관리자 | ","2020-12-01","[공지] 포레1.0 버전 안내","새롭게 런칭한 포레 1.0 버전 안내드립니다."));
        list.add(new ModelNotice("관리자 | ","2020-12-05","[이벤트] 친구 추천 이벤트","내가 만든 포레에 더 많은 회원을 모집해보세요"));
        list.add(new ModelNotice("관리자 | ","2020-12-06","[안내] 오류 및 버그 안내","버그 및 오류로 인한 불편사항은 관리자에게 문의바랍니다."));
        list.add(new ModelNotice("관리자 | ","2020-12-09","[공지] 12월 25일 휴무 안내","12월 25일 성탄절 서비스 휴무 안내드립니다."));
    }

    @Override //메뉴 이벤트 처리
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}