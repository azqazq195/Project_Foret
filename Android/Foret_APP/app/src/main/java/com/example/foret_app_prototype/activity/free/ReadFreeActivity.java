package com.example.foret_app_prototype.activity.free;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.login.SessionManager;
import com.example.foret_app_prototype.adapter.free.CommentListFreeBoardAdapter;
import com.example.foret_app_prototype.model.ForetBoard;
import com.example.foret_app_prototype.model.ForetBoardComment;
import com.example.foret_app_prototype.response.CommentListResponse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ReadFreeActivity extends AppCompatActivity implements OnClickListener, CommentListFreeBoardAdapter.CommentClickListener {

    Toolbar toolbar_writer, toolbar_noWriter;
    TextView textView_writer, textView_like, textView_subject, textView_date, textView_seq,
            textView_reply, textView_content, textView_comment;
    ToggleButton likeButton;
    ImageView button_cancel; //답글 닫기
    EditText editText_comment;
    RecyclerView recyclerView;
    Button button_input; //답글 달기 버튼
    int memberID;
    Intent intent;
    AsyncHttpClient client;
    CommentListResponse listResponse;
    InsertCommentResponse writeCommentResponse;
    InsertReCommentResponse writeReplyResponse;
    ViewFreeBoardResponse readBoardResponse;
    CommentListFreeBoardAdapter adapter;
    List<ForetBoardComment> commentlist;
    ForetBoard foretBoard;
    ForetBoardComment foretBoardComment;
    int like_count;
    int comment_count;
    InputMethodManager inputMethodManager;
    int position;
    String target;
    boolean replying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_free);
        //툴바 설정
        toolbar_writer = findViewById(R.id.toolbar_writer);
        toolbar_noWriter = findViewById(R.id.toolbar_noWriter);
        setSupportActionBar(toolbar_writer); //테스트용이라 작성자용 툴바만 설정
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView_writer = findViewById(R.id.textView_writer);
        textView_like = findViewById(R.id.textView_like); //좋아요 개수
        textView_subject = findViewById(R.id.textView_subject);
        textView_date = findViewById(R.id.textView_date);
        textView_seq = findViewById(R.id.textView_seq);
        textView_reply = findViewById(R.id.textView_reply); //OO님께 댓글 작성중입니다 창뜨기
        textView_comment = findViewById(R.id.textView_comment);
        textView_content = findViewById(R.id.textView_content);
        likeButton = findViewById(R.id.likeButton);
        button_cancel = findViewById(R.id.button_cancel); //답글달기 취소
        editText_comment = findViewById(R.id.editText_comment); //코멘트창
        recyclerView = findViewById(R.id.recyclerView);
        button_input = findViewById(R.id.button_input);

        inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        SessionManager sessionManager = new SessionManager(this);
        memberID = sessionManager.getSession();

        commentlist = new ArrayList<>();
        client = new AsyncHttpClient();
        listResponse = new CommentListResponse();
        writeCommentResponse = new InsertCommentResponse();
        writeReplyResponse = new InsertReCommentResponse();
        readBoardResponse = new ViewFreeBoardResponse();
        
        foretBoard = (ForetBoard) getIntent().getSerializableExtra("foretBoard");
        like_count = foretBoard.getLike_count();
        comment_count = foretBoard.getComment_count();

        setDataBoard(foretBoard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        textView_reply.setVisibility(View.GONE);
        button_cancel.setVisibility(View.GONE);
        button_cancel.setOnClickListener(this); //답글 작성 취소
        button_input.setOnClickListener(this); //답글쓰기 ->서버 리셋할 것
        likeButton.setOnClickListener(this); //좋아요버튼
    }

    //좋아요 수 변화 때문에 반드시 서버에서 데이터 불러오거나 할것
    private void setDataBoard(ForetBoard foretBoard) {
        textView_writer.setText(foretBoard.getWriter());
        textView_subject.setText(foretBoard.getSubject());
        textView_content.setText(foretBoard.getContent());
        textView_like.setText(foretBoard.getLike_count()+"");
        textView_seq.setText(foretBoard.getId()+"");
        textView_date.setText(foretBoard.getReg_date());
        textView_comment.setText(foretBoard.getComment_count()+"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //불러오기 요청
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
            case R.id.btn_modify:
                intent = new Intent(this, EditFreeActivity.class);
                intent.putExtra("수정할 데이터", "수정할 데이터");
                startActivity(intent);
                break;
            case R.id.btn_delete :
                showDeleteDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("삭제 하시겠습니까?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //삭제하기
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel : //답글 작성 취소
                textView_reply.setVisibility(View.GONE);
                button_cancel.setVisibility(View.GONE);
                editText_comment.setText("");
                replying = false;
                break;
            case R.id.button_input : //댓글 쓰기
                inputComment();
                break;
            case R.id.likeButton : //좋아요 처리
                if(likeButton.isChecked()) {
                    like_count++;
                    textView_like.setText(like_count+"");
                } else {
                    like_count--;
                    textView_like.setText(like_count + "");
                }
                break;
        }
    }

    private void inputComment() {
        RequestParams params = new RequestParams();
        String url = "";
        params.put("board_id", foretBoard.getId());
        params.put("writer", memberID);
        params.put("content", editText_comment.getText().toString().trim());
        if(!replying) { //대댓글쓰기
            params.put("comment_id", foretBoardComment.getGroup_no());
            client.post("http://34.72.240.24:8085/foret/comment/comment_insert.do", params, writeCommentResponse);
        } else {
            client.post("http://34.72.240.24:8085/foret/comment/recomment_insert.do", params, writeReplyResponse);
        }
    }

    //답글버튼 눌렀을 때
    @Override
    public void onReplyButtonClick(View v, String target, int position, boolean reply) {
        if(reply) {
            textView_reply.setText(target + "님에게 답글 작성중 입니다.");
            editText_comment.setText("@" + target + " ");
            textView_reply.setVisibility(View.VISIBLE);
            button_cancel.setVisibility(View.VISIBLE);
            this.position = position;
            this.target = target;
            replying = true;
        }
    }

    //수정버튼 눌렀을 때
    @Override
    public void onModifyButtonClick(View v, boolean modify) {
        if(modify) {
            textView_reply.setVisibility(View.GONE);
            button_cancel.setVisibility(View.GONE);
            editText_comment.setVisibility(View.GONE);
            button_input.setVisibility(View.GONE);
        } else {
            textView_reply.setVisibility(View.VISIBLE);
            button_cancel.setVisibility(View.VISIBLE);
            editText_comment.setVisibility(View.VISIBLE);
            button_input.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeleteButtonClick(boolean delete) {
        if(delete) {
            comment_count --;
        }
    }

    class ViewFreeBoardResponse extends AsyncHttpResponseHandler { //글 상세보기기

       @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }

    class CommentListResponse extends AsyncHttpResponseHandler { //댓글목록 불러오기 : http://34.72.240.24:8085/foret/search/commentList.do

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getString("RT").equals("OK")) {
                    JSONArray board = json.getJSONArray("board");
                    for(int a=0; a<board.length(); a++) {
                        JSONObject object = board.getJSONObject(a);
                        foretBoardComment = new ForetBoardComment();
                        foretBoardComment.setReg_date(object.getString("reg_date"));
                        foretBoardComment.setBoard_id(object.getInt("board_id"));
                        foretBoardComment.setId(object.getInt("id"));
                        foretBoardComment.setGrade(object.getString("content"));
                        foretBoardComment.setGroup_no(object.getInt("group_no"));
                        commentlist.add(foretBoardComment);
                        adapter = new CommentListFreeBoardAdapter(commentlist, ReadFreeActivity.this, memberID);
                        adapter.setCommentClickListener(ReadFreeActivity.this);
                        recyclerView.setAdapter(adapter);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ReadFreeActivity.this, "댓글 목록을 불러 올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    class InsertCommentResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getString("commentRT").equals("OK")) {
                    Toast.makeText(ReadFreeActivity.this, "댓글 작성 성공", Toast.LENGTH_SHORT).show();
                    //후 서버 다시 요청->리스트 정렬
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ReadFreeActivity.this, "댓글 작성 실패", Toast.LENGTH_SHORT).show();
        }
    }

    class InsertReCommentResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getString("commentRT").equals("OK")) {
                    Toast.makeText(ReadFreeActivity.this, "댓글 작성 성공", Toast.LENGTH_SHORT).show();
                    //후 서버 다시 요청->리스트 정렬
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ReadFreeActivity.this, "댓글 작성 실패", Toast.LENGTH_SHORT).show();
        }
    }
}