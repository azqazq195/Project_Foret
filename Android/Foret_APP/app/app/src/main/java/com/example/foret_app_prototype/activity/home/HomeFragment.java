package com.example.foret_app_prototype.activity.home;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.activity.foret.ViewForetActivity;
//import com.example.foret_app_prototype.activity.foret.board.ListForetBoardActivity;
import com.example.foret_app_prototype.activity.foret.board.ReadForetBoardActivity;
import com.example.foret_app_prototype.activity.login.SessionManager;
import com.example.foret_app_prototype.activity.search.SearchFragment;
import com.example.foret_app_prototype.adapter.foret.ForetAdapter;
import com.example.foret_app_prototype.adapter.foret.ForetBoardAdapter;
import com.example.foret_app_prototype.adapter.foret.NewBoardFeedAdapter;
import com.example.foret_app_prototype.model.Foret;
import com.example.foret_app_prototype.model.ForetBoard;
import com.example.foret_app_prototype.model.Member;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment
        implements ViewPager.OnPageChangeListener, View.OnClickListener {
    Member member;
    Toolbar toolbar;
    MainActivity activity;
    TextView button1, textView_name;
    ImageView button2;
    RecyclerView recyclerView1, recyclerView2, recyclerView3;
    Intent intent;
    SearchFragment searchFragment;

    // 뷰페이저 (포레)
    ViewPager viewPager;
    List<Foret> foretList;
    Foret foret;
    ForetAdapter foretAdapter;
    Integer[] colors = null;
    ArgbEvaluator evaluator = new ArgbEvaluator();

    // 포레 게시판
    List<ForetBoard> foretBoardList;
    ForetBoard foretBoard;
    ForetBoardAdapter foretBoardAdapter;
    NewBoardFeedAdapter newBoardFeedAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = rootView.findViewById(R.id.home_toolbar);
        activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);

        addMember(); // 셋 멤버

        viewPager = rootView.findViewById(R.id.viewPager);
        button1 = rootView.findViewById(R.id.button1);
        button2 = rootView.findViewById(R.id.button2);
        textView_name = rootView.findViewById(R.id.textView_name);
        recyclerView1 = rootView.findViewById(R.id.recyclerView1);
        recyclerView2 = rootView.findViewById(R.id.recyclerView2);
        recyclerView3 = rootView.findViewById(R.id.recyclerView3);
        intent = activity.getIntent();
        searchFragment = new SearchFragment();

        // 뷰페이저(포레)
        foretList = new ArrayList<>();
        addForet();
        foretAdapter = new ForetAdapter(activity, foretList);

        viewPager = rootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(foretAdapter);
        viewPager.setPadding(130, 0, 130, 0);

        colors = new Integer[foretList.size()];
        Integer[] colors_temp = new Integer[foretList.size()];

        for(int i=0; i<colors_temp.length; i++) {
            colors_temp[i] = getResources().getColor(R.color.color+(i+1));
            colors[i] = colors_temp[i];
        }
        viewPager.setOnPageChangeListener(this);

        // 포레 게시판
        addForetBoard1();
        setView();

        // 버튼 이벤트
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 포레 클릭 이벤트
        foretAdapter.setOnClickListener(new ForetAdapter.OnClickListener() {
            @Override
            public void onClick(View v, Foret foret) {
                Intent intent = new Intent(activity, ViewForetActivity.class);
                intent.putExtra("member", member);
                intent.putExtra("foret", foret);
                intent.putExtra("foretList", (Serializable) foretList);
                intent.putExtra("foretBoardList", (Serializable) foretBoardList);
                startActivityForResult(intent, 888);
            }
        });
    }

    private void setView() {
        // 공지사항
        foretBoardAdapter = new ForetBoardAdapter(getActivity(), member, foretList, foretBoardList);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView1.setAdapter(foretBoardAdapter);
        // 일정
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setAdapter(foretBoardAdapter);
        // 새글 피드
        newBoardFeedAdapter = new NewBoardFeedAdapter(getActivity(), member, foretList, foretBoardList);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView3.setAdapter(newBoardFeedAdapter);

//        foretBoardAdapter.setItems(foretBoardList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == activity.RESULT_OK) {
            switch (requestCode) {
                case 999: // 포레 게시판 뷰
                    foretBoardList = (List<ForetBoard>) data.getSerializableExtra("foretBoardList");
                    setView();
                    break;
                case 888: // 포레
                    break;
                case 777: // 포레 게시판 리스트
                    break;
            }
        }
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
            Toast.makeText(activity, "사이드 바", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    // 뷰페이저 이벤트
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("[TEST]", "onPageScrolled 호출 : " + position);
        if (position < (foretAdapter.getCount() -1) && position < (colors.length - 1)) {
            viewPager.setBackgroundColor(
                    (Integer) evaluator.evaluate(
                            positionOffset,
                            colors[position],
                            colors[position + 1]
                    )
            );
        }
        else {
            viewPager.setBackgroundColor(colors[colors.length - 1]);
        }
        foret = foretList.get(position);
        textView_name.setText(foret.getName());
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("[TEST]", "onPageSelected 호출 : " + position);
        switch (position) {
            case 0:
                addForetBoard1();
                setView();
                break;
            case 1:
                addForetBoard2();
                setView();
                break;
            case 2:
                addForetBoard3();
                setView();
                break;
            case 3:
                addForetBoard4();
                setView();
                break;
            case 4:
                addForetBoard5();
                setView();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("[TEST]", "onPageScrollStateChanged 호출 : " + state);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1 : // 더많포레
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, searchFragment).commit();
                activity.getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
            case R.id.button2 : // 화살표
                intent = new Intent(activity, ListForetBoardActivity.class);
                intent.putExtra("member", member);
                intent.putExtra("foret", foret);
                intent.putExtra("foretBoardList", (Serializable) foretBoardList);
                startActivityForResult(intent, 777);
                break;
        }
    }


    //////////////////////////데이터 추가
    // 멤버
    private void addMember() {
        member = new Member();
        member.setId("test");
        member.setPw("1234");
        member.setName("테스터");
        member.setBirth("911111");
        member.setEmail("test@naver.com");
        member.setImage(R.drawable.iu);
    }
    // 포레
    private void addForet() {
        foretList = new ArrayList<>();
        String[] leader = {"문성하", "임선미", "전상만", "전성환", "이인제"};
        String[] member = {"문성하", "임선미", "전상만", "전성환", "이인제"};
        String[] region = {"서울시 ", "경기도 성남시 ", "강남구 ", "분당구 "};
        String[] foret_tag = {"#Java ", "#Spring ", "#Android ", "#SQL "};
        for(int a=0; a<5; a++) {
            Foret foret = new Foret();
            foret.setName("프로그래밍 스터디 포레 " + (a+1));
            foret.setIntroduce("프로그래밍 스터디 포레 " + (a+1) + " 소개");
            foret.setMax_member(10);
            foret.setReg_date("2020.11." + (a+1));
            foret.setForet_tag(foret_tag);
            foret.setMember(member);
            foret.setLeader(leader[a]);
            foret.setForet_region(region);
            foret.setForetImage(R.drawable.iu+(a+1));
            foretList.add(foret);
        }
    }

    // 포레 게시판
    private void addForetBoard1() {
        foretBoardList = new ArrayList<>();
        String[] board_photo = {"photo.jpg", "photo2.jpg", "photo3.jpg"};
        int b = 0;
        int c = 1;
        int d = 5;
        for(int a=0; a<5; a++) {
            ForetBoard foretBoard = new ForetBoard();
            foretBoard.setId(c);
            foretBoard.setWriter("문성하");
            foretBoard.setType(c);
            foretBoard.setHit(b);
            foretBoard.setSubject("포레 1 게시판 제목 " + (a+1));
            foretBoard.setContent("포레 1 게시판 내용 " + (a+1));
            foretBoard.setReg_date("2020.11.26 12:"+ (a+1));
            foretBoard.setEdit_date("2020.11.26 12:"+ (a+1));
            foretBoard.setMember_photo("iu.jpg");
            foretBoard.setBoard_photo(board_photo);
            foretBoard.setLike_count(b);
            foretBoard.setComment_count(d);
            foretBoard.setMemberImage(R.drawable.iu+(a+1));
            foretBoard.setBoradImage(R.drawable.iu+(6+a));
            foretBoardList.add(foretBoard);
            Log.d("[TEST]", "foretBoardList.size() => " + foretBoardList.size());
        }
    }

    // 포레 게시판
    private void addForetBoard2() {
        foretBoardList = new ArrayList<>();
        String[] board_photo = {"photo.jpg", "photo2.jpg", "photo3.jpg"};
        int b = 0;
        int c = 1;
        int d = 5;
        for(int a=0; a<5; a++) {
            ForetBoard foretBoard = new ForetBoard();
            foretBoard.setId(c);
            foretBoard.setWriter("임선미");
            foretBoard.setType(c);
            foretBoard.setHit(b);
            foretBoard.setSubject("포레 2 게시판 제목 " + (a+1));
            foretBoard.setContent("포레 2 게시판 내용 " + (a+1));
            foretBoard.setReg_date("2020.11.26 12:"+ (a+1));
            foretBoard.setEdit_date("2020.11.26 12:"+ (a+1));
            foretBoard.setMember_photo("iu.jpg");
            foretBoard.setBoard_photo(board_photo);
            foretBoard.setLike_count(b);
            foretBoard.setComment_count(d);
            foretBoard.setMemberImage(R.drawable.iu+(a+1));
            foretBoard.setBoradImage(R.drawable.iu+(6+a));
            foretBoardList.add(foretBoard);
            Log.d("[TEST]", "foretBoardList.size() => " + foretBoardList.size());
        }
    }

    // 포레 게시판
    private void addForetBoard3() {
        foretBoardList = new ArrayList<>();
        String[] board_photo = {"photo.jpg", "photo2.jpg", "photo3.jpg"};
        int b = 0;
        int c = 1;
        int d = 5;
        for(int a=0; a<5; a++) {
            ForetBoard foretBoard = new ForetBoard();
            foretBoard.setId(c);
            foretBoard.setWriter("전성환");
            foretBoard.setType(c);
            foretBoard.setHit(b);
            foretBoard.setSubject("포레 3 게시판 제목 " + (a+1));
            foretBoard.setContent("포레 3 게시판 내용 " + (a+1));
            foretBoard.setReg_date("2020.11.26 12:"+ (a+1));
            foretBoard.setEdit_date("2020.11.26 12:"+ (a+1));
            foretBoard.setMember_photo("iu.jpg");
            foretBoard.setBoard_photo(board_photo);
            foretBoard.setLike_count(b);
            foretBoard.setComment_count(d);
            foretBoard.setMemberImage(R.drawable.iu+(a+1));
            foretBoard.setBoradImage(R.drawable.iu+(6+a));
            foretBoardList.add(foretBoard);
            Log.d("[TEST]", "foretBoardList.size() => " + foretBoardList.size());
        }
    }

    // 포레 게시판
    private void addForetBoard4() {
        foretBoardList = new ArrayList<>();
        String[] board_photo = {"photo.jpg", "photo2.jpg", "photo3.jpg"};
        int b = 0;
        int c = 1;
        int d = 5;
        for(int a=0; a<5; a++) {
            ForetBoard foretBoard = new ForetBoard();
            foretBoard.setId(c);
            foretBoard.setWriter("이인제");
            foretBoard.setType(c);
            foretBoard.setHit(b);
            foretBoard.setSubject("포레 4 게시판 제목 " + (a+1));
            foretBoard.setContent("포레 4 게시판 내용 " + (a+1));
            foretBoard.setReg_date("2020.11.26 12:"+ (a+1));
            foretBoard.setEdit_date("2020.11.26 12:"+ (a+1));
            foretBoard.setMember_photo("iu.jpg");
            foretBoard.setBoard_photo(board_photo);
            foretBoard.setLike_count(b);
            foretBoard.setComment_count(d);
            foretBoard.setMemberImage(R.drawable.iu+(a+1));
            foretBoard.setBoradImage(R.drawable.iu+(6+a));
            foretBoardList.add(foretBoard);
            Log.d("[TEST]", "foretBoardList.size() => " + foretBoardList.size());
        }
    }

    // 포레 게시판
    private void addForetBoard5() {
        foretBoardList = new ArrayList<>();
        String[] board_photo = {"photo.jpg", "photo2.jpg", "photo3.jpg"};
        int b = 0;
        int c = 1;
        int d = 5;
        for(int a=0; a<5; a++) {
            ForetBoard foretBoard = new ForetBoard();
            foretBoard.setId(c);
            foretBoard.setWriter("이인제");
            foretBoard.setType(c);
            foretBoard.setHit(b);
            foretBoard.setSubject("포레 5 게시판 제목 " + (a+1));
            foretBoard.setContent("포레 5 게시판 내용 " + (a+1));
            foretBoard.setReg_date("2020.11.26 12:"+ (a+1));
            foretBoard.setEdit_date("2020.11.26 12:"+ (a+1));
            foretBoard.setMember_photo("iu.jpg");
            foretBoard.setBoard_photo(board_photo);
            foretBoard.setLike_count(b);
            foretBoard.setComment_count(d);
            foretBoard.setMemberImage(R.drawable.iu+(a+1));
            foretBoard.setBoradImage(R.drawable.iu+(6+a));
            foretBoardList.add(foretBoard);
            Log.d("[TEST]", "foretBoardList.size() => " + foretBoardList.size());
        }
    }


}