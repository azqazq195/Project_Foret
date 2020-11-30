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

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.foret.board.ReadForetBoardActivity;
import com.example.foret_app_prototype.model.Foret;
import com.example.foret_app_prototype.model.ForetBoard;
import com.example.foret_app_prototype.model.ForetBoardComment;
import com.example.foret_app_prototype.model.Member;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.List;

// 리사이클러뷰 어답터
public class BoardViewAdapter extends RecyclerView.Adapter<BoardViewAdapter.ViewHolder> implements View.OnClickListener {
    private Activity activity;
    private Member member;
    private Foret foret;
    private List<ForetBoard> foretBoardList;
    private List<ForetBoardComment> commentList;
    private ViewHolder viewHolder;
    private BoardViewPagerAdapter boardViewPagerAdapter;

//    private OnClickListener mListener = null;

    public BoardViewAdapter(Activity activity, Member member, Foret foret, List<ForetBoard> foretBoardList, List<ForetBoardComment> commentList) {
        this.activity = activity;
        this.member = member;
        this.foret = foret;
        this.foretBoardList = foretBoardList;
        this.commentList = commentList;
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
        ForetBoard foretBoard = foretBoardList.get(position);
        ForetBoardComment comment = commentList.get(position);
        Log.d("[TEST]", "foretBoardList.size() => " + foretBoardList.size());
        Log.d("[TEST]", "commentList.size() => " + commentList.size());
        Log.d("[TEST]", "item.getLike_count().size() => " + foretBoard.getLike_count());

//        Glide.with(viewHolder.imageBoard.getContext()).load(item.getPhoto_name())
//                .placeholder(R.drawable.noimage).into(viewHolder.imageBoard);

        viewHolder.imageBoard.setImageResource(foretBoard.getMemberImage());
        viewHolder.name.setText(foretBoard.getWriter());
        switch (foretBoard.getType()) {
            case 1:
                viewHolder.type.setText("일반");
                break;
            case 2:
                viewHolder.type.setText("일정");
                break;
            case 3:
                viewHolder.type.setText("공지사항");
                break;
        }
        viewHolder.like_count.setText("공감 " + foretBoard.getLike_count() + "개");
        viewHolder.subject.setText(foretBoard.getSubject());
        viewHolder.content.setText(foretBoard.getContent());
        viewHolder.comment_count.setText("댓글 " + foretBoard.getComment_count() + "개 모두 보기");
        viewHolder.comment.setText(comment.getWriter() + "  " + comment.getContent());
        viewHolder.date.setText(foretBoard.getReg_date());

        if(foretBoard.getBoradImage() != 0) {
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
                    Toast.makeText(activity, "공감", Toast.LENGTH_SHORT).show();
//                    viewHolder.like_btn.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.like));
                    foretBoard.setLike_count(foretBoard.getLike_count()+1);
                    viewHolder.like_count.setText("공감 " + foretBoard.getLike_count() + "개");
                } else {
                    Toast.makeText(activity, "공감 취소", Toast.LENGTH_SHORT).show();
//                    viewHolder.like_btn.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.like_off));
                    foretBoard.setLike_count(foretBoard.getLike_count()-1);
                    viewHolder.like_count.setText("공감 " + foretBoard.getLike_count() + "개");
                }
            }
        });

        viewHolder.commenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReadForetBoardActivity.class);
                intent.putExtra("member", member);
                intent.putExtra("foret", foret);
                intent.putExtra("foretBoard", foretBoard);
                intent.putExtra("foretBoardList", (Serializable) foretBoardList);
                activity.startActivity(intent);
            }
        });

        viewHolder.comment_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReadForetBoardActivity.class);
                intent.putExtra("member", member);
                intent.putExtra("foret", foret);
                intent.putExtra("foretBoard", foretBoard);
                intent.putExtra("foretBoardList", (Serializable) foretBoardList);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foretBoardList.size();
    }

    public void setItems(List<ForetBoard> items, List<ForetBoardComment> comment_items) {
        this.foretBoardList = items;
        this.commentList = comment_items;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ToggleButton like_btn;
        ImageView imageBoard, commenting;
        TextView name, type, like_count, subject, content, comment_count, comment, date;
        ViewPager viewPager_board;
        TabLayout tab_layout;

        ViewHolder(View itemView) {
            super(itemView);

            imageBoard = itemView.findViewById(R.id.imageBoard);
            like_btn = itemView.findViewById(R.id.like_btn);
            commenting = itemView.findViewById(R.id.commenting);
            type = itemView.findViewById(R.id.type);
            name = itemView.findViewById(R.id.name);
            like_count = itemView.findViewById(R.id.like_count);
            subject = itemView.findViewById(R.id.subject);
            content = itemView.findViewById(R.id.content);
            comment_count = itemView.findViewById(R.id.comment_count);
            comment = itemView.findViewById(R.id.comment);
            date = itemView.findViewById(R.id.date);
            viewPager_board = itemView.findViewById(R.id.viewPager_board);
            tab_layout = itemView.findViewById(R.id.tab_layout);

            boardViewPagerAdapter = new BoardViewPagerAdapter((Activity) itemView.getContext(), foretBoardList);

//            // 좋아요 버튼
//            like_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    boolean check = like_btn.isChecked();
//                    int position = getAdapterPosition();
//                    Log.d("[TEST]", "position ===> " + position);
//                    if (position != RecyclerView.NO_POSITION) {
//                        if (mListener != null) {
//                            if(check) {
//                                like_btn.setBackgroundDrawable(v.getResources().
//                                        getDrawable(R.drawable.like));
//                                mListener.onClick(v, check, position);
//                            } else {
//                                like_btn.setBackgroundDrawable(v.getResources().
//                                        getDrawable(R.drawable.like_off));
//                                mListener.onClick(v, check, position);
//                            }
//                        }
//                    }
//                }
//            });

//            // 댓글 쓰기 버튼
//            commenting.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        mListener.onItemClick(v, position);
//                    }
//                }
//            });

//            // 댓글 n개 모두보기
//            comment_count.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        mListener.onItemClick(v, position);
//                    }
//                }
//            });
        }
    }

//    // 이벤트
//    public interface OnClickListener {
//        void onClick(View v, boolean check, int position);
//        void onItemClick(View v, int position);
//    }
//
//    // 이벤트
//    public void setOnClickListener(OnClickListener listener) {
//        this.mListener = listener;
//    }

    
}
