package com.example.foret_app_prototype.activity.foret;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.foret.board.ListForetBoardActivity;
import com.example.foret_app_prototype.activity.home.HomeFragment;
import com.example.foret_app_prototype.activity.login.SessionManager;
import com.example.foret_app_prototype.adapter.foret.ForetBoardAdapter;
import com.example.foret_app_prototype.model.Foret;
import com.example.foret_app_prototype.model.ForetBoard;
import com.example.foret_app_prototype.model.ForetBoardDTO;
import com.example.foret_app_prototype.model.ForetDTO;
import com.example.foret_app_prototype.model.Member;
import com.example.foret_app_prototype.model.MemberDTO;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ViewForetActivity extends AppCompatActivity implements View.OnClickListener {
    MemberDTO memberDTO;
    ForetDTO foretDTO;
    ForetBoardDTO foretBoardDTO;
    List<ForetBoardDTO> foretBoardDTOList;

    ForetBoardAdapter foretBoardAdapter;

    Toolbar toolbar;
    Intent intent;
    RecyclerView listView_sche, listView_notice;
    TextView textView_tag, textView_region, textView_intro, textView_member, textView_master, textView_foretName, textView_date;
    Button button1, button2, button3;
    ImageView imageView_profile;

    String url = null;
    AsyncHttpClient client;
    ViewForetResponse viewForetResponse;
    ForetBoardResponse foretBoardResponse;

    int GRADE = 0; //포레에 가입한 멤버/마스터/가입안함 구분 코드(0=가입안함, 1=가입함, 2=마스터)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_foret);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFindId(); // 객체 초기화

        getMember(); // 회원 정보 가져오기

        foretDTO = (ForetDTO) getIntent().getSerializableExtra("foretDTO");

        //GRADE = getIntent().getIntExtra("GRADE", 0);

        if(GRADE == 0) { //포레 가입전
            button1.setVisibility(View.VISIBLE);
        } else if(GRADE == 1) { //가입한 상태
            button2.setVisibility(View.VISIBLE);
        } else if(GRADE == 2) { //마스터
            button3.setVisibility(View.VISIBLE);
        }

        dataSetting(); // 데이터 세팅


    }

    private void getMember() {
        SessionManager sessionManager = new SessionManager(this);
        String email = sessionManager.getSessionEmail();
        String password = sessionManager.getSessionPassword();
        url = "http://34.72.240.24:8085/foret/search/member.do";
        client = new AsyncHttpClient();
        viewForetResponse = new ViewForetResponse();
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        client.post(url, params, viewForetResponse);
    }

    private void getFindId() {
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        imageView_profile = findViewById(R.id.imageView_profile);
        textView_foretName = findViewById(R.id.textView_foretName);
        textView_tag = findViewById(R.id.textView_tag);
        textView_region = findViewById(R.id.textView_region);
        textView_member = findViewById(R.id.textView_member);
        textView_master = findViewById(R.id.textView_master);
        textView_date = findViewById(R.id.textView_date);
        textView_intro = findViewById(R.id.textView_intro);
        listView_notice = findViewById(R.id.listView_notice);
        listView_sche = findViewById(R.id.listView_sche);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    private void dataSetting() {
        if(foretDTO.getForet_photo().equals("") && foretDTO.getForet_photo().equals(null)) {
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

        foretBoardAdapter = new ForetBoardAdapter(this, foretBoardDTOList);
        listView_notice.setLayoutManager(new LinearLayoutManager(this));
        listView_notice.setAdapter(foretBoardAdapter);

        listView_sche.setLayoutManager(new LinearLayoutManager(this));
        listView_sche.setAdapter(foretBoardAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.basic_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_next :
                intent = new Intent(this, ListForetBoardActivity.class);
                intent.putExtra("foretDTO", foretDTO);
                startActivity(intent);
                break;
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
            case R.id.button2 : // 가입하고 있을 때 -> 가입중 상태 -> 누르면 포레 게시판 홈으로 이동
                intent = new Intent(this, ListForetBoardActivity.class);
                intent.putExtra("foretDTO", foretDTO);
                startActivity(intent);
                break;
            case R.id.button3 : // 가입하고 있고, 관리자일 때 -> 수정하기 화면으로 이동
                intent = new Intent(this, EditForetActivity.class);
                intent.putExtra("foretDTO", foretDTO);
                startActivity(intent);
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
                button2.setVisibility(View.VISIBLE);
                button1.setVisibility(View.GONE);
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
            url = "http://34.72.240.24:8085/search/homeFragement.do";
            foretBoardResponse = new ForetBoardResponse();
            RequestParams params = new RequestParams();
            params.put("foret_id", foretDTO.getForet_id());
            client.post(url, params, foretBoardResponse);

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            Gson gson = new Gson();
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if(RT.equals("OK")) {
                    JSONArray member = json.getJSONArray("member");
                    JSONObject temp = member.getJSONObject(0);
                    memberDTO = gson.fromJson(temp.toString(), MemberDTO.class);
                    Toast.makeText(ViewForetActivity.this, "회원정보 가져옴", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ViewForetActivity.this, "회원정보 못가져옴", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ViewForetActivity.this, "ViewForetResponse 통신 실패", Toast.LENGTH_SHORT).show();
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
                if(RT.equals("OK")) {
                    JSONArray board = json.getJSONArray("board");
                    JSONObject temp = board.getJSONObject(0);
                    for(int i=0; i<board.length(); i++) {
                        foretBoardDTO = gson.fromJson(temp.toString(), ForetBoardDTO.class);
                        foretBoardDTOList.add(foretBoardDTO);
                    }
                    Log.d("[TEST]", "foretBoardDTOList.size() => " + foretBoardDTOList.size());
                    Toast.makeText(ViewForetActivity.this, "포레 게시판 정보 가져옴", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ViewForetActivity.this, "포레 게시판 정보 못가져옴", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ViewForetActivity.this, "ForetBoardResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }
}