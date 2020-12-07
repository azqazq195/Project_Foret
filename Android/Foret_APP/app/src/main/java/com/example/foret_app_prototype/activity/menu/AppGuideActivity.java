package com.example.foret_app_prototype.activity.menu;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.adapter.AdapterImageForGuide;

import java.util.ArrayList;
import java.util.List;

public class AppGuideActivity extends AppCompatActivity {

    ViewPager viewPager;
    AdapterImageForGuide adapterImageForGuide;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);

        // 상태바 색상 변경
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.foret4));

        list = new ArrayList<>();
        viewPager = findViewById(R.id.viewPager);
        adapterImageForGuide = new AdapterImageForGuide(this,list);
        viewPager.setAdapter(adapterImageForGuide);

        addData();

        adapterImageForGuide.notifyDataSetChanged();
    }

    private void addData() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
    }
}