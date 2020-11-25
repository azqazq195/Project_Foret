package com.example.foret.Activity.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.foret.R;
import com.example.foret.helper.Calendar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView1, textView2, textView3;

    Button buttonToGoUserList, buttonToGoChatList;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();

        linearLayout = findViewById(R.id.itemView);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        buttonToGoUserList = findViewById(R.id.buttonToGoUserList);
        buttonToGoChatList = findViewById(R.id.buttonToGoChatList);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            boolean emailVerified = user.isEmailVerified();

            String uid = user.getUid();

            textView1.setText(name);
            textView2.setText(email);
            textView3.setText(uid);


        }
        buttonToGoChatList.setOnClickListener(this);
        buttonToGoUserList.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        linearLayout.setVisibility(View.GONE);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //채링 리스트로 이동
        if (v.getId() == R.id.buttonToGoChatList) {
            //startActivity(new Intent(this,ChatListFragment.class));
            ChatListFragment fragmentchatList = new ChatListFragment();
            ft.replace(R.id.container, fragmentchatList, "").commit();

        } else if (v.getId() == R.id.buttonToGoUserList) {
            //startActivity(new Intent(this,UsersFragment.class));
            UsersFragment fragmentuserList = new UsersFragment();
            ft.replace(R.id.container, fragmentuserList, "").commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //checkUserStatus();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //유저가 어플 종료시 대화 상태 끄기
        //updateuserActiveStatusOff();
    }

    //유저 접송 상태
    private void checkUserStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            updateuserActiveStatusOn();
        } else {

            updateuserActiveStatusOff();
        }
    }

    //온라인 상태 만들기
    private void updateuserActiveStatusOn() {
        FirebaseUser currentUseruser = FirebaseAuth.getInstance().getCurrentUser();
        final String userUid = currentUseruser.getUid();
        DatabaseReference userAcitive = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
        HashMap<String, Object> onlineStatus = new HashMap<>();
        onlineStatus.put("onlineStatus", "online");
        onlineStatus.put("listlogined_date","현재 접속중");
        userAcitive.updateChildren(onlineStatus);
    }

    //오프라인 상태 만들기
    private void updateuserActiveStatusOff() {
        FirebaseUser currentUseruser = FirebaseAuth.getInstance().getCurrentUser();
        final String userUid = currentUseruser.getUid();
        DatabaseReference userAcitive = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
        HashMap<String, Object> onlineStatus = new HashMap<>();
        onlineStatus.put("onlineStatus", "offline");
        onlineStatus.put("listlogined_date",new Calendar().getCurrentTime());
        userAcitive.updateChildren(onlineStatus);
    }

}