package com.example.foret_app_prototype.adapter.foret;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.foret.board.ReadForetBoardActivity;
import com.example.foret_app_prototype.model.Foret;
import com.example.foret_app_prototype.model.ForetBoard;
import com.example.foret_app_prototype.model.Member;

import java.io.Serializable;
import java.util.List;

public class ForetBoardAdapter extends RecyclerView.Adapter<ForetBoardAdapter.ViewHolder> {
    private Activity activity;
    private Member member;
    private List<Foret> foretList;
    private List<ForetBoard> foretBoardList;
    private ViewHolder viewHolder;

//    private OnItemClickListener mListener = null;


    public ForetBoardAdapter(Activity activity, Member member, List<Foret> foretList, List<ForetBoard> foretBoardList) {
        this.activity = activity;
        this.member = member;
        this.foretList = foretList;
        this.foretBoardList = foretBoardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_main_home_board_thum, parent, false);
        viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foret foret = foretList.get(position);
        ForetBoard foretBoard = foretBoardList.get(position);

//        Glide.with(viewHolder.imageBoard.getContext()).load(item.getPhoto_name())
//                .placeholder(R.drawable.noimage).into(viewHolder.imageBoard);

        switch (foretBoard.getType()) {
            case 1:
                viewHolder.type.setText("[일반]");
                break;
            case 2:
                viewHolder.type.setText("[일정]");
                break;
            case 3:
                viewHolder.type.setText("[공지사항]");
                break;
        }
        viewHolder.subject.setText(foretBoard.getSubject());
        viewHolder.date.setText(foretBoard.getReg_date());

        // 아이템 클릭 이벤트 처리.
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
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

    public void setItems(List<ForetBoard> items) {
        this.foretBoardList = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView type, subject, date;
        LinearLayout layout;

        ViewHolder(View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.type);
            subject = itemView.findViewById(R.id.subject);
            date = itemView.findViewById(R.id.date);
            layout = itemView.findViewById(R.id.layout);
        }
    }

//    public interface OnItemClickListener {
//        void onItemClick(View v, int position);
//    }
//
//    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mListener = listener;
//    }
}
