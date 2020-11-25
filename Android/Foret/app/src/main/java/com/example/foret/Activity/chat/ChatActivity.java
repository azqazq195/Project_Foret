package com.example.foret.Activity.chat;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foret.R;
import com.example.foret.adapter.ChatAdapter;
import com.example.foret.model.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity implements OnClickListener {

    RecyclerView recyclerView;

    ImageView profilelv;
    TextView nameTv, userStatusTv;
    EditText messageEt;
    ImageButton sendBtn;

    String photoRoot;

    String myUid;
    String hisUid;

    Context context;

    String target_nickName;
    String my_nickname;

    DatabaseReference chatRoomList;
    DatabaseReference chatRoomList1;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDbRef;

    //메세지 읽음 여부 체크
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    List<ModelChat> chatList;
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_in_room);
        Log.e("[test]","Chat 액티비티 시작됨 ");
        context = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        recyclerView = findViewById(R.id.chat_recyclerView);

        profilelv = findViewById(R.id.profileIv);
        nameTv = findViewById(R.id.nameTv);
        userStatusTv = findViewById(R.id.userStatusTv);

        messageEt = findViewById(R.id.messagaEt);
        sendBtn = findViewById(R.id.sendBtn);

        //레이아웃 셋팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //파이어 베이스 인스턴스
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDbRef = firebaseDatabase.getReference("Users");

        //uid 셋팅
        myUid = firebaseAuth.getCurrentUser().getUid();
        hisUid = getIntent().getStringExtra("hisUid");

        //내 닉네임 찾기
        Query userQueryMine = usersDbRef.orderByChild("uid").equalTo(myUid);
        userQueryMine.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //맞는 정보 찾을때가지 for문
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //데이터 얻기
                    String nickname = "" + ds.child("nickname").getValue();
                    my_nickname = nickname;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // 상대 정보 찾기
        Query userQueryTarget = usersDbRef.orderByChild("uid").equalTo(hisUid);
        //상대 디테일 정보 얻기
        userQueryTarget.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //맞는 정보 찾을때가지 for문
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //데이터 얻기
                    String nickname = "" + ds.child("nickname").getValue();
                    target_nickName = nickname;
                    photoRoot = "" + ds.child("photoRoot").getValue();
                    String onlineStatus = "offline";
                    if (ds.child("onlineStatus").getValue().toString().equals("online")) {
                        onlineStatus = "" + ds.child("onlineStatus").getValue();
                    } else {
                        onlineStatus ="" +ds.child("listlogined_date").getValue();
                    }

                    //데이터 셋팅
                    nameTv.setText(nickname);
                    userStatusTv.setText(onlineStatus);
                    //이미지 로드
                    try {
                        Glide.with(context).load(photoRoot)
                                .fallback(R.drawable.ic_default_image_foreground)
                                .into(profilelv);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        sendBtn.setOnClickListener(this);

        // 현재 사용자에 채팅 리스트 추가 함수
        chatRoomList = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(myUid)
                .child(hisUid);

        chatRoomList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRoomList.child("id").setValue(hisUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //대화 상대 채팅 리스트에 추가
        chatRoomList1 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(hisUid)
                .child(myUid);

        chatRoomList1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRoomList1.child("id").setValue(myUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        seenMessage();
        readMessages();
    }

    //메세지 읽음 여부 저장
    public void seenMessage() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)) {
                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                        hasSeenHashMap.put("isSeen", true);
                        ds.getRef().updateChildren(hasSeenHashMap);
                    }
                }
                // recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //텍스트 읽음 여부 새롭게 데이터 셋팅
    public void readMessages() {
        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    chat.setSeen(ds.getValue(ModelChat.class).isSeen);
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid) ||
                            chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)) {
                        chatList.add(chat);
                        //Log.d("[test]","리드에서 읽은 데이터 불린 ?"+chat.isSeen);
                    }
                    //어뎁터설정
                    adapter = new ChatAdapter(ChatActivity.this, chatList, photoRoot);
                    adapter.notifyDataSetChanged();

                    //뷰 설정
                    recyclerView.setAdapter(adapter);
                }
                //recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //텍스트 메세지만 보낼 때
    private void sendMessage(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("isSeen", false);
        hashMap.put("timestamp", timestamp);

        //저장될 path
        //databaseReference.child("Chats").child(target_nickName + my_nickname).push().setValue(hashMap);
        databaseReference.child("Chats").push().setValue(hashMap);
        //에딧 텍스트 초기화
        messageEt.setText("");
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
    }

    //유저 접송 상태
    private void checkUserStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //유저가 로그인한 상태
        } else {
            //유저가 로그인 안한 상태
            Toast.makeText(this, my_nickname + "님이 오프라인 상태입니다.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        String message = messageEt.getText().toString().trim();
        //입력검사
        if (message.equals("") && message == null) {
            //터치 무시
        } else {
            sendMessage(message);
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        updateuserActiveStatusOn();
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateuserActiveStatusOff();

        userRefForSeen.removeEventListener(seenListener);
    }

    //온라인 상태 만들기
    private void updateuserActiveStatusOn() {
        FirebaseUser currentUseruser = FirebaseAuth.getInstance().getCurrentUser();
        final String userUid = currentUseruser.getUid();
        DatabaseReference userAcitive = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
        HashMap<String, Object> onlineStatus = new HashMap<>();
        onlineStatus.put("onlineStatus", "online");
        onlineStatus.put("listlogined_date", "현재 접속중");
        userAcitive.updateChildren(onlineStatus);
    }

    //오프라인 상태 만들기
    private void updateuserActiveStatusOff() {
        FirebaseUser currentUseruser = FirebaseAuth.getInstance().getCurrentUser();
        final String userUid = currentUseruser.getUid();
        DatabaseReference userAcitive = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
        HashMap<String, Object> onlineStatus = new HashMap<>();
        onlineStatus.put("onlineStatus", "offline");

        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.KOREAN);
        cal.setTimeInMillis(Long.parseLong(String.valueOf(System.currentTimeMillis())));
        String dateTime = DateFormat.format("yy/MM/dd hh:mm aa",cal).toString();
        //Toast.makeText(this,dateTime,Toast.LENGTH_LONG).show();
        //Log.d("[tset","LAST seen at"+ dateTime);

        onlineStatus.put("listlogined_date", "Last Seen at : "+dateTime);
        userAcitive.updateChildren(onlineStatus);
    }
}