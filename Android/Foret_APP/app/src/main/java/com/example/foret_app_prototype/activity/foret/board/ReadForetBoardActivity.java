package com.example.foret_app_prototype.activity.foret.board;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.adapter.foret.BoardViewPagerAdapter;
import com.example.foret_app_prototype.adapter.foret.CommentsAdapter;
import com.example.foret_app_prototype.model.FBCommentDTO;
import com.example.foret_app_prototype.model.ForetBoard;
import com.example.foret_app_prototype.model.ForetBoardComment;
import com.example.foret_app_prototype.model.ForetBoardDTO;
import com.example.foret_app_prototype.model.MemberDTO;
import com.example.foret_app_prototype.model.ReadForetDTO;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReadForetBoardActivity extends AppCompatActivity implements View.OnClickListener {
    MemberDTO memberDTO;
    ReadForetDTO readForetDTO;
    ForetBoardDTO foretBoardDTO;
    List<ReadForetDTO> readForetDTOList;
    List<ForetBoardDTO> foretBoardDTOList;
    FBCommentDTO commentDTO;
    List<FBCommentDTO> commentList;
    int value;

    String url;
    AsyncHttpClient client;
    MemberResponse memberResponse;
    BoardResponse boardResponse;
    LikeResponse likeResponse;
    LikeOffResponse likeOffResponse;

    ViewPager imageViewpager;
    TabLayout tab_layout;
    TextView textView_boardName, textView_subject, textView_writer, textView_content,
            textView_date, textView_like, textView_comment2, textView_reply;
    EditText editText_comment;
    ToggleButton likeButton;
    Button button_input;
    ImageView button_cancel;
    CircleImageView image_profile;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    BoardViewPagerAdapter boardViewPagerAdapter;
    CommentsAdapter commentsAdapter;

    Intent intent;
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
        setContentView(R.layout.activity_read_foret_board);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기존 타이틀 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        foretBoardDTOList = new ArrayList<>();

        memberDTO = new MemberDTO();
        memberDTO = (MemberDTO) getIntent().getSerializableExtra("memberDTO");
        foretBoardDTO = (ForetBoardDTO) getIntent().getSerializableExtra("foretBoardDTO");
        foretBoardDTOList = (List<ForetBoardDTO>) getIntent().getSerializableExtra("foretBoardDTOList");
        Log.d("[TEST]", "멤버아디 ===> " + memberDTO.getId());
        Log.d("[TEST]", "foretBoardDTO ===> " + foretBoardDTO.getId());
        Log.d("[TEST]", "foretBoardDTOList ===> " + foretBoardDTOList.size());

        setFindById(); // 객체 초기화
        getBoard();
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
        textView_comment2 = findViewById(R.id.textView_comment2);
        textView_reply = findViewById(R.id.textView_reply);
        button_cancel = findViewById(R.id.button_cancel);
        editText_comment = findViewById(R.id.editText_comment);
        likeButton = findViewById(R.id.likeButton);
        button_input = findViewById(R.id.button_input);
        recyclerView = findViewById(R.id.comment_listView);
        imageViewpager = findViewById(R.id.imageViewpager);
        tab_layout = findViewById(R.id.tab_layout);
        boardViewPagerAdapter = new BoardViewPagerAdapter(this, memberDTO, foretBoardDTOList);

        layoutManager = new LinearLayoutManager(this);

        likeButton.setOnClickListener(this);
        button_input.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getMember();
    }

    private void setViewContent() {
        textView_writer.setText(readForetDTO.getWriter_nickname());
        String date = readForetDTO.getReg_date().substring(0, 10);
        textView_date.setText(date);
        textView_subject.setText(readForetDTO.getSubject());
        textView_content.setText(readForetDTO.getContent());

        // 뷰페이저
        if(foretBoardDTOList.size() > 0) {
            imageViewpager.setAdapter(boardViewPagerAdapter);
            tab_layout.setupWithViewPager(imageViewpager, true);
        } else {
            imageViewpager.setVisibility(View.GONE);
            tab_layout.setVisibility(View.GONE);
        }

        textView_like.setText("공감(" + readForetDTO.getBoard_like()+ ")");

        textView_comment2.setText("댓글("+ readForetDTO.getBoard_comment()+")");


        // 댓글 리스트
        commentsAdapter = new CommentsAdapter(this, memberDTO, commentList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(commentsAdapter);
//        commentsAdapter.setItems(commentList);

        // 대댓글 달때만 보이게, '대댓글 작성자'님께로 추후 수정
        textView_reply.setVisibility(View.GONE);
        button_cancel.setVisibility(View.GONE);
//        textView_reply.setText(foretBoard.getWriter() + "님께 답글 작성중입니다...");
    }

    private void getMember() {
        url = "http://34.72.240.24:8085/foret/search/member.do";
        client = new AsyncHttpClient();
        memberResponse = new MemberResponse();
        RequestParams params = new RequestParams();
        params.put("id", memberDTO.getId());
        client.post(url, params, memberResponse);
    }

    private void getBoard() {
        url = "http://34.72.240.24:8085/foret/search/boardSelect.do";
        client = new AsyncHttpClient();
        boardResponse = new BoardResponse();
        RequestParams params = new RequestParams();
        params.put("id", memberDTO.getId());
        client.post(url, params, boardResponse);
    }

    private void inputComment() {
        String comment_content = editText_comment.getText().toString().trim();
        if(comment_content.equals("")) {
            Toast.makeText(this,"댓글을 작성해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        commentDTO = new FBCommentDTO();
        commentDTO.setWriter_nickname(memberDTO.getNickname());
        commentDTO.setContent(comment_content);
        commentDTO.setReg_date(memberDTO.getReg_date());
        commentDTO.setWriter_photo(memberDTO.getPhoto());
        commentList.add(commentDTO);

        recyclerView.setLayoutManager(layoutManager);
//        layoutManager.scrollToPosition(commentList.size()-1);
        recyclerView.setAdapter(commentsAdapter);
        commentsAdapter.notifyDataSetChanged();
//        commentsAdapter.setItems(commentList);
        textView_comment2.setText("댓글("+ commentList.size() +")");
        editText_comment.setText("");
    }

    // 버튼 이벤트
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.likeButton: // 좋아요 버튼
                boolean check = likeButton.isChecked();
                if(check) {
                    likeButton.setBackgroundDrawable(v.getResources().
                            getDrawable(R.drawable.like));
                    plusLike(readForetDTO.getId());
                } else {
                    likeButton.setBackgroundDrawable(v.getResources().
                            getDrawable(R.drawable.like_off));
                    minusLike(readForetDTO.getId());
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
                intent.putExtra("membetDTO", memberDTO);
                intent.putExtra("readForetDTO", readForetDTO);
                startActivity(intent);
                break;
            case R.id.btn_delete : // 삭제 버튼
                getDialog();
                break;
            case android.R.id.home : // 뒤로가기 버튼
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
                readForetDTOList.remove(value);
                Log.d("[TEST]", "foretBoardList.size() => " + readForetDTOList.size());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("foretBoardList", (Serializable) readForetDTOList);
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
                    readForetDTO = (ReadForetDTO) data.getSerializableExtra("foretBoard");
                    readForetDTOList.get(value).setType(readForetDTO.getType());
                    readForetDTOList.get(value).setSubject(readForetDTO.getSubject());
                    readForetDTOList.get(value).setContent(readForetDTO.getContent());
                    setViewContent();
                    break;
            }
        }
    }

    class MemberResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            Log.d("[TEST]", "MemeberResponse onStart() 호출");
            getBoard();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            Log.d("[TEST]", "MemeberResponse onStart() 호출");
            getBoard(); // 보드정보 가져오기
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
            Toast.makeText(ReadForetBoardActivity.this, "MemeberResponse 통신 실패", Toast.LENGTH_SHORT).show();
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
            setViewContent();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            readForetDTOList = new ArrayList<>();
            String str = new String(responseBody);
            Gson gson = new Gson();
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if (RT.equals("OK")) {
                    JSONArray board = json.getJSONArray("board");
                    for (int i=0; i<board.length(); i++) {
                        JSONObject temp = board.getJSONObject(i);
                        readForetDTO = new ReadForetDTO();
                        readForetDTO = gson.fromJson(temp.toString(), ReadForetDTO.class);

                        JSONArray comment = temp.getJSONArray("comment");
                        commentList = new ArrayList<>();
                        if(comment.length() > 0) {
                            for (int a=0; a<comment.length(); a++) {
                                JSONObject temp2 = comment.getJSONObject(a);
                                FBCommentDTO[] fbCommentDTO = new FBCommentDTO[comment.length()];
                                fbCommentDTO[a] = gson.fromJson(temp2.toString(), FBCommentDTO.class);
                                Log.d("[TEST]", "fbCommentDTO[a].getContent() => " + fbCommentDTO[a].getContent());
                                Log.d("[TEST]", "fbCommentDTO[a].getWriter_nickname => " + fbCommentDTO[a].getWriter_nickname());
                                commentList.add(fbCommentDTO[a]);
                                Log.d("[TEST]", "포문 안commentList.size() => " + commentList.size());
                            }
                            readForetDTO.setFbCommentDTOList(commentList);
                            Log.d("[TEST]", "포문끝나고 foretBoard.getFbCommentDTOList() 저장된 사이즈 => " + readForetDTO.getFbCommentDTOList().size());
                        }
                        readForetDTOList.add(readForetDTO);
                        Log.d("[TEST]", "foretBoardList.add(foretBoard) 사이즈 => " + readForetDTOList.size());
                        Log.d("[TEST]", "foretBoard.getFbCommentDTOList() 사이즈 => " + readForetDTO.getFbCommentDTOList().size());
                        Log.d("[TEST]", "foretBoardList.get(i).getId() => " + readForetDTOList.get(i).getId());
                        Log.d("[TEST]", "foretBoardList.get(i).getWriter() => " + readForetDTOList.get(i).getWriter());
                        Log.d("[TEST]", "foretBoardList.get(i).getForet_id() => " + readForetDTOList.get(i).getForet_id());
                        Log.d("[TEST]", "foretBoardList.get(i).getWriter_nickname() => " + readForetDTOList.get(i).getWriter_nickname());
                    }
                    Log.d("[TEST]", "포레 게시판 리스트 가져옴");
                    Log.d("[TEST]", "foretBoardList.size() => " + readForetDTOList.size());
                    Log.d("[TEST]", "foretBoard.getFbCommentDTOList() 사이즈 => " + readForetDTO.getFbCommentDTOList().size());
                } else {
                    Log.d("[TEST]", "포레 게시판 리스트 못가져옴");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ReadForetBoardActivity.this, "BoardResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }

    private void plusLike(int board_id) {
        String url = "http://34.72.240.24:8085/foret/board/member_board_like.do";
        client = new AsyncHttpClient();
        likeResponse = new LikeResponse();
        RequestParams params = new RequestParams();

//        params.put("id", memberDTO.getId());
        params.put("id", 1);
        params.put("board_id", board_id);

        client.post(url, params, likeResponse);
    }

    private void minusLike(int board_id) {
        String url = "http://34.72.240.24:8085/foret/board/member_board_dislike.do";
        client = new AsyncHttpClient();
        likeOffResponse = new LikeOffResponse();
        RequestParams params = new RequestParams();

        params.put("id", memberDTO.getId());
        params.put("id", 1);
        params.put("board_id", board_id);

        client.post(url, params, likeOffResponse);
    }

    class LikeResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            textView_like.setText("공감("+ readForetDTO.getBoard_like()+")");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String memberRT = json.getString("OK");
                if(memberRT.equals("OK")) {
                    Toast.makeText(ReadForetBoardActivity.this, "공감", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReadForetBoardActivity.this, "공감 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ReadForetBoardActivity.this, "LikeResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }

    class LikeOffResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            textView_like.setText("공감("+ readForetDTO.getBoard_like()+")");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String memberRT = json.getString("OK");
                if(memberRT.equals("OK")) {
                    Toast.makeText(ReadForetBoardActivity.this, "공감 취소", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReadForetBoardActivity.this, "공감 취소 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(ReadForetBoardActivity.this, "LikeOffResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }

}
