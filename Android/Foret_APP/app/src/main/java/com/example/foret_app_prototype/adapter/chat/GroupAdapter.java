package com.example.foret_app_prototype.adapter.chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.chat.chatactivity.GroupChatActivity;
import com.example.foret_app_prototype.helper.CalendarHelper;
import com.example.foret_app_prototype.model.ModelGroupChatList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    Context context;
    List<ModelGroupChatList> groupChatLists;

    public GroupAdapter(Context context, List<ModelGroupChatList> groupChatLists) {
        this.context = context;
        this.groupChatLists = groupChatLists;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView groupImage;
        TextView groupName, SenderName, lastMessage, sendedTime;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            groupImage = itemView.findViewById(R.id.groupImage);
            groupName = itemView.findViewById(R.id.groupName);

            SenderName = itemView.findViewById(R.id.SenderName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            sendedTime = itemView.findViewById(R.id.sendedTime);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.group_chat_list_row, parent, false);
  MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelGroupChatList model = groupChatLists.get(position);
        String groupId = model.getGroupId();
        String groupPhoto = model.getGroupPhoto();
        String grounName = model.getGroupName();


        loadLastMessage(model, holder);


        holder.groupName.setText(grounName);
        try {
            Glide.with(context).load(groupPhoto).fallback(R.drawable.ic_launcher_foreground)
                    .into(holder.groupImage);
        } catch (Exception e) {
            holder.groupImage.setImageResource(R.drawable.ic_default_image_foreground);
        }

        //채팅으로 이동
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra("groupId", groupId);
                intent.putExtra("grounName", grounName);
                context.startActivity(intent);
            }
        });
    }

    //마지막 메세지 셋팅
    private void loadLastMessage(ModelGroupChatList model, MyViewHolder holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GroupChats");
        //Log.e("[test]"," ref.child(model.getGroupName()).child(\"Messages\").limitToLast(1)?"+ ref.child(model.getGroupName()).child("Messages").limitToLast(1).toString());
        ref.child(model.getGroupName()).child("Messages").limitToLast(1)
                //.child("message").limitToLast(1).addValueEventListener(new ValueEventListener() {
                .addValueEventListener(new ValueEventListener() {
                    //마지막 아이템을 가져오는 메소드
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String type = "" + ds.child("type").getValue();
                            String message="";
                            if(type.equals("text")){
                                message = "" + ds.child("message").getValue();
                            }else if(type.equals("image")){
                                message = "이미지 파일이 전송되었습니다.";
                            }else if(type.equals("video")){
                                message = "동영상 파일이 전성되었습니다.";
                            }

                            String sender = "" + ds.child("sender").getValue();
                            //Log.d("[test]", "ds의 message? " + message);
                            String timestamp = "" + ds.child("timestamp").getValue();

                            int countSeen = Integer.parseInt("" + ds.child("countSeen").getValue());

                            String convertTime = CalendarHelper.getInstance().getRelativeTime(timestamp);

                            holder.lastMessage.setText(message);
                            holder.sendedTime.setText(convertTime);

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                            ref.orderByChild("uid").equalTo(sender).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        String nickname = "" + ds.child("nickname").getValue();
                                        holder.SenderName.setText(nickname);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return groupChatLists.size();
    }


}
