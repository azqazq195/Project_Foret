package com.example.foret_app_prototype.activity.menu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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