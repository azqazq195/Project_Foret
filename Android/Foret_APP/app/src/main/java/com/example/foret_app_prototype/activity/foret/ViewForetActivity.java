package com.example.foret_app_prototype.activity.foret;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.foret.board.WriteForetBoardActivity;
import com.example.foret_app_prototype.activity.notify.APIService;
import com.example.foret_app_prototype.activity.notify.Client;
import com.example.foret_app_prototype.activity.notify.Data;
import com.example.foret_app_prototype.activity.notify.Response;
import com.example.foret_app_prototype.activity.notify.Sender;
import com.example.foret_app_prototype.activity.notify.Token;
import com.example.foret_app_prototype.activity.search.SearchFragment;
import com.example.foret_app_prototype.adapter.foret.BoardViewAdapter;
import com.example.foret_app_prototype.adapter.foret.ViewForetBoardAdapter;
import com.example.foret_app_prototype.model.ForetBoardDTO;
import com.example.foret_app_prototype.model.ForetDTO;
import com.example.foret_app_prototype.model.ForetViewDTO;
import com.example.foret_app_prototype.model.MemberDTO;
import com.example.foret_app_prototype.model.ModelUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;

public class ViewForetActivity extends AppCompatActivity implements View.OnClickListener {
    MemberDTO memberDTO;
    int foret_id;

    ForetViewDTO foretViewDTO;
    ForetBoardDTO foretBoardDTO;
    List<ForetBoardDTO> foretBoardDTOList;

    ViewForetBoardAdapter viewForetBoardAdapter;
    BoardViewAdapter boardViewAdapter;

    Toolbar toolbar;
    Intent intent;
    RecyclerView board_list, listView_notice;
    TextView textView_tag, textView_region, textView_intro, textView_member,
            textView_master, textView_foretName, textView_date, button10, button11;
    Button button1, button2, button3;
    ImageView imageView_profile;
    LinearLayout noti_layout, board_layout;
    FloatingActionButton write_fab_add;

    String url;
    AsyncHttpClient client;
    ViewForetResponse viewForetResponse;
    MemberResponse memberResponse;
    NoticeResponse noticeResponse;
    BoardResponse boardResponse;
    JoinResponse joinResponse;
    LeaveResponse leaveResponse;

    // 공지사항 페이징
    int noti_pg = 1;
    final int noti_size = 3;

    // 새글 페이징
    int board_pg = 1;
    final int board_size = 5;

    String rank = ""; // guest = 가입안함, member = 가입함, leader = 리더

    String foretname;
    String myNickName;

    APIService apiService;
    boolean notify = false;
    String hisUid, myUid;
    //SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memberDTO = (MemberDTO) getIntent().getSerializableExtra("memberDTO");
        //  searchFragment = new SearchFragment(memberDTO);
        //  searchFragment = searchFragment.getSearchFragment();

        setContentView(R.layout.activity_view_foret);

        // 상태바 색상 변경
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.foret4));
        MemberDTO memberDTO = (MemberDTO) getIntent().getSerializableExtra("memberDTO");
        foret_id = getIntent().getIntExtra("foret_id", 0);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFindId(); // 객체 초기화
        addFab(); // 플로팅 버튼
        getNotice();
        getBoard();
        checkUserStatus();

        Log.d("[TEST]", "넘어온 포레 아디 => " + foret_id);
        Log.d("[TEST]", "넘어온 회원 아디 => " + memberDTO.getId());
        Log.d("[TEST]", "넘어온 회원 아디 => " + memberDTO.getNickname());
        myNickName = memberDTO.getNickname();

        // 푸쉬 알림 생성
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);


    }

    @Override
    protected void onStart() {
        super.onStart();
        getMember(); // 회원 정보 가져오기
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getForet(); // 포레 정보

        board_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView board_list, int newState) {
                super.onScrollStateChanged(board_list, newState);
                Log.d("[TEST]", "onScrollStateChanged 호출 => " + newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView board_list, int dx, int dy) {
                super.onScrolled(board_list, dx, dy);
                Log.d("[TEST]", "onScrolled 호출 => " + dx + " / " + dy);
                int lastPosition = ((LinearLayoutManager) board_list.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = board_list.getAdapter().getItemCount();

                if (lastPosition == totalCount) {
                    board_pg++;
                    getBoard();
                    Log.d("[TEST]", "board_pg 호출 => " + board_pg);
                }
            }
        });

    }

    // 플로팅 버튼
    private void addFab() {
        write_fab_add = findViewById(R.id.fab_add3);
        write_fab_add.bringToFront();
        write_fab_add.setVisibility(View.GONE);
        write_fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ViewForetActivity.this, WriteForetBoardActivity.class);
                intent.putExtra("foret_id", foret_id);
                intent.putExtra("memberDTO", memberDTO);
                intent.putExtra("foretname", foretname);
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
        url = "http://34.72.240.24:8085/foret/search/foretSelect.do";
        client = new AsyncHttpClient();
        viewForetResponse = new ViewForetResponse();
        RequestParams params = new RequestParams();
        params.put("foret_id", foret_id);
        params.put("member_id", memberDTO.getId());
        client.post(url, params, viewForetResponse);
    }

    private void getNotice() {
        url = "http://34.72.240.24:8085/foret/search/boardList.do";
        client = new AsyncHttpClient();
        noticeResponse = new NoticeResponse();
        RequestParams params = new RequestParams();
        params.put("type", 2);
        params.put("foret_id", foret_id);
        params.put("pg", noti_pg);
        params.put("size", noti_size);
        params.put("inquiry_type", 1);
        client.post(url, params, noticeResponse);
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
        noti_layout = findViewById(R.id.noti_layout);
        board_layout = findViewById(R.id.board_layout);
        foretBoardDTOList = new ArrayList<>();

        button1.setOnClickListener(this); // 가입하기
        button2.setOnClickListener(this); // 탈퇴하기
        button3.setOnClickListener(this); // 수정하기
        button10.setOnClickListener(this); // 다음
        button11.setOnClickListener(this); // 이전
    }

    private void dataSetting() {
        Log.e("[test]", "foretViewDTO.getPhoto()?" + foretViewDTO.getPhoto());
        Glide.with(this).load(foretViewDTO.getPhoto()).
                placeholder(R.drawable.sss).into(imageView_profile);

        textView_foretName.setText(foretViewDTO.getName());
        String[] str_tag = new String[foretViewDTO.getForet_tag().size()];
        for (int i = 0; i < foretViewDTO.getForet_tag().size(); i++) {
            str_tag[i] = "#" + foretViewDTO.getForet_tag().get(i) + " ";
        }
        textView_tag.setText(Arrays.toString(str_tag));
        String[] str_si = new String[foretViewDTO.getForet_region_si().size()];
        String[] str_gu = new String[foretViewDTO.getForet_region_gu().size()];
        String str_region = "";
        for (int i = 0; i < foretViewDTO.getForet_region_si().size(); i++) {
            str_si[i] = foretViewDTO.getForet_region_si().get(i);
            str_gu[i] = foretViewDTO.getForet_region_gu().get(i);
            str_region += str_si[i] + " " + str_gu[i] + ", ";
        }
        textView_region.setText(str_region.substring(0, str_region.length() - 2));
        textView_member.setText(foretViewDTO.getMember().size() + "/" + foretViewDTO.getMax_member());
        textView_master.setText("포레 리더 : " + memberDTO.getNickname());
        String date = foretViewDTO.getReg_date().substring(0, 10);
        textView_date.setText(date);
        textView_intro.setText(foretViewDTO.getIntroduce());

        //파이어 베이스용
        foretname = foretViewDTO.getName();
    }

    private void setBoard() {
        viewForetBoardAdapter = new ViewForetBoardAdapter(this, memberDTO, foretBoardDTOList);
        listView_notice.setLayoutManager(new LinearLayoutManager(this));
        listView_notice.setAdapter(viewForetBoardAdapter);

        boardViewAdapter = new BoardViewAdapter(this, memberDTO, foretBoardDTOList);
        board_list.setLayoutManager(new LinearLayoutManager(this));
        board_list.setAdapter(boardViewAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1: // 가입하고 있지 않을 때 -> 가입하기버튼 누를시 가입
                joinDialog();
                break;
            case R.id.button2: // 가입하고 있을 때 -> 회원탈퇴
                leaveDialog();
                break;
            case R.id.button3: // 가입하고 있고, 관리자일 때 -> 수정하기 화면으로 이동
                intent = new Intent(this, EditForetActivity.class);
                intent.putExtra("foretViewDTO", foretViewDTO);
                intent.putExtra("memberDTO", memberDTO);
                startActivity(intent);
                break;
            case R.id.button10: // 공지사항 다음
                noti_pg++;
                getNotice();
                if (noti_pg > 1) {
                    button11.setVisibility(View.VISIBLE);
                } else if (viewForetBoardAdapter.getItemCount() < 3) {
                    button11.setVisibility(View.INVISIBLE);
                } else if (viewForetBoardAdapter.getItemCount() == 3) {
                    button11.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.button11: // 공지사항 이전
                noti_pg--;
                getNotice();
                if (noti_pg == 1) {
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
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //초기 데이터 로딩 멤버 정보 가져오기
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
            getForet();
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
                    Log.d("[member]", "아디 가져옴 ==== " + memberDTO.getId());
                    Log.d("[member]", "닉넴 가져옴 ==== " + memberDTO.getNickname());
                    Log.d("[member]", "이름 가져옴 ==== " + memberDTO.getName());
                    Log.d("[member]", "생일 가져옴 ==== " + memberDTO.getBirth());
                    Log.d("[member]", "사진 가져옴 ==== " + memberDTO.getPhoto());
                    Log.d("[member]", "이멜 가져옴 ==== " + memberDTO.getEmail());
                    Log.d("[member]", "비번 가져옴 ==== " + memberDTO.getPassword());
                    Log.d("[member]", "가입일 가져옴 ==== " + memberDTO.getReg_date());
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

    //초기 데이터 로딩 포레 정보
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
            String str = new String(responseBody);
            Gson gson = new Gson();
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");

                if (RT.equals("OK")) {
                    JSONArray foret = json.getJSONArray("foret");
                    JSONObject temp = foret.getJSONObject(0);
                    foretViewDTO = new ForetViewDTO();
                    foretViewDTO = gson.fromJson(temp.toString(), ForetViewDTO.class);

                    JSONArray region_si = temp.getJSONArray("region_si");
                    List<String> si_list = new ArrayList<>();
                    for (int i = 0; i < region_si.length(); i++) {
                        String str_si = String.valueOf(region_si.get(i));
                        si_list.add(str_si);
                    }
                    foretViewDTO.setForet_region_si(si_list);

                    JSONArray region_gu = temp.getJSONArray("region_gu");
                    List<String> gu_list = new ArrayList<>();
                    for (int i = 0; i < region_gu.length(); i++) {
                        String str_gu = String.valueOf(region_gu.get(i));
                        gu_list.add(str_gu);
                    }
                    foretViewDTO.setForet_region_gu(gu_list);

                    JSONArray tag = temp.getJSONArray("tag");
                    List<String> tag_list = new ArrayList<>();
                    for (int i = 0; i < tag.length(); i++) {
                        String str_tag = String.valueOf(tag.get(i));
                        tag_list.add(str_tag);
                    }
                    foretViewDTO.setForet_tag(tag_list);

                    JSONArray member = temp.getJSONArray("member");
                    List<Integer> member_list = new ArrayList<>();
                    for (int i = 0; i < member.length(); i++) {
                        int mem = (int) member.get(i);
                        member_list.add(mem);
                    }
                    foretViewDTO.setMember(member_list);

                    foretViewDTO.setRank(json.getString("rank"));
                    Log.d("[TEST]", "포레정보 가져옴");
                    Log.d("[TEST]", "foretDTO.getId => " + foretViewDTO.getId());
                    Log.d("[TEST]", "foretDTO.getPhoto => " + foretViewDTO.getPhoto());
                    Log.d("[TEST]", "foretDTO.getName => " + foretViewDTO.getName());
                    Log.d("[TEST]", "foretDTO.getIntroduce => " + foretViewDTO.getIntroduce());
                    Log.d("[TEST]", "foretDTO.getLeader_id => " + foretViewDTO.getLeader_id());
                    Log.d("[TEST]", "foretDTO.getForet_region_gu => " + foretViewDTO.getForet_region_gu().size());
                    Log.d("[TEST]", "foretDTO.getForet_region_si => " + foretViewDTO.getForet_region_si().size());
                    Log.d("[TEST]", "foretDTO.getForet_tag => " + foretViewDTO.getForet_tag().size());
                    Log.d("[TEST]", "foretDTO.getRank => " + foretViewDTO.getRank());


                    rank = foretViewDTO.getRank();
                    if (rank.equals("guest")) { // 포레 가입전 - 가입하기
                        button1.setVisibility(View.VISIBLE);
                        noti_layout.setVisibility(View.GONE);
                        board_layout.setVisibility(View.GONE);
                        write_fab_add.setVisibility(View.GONE);
                    } else if (rank.equals("member")) { // 가입한 상태 - 탈퇴하기
                        button2.setVisibility(View.VISIBLE);
                        noti_layout.setVisibility(View.VISIBLE);
                        board_layout.setVisibility(View.VISIBLE);
                        write_fab_add.setVisibility(View.VISIBLE);
                    } else if (rank.equals("leader")) { // 리더 - 수정하기
                        button3.setVisibility(View.VISIBLE);
                        noti_layout.setVisibility(View.VISIBLE);
                        board_layout.setVisibility(View.VISIBLE);
                        write_fab_add.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ViewForetActivity.this, "언노운", Toast.LENGTH_SHORT).show();
                    }
                    dataSetting();
                } else {
                    Log.d("[TEST]", "포레정보 못가져옴");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ViewForetActivity.this, "ViewForetResponse 통신 실패", Toast.LENGTH_SHORT).show();
            Log.e("[test]", "ViewForetResponse 통신 실패 오류코드 " + statusCode + "/ error? " + error.getStackTrace());
        }
    }

    //초기 데이터 리딩 노티스
    class NoticeResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            Log.d("[TEST]", "NoticeResponse onStart() 호출");
        }

        @Override
        public void onFinish() {
            Log.d("[TEST]", "NoticeResponse onFinish() 호출");
            viewForetBoardAdapter = new ViewForetBoardAdapter(ViewForetActivity.this, memberDTO, foretBoardDTOList);
            listView_notice.setLayoutManager(new LinearLayoutManager(ViewForetActivity.this));
            listView_notice.setAdapter(viewForetBoardAdapter);
            viewForetBoardAdapter.setItems(foretBoardDTOList);

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
            Toast.makeText(ViewForetActivity.this, "NoticeResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }
    //초기 데이터 리딩 보드

    class BoardResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            Log.d("[TEST]", "BoardResponse onStart() 호출");
        }

        @Override
        public void onFinish() {
            Log.d("[TEST]", "BoardResponse onFinish() 호출");
            boardViewAdapter = new BoardViewAdapter(ViewForetActivity.this, memberDTO, foretBoardDTOList);
            board_list.setLayoutManager(new LinearLayoutManager(ViewForetActivity.this));
            board_list.setAdapter(boardViewAdapter);
            boardViewAdapter.setItems(foretBoardDTOList);

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

    //신규 가입시
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
                    button2.setVisibility(View.VISIBLE);
                    button1.setVisibility(View.GONE);
                    write_fab_add.setVisibility(View.VISIBLE);
                    noti_layout.setVisibility(View.VISIBLE);
                    board_layout.setVisibility(View.VISIBLE);

                    //파이어 베이스 연동
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    myUid = user.getUid();
                    String timestamp = "" + System.currentTimeMillis();
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("uid", myUid);
                    hashMap.put("joinedDate", timestamp);
                    hashMap.put("participantName", myNickName);

                    hisUid = "";


                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
                    ref.child(foretname).child("participants").child(user.getUid()).setValue(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ViewForetActivity.this, "채팅방 생성 성공", Toast.LENGTH_LONG).show();

                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Groups");
                                    ref2.child(foretname).child("participants").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                hisUid = ds.getKey();
                                                //
                                                notify = true;
                                                if (!myUid.equals(ds.getKey())) {
                                                    //알림설정
                                                    updateNewItem("GROUP_NEW_ITEM", myUid, hisUid, "새로운 멤버가 가입하였습니다.", "" + System.currentTimeMillis());
                                                    String msg = "신규 Foret 멤버 가입! 환영해주세요";
                                                    //설정
                                                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
                                                    database.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            ModelUser user = snapshot.getValue(ModelUser.class);

                                                            if (notify) {
                                                                sendNotification(hisUid, user.getNickname(), msg);
                                                            }
                                                            notify = false;
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ViewForetActivity.this, "채팅방 등록 실패" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

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

    //탈퇴시
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
                    button2.setVisibility(View.VISIBLE);
                    button1.setVisibility(View.GONE);
                    noti_layout.setVisibility(View.GONE);
                    board_layout.setVisibility(View.GONE);
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


    // 유저 접송 상태
    private void checkUserStatus() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

        } else {
            // 유저가 로그인 안한 상태
            Toast.makeText(this, "오프라인 상태입니다.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // 새글데이터
    public void updateNewItem(String type, String sender, String receiver, String content, String time) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notify");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", type);
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("content", content);
        hashMap.put("time", time);
        hashMap.put("isSeen", false);

        ref.child(receiver).push().setValue(hashMap);
    }

    // 알림 발송 설정.
    private void sendNotification(String hisUid, String nickname, String message) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUid); // 상대 찾기
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    // 상대 토큰값 토큰화 하기
                    Token token = ds.getValue(Token.class);

                    // 데이터 셋팅
                    Data data = new Data(myUid, message, foretname + "의 포레 알림", hisUid,
                            R.drawable.foret_logo);

                    // 보내는 사람 셋팅
                    Sender sender = new Sender(data, token.getToken());
                    // 발송
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(
                                        Call<Response> call,
                                        retrofit2.Response<com.example.foret_app_prototype.activity.notify.Response> response) {
                                    // Toast.makeText(ChatActivity.this,""+response.message(),Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}