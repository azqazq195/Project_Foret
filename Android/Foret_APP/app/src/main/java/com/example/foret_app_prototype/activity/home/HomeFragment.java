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

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

//import com.example.foret_app_prototype.activity.foret.board.ListForetBoardActivity;

public class HomeFragment extends Fragment
        implements ViewPager.OnPageChangeListener, View.OnClickListener {
    MemberDTO memberDTO;

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
    List<HomeForetBoardDTO> homeForetBoardDTOList;
    ForetAdapter foretAdapter;
    Integer[] colors = null;
    ArgbEvaluator evaluator = new ArgbEvaluator();

    // 포레 게시판
    ForetBoardAdapter foretBoardAdapter;
    NewBoardFeedAdapter newBoardFeedAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = rootView.findViewById(R.id.home_toolbar);
        activity = (MainActivity) getActivity();

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);

        viewPager = rootView.findViewById(R.id.viewPager);
        button1 = rootView.findViewById(R.id.button1);
        textView_name = rootView.findViewById(R.id.textView_name);
        recyclerView1 = rootView.findViewById(R.id.recyclerView1);
        recyclerView3 = rootView.findViewById(R.id.recyclerView3);
        intent = activity.getIntent();
        searchFragment = new SearchFragment();
        homeFragment = new HomeFragment();
        homeForetDTOList = new ArrayList<>();
        homeForetBoardDTOList = new ArrayList<>();

        memberDTO = new MemberDTO();
        memberDTO.setId(1);
        memberDTO.setName("문성하");
        memberDTO.setNickname("zi젼성하");
        memberDTO.setPhoto("data:image/jpeg;base64,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");

        Log.d("[TEST]", "getHomeData() 실행");
        getMember(); // 회원 정보 가져오기
//        getHomeData();

        Log.d("[TEST]", "getHomeData() 종료");

        // 뷰페이저(포레)
        viewPager = rootView.findViewById(R.id.viewPager);
        viewPager.setOnPageChangeListener(this);


        // 버튼 이벤트
        button1.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getMember() {
        url = "http://34.72.240.24:8085/foret/search/member.do";
        client = new AsyncHttpClient();
        memberResponse = new MemberResponse();
        RequestParams params = new RequestParams();
        params.put("id", memberDTO.getId());
        client.post(url, params, memberResponse);
    }

    private void getHomeData() {
        url = "http://34.72.240.24:8085/foret/search/home.do";
        client = new AsyncHttpClient();

//        final int DEFAULT_TIME = 50*1000;
//        client.setConnectTimeout(DEFAULT_TIME);
//        client.setResponseTimeout(DEFAULT_TIME);
//        client.setTimeout(DEFAULT_TIME);
//        client.setResponseTimeout(DEFAULT_TIME);

        homeDataResponse = new HomeDataResponse();
        RequestParams params = new RequestParams();
//        params.put("id", memberDTO.getId());
        params.put("id", 1);
        client.post(url, params, homeDataResponse);
    }


    private void getViewPager() {
        foretAdapter = new ForetAdapter(activity, homeForetDTOList);
        viewPager.setAdapter(foretAdapter);
        viewPager.setPadding(130, 0, 130, 0);
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

    private void setView() {
        // 공지사항
//        foretBoardAdapter = new ForetBoardAdapter(getActivity(), homeForetBoardDTOList);
        foretBoardAdapter = new ForetBoardAdapter(getActivity(), homeForetDTO.getHomeForetBoardDTOList());
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView1.setAdapter(foretBoardAdapter);

//        foretBoardAdapter.setItems(homeForetBoardDTOList);
        foretBoardAdapter.setItems(homeForetDTO.getHomeForetBoardDTOList());

        // 새글 피드
        newBoardFeedAdapter = new NewBoardFeedAdapter(getActivity(), homeForetDTO.getHomeForetBoardDTOList());
//        newBoardFeedAdapter = new NewBoardFeedAdapter(getActivity(), homeForetBoardDTOList);
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView3.setAdapter(newBoardFeedAdapter);

//        newBoardFeedAdapter.setItems(homeForetBoardDTOList);
        newBoardFeedAdapter.setItems(homeForetDTO.getHomeForetBoardDTOList());
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

        homeForetDTOList.get(position).getHomeForetBoardDTOList();
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
                activity.getSupportFragmentManager().beginTransaction().remove(this).commit();
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
            getHomeData();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
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

                        JSONArray board = temp.getJSONArray("board");
                        homeForetBoardDTOList = new ArrayList<>();
                        if(board.length() > 0) {
                            for (int a=0; a<board.length(); a++) {
                                JSONObject temp2 = board.getJSONObject(a);
                                HomeForetBoardDTO[] homeForetBoardDTO = new HomeForetBoardDTO[board.length()];
                                homeForetBoardDTO[a] = gson.fromJson(temp2.toString(), HomeForetBoardDTO.class);
                                Log.d("[TEST]", "homeForetBoardDTO[a] => " + homeForetBoardDTO[a].getSubject());
                                homeForetBoardDTOList.add(homeForetBoardDTO[a]);
                                Log.d("[TEST]", "homeForetBoardDTOList.size() => " + homeForetBoardDTOList.size());
                                Log.d("[TEST]", "포문안 리스트 사이즈 => " + homeForetBoardDTOList.size());

                            }
                            homeForetDTO.setHomeForetBoardDTOList(homeForetBoardDTOList);
                            Log.d("[TEST]", "포문끝나고 setHomeForetBoardDTOList 저장된 사이즈 => " + homeForetDTO.getHomeForetBoardDTOList().size());
                            Log.d("[TEST]", "homeForetBoardDTOList.size() => " + homeForetBoardDTOList.size());
                        }
                        homeForetDTOList.add(homeForetDTO);
                        Log.d("[TEST]", "homeForetDTOList.add(homeForetDTO) 사이즈 => " + homeForetDTOList.size());
                        Log.d("[TEST]", "homeForetDTO.getHomeForetBoardDTOList().size() 사이즈 => " + homeForetDTO.getHomeForetBoardDTOList().size());
                        Log.d("[TEST]", "homeForetDTOList.get(i).getName() => " + homeForetDTOList.get(i).getName());
                        Log.d("[TEST]", "homeForetDTOList.get(i).getId() => " + homeForetDTOList.get(i).getId());
                    }
                    Log.d("[TEST]", "포레 게시판 리스트 가져옴");
                    Log.d("[TEST]", "homeForetDTOList.size() => " + homeForetDTOList.size());
                    Log.d("[TEST]", "homeForetDTO.getHomeForetBoardDTOList().size() 사이즈 => " + homeForetDTO.getHomeForetBoardDTOList().size());
                    Log.d("[TEST]", "homeForetDTOList.get(0) 사이즈 => " + homeForetDTOList.get(0).getHomeForetBoardDTOList().size());
                    Log.d("[TEST]", "homeForetDTOList.get(1) 사이즈 => " + homeForetDTOList.get(1).getHomeForetBoardDTOList().size());
                    Log.d("[TEST]", "homeForetDTOList.get(2) 사이즈 => " + homeForetDTOList.get(2).getHomeForetBoardDTOList().size());
//                    Log.d("[TEST]", "homeForetBoardDTOList.size() => " + homeForetBoardDTOList.size());
                } else {
                    Log.d("[TEST]", "포레 게시판 리스트 못가져오거나 가입한 포레가 없음");
                    // 가입한 포레가 없을시 홈에서 보여줄 기본 데이터
                    homeForetDTO = new HomeForetDTO();
                    homeForetDTO.setName("가입한 포레가 없습니다");
                    homeForetDTO.setPhoto("");
                    homeForetDTOList.add(homeForetDTO);
                    homeForetBoardDTO = new HomeForetBoardDTO();
                    homeForetBoardDTO.setSubject("For your study");
                    homeForetBoardDTO.setContent("Let's do it together");
                    homeForetBoardDTO.setReg_date("");
                    homeForetBoardDTOList.add(homeForetBoardDTO);
                    homeForetDTO.setHomeForetBoardDTOList(homeForetBoardDTOList);
                    Log.d("[TEST]", "homeForetDTOList.size() => " + homeForetDTOList.size());
                    Log.d("[TEST]", "homeForetDTO.getHomeForetBoardDTOList().size() => " + homeForetDTO.getHomeForetBoardDTOList().size());
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