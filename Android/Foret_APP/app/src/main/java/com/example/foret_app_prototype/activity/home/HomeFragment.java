package com.example.foret_app_prototype.activity.home;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.activity.foret.ViewForetActivity;
import com.example.foret_app_prototype.activity.foret.board.ReadForetBoardActivity;
import com.example.foret_app_prototype.activity.login.SessionManager;
import com.example.foret_app_prototype.activity.search.SearchFragment;
import com.example.foret_app_prototype.adapter.foret.ForetAdapter;
import com.example.foret_app_prototype.adapter.foret.ForetBoardAdapter;
import com.example.foret_app_prototype.adapter.foret.NewBoardFeedAdapter;
import com.example.foret_app_prototype.model.HomeForetBoardDTO;
import com.example.foret_app_prototype.model.HomeForetDTO;
import com.example.foret_app_prototype.model.MemberDTO;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

//import com.example.foret_app_prototype.activity.foret.board.ListForetBoardActivity;

public class HomeFragment extends Fragment
        implements ViewPager.OnPageChangeListener, View.OnClickListener {
    MemberDTO memberDTO;
    String id;

    Toolbar toolbar;
    MainActivity activity;
    TextView button1, textView_name;
    RecyclerView recyclerView1, recyclerView3;
    Intent intent;
    SearchFragment searchFragment;
    HomeFragment homeFragment;

    AsyncHttpClient client;
    MemberResponse memberResponse;     // member.do
    HomeDataResponse homeDataResponse; // home.do

    String url;

    // 뷰페이저 (포레)
    ViewPager viewPager;
    HomeForetDTO homeForetDTO;
    List<HomeForetDTO> homeForetDTOList;
    HomeForetBoardDTO homeForetBoardDTO;
    //    List<HomeForetBoardDTO> homeForetBoardDTOList;
    List<HomeForetBoardDTO> homeNoticeList;
    List<HomeForetBoardDTO> homeBoardList;
    ForetAdapter foretAdapter;
    Integer[] colors = null;
    ArgbEvaluator evaluator = new ArgbEvaluator();
    Context context;
    // 포레 게시판
    ForetBoardAdapter foretBoardAdapter;
    NewBoardFeedAdapter newBoardFeedAdapter;

     public HomeFragment() {
    }

    public HomeFragment(Context context) {
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = rootView.findViewById(R.id.home_toolbar);
        activity = (MainActivity) getActivity();
        id = activity.getId()+"";
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);

        viewPager = rootView.findViewById(R.id.viewPager);
        button1 = rootView.findViewById(R.id.button1);
        textView_name = rootView.findViewById(R.id.textView_name);
        recyclerView1 = rootView.findViewById(R.id.recyclerView1);
        recyclerView3 = rootView.findViewById(R.id.recyclerView3);
        intent = activity.getIntent();
        searchFragment = new SearchFragment(context);
        homeFragment = new HomeFragment(context);



//        id = 1;
//        memberDTO = new MemberDTO();
//        memberDTO.setId(1);
//        memberDTO.setName("문성하");
//        memberDTO.setNickname("zi젼성하");
//        memberDTO.setPhoto("asd");

        Log.d("[TEST]", "getHomeData() 종료");

        // 뷰페이저(포레)
        viewPager = rootView.findViewById(R.id.viewPager);
        viewPager.setOnPageChangeListener(this);

        // 버튼 이벤트
        button1.setOnClickListener(this);

        //여기로
        getMember(id); // 회원 정보 가져오기

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //getMember(id); // 회원 정보 가져오기
        //이거 움김

    }

    private void getMember(String id) {
        url = "http://34.72.240.24:8085/foret/search/member.do";
        client = new AsyncHttpClient();
        memberResponse = new MemberResponse();
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(url, params, memberResponse);
    }

    private void getHomeData() {
        url = "http://34.72.240.24:8085/foret/search/home.do";
        client = new AsyncHttpClient();

        final int DEFAULT_TIME = 50*1000;
        client.setConnectTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);
        client.setTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);

        homeDataResponse = new HomeDataResponse();
        RequestParams params = new RequestParams();
//        params.put("id", memberDTO.getId());
        params.put("id", id);
        client.post(url, params, homeDataResponse);
    }


    //메인 페이저 뷰
    private void getViewPager() {
        foretAdapter = new ForetAdapter(activity, homeForetDTOList);
        viewPager.setAdapter(foretAdapter);
        viewPager.setPadding(100, 0, 100, 0);
        colors = new Integer[homeForetDTOList.size()];
        Integer[] colors_temp = new Integer[homeForetDTOList.size()];

        for (int i=0; i<colors_temp.length; i++) {
            colors_temp[i] = getResources().getColor(R.color.color + (i + 1));
            colors[i] = colors_temp[i];
        }

        // 포레 클릭 이벤트
        foretAdapter.setOnClickListener(new ForetAdapter.OnClickListener() {
            @Override
            public void onClick(View v, HomeForetDTO homeForetDTO) {
                if(homeForetDTO.getId() > 0) {
                    intent = new Intent(activity, ViewForetActivity.class);
                    Log.d("[TEST]", "포레 클릭 homeForetDTO.getId() => " + homeForetDTO.getId());
                    intent.putExtra("foret_id", homeForetDTO.getId()); // 포레 아이디값 넘김
                    intent.putExtra("memberDTO", memberDTO);
                    startActivity(intent);
                } else {
                    activity.getSupportFragmentManager().beginTransaction().remove(homeFragment).commit();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, searchFragment).commit();
                }
            }
        });
    }


    //데이터 셋팅
    private void setView() {
        // 공지사항
        foretBoardAdapter = new ForetBoardAdapter(getActivity(), memberDTO, homeForetDTO.getHomeNoticeList());
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView1.setAdapter(foretBoardAdapter);

        foretBoardAdapter.setOnClickListener(new ForetBoardAdapter.OnClickListener() {
            @Override
            public void onClick(View v, HomeForetBoardDTO homeForetBoardDTO) {
                if(homeForetBoardDTO.getId() == 0) {
                    activity.getSupportFragmentManager().beginTransaction().remove(homeFragment).commit();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, searchFragment).commit();
                } else {
                    Intent intent = new Intent(getActivity(), ReadForetBoardActivity.class);
                    intent.putExtra("board_id", homeForetBoardDTO.getId());
                    intent.putExtra("memberDTO", memberDTO);
                    startActivity(intent);
                }
            }
        });

//        foretBoardAdapter.setItems(homeForetDTO.getHomeNoticeList());

        // 새글
        newBoardFeedAdapter = new NewBoardFeedAdapter(getActivity(), memberDTO, homeForetDTO.getHomeBoardList());
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView3.setAdapter(newBoardFeedAdapter);

        newBoardFeedAdapter.setOnClickListener(new NewBoardFeedAdapter.OnClickListener() {
            @Override
            public void onClick(View v, HomeForetBoardDTO homeForetBoardDTO) {
                if(homeForetBoardDTO.getId() == 0) {
                    activity.getSupportFragmentManager().beginTransaction().remove(homeFragment).commit();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, searchFragment).commit();
                } else {
                    Intent intent = new Intent(getActivity(), ReadForetBoardActivity.class);
                    intent.putExtra("board_id", homeForetBoardDTO.getId());
                    intent.putExtra("memberDTO", memberDTO);
                    startActivity(intent);
                }
            }
        });

//        newBoardFeedAdapter.setItems(homeForetDTO.getHomeBoardList());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.normal_toolbar2, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu) {
            DrawerLayout container = activity.findViewById(R.id.container);
            container.openDrawer(GravityCompat.END);
        }
        return super.onOptionsItemSelected(item);
    }

    // 뷰페이저 이벤트
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("[TEST]", "onPageScrolled 호출 : " + position);
        if (position < (foretAdapter.getCount() - 1) && position < (colors.length - 1)) {
            viewPager.setBackgroundColor(
                    (Integer) evaluator.evaluate(
                            positionOffset,
                            colors[position],
                            colors[position + 1]
                    )
            );
        } else {
            viewPager.setBackgroundColor(colors[colors.length - 1]);
        }

        homeForetDTO = homeForetDTOList.get(position);
        textView_name.setText(homeForetDTO.getName());

        homeForetDTOList.get(position).getHomeNoticeList();
        homeForetDTOList.get(position).getHomeBoardList();
        setView();
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("[TEST]", "onPageSelected 호출 : " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("[TEST]", "onPageScrollStateChanged 호출 : " + state);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1: // 더많포레 -> 서치로 이동
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, searchFragment).commit();

                //activity.getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
        }
    }

    class MemberResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            Log.d("[TEST]", "MemeberResponse onStart() 호출");
        }

        @Override
        public void onFinish() {
            super.onFinish();
            Log.d("[TEST]", "MemeberResponse onStart() 호출");

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            homeForetDTOList = new ArrayList<>();
//        homeForetBoardDTOList = new ArrayList<>();
            homeNoticeList = new ArrayList<>();
            homeBoardList = new ArrayList<>();
            String str = new String(responseBody);
            Gson gson = new Gson();
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if (RT.equals("OK")) {
                    JSONArray member = json.getJSONArray("member");
                    JSONObject temp = member.getJSONObject(0);
                    memberDTO = gson.fromJson(temp.toString(), MemberDTO.class);
                    Log.d("[TEST]", "회원정보 가져옴");
                    Log.d("[member]", "아디 가져옴 ==== " + memberDTO.getId());
                    Log.d("[member]", "닉넴 가져옴 ==== " + memberDTO.getNickname());
                    Log.d("[member]", "이름 가져옴 ==== " + memberDTO.getName());
                    Log.d("[member]", "생일 가져옴 ==== " + memberDTO.getBirth());
                    Log.d("[member]", "사진 가져옴 ==== " + memberDTO.getPhoto());
                    Log.d("[member]", "이멜 가져옴 ==== " + memberDTO.getEmail());
                    Log.d("[member]", "비번 가져옴 ==== " + memberDTO.getPassword());
                    Log.d("[member]", "가입일 가져옴 ==== " + memberDTO.getReg_date());

                    getHomeData();
                } else {
                    Log.d("[TEST]", "회원정보 못가져옴");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getActivity(), "MemeberResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }


    class HomeDataResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            Log.d("[TEST]", "HomeDataResponse onStart() 호출");
        }

        @Override
        public void onFinish() {
            Log.d("[TEST]", "HomeDataResponse onFinish() 호출");
            // 뷰페이져
            getViewPager();
            // 게시판
            setView();
            textView_name.setText(homeForetDTOList.get(0).getName());
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            Gson gson = new Gson();
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
//                int foretTotal = json.getInt("foretTotal");
                if (RT.equals("OK")) {
                    JSONArray foret = json.getJSONArray("foret");
                    for (int i=0; i<foret.length(); i++) {
                        JSONObject temp = foret.getJSONObject(i);
                        homeForetDTO = new HomeForetDTO();
                        homeForetDTO = gson.fromJson(temp.toString(), HomeForetDTO.class);

                        // 가입한 포레가 없을시 홈에서 보여줄 기본 데이터
                        if (homeForetDTO.getName() == null) {
                            homeForetDTO = new HomeForetDTO();
                            homeForetDTO.setId(0);
                            homeForetDTO.setName("가입한 포레가 없습니다");
                            homeForetDTO.setPhoto("https://lh3.googleusercontent.com/proxy/ENLBHF-rHAT6N6RhgkF0ybfvVNynwKS_YkuNTfEXk3zdgEHvkUTzHd0ueG08skKIZhjuxrZEZHSn3r_hubApTyNtuY9pZWF_Aot8rOIGAj2vT45l");
                            homeForetDTOList.add(homeForetDTO);
                            homeForetBoardDTO = new HomeForetBoardDTO();
                            homeForetBoardDTO.setSubject("Join or create Foret");
                            homeForetBoardDTO.setReg_date(" ");
                            homeForetBoardDTO.setType(2);
                            homeNoticeList.add(homeForetBoardDTO);
                            homeForetBoardDTO = new HomeForetBoardDTO();
                            homeForetBoardDTO.setSubject("For your study");
                            homeForetBoardDTO.setContent("Let's do it together");
                            homeForetBoardDTO.setReg_date(" ");
                            homeForetBoardDTO.setType(4);
                            homeBoardList.add(homeForetBoardDTO);
                            homeForetDTO.setHomeNoticeList(homeNoticeList);
                            homeForetDTO.setHomeBoardList(homeBoardList);
                            Log.d("[TEST]", "homeForetDTOList.size() => " + homeForetDTOList.size());
                            Log.d("[TEST]", "homeForetDTO.getHomeNoticeList().size() => " + homeForetDTO.getHomeNoticeList().size());
                            Log.d("[TEST]", "homeForetDTO.getHomeBoardList().size() => " + homeForetDTO.getHomeBoardList().size());
                            return;
                        }

                        JSONArray board = temp.getJSONArray("board");
                        homeNoticeList = new ArrayList<>();
                        homeBoardList = new ArrayList<>();
                        if(board.length() > 0) {
                            for (int a=0; a<board.length(); a++) {
                                JSONObject temp2 = board.getJSONObject(a);
                                HomeForetBoardDTO[] homeForetBoardDTO = new HomeForetBoardDTO[board.length()];
                                homeForetBoardDTO[a] = gson.fromJson(temp2.toString(), HomeForetBoardDTO.class);
                                Log.d("[TEST]", "homeForetBoardDTO[a] => " + homeForetBoardDTO[a].getType());
                                if(homeForetBoardDTO[a].getType() == 2) {
                                    homeNoticeList.add(homeForetBoardDTO[a]);
                                } else if(homeForetBoardDTO[a].getType() == 4) {
                                    homeBoardList.add(homeForetBoardDTO[a]);
                                }
                                Log.d("[TEST]", "homeNoticeList.size() => " + homeNoticeList.size());
                                Log.d("[TEST]", "homeBoardList.size() => " + homeBoardList.size());
                            }
                            homeForetDTO.setHomeNoticeList(homeNoticeList);
                            homeForetDTO.setHomeBoardList(homeBoardList);
                        }
                        homeForetDTOList.add(homeForetDTO);
                        Log.d("[TEST]", "최종 저장된 homeForetDTOList.add(homeForetDTO) 사이즈 => " + homeForetDTOList.size());
                        Log.d("[TEST]", "최종 저장된 homeNoticeList.size() 사이즈 => " + homeForetDTO.getHomeNoticeList().size());
                        Log.d("[TEST]", "최종 저장된 homeNoticeList.size() 사이즈 => " + homeForetDTO.getHomeBoardList().size());
                        Log.d("[TEST]", "homeForetDTOList.get(i).getId() => " + homeForetDTOList.get(i).getId());
                        Log.d("[TEST]", "homeForetDTOList.get(i).getName() => " + homeForetDTOList.get(i).getName());
                    }
                    Log.d("[TEST]", "포레 게시판 리스트 가져옴");
                    Log.d("[TEST]", "homeForetDTOList.size() => " + homeForetDTOList.size());
                    Log.d("[TEST]", "homeForetDTO.getHomeNoticeList().size() => " + homeForetDTO.getHomeNoticeList().size());
                    Log.d("[TEST]", "homeForetDTO.getHomeBoardList().size() => " + homeForetDTO.getHomeBoardList().size());
//                    Log.d("[TEST]", "homeForetBoardDTOList.size() => " + homeForetBoardDTOList.size());
                } else {
                    Log.d("[TEST]", "포레 게시판 리스트 가져오지못함");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getActivity(), "HomeDataResponse 통신 실패", Toast.LENGTH_SHORT).show();
            Log.d("[TEST]", "HomeDataResponse 통신 실패");
        }
    }

}