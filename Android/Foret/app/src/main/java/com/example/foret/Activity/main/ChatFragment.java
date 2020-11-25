package com.example.foret.Activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foret.Activity.chat.TestLoginActivity;
import com.example.foret.R;

public class ChatFragment extends Fragment implements View.OnClickListener {

    Button buttongototestChat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.main_fragment_chat, container, false);
        buttongototestChat = view.findViewById(R.id.buttongototestChat);
        buttongototestChat.setOnClickListener(this);
                return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(),"버튼 눌림",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), TestLoginActivity.class);
        startActivity(intent);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }
}