package com.example.foret.Activity.chat;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret.R;
import com.example.foret.adapter.ChatListAdapter;
import com.example.foret.model.ModelChat;
import com.example.foret.model.ModelChatList;
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
import java.util.Locale;


public class ChatListFragment extends Fragment implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModelChatList> chatlistList;
    List<ModelUser> userList;
    DatabaseReference reference;
    FirebaseUser curruntUser;

    ChatListAdapter adapter;

    Button button_plus;
    SearchView button_search;

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

        recyclerView = view.findViewById(R.id.my_recycler_view);

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

        return view;
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
                    lastMessage(userList.get(i).getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void lastMessage(final String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String thelastMeesage = "default";
                String lastMessageTime = "";
                for (DataSnapshot ds : snapshot.getChildren()) {
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
                        thelastMeesage = chat.getMessage();

                        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.KOREAN);
                        cal.setTimeInMillis(Long.parseLong(chat.getTimestamp()));
                        lastMessageTime = DateFormat.format("MM/dd hh:mm aa", cal).toString();

                    }
                }
                adapter.setLastMessageMap(userId, thelastMeesage);
                adapter.setLastMessageTimeMap(userId, lastMessageTime);
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
    public void onClick(View v) {
        if(v.getId()== R.id.button_plus){
            getFragmentManager().beginTransaction().replace(R.id.container,new UsersFragment(),"").commit();
        }
    }
}