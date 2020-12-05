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
import com.example.foret_app_prototype.model.ForetBoardDTO;

import java.util.List;

public class ViewForetBoardAdapter extends RecyclerView.Adapter<ViewForetBoardAdapter.ViewHolder> {
    private Activity activity;
    private List<ForetBoardDTO> foretBoardDTOList;
    private ViewHolder viewHolder;

    public ViewForetBoardAdapter(Activity activity, List<ForetBoardDTO> foretBoardDTOList) {
        this.activity = activity;
        this.foretBoardDTOList = foretBoardDTOList;
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
        ForetBoardDTO foretBoardDTO = foretBoardDTOList.get(position);

        viewHolder.subject.setText(foretBoardDTO.getSubject());
        viewHolder.date.setText(foretBoardDTO.getReg_date());

        // 아이템 클릭 이벤트 처리.
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReadForetBoardActivity.class);
                intent.putExtra("foretBoardDTO", foretBoardDTO);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foretBoardDTOList.size();
    }

    public void setItems(List<ForetBoardDTO> items) {
        this.foretBoardDTOList = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subject, date;
        LinearLayout layout;

        ViewHolder(View itemView) {
            super(itemView);

            subject = itemView.findViewById(R.id.subject);
            date = itemView.findViewById(R.id.date);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
