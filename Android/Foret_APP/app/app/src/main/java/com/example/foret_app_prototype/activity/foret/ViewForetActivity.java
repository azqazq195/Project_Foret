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
import com.example.foret_app_prototype.adapter.foret.ForetBoardAdapter;
import com.example.foret_app_prototype.model.Foret;
import com.example.foret_app_prototype.model.ForetBoard;
import com.example.foret_app_prototype.model.Member;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class ViewForetActivity extends AppCompatActivity implements View.OnClickListener {
    Member member;
    Foret foret;
    List<Foret> foretList;
    List<ForetBoard> foretBoardList;

    ForetBoardAdapter foretBoardAdapter;

    Toolbar toolbar;
    Intent intent;
    RecyclerView listView_sche, listView_notice;
    TextView textView_tag, textView_region, textView_intro, textView_member, textView_master, textView_foretName, textView_date;
    Button button1, button2, button3;
    ImageView imageView_profile;

    int GRADE = 2; //포레에 가입한 멤버/마스터/가입안함 구분 코드(0=가입안함, 1=가입함, 2=마스터)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_foret);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        member = (Member) getIntent().getSerializableExtra("member");
        foret = (Foret) getIntent().getSerializableExtra("foret");
        foretList = (List<Foret>) getIntent().getSerializableExtra("foretList");
        foretBoardList = (List<ForetBoard>) getIntent().getSerializableExtra("foretBoardList");

        //GRADE = getIntent().getIntExtra("GRADE", 0);
        if(GRADE == 0) { //포레 가입전
            button1.setVisibility(View.VISIBLE);
        } else if(GRADE == 1) { //가입한 상태
            button2.setVisibility(View.VISIBLE);
        } else if(GRADE == 2) { //마스터
            button3.setVisibility(View.VISIBLE);
        }

        dataSetting();

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    private void dataSetting() {
        //Glide.with(this).load(filePath).into(imageView_profile);
        imageView_profile.setImageResource(foret.getForetImage());
        textView_foretName.setText(foret.getName());
        textView_tag.setText(Arrays.toString(foret.getForet_tag()));
        textView_region.setText(Arrays.toString(foret.getForet_region()));
        textView_member.setText(foret.getMember().length + "/" + foret.getMax_member());
        textView_master.setText("포레 마스터 : " + foret.getLeader());
        textView_date.setText(foret.getReg_date());
        String intro = "";
        for (int a=0; a<=6; a++) {
            intro += foret.getIntroduce() + " ";
        }
        textView_intro.setText(intro);

        foretBoardAdapter = new ForetBoardAdapter(this, member, foretList, foretBoardList);
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
                intent.putExtra("member", member);
                intent.putExtra("foret", foret);
                intent.putExtra("foretBoardList", (Serializable) foretBoardList);
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
            case R.id.button1 : //가입하고 있지 않을 때 ->가입하기버튼 누를시 가입
                joinDialog();
                break;
            case R.id.button2 :  //가입하고 있을 때 ->가입중 상태->누르면 포레 게시판 홈으로 이동
                intent = new Intent(this, ListForetBoardActivity.class);
                intent.putExtra("member", member);
                intent.putExtra("foret", foret);
                intent.putExtra("foretBoardList", (Serializable) foretBoardList);
                startActivity(intent);
                break;
            case R.id.button3 : //가입하고 있고, 관리자일 때 ->수정하기 화면으로 이동
                intent = new Intent(this, EditForetActivity.class);
                intent.putExtra("foret", foret);
                startActivityForResult(intent, 119);
                break;
        }
    }

    private void joinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("가입 하시겠습니까?");
        builder.setPositiveButton("가입", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //포레 가입
                button2.setVisibility(View.VISIBLE);
                button1.setVisibility(View.GONE);
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case 119:
                    foret = (Foret) data.getSerializableExtra("foret");
                    dataSetting();
                    break;
            }
        }
    }
}