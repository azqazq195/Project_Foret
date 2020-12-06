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

import java.util.List;

public class NewBoardFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<HomeForetBoardDTO> homeForetBoardDTOList;
    private ViewHolder viewHolder;


    public NewBoardFeedAdapter(Activity activity, List<HomeForetBoardDTO> homeForetBoardDTOList) {
        this.activity = activity;
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
        viewHolder.textView4.setText(homeForetBoardDTO.getReg_date());

        // 아이템 클릭 이벤트 처리.
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReadForetBoardActivity.class);
                intent.putExtra("homeForetBoardDTO", homeForetBoardDTO);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeForetBoardDTOList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {
        TextView  textView2, textView3, textView4;
        HomeForetBoardDTO homeForetBoardDTO;
        FrameLayout layout;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            layout = itemView.findViewById(R.id.layout);

            textView2.setText(homeForetBoardDTO.getSubject());
            textView3.setText(homeForetBoardDTO.getContent());
            textView4.setText(homeForetBoardDTO.getReg_date());
        }
    }
}