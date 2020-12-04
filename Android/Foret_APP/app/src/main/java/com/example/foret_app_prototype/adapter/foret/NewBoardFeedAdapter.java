package com.example.foret_app_prototype.adapter.foret;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.foret.board.ReadForetBoardActivity;
import com.example.foret_app_prototype.model.ForetBoardDTO;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NewBoardFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<ForetBoardDTO> foretBoardDTOList;

    private final int TYPE_HEADER = 0;
    private final int TYPE_FOOTER = 1;
    private final int TYPE_ITEM = 2;

    public NewBoardFeedAdapter(Activity activity, List<ForetBoardDTO> foretBoardDTOList) {
        this.activity = activity;
        this.foretBoardDTOList = foretBoardDTOList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item5, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, parent, false);
            return new FooterViewHolder(view);
        } else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("[TEST]", "position => " + position);

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.textView43.setText("새 글 피드");
        } else
        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
        } else if (holder instanceof ItemViewHolder) {
            // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.onBind((foretBoardDTOList.get(position-1)), position);
        }
    }

    @Override
    public int getItemCount() {
        return foretBoardDTOList.size()+ 2;
    }

    public class ItemViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView1, textView2, textView3, textView4;
        ForetBoardDTO foretBoardDTO;
        int position;
        FrameLayout layout;

        public ItemViewHolder (@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            layout = itemView.findViewById(R.id.layout);
        }

        void onBind(ForetBoardDTO foretBoardDTO, int position) {
            this.foretBoardDTO = foretBoardDTO;
            this.position = position;

            if(!foretBoardDTO.getForet_board_photo().equals("") &&
                    !foretBoardDTO.getForet_board_photo().equals(null)) { // 사진이 있으면
                String Board_image = foretBoardDTO.getForet_board_photo().get(0); // 첫번째 사진을 가져옴
                Glide.with(activity).load(Board_image).into(imageView);
            }

            // 1 : 공지사항, 2 : 포레 공지사항, 3 : 포레 일정 게시판, 4 : 포레 게시판, 0 : 익명 게시판
            switch (foretBoardDTO.getType()) {
                case 0:
                    textView1.setText("[익명 게시판]");
                    break;
                case 1:
                    textView1.setText("[공지사항]");
                    break;
                case 2:
                    textView1.setText("[포레 공지사항]");
                    break;
                case 3:
                    textView1.setText("[포레 일정]");
                    break;
                case 4:
                    textView1.setText("[일반]");
                    break;
            }
            textView2.setText(foretBoardDTO.getSubject());
            textView3.setText(foretBoardDTO.getContent());
            textView4.setText(foretBoardDTO.getReg_date());

            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, ReadForetBoardActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == foretBoardDTOList.size() + 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textView43;

        public HeaderViewHolder(View view) {
            super(view);
            textView43 = view.findViewById(R.id.textView43);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout layout2;

        FooterViewHolder(View footerView) {
            super(footerView);
            layout2 = footerView.findViewById(R.id.layout2);

            layout2.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("[TEST]", "푸터 온클릭 호출");
            String url = "http://34.72.240.24:8085/search/homeFragement.do"; // 페이징.두
            AsyncHttpClient client = new AsyncHttpClient();
            FooterResponse footerResponse = new FooterResponse();
            RequestParams params = new RequestParams();
            params.put("foret_id", foretBoardDTOList.get(1).getForet_id());
            params.put("startNum", "startNum");
            params.put("endNum", "endNum");
            client.post(url, params, footerResponse);
        }
    }

    class FooterResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            Log.d("[TEST]", "ForetBoardResponse onStart() 호출");
        }
        @Override
        public void onFinish() {
            Log.d("[TEST]", "ForetBoardResponse onFinish() 호출");
            notifyDataSetChanged(); // 새로고침
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            foretBoardDTOList = new ArrayList<>();
            String str = new String(responseBody);
            Gson gson = new Gson();
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if(RT.equals("OK")) {
                    JSONArray board = json.getJSONArray("board");
                    JSONObject temp = board.getJSONObject(0);
                    for(int i=0; i<board.length(); i++) {
                        ForetBoardDTO foretBoardDTO = gson.fromJson(temp.toString(), ForetBoardDTO.class);
                        foretBoardDTOList.add(foretBoardDTO);
                    }
                    Log.d("[TEST]", "foretBoardDTOList.size() => " + foretBoardDTOList.size());
                    Toast.makeText(activity, "포레 게시판 정보 가져옴", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "포레 게시판 정보 못가져옴", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "ForetBoardResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }
}