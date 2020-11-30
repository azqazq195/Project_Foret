package com.example.foret.Activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foret.R;
import com.example.foret.helper.CalendarHelper;
import com.example.foret.model.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TestLoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextNick, editTextEmail, editTextId, editTextdate;
    Button button,button2;

    boolean userisExist = false;

    //현재 유저 정보
    FirebaseUser currentUser;

    FirebaseAuth mAuth;
    //유저 등록
    RegistrationUserInFirebase registor;
    private static final int RC_SIGN_IN = 100;
    //구글등록
    //GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextNick = findViewById(R.id.editText1);
        editTextId = findViewById(R.id.editText2);
        editTextdate = findViewById(R.id.editText3);
        editTextEmail = findViewById(R.id.editText4);

        //날짜 자동 등록
        registor =new RegistrationUserInFirebase(this);

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);

        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        if(currentUser ==null ){
            userisExist = false;
        }else{
            userisExist = true;
        }
        Toast.makeText(this,"현재 유저?"+currentUser,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button) {
            if (!userisExist) {
                ModelUser modelUser = new ModelUser();
                String userNick = editTextNick.getText().toString();
                String userId = editTextId.getText().toString();
                String date =  CalendarHelper.getInstance().getCurrentTime();
                editTextdate.setText(date);
                String user_email = editTextEmail.getText().toString();

                modelUser.setDate(date);
                modelUser.setNickname(userNick);
                modelUser.setEmail(user_email);
                modelUser.setUser_id(userId);

                registor.registerUser(modelUser);
            } else {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
            }
        }else{

            mAuth.signOut();
            Toast.makeText(this,"로그아웃 눌림,"+currentUser,Toast.LENGTH_LONG).show();
            /*
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });

             */
        }
        //registor.checkUser();
    }
}