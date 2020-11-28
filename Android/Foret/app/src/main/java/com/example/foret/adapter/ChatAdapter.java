package com.example.foret.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.format.DateFormat;
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

import com.example.foret.R;
import com.example.foret.model.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    //텍스트 위치 정하기
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    Context context;
    List<ModelChat> chatList;
    String photoRoot;

    FirebaseUser fuser;

    public ChatAdapter() {
    }

    public ChatAdapter(Context context, List<ModelChat> chatList, String photoRoot) {
        this.context = context;
        this.chatList = chatList;
        this.photoRoot = photoRoot;
    }

    //뷰 홀더 클래스
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView profileIv;
        TextView messageTv, timeTv, isSeenTv;
        LinearLayout messageLayout;


        public MyViewHolder(View v) {
            super(v);
            messageTv = v.findViewById(R.id.messageTv);
            profileIv = v.findViewById(R.id.profile);
            timeTv = v.findViewById(R.id.timeTv);
            isSeenTv = v.findViewById(R.id.isSeentTv);
            messageLayout = v.findViewById(R.id.messageLayout);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = null;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_row_right, parent, false);

        } else if (viewType == MSG_TYPE_LEFT) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_row_left, parent, false);
        }
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_row, parent, false);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ModelChat chatItem = chatList.get(position);
        String message = chatItem.getMessage();
        String timeStamp = chatItem.getTimestamp();

        //convert Time stamp to dd/mm/yyyy hh:mm  am/pm
        Calendar cal = Calendar.getInstance(Locale.KOREAN);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = DateFormat.format("MM월 dd일 hh:mm aa", cal).toString();

        //setData
        holder.messageTv.setText(message);
        holder.timeTv.setText(dateTime);

        try {
            Glide.with(context).load(photoRoot)
                    .fallback(R.drawable.ic_default_image_foreground)
                    .into(holder.profileIv);
        } catch (Exception e) {
            e.getMessage();
        }

        //클릭시 삭제 이벤트
        if(holder.messageTv.getText().equals("이 메세지는 삭제되었습니다.")){
            holder.messageTv.setTextColor(Color.parseColor("#d0cdcd"));
        }else{

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
                        }
                    });
                    builder.create();
                    builder.show();
                }
            });
        }


        //상대가 읽었는지 여부 체크
        if (position == chatList.size() - 1) {
            // Log.d("[test]","chatItem.isSeen()?"+chatItem.isSeen);
            if (chatItem.isSeen) {
                holder.isSeenTv.setText("읽음");
            } else {
                holder.isSeenTv.setText("전송됨");
            }
        } else {
            holder.isSeenTv.setVisibility(View.GONE);
        }
    }

    private void deleteMessage(int position) {
        String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //보낸 시간으로 메세지 캐치
        String megTimeStamp = chatList.get(position).getTimestamp();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        Query query = dbRef.orderByChild("timestamp").equalTo(megTimeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("sender").getValue().equals(myUID)) {
                        //메세지 완전 삭제
                        //ds.getRef().removeValue();

                        //메세지 내용 체인지
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("message", "이 메세지는 삭제되었습니다.");
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


    @Override
    public int getItemCount() {
        //삼항 연산자
        return chatList == null ? 0 : chatList.size();
    }
/*
    public ModelUser getChat(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }

    public void addChat(ModelUser chat) {
        mDataset.add(chat);
        notifyItemInserted(mDataset.size() - 1); //갱신
    }

 */

    @Override
    public int getItemViewType(int position) {
        //현재 로그인된 유저 정보 얻기
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if (chatList.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
