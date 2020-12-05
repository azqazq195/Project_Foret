package com.example.foret_app_prototype.activity.free;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.activity.login.SessionManager;
import com.example.foret_app_prototype.adapter.free.ListFreeBoardAdapter;
import com.example.foret_app_prototype.model.ForetBoard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FreeFragment extends Fragment implements View.OnClickListener {

    MainActivity activity;
    RecyclerView recyclerView2;
    TextView button1, button2, button3;
    FloatingActionButton button4;
    ImageView button_back;
    Toolbar toolbar;
    LinearLayout layout_search;
    ListView recyclerView1;
    ListFreeBoardAdapter adapter;
    AsyncHttpClient client;
    FreeboardListResponse response1;
    FreeboardLikeResponse response2;
    List<ForetBoard> list;
    List<JSONArray> like;

    int id;
    String email;
    String pw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_free, container, false);
        toolbar = (androidx.appcompat.widget.Toolbar) rootView.findViewById(R.id.free_toolbar);
        activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(null);
        setHasOptionsMenu(true);

        list = new ArrayList<>();
        layout_search = rootView.findViewById(R.id.layout_search);
        recyclerView1 = rootView.findViewById(R.id.recyclerView1);
        recyclerView2 = rootView.findViewById(R.id.recyclerView2);
        button1 = rootView.findViewById(R.id.button1);
        button2 = rootView.findViewById(R.id.button2);
        button3 = rootView.findViewById(R.id.button3);
        button4 = rootView.findViewById(R.id.button4);
        button_back = rootView.findViewById(R.id.button_back);
        client = new AsyncHttpClient();
        response1 = new FreeboardListResponse(); //리스트
        response2 = new FreeboardLikeResponse(); //좋아요
        like = new ArrayList<JSONArray>();

        SessionManager sessionManager = new SessionManager(activity);
        id = sessionManager.getSession();
        email = sessionManager.getSessionEmail();
        pw = sessionManager.getSessionPassword();

        recyclerView2.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));

        testData();

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button_back.setOnClickListener(this);

        return rootView;
    }

   /* public void onResume() {
        super.onResume();
        list.clear();
        RequestParams params = new RequestParams();
        params.put("type", 0);
        params.put("pg", 1);
        params.put("size", 10);
        client.post("http://34.72.240.24:8085/foret/search/boardListRecentPage.do", params, response1);
    }*/

    private void testData() {
        for (int a=0; a<3; a++) {
            ForetBoard foretBoard = new ForetBoard();
            foretBoard.setContent("테스트용 만들기");
            foretBoard.setSubject("테스트용 ");
            foretBoard.setComment_count(5);
            foretBoard.setLike_count(3);
            foretBoard.setReg_date("2012-02-02");
            foretBoard.setId(3);
            foretBoard.setWriter("15");
            list.add(foretBoard);
        }
        adapter = new ListFreeBoardAdapter(list, activity, id);
        recyclerView2.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.normal_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu :
                DrawerLayout container = activity.findViewById(R.id.container);
                container.openDrawer(GravityCompat.END);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        RequestParams params = new RequestParams();
        params.put("type", 0);
        params.put("pg", 1);
        params.put("size", 10);
        switch (v.getId()) {
            case R.id.button1 : //최신순
                button1.setTextColor(Color.parseColor("#22997b"));
                button1.setTypeface(Typeface.DEFAULT_BOLD);
                button2.setTypeface(null);
                button2.setTextColor(Color.GRAY);
                button3.setTypeface(null);
                button3.setTextColor(Color.GRAY);
                client.post("http://34.72.240.24:8085/foret/search/etcBoardListRecentPage.do", params, response1);
                break;
            case R.id.button2 : //추천순
                button2.setTextColor(Color.parseColor("#22997b"));
                button2.setTypeface(Typeface.DEFAULT_BOLD);
                button1.setTypeface(null);
                button1.setTextColor(Color.GRAY);
                button3.setTypeface(null);
                button3.setTextColor(Color.GRAY);
                client.post("http://34.72.240.24:8085/foret/search/etcBoardListLikePage.do", params, response1);
                break;
            case R.id.button3 : //댓글순
                button3.setTextColor(Color.parseColor("#22997b"));
                button3.setTypeface(Typeface.DEFAULT_BOLD);
                button2.setTypeface(null);
                button2.setTextColor(Color.GRAY);
                button1.setTypeface(null);
                button1.setTextColor(Color.GRAY);
                client.post("http://34.72.240.24:8085/foret/search/etcBoardListCommentPage.do", params, response1);
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

    class FreeboardListResponse extends AsyncHttpResponseHandler {

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getString("RT").equals("OK")) {
                    JSONArray board = json.getJSONArray("board");
                    for(int a=0; a<board.length(); a++) {
                        ForetBoard foretBoard = new ForetBoard();
                        JSONObject object = board.getJSONObject(a);
                        foretBoard.setReg_date(object.getString("reg_date"));
                        foretBoard.setHit(object.getInt("hit"));
                        foretBoard.setLike_count(object.getInt("board_like"));
                        foretBoard.setSubject(object.getString("subject"));
                        foretBoard.setComment_count(object.getInt("board_comment"));
                        foretBoard.setId(object.getInt("id")); //글 번호
                        foretBoard.setWriter(String.valueOf(object.getInt("writer")));
                        foretBoard.setEdit_date(object.getString("edit_date"));
                        foretBoard.setContent(object.getString("content"));
                        list.add(foretBoard);
                    }
                    RequestParams params = new RequestParams();
                    params.put("email", email);
                    params.put("password", pw);
                    final int DEFAULT_TIME = 20*1000;
                    client.setConnectTimeout(DEFAULT_TIME);
                    client.setResponseTimeout(DEFAULT_TIME);
                    client.setTimeout(DEFAULT_TIME);
                    client.setResponseTimeout(DEFAULT_TIME);
                    client.post("http://34.72.240.24:8085/foret/search/member.do", params, response2);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "목록을 불러 올 수 없습니다", Toast.LENGTH_SHORT).show();
        }
    }

    class FreeboardLikeResponse extends  AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getString("RT").equals("OK")){
                    JSONArray member = json.getJSONArray("member");
                    JSONObject object = member.getJSONObject(0);
                    JSONArray like_comment = object.getJSONArray("like_comment");
                    like.add(like_comment);
                    for (ForetBoard foretBoard : list) {
                        int seq = foretBoard.getId();
                        if(like.contains(seq)) {
                            foretBoard.setLike(true);
                        } else {
                            foretBoard.setLike(false);
                        }
                    }
                    adapter = new ListFreeBoardAdapter(list, activity, id);
                    recyclerView2.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }

}