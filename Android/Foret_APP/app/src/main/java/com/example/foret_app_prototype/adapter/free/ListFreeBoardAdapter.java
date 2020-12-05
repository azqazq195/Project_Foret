package com.example.foret_app_prototype.adapter.free;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.free.ReadFreeActivity;
import com.example.foret_app_prototype.model.ForetBoard;

import java.util.List;

public class ListFreeBoardAdapter extends RecyclerView.Adapter<ListFreeBoardAdapter.ItemView> {

    List<ForetBoard> list;
    Activity activity;
    int member_id;
    int like_count;
    FreeBoardOnClickListener onClick=null;
    boolean singline = true;

    public ListFreeBoardAdapter(List<ForetBoard> list, Context context, int member_id) {
        this.list = list;
        this.activity = (Activity)context;
        this.member_id = member_id;
    }

    public void setOnClick(FreeBoardOnClickListener onClick) {
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item4, parent, false);
        return new ItemView(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemView holder, int position) {
        ForetBoard foretBoard = list.get(position);
        like_count = foretBoard.getLike_count();
        holder.textView1.setText(foretBoard.getId()+"");
        holder.textView2.setText(foretBoard.getSubject());
        holder.textView3.setText(foretBoard.getWriter());
        holder.textView4.setText(foretBoard.getContent());
        holder.textView5.setText(like_count+"");
        holder.textView6.setText(foretBoard.getComment_count()+"");
        holder.textView7.setText(foretBoard.getReg_date());
        if(foretBoard.isLike()) {
            holder.button_like.isChecked();
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReadFreeActivity.class);
                intent.putExtra("foretBoard", foretBoard);
                activity.startActivity(intent);
            }
        });
        holder.button_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.button_like.isChecked()) {
                    like_count++;
                    holder.textView5.setText(like_count+"");
                } else {
                    like_count--;
                    holder.textView5.setText(like_count+"");
                }
                onClick.onlikeClick(v, like_count);
            }
        });
        holder.textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(singline) {
                    holder.textView4.setMaxLines(10);
                    singline = false;
                }  else {
                    holder.textView4.setMaxLines(2);
                    singline = true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemView extends RecyclerView.ViewHolder {

        TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
        ToggleButton button_like;
        LinearLayout layout;

        public ItemView(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            textView6 = itemView.findViewById(R.id.textView6);
            textView7 = itemView.findViewById(R.id.textView7);
            button_like = itemView.findViewById(R.id.button_like);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    public interface FreeBoardOnClickListener {
        public void onlikeClick(View v, int likeCount);
    }
}