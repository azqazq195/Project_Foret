package com.example.foret_app_prototype.activity.free;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.adapter.RecyclerAdapter3;
import com.example.foret_app_prototype.adapter.RecyclerAdapter4;
import com.example.foret_app_prototype.model.Test;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FreeFragment extends Fragment implements View.OnClickListener {

    MainActivity activity;
    RecyclerView recyclerView2;
    TextView button1, button2, button3;
    FloatingActionButton button4;
    ImageView button_back;
    Toolbar toolbar;
    LinearLayout layout_search;
    SearchView searchView;
    ListView recyclerView1;
    RecyclerAdapter4 adapter4;
    List<Test> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_free, container, false);
        list = new ArrayList<>();
        toolbar = (androidx.appcompat.widget.Toolbar) rootView.findViewById(R.id.free_toolbar);
        activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(null);
        setHasOptionsMenu(true);

        layout_search = rootView.findViewById(R.id.layout_search);
        recyclerView1 = rootView.findViewById(R.id.recyclerView1);
        recyclerView2 = rootView.findViewById(R.id.recyclerView2);
//        searchView = rootView.findViewById(R.id.searchView);
        button1 = rootView.findViewById(R.id.button1);
        button2 = rootView.findViewById(R.id.button2);
        button3 = rootView.findViewById(R.id.button3);
        button4 = rootView.findViewById(R.id.button4);
        button_back = rootView.findViewById(R.id.button_back);

        //recyclerView1.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));

        testData();

        button1.setOnClickListener(this); //
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button_back.setOnClickListener(this);

        return rootView;
    }

    private void testData() {
        for(int a=0; a<10; a++) {
            Test test = new Test();
            test.setTest1("1202"+a);
            test.setTest2("익명게시판 글 제목");
            test.setTest3("91989202"+a);
            test.setTest4("우리 지각대장님들 화이팅이에여 힘냅시당 화이팅 화이팅");
            test.setTest5("공감(0)");
            test.setTest6("댓글(0)");
            test.setTest7("2020-12-02");
            list.add(test);
        }
        adapter4 = new RecyclerAdapter4(list, activity);
        recyclerView2.setAdapter(adapter4);
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
            case R.id.item_search : //검색 버튼 눌렀을때
                layout_search.setVisibility(View.VISIBLE);
                searchView.setQueryHint("제목/내용");
                button4.setVisibility(View.GONE);
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
            case R.id.button1 :
                button1.setTextColor(Color.BLACK);
                button1.setTypeface(Typeface.DEFAULT_BOLD);
                button2.setTypeface(null);
                button2.setTextColor(Color.GRAY);
                button3.setTypeface(null);
                button3.setTextColor(Color.GRAY);
                break;
            case R.id.button2 :
                button2.setTextColor(Color.BLACK);
                button2.setTypeface(Typeface.DEFAULT_BOLD);
                button1.setTypeface(null);
                button1.setTextColor(Color.GRAY);
                button3.setTypeface(null);
                button3.setTextColor(Color.GRAY);
                break;
            case R.id.button3 :
                button3.setTextColor(Color.BLACK);
                button3.setTypeface(Typeface.DEFAULT_BOLD);
                button2.setTypeface(null);
                button2.setTextColor(Color.GRAY);
                button1.setTypeface(null);
                button1.setTextColor(Color.GRAY);
                break;
            case R.id.button4 :
                intent = new Intent(activity, WriteFreeActivity.class);
                startActivity(intent);
                break;
            case R.id.button_back :
                layout_search.setVisibility(View.GONE);
                break;
        }
    }
}