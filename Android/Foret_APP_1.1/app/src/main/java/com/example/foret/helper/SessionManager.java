package com.example.foret.helper;//package com.example.foret_app_prototype.helper;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//
//import com.example.foret_app_prototype.activity.MainActivity;
//
//import java.util.HashMap;
//
//public class SessionManager {
//    SharedPreferences sharedPreferences;
//    public SharedPreferences.Editor editor;
//    public Context context;
//
//    private static final String PREF_NAME = "LOGIN";
//    private static final String LOGIN = "IS_LOGIN";
//    public static final String NAME = "NAME";
//    public static final String ID = "ID";
//    public static final String CATE1 = "CATE1";
//    public static final String CATE2 = "CATE2";
//    public static final String CATE3 = "CATE3";
//    public static final String GOOGLE_ID = "GOOGLE_ID";
//    public static final String KAKAO_ID = "KAKAO_ID";
//
//
//    public SessionManager(Context context) {
//        this.context = context;
//        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//    }
//
//    public void createSession(String id, String  name,
//                              String cate1, String cate2, String cate3, String google, String kakao){
//        editor.putBoolean(LOGIN, true);
//        editor.putString(NAME, name);
//        editor.putString(ID, id);
//        editor.putString(CATE1, cate1);
//        editor.putString(CATE2, cate2);
//        editor.putString(CATE3, cate3);
//        editor.putString(GOOGLE_ID, google);
//        editor.putString(KAKAO_ID, kakao);
//        editor.apply();
//        editor.commit();
//    }
//
//    public boolean isLogin(){
//        return sharedPreferences.getBoolean(LOGIN, false);
//    }
//
//    public void checkLogin(){ // 로그인 안 되어있으면 로그인 창으로 이동해라 함수
//        if (!this.isLogin()){
//            Intent intent = new Intent(context, IndexActivity.class);
//            context.startActivity(intent);
//        }
//    }
//
//    public HashMap<String, String > getUserDetail(){
//        HashMap<String , String> user = new HashMap<>();
//        user.put(NAME, sharedPreferences.getString(NAME, null));
//        user.put(ID, sharedPreferences.getString(ID, null));
//        user.put(CATE1, sharedPreferences.getString(CATE1, null));
//        user.put(CATE2, sharedPreferences.getString(CATE2, null));
//        user.put(CATE3, sharedPreferences.getString(CATE3, null));
//        user.put(GOOGLE_ID, sharedPreferences.getString(GOOGLE_ID, null));
//        user.put(KAKAO_ID, sharedPreferences.getString(KAKAO_ID, null));
//
//        return user;
//    }
//
//    public void logout(){ // 로그아웃 시 바로 로그인창으로 가라 함수
//        editor.clear();
//        editor.commit();
//        MainActivity.LoginOK = false;
////        Intent intent = new Intent(context, MainActivity.class);
////        context.startActivity(intent);
//    }
//}