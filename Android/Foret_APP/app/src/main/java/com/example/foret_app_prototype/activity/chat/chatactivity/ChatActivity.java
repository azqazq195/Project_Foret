package com.example.foret_app_prototype.activity.chat.chatactivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.notify.APIService;
import com.example.foret_app_prototype.activity.notify.Client;
import com.example.foret_app_prototype.activity.notify.Data;
import com.example.foret_app_prototype.activity.notify.Response;
import com.example.foret_app_prototype.activity.notify.Sender;
import com.example.foret_app_prototype.activity.notify.Token;
import com.example.foret_app_prototype.adapter.chat.ChatAdapter;
import com.example.foret_app_prototype.helper.FileUtils;
import com.example.foret_app_prototype.helper.PhotoHelper;
import com.example.foret_app_prototype.helper.ProgressDialogHelper;
import com.example.foret_app_prototype.model.ModelChat;
import com.example.foret_app_prototype.model.ModelUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    ImageView profilelv;
    TextView nameTv, userStatusTv;
    EditText messageEt;
    ImageButton sendBtn, sendItemBtn;

    String photoRoot;
    String myUid;
    String hisUid;
    Context context;
    String target_nickName;
    String my_nickname;

    //파이어베이스 관련 변수
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

    File file;
    String filepath;

    //이미지 담길 uri
    Uri image_rui = null;

    //알림설정
    APIService apiService;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
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
        sendItemBtn = findViewById(R.id.attachButton);

        //레이아웃 셋팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        //알림 설정
        apiService = Client.getRetrofit("https://fcm.googleapis.com").create(APIService.class);


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
                        onlineStatus = "" + ds.child("listlogined_date").getValue();
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

        //버튼 이벤트 구역
        sendBtn.setOnClickListener(this);
        sendItemBtn.setOnClickListener(this);

        //퍼미션 얻기
        getPermission();

        openNewChat();
        seenMessage();
        readMessages();
    }

    //채팅방리스트 추가
    public void openNewChat() {
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
    }

    //버튼 이벤트
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendBtn:
                String message = messageEt.getText().toString().trim();
                //입력검사
                if (message.equals("") && message == null) {
                    //터치 무시
                } else {
                    sendMessage(message);
                    //새로운 게시물 여부 알림
                    notify = true;
                }
                break;
            case R.id.attachButton:
                showItemSelectListDialog();
                //새로운 게시물 여부 알림
                notify = true;
                break;

        }

    }

    //퍼미션 얻기
    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_MEDIA_LOCATION,
                                Manifest.permission.CAMERA}, 100);
            }
        }

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

    // 리스트 셋팅
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
        hashMap.put("type", "text");
        hashMap.put("isSeen", false);
        hashMap.put("timestamp", timestamp);

        databaseReference.child("Chats").push().setValue(hashMap);
        //에딧 텍스트 초기화
        messageEt.setText("");
        recyclerView.smoothScrollToPosition(adapter.getItemCount());


        String msg = message;
        //설정
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelUser user = snapshot.getValue(ModelUser.class);

                if(notify){
                    sendNotification(hisUid,user.getNickname(),message);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //설정
    private void sendNotification(String hisUid, String nickname, String message) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(myUid,my_nickname+" : "+message,"New Message",hisUid,R.drawable.foret_logo);

                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(ChatActivity.this,""+response.message(),Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        String dateTime = DateFormat.format("yy/MM/dd hh:mm aa", cal).toString();

        onlineStatus.put("listlogined_date", "Last Seen at : " + dateTime);
        userAcitive.updateChildren(onlineStatus);
    }

    //아이템 선택
    private void showItemSelectListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"사진 보내기", "동영상 보내기"};

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                switch (which) {
                    case 0: // 사진보내기
                        showListDialog();
                        break;
                    case 1: // 동영상보내기
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("video/*");
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        startActivityForResult(intent, 102);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //사진 선택 다이어 로그
    private void showListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"새로 촬영하기", "갤러리에서 가져오기"};

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                switch (which) {
                    case 0: // 새로 촬영하기 기능
                        filepath = PhotoHelper.getInstance().getNewPhotoPath();
                        // 카메라 앱 호출
                        file = new File(filepath);
                        image_rui = null;
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            image_rui = FileProvider.getUriForFile(ChatActivity.this,
                                    getApplicationContext().getPackageName() + ".fileprovider", file);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        } else {
                            image_rui = Uri.fromFile(file);
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_rui);
                        intent.putExtra(AUDIO_SERVICE, false);
                        startActivityForResult(intent, 100);
                        break;
                    case 1: // 갤러리에서 가져오기
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        startActivityForResult(intent, 101);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:   // 카메라 앱 호출 후
                    Intent photoIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(filepath));
                    sendBroadcast(photoIntent);
                    sendImageMessage(image_rui);

                    break;
                case 101:   // 갤러리 앱 호출 후
                    image_rui = null;
                    image_rui = data.getData();
                    sendImageMessage(image_rui);
                    break;
                case 102:
                    Uri uri = data.getData();
                    String filepath = FileUtils.getPath(this, uri);
                    file = null;
                    file = new File(filepath);
                    sendVideo(file);
                    break;
            }
        }
    }

    //비디오 보내기
    private void sendVideo(File file) {

        ProgressDialogHelper.getInstance().getProgressbar(this, "비디오 전송중입니다..");

        String timeStamp = "" + System.currentTimeMillis();
        String fileNameAndPath = "ChatVideos/" + "post_" + timeStamp + " by " + hisUid + " file : ";

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        Uri file1 = Uri.fromFile(file);

        StorageReference riversRef = storageRef.child(fileNameAndPath + file1.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file1);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                ProgressDialogHelper.getInstance().removeProgressbar();
                Toast.makeText(context, "업로드 실패", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                ProgressDialogHelper.getInstance().removeProgressbar();
                Toast.makeText(context, "업로드 성공", Toast.LENGTH_LONG).show();

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                String downloadUri = uriTask.getResult().toString();

                if (uriTask.isSuccessful()) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("sender", myUid);
                    hashMap.put("receiver", hisUid);
                    hashMap.put("message", downloadUri);
                    hashMap.put("timestamp", timeStamp);
                    hashMap.put("type", "video");
                    hashMap.put("isSeen", false);

                    databaseReference.child("Chats").push().setValue(hashMap);

                }
            }
        });

    }

    //이미지 보내기
    private void sendImageMessage(Uri image_rui) {
        ProgressDialogHelper.getInstance().getProgressbar(this, "사진을 전송중입니다..");

        String timeStamp = "" + System.currentTimeMillis();
        String fileNameAndPath = "ChatImages/" + "post_" + timeStamp + " by " + hisUid + " file : ";

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_rui);

            ByteArrayOutputStream baos = null;
            baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference ref = FirebaseStorage.getInstance().getReference().child(fileNameAndPath + image_rui.getLastPathSegment());

            ref.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ProgressDialogHelper.getInstance().removeProgressbar();
                            Toast.makeText(context, "업로드 성공", Toast.LENGTH_LONG).show();
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("sender", myUid);
                                hashMap.put("receiver", hisUid);
                                hashMap.put("message", downloadUri);
                                hashMap.put("timestamp", timeStamp);
                                hashMap.put("type", "image");
                                hashMap.put("isSeen", false);

                                databaseReference.child("Chats").push().setValue(hashMap);

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ProgressDialogHelper.getInstance().removeProgressbar();
                            Toast.makeText(context, "업로드 실패", Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}