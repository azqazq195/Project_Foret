package com.example.foret_app_prototype.activity.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChatFragment extends Fragment implements View.OnClickListener {

    Toolbar toolbar;
    MainActivity activity;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView1, recyclerView2;
    LinearLayout layout_search;
    ImageView button_back;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        toolbar = (androidx.appcompat.widget.Toolbar) rootView.findViewById(R.id.toolbar);
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
                Toast.makeText(activity, "햄버거 메뉴 나타남", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(activity, "알아서 기능 넣기", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}