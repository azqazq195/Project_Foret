package com.example.foret_app_prototype.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.activity.foret.ViewForetActivity;
import com.example.foret_app_prototype.activity.foret.board.ListForetBoardActivity;
import com.example.foret_app_prototype.activity.search.SearchFragment;
import com.example.foret_app_prototype.adapter.RecyclerAdapter1;
import com.example.foret_app_prototype.adapter.RecyclerAdapter3;
import com.example.foret_app_prototype.adapter.RecyclerAdapter5;
import com.example.foret_app_prototype.model.Test;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    androidx.appcompat.widget.Toolbar toolbar;
    //ViewPager viewPager;
    MainActivity activity;
    TextView button1, textView_name;
    ImageView button2;
    ListView listView1, listView2;
    RecyclerView recyclerView, testRecycle; ///
    RecyclerAdapter1 adapter1; ///
    RecyclerAdapter5 adapter5; ///
    Intent intent;
    SearchFragment searchFragment;
    List<Test> list1; ///
    List<Test> list2; ///

    //가벼운 동작 테스트만을 위해 뷰페이저를 빼버렸습니다.

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = (androidx.appcompat.widget.Toolbar) rootView.findViewById(R.id.home_toolbar);
        activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);

        testRecycle = rootView.findViewById(R.id.testRecycle);
        button1 = rootView.findViewById(R.id.button1);
        button2 = rootView.findViewById(R.id.button2);
        textView_name = rootView.findViewById(R.id.textView_name);
        listView1 = rootView.findViewById(R.id.listView1);
        listView2 = rootView.findViewById(R.id.listView2);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        intent = activity.getIntent();
        searchFragment = new SearchFragment();
        list1 = new ArrayList<>(); ///
        list2 = new ArrayList<>(); ///
        testRecycle.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        testData1(); ///
        testData2();

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView1ItemClick(parent, view, position, id);
                //Intent intent = new Intent(activity, )
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView2ItemClick(parent, view, position, id);
            }
        });

        return rootView;
    }

    private void testData2() {
        int testArry[] = {R.drawable.icon, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5};
        for (int a=0; a<testArry.length; a++) {
            Test test = new Test();
            test.setResource(testArry[a]);
            test.setTest1("[일반]");
            test.setTest2("새 글 제목");
            test.setTest3("글 내용이 들어가는데 글 내용이 들어가는데");
            test.setTest4("20-12-02");
            list2.add(test);
        }
        adapter5 = new RecyclerAdapter5(activity, list2);
        recyclerView.setAdapter(adapter5);
    }

    private void testData1() {
        int testArry[] = {R.drawable.icon, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5};
        for (int a=0; a<testArry.length; a++) {
            Test test = new Test();
            test.setResource(testArry[a]);
            list1.add(test);
        }
        adapter1 = new RecyclerAdapter1(list1, activity);
        testRecycle.setAdapter(adapter1);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.normal_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu) {
            Toast.makeText(getActivity(), "테스트", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //공지사항 리스트뷰 이벤트
    private void listView1ItemClick(AdapterView<?> parent, View view, int position, long id) {
        intent = new Intent(activity, ListForetBoardActivity.class);
        startActivity(intent);
    }

    //일정 리스트뷰 이벤트
    private void listView2ItemClick(AdapterView<?> parent, View view, int position, long id) {
        intent = new Intent(activity, ListForetBoardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1 :
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, searchFragment).commit();
                activity.getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
            case R.id.button2 :
                intent = new Intent(activity, ViewForetActivity.class);
                startActivity(intent);
                break;
        }
    }
}