package com.example.foret.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foret.Activity.chat.ChatActivity;
import com.example.foret.R;
import com.example.foret.model.ModelChat;
import com.example.foret.model.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    Context context;
    private List<ModelUser> user_list;    //유저 정보
    private HashMap<String, String> lastMessageMap;
    private HashMap<String, String> lastMessageTimeMap;

    public ChatListAdapter(Context context, List<ModelUser> user_list) {
        this.context = context;
        this.user_list = user_list;
        lastMessageMap = new HashMap<>();
        lastMessageTimeMap = new HashMap<>();
        }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNick, textViewContent, textViewTime, textViewCount;
        public ImageView imageViewPhoto, imageViewOnOff;

        public View rootView;

        public MyViewHolder(View v) {
            super(v);
            textViewNick = v.findViewById(R.id.textViewNick);
            textViewContent = v.findViewById(R.id.textViewContent);
            textViewTime = v.findViewById(R.id.textViewTime);
            imageViewPhoto = v.findViewById(R.id.imageViewPhoto);
            imageViewOnOff = v.findViewById(R.id.imageViewOnOff);
            textViewCount = v.findViewById(R.id.textViewCountM);
            rootView = v;
        }
    }

    @NonNull
    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_row,parent,false);
        ChatListAdapter.MyViewHolder vh = new ChatListAdapter.MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.MyViewHolder holder, int position) {
        //데이터 얻고
        //Log.d("[test","리스트 사이즈 "+user_list.size());
        final String hisUid = user_list.get(position).getUid();
        String user_Photo = user_list.get(position).getPhotoRoot();
        String user_name = user_list.get(position).getNickname();
        String lastMessage = lastMessageMap.get(hisUid);
        String lastMessageTime = lastMessageTimeMap.get(hisUid);

        String myUid1 = FirebaseAuth.getInstance().getUid();
        int unreadMessage = getUnReadMessage(myUid1, hisUid);

        if(unreadMessage<1){
            holder.textViewCount.setVisibility(View.GONE);
        }

        //데이터 셋팅
        holder.textViewNick.setText(user_name);
        if (lastMessage == null || lastMessage.equals("default") || lastMessage.equals("")) {
            holder.textViewContent.setVisibility(View.GONE);
        } else {
            holder.textViewContent.setVisibility(View.VISIBLE);
            holder.textViewContent.setText(lastMessage);
            holder.textViewTime.setText(lastMessageTime);
            holder.textViewCount.setText("" + unreadMessage);
        }

            try{
                String user_photo_root = user_list.get(position).getPhotoRoot();
                Glide.with(context)
                        .load(user_photo_root)
                        .fallback(R.drawable.ic_launcher_background)   // 메세지 여부
                        .into(holder.imageViewPhoto);

            }catch (Exception e){
                Glide.with(context)
                        .load(R.drawable.ic_launcher_background)//이미지 없을 떄 디폴트
                        .into(holder.imageViewPhoto);
            }
          //온라인 여부 설정
        try {
            if (user_list.get(position).getOnlineStatus().equals("online")) {
                Glide.with(context)
                        .load(R.drawable.circle_online) //온라인일떄
                        .into(holder.imageViewOnOff);
            } else {

            }
        }catch (Exception e){
            Glide.with(context)
                    .load(R.drawable.circle_offline) //온라인일떄
                    .into(holder.imageViewOnOff);
        }


        //클릭시 디테일 채팅룸으로 이동
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //채팅 시작
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid", hisUid);
                context.startActivity(intent);
            }
        });
    }

    public void setLastMessageMap(String user_id, String lastMessage) {
        lastMessageMap.put(user_id, lastMessage);
    }
    public void setLastMessageTimeMap(String user_id, String lastMessageTime) {
        lastMessageTimeMap.put(user_id, lastMessageTime);
    }

    @Override
    public int getItemCount() {
        return user_list == null ? 0 : user_list.size();
    }

    public ModelUser getChat(int position) {
        return user_list != null ? user_list.get(position) : null;
    }

    public void addChat(ModelUser chat) {
        user_list.add(chat);
        notifyItemInserted(user_list.size() - 1); //갱신
    }

    //안읽은 메세지 갯수 구하기
    public int getUnReadMessage(final String myUid1, final String hisUid1) {
        final int[] count = {0};
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat.getReceiver().equals(myUid1) && chat.getSender().equals(hisUid1)) {
                        if (!chat.isSeen) count[0]++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return count[0];
    }

}
