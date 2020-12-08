package com.example.foret_app_prototype.adapter.foret;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.model.FBCommentDTO;
import com.example.foret_app_prototype.model.MemberDTO;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    Activity activity;
    MemberDTO memberDTO;
    List<FBCommentDTO> commentList;
    ViewHolder viewHolder;

    public CommentsAdapter(Activity activity, MemberDTO memberDTO, List<FBCommentDTO> commentList) {
        this.activity = activity;
        this.memberDTO = memberDTO;
        this.commentList = commentList;
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
        FBCommentDTO item = commentList.get(position);

        if (item.getWriter_photo().equals("") && item.getWriter_photo() == null) {
            viewHolder.image_writer.setImageResource(R.drawable.basic_image);
        } else {
            Glide.with(activity).load(item.getWriter_photo()).
                    placeholder(R.drawable.sss).into(viewHolder.image_writer);
        }
        viewHolder.commentName.setText(item.getWriter_nickname());
        String date = item.getReg_date().substring(0, 10);
        viewHolder.commentDate.setText(date);
        viewHolder.commentContent.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void  setItems(List<FBCommentDTO> items) {
        this.commentList = items;
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
