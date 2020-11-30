package com.example.foret_app_prototype.adapter.foret;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class NewBoardFeedAdapter extends RecyclerView.Adapter<NewBoardFeedAdapter.ViewHolder> {
    private Activity activity;
    private Member member;
    private List<Foret> foretList;
    private List<ForetBoard> foretBoardList;
    private ViewHolder viewHolder;

    public NewBoardFeedAdapter(Activity activity, Member member, List<Foret> foretList, List<ForetBoard> foretBoardList) {
        this.activity = activity;
        this.member = member;
        this.foretList = foretList;
        this.foretBoardList = foretBoardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item5, parent, false);
        viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foret foret = foretList.get(position);
        ForetBoard foretBoard = foretBoardList.get(position);

        viewHolder.imageView.setImageResource(foretBoard.getBoradImage());
        switch (foretBoard.getType()) {
            case 1:
                holder.textView1.setText("[일반]");
                break;
            case 2:
                holder.textView1.setText("[일정]");
                break;
            case 3:
                holder.textView1.setText("[공지사항]");
                break;
        }
        viewHolder.textView2.setText(foretBoard.getSubject());
        viewHolder.textView3.setText(foretBoard.getContent());
        viewHolder.textView4.setText(foretBoard.getReg_date());

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1, textView2, textView3, textView4;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}