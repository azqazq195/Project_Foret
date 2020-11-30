package com.example.foret_app_prototype.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.free.ReadFreeActivity;
import com.example.foret_app_prototype.model.Test;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerAdapter4 extends RecyclerView.Adapter<RecyclerAdapter4.Adapter4> {

    List<Test> list;
    Activity activity;

    public RecyclerAdapter4(List<Test> list, Context context) {
        this.list = list;
        this.activity = (Activity)context;
    }

    @NonNull
    @Override
    public RecyclerAdapter4.Adapter4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item4, parent, false);
        return new Adapter4(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter4.Adapter4 holder, int position) {
        Test test = list.get(position);
        holder.textView1.setText(test.getTest1());
        holder.textView2.setText(test.getTest2());
        holder.textView3.setText(test.getTest3());
        holder.textView4.setText(test.getTest4());
        holder.textView5.setText(test.getTest5());
        holder.textView6.setText(test.getTest6());
        holder.textView7.setText(test.getTest7());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReadFreeActivity.class);
                intent.putExtra("test", test);
                activity.startActivity(intent);
            }
        });
        holder.button_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "좋아요 누르기", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Adapter4 extends RecyclerView.ViewHolder {

        TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
        Button button_like;
        LinearLayout layout;

        public Adapter4(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            textView6 = itemView.findViewById(R.id.textView6);
            textView7 = itemView.findViewById(R.id.textView7);
            button_like = itemView.findViewById(R.id.button_like);
            layout = itemView.findViewById(R.id.layout);
        }

    }
}
