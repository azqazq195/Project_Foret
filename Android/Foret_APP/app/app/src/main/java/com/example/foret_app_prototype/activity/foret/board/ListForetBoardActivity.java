package com.example.foret_app_prototype.activity.foret.board;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.adapter.foret.BoardViewAdapter;
import com.example.foret_app_prototype.model.Foret;
import com.example.foret_app_prototype.model.ForetBoard;
import com.example.foret_app_prototype.model.ForetBoardComment;
import com.example.foret_app_prototype.model.Member;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListForetBoardActivity extends AppCompatActivity {
    Member member;
    Foret foret;
    List<ForetBoard> foretBoardList;
    List<ForetBoardComment> commentList;

    TextView foretTitle;
    RecyclerView board_list;
    BoardViewAdapter boardViewAdapter;

    FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_foret_board);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기존 타이틀 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        addFab(); // 플로팅 버튼

        member = (Member) getIntent().getSerializableExtra("member");
        foret = (Foret) getIntent().getSerializableExtra("foret");
        foretBoardList = (List<ForetBoard>) getIntent().getSerializableExtra("foretBoardList");

        addComment();

        foretTitle = findViewById(R.id.foretTitle);
        foretTitle.setText(foret.getName());

        board_list = findViewById(R.id.board_list);
        boardViewAdapter = new BoardViewAdapter(this, member, foret, foretBoardList, commentList);

        board_list.setLayoutManager(new LinearLayoutManager(ListForetBoardActivity.this));
        board_list.setAdapter(boardViewAdapter);

        boardViewAdapter.setItems(foretBoardList, commentList);

//        boardViewAdapter.setOnClickListener(new BoardViewAdapter.OnClickListener() {
//            @Override
//            public void onClick(View v, boolean check, int position) {
//                if(check) {
//                    Toast.makeText(ListForetBoardActivity.this, "공감",
//                            Toast.LENGTH_SHORT).show();
//                    foretBoardList.get(position).setLike_count(foretBoardList.get(position).getLike_count()+1);
//                    boardViewAdapter.notifyItemChanged(position);
//                } else {
//                    Toast.makeText(ListForetBoardActivity.this, "공감 취소",
//                            Toast.LENGTH_SHORT).show();
//                    foretBoardList.get(position).setLike_count(foretBoardList.get(position).getLike_count()-1);
//                    boardViewAdapter.notifyItemChanged(position);
//                }
//            }

//            @Override
//            public void onItemClick(View v, int position) {
//                Log.d("[TEST]", "foretView position => " + position);
//                int value = position;
//                if(value == position) {
//                    ForetBoard foretBoard = foretBoardList.get(value);
//                    Log.d("[TEST]", "foretBoard => " + foretBoard);
//                    Intent intent = new Intent(ListForetBoardActivity.this, ReadForetBoardActivity.class);
//                    intent.putExtra("member", member);
//                    intent.putExtra("foret", foret);
//                    intent.putExtra("foretBoard", foretBoard);
//                    intent.putExtra("foretBoardList", (Serializable) foretBoardList);
//                    intent.putExtra("value", value); // 리스트 인덱스값
//                    startActivityForResult(intent, 333);
//                }
//            }
//        });
    }

    // 플로팅 버튼
    private void addFab() {
        fab_add = findViewById(R.id.fab_add);
        fab_add.bringToFront();
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListForetBoardActivity.this, WriteForetBoardActivity.class);
                intent.putExtra("foretBoardList", (Serializable) foretBoardList);
                startActivityForResult(intent, 222);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 222: // 새글 작성
                    foretBoardList = (List<ForetBoard>) data.getSerializableExtra("foretBoardList");
                    break;
                case 333:
                    break;
            }
        }
    }

    // 메뉴 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 수행 표시줄에 항목이 있는 경우 이 항목이 추가됨.
        getMenuInflater().inflate(R.menu.board_list_menu, menu);
        return true;
    }

    // 메뉴 버튼 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.board_search : // 검색
                Toast.makeText(this, "검색", Toast.LENGTH_SHORT).show();
                break;
            case R.id.board_type1 : // 일반 정렬
                Toast.makeText(this, "일반 정렬", Toast.LENGTH_SHORT).show();
                break;
            case R.id.board_type2 : // 일정 정렬
                Toast.makeText(this, "일정 정렬", Toast.LENGTH_SHORT).show();
                break;
            case R.id.board_type3 : // 공지 정렬
                Toast.makeText(this, "공지 정렬", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home : // 뒤로가기 버튼
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //////////////////////////////////////댓글 데이터
    private void addComment() {
        commentList = new ArrayList<>();
        commentList.add(new ForetBoardComment("아잇싯팔", "문성하",
                "2020-11-24 2:00", R.drawable.iu1));
        commentList.add(new ForetBoardComment("성하 어서오고", "전성환",
                "2020-11-24 2:00", R.drawable.iu2));
        commentList.add(new ForetBoardComment("아잇싯팔리스트 빵터지넼ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", "이인제",
                "2020-11-24 2:00", R.drawable.iu3));
        commentList.add(new ForetBoardComment("강사님 질문이써여", "임선미",
                "2020-11-24 2:00", R.drawable.iu4));
        commentList.add(new ForetBoardComment("예 말씀하십쇼", "윤강사님",
                "2020-11-24 2:00", R.drawable.iu5));
    }
}