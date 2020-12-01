package com.example.foret_app_prototype.adapter.chat;

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
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.chat.chatactivity.ChatActivity;
import com.example.foret_app_prototype.model.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    Context context;
    private List<ModelUser> user_list;    //유저 정보
    private HashMap<String, String> lastMessageMap;
    private HashMap<String, String> lastMessageTimeMap;
    private HashMap<String, Integer> unreadMessageCount;


    public ChatListAdapter(Context context, List<ModelUser> user_list) {
        this.context = context;
        this.user_list = user_list;
        lastMessageMap = new HashMap<>();
        lastMessageTimeMap = new HashMap<>();
        unreadMessageCount = new HashMap<>();
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

        View view = LayoutInflater.from(context).inflate(R.layout.item_row_chatlist, parent, false);
      ChatListAdapter.MyViewHolder vh = new ChatListAdapter.MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.MyViewHolder holder, int position) {
        //데이터 받기
        final String hisUid = user_list.get(position).getUid();
        String user_Photo = user_list.get(position).getPhotoRoot();
        String user_name = user_list.get(position).getNickname();

        String lastMessage = lastMessageMap.get(hisUid);
        String lastMessageTime = lastMessageTimeMap.get(hisUid);
            int unreadMessageCount1=0;
        try{
            unreadMessageCount1 = unreadMessageCount.get(hisUid);
        }catch (Exception e){
            //Log.e("[test","오류시 his?" + hisUid+ "unreadMessageCount1?"+unreadMessageCount1);
        }

        String myUid1 = FirebaseAuth.getInstance().getUid();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");

        //안 읽은 메세지 카운트
        if (unreadMessageCount1 < 1) {
            holder.textViewCount.setVisibility(View.GONE);
        } else {
            holder.textViewCount.setVisibility(View.VISIBLE);
            holder.textViewCount.setText("" + unreadMessageCount1);
        }

        //데이터 셋팅
        holder.textViewNick.setText(user_name);
        if (lastMessage == null || lastMessage.equals("default") || lastMessage.equals("")) {
            holder.textViewContent.setVisibility(View.GONE);
        } else {
            holder.textViewContent.setVisibility(View.VISIBLE);
            holder.textViewContent.setText(lastMessage);
            holder.textViewTime.setText(lastMessageTime);
        }

        try {
            String user_photo_root = user_list.get(position).getPhotoRoot();
            Glide.with(context)
                    .load(user_photo_root)
                    .fallback(R.drawable.ic_launcher_foreground)   // 메세지 여부
                    .into(holder.imageViewPhoto);

        } catch (Exception e) {
            Glide.with(context)
                    .load(R.drawable.ic_launcher_foreground)//이미지 없을 떄 디폴트
                    .into(holder.imageViewPhoto);
        }
        //온라인 여부 설정
        try {
            if (user_list.get(position).getOnlineStatus().equals("online")) {
                Glide.with(context)
                        .load(R.drawable.circle_online) //온라인일떄
                        .into(holder.imageViewOnOff);
            } else {
                Glide.with(context)
                        .load(R.drawable.circle_offline) //오프라인일때
                        .into(holder.imageViewOnOff);
            }
        } catch (Exception e) {
            Glide.with(context)
                    .load(R.drawable.ic_launcher_foreground) //여부가 없을떄
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

    public void setUnreadMessageCount(String user_id, int count) {
        unreadMessageCount.put(user_id, count);
        //Log.e("[test]", "유저 아이디랑 값" + user_id + ", " + count);
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

}
