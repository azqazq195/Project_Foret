package com.example.foret_app_prototype.activity.free;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    LikeChangeResponse0 response3;
    List<ForetBoard> list;
    List<Integer> like;
    ForetBoard foretBoard;
    List<Integer> like_plus;
    List<Integer> like_minus;

    int id;

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
        response3 = new LikeChangeResponse0(); //화면이 닫힐 때 좋아요 상태 서버에 업그레이드
        like = new ArrayList<>();
        foretBoard = new ForetBoard();
        like_plus = new ArrayList<>();
        like_minus = new ArrayList<>();

        SessionManager sessionManager = new SessionManager(activity);
        id = sessionManager.getSession();

        recyclerView2.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button_back.setOnClickListener(this);

        return rootView;
    }

    public void onResume() {
        super.onResume();
        list.clear();
        like.clear();
        RequestParams params = new RequestParams();
        params.put("type", 0);
        params.put("inquiry_type", 1);
        params.put("pg", 1);
        params.put("size", 10);
        client.post("http://34.72.240.24:8085/foret/search/boardList.do", params, response1);
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
        list.clear();
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
                params.put("inquiry_type", 1);
                client.post("http://34.72.240.24:8085/foret/search/boardList.do", params, response1);
                break;
            case R.id.button2 : //댓글순
                button2.setTextColor(Color.parseColor("#22997b"));
                button2.setTypeface(Typeface.DEFAULT_BOLD);
                button1.setTypeface(null);
                button1.setTextColor(Color.GRAY);
                button3.setTypeface(null);
                button3.setTextColor(Color.GRAY);
                params.put("inquiry_type", 4);
                client.post("http://34.72.240.24:8085/foret/search/boardList.do", params, response1);
                break;
            case R.id.button3 : //추천순
                button3.setTextColor(Color.parseColor("#22997b"));
                button3.setTypeface(Typeface.DEFAULT_BOLD);
                button2.setTypeface(null);
                button2.setTextColor(Color.GRAY);
                button1.setTypeface(null);
                button1.setTextColor(Color.GRAY);
                params.put("inquiry_type", 3);
                client.post("http://34.72.240.24:8085/foret/search/boardList.do", params, response1);
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

/*    @Override
    public void onlikeClick(View view, int count, int board_id, int original_count) {
        if (count != original_count) {
            if(count > original_count) { //좋아요 수가 증가한 것임
                like_plus.add(board_id);
            } else { //좋아요 수가 감소한 것임
                like_minus.add(board_id);
            }
        } else { //좋아요 버튼 변화가 있지만 count = original_count일때. (좋아요를 눌렀다가 취소하거나 다시 좋아요를 누름) -> 변화전달x(리스트에서 제거)
            if (like_plus.contains(board_id)) {
                like_plus.remove((int) board_id);
            } else if (like_minus.contains(board_id)) {
                like_minus.remove((int)board_id);
            } else {
                return; //에러방지
            }
        }
    }*/

/*    //화면이 닫힐때 상태를 저장해서 서버에 전달해야함
    @Override
    public void onPause() {
        super.onPause();
        Log.e("[진입]", "onPause");
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("type", 0);
        for(int a=0; a<like_plus.size(); a++) { //좋아요 개수가 증가한 글
            params.put("board_id", list.get(a));
            client.post("http://34.72.240.24:8085/foret/member/member_board_like.do", params, response3);
        }
        for(int a=0; a<like_minus.size(); a++) { //좋아요 개수가 감소한 글
            client.post("http://34.72.240.24:8085/foret/member/member_board_like.do", params, response3);
        }
    }*/

    class FreeboardListResponse extends AsyncHttpResponseHandler {

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Gson gson = new GsonBuilder().create();
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if (json.getString("RT").equals("OK")) {
                    JSONArray board = json.getJSONArray("board");
                    for(int a=0; a<board.length(); a++) {
                        foretBoard = new ForetBoard();
                        JSONObject object = board.getJSONObject(a);
                       // foretBoard = gson.fromJson(object.toString(), ForetBoard.class);
                        foretBoard.setReg_date(object.getString("reg_date"));
                        foretBoard.setLike_count(object.getInt("board_like"));
                        foretBoard.setSubject(object.getString("subject"));
                        foretBoard.setComment_count(object.getInt("board_comment"));
                        foretBoard.setId(object.getInt("id")); //글 번호
                        foretBoard.setWriter(String.valueOf(object.getInt("writer")));
                        foretBoard.setContent(object.getString("content"));
                        list.add(foretBoard);
                    }
                    RequestParams params = new RequestParams();
                    params.put("id", id);
                    final int DEFAULT_TIME = 50*1000;
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
                    JSONArray like_board= object.getJSONArray("like_board");
                    for (int a=0; a<like_board.length(); a++) {
                        int board_seq = (int) like_board.get(a);
                        like.add(board_seq);
                    }
                    for (int a=0; a<list.size(); a++) {
                        ForetBoard foretBoard = list.get(a);
                        int seq = foretBoard.getId();
                        if (like.contains(seq)) {
                            foretBoard.setLike(true);
                        } else {
                            foretBoard.setLike(false);
                        }
                        list.set(a, foretBoard);
                    }
                    adapter = new ListFreeBoardAdapter(list, activity, id);
                    recyclerView2.setAdapter(adapter);
                    //adapter.setOnClick(FreeFragment.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "댓글 목록을 불러 올 수 없습니다", Toast.LENGTH_SHORT).show();
        }
    }

    class LikeChangeResponse0 extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.e("[진입]", 4+"");
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getString("memberRT").equals("OK")) {
                    Log.e("[진입]", 5+"");
                    Toast.makeText(activity, "좋아요 상태 저장 성공", Toast.LENGTH_SHORT).show();
                    Log.e("[진입]", 6+"");
                    Log.e("[확인]", foretBoard.getId()+"");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "좋아요 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }

}