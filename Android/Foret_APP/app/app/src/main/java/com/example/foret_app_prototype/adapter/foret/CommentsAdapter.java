package com.example.foret_app_prototype.adapter.foret;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.model.ForetBoardComment;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    List<ForetBoardComment> foretBoardCommentList;
    ViewHolder viewHolder;

    public CommentsAdapter(List<ForetBoardComment> foretBoardCommentList) {
        this.foretBoardCommentList = foretBoardCommentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.foret_board_list_comment, parent, false);
        viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForetBoardComment item = foretBoardCommentList.get(position);

        viewHolder.image_writer.setImageResource(item.getWriter_image());
        viewHolder.commentName.setText(item.getWriter());
        viewHolder.commentDate.setText(item.getReg_date());
        viewHolder.commentContent.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return foretBoardCommentList.size();
    }

    public void  setItems(List<ForetBoardComment> items) {
        this.foretBoardCommentList = items;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_writer;
        TextView commentName, commentDate, commentContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_writer = itemView.findViewById(R.id.image_writer);
            commentName = itemView.findViewById(R.id.commentName);
            commentDate = itemView.findViewById(R.id.commentDate);
            commentContent = itemView.findViewById(R.id.commentContent);
        }
    }
}
