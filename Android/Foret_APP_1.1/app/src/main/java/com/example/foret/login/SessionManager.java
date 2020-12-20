package com.example.foret.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.foret.model.MemberDTO;

import java.lang.reflect.Member;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(MemberDTO memberDTO){
        int id = memberDTO.getId();
        String email = memberDTO.getEmail();
        String password = memberDTO.getPassword();
        editor.putInt(SESSION_KEY, id).commit();
        editor.putString("email", email).commit();
        editor.putString("password", password).commit();
    }

    public int getSession() {
        return sharedPreferences.getInt(SESSION_KEY, -1);
    }
    public String getSessionEmail() {
        return sharedPreferences.getString("email", null);
    }
    public String getSessionPassword() {
        return sharedPreferences.getString("password", null);
    }

    public void removeSession() {
        editor.putInt(SESSION_KEY, -1).commit();
    }
}