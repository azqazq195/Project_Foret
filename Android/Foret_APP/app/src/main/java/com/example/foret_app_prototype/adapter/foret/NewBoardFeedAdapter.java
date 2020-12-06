package com.example.foret_app_prototype.adapter.foret;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.foret.board.ReadForetBoardActivity;
import com.example.foret_app_prototype.model.HomeForetBoardDTO;
import com.example.foret_app_prototype.model.MemberDTO;

import java.util.List;

// 메인 프래그먼트에서 사용
public class NewBoardFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private MemberDTO memberDTO;
    private List<HomeForetBoardDTO> homeForetBoardDTOList;
    private ViewHolder viewHolder;

    public NewBoardFeedAdapter(Activity activity, MemberDTO memberDTO, List<HomeForetBoardDTO> homeForetBoardDTOList) {
        this.activity = activity;
        this.memberDTO = memberDTO;
        this.homeForetBoardDTOList = homeForetBoardDTOList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycle_item5, parent, false);
        viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("[TEST]", "position => " + position);
        HomeForetBoardDTO homeForetBoardDTO = homeForetBoardDTOList.get(position);

        viewHolder.textView2.setText(homeForetBoardDTO.getSubject());
        viewHolder.textView3.setText(homeForetBoardDTO.getContent());

//            String date = homeForetBoardDTO.getReg_date().substring(0, 10);
        viewHolder.textView4.setText(homeForetBoardDTO.getReg_date());


        // 아이템 클릭 이벤트 처리.
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReadForetBoardActivity.class);
                intent.putExtra("homeForetBoardDTO", homeForetBoardDTO);
                intent.putExtra("memberDTO", memberDTO);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(homeForetBoardDTOList.size() > 5) {
            return 5;
        } else {
            return homeForetBoardDTOList.size();
        }
    }

    public void setItems(List<HomeForetBoardDTO> items) {
        this.homeForetBoardDTOList = items;
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {
        TextView  textView2, textView3, textView4;
        FrameLayout layout;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}