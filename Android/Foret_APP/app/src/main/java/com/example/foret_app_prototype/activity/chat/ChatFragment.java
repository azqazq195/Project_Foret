package com.example.foret_app_prototype.activity.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.adapter.chat.ChatListAdapter;
import com.example.foret_app_prototype.adapter.chat.GroupAdapter;
import com.example.foret_app_prototype.helper.CalendarHelper;
import com.example.foret_app_prototype.model.ModelChat;
import com.example.foret_app_prototype.model.ModelChatList;
import com.example.foret_app_prototype.model.ModelGroupChatList;
import com.example.foret_app_prototype.model.ModelUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment implements View.OnClickListener {

    Toolbar toolbar;
    MainActivity activity;
    FloatingActionButton floatingActionButton;

    RecyclerView recyclerView1, recyclerView2;

    LinearLayout layout_search;
    ImageView button_back;
    SearchView searchView;


    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    FirebaseUser curruntUser;


    RecyclerView recyclerViewpersonal;
    List<ModelChatList> chatlistList;

    List<ModelUser> userList;
    ChatListAdapter adapter;

    //그룹구역
    RecyclerView reCycleViewGroup;
    List<ModelGroupChatList> groupChatLists;
    GroupAdapter groupAdapter;

    //그룹 정보
    String groupName;
    String member_id;

    Context context;

    public ChatFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(null);
        setHasOptionsMenu(true);

        floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        recyclerView1 = rootView.findViewById(R.id.recyclerView1);
        recyclerView2 = rootView.findViewById(R.id.recyclerView2);
        layout_search = rootView.findViewById(R.id.layout_search);
        button_back = rootView.findViewById(R.id.button_back);

        button_back.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);

        //유저 정보 받기
        firebaseAuth = FirebaseAuth.getInstance();
        curruntUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerViewpersonal = rootView.findViewById(R.id.recyclerViewForPersonal);
        reCycleViewGroup = rootView.findViewById(R.id.recyclerViewForGroup);

        firebaseAuth = FirebaseAuth.getInstance();


        context = container.getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewpersonal.setHasFixedSize(true);
        recyclerViewpersonal.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(container.getContext());
        linearLayoutManager1.setStackFromEnd(true);
        reCycleViewGroup.setHasFixedSize(true);
        reCycleViewGroup.setLayoutManager(linearLayoutManager1);

        //채팅방 내용 로드
        loadGroupChatList();
        loadPersonalChatList();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_fragment_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_search :
                layout_search.setVisibility(View.VISIBLE);
                break;
            case R.id.item_menu :
                DrawerLayout container = activity.findViewById(R.id.container);
                container.openDrawer(GravityCompat.END);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back :
                layout_search.setVisibility(View.GONE);
                break;
            case R.id.floatingActionButton :
                //플러스 버튼


                Toast.makeText(activity, "알아서 기능 넣기", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    //내 개인 채팅방 리스트 로드
    private void loadPersonalChatList() {
        chatlistList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(curruntUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatlistList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChatList chatlist = ds.getValue(ModelChatList.class);
                    chatlistList.add(chatlist);
                }
                loadChat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    //내 그룹 채팅방 로드
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
                    groupAdapter = new GroupAdapter(context, groupChatLists);
                    reCycleViewGroup.setAdapter(groupAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "해당 멤버가 아닙니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    //유저 리스트 로드
    private void loadChat() {
        userList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelUser user = ds.getValue(ModelUser.class);
                    for (ModelChatList chatlist : chatlistList) {
                        if (user.getUid() != null && user.getUid().equals(chatlist.getId())) {
                            userList.add(user);
                            break;
                        }
                    }
                }
                //adapter
                adapter = new ChatListAdapter(getContext(), userList);
                recyclerViewpersonal.setAdapter(adapter);
                //마지막 메세지 읽어오기
                for (int i = 0; i < userList.size(); i++) {
                    loadPersonalLastMessage(userList.get(i).getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //내 개인 채팅방 마지막 메세지 찾기
    private void loadPersonalLastMessage(final String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String thelastMeesage = "default";
                String lastMessageTime = "";
                int unreadMessageCount = 0;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    // Log.d("[test]","ds ref?"+ds.getRef());
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat == null) {
                        continue;
                    }
                    String sender = chat.getSender();
                    String receiver = chat.getReceiver();
                    if (sender == null || receiver == null) {
                        continue;
                    }
                    if (chat.getReceiver().equals(curruntUser.getUid()) && chat.getSender().equals(userId)
                            || chat.getReceiver().equals(userId) && chat.getSender().equals(curruntUser.getUid())) {

                        try {
                            if (chat.getType().equals("image")) {
                                thelastMeesage = "이미지 파일이 전송되었습니다.";
                            } else if (chat.getType().equals("video")) {
                                thelastMeesage = "동영상 파일이 전송되었습니다.";
                            } else if (chat.getType().equals("text") || chat.getType() == null) {
                                thelastMeesage = chat.getMessage();
                            } else {

                            }
                        } catch (Exception e) {
                            thelastMeesage = chat.getMessage();
                        }

                        lastMessageTime = CalendarHelper.getInstance().getRelativeTime(chat.getTimestamp());
                    }

                    if (chat.getReceiver().equals(curruntUser.getUid()) && chat.getSender().equals(userId) && !chat.isSeen) {
                        unreadMessageCount++;
                    }
                }

                adapter.setLastMessageMap(userId, thelastMeesage);
                adapter.setLastMessageTimeMap(userId, lastMessageTime);
                adapter.setUnreadMessageCount(userId, unreadMessageCount);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}