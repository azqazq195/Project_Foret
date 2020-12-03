package com.example.foret_app_prototype.activity.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.model.MemberDTO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    MemberDTO memberDTO;
    TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, button_out;
    ImageView profile;
    String region = "";
    String tag = "";
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        memberDTO = (MemberDTO) getIntent().getSerializableExtra("memberDTO");
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        button_out = findViewById(R.id.button_out);
        profile = findViewById(R.id.profile);

        client = new AsyncHttpClient();

        setData();

        button_out.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //서버에서 데이터 받아와서 setData();
    }

    private void setData() {
        region = (memberDTO.getRegion_si().toString()+","+memberDTO.getRegion_gu()).replace("[", "").replace("]","");
        for (int a=0; a<memberDTO.getTag().size(); a++) {
            tag += "#"+memberDTO.getTag().get(a)+" ";
        }
        textView1.setText(memberDTO.getNickname());
        textView2.setText(memberDTO.getEmail());
        textView3.setText(memberDTO.getId());
        textView4.setText(memberDTO.getBirth());
        textView5.setText(memberDTO.getReg_date());
        textView6.setText(region);
        textView7.setText(tag);
        Glide.with(this).load("memberDTO.getPhoto()").into(profile);
    }

    @Override //메뉴 설정
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.modify_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override //메뉴 이벤트 처리
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modify :
                Intent intent = new Intent(this, EditMyInfoActivity.class);
                intent.putExtra("memberDTO", memberDTO);
                intent.putExtra("region", region);
                intent.putExtra("tag", tag);
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
            case R.id.button_out :
                dialogOUT();
                break;
        }
    }

    private void dialogOUT() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("정말 탈퇴 하시겠습니까?\n(삭제된 정보는 복구할 수 없습니다.)");
        builder.setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestParams params = new RequestParams();
                params.put("id", memberDTO.getId());
                client.post("http://34.72.240.24:8085/foret/member/member_delete.do", params);
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}