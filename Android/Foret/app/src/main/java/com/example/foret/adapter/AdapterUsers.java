package com.example.foret.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.foret.model.ModelUser;


import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {

    Context context;
    List<ModelUser> userList;

    public AdapterUsers(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView mAvaterIv;    //유저 리스트에 보일 이미지
        TextView mNameTv, mEmailTv; //유저 리스트에 보일 닉네임과 이메일

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mAvaterIv = itemView.findViewById(R.id.avatarIv);
            mNameTv = itemView.findViewById(R.id.nameTv);
            mEmailTv = itemView.findViewById(R.id.emailTv);

        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //인플레이터로 한줄 화면 완성
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        final String hisUID = userList.get(position).getUid();
        String userPhoto = userList.get(position).getPhotoRoot();
        String userName = userList.get(position).getNickname();
        final String userEmail = userList.get(position).getEmail();

        //set data
        holder.mNameTv.setText(userName);
        holder.mEmailTv.setText(userEmail);

        Log.d("[test]", "his ID" + hisUID);

        try {
            Glide.with(context).load(userPhoto).fallback(R.drawable.ic_default_image_foreground)
                    .into(holder.mAvaterIv);
        } catch (Exception e) {
            e.getStackTrace();
        }
        //핸들 아이템 클릭
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("[test]", "클릭 됨");
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid", hisUID);
                context.startActivity(intent);
                Log.d("[test]","스타트 액티비티 ");
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
