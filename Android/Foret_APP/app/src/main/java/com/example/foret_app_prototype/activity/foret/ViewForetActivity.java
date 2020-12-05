package com.example.foret_app_prototype.activity.foret;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.foret.board.WriteForetBoardActivity;

import com.example.foret_app_prototype.adapter.foret.ViewForetBoardAdapter;
import com.example.foret_app_prototype.model.ForetBoardDTO;
import com.example.foret_app_prototype.model.ForetDTO;
import com.example.foret_app_prototype.model.MemberDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ViewForetActivity extends AppCompatActivity implements View.OnClickListener {
    MemberDTO memberDTO;
    ForetDTO foretDTO;
    ForetBoardDTO foretBoardDTO;
    List<ForetBoardDTO> foretBoardDTOList;

    ViewForetBoardAdapter viewForetBoardAdapter;

    Toolbar toolbar;
    Intent intent;
    RecyclerView board_list, listView_notice;
    TextView textView_tag, textView_region, textView_intro, textView_member,
            textView_master, textView_foretName, textView_date, button10, button11;
    Button button1, button2, button3;
    ImageView imageView_profile;
    LinearLayout noti_layout, board_layout;
    FloatingActionButton fab_add;

    String url;
    AsyncHttpClient client;
    ViewForetResponse viewForetResponse;
    MemberResponse memberResponse;
    BoardResponse boardResponse;
    JoinResponse joinResponse;
    LeaveResponse leaveResponse;

    String foret_id = "";
    // 공지사항 페이징
    int noti_pg = 1;
    final int noti_size = 3;

    // 새글 페이징
    int board_pg = 1;
    final int board_size = 5;

    int GRADE = 0; // 포레에 가입한 멤버/마스터/가입안함 구분 코드(0=가입안함, 1=가입함, 2=마스터)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_foret);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foret_id = getIntent().getStringExtra("foret_id");
        getFindId(); // 객체 초기화
        addFab(); // 플로팅 버튼

        getMember(); // 회원 정보 가져오기

        int foret_id = getIntent().getIntExtra("foret_id" , foretDTO.getForet_id());
        foretDTO.setForet_id(foret_id);

        //GRADE = getIntent().getIntExtra("GRADE", 0);

        if(GRADE == 0) { // 포레 가입전 - 가입하기
            button1.setVisibility(View.VISIBLE);
        } else if(GRADE == 1) { // 가입한 상태 - 탈퇴하기
            button2.setVisibility(View.VISIBLE);
            noti_layout.setVisibility(View.VISIBLE);
            board_layout.setVisibility(View.VISIBLE);
        } else if(GRADE == 2) { // 마스터 - 수정하기
            button3.setVisibility(View.VISIBLE);
            noti_layout.setVisibility(View.VISIBLE);
            board_layout.setVisibility(View.VISIBLE);
        }

        dataSetting(); // 데이터 세팅


    }

    @Override
    protected void onResume() {
        super.onResume();
        board_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if(lastPosition == totalCount){
                    board_pg++;
                    getBoard();
                }
            }
        });

    }

    // 플로팅 버튼
    private void addFab() {
        fab_add = findViewById(R.id.fab_add);
        fab_add.bringToFront();
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ViewForetActivity.this, WriteForetBoardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getMember() {
        url = "http://34.72.240.24:8085/foret/search/member.do";
        client = new AsyncHttpClient();
        memberResponse = new MemberResponse();
        RequestParams params = new RequestParams();
        params.put("id", memberDTO.getId());
        client.post(url, params, memberResponse);
    }

    private void getForet() {
        url = "http://34.72.240.24:8085/foret/search/member.do";
        client = new AsyncHttpClient();
        viewForetResponse = new ViewForetResponse();
        RequestParams params = new RequestParams();
        params.put("id", memberDTO.getId());
        client.post(url, params, viewForetResponse);
    }

    private void getNotice() {
        url = "http://34.72.240.24:8085/foret/search/boardList.do";
        client = new AsyncHttpClient();
        boardResponse = new BoardResponse();
        RequestParams params = new RequestParams();
        params.put("type", 2);
        params.put("foret_id", foret_id);
        params.put("pg", noti_pg);
        params.put("size", noti_size);
        params.put("inquiry_type", 1);
        client.post(url, params, boardResponse);
    }

    private void getBoard() {
        url = "http://34.72.240.24:8085/foret/search/boardList.do";
        client = new AsyncHttpClient();
        boardResponse = new BoardResponse();
        RequestParams params = new RequestParams();
        params.put("type", 4);
        params.put("foret_id", foret_id);
        params.put("pg", board_pg);
        params.put("size", board_size);
        params.put("inquiry_type", 1);
        client.post(url, params, boardResponse);
    }

    private void foretJoin() {
        url = "http://34.72.240.24:8085/foret/foret/foret_member_insert.do";
        client = new AsyncHttpClient();
        joinResponse = new JoinResponse();
        RequestParams params = new RequestParams();
        params.put("foret_id", foret_id);
        params.put("member_id", memberDTO.getId());
        client.post(url, params, joinResponse);
    }

    private void foretLeave() {
        url = "http://34.72.240.24:8085/foret/foret/foret_member_delete.do";
        client = new AsyncHttpClient();
        leaveResponse = new LeaveResponse();
        RequestParams params = new RequestParams();
        params.put("foret_id", foret_id);
        params.put("member_id", memberDTO.getId());
        client.post(url, params, leaveResponse);
    }


    private void getFindId() {
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button10 = findViewById(R.id.button10);
        button11 = findViewById(R.id.button11);
        imageView_profile = findViewById(R.id.imageView_profile);
        textView_foretName = findViewById(R.id.textView_foretName);
        textView_tag = findViewById(R.id.textView_tag);
        textView_region = findViewById(R.id.textView_region);
        textView_member = findViewById(R.id.textView_member);
        textView_master = findViewById(R.id.textView_master);
        textView_date = findViewById(R.id.textView_date);
        textView_intro = findViewById(R.id.textView_intro);
        listView_notice = findViewById(R.id.listView_notice);
        board_list = findViewById(R.id.board_list);

        button1.setOnClickListener(this); // 가입하기
        button2.setOnClickListener(this); // 탈퇴하기
        button3.setOnClickListener(this); // 수정하기
        button10.setOnClickListener(this); // 다음
        button11.setOnClickListener(this); // 이전
    }

    private void dataSetting() {
        if(foretDTO.getForet_photo().equals("") || foretDTO.getForet_photo().equals(null)) {
            Glide.with(this).load(foretDTO.getForet_photo()).into(imageView_profile);
        }
        textView_foretName.setText(foretDTO.getForet_name());
        String[] str_tag = new String[foretDTO.getForet_tag().size()];
        for(int i=0; i<foretDTO.getForet_tag().size(); i++) {
            str_tag[i] = "#" + foretDTO.getForet_tag().get(i) + " ";
        }
        textView_tag.setText(Arrays.toString(str_tag));
        String[] str_si = new String[foretDTO.getForet_region_si().size()];
        String[] str_gu = new String[foretDTO.getForet_region_gu().size()];
        String str_region = "";
        for(int i=0; i<foretDTO.getForet_region_si().size(); i++) {
            str_si[i] = foretDTO.getForet_region_si().get(i);
            str_gu[i] = foretDTO.getForet_region_gu().get(i);
            str_region += str_si[i] + " " + str_gu[i] + ", ";
        }
        textView_region.setText(str_region.substring(0, str_region.length()-2));
        textView_member.setText(foretDTO.getForet_member().size() + "/" + foretDTO.getMax_member());
        textView_master.setText("포레 리더 : " + foretDTO.getForet_leader());
        textView_date.setText(foretDTO.getReg_date());
        textView_intro.setText(foretDTO.getIntroduce());

        viewForetBoardAdapter = new ViewForetBoardAdapter(this, foretBoardDTOList);
        listView_notice.setLayoutManager(new LinearLayoutManager(this));
        listView_notice.setAdapter(viewForetBoardAdapter);

        board_list.setLayoutManager(new LinearLayoutManager(this));
        board_list.setAdapter(viewForetBoardAdapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.basic_toolbar, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1 : // 가입하고 있지 않을 때 -> 가입하기버튼 누를시 가입
                joinDialog();
                break;
            case R.id.button2 : // 가입하고 있을 때 -> 회원탈퇴
                leaveDialog();
                break;
            case R.id.button3 : // 가입하고 있고, 관리자일 때 -> 수정하기 화면으로 이동
                intent = new Intent(this, EditForetActivity.class);
                intent.putExtra("foretDTO", foretDTO);
                startActivity(intent);
                break;
            case R.id.button10: // 공지사항 다음
                noti_pg++;
                getNotice();
                break;
            case R.id.button11: // 공지사항 이전
                noti_pg--;
                getNotice();
                if(noti_pg == 1) {
                    button11.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    private void joinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("가입 하시겠습니까?");
        builder.setPositiveButton("가입", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 포레 가입
                foretJoin();
                button2.setVisibility(View.VISIBLE);
                button1.setVisibility(View.GONE);
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void leaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("정말로 탈퇴 하시겠습니까?");
        builder.setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 포레 탈퇴
                foretLeave();
                button2.setVisibility(View.VISIBLE);
                button1.setVisibility(View.GONE);
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
            Toast.makeText(ViewForetActivity.this, "MemeberResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }

    class ViewForetResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            Log.d("[TEST]", "ViewForetResponse onStart() 호출");
        }

        @Override
        public void onFinish() {
            super.onFinish();
            Log.d("[TEST]", "ViewForetResponse onFinish() 호출");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            // 포레 데이터 받는 제이슨
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ViewForetActivity.this, "ViewForetResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }

    class BoardResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            Log.d("[TEST]", "BoardResponse onStart() 호출");
        }

        @Override
        public void onFinish() {
            Log.d("[TEST]", "BoardResponse onFinish() 호출");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            foretBoardDTOList = new ArrayList<>();
            String str = new String(responseBody);
            Gson gson = new Gson();
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if(RT.equals("OK")) {
                    JSONArray board = json.getJSONArray("board");
                    JSONObject temp = board.getJSONObject(0);
                    for(int i=0; i<board.length(); i++) {
                        foretBoardDTO = gson.fromJson(temp.toString(), ForetBoardDTO.class);
                        foretBoardDTOList.add(foretBoardDTO);
                    }
                    Log.d("[TEST]", "foretBoardDTOList.size() => " + foretBoardDTOList.size());
                    Log.d("[TEST]", "포레 게시판 리스트 가져옴");
                } else {
                    Log.d("[TEST]", "포레 게시판 리스트 못가져옴");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ViewForetActivity.this, "BoardResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }

    class JoinResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            Log.d("[TEST]", "JoinResponse onStart() 호출");
        }

        @Override
        public void onFinish() {
            super.onFinish();
            Log.d("[TEST]", "JoinResponse onStart() 호출");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String foretMemberRT = json.getString("foretMemberRT");
                if (foretMemberRT.equals("OK")) {
                    Toast.makeText(ViewForetActivity.this, "포레에 가입했습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("[TEST]", "가입 성공");
                } else {
                    Log.d("[TEST]", "가입 실패");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ViewForetActivity.this, "JoinResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }

    class LeaveResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            Log.d("[TEST]", "LeaveResponse onStart() 호출");
        }

        @Override
        public void onFinish() {
            super.onFinish();
            Log.d("[TEST]", "LeaveResponse onStart() 호출");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String foretMemberRT = json.getString("foretMemberRT");
                if (foretMemberRT.equals("OK")) {
                    Toast.makeText(ViewForetActivity.this, "포레를 탈퇴했습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("[TEST]", "탈퇴 성공");
                } else {
                    Log.d("[TEST]", "탈퇴 실패");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ViewForetActivity.this, "LeaveResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }
}