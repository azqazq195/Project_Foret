package com.example.foret_app_prototype.adapter.foret;

import android.app.Activity;
import android.content.Context;
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
import com.example.foret_app_prototype.model.Foret;
import com.example.foret_app_prototype.model.ForetBoard;
import com.example.foret_app_prototype.model.Member;

import java.util.ArrayList;
import java.util.List;

public class NewBoardFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private Member member;
    private List<Foret> foretList;
    private List<ForetBoard> foretBoardList = new ArrayList<>();

    private final int TYPE_HEADER = 0;
    private final int TYPE_FOOTER = 1;
    private final int TYPE_ITEM = 2;


    public NewBoardFeedAdapter(Activity activity, List<ForetBoard> foretBoardList) {
        this.activity = activity;
        this.foretBoardList = foretBoardList;
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
            itemViewHolder.onBind((foretBoardList.get(position-1)), position);
        }
    }

    @Override
    public int getItemCount() {
        return foretBoardList.size()+ 2;
    }

    public class ItemViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView1, textView2, textView3, textView4;
        ForetBoard foretBoard;
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

        void onBind(ForetBoard foretBoard, int position) {
            this.foretBoard = foretBoard;
            this.position = position;

            if(foretBoard.getBoradImage() != 0) { // 사진이 있으면
//                String Board_image = foretBoard.getBoard_photo()[0]; // 첫번째 사진을 가져옴
                imageView.setImageResource(foretBoard.getBoradImage());
//                Glide.with(activity).load(Board_image).into(imageView);
            }
            switch (foretBoard.getType()) {
                case 1:
                    textView1.setText("[일반]");
                    break;
                case 2:
                    textView1.setText("[일정]");
                    break;
                case 3:
                    textView1.setText("[공지사항]");
                    break;
            }
            textView2.setText(foretBoard.getSubject());
            textView3.setText(foretBoard.getContent());
            textView4.setText(foretBoard.getReg_date());

            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, ReadForetBoardActivity.class);
            intent.putExtra("member", member);
            intent.putExtra("foretBoard", foretBoard);
            activity.startActivity(intent);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == foretBoardList.size() + 1) {
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
            addForetBoard1();
            notifyDataSetChanged();
        }
    }

    // 포레 게시판
    private void addForetBoard1() {
        String[] board_photo = {"photo.jpg", "photo2.jpg", "photo3.jpg"};
        int b = 0;
        int c = 1;
        int d = 5;
        for(int a=0; a<5; a++) {
            ForetBoard foretBoard = new ForetBoard();
            foretBoard.setId(c);
            foretBoard.setWriter("문성하");
            foretBoard.setType(c);
            foretBoard.setHit(b);
            foretBoard.setSubject("포레 1 게시판 제목 " + (a+1));
            foretBoard.setContent("포레 1 게시판 내용 " + (a+1));
            foretBoard.setReg_date("2020.11.26 12:"+ (a+1));
            foretBoard.setEdit_date("2020.11.26 12:"+ (a+1));
            foretBoard.setMember_photo("iu.jpg");
            foretBoard.setBoard_photo(board_photo);
            foretBoard.setLike_count(b);
            foretBoard.setComment_count(d);
            foretBoard.setMemberImage(R.drawable.iu+(a+1));
            foretBoard.setBoradImage(R.drawable.iu+(6+a));
            foretBoardList.add(foretBoard);
            Log.d("[TEST]", "foretBoardList.size() => " + foretBoardList.size());
        }
    }
}