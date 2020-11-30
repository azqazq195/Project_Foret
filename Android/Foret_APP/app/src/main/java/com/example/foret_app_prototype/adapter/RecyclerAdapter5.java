package com.example.foret_app_prototype.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.foret.ViewForetActivity;
import com.example.foret_app_prototype.model.Test;

import java.util.List;

public class RecyclerAdapter5 extends RecyclerView.Adapter<RecyclerAdapter5.Adapter5> {

    Activity activity;
    List<Test> list;

    public RecyclerAdapter5(Context context, List<Test> list) {
        this.activity = (Activity)context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerAdapter5.Adapter5 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item5, parent, false);
        return new Adapter5(holderView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter5.Adapter5 holder, int position) {
        Test test = list.get(position);
        holder.imageView.setImageResource(test.getResource());
        holder.textView1.setText(test.getTest1());
        holder.textView2.setText(test.getTest2());
        holder.textView3.setText(test.getTest3());
        holder.textView4.setText(test.getTest4());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ViewForetActivity.class);
                intent.putExtra("test", test);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Adapter5 extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1, textView2, textView3, textView4;
        LinearLayout layout;
        public Adapter5(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
