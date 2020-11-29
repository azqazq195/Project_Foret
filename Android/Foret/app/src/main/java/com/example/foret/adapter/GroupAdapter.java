package com.example.foret.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foret.R;
import com.example.foret.model.ModelGroupChatList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.HolderGroupAdapter> {

    private Context context;
    private ArrayList<ModelGroupChatList> groupChatLists;

    public GroupAdapter(Context context, ArrayList<ModelGroupChatList> groupChatLists) {
        this.context = context;
        this.groupChatLists = groupChatLists;
    }

    @NonNull
    @Override
    public HolderGroupAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.group_chat_list_row, parent, false);
        return new HolderGroupAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderGroupAdapter holder, int position) {
       ModelGroupChatList model = groupChatLists.get(position);
       String groupId = model.getGroupId();
       String groupPhoto = model.getGroupPhoto();
       String grounName = model.getGroupName();

       holder.groupName.setText(grounName);
       try {
           Glide.with(context).load(groupPhoto).fallback(R.drawable.ic_launcher_foreground)
                   .into(holder.groupImage);
        }catch (Exception e){
           holder.groupImage.setImageResource(R.drawable.header_logo);
       }

       //채팅으로 이동
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });






    }

    @Override
    public int getItemCount() {
        return groupChatLists.size();
    }

    //카드 뷰 아이템 찾기
    class HolderGroupAdapter extends RecyclerView.ViewHolder {
        private CircleImageView groupImage;
        private TextView groupName, SenderName, lastMessage, sendedTime;

        public HolderGroupAdapter(@NonNull View itemView) {
            super(itemView);
            groupImage = itemView.findViewById(R.id.groupImage);
            groupName = itemView.findViewById(R.id.groupName);
            SenderName = itemView.findViewById(R.id.SenderName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            sendedTime = itemView.findViewById(R.id.sendedTime);

        }
    }

}
