package com.example.foret_app_prototype.activity.chat.chatactivity;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.foret_app_prototype.model.ModelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationUserInFirebase {

    ;
    Context context;
    FirebaseAuth mAuth;

    public RegistrationUserInFirebase() {
    }

    public RegistrationUserInFirebase(Context context) {
        this.context = context;
    }

    //유저 정보 받기

    //저장


    //신규 등록 이메일 + 유저 Id를 통해
    public void registerUser(final ModelUser chatuser) {
        Toast.makeText(context,"신규 등록 진행중",Toast.LENGTH_LONG).show();
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(chatuser.getEmail(), chatuser.getUser_id())
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //성공 시 firebase 에 유저 등록됨. 이에 uID를 받음.
                        if (task.isSuccessful()) {
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //유저 정보 얻기
                            String email = user.getEmail();
                            String uid = user.getUid();
                            //해쉬멥에 담아서 저장
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("nickname", chatuser.getNickname());
                            hashMap.put("photoRoot", chatuser.getPhotoRoot());
                            hashMap.put("user_id", chatuser.getUser_id());
                            hashMap.put("joineddate",chatuser.getDate());

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            //파이어 베이스에 유저 등록하기
                            DatabaseReference reference = database.getReference("Users");

                            //유저를 헤쉬맵을 통해 등록하기
                            reference.child(uid).setValue(hashMap);


                            //다음 화면으로 이동하기.
                            //context.startActivity(new Intent(context, ProfileActivity.class));
                            //((Activity) context).finish();
                            //updateUI(user);
                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show();

                            //updateUI(null);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //사용중인 이메일이 있을때 나옴.
                Toast.makeText(context, "Fail : " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    //유저가 로그인 되어 있는지 확인.
    public void onStart() {
        //super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }
    /*
    private void updateUI(@Nullable FirebaseUser user) {
        if (user != null) {
            mBinding.status.setText(getString(R.string.passwordless_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
        }
    }
    */

    //기존 사용자 로그인
    public void joinedMember(String member_email, String member_id) {

        mAuth.signInWithEmailAndPassword(member_email, member_id).
                addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);

                        }


                    }
                });
    }

    //사용자 정보 엑세스
    public void checkUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }

    //정보 업데이트 함수
    public void updateUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Jane Q. User")
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User profile updated.");
                        }
                    }
                });
    }

    //유저 삭제 함수
    public void deleteUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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

