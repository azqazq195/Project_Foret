package com.example.foret_app_prototype.activity.foret.board;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.adapter.foret.BoardViewPagerAdapter;
import com.example.foret_app_prototype.adapter.foret.CommentsAdapter;
import com.example.foret_app_prototype.model.Foret;
import com.example.foret_app_prototype.model.ForetBoard;
import com.example.foret_app_prototype.model.ForetBoardComment;
import com.example.foret_app_prototype.model.Member;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReadForetBoardActivity extends AppCompatActivity implements View.OnClickListener {
    // 데이터
    Foret foret;
    ForetBoard foretBoard;
    List<ForetBoard> foretBoardList;
    List<ForetBoardComment> commentList;
    ForetBoardComment foretBoardComment;
    Member member;
    int value;

    ViewPager imageViewpager;
    TabLayout tab_layout;
    TextView textView_boardName, textView_subject, textView_writer, textView_content,
            textView_date, textView_like, textView_comment_count, textView_reply;
    EditText editText_comment;
    ToggleButton likeButton;
    Button button_input;
    ImageView button_cancel;
    CircleImageView image_profile;
    RecyclerView comment_listView;
    RecyclerView.LayoutManager layoutManager;

    BoardViewPagerAdapter boardViewPagerAdapter;
    CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_foret_board);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기존 타이틀 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        member = (Member) getIntent().getSerializableExtra("member");
        foret = (Foret) getIntent().getSerializableExtra("foret");
        foretBoard = (ForetBoard) getIntent().getSerializableExtra("foretBoard");
        foretBoardList = (List<ForetBoard>) getIntent().getSerializableExtra("foretBoardList");
        Log.d("[TEST]", "foretBoardList.size() => " + foretBoardList.size());
        value = getIntent().getIntExtra("value", value);
        Log.d("[TEST]", "foretBoardList.get(value) => " + foretBoardList.get(value));

        addComment(); // 셋댓글
        setFindById(); // 객체 초기화
        setViewContent();
    }

    private void setFindById() {
        imageViewpager = findViewById(R.id.imageViewpager);
        image_profile = findViewById(R.id.image_profile);
        textView_boardName = findViewById(R.id.textView_boardName);
        textView_subject = findViewById(R.id.textView_subject);
        textView_writer = findViewById(R.id.textView_writer);
        textView_content = findViewById(R.id.textView_content);
        textView_date = findViewById(R.id.textView_date);
        textView_like = findViewById(R.id.textView_like);
        textView_comment_count = findViewById(R.id.textView_comment_count);
        textView_reply = findViewById(R.id.textView_reply);
        button_cancel = findViewById(R.id.button_cancel);
        editText_comment = findViewById(R.id.editText_comment);
        likeButton = findViewById(R.id.likeButton);
        button_input = findViewById(R.id.button_input);
        comment_listView = findViewById(R.id.comment_listView);
        imageViewpager = findViewById(R.id.imageViewpager);
        tab_layout = findViewById(R.id.tab_layout);
        boardViewPagerAdapter = new BoardViewPagerAdapter(this, foretBoardList);

        layoutManager = new LinearLayoutManager(this);

        likeButton.setOnClickListener(this);
        button_input.setOnClickListener(this);
    }

    private void setViewContent() {
        textView_boardName.setText(foret.getName() + " 게시판");
        image_profile.setImageResource(foretBoard.getMemberImage());
        textView_writer.setText(foretBoard.getWriter());
        textView_date.setText(foretBoard.getReg_date());
        textView_subject.setText(foretBoard.getSubject());
        textView_content.setText(foretBoard.getContent());

        // 뷰페이저
        imageViewpager.setAdapter(boardViewPagerAdapter);
        tab_layout.setupWithViewPager(imageViewpager, true);

        textView_like.setText("공감("+ foretBoard.getLike_count()+")");

//        textView_comment_count.setText("댓글("+ foretBoard.getComment_count()+")");
        textView_comment_count.setText("댓글("+ commentList.size() +")");

        // 댓글 리스트
        commentsAdapter = new CommentsAdapter(commentList);
        comment_listView.setLayoutManager(layoutManager);
        comment_listView.setAdapter(commentsAdapter);
//        commentsAdapter.setItems(commentList);

        // 대댓글 달때만 보이게, '대댓글 작성자'님께로 추후 수정
        textView_reply.setVisibility(View.GONE);
        button_cancel.setVisibility(View.GONE);
//        textView_reply.setText(foretBoard.getWriter() + "님께 답글 작성중입니다...");
    }

    private void inputComment() {
        String comment_content = editText_comment.getText().toString().trim();
        if(comment_content.equals("")) {
            Toast.makeText(this,"댓글을 작성해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        foretBoardComment = new ForetBoardComment(comment_content, member.getName(), "2020.11.27", member.getImage());
        commentList.add(foretBoardComment);
//        comment_listView.setLayoutManager(layoutManager);
//        commentsAdapter.notifyDataSetChanged();
//        layoutManager.scrollToPosition(commentList.size()-1);
        comment_listView.setAdapter(commentsAdapter);
//        commentsAdapter.setItems(commentList);
        textView_comment_count.setText("댓글("+ commentList.size() +")");
        editText_comment.setText("");
    }

    // 버튼 이벤트
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.likeButton: // 좋아요 버튼
                boolean check = likeButton.isChecked();
                if(check) {
                    Toast.makeText(this, "공감", Toast.LENGTH_SHORT).show();
                    likeButton.setBackgroundDrawable(v.getResources().
                            getDrawable(R.drawable.like));
                    foretBoard.setLike_count(foretBoard.getLike_count()+1);
                    textView_like.setText("공감("+ foretBoard.getLike_count()+")");
                } else {
                    Toast.makeText(this, "공감 취소", Toast.LENGTH_SHORT).show();
                    likeButton.setBackgroundDrawable(v.getResources().
                            getDrawable(R.drawable.like_off));
                    foretBoard.setLike_count(foretBoard.getLike_count()-1);
                    textView_like.setText("공감("+ foretBoard.getLike_count()+")");
                }
                break;
            case R.id.button_input: // 댓글 입력 버튼
                inputComment();
                break;
        }
    }

    // 툴바 메뉴 버튼 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.btn_modify : // 수정 버튼
                Toast.makeText(this, "게시판 수정", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, EditForetBoardActivity.class);
                intent.putExtra("member", member);
                intent.putExtra("foretBoard", foretBoard);
                startActivityForResult(intent, 123);
                break;
            case R.id.btn_delete : // 삭제 버튼
                getDialog();
                break;
            case android.R.id.home : // 뒤로가기 버튼
                intent = new Intent();
                intent.putExtra("foretBoardList", (Serializable) foretBoardList);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // 메뉴 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 수행 표시줄에 항목이 있는 경우 이 항목이 추가됨.
        getMenuInflater().inflate(R.menu.edit_toolbar, menu);
        return true;
    }

    private void getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게시글 삭제");
        builder.setMessage("정말 삭제하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setCancelable(false);

        // 삭제
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                foretBoardList.remove(value);
                Log.d("[TEST]", "foretBoardList.size() => " + foretBoardList.size());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("foretBoardList", (Serializable) foretBoardList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        // 취소
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"삭제 취소",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case 123:
                    foretBoard = (ForetBoard) data.getSerializableExtra("foretBoard");
                    foretBoardList.get(value).setType(foretBoard.getType());
                    foretBoardList.get(value).setSubject(foretBoard.getSubject());
                    foretBoardList.get(value).setContent(foretBoard.getContent());
                    setViewContent();
                    break;
            }
        }
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
