package com.example.foret_app_prototype.activity.foret;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.example.foret_app_prototype.activity.foret.board.ListForetBoardActivity;

public class ViewForetActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView_tag, textView_region, textView_intro, textView_member, textView_master, textView_foretName, textView_date;
    Button button1, button2, button3;
    ImageView imageView_profile;
    Toolbar toolbar;
    Intent intent;
    int GRADE=2; //포레에 가입한 멤버/마스터/가입안함 구분 코드(0=가입안함, 1=가입함, 2=마스터)

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
        textView_foretName = findViewById(R.id.textView_foretName);
        textView_tag = findViewById(R.id.textView_tag);
        textView_region = findViewById(R.id.textView_region);
        textView_member = findViewById(R.id.textView_member);
        textView_master = findViewById(R.id.textView_master);
        textView_date = findViewById(R.id.textView_date);
        textView_intro = findViewById(R.id.textView_intro);

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
        textView_foretName.setText("선미님 착하당");
        textView_tag.setText("#선미님, #최고, #착함");
        textView_region.setText("성수동");
        textView_member.setText("가입한인원수/최대인원수");
        textView_master.setText("포레 마스터 : SANDY");
        textView_date.setText("2020.12.02");
        String intro = "";
        for (int a=0; a<=6; a++) { //태그도 이런 식으로 "#태그, " .... 이렇게 쭉 이어지게 출력해주세요)
            intro += "세상에서 제일 이뿐 선미님 ";
        }
        textView_intro.setText(intro);
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
                intent.putExtra("포레정보", "포레정보");
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
                intent.putExtra("포레정보", "포레정보");
                startActivity(intent);
                break;
            case R.id.button3 : //가입하고 있고, 관리자일 때 ->수정하기 화면으로 이동
                intent = new Intent(this, EditForetActivity.class);
                intent.putExtra("포레정보", "포레정보");
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
                //포레 가입
                button2.setVisibility(View.VISIBLE);
                button1.setVisibility(View.GONE);
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}