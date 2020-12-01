package com.example.foret_app_prototype.adapter.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.chat.chatactivity.VideoViewActivity;
import com.example.foret_app_prototype.helper.CalendarHelper;
import com.example.foret_app_prototype.model.ModelGroupChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.MyViewHolder> {

    //텍스트 위치 정하기
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    Context context;
    List<ModelGroupChat> chatList;
    String grounName;
    FirebaseAuth firebaseAuth;

    public GroupChatAdapter(Context context, List<ModelGroupChat> chatList, String grounName) {
        this.context = context;
        this.chatList = chatList;
        this.grounName = grounName;

        firebaseAuth = FirebaseAuth.getInstance();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView photo;
        TextView senderName, timeTv, isSeentTv, messageTv;
        ImageView messageImage, videoView;
        LinearLayout messageLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            senderName = itemView.findViewById(R.id.senderName);
            timeTv = itemView.findViewById(R.id.timeTv);
            isSeentTv = itemView.findViewById(R.id.isSeentTv);
            messageTv = itemView.findViewById(R.id.messageTv);
            messageImage = itemView.findViewById(R.id.messageImage);
            videoView = itemView.findViewById(R.id.videoView);
            messageLayout = itemView.findViewById(R.id.messageLayout);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.group_chat_row_right, parent, false);

        } else if (viewType == MSG_TYPE_LEFT) {
            view = LayoutInflater.from(context).inflate(R.layout.group_chat_row_left, parent, false);
        }

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelGroupChat chat = chatList.get(position);
        String message = chat.getMessage();
        String sender = chat.getSender();
        String senderPhoto = chat.getSenderPhoto();
        String timestamp = chat.getTimestamp();
        String type = chat.getType();

        setVisiblityGone(holder);
        //데이터 처리
        holder.messageTv.setText(message);
        String time = CalendarHelper.getInstance().getRelativeTime(timestamp);

        holder.timeTv.setText(time);
        try {
            Glide.with(context).load(senderPhoto)
                    .fallback(R.drawable.ic_launcher_foreground)
                    .into(holder.photo);
        } catch (Exception e) {

        }
        setUserName(chat, holder);

        try {
            if (type.equals("image")) {
                holder.messageImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(message).fallback(R.drawable.ic_default_image_foreground)
                        .into(holder.messageImage);

            } else if (type.equals("text")) {
                holder.messageTv.setVisibility(View.VISIBLE);
                holder.messageTv.setText(message);

            } else if (type.equals("video")) {

                holder.videoView.setVisibility(View.VISIBLE);
                holder.videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VideoViewActivity.class);
                        intent.putExtra("video", message);
                        context.startActivity(intent);
                    }
                });
            }

        } catch (Exception e) {
            holder.messageTv.setVisibility(View.VISIBLE);
            holder.messageTv.setText(message);
        }

//클릭시 삭제 이벤트
        if (holder.messageTv.getText().equals("이 메세지는 삭제되었습니다.")) {
            holder.messageTv.setTextColor(Color.parseColor("#d0cdcd"));
        } else {
            holder.messageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("메세지 삭제");
                    builder.setMessage("이 메제지를 삭제하시겠습니까?");

                    builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteMessage(position);
                            setVisiblityGone(holder);
                            holder.messageTv.setVisibility(View.VISIBLE);
                        }
                    });
                    builder.create();
                    builder.show();
                }
            });
        }

    }

    //아이템에 따라 열기
    private void setVisiblityGone(MyViewHolder holder) {
        holder.messageImage.setVisibility(View.GONE);
        holder.messageTv.setVisibility(View.GONE);
        holder.videoView.setVisibility(View.GONE);
    }

    private void setUserName(ModelGroupChat chat, MyViewHolder holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(chat.getSender()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = "" + ds.child("nickname").getValue();

                    holder.senderName.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //현재 로그인된 유저 정보 얻기
        FirebaseUser fuser = firebaseAuth.getCurrentUser();
        if (chatList.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }


    private void deleteMessage(int position) {
        String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Log.d("[test]", "삭제 메소드 구현");
        //보낸 시간으로 메세지 캐치
        String megTimeStamp = chatList.get(position).getTimestamp();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("GroupChats").child(grounName).child("Messages");
        Query query = dbRef.orderByChild("timestamp").equalTo(megTimeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("sender").getValue().equals(myUID)) {
                    ///    Log.d("[test]", "삭제 메소드에서 ds?"+ds.getRef());
                        //메세지 내용 체인지
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("message", "이 메세지는 삭제되었습니다.");
                        hashMap.put("type", "text");

                        ds.getRef().updateChildren(hashMap);

                    } else {
                        Toast.makeText(context, "내가 보낸 메세지만 삭제할 수 있습니다.", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}


