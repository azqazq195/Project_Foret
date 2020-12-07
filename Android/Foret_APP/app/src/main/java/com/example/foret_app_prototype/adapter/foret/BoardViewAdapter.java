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
    private LikeResponse likeResponse;
    private LikeOffResponse likeOffResponse;

//    private OnClickListener mListener = null;


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
//        addData();
        ForetBoardDTO foretBoardDTO = foretBoardDTOList.get(position);
        Log.d("[TEST]", "foretBoardList.size() => " + foretBoardDTOList.size());
        Log.d("[TEST]", "item.getLike_count().size() => " + foretBoardDTO.getBoard_like());

        Glide.with(viewHolder.imageBoard.getContext()).load(foretBoardDTO.getPhoto()).
                placeholder(R.drawable.sss).into(viewHolder.imageBoard);

        viewHolder.name.setText(memberDTO.getNickname());
//        viewHolder.name.setText("이름에게");

        viewHolder.like_count.setText("공감 " + foretBoardDTO.getBoard_like() + "개");
        viewHolder.subject.setText(foretBoardDTO.getSubject());
        viewHolder.content.setText(foretBoardDTO.getContent());
        viewHolder.comment_count.setText("댓글 " + foretBoardDTO.getBoard_comment() + "개 모두 보기");
        String date= "";
        if(foretBoardDTO.getReg_date() != null && foretBoardDTO.getReg_date().equals("")){
            date = foretBoardDTO.getReg_date().substring(0, 10);
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
                Log.d("[TEST]", "like_btn 버튼 클릭 됨");
//                boolean check = viewHolder.like_btn.isChecked();
//                Log.d("[TEST]", "check 값 => " + check);
                if(!viewHolder.like_btn.isChecked()) {
//                    Toast.makeText(activity, "공감", Toast.LENGTH_SHORT).show();
                    viewHolder.like_btn.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.like));
                    plusLike(foretBoardDTO.getForet_id());
                    Log.d("[Test]", "공감 => " + foretBoardDTO.getBoard_like());
                } else {
//                    Toast.makeText(activity, "공감 취소", Toast.LENGTH_SHORT).show();
                    viewHolder.like_btn.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.like_off));
                    minusLike(foretBoardDTO.getForet_id());
                    Log.d("[Test]", "공감 취소 => " + foretBoardDTO.getBoard_like());
                }
            }
        });

        viewHolder.commenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReadForetBoardActivity.class);
                intent.putExtra("foretBoard_id", foretBoardDTO.getId());
                intent.putExtra("memberDTO", memberDTO);
                Log.d("[Test]", "memberDTO  => " + memberDTO.getId());
                activity.startActivity(intent);
            }
        });

        viewHolder.comment_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReadForetBoardActivity.class);
                intent.putExtra("foretBoard_id", foretBoardDTO.getId());
                intent.putExtra("memberDTO", memberDTO);
                Log.d("[Test]", "memberDTO  => " + memberDTO.getId());
                activity.startActivity(intent);
            }
        });
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

            boardViewPagerAdapter = new BoardViewPagerAdapter((Activity) itemView.getContext(), memberDTO, foretBoardDTOList);
        }
    }

    private void plusLike(int board_id) {
        String url = "http://34.72.240.24:8085/foret/board/member_board_like.do";
        client = new AsyncHttpClient();
        likeResponse = new LikeResponse();
        RequestParams params = new RequestParams();

        params.put("id", memberDTO.getId());
//        params.put("id", 1);
        params.put("board_id", board_id);

        client.post(url, params, likeResponse);
    }

    private void minusLike(int board_id) {
        String url = "http://34.72.240.24:8085/foret/board/member_board_dislike.do";
        client = new AsyncHttpClient();
        likeOffResponse = new LikeOffResponse();
        RequestParams params = new RequestParams();

        params.put("id", memberDTO.getId());
//        params.put("id", 1);
        params.put("board_id", board_id);

        client.post(url, params, likeOffResponse);
    }

    class LikeResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String memberRT = json.getString("OK");
                if(memberRT.equals("OK")) {
                    Toast.makeText(activity, "공감", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "공감 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "LikeResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }

    class LikeOffResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String memberRT = json.getString("OK");
                if(memberRT.equals("OK")) {
                    Toast.makeText(activity, "공감 취소", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "공감 취소 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "LikeOffResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }
}
