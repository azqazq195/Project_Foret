package com.example.foret.Activity.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import com.example.foret.R;
import com.example.foret.adapter.GroupAdapter;
import com.example.foret.model.ModelGroupChatList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupChatListActivity extends AppCompatActivity {

    RecyclerView groupReCycleView;
    FirebaseAuth firebaseAuth;

    List<ModelGroupChatList> groupChatLists;
    GroupAdapter groupAdapter;

    //그룹 정보
    String groupName;
    String member_id;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chatlist);

        groupReCycleView = findViewById(R.id.groupReCycleView);
        firebaseAuth = FirebaseAuth.getInstance();

        context = this;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        groupReCycleView.setHasFixedSize(true);
        groupReCycleView.setLayoutManager(linearLayoutManager);

        loadGroupChatList();

    }

    private void loadGroupChatList() {
        groupChatLists = new ArrayList<>();
        //groupName = groupChatLists.get(groupChatLists.size()).getGroupName();
        groupName = "영어 그룹";
        member_id = "opihgfy";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupChatLists.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //여기 확인 필요 - 현재 유저가 해당 그룹의 인원일 떄
                    if (!ds.child("participants").child(member_id).exists()) {
                        ModelGroupChatList model = ds.getValue(ModelGroupChatList.class);
                        groupChatLists.add(model);
                    }
                    groupAdapter = new GroupAdapter(GroupChatListActivity.this, groupChatLists);
                    groupReCycleView.setAdapter(groupAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "해당 멤버가 아닙니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    //검색 명령어
    private void searchGroupChatList(String query) {
        groupChatLists = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.child(groupName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupChatLists.size();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (!ds.child("participants").child(member_id).exists()) {

                        //내가 해당 멤버이고, 그룹이름으로 방 찾기
                        if (ds.child("GroupName").toString().toLowerCase().contains(query.toLowerCase())) {
                            ModelGroupChatList model = ds.getValue(ModelGroupChatList.class);
                            groupChatLists.add(model);
                        }

                    }
                }

                groupAdapter = new GroupAdapter(context, groupChatLists);
                groupReCycleView.setAdapter(groupAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "해당 멤버가 아닙니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//inflautor
        //메뉴 인플레이터
        // getMenuInflater().inflate(R.menu.activity_main_menu,menu);

        //메뉴 아이템 숨기기
        //menu.findItem(R.id.mennuitem).setVisible(false);

        //검색 뷰생성
        MenuItem item =menu.findItem(R.id.button_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        //리스너
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //검색 버튼을 눌렀을떄 동작하고, 빈내용 검색 불가
                if(!TextUtils.isEmpty(query.trim())){
                    searchGroupChatList(query);
                }else{
                    loadGroupChatList();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //글자로 검색했을 때 작동
                if(!TextUtils.isEmpty(newText.trim())){
                    searchGroupChatList(newText);
                }else{
                    loadGroupChatList();
                }
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);//inflautor
    }


    //상단 버튼 액션 실행
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //메뉴 아이디
        int id = item.getItemId();
        //검색 버튼일 떄
        if(id==R.id.button_search){

        }
        //채팅검색일 떄
        //startActivity(new Intent(this,GroupChatCreateActivity.class));

        return super.onOptionsItemSelected(item);
    }
     */
}