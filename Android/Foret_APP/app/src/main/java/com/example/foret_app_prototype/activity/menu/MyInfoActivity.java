package com.example.foret_app_prototype.activity.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.login.SplashActivity;
import com.example.foret_app_prototype.model.MemberDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    MemberDTO memberDTO;
    TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, button_out;
    ImageView profile;
    String region = "";
    String tag = "";
    AsyncHttpClient client;
    Intent intent;
    DeleteMemberResponse deleteResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        // 상태바 색상 변경
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.foret4));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        memberDTO = (MemberDTO) getIntent().getSerializableExtra("memberDTO");

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView_region);
        textView7 = findViewById(R.id.textView7);
        button_out = findViewById(R.id.button_out);
        profile = findViewById(R.id.profile);

        deleteResponse = new DeleteMemberResponse();
        client = new AsyncHttpClient();

        setData(memberDTO);

        button_out.setOnClickListener(this);

    }

    private void setData(MemberDTO memberDTO) {
        region = (memberDTO.getRegion_si().toString() + "," + memberDTO.getRegion_gu().toString()).replace("[", "").replace("]", "");
        for (int a = 0; a < memberDTO.getTag().size(); a++) {
            tag += "#" + memberDTO.getTag().get(a) + " ";
        }

        textView1.setText(memberDTO.getNickname());
        textView2.setText(memberDTO.getEmail());
        textView3.setText(memberDTO.getId() + "");
        textView4.setText(memberDTO.getBirth());
        textView5.setText(memberDTO.getReg_date());
        textView6.setText(region);
        textView7.setText(tag);
        Glide.with(this).load(memberDTO.getPhoto())
                .fallback(R.drawable.icon2)
                .into(profile);


    }

    @Override //메뉴 설정
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.modify_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override //메뉴 이벤트 처리
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modify:
                intent = new Intent(this, EditMyInfoActivity.class);
                intent.putExtra("memberDTO", memberDTO);
                intent.putExtra("region", region);
                intent.putExtra("tag", tag);
                startActivityForResult(intent, 1);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_out:
                dialogOUT();
                break;
        }
    }

    private void dialogOUT() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("정말 탈퇴 하시겠습니까?\n(삭제된 정보는 복구할 수 없습니다.)");
        builder.setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestParams params = new RequestParams();
                params.put("id", memberDTO.getId());
                client.post("http://34.72.240.24:8085/foret/member/member_delete.do", params, deleteResponse);
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {
                case RESULT_OK:
                    memberDTO = (MemberDTO) data.getSerializableExtra("memberDTO");
                    setData(memberDTO);


                    break;
                case RESULT_CANCELED: //아무것도 안함
                    break;
            }
        }
    }

    class DeleteMemberResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if (json.getString("memberRT").equals("OK")) {
                    Toast.makeText(MyInfoActivity.this, "탈퇴하셨습니다.", Toast.LENGTH_SHORT).show();
                    intent = new Intent(MyInfoActivity.this, SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    //파이어 베이스 제거
                    //removeFirebase();

                    startActivity(intent);
                    finish();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(MyInfoActivity.this, "회원탈퇴 500에러뜸", Toast.LENGTH_SHORT).show();
        }

        //파이어 베이스 모두제거
        private void removeFirebase() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            //개인아이디 삭제
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(user.getUid()).removeValue();

            //내가 개인채팅 삭제
            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("ChatsList");
            ref1.child(user.getUid()).removeValue();

            //상대방채팅에 나 삭제
            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("ChatsList");
            ref2.removeEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.getRef().equals(user.getUid())) {
                            ds.child(user.getUid()).getRef().removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            //그룹에서 삭제
            DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("ChatsList");
            ref3.removeEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.child("participants").child(memberDTO.getNickname()).exists()) {
                            // ds.child("participants").child(memberDTO.getNickname()).removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            //파이어베이스에서 삭제
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "User account deleted.");
                            }
                        }
                    });
        }
    }

}