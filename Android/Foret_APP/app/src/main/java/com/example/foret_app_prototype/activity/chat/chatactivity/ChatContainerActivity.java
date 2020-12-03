package com.example.foret_app_prototype.activity.chat.chatactivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.chat.UsersFragment;

public class ChatContainerActivity extends AppCompatActivity {

    //객체 선언
    UsersFragment usersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_container);

        usersFragment = new UsersFragment();

        if( savedInstanceState == null){
           getSupportFragmentManager().beginTransaction().add(R.id.container,usersFragment).commit();
        }


    }
}