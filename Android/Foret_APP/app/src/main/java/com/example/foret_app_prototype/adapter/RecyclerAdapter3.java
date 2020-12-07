package com.example.foret_app_prototype.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.foret.ViewForetActivity;
import com.example.foret_app_prototype.model.ForetDTO;
import com.example.foret_app_prototype.model.Test;

import java.util.Arrays;
import java.util.List;

public class RecyclerAdapter3 extends RecyclerView.Adapter<RecyclerAdapter3.Adapter3> {

    List<ForetDTO> list;
    Activity activity;
    View holderView;

    public RecyclerAdapter3(List<ForetDTO> list, Context context) {
        this.list = list;
        this.activity = (Activity)context;
    }

    @NonNull
    @Override
    public RecyclerAdapter3.Adapter3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item3, parent, false);
        return new Adapter3(holderView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter3.Adapter3 holder, int position) {
        ForetDTO foret = list.get(position);
        String [] tag_name = foret.getForet_tag().toArray(new String[foret.getForet_tag().size()]);
        String [] si = foret.getForet_region_si().toArray(new String[foret.getForet_region_si().size()]);
        String [] gu = foret.getForet_region_gu().toArray(new String[foret.getForet_region_gu().size()]);

        Glide.with(holderView).load(foret.getForet_photo()).into(holder.imageView);
        holder.textView1.setText(foret.getForet_name());
        holder.textView2.setText(Arrays.toString(tag_name));
        holder.textView3.setText(foret.getIntroduce());
        holder.textView4.setText(foret.getReg_date());
        holder.textView5.setText(Arrays.toString(si)+Arrays.toString(gu));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ViewForetActivity.class);
                intent.putExtra("foret_id", foret.getForet_id());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Adapter3 extends RecyclerView.ViewHolder {
        LinearLayout layout;
        ImageView imageView;
        TextView textView1, textView2, textView3, textView4, textView5;
        public Adapter3(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
