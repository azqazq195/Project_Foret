package com.example.foret_app_prototype.activity.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.activity.foret.MakeForetActivity;
import com.example.foret_app_prototype.adapter.RecyclerAdapter1;
import com.example.foret_app_prototype.adapter.RecyclerAdapter2;
import com.example.foret_app_prototype.adapter.RecyclerAdapter3;
import com.example.foret_app_prototype.model.Test;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements View.OnClickListener {

    Toolbar toolbar;
    MainActivity activity;
    LinearLayout layout_search;
    ImageView button_back;
    SearchView searchView;
    ImageView button1;
    Button button2, button3, button4, button5, button6, button7, button8;
    RecyclerView recyclerView1, recyclerView2;
    FloatingActionButton button9;
    List<Test> list1;
    List<Test> list2;
    RecyclerAdapter2 adapter2;
    RecyclerAdapter3 adapter3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        toolbar = (androidx.appcompat.widget.Toolbar) rootView.findViewById(R.id.search_toolbar);
        activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(null);
        setHasOptionsMenu(true);

        layout_search = rootView.findViewById(R.id.layout_search);
        button_back = rootView.findViewById(R.id.button_back);
        searchView = rootView.findViewById(R.id.searchView);
        button1 = rootView.findViewById(R.id.button1); //내 관심태그 설정 페이지로 이동(햄버거 메뉴에 넣을 예정)
        button2 = rootView.findViewById(R.id.button2); //인기태그1
        button3 = rootView.findViewById(R.id.button3); //인기태그2
        button4 = rootView.findViewById(R.id.button4); //인기태그3
        button5 = rootView.findViewById(R.id.button5); //인기태그4
        button6 = rootView.findViewById(R.id.button6); //인기태그5
        button7 = rootView.findViewById(R.id.button7); //포레 만들기 화면으로 이동
        button8 = rootView.findViewById(R.id.button8); //갱신
        button9 = rootView.findViewById(R.id.button9); //갱신
        recyclerView1 = rootView.findViewById(R.id.recyclerView1); //관심태그 목록
        recyclerView2 = rootView.findViewById(R.id.recyclerView2); //인기포레 목록

        recyclerView1.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));

        testData1();
        testData2();

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);

        button_back.setOnClickListener(this);
        return rootView;
    }

    private void testData2() {
        int resource[] = {R.drawable.icon, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4,
                R.drawable.icon5, R.drawable.icon, R.drawable.icon4, R.drawable.icon2, R.drawable.icon5};
        for(int a=0; a<resource.length; a++) {
            Test test = new Test();
            test.setResource(resource[a]);
            test.setTest1("포레 이름");
            test.setTest2("#태그1, #태그2, #태그3...");
            test.setTest3("여러분 화이팅 합시다 할수있어요!!");
            test.setTest4("20-12-02");
            test.setTest5("지역 : 성수동, 용산구");
            list2.add(test);
        }
        adapter3 = new RecyclerAdapter3(list2, activity);
        recyclerView2.setAdapter(adapter3);
    }

    private void testData1() {
        String testArray[] = {"디자인", "코딩", "영어", "다이어트", "조깅", "성수동"};
        for (int a=0; a<testArray.length; a++) {
            Test test = new Test();
            test.setTest1(testArray[a]);
            list1.add(test);
        }
        adapter2 = new RecyclerAdapter2(list1, activity);
        recyclerView1.setAdapter(adapter2);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_fragment_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_search :
                layout_search.setVisibility(View.VISIBLE);
                searchView.setQueryHint("이름/태그");
                break;
            case R.id.item_menu :
                Toast.makeText(activity, "햄버거 메뉴 나타남", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button1 : //내 관심 설정페이지로 이동
                Toast.makeText(activity, "내 관심 설정 페이지로 이동", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2 : //인기태그1 검색화면으로 이동
                intent = new Intent(activity, SearchResultActivity.class);
                intent.putExtra("tag", "공부");
                activity.startActivity(intent);
                break;
            case R.id.button3 : //인기태그2 검색화면으로 이동
                intent = new Intent(activity, SearchResultActivity.class);
                intent.putExtra("tag", "코딩");
                activity.startActivity(intent);
                break;
            case R.id.button4 : //인기태그3 검색화면으로 이동
                intent = new Intent(activity, SearchResultActivity.class);
                intent.putExtra("tag", "영어");
                activity.startActivity(intent);
                break;
            case R.id.button5 : //인기태그4 검색화면으로 이동
                intent = new Intent(activity, SearchResultActivity.class);
                intent.putExtra("tag", "아이패드");
                activity.startActivity(intent);
                break;
            case R.id.button6 : //인기태그5 검색화면으로 이동
                intent = new Intent(activity, SearchResultActivity.class);
                intent.putExtra("tag", "영어회화");
                activity.startActivity(intent);
                break;
            case R.id.button7 : //포레 만들기 화면으로 이동
                intent = new Intent(activity, MakeForetActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.button8 : //갱신버튼
                Toast.makeText(activity, "누르면 항목이 바껴요", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button9 : //포레 만들기 화면으로 이동
                intent = new Intent(activity, MakeForetActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.button_back :
                layout_search.setVisibility(View.GONE);
                break;
        }
    }
}