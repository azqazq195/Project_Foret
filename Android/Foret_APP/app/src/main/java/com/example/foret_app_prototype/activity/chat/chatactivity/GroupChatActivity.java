package com.example.foret_app_prototype.activity.chat.chatactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.foret_app_prototype.adapter.chat.GroupChatAdapter;
import com.example.foret_app_prototype.helper.FileUtils;
import com.example.foret_app_prototype.helper.PhotoHelper;
import com.example.foret_app_prototype.helper.ProgressDialogHelper;
import com.example.foret_app_prototype.model.ModelGroupChat;
import com.example.foret_app_prototype.model.ModelGroupChatList;
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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener {

    String grounId, grounName, grounLeader;

    CircleImageView groupImage;
    TextView groupName;
    EditText messageEt;
    ImageButton attachButton, sendBtn;
    RecyclerView chat_recyclerView;
    Button buttonInvite;

    Activity activity;
    List<String> list;

    int countSeen;

    String myPhotoUri;

    List<ModelGroupChat> groupChatList;
    GroupChatAdapter chatAdapter;

    File file;
    String filepath;

    Context context;

    //이미지 담길 uri
    Uri image_rui = null;

    FirebaseAuth firebaseAuth;
    //알림설정
    APIService apiService;
    boolean notify = false;

    String myUid;
    String hisUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        context = this;
        activity = this;
        grounName = getIntent().getStringExtra("grounName");
        grounId = getIntent().getStringExtra("grounId");

        groupImage = findViewById(R.id.groupImage);
        groupName = findViewById(R.id.groupName);
        messageEt = findViewById(R.id.messagaEt);
        attachButton = findViewById(R.id.attachButton);
        sendBtn = findViewById(R.id.sendBtn);
        buttonInvite = findViewById(R.id.buttonInvite);
        buttonInvite.setVisibility(View.GONE);


        //푸쉬 알림 생성
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);


        chat_recyclerView = findViewById(R.id.chat_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chat_recyclerView.setHasFixedSize(true);
        chat_recyclerView.setLayoutManager(linearLayoutManager);


        sendBtn.setOnClickListener(this);
        attachButton.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        myUid = firebaseAuth.getCurrentUser().getUid();
        //데이터 셋팅
        getImageFromFirebaseStorage();
        groupName.setText(grounName);
        myPhotoUri = "";

        loadGroupInfo();
        loadGroupMessage();
        loadAmILeader();

    }


    private void loadAmILeader() {
        //Log.e("[test]","로드 리더체크 진입");
        //그룹 리더 가져오기
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(grounName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelGroupChatList model = snapshot.getValue(ModelGroupChatList.class);
                grounLeader = model.getGroupLeader();
                //  Log.e("[test]","grounLeader값?"+grounLeader);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //내가 리더인가?
        ref.child(grounName).child("participants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //if(ds.child("uid").getValue().equals(user.getUid())){
                    if (ds.getKey().equals(user.getUid())) {
                        String myNickname = "" + ds.child("participantName").getValue();
                        buttonInvite.setVisibility(View.VISIBLE);
                        buttonInvite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, GroupJoinActivity.class);
                                intent.putExtra("groudId", grounId);
                                intent.putExtra("grounName", grounName);
                                intent.putExtra("grounLeader", grounLeader);
                                startActivity(intent);
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadGroupMessage() {
        groupChatList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GroupChats");
        ref.child(grounName).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupChatList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //   Log.e("[test]", ds.getRef() + "loadGroupMessage 진입");
                    ModelGroupChat chat = ds.getValue(ModelGroupChat.class);
                    //   Log.e("[test]", chat.getSender());
                    groupChatList.add(chat);

                    chatAdapter = new GroupChatAdapter(GroupChatActivity.this, groupChatList, grounName);
                    chat_recyclerView.setAdapter(chatAdapter);
                    chat_recyclerView.scrollToPosition(groupChatList.size()-1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attachButton:
                showItemSelectListDialog();

                break;
            case R.id.sendBtn:
                String message = messageEt.getText().toString().trim();
                if (message.equals("") || message == null) {
                    //터치 무시
                } else {
                    sendMessage(message);
                    messageEt.setText("");
                    notify = true;
                }
                break;

        }
    }

    private void sendMessage(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GroupChats");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String timestamp = "" + System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", user.getUid());
        hashMap.put("message", message);
        hashMap.put("type", "text");
        hashMap.put("countSeen", countSeen);
        hashMap.put("timestamp", timestamp);
        hashMap.put("senderPhoto", myPhotoUri);

        databaseReference.child(String.valueOf(grounName)).child("Messages").child(timestamp)
                .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //성공
                messageEt.setText("");

                hisUid = "";
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
                ref.child(grounName).child("participants").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()){
                            hisUid = ds.getKey();
                            //
                            if(!myUid.equals(ds.getKey())){
                                updateNewItem("MESSAGE_NEW_ITEM",myUid,hisUid,message,""+System.currentTimeMillis());
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
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //실패

            }
        });

    }

    //파이어 베이스에 이미지 바로 얻기
    public void getImageFromFirebaseStorage() {
        StorageReference islandRef = FirebaseStorage.getInstance().getReference("ChatImages").child("iu.jpeg");

        islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(activity)
                        .load(islandRef)
                        .into(groupImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(activity, "불러오기 실패", Toast.LENGTH_LONG).show();
                Glide.with(activity)
                        .load(R.drawable.icon_foret)
                        .into(groupImage);
            }
        });
    }

    private void loadGroupInfo() {
        list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("GroupName").getValue().equals(grounName)) {
                        String convert = "" + ds.child("GroupCurrentJoinedMember").getValue();
                      //  Log.e("[test]", "ds.child(\"GroupCurrentJoinedMember\").getValue()?" + ds.child("GroupCurrentJoinedMember").getValue());
                        countSeen = Integer.parseInt(convert);
                        HashMap<String, Object> update = new HashMap<>();
                        update.put("GroupCurrentJoinedMember", "" + ds.child("participants").getChildrenCount());
                        reference.child(grounName).updateChildren(update);
                        /*
                        String participant = "" + ds.getChildrenCount();
                        Log.e("[test]","participant?" +participant);
                        Log.e("[test]","countSeen?" +countSeen);

                        list.add(participant);
                        Log.e("[test]","현재 인원?" + list.size());
                                                */
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity, "해당 멤버가 아닙니다.", Toast.LENGTH_LONG).show();
            }
        });

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
                        startActivityForResult(intent, 300);
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
                            image_rui = FileProvider.getUriForFile(GroupChatActivity.this,
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
                        startActivityForResult(intent, 200);
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
                case 200:   // 갤러리 앱 호출 후

                    //사용자가 선택한 파일 정보
                    image_rui = null;
                    image_rui = data.getData();
                    sendImageMessage(image_rui);

                    break;
                case 300:
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
        notify = true;
        ProgressDialogHelper.getInstance().getProgressbar(this, "비디오 전송중입니다..");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String timeStamp = "" + System.currentTimeMillis();
        String fileNameAndPath = "ChatGroupVideos/" + "post_" + timeStamp + " by " + user.getUid() + " file : ";

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
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GroupChats");

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("sender", user.getUid());
                    hashMap.put("message", downloadUri);
                    hashMap.put("timestamp", timeStamp);
                    hashMap.put("type", "video");
                    hashMap.put("countSeen", countSeen);
                    hashMap.put("senderPhoto", myPhotoUri);
                    databaseReference.child(String.valueOf(grounName)).child("Messages").child(timeStamp)
                            .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //성공
                            Toast.makeText(activity, "동영상 업로드 성공", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //실패

                        }
                    });


                }
            }
        });

    }

    //이미지 보내기
    private void sendImageMessage(Uri image_rui) {
        notify = true;
        ProgressDialogHelper.getInstance().getProgressbar(this, "사진을 전송중입니다..");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        String timeStamp = "" + System.currentTimeMillis();
        String fileNameAndPath = "ChatGroupImages/" + "post_" + timeStamp + " by " + user.getUid() + " file : ";

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
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GroupChats");

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("sender", user.getUid());
                                hashMap.put("message", downloadUri);
                                hashMap.put("timestamp", timeStamp);
                                hashMap.put("type", "image");
                                hashMap.put("countSeen", countSeen);
                                hashMap.put("senderPhoto", myPhotoUri);

                                databaseReference.child(String.valueOf(grounName)).child("Messages").child(timeStamp)
                                        .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //성공
                                        Toast.makeText(activity, "사진 업로드 성공", Toast.LENGTH_LONG).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //실패

                                    }
                                });

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


    //새글데이터
    public void updateNewItem(String type,String sender,String receiver, String content ,String time){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notify");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("type",type);
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("content",content);
        hashMap.put("time",time);
        hashMap.put("isSeen",false);

        ref.child(receiver).push().setValue(hashMap);
    }

    // 알림 발송 설정.
    private void sendNotification(String hisUid, String nickname, String message) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUid); //상대 찾기
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot ds : snapshot.getChildren()){
                    //상대 토큰값 토큰화 하기
                    Token token = ds.getValue(Token.class);

                    //데이터 셋팅
                    Data data = new Data(myUid,nickname+" : "+message,"New Message",hisUid,R.drawable.foret_logo);

                    //보내는 사람 셋팅
                    Sender sender = new Sender(data, token.getToken());
                    //발송
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    //Toast.makeText(ChatActivity.this,""+response.message(),Toast.LENGTH_LONG).show();
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
            updateuserActiveStatusOn();
        } else {
            //유저가 로그인 안한 상태
            Toast.makeText(this, "오프라인 상태입니다.", Toast.LENGTH_LONG).show();
            finish();
        }
    }


    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //updateuserActiveStatusOff();
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


}