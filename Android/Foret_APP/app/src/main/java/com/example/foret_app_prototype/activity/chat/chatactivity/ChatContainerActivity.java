package com.example.foret_app_prototype.activity.chat.chatactivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.chat.UsersFragment;

public class ChatContainerActivity extends AppCompatActivity {

    //객체 선언
    UsersFragment usersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_container);

        // 상태바 색상 변경
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.foret4));

        usersFragment = new UsersFragment();

        if( savedInstanceState == null){
           getSupportFragmentManager().beginTransaction().add(R.id.container,usersFragment).commit();
        }


    }
}