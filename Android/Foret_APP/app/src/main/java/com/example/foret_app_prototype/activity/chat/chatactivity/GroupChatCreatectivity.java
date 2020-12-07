package com.example.foret_app_prototype.activity.chat.chatactivity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.model.Foret;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class GroupChatCreatectivity extends AppCompatActivity implements View.OnClickListener {


    //파이에베이스 계정 얻기
    private FirebaseAuth firebaseAuth;

    ImageView groupimage;
    Button buttonJoin;
    TextView name, description;

    //내 정보 얻어오기
    ModelUser user;
    String userUid;

    //그룹 얻어오기
    Foret foret;
    String group_name;

    AsyncHttpClient client;
    Activity activity;

    boolean checkAmIMemberIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_createctivity);

        // 상태바 색상 변경
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.foret4));

        activity = this;
        groupimage = findViewById(R.id.imageViewGroup);
        buttonJoin = findViewById(R.id.buttonParticipate);
        name = findViewById(R.id.textViewForGroupName);
        description = findViewById(R.id.textViewForGroupDescription);

        client = new AsyncHttpClient();

        firebaseAuth = FirebaseAuth.getInstance();

        buttonJoin.setOnClickListener(this);

        //내가 그룹 멤버인지 체크
        checkUser();
        //testgetDate();

        //통신이 있을떄 하기
        //getDate();

    }

    //임시 데이터 셋팅
    /*
    private void testgetDate() {
        checkAmIMemberIn = true;

        getImageFromFirebaseStorage();
        foret = new Foret();
        foret.setGroup_no(1);
        foret.setGroup_name("영어 그룹");
        foret.setGroup_currunt_member_count(2);
        foret.setGroup_leader("달려라 하니");
        foret.setGroup_profile("설명을 못한다");

        //사진정보는 온라인에서
        //foret.setGroup_photo(getPhotoUri);
        foret.setGroup_tag("태그1");
        foret.setGroup_region("서울");
        foret.setGroup_max_member(10);
        foret.setGroup_date_issued("2020-11-29");

        String photo_name = "iu";
        if (photo_name.equals("")) photo_name = "0";
        foret.setPhoto_name(photo_name);
    }

     */

    //파이어 베이스에 이미지 바로 얻기
    public void getImageFromFirebaseStorage() {
        StorageReference islandRef = FirebaseStorage.getInstance().getReference("ChatImages").child("iu.jpeg");

        islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(activity)
                        .load(islandRef)
                        .into(groupimage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(activity, "불러오기 실패", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        //멤버이면 버튼 활성화
        /*
        if (checkAmIMemberIn) {

            //그룹 정보 업로드
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("GroupName", foret.getGroup_name());
            hashMap.put("GroupPhoto", foret.getGroup_photo());
            hashMap.put("GroupLeader", foret.getGroup_leader());
            hashMap.put("GroupDescription", foret.getGroup_profile());
            hashMap.put("GroupId", ""+foret.getGroup_no());
            hashMap.put("GroupMaxMember", ""+foret.getGroup_max_member());
            hashMap.put("GroupCurrentJoinedMember", ""+foret.getGroup_currunt_member_count());
            hashMap.put("Group_date_issued",foret.getGroup_date_issued());

            //그룹 항목 만들기
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups").child(foret.getGroup_name());
            ref.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //유저 정보 얻로드
                    HashMap<String, String> hashMap1 = new HashMap<>();
                    hashMap1.put("participantName", user.getNickname());
                    hashMap1.put("uid", "" + firebaseAuth.getCurrentUser().getUid());
                    hashMap1.put("joinedDate", "" + System.currentTimeMillis());

                    ref.child("participants").child(user.getUser_id()).setValue(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        //reference.child("participants").child(user.getUser_id()).setValue(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(activity, "생성 성공", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(activity, GroupChatListActivity.class));

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(activity, "유저 정보 업로드 실패", Toast.LENGTH_LONG).show();
                                }
                            });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity, "생성 실패", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            //아니면 닫아지기
            Toast.makeText(this, "해당 그룹 멤버가 아닙니다.", Toast.LENGTH_LONG).show();
            finish();
        }

         */
    }

    @Override
    protected void onResume() {
        super.onResume();
        //description.setText(foret.getGroup_profile());
        //name.setText(foret.getGroup_name());
        getImageFromFirebaseStorage();

        //Toast.makeText(activity, "이거" + foret.getGroup_name() + "이고," + foret.getGroup_profile(), Toast.LENGTH_LONG).show();

    }

    private void getDate() {
        //내가 가입한 그룹 1개 이름으로 가져오기
        String url = "url을 여기다 입력하기";
        RequestParams params = new RequestParams();
        params.put("member_id", user.getUser_id());

        params.put("group_name", group_name);
        //client.get(url, params, new Response(activity));
    }

    //포레 정보 얻기 통신
    /*
    private class Response extends AsyncHttpResponseHandler {

        Activity activity;

        public Response(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String result = new String(responseBody);
            try {
                JSONObject json = new JSONObject(result);
                String rt = json.getString("rt");

                if (rt.equals("OK")) {
                    checkAmIMemberIn = true;

                    JSONObject temp = json.getJSONObject("item");
                    foret = new Foret();
                    foret.setGroup_no(temp.getInt("group_no"));
                    foret.setGroup_name(temp.getString("group_name"));
                    foret.setGroup_currunt_member_count(temp.getInt("group_currunt_member_count"));
                    foret.setGroup_leader(temp.getString("group_leader"));
                    foret.setGroup_profile(temp.getString("group_profile"));
                    foret.setGroup_photo(temp.getString("group_photo"));
                    foret.setGroup_tag(temp.getString("group_tag"));
                    foret.setGroup_region(temp.getString("group_region"));
                    foret.setGroup_max_member(temp.getInt("group_max_member"));
                    foret.setGroup_date_issued(temp.getString("group_date_issued"));

                    String photo_name = temp.getString("photo_name");
                    if (photo_name.equals("")) photo_name = "0";
                    foret.setPhoto_name(photo_name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신오류.", Toast.LENGTH_LONG).show();
        }
    }

     */

    //해당 그룹에 멤버 일 때만 대화 가능한 메소드 구현
    private void checkUser() {
        FirebaseUser nowUser = firebaseAuth.getCurrentUser();
        userUid = nowUser.getUid();

        DatabaseReference userName = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
        userName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(ModelUser.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}