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
import com.example.foret_app_prototype.activity.login.SessionManager;
import com.example.foret_app_prototype.activity.search.SearchFragment;
import com.example.foret_app_prototype.adapter.foret.ForetAdapter;
import com.example.foret_app_prototype.adapter.foret.ForetBoardAdapter;
import com.example.foret_app_prototype.adapter.foret.NewBoardFeedAdapter;
import com.example.foret_app_prototype.model.ForetBoardDTO;
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
    ForetBoardDTO foretBoardDTO;
    List<ForetBoardDTO> foretBoardDTOList;
    ForetBoardAdapter foretBoardAdapter;
    NewBoardFeedAdapter newBoardFeedAdapter;

//    String[] leader = {"문성하", "임선미", "전상만", "전성환", "이인제"};
//    String[] member = {"문성하", "임선미", "전상만", "전성환", "이인제"};
//    String[] region_si = {"서울시", "성남시", "서울시", "서울시", "서울시"};
//    String[] region_gu = {"관악구", "분당구", "강남구 ", "구로구", "성동구"};
//    String[] foret_tag = {"#Java ", "#Spring ", "#Android ", "#SQL ", "#영어"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = rootView.findViewById(R.id.home_toolbar);
        activity = (MainActivity) getActivity();

//        addForet();
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

        Log.d("[TEST]", "getHomeData() 실행");
//        getMember(); // 회원 정보 가져오기
        getHomeData();
        Log.d("[TEST]", "getHomeData() 종료");

        // 뷰페이저(포레)
        foretAdapter = new ForetAdapter(activity, homeForetDTOList);

        viewPager = rootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(foretAdapter);
        viewPager.setPadding(130, 0, 130, 0);

        colors = new Integer[homeForetDTOList.size()];
        Integer[] colors_temp = new Integer[homeForetDTOList.size()];

        for (int i=0; i<colors_temp.length; i++) {
            colors_temp[i] = getResources().getColor(R.color.color + (i + 1));
            colors[i] = colors_temp[i];
        }
        viewPager.setOnPageChangeListener(this);

        // 포레 게시판
        setView();

        // 버튼 이벤트
        button1.setOnClickListener(this);

        return rootView;
    }



//    private void addForet() {
//        foretDTOList = new ArrayList<>();
//
//        List<String> member_list = new ArrayList<>();
//        List<String> si_list = new ArrayList<>();
//        List<String> gu_list = new ArrayList<>();
//        List<String> tag_list = new ArrayList<>();
//
//        for (int i = 0; i < leader.length; i++) {
//            member_list.add(member[i]);
//            si_list.add(region_si[i]);
//            gu_list.add(region_gu[i]);
//            tag_list.add(foret_tag[i]);
//        }
//        for (int a = 0; a < 5; a++) {
//            ForetDTO foretDTO = new ForetDTO();
//            foretDTO.setForet_name("프로그래밍 스터디 포레 " + (a + 1));
//            foretDTO.setIntroduce("프로그래밍 스터디 포레 " + (a + 1) + " 소개");
//            foretDTO.setMax_member(10);
//            foretDTO.setReg_date("2020.11." + (a + 1));
//            foretDTO.setForet_tag(tag_list);
//            foretDTO.setForet_member(member_list);
//            foretDTO.setForet_leader(leader[a]);
//            foretDTO.setForet_region_si(si_list);
//            foretDTO.setForet_region_gu(gu_list);
//            foretDTO.setForet_photo("https://www.google.com/search?q=%EC%95%84%EC%9D%B4%EC%9C%A0&source=lnms&tbm=isch&sa=X&ved=2ahUKEwiOw7Xnj7TtAhXL62EKHasdArIQ_AUoAXoECAUQAw&biw=1920&bih=937#imgrc=1dKx6vBOwCxVWM");
//            foretDTOList.add(foretDTO);
//        }
//    }

    private void getMember() {
        url = "http://34.72.240.24:8085/foret/search/member.do";
        client = new AsyncHttpClient();
        memberResponse = new MemberResponse();
        RequestParams params = new RequestParams();
        SessionManager sessionManager = new SessionManager(getContext());

        String id = ""+sessionManager.getSession();
        //params.put("id", id);
        //params.put("id", memberDTO.getId());
        params.put("id", "1");
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
        params.put("id", 1);
        client.post(url, params, homeDataResponse);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 포레 클릭 이벤트
        foretAdapter.setOnClickListener(new ForetAdapter.OnClickListener() {
            @Override
            public void onClick(View v, HomeForetDTO homeForetDTO) {
                if(homeForetDTO.getId() > 0) {
                    Intent intent = new Intent(activity, ViewForetActivity.class);
                    intent.putExtra("foret_id", homeForetDTO.getId()); // 포레 아이디값 넘김
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
        foretBoardAdapter = new ForetBoardAdapter(getActivity(), homeForetBoardDTOList);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView1.setAdapter(foretBoardAdapter);

        // 새글 피드
        newBoardFeedAdapter = new NewBoardFeedAdapter(getActivity(), homeForetBoardDTOList);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView3.setAdapter(newBoardFeedAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.normal_toolbar, menu);
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
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("[TEST]", "onPageSelected 호출 : " + position);
        int page_position = position;
        switch (position) {
            case 0:
//                getViewChange(page_position);
                break;
            case 1:
//                getViewChange(page_position);
                break;
            case 2:
//                getViewChange(page_position);
                break;
            case 3:
//                getViewChange(page_position);
                break;
            case 4:
//                getViewChange(page_position);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("[TEST]", "onPageScrollStateChanged 호출 : " + state);
    }

    // 뷰페이져뷰 이동시 포레 게시판 내용 변환
//    private void getViewChange(int page_position) {
//        url = "http://34.72.240.24:8085/search/homeFragement.do";
//        client = new AsyncHttpClient();
//        homeDataResponse = new HomeDataResponse();
//        RequestParams params = new RequestParams();
//        params.put("id", page_position + 1);
//        client.post(url, params, foretBoardResponse);
//        setView();
//    }

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
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            homeForetDTOList = new ArrayList<>();
            homeForetBoardDTOList = new ArrayList<>();
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                HomeForetDTO.setRT(json.getString("RT"));
                HomeForetDTO.setForetTotal(json.getInt("foretTotal"));
                if (HomeForetDTO.getRT().equals("OK")) {
                    JSONArray foret = json.getJSONArray("foret");
                    for (int i=0; i<foret.length(); i++) {
                        JSONObject temp = foret.getJSONObject(i);
                        homeForetDTO = new HomeForetDTO();
                        homeForetDTO.setName(temp.getString("name"));
                        homeForetDTO.setPhoto(temp.getString("photo"));
                        homeForetDTO.setId(temp.getInt("id"));

                        JSONArray board = json.getJSONArray("board");
                        for(int a=0; a>board.length(); a++) {
                            JSONObject temp2 = board.getJSONObject(i);
                            homeForetBoardDTO = new HomeForetBoardDTO();
                            homeForetBoardDTO.setReg_date(temp2.getString("reg_date"));
                            homeForetBoardDTO.setSubject(temp2.getString("subject"));
                            homeForetBoardDTO.setEdit_date(temp2.getString("edit_date"));
                            homeForetBoardDTO.setContent(temp2.getString("content"));
                            homeForetBoardDTO.setHit(temp2.getInt("hit"));
                            homeForetBoardDTO.setBoard_like(temp2.getInt("board_like"));
                            homeForetBoardDTO.setBoard_comment(temp2.getInt("board_comment"));
                            homeForetBoardDTO.setId(temp2.getInt("id"));
                            homeForetBoardDTO.setWriter(temp2.getInt("writer"));
                            homeForetBoardDTO.setType(temp2.getInt("type"));

                            homeForetBoardDTOList.add(homeForetBoardDTO);
                        }
                        homeForetDTOList.add(homeForetDTO);
                    }
                    Log.d("[TEST]", "포레 게시판 리스트 가져옴");
                    Log.d("[TEST]", "homeForetDTOList.size() => " + homeForetDTOList.size());
                    Log.d("[TEST]", "homeForetBoardDTOList.size() => " + homeForetBoardDTOList.size());
                } else {
                    Log.d("[TEST]", "포레 게시판 리스트 못가져오거나 가입한 포레가 없음");
                    // 가입한 포레가 없을시 홈에서 보여줄 기본 데이터
                    homeForetDTO = new HomeForetDTO();
                    homeForetDTO.setName("가입한 포레가 없습니다.");
                    homeForetDTO.setPhoto("");
                    homeForetDTOList.add(homeForetDTO);
                    homeForetBoardDTO = new HomeForetBoardDTO();
                    homeForetBoardDTO.setContent("내게 맞는 포레를 찾아보세요.");
                    homeForetBoardDTO.setReg_date("");
                    homeForetBoardDTOList.add(homeForetBoardDTO);
                    Log.d("[TEST]", "homeForetDTOList.size() => " + homeForetDTOList.size());
                    Log.d("[TEST]", "foretBoardDTOList.size() => " + homeForetBoardDTOList.size());
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

//    class HomeDataResponse extends AsyncHttpResponseHandler {
//        @Override
//        public void onStart() {
//            Log.d("[TEST]", "HomeDataResponse onStart() 호출");
//        }
//
//        @Override
//        public void onFinish() {
//            Log.d("[TEST]", "HomeDataResponse onFinish() 호출");
//            homeForetBoardDTOList = new ArrayList<>();
//            for(int a=0; a<homeForetDTO.getBoard().size(); a++) {
//                homeForetBoardDTOList.add(homeForetDTO.getBoard().get(a));
//            }
//
//        }
//
//        @Override
//        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//            homeForetDTOList = new ArrayList<>();
//
//            String str = new String(responseBody);
//            Gson gson = new Gson();
//            try {
//                JSONObject json = new JSONObject(str);
//                String RT = json.getString("RT");
//                int foretTotal = json.getInt("foretTotal");
//                if (RT.equals("OK")) {
//                    JSONArray foret = json.getJSONArray("foret");
//                    JSONObject temp = foret.getJSONObject(0);
//                    for (int i=0; i<foret.length(); i++) {
//                        homeForetDTO = gson.fromJson(temp.toString(), HomeForetDTO.class);
//                        homeForetDTOList.add(homeForetDTO);
//                    }
//                    Log.d("[TEST]", "포레 게시판 리스트 가져옴");
//                    Log.d("[TEST]", "homeForetDTOList.size() => " + homeForetDTOList.size());
//                    Log.d("[TEST]", "homeForetBoardDTOList.size() => " + homeForetDTO.getBoard().size());
//                } else {
//                    Log.d("[TEST]", "포레 게시판 리스트 못가져오거나 가입한 포레가 없음");
//                    // 가입한 포레가 없을시 홈에서 보여줄 기본 데이터
//                    homeForetDTO.setName("가입한 포레가 없습니다.");
//                    homeForetDTO.setPhoto("");
//                    homeForetDTOList.add(homeForetDTO);
//                    homeForetBoardDTO.setContent("내게 맞는 포레를 찾아보세요.");
//                    homeForetBoardDTO.setReg_date("");
//                    homeForetBoardDTOList.add(homeForetBoardDTO);
//                    Log.d("[TEST]", "homeForetDTOList.size() => " + homeForetDTOList.size());
//                    Log.d("[TEST]", "foretBoardDTOList.size() => " + homeForetBoardDTOList.size());
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//            Toast.makeText(getActivity(), "HomeDataResponse 통신 실패", Toast.LENGTH_SHORT).show();
//            Log.d("[TEST]", "HomeDataResponse 통신 실패");
//        }
//    }

}