package com.example.foret_app_prototype.activity.chat.chatactivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.adapter.chat.GroupJoinAdapter;
import com.example.foret_app_prototype.model.ModelUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupJoinActivity extends AppCompatActivity {

    RecyclerView userRv;
    Toolbar actionBar;
    FirebaseAuth firebaseAuth;

    String groupId, groudName, groudLeader;
    boolean getonce = true;

    List<ModelUser> userList;
    GroupJoinAdapter adapter;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_join);

        // Log.d("[test]","초대 창 진입");

        userRv = findViewById(R.id.userRv);

        context = this;
        firebaseAuth = FirebaseAuth.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        userRv.setHasFixedSize(true);
        userRv.setLayoutManager(linearLayoutManager);

        groudLeader = getIntent().getStringExtra("grounLeader");
        groudName = getIntent().getStringExtra("grounName");
        groupId = getIntent().getStringExtra("groudId");

        loadGroupInfo();


    }

    private void getAllUser() {
        // Log.d("[test]","초대 창 유저 데이터 얻기 진입");
        userList = new ArrayList<>();
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //Log.d("[test]","초대 창 유저 데이터 얻기 진입 함수에 진입.");
                    ModelUser mdModelUser = ds.getValue(ModelUser.class);
                    if (!mdModelUser.getUid().equals(fUser.getUid())) {
                        //  Log.d("[test]","초대 창 유저 데이터 얻기 진입 함수에 진입. 내가 아니라면 리스트에 추가");
                        userList.add(mdModelUser);
                    }
                    //어뎁터
                    adapter = new GroupJoinAdapter(GroupJoinActivity.this, userList, groupId, groudLeader, groudName);
                    //뷰에 어뎁터 연결
                    userRv.setAdapter(adapter);
                    adapter.setOncitemClickListener(new GroupJoinAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            ModelUser modelUser = userList.get(position);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
                            ref.child(groudName).child("participants").child(modelUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        //존재한다면 삭제 기능
                                        removThisMember(modelUser);
                                        Log.d("[test]", "ref,exist true");
                                    } else {
                                        //아니라면 초대
                                        addMember(modelUser);
                                        Log.d("[test]", "ref,exist false");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    });
                    //Log.d("[test]","어뎁터 연결 끝");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void loadGroupInfo() {

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Groups");

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Groups");

        //안불러와 질떄 이곳 체크
        ref1.child(groudName).orderByChild("GroupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Log.d("[test]","ref1 오더바이 가 그룹 아이디랑 같은지 찾기 진입");
                for (DataSnapshot ds : snapshot.getChildren()) {
                    // Log.d("[test]","ref 스냅샷?"+ds.getRef());
                    String groupId = "" + ds.getValue().equals("groupId");
                    String GroupDescription = "" + ds.getValue().equals("GroupDescription");
                    String GroupCurrentJoinedMember = "" + ds.getValue().equals("GroupCurrentJoinedMember");
                    String GroupMaxMember = "" + ds.getValue().equals("GroupMaxMember");
                    String GroupName = "" + ds.getValue().equals("GroupName");
                    String Group_date_issued = "" + ds.getValue().equals("Group_date_issued");
                    String GroupPhoto = "";
                    //   Log.d("[test]","초대 창 그룹 정보 진입"+groupId);

                    //actionBar.setTitle("그룹원 초대");
                    ref2.child(groudName).child("participants")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //  Log.d("[test]","ref2 진입 레퍼는?"+ ref2.child(groudName).child("participants").getRef());
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        //내가 멈버인지
                                        if (ds.child("uid").getValue().equals(firebaseAuth.getUid())) {
                                            String mynickname = ds.child("participantName").getValue() + "";
                                            //  Log.d("[test]","ref2 닉네임?"+ mynickname);
                                            //그룹 리더라면
                                            if (mynickname.equals(groudLeader)) {
                                                //  actionBar.setTitle(groudName + "(그룹 리더)");
                                            } else {
                                                //  actionBar.setTitle(groudName + "(멤버)");
                                            }
                                            if (getonce) {
                                                getAllUser();
                                                getonce = false;
                                            }

                                        }
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    //멤버 삭제
    private void removThisMember(ModelUser modelUser) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groudName).child("participants").child(modelUser.getUid()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "삭제 성공", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "삭제 실패," + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    //멤버 추가
    private void addMember(ModelUser modelUser) {
        String timestamp = "" + System.currentTimeMillis();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", modelUser.getUid());
        hashMap.put("joinedDate", timestamp);
        hashMap.put("participantName", modelUser.getNickname());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groudName).child("participants").child(modelUser.getUid()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "등록성공", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "등록 실패" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}