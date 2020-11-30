package com.example.foret_app_prototype.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.foret.ViewForetActivity;
import com.example.foret_app_prototype.model.Test;

import java.util.List;

public class RecyclerAdapter1 extends RecyclerView.Adapter<RecyclerAdapter1.Adapter1> {

    List<Test> list;
    Activity activity;

    public RecyclerAdapter1(List<Test> list, Context context) {
        this.list = list;
        activity = (Activity)context;
    }

    @NonNull
    @Override
    public Adapter1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item1, parent, false);
        return new Adapter1(holderView);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter1 holder, int position) {
        Test test = list.get(position);
        holder.imageView.setImageResource(test.getResource());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ViewForetActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Adapter1 extends RecyclerView.ViewHolder {
        ImageView imageView;
        public Adapter1(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
