package com.example.foret_app_prototype.adapter.foret;

import java.util.Arrays;
import java.util.List;

import javax.swing.text.View;
import javax.swing.text.html.ImageView;

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

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sun.jvm.hotspot.memory.HeapBlock.Header;

// 리사이클러뷰 어답터 뷰포레사용
public class BoardViewAdapter extends RecyclerView.Adapter<BoardViewAdapter.ViewHolder> {
    private Activity activity;
    private MemberDTO memberDTO;
    private List<ForetBoardDTO> foretBoardDTOList;
    private ViewHolder viewHolder;
    private BoardViewPagerAdapter boardViewPagerAdapter;

    private AsyncHttpClient client;
    private LikeChangeResponse likeChangeResponse;
    // private LikeResponse likeResponse;
    // private LikeOffResponse likeOffResponse;
    private WriterResponse writerResponse;
    private String writer;

    ImageView writerPhoto;
    TextView textViewWriter;

    public TextView getTextViewWriter() {
        return textViewWriter;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public ImageView getWriterPhoto() {
        return writerPhoto;
    }

    private int like_count;
    int initial_likecount; // 내가 처음 글을 봤을 때의 라이크 개수 저장 변수

    public BoardViewAdapter(Activity activity, MemberDTO memberDTO, List<ForetBoardDTO> foretBoardDTOList) {
        this.activity = activity;
        this.memberDTO = memberDTO;
        this.foretBoardDTOList = foretBoardDTOList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_home_foret_list_board,
                parent, false);
        viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForetBoardDTO foretBoardDTO = foretBoardDTOList.get(position);

        getWriter(foretBoardDTO.getWriter());

        String photopath = Arrays.toString(foretBoardDTO.getPhoto()).replace("[", "").replace("]", "");
        String convertImageUrl = "http://34.72.240.24:8085/foret/storage/" + photopath;

        if (!photopath.equals("null")) {
            Glide.with(activity).load(convertImageUrl).fallback(R.drawable.icon_defalut).into(holder.viewPager_board);
            holder.viewPager_board.setVisibility(View.VISIBLE);
        }

        initial_likecount = foretBoardDTO.getBoard_like(); // 초반 라이크 수 저장
        like_count = foretBoardDTO.getBoard_like();

        viewHolder.name.setText(writer);

        viewHolder.like_count.setText("공감(" + foretBoardDTO.getBoard_like() + ")");
        viewHolder.subject.setText(foretBoardDTO.getSubject());
        viewHolder.content.setText(foretBoardDTO.getContent());
        viewHolder.comment_count.setText("댓글(" + foretBoardDTO.getBoard_comment() + ")");

        String date = "";
        if (foretBoardDTO.getReg_date() != null && foretBoardDTO.getReg_date().equals("")) {
            date = foretBoardDTO.getReg_date().substring(0, 16);
        }
        viewHolder.date.setText(date);

        viewHolder.like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.like_btn.isChecked()) {
                    like_count++;
                    viewHolder.like_count.setText(like_count + "");
                } else {
                    like_count--;
                    viewHolder.like_count.setText(like_count + "");
                }
                if (initial_likecount != like_count) {
                    // 처음 라이크 수와 달라졌을 때, 좋아요 상태를 저장해야한다. 첫 라이크보다 적어지면(-1) 좋아요 마이너스.(좋아요 삭제)
                    // 첫 라이크보다 커지면(+1) 좋아요를 추가한 상태임을 DB에 저장한다.
                    RequestParams params = new RequestParams();
                    params.put("id", memberDTO.getId());
                    params.put("board_id", foretBoardDTO.getId());
                    params.put("type", 4);
                    if (initial_likecount > like_count) { // 좋아요 수가 1감소함->좋아요 삭제
                        client.post("http://34.72.240.24:8085/foret/member/member_board_dislike.do", params,
                                likeChangeResponse);
                    } else { // 어차피 초반 if문이 처음 좋아요개수가 같지 않을때 였으므로 else를 쓰면 라이크 수가 증가한 경우만 해당
                        client.post("http://34.72.240.24:8085/foret/member/member_board_like.do", params,
                                likeChangeResponse);
                    }
                }
                notifyItemChanged(position);
            }
        });

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
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
        ImageView imageBoard, commenting, viewPager_board;
        TextView name, like_count, subject, content, comment_count, date;

        TabLayout tab_layout;
        LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.gotonext);
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
            writerPhoto = imageBoard;
            textViewWriter = name;
            client = new AsyncHttpClient();
            likeChangeResponse = new LikeChangeResponse();
            writerResponse = new WriterResponse();
        }
    }

    class LikeChangeResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if (json.getString("memberRT").equals("OK")) {
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
                    if (!temp.isNull("photo")) {
                        ImageView imageView = getWriterPhoto();
                        String convertImageUrl = "http://34.72.240.24:8085/foret/storage/" + temp.getString("photo");
                        Glide.with(activity).load(convertImageUrl).fallback(R.drawable.icon_defalut).into(imageView);
                    }
                    TextView textView = getTextViewWriter();
                    textView.setText(writer);
                } else {

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
