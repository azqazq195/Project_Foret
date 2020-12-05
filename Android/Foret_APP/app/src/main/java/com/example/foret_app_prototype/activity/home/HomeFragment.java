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
import android.widget.ImageView;
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
import com.example.foret_app_prototype.activity.foret.board.ListForetBoardActivity;
import com.example.foret_app_prototype.activity.login.SessionManager;
import com.example.foret_app_prototype.activity.search.SearchFragment;
import com.example.foret_app_prototype.adapter.foret.ForetAdapter;
import com.example.foret_app_prototype.adapter.foret.ForetBoardAdapter;
import com.example.foret_app_prototype.adapter.foret.NewBoardFeedAdapter;
import com.example.foret_app_prototype.model.ForetBoardDTO;
import com.example.foret_app_prototype.model.ForetDTO;
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
    ImageView button2;
    RecyclerView recyclerView1, recyclerView2, recyclerView3;
    Intent intent;
    SearchFragment searchFragment;

    AsyncHttpClient client;
    MainFragmentResponse mainFragmentResponse;
    ForetResponse foretResponse;
    ForetBoardResponse foretBoardResponse;

    String url = null;
//    String url = "http://34.72.240.24:8085/foret/search/member.do";  // 멤버 데이터 가져오기
//    String url = "http://34.72.240.24:8085/search/foretList.do";     // 포레 리스트 가져오기 *필요
//    String url = "http://34.72.240.24:8085/search/homeFragement.do"; // 포레 게시판리스트 가져오기

    // 뷰페이저 (포레)
    ViewPager viewPager;
    List<ForetDTO> foretDTOList;
    ForetDTO foretDTO;
    ForetAdapter foretAdapter;
    Integer[] colors = null;
    ArgbEvaluator evaluator = new ArgbEvaluator();

    // 포레 게시판
    ForetBoardDTO foretBoardDTO;
    List<ForetBoardDTO> foretBoardDTOList;
    ForetBoardAdapter foretBoardAdapter;
    NewBoardFeedAdapter newBoardFeedAdapter;


    String[] leader = {"문성하", "임선미", "전상만", "전성환", "이인제"};
    String[] member = {"문성하", "임선미", "전상만", "전성환", "이인제"};
    String[] region_si = {"서울시", "성남시", "서울시", "서울시", "서울시"};
    String[] region_gu = {"관악구", "분당구", "강남구 ", "구로구", "성동구"};
    String[] foret_tag = {"#Java ", "#Spring ", "#Android ", "#SQL ", "#영어"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = rootView.findViewById(R.id.home_toolbar);
        activity = (MainActivity) getActivity();

        addForet();

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);

        viewPager = rootView.findViewById(R.id.viewPager);
        button1 = rootView.findViewById(R.id.button1);
        //button2 = rootView.findViewById(R.id.button2);
        textView_name = rootView.findViewById(R.id.textView_name);
        recyclerView1 = rootView.findViewById(R.id.recyclerView1);
        recyclerView2 = rootView.findViewById(R.id.recyclerView2);
        recyclerView3 = rootView.findViewById(R.id.recyclerView3);
        intent = activity.getIntent();
        searchFragment = new SearchFragment();

        getMember(); // 회원 정보 가져오기

        // 뷰페이저(포레)
//        foretDTOList = new ArrayList<>();
        // 포레추가할 코드 <-
        foretAdapter = new ForetAdapter(activity, foretDTOList);

        viewPager = rootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(foretAdapter);
        viewPager.setPadding(130, 0, 130, 0);

        colors = new Integer[foretDTOList.size()];
        Integer[] colors_temp = new Integer[foretDTOList.size()];

        for (int i = 0; i < colors_temp.length; i++) {
            colors_temp[i] = getResources().getColor(R.color.color + (i + 1));
            colors[i] = colors_temp[i];
        }
        viewPager.setOnPageChangeListener(this);

        // 포레 게시판
        setView();

        // 버튼 이벤트
        button1.setOnClickListener(this);
        //button2.setOnClickListener(this);

        return rootView;
    }


    private void addForet() {
        foretDTOList = new ArrayList<>();

        List<String> member_list = new ArrayList<>();
        List<String> si_list = new ArrayList<>();
        List<String> gu_list = new ArrayList<>();
        List<String> tag_list = new ArrayList<>();

        for (int i = 0; i < leader.length; i++) {
            member_list.add(member[i]);
            si_list.add(region_si[i]);
            gu_list.add(region_gu[i]);
            tag_list.add(foret_tag[i]);
        }
        for (int a = 0; a < 5; a++) {
            ForetDTO foretDTO = new ForetDTO();
            foretDTO.setForet_name("프로그래밍 스터디 포레 " + (a + 1));
            foretDTO.setIntroduce("프로그래밍 스터디 포레 " + (a + 1) + " 소개");
            foretDTO.setMax_member(10);
            foretDTO.setReg_date("2020.11." + (a + 1));
            foretDTO.setForet_tag(tag_list);
            foretDTO.setForet_member(member_list);
            foretDTO.setForet_leader(leader[a]);
            foretDTO.setForet_region_si(si_list);
            foretDTO.setForet_region_gu(gu_list);
            foretDTO.setForet_photo("https://www.google.com/search?q=%EC%95%84%EC%9D%B4%EC%9C%A0&source=lnms&tbm=isch&sa=X&ved=2ahUKEwiOw7Xnj7TtAhXL62EKHasdArIQ_AUoAXoECAUQAw&biw=1920&bih=937#imgrc=1dKx6vBOwCxVWM");
            foretDTOList.add(foretDTO);

        }
    }

    private void getMember() {
        SessionManager sessionManager = new SessionManager(getContext());
        String email = sessionManager.getSessionEmail();
        String password = sessionManager.getSessionPassword();

        url = "http://34.72.240.24:8085/foret/search/member.do";
        client = new AsyncHttpClient();
        mainFragmentResponse = new MainFragmentResponse();
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        //client.post(url, params, mainFragmentResponse);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 포레 클릭 이벤트
        foretAdapter.setOnClickListener(new ForetAdapter.OnClickListener() {
            @Override
            public void onClick(View v, ForetDTO foretDTO) {
                Intent intent = new Intent(activity, ViewForetActivity.class);
                intent.putExtra("ForetDTO", foretDTO);
                startActivity(intent);
            }
        });
    }

    private void setView() {
        // 공지사항
        //foretBoardAdapter = new ForetBoardAdapter(getActivity(), foretBoardDTOList);
        //recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView1.setAdapter(foretBoardAdapter);

        // 일정
        //recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView2.setAdapter(foretBoardAdapter);

        // 새글 피드
        //newBoardFeedAdapter = new NewBoardFeedAdapter(getActivity(), foretBoardDTOList);
        //recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView3.setAdapter(newBoardFeedAdapter);
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
        foretDTO = foretDTOList.get(position);
        textView_name.setText(foretDTO.getForet_name());
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("[TEST]", "onPageSelected 호출 : " + position);
        int page_position = position;
        switch (position) {
            case 0:
                getViewChange(page_position);
                break;
            case 1:
                getViewChange(page_position);
                break;
            case 2:
                getViewChange(page_position);
                break;
            case 3:
                getViewChange(page_position);
                break;
            case 4:
                getViewChange(page_position);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("[TEST]", "onPageScrollStateChanged 호출 : " + state);
    }

    private void getViewChange(int page_position) {
        url = "http://34.72.240.24:8085/search/homeFragement.do";
        client = new AsyncHttpClient();
        foretBoardResponse = new ForetBoardResponse();
        RequestParams params = new RequestParams();
        params.put("foret_id", page_position + 1);
        //client.post(url, params, foretBoardResponse);
        setView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1: // 더많포레 -> 서치로 이동
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, searchFragment).commit();
                activity.getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
            //case R.id.button2: // 화살표
            //    intent = new Intent(activity, ListForetBoardActivity.class);
            //    startActivity(intent);
            //    break;
        }
    }

    class MainFragmentResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            Log.d("[TEST]", "MainFragmentResponse onStart() 호출");
        }

        @Override
        public void onFinish() {
            super.onFinish();
            Log.d("[TEST]", "MainFragmentResponse onFinish() 호출");
            url = "http://34.72.240.24:8085/search/foretList.do";
            foretResponse = new ForetResponse();
            RequestParams params = new RequestParams();
            params.put("member_id", memberDTO.getId());
          //  client.post(url, params, foretResponse);

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
                    Toast.makeText(getActivity(), "회원정보 가져옴", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "회원정보 못가져옴", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getActivity(), "MainFragmentResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }

    class ForetResponse extends AsyncHttpResponseHandler {

        @Override
        public void onStart() {
            Log.d("[TEST]", "ForetResponse onStart() 호출");
        }

        @Override
        public void onFinish() {
            Log.d("[TEST]", "ForetResponse onFinish() 호출");
            url = "http://34.72.240.24:8085/search/homeFragement.do";
            foretBoardResponse = new ForetBoardResponse();
            RequestParams params = new RequestParams();
            params.put("foret_id", foretDTO.getForet_id());
            client.post(url, params, foretBoardResponse);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            foretDTOList = new ArrayList<>();
            String str = new String(responseBody);
            Gson gson = new Gson();
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if (RT.equals("OK")) {
                    JSONArray foret = json.getJSONArray("foret");
                    JSONObject temp = foret.getJSONObject(0);
                    for (int i = 0; i < foret.length(); i++) {
                        foretDTO = gson.fromJson(temp.toString(), ForetDTO.class);
                        foretDTOList.add(foretDTO);
                    }
                    Log.d("[TEST]", "foretDTOList.size() => " + foretDTOList.size());
                    Toast.makeText(getActivity(), "포레정보 가져옴", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "포레정보 못가져옴", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getActivity(), "ForetResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }

    class ForetBoardResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            Log.d("[TEST]", "ForetBoardResponse onStart() 호출");
        }

        @Override
        public void onFinish() {
            Log.d("[TEST]", "ForetBoardResponse onFinish() 호출");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            foretBoardDTOList = new ArrayList<>();
            String str = new String(responseBody);
            Gson gson = new Gson();
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if (RT.equals("OK")) {
                    JSONArray board = json.getJSONArray("board");
                    JSONObject temp = board.getJSONObject(0);
                    for (int i = 0; i < board.length(); i++) {
                        foretBoardDTO = gson.fromJson(temp.toString(), ForetBoardDTO.class);
                        foretBoardDTOList.add(foretBoardDTO);
                    }
                    Log.d("[TEST]", "foretBoardDTOList.size() => " + foretBoardDTOList.size());
                    Toast.makeText(getActivity(), "포레 게시판 정보 가져옴", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "포레 게시판 정보 못가져옴", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getActivity(), "ForetBoardResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }

}