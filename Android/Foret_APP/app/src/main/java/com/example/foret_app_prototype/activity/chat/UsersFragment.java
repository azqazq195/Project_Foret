package com.example.foret_app_prototype.activity.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.adapter.chat.AdapterUsers;
import com.example.foret_app_prototype.model.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<ModelUser> userlist;

    public UsersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        recyclerView = view.findViewById(R.id.user_recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //유저 데이터 삽입
        userlist = new ArrayList<>();

        //모든 유저 정보 얻기
        getAllusers();
        return view;
    }

    private void getAllusers() {
        //현재 유저 정보 얻기
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        //유저 페스 읽기
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        //해당 패스 데이터 읽어오기
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userlist.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelUser mdModelUser = ds.getValue(ModelUser.class);
                    // Log.d("test]", "이름 : list에서 가져옴"+mdModelUser.getNickname());
                    //현재 가입되어 있는 유저 정보 얻기
                    if (!mdModelUser.getUid().equals(fUser.getUid())) {
                        userlist.add(mdModelUser);
                    }
                }
                //어뎁터
                adapterUsers = new AdapterUsers(getActivity(), userlist);
                //뷰에 어뎁터 연결
                recyclerView.setAdapter(adapterUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void checkUserStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //유저가 로그인한 상태
        }else{
            //유저가 로그인 안한 상태
        }

    }
}