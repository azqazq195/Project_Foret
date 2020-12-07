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
import com.example.foret_app_prototype.model.MemberDTO;

import java.io.Serializable;
import java.util.List;

// 뷰포레에서 사용
public class ViewForetBoardAdapter extends RecyclerView.Adapter<ViewForetBoardAdapter.ViewHolder> {
    private Activity activity;
    private MemberDTO memberDTO;
    private List<ForetBoardDTO> foretBoardDTOList;
    private ViewHolder viewHolder;

    public ViewForetBoardAdapter(Activity activity, MemberDTO memberDTO, List<ForetBoardDTO> foretBoardDTOList) {
        this.activity = activity;
        this.memberDTO = memberDTO;
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
        if(foretBoardDTO.getReg_date() != null) {
            String date = foretBoardDTO.getReg_date().substring(0, 10);
            viewHolder.date.setText(date);
        }

        // 아이템 클릭 이벤트 처리.
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReadForetBoardActivity.class);
                intent.putExtra("memberDTO", memberDTO);
                intent.putExtra("board_id", foretBoardDTO.getId());
                intent.putExtra("foretBoardDTOList", (Serializable) foretBoardDTOList);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(foretBoardDTOList.size() > 3) {
            return 3;
        } else {
            return foretBoardDTOList.size();
        }
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

