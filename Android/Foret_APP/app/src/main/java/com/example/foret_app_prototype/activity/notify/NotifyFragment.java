package com.example.foret_app_prototype.activity.notify;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.adapter.notification.NotificationAdapter2;
import com.example.foret_app_prototype.model.ModelNotify;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotifyFragment extends Fragment implements View.OnClickListener {

    androidx.appcompat.widget.Toolbar toolbar;
    MainActivity activity;
    ListView listView;
    List<ModelNotify> notifyList;
    NotificationAdapter2 adapter;
    String type;
    String sender ;
    String time ;
    String content;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notify, container, false);
        toolbar = (androidx.appcompat.widget.Toolbar) rootView.findViewById(R.id.notify_toolbar);
        activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(null);
        setHasOptionsMenu(true);
        context = rootView.getContext();

        listView = rootView.findViewById(R.id.listView);
        View footer = getLayoutInflater().inflate(R.layout.footer, null, false);
        listView.addFooterView(footer);
        //LinearLayout layout = (LinearLayout)footer.findViewById(R.id.layout);




        getItem();
        return rootView;
    }

    private void getItem() {
        notifyList = new ArrayList<>();
        notifyList.clear();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notify").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelNotify item = ds.getValue(ModelNotify.class);
                    Log.e("[test]", "ref??" + ref.getRef());
                    Log.e("[test]", "item???" + item.toString());
                    /*f (ds.child("sender").getValue().equals(user.getUid())) {
                      if(ds.child("receiver").getValue().equals(user.getUid())){
                        Log.e("[test]","ds.get벨류?"+ds.getValue());
                         type = ""+ds.child("type").getValue();
                         sender = ""+ds.child("sender").getValue();
                         time = ""+ds.child("time").getValue();
                         content = ""+ds.child("content").getValue();
                        Log.e("[test]","content?"+content);
                        }
                        */


                    adapter = new NotificationAdapter2(context, R.layout.item_row_notification, notifyList);
                    listView.setAdapter(adapter);

                    //notifyList.add(new ModelNotify(type,content,time,""+R.drawable.ic_launcher_foreground));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //notifyList.add(new ModelNotify("GROUP_NEW_ITEM", "그룹 새글이 등록됨", "" + System.currentTimeMillis(), "" + R.drawable.ic_launcher_foreground));
        //notifyList.add(new ModelNotify("PUBLIC_NOTICE_NEW_ITEM", "공지가 등록됨", "" + System.currentTimeMillis(), "" + R.drawable.ic_launcher_foreground));
        //notifyList.add(new ModelNotify("MESSAGE_NEW_ITEM", "새로운 메세지가 있음", "" + System.currentTimeMillis(), "" + R.drawable.ic_launcher_foreground));
        //notifyList.add(new ModelNotify("ANONYMOUS_BOARD_NEW_ITEM", "익명게시판 댓글이 등록됨", "" + System.currentTimeMillis(), "" + R.drawable.ic_launcher_foreground));


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.normal_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu) {
            DrawerLayout container = activity.findViewById(R.id.container);
            container.openDrawer(GravityCompat.END);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("[test]",notifyList.size()+"");
    }
}