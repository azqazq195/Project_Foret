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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class NotifyFragment extends Fragment implements View.OnClickListener {

    androidx.appcompat.widget.Toolbar toolbar;
    MainActivity activity;
    ListView listView;
    List<ModelNotify> notifyList;
    NotificationAdapter2 adapter;

    Context context;
    Fragment thisFragment;

    public NotifyFragment(Context context) {
        this.context = context;
    }

    public NotifyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notify, container, false);
        toolbar = (androidx.appcompat.widget.Toolbar) rootView.findViewById(R.id.notify_toolbar);
        activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(null);
        setHasOptionsMenu(true);
        //context = rootView.getContext();
        thisFragment = this;

        listView = rootView.findViewById(R.id.listView);

       /* View footer = getLayoutInflater().inflate(R.layout.footer, null, false);
        listView.addFooterView(footer);*/

        //LinearLayout layout = (LinearLayout)footer.findViewById(R.id.layout);
        setReaded();
        return rootView;
    }

    private void setReaded() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notify").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    boolean a = ds.child("isSeen").getValue(Boolean.class);
                    if(!a){
                        DatabaseReference userReading = FirebaseDatabase.getInstance().getReference("Notify").child(user.getUid());
                        HashMap<String, Object> read = new HashMap<>();
                        read.put("isSeen", true);
                        userReading.child(ds.getKey()).updateChildren(read);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getItem() {
        notifyList = new ArrayList<>();
        notifyList.clear();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notify").child(user.getUid());

        Query query = FirebaseDatabase.getInstance().getReference("Notify").child(user.getUid());
        query.orderByChild("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifyList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelNotify item = ds.getValue(ModelNotify.class);

                    adapter = new NotificationAdapter2(context, R.layout.item_row_notification, notifyList,thisFragment);
                    listView.setAdapter(adapter);
                    notifyList.add(item);

                }

                //Collections.sort(notifyList, Collections.reverseOrder());
                Log.e("[test]","최신 1128");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifyList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelNotify item = ds.getValue(ModelNotify.class);

                    adapter = new NotificationAdapter2(context, R.layout.item_row_notification, notifyList,thisFragment);
                    listView.setAdapter(adapter);
                    notifyList.add(item);

                }

                //Collections.sort(notifyList, Collections.reverseOrder());
                Log.e("[test]","최신 1128");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/
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
        getItem();
    }
}