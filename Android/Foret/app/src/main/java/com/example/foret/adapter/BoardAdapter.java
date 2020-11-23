package com.example.foret.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foret.R;
import com.example.foret.model.ForetBoard;

import java.util.ArrayList;

// 리사이클러뷰 어답터
public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {
    private ArrayList<ForetBoard> foretBoardList;
    private ViewHolder viewHolder;

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;

    public BoardAdapter(ArrayList<ForetBoard> boardList) {
        this.foretBoardList = foretBoardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_fragment_home_list_board, parent, false);
        viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForetBoard item = foretBoardList.get(position);

        Glide.with(viewHolder.imageBoard.getContext()).load(item.getPhoto_name())
                .placeholder(R.drawable.foret_no_image).into(viewHolder.imageBoard);
        viewHolder.subject.setText(item.getBoard_subject());
        viewHolder.content.setText(item.getBoard_content());

    }

    @Override
    public int getItemCount() {
        return foretBoardList.size();
    }

    public void setItems(ArrayList<ForetBoard> items) {
        this.foretBoardList = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBoard;
        TextView subject, content;

        ViewHolder(View itemView) {
            super(itemView);

            imageBoard = itemView.findViewById(R.id.imageBoard);
            subject = itemView.findViewById(R.id.subject);
            content = itemView.findViewById(R.id.content);

            // 아이템 클릭 이벤트 처리.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition() ;
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(v, position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(View v, int position);
    }

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
}
