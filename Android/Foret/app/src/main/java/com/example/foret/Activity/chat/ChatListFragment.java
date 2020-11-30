package com.example.foret.Activity.chat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret.R;
import com.example.foret.adapter.ChatListAdapter;
import com.example.foret.adapter.GroupAdapter;
import com.example.foret.helper.CalendarHelper;
import com.example.foret.model.ModelChat;
import com.example.foret.model.ModelChatList;
import com.example.foret.model.ModelGroupChatList;
import com.example.foret.model.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatListFragment extends Fragment implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModelChatList> chatlistList;
    List<ModelUser> userList;
    DatabaseReference reference;
    FirebaseUser curruntUser;

    TextView textViewforgroup;

    ChatListAdapter adapter;

    Button button_plus;
    SearchView button_search;

    //그룹구역
    RecyclerView groupReCycleView;

    List<ModelGroupChatList> groupChatLists;
    GroupAdapter groupAdapter;

    //그룹 정보
    String groupName;
    String member_id;
    Context context;

    public ChatListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        button_plus = view.findViewById(R.id.button_plus);
        button_search = view.findViewById(R.id.button_search);
        //유저 정보 받기
        firebaseAuth = FirebaseAuth.getInstance();
        curruntUser = FirebaseAuth.getInstance().getCurrentUser();

        textViewforgroup = view.findViewById(R.id.textViewforgroup);

        recyclerView = view.findViewById(R.id.my_recycler_view_personal);

        groupReCycleView = view.findViewById(R.id.groupReCycleView);
        firebaseAuth = FirebaseAuth.getInstance();


        context = container.getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        linearLayoutManager.setStackFromEnd(true);
        groupReCycleView.setHasFixedSize(true);
        groupReCycleView.setLayoutManager(linearLayoutManager);

        loadGroupChatList();
        loadPersonalChatList();

        return view;
    }


    //내 채팅방 로드
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
                            // Log.d("[test","리스트 만들어지는 중 "+userList.size());
                            break;
                        }
                    }
                }
                //adapter
                adapter = new ChatListAdapter(getContext(), userList);
                recyclerView.setAdapter(adapter);
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

    //내 채팅방 마지막 메세지 찾기
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


    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

        } else {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(groupChatLists.size()==0){
            //textViewforgroup.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_plus) {
            getFragmentManager().beginTransaction().replace(R.id.container, new UsersFragment(), "").commit();
        }
    }

    //그룹 채팅방 로드
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
                    groupReCycleView.setAdapter(groupAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "해당 멤버가 아닙니다.", Toast.LENGTH_LONG).show();
            }
        });
    }
}