package com.example.foret_app_prototype.activity.free;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.login.SessionManager;
import com.example.foret_app_prototype.adapter.free.CommentListFreeBoardAdapter;
import com.example.foret_app_prototype.model.ForetBoard;
import com.example.foret_app_prototype.model.ForetBoardComment;
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
    RecyclerView comment_listView;
    Button button_input; //답글 달기 버튼

    int memberID;
    Intent intent;
    AsyncHttpClient client;
    ViewFreeBoardResponse viewResponse;
    InsertCommentResponse writeCommentResponse;
    ViewFreeBoardResponse readBoardResponse;
    LikeChangeResponse likeChangeResponse;
    DeleteBoardResponse deleteBoardResponse;

    CommentListFreeBoardAdapter adapter;
    List<ForetBoardComment> commentlist;
    ForetBoard foretBoard;
    ForetBoardComment foretBoardComment;

    int like_count;
    int comment_count;
    InputMethodManager inputMethodManager;
    String target;
    boolean replying = false;
    int initial_likecount; //내가 처음 글을 봤을 때의 라이크 개수 저장 변수

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
        textView_comment = findViewById(R.id.textView_comment_count);
        textView_content = findViewById(R.id.textView_content);
        likeButton = findViewById(R.id.likeButton); //좋아요 버튼
        button_cancel = findViewById(R.id.button_cancel); //답글달기 취소
        editText_comment = findViewById(R.id.editText_comment); //코멘트창
        comment_listView = findViewById(R.id.comment_listView); //댓글이 나올 리사이클러뷰
        button_input = findViewById(R.id.button_input);

        inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        SessionManager sessionManager = new SessionManager(this);
        memberID = sessionManager.getSession();

        commentlist = new ArrayList<>();
        client = new AsyncHttpClient();
        viewResponse = new ViewFreeBoardResponse();
        writeCommentResponse = new InsertCommentResponse();
        readBoardResponse = new ViewFreeBoardResponse();
        likeChangeResponse = new LikeChangeResponse();
        deleteBoardResponse = new DeleteBoardResponse();

        foretBoard = (ForetBoard) getIntent().getSerializableExtra("foretBoard");
        initial_likecount = foretBoard.getLike_count(); //초반 라이크 수 저장
        like_count = foretBoard.getLike_count();
        comment_count = foretBoard.getComment_count();

        setDataBoard(foretBoard);
        comment_listView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        textView_reply.setVisibility(View.GONE);
        button_cancel.setVisibility(View.GONE);
        button_cancel.setOnClickListener(this); //답글 작성 취소
        button_input.setOnClickListener(this); //댓글쓰기 ->서버 리셋할 것
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
        if(foretBoard.isLike()) {
            likeButton.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        commentlist.clear();
        //글 불러오기
        RequestParams params = new RequestParams();
        params.put("id", foretBoard.getId());
        params.put("type", 0);
        client.post("http://34.72.240.24:8085/foret/search/boardSelect.do", params, viewResponse);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(String.valueOf(memberID).equals(foretBoard.getWriter())) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.edit_toolbar, menu);
        }
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
                intent.putExtra("foretBoard", foretBoard);
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
                RequestParams params = new RequestParams();
                params.put("id", foretBoard.getId());
                client.post("http://34.72.240.24:8085/foret/board/board_delete.do", params, deleteBoardResponse);
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

    private void inputComment() { //댓글 쓰기
        foretBoardComment = new ForetBoardComment();
        foretBoardComment.setWriter(String.valueOf(memberID));
        foretBoardComment.setContent(editText_comment.getText().toString().trim());
        foretBoardComment.setBoard_id(foretBoard.getId());
        RequestParams params = new RequestParams();
        String url = "";
        params.put("board_id", foretBoard.getId());
        params.put("writer", memberID);
        params.put("content", editText_comment.getText().toString().trim());
        client.post("http://34.72.240.24:8085/foret/comment/comment_insert.do", params, writeCommentResponse);
    }

    //답글버튼 눌렀을 때
    @Override
    public void onReplyButtonClick(View v, String target, boolean reply) {
        if(reply) {
            textView_reply.setText(target + "님에게 답글 작성중 입니다.");
            editText_comment.setText("@" + target + " ");
            textView_reply.setVisibility(View.VISIBLE);
            button_cancel.setVisibility(View.VISIBLE);
            this.target = target;
            replying = true;
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
            //textView_reply.setVisibility(View.VISIBLE);
            button_cancel.setVisibility(View.VISIBLE);
            editText_comment.setVisibility(View.VISIBLE);
            button_input.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeleteButtonClick(boolean delete) {
        if(delete) {
            comment_count --;
            textView_comment.setText(String.valueOf(comment_count));
        }
    }

    //좋아요 상태 저장
    @Override
    protected void onPause() {
        super.onPause();
        if(initial_likecount != like_count) {
            //처음 라이크 수와 달라졌을 때, 좋아요 상태를 저장해야한다. 첫 라이크보다 적어지면(-1) 좋아요 마이너스.(좋아요 삭제)
            //첫 라이크보다 커지면(+1) 좋아요를 추가한 상태임을 DB에 저장한다.
            RequestParams params = new RequestParams();
            params.put("id", memberID);
            params.put("board_id", foretBoard.getId());
            params.put("type", 0);
            if(initial_likecount > like_count) { //좋아요 수가 1감소함->좋아요 삭제
                client.post("http://34.72.240.24:8085/foret/member/member_board_dislike.do", params, likeChangeResponse);
            } else { //어차피 초반 if문이 처음 좋아요개수가 같지 않을때 였으므로 else를 쓰면 라이크 수가 증가한 경우만 해당
                client.post("http://34.72.240.24:8085/foret/member/member_board_like.do", params, likeChangeResponse);
            }
        }
    }

    class ViewFreeBoardResponse extends AsyncHttpResponseHandler { //글 상세보기기

       @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
           String str = new String(responseBody);
           try {
               JSONObject json = new JSONObject(str);
               if(json.getString("RT").equals("OK")) {
                   JSONArray board = json.getJSONArray("board");
                   JSONObject object = board.optJSONObject(0);
                   foretBoard.setSubject(object.getString("subject"));
                   foretBoard.setLike_count(object.getInt("board_like"));
                   foretBoard.setType(0);
                   foretBoard.setContent(object.getString("content"));
                   foretBoard.setReg_date(object.getString("reg_date"));
                   foretBoard.setComment_count(object.getInt("board_comment"));
                   setDataBoard(foretBoard);
                   if(object.getJSONArray("comment").length() != 0) {
                       JSONArray comment = object.getJSONArray("comment");
                           for (int a = 0; a < comment.length(); a++) {
                               JSONObject commnetOject = comment.getJSONObject(a);
                               foretBoardComment = new ForetBoardComment();
                               foretBoardComment.setId(commnetOject.getInt("id"));
                               foretBoardComment.setReg_date(commnetOject.getString("reg_date"));
                               foretBoardComment.setGroup_no(commnetOject.getInt("group_no"));
                               foretBoardComment.setWriter(String.valueOf(commnetOject.getInt("writer")));
                               foretBoardComment.setContent(commnetOject.getString("content"));
                               if (foretBoardComment.getId() == foretBoardComment.getGroup_no()) {
                                   foretBoardComment.setParent(true);
                               } else {
                                   foretBoardComment.setParent(false);
                               }
                               commentlist.add(foretBoardComment);
                           }
                       adapter = new CommentListFreeBoardAdapter(commentlist, ReadFreeActivity.this, memberID);
                       adapter.setCommentClickListener(ReadFreeActivity.this);
                       comment_listView.setAdapter(adapter);
                     }
                   }
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ReadFreeActivity.this, "댓글 목록 불러오기 실패함", Toast.LENGTH_SHORT).show();
        }
    }

    class DeleteBoardResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getString("boardRT").equals("OK")) {
                    Toast.makeText(ReadFreeActivity.this, "삭제 성공", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ReadFreeActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
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
                    Log.e("[TEST1]", commentlist.size()+"");
                    commentlist.add(foretBoardComment);
                    Log.e("[TEST2]", commentlist.size()+"");
                    editText_comment.setText("");
                    inputMethodManager.hideSoftInputFromWindow(editText_comment.getWindowToken(), 0);
                    adapter = new CommentListFreeBoardAdapter(commentlist, ReadFreeActivity.this, memberID);
                    Log.e("[TEST3]", commentlist.size()+"");
                    comment_listView.setAdapter(adapter);
                    comment_listView.scrollToPosition(commentlist.size());
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

    class LikeChangeResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getString("memberRT").equals("OK")) {
                    Toast.makeText(ReadFreeActivity.this, "좋아요 상태 저장 성공", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ReadFreeActivity.this, "좋아요 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }

}