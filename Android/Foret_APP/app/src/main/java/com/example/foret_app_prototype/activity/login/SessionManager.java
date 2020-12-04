package com.example.foret_app_prototype.activity.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.foret_app_prototype.model.MemberDTO;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(MemberDTO memberDTO){
        int id = memberDTO.getId();
        editor.putInt(SESSION_KEY, id).commit();

        String email = memberDTO.getEmail();
        String pwd = memberDTO.getPassword();
        editor.putString("email", email).commit();
        editor.putString("password", pwd).commit();
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