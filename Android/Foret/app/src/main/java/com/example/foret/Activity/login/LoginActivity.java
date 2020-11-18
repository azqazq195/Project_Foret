package com.example.foret.Activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foret.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "[LOGIN]";
    Button btn_SignUp, btn_SignIn,
            btn_googleSignIn, btn_googleSignOut,
            btn_kakaoSignIn, btn_kakaoSignOut;

    // 구글 로그인 (기본)
    private int GOOGLE_SIGN_IN = 100;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    // 구글 로그인 (구글 용)
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        btn_SignIn = findViewById(R.id.btn_SignIn);
        btn_SignUp = findViewById(R.id.btn_SignUp);
        btn_googleSignIn = findViewById(R.id.btn_googleSignIn);
        btn_googleSignOut = findViewById(R.id.btn_googleSignOut);
        btn_kakaoSignIn = findViewById(R.id.btn_kakaoSignIn);
        btn_kakaoSignOut = findViewById(R.id.btn_kakaoSignOut);

        btn_SignIn.setOnClickListener(this);
        btn_SignUp.setOnClickListener(this);
        btn_googleSignIn.setOnClickListener(this);
        btn_googleSignOut.setOnClickListener(this);
        btn_kakaoSignIn.setOnClickListener(this);
        btn_kakaoSignOut.setOnClickListener(this);

        // 구글 로그인 (구글 용)
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    // 구글 로그인 (구글 용)
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                Log.d(TAG, "onActivityResult: 구글 로그인");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private void googleSignIn() {
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                GOOGLE_SIGN_IN);
    }

    private void googleSignOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: 구글 로그아웃");
                    }
                });
    }

    private void googleSignDelete() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: 구글 로그아웃, 삭제");
                    }
                });
    }

    private void kakaoSignIn() {

    }

    private void kakaoSignOut() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_SignIn:
                break;
            case R.id.btn_SignUp:
                Intent intent = new Intent(this, com.example.foret.Activity.login.SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_googleSignIn:
                googleSignIn();
                break;
            case R.id.btn_googleSignOut:
                // googleSignOut();
                googleSignDelete();
                break;
            case R.id.btn_kakaoSignIn:
                break;
            case R.id.btn_kakaoSignOut:
                break;
        }
    }
}