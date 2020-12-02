package com.example.foret_app_prototype.activity.notify;

import android.os.Bundle;
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

public class NotifyFragment extends Fragment implements View.OnClickListener {

    androidx.appcompat.widget.Toolbar toolbar;
    MainActivity activity;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notify, container, false);
        toolbar = (androidx.appcompat.widget.Toolbar) rootView.findViewById(R.id.notify_toolbar);
        activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(null);
        setHasOptionsMenu(true);

        listView = rootView.findViewById(R.id.listView);
        View footer = getLayoutInflater().inflate(R.layout.footer, null, false);
        listView.addFooterView(footer);
        LinearLayout layout = (LinearLayout)footer.findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "누르면 항목 추가되는 기능 구현 계획", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.normal_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu) {
            DrawerLayout container = activity.findViewById(R.id.container);
            container.openDrawer(GravityCompat.END);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}