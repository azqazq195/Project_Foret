package com.example.foret_app_prototype.adapter.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.model.ModelUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class GroupJoinAdapter extends RecyclerView.Adapter<GroupJoinAdapter.MyHolder> {

    Context context;
    List<ModelUser> userList;
    String groupId, leader;
    String groupName;


    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mListener = null;

    public void setOncitemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public GroupJoinAdapter(Context context, List<ModelUser> userList, String groupId, String leader, String groupName) {
        this.context = context;
        this.userList = userList;
        this.groupId = groupId;
        this.leader = leader;
        this.groupName = groupName;

    }

    @NonNull
    @Override
    public GroupJoinAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_join_group, parent, false);
     GroupJoinAdapter.MyHolder vh = new GroupJoinAdapter.MyHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupJoinAdapter.MyHolder holder, int position) {

        ModelUser modelUser = userList.get(position);
        String name = modelUser.getNickname();
        String email = modelUser.getEmail();
        String image = modelUser.getPhotoRoot();
        String uid = modelUser.getUid();

        holder.mNameTv.setText(name);
        holder.mEmailTv.setText(email);

        try {
            Glide.with(context).load(image).fallback(R.drawable.ic_launcher_foreground)
                    .into(holder.avatarIv);
        } catch (Exception e) {
            holder.avatarIv.setImageResource(R.drawable.ic_default_image_foreground);
        }

        checkIfAlreadExist(modelUser, holder);

    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView avatarIv;
        TextView mNameTv, mEmailTv, statusTv;
        LinearLayout layoutforevent;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            avatarIv = itemView.findViewById(R.id.avatarIv);
            mNameTv = itemView.findViewById(R.id.nameTv);
            mEmailTv = itemView.findViewById(R.id.emailTv);
            statusTv = itemView.findViewById(R.id.statusTv);
            layoutforevent = itemView.findViewById(R.id.layoutforevent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //유저가 이미 멤버인지 아닌지
                    //만약 이미 멤버라면, 추방 기능
                    //아니라면, 추가하기
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }

                }
            });

        }

    }


    //그룹 리더인지 아닌지 체크하는 함수.
    private void checkIfAlreadExist(ModelUser modelUser, MyHolder holderm) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");

        //Log.e("[test]","그룹원 상태값 가져오기 함수 진입 현재 유저"+modelUser.getNickname());
        //Log.e("[test]","현재 status?" + holderm.statusTv.getText());
        ref.child(groupName).child("participants").child(modelUser.getUser_id())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //   Log.e("[test]","그룹원인가? ");
                        if (snapshot.exists()) {
                            //     Log.e("[test]","그룹원에 진입.");
                            if (snapshot.child("participantName").getValue().toString().equals(leader)) {
                                //       Log.e("[test]","리더인가?");
                                holderm.statusTv.setText("그룹리더");
                            } else {
                                //     Log.e("[test]","회원인가인가?");
                                holderm.statusTv.setText("회원");
                            }

                        } else {
                            //Log.e("[test]","멤버가 아님");
                            holderm.statusTv.setText("");
                        }
                        //Log.e("[test]","함수 끝나고 status?" + holderm.statusTv.getText());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
