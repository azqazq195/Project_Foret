package com.example.foret_app_prototype.activity.notify;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String tokoenRefresth = FirebaseInstanceId.getInstance().getToken();

        if(user != null){
            updateToken(tokoenRefresth);
        }
    }
        //토큰을 데이터에 저장해두기.
    private void updateToken(String tokoenRefresth) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(tokoenRefresth);
        ref.child(user.getUid()).setValue(token);
    }
}
