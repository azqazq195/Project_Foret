package com.example.foret.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.foret.R;
import com.example.foret.model.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
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

        public MyViewHolder(View v) {
            super(v);
            messageTv = v.findViewById(R.id.messageTv);
            profileIv = v.findViewById(R.id.profile);
            timeTv = v.findViewById(R.id.timeTv);
            isSeenTv = v.findViewById(R.id.isSeentTv);

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
