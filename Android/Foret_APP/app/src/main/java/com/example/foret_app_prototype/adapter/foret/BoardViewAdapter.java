package com.example.foret_app_prototype.adapter.foret;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.foret.board.ReadForetBoardActivity;
import com.example.foret_app_prototype.model.ForetBoardDTO;
import com.example.foret_app_prototype.model.MemberDTO;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

// 리사이클러뷰 어답터 뷰포레사용
public class BoardViewAdapter extends RecyclerView.Adapter<BoardViewAdapter.ViewHolder>  {
    private Activity activity;
    private MemberDTO memberDTO;
    private List<ForetBoardDTO> foretBoardDTOList;
    private ViewHolder viewHolder;
    private BoardViewPagerAdapter boardViewPagerAdapter;

    private AsyncHttpClient client;
    private LikeChangeResponse likeChangeResponse;
//    private LikeResponse likeResponse;
//    private LikeOffResponse likeOffResponse;
    private WriterResponse writerResponse;
    private String writer;

//    private OnClickListener mListener = null;

    private int like_count;
    int initial_likecount; //내가 처음 글을 봤을 때의 라이크 개수 저장 변수


    public BoardViewAdapter(Activity activity, MemberDTO memberDTO, List<ForetBoardDTO> foretBoardDTOList) {
        this.activity = activity;
        this.memberDTO = memberDTO;
        this.foretBoardDTOList = foretBoardDTOList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_main_home_foret_list_board, parent, false);
        viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForetBoardDTO foretBoardDTO = foretBoardDTOList.get(position);
        Log.d("[TEST]", "foretBoardList.size() => " + foretBoardDTOList.size());
        Log.d("[TEST]", "item.getLike_count().size() => " + foretBoardDTO.getBoard_like());

        getWriter(foretBoardDTO.getWriter());

        boardViewPagerAdapter = new BoardViewPagerAdapter(activity, foretBoardDTO.getPhoto());
        Log.e("[TEST]", "뷰페이저 사진 개수 =>" + foretBoardDTO.getPhoto().length);
        Log.e("[TEST]", "뷰페이저 사진 이름 =>" + foretBoardDTO.getPhoto()[(position-1)]);

        for(int i=0; i<foretBoardDTOList.size(); i++) {
            Log.d("[TEST]", "리스트 내용 => " + foretBoardDTOList.get(i).getId());
        }

        initial_likecount = foretBoardDTO.getBoard_like(); //초반 라이크 수 저장
        like_count = foretBoardDTO.getBoard_like();

        Glide.with(viewHolder.imageBoard.getContext()).load(foretBoardDTO.getPhoto()).
                placeholder(R.drawable.sss).into(viewHolder.imageBoard);

        viewHolder.name.setText(writer);

        viewHolder.like_count.setText("공감(" + foretBoardDTO.getBoard_like() + ")");
        viewHolder.subject.setText(foretBoardDTO.getSubject());
        viewHolder.content.setText(foretBoardDTO.getContent());
        viewHolder.comment_count.setText("댓글(" + foretBoardDTO.getBoard_comment() + ")");

        String date= "";
        if(foretBoardDTO.getReg_date() != null && foretBoardDTO.getReg_date().equals("")){
            date = foretBoardDTO.getReg_date().substring(0, 16);
        }
        viewHolder.date.setText(date);

        if(!foretBoardDTO.getPhoto().equals("") && !foretBoardDTO.getPhoto().equals(null)) {
            viewHolder.viewPager_board.setVisibility(View.VISIBLE);
            viewHolder.tab_layout.setVisibility(View.VISIBLE);
            viewHolder.viewPager_board.setAdapter(boardViewPagerAdapter);
            viewHolder.tab_layout.setupWithViewPager(viewHolder.viewPager_board, true);
        }

        viewHolder.like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.like_btn.isChecked()) {
                    like_count++;
                    viewHolder.like_count.setText(like_count+"");
                } else {
                    like_count--;
                    viewHolder.like_count.setText(like_count + "");
                }
                if(initial_likecount != like_count) {
                    //처음 라이크 수와 달라졌을 때, 좋아요 상태를 저장해야한다. 첫 라이크보다 적어지면(-1) 좋아요 마이너스.(좋아요 삭제)
                    //첫 라이크보다 커지면(+1) 좋아요를 추가한 상태임을 DB에 저장한다.
                    RequestParams params = new RequestParams();
                    params.put("id", memberDTO.getId());
                    params.put("board_id", foretBoardDTO.getId());
                    params.put("type", 4);
                    if(initial_likecount > like_count) { //좋아요 수가 1감소함->좋아요 삭제
                        client.post("http://34.72.240.24:8085/foret/member/member_board_dislike.do", params, likeChangeResponse);
                    } else { //어차피 초반 if문이 처음 좋아요개수가 같지 않을때 였으므로 else를 쓰면 라이크 수가 증가한 경우만 해당
                        client.post("http://34.72.240.24:8085/foret/member/member_board_like.do", params, likeChangeResponse);
                    }
                }
                notifyItemChanged(position);
            }
        });

        viewHolder.commenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReadForetBoardActivity.class);
                intent.putExtra("board_id", foretBoardDTO.getId());
                intent.putExtra("memberDTO", memberDTO);
                Log.d("[Test]", "memberDTO  => " + memberDTO.getId());
                activity.startActivity(intent);
            }
        });

        viewHolder.comment_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReadForetBoardActivity.class);
                intent.putExtra("board_id", foretBoardDTO.getId());
                intent.putExtra("memberDTO", memberDTO);
                Log.d("[Test]", "memberDTO  => " + memberDTO.getId());
                activity.startActivity(intent);
            }
        });
    }

    private void getWriter(int id) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post("http://34.72.240.24:8085/foret/search/member.do", params, writerResponse);
    }


    @Override
    public int getItemCount() {
        return foretBoardDTOList.size();
    }

    public void setItems(List<ForetBoardDTO> items) {
        this.foretBoardDTOList = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ToggleButton like_btn;
        ImageView imageBoard, commenting;
        TextView name, like_count, subject, content, comment_count, date;
        ViewPager viewPager_board;
        TabLayout tab_layout;

        ViewHolder(View itemView) {
            super(itemView);

            imageBoard = itemView.findViewById(R.id.imageBoard);
            like_btn = itemView.findViewById(R.id.like_btn);
            commenting = itemView.findViewById(R.id.commenting);
            name = itemView.findViewById(R.id.name);
            like_count = itemView.findViewById(R.id.like_count);
            subject = itemView.findViewById(R.id.subject);
            content = itemView.findViewById(R.id.content);
            comment_count = itemView.findViewById(R.id.comment_count);
            date = itemView.findViewById(R.id.date);
            viewPager_board = itemView.findViewById(R.id.viewPager_board);
            tab_layout = itemView.findViewById(R.id.tab_layout);

            client = new AsyncHttpClient();
            likeChangeResponse = new LikeChangeResponse();
            writerResponse = new WriterResponse();
        }
    }

//    private void plusLike(int board_id) {
//        String url = "http://34.72.240.24:8085/foret/board/member_board_like.do";
//        client = new AsyncHttpClient();
//        likeResponse = new LikeResponse();
//        RequestParams params = new RequestParams();
//
//        params.put("id", memberDTO.getId());
//        params.put("board_id", board_id);
//
//        client.post(url, params, likeResponse);
//    }
//
//    private void minusLike(int board_id) {
//        String url = "http://34.72.240.24:8085/foret/board/member_board_dislike.do";
//        client = new AsyncHttpClient();
//        likeOffResponse = new LikeOffResponse();
//        RequestParams params = new RequestParams();
//
//        params.put("id", memberDTO.getId());
//        params.put("board_id", board_id);
//
//        client.post(url, params, likeOffResponse);
//    }

//    class LikeResponse extends AsyncHttpResponseHandler {
//        @Override
//        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//            String str = new String(responseBody);
//            try {
//                JSONObject json = new JSONObject(str);
//                String memberRT = json.getString("OK");
//                if(memberRT.equals("OK")) {
//                    Toast.makeText(activity, "공감", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(activity, "공감 실패", Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        @Override
//        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//            Toast.makeText(activity, "LikeResponse 통신 실패", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    class LikeOffResponse extends AsyncHttpResponseHandler {
//        @Override
//        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//            String str = new String(responseBody);
//            try {
//                JSONObject json = new JSONObject(str);
//                String memberRT = json.getString("OK");
//                if(memberRT.equals("OK")) {
//                    Toast.makeText(activity, "공감 취소", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(activity, "공감 취소 실패", Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        @Override
//        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//            Toast.makeText(activity, "LikeOffResponse 통신 실패", Toast.LENGTH_SHORT).show();
//        }
//    }

    class LikeChangeResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getString("memberRT").equals("OK")) {
                    Toast.makeText(activity, "좋아요", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "좋아요 실패", Toast.LENGTH_SHORT).show();
        }
    }

    class WriterResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if (RT.equals("OK")) {
                    JSONArray member = json.getJSONArray("member");
                    JSONObject temp = member.getJSONObject(0);
                    writer = temp.getString("nickname");
                    Log.d("[TEST]", "작성자 이름 가져옴 => " + writer);
                } else {
                    Log.d("[TEST]", "작성자 이름 못가져옴");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "WriterResponse 통신 실패 " + statusCode, Toast.LENGTH_SHORT).show();
        }
    }
}
