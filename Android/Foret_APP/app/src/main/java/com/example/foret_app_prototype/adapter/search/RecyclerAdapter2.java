package com.example.foret_app_prototype.adapter.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.search.SearchFragment;
import com.example.foret_app_prototype.activity.search.SearchResultActivity;
import com.example.foret_app_prototype.model.Test;
import com.loopj.android.http.RequestParams;

import java.util.List;

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.Adapter2> {

    List<String> list;
    Activity activity;
    SearchFragment parent;

    public RecyclerAdapter2(List<String> list, Context context, SearchFragment searchFragment) {
        this.list = list;
        activity = (Activity) context;
        parent = searchFragment;
    }

    @NonNull
    @Override
    public RecyclerAdapter2.Adapter2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item2, parent, false);
        return new Adapter2(holderView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter2.Adapter2 holder, int position) {
        String test = list.get(position);
        holder.button.setText("#" + test);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity,test+"클릭",Toast.LENGTH_SHORT).show();
                parent.search_keyword(test);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Adapter2 extends RecyclerView.ViewHolder {

        Button button;

        public Adapter2(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
        }
    }
}
