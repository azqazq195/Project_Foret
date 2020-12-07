package com.example.foret_app_prototype.adapter.foret;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.foret.board.ReadForetBoardActivity;
import com.example.foret_app_prototype.activity.search.SearchFragment;
import com.example.foret_app_prototype.model.HomeForetBoardDTO;
import com.example.foret_app_prototype.model.HomeForetDTO;
import com.example.foret_app_prototype.model.MemberDTO;

import java.util.List;

// 홈프래그먼트에서 사용
public class ForetBoardAdapter extends RecyclerView.Adapter<ForetBoardAdapter.ViewHolder> {
    private Activity activity;
    private MemberDTO memberDTO;
    private List<HomeForetBoardDTO> homeForetBoardDTOList;
    private ViewHolder viewHolder;

    private OnClickListener clickListener = null;

    public ForetBoardAdapter(Activity activity, MemberDTO memberDTO, List<HomeForetBoardDTO> homeForetBoardDTOList) {
        this.activity = activity;
        this.memberDTO = memberDTO;
        this.homeForetBoardDTOList = homeForetBoardDTOList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_main_home_board_thum, parent, false);
        viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeForetBoardDTO homeForetBoardDTO = homeForetBoardDTOList.get(position);

        viewHolder.subject.setText(homeForetBoardDTO.getSubject());
//            String date = homeForetBoardDTO.getReg_date().substring(0, 10);
        viewHolder.date.setText(homeForetBoardDTO.getReg_date());

        // 아이템 클릭 이벤트 처리.
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v, homeForetBoardDTO);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(homeForetBoardDTOList.size() > 3) {
            return 3;
        } else {
            return homeForetBoardDTOList.size();
        }
    }

    public void setItems(List<HomeForetBoardDTO> items) {
        this.homeForetBoardDTOList = items;
    }

    @Override
    public int getItemViewType(int position) {
        return homeForetBoardDTOList.get(position).getType();
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

    public interface OnClickListener {
        void onClick(View v, HomeForetBoardDTO homeForetBoardDTO);
    }

    // OnClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
