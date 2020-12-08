package com.example.foret_app_prototype.adapter.search;

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
import com.example.foret_app_prototype.model.MemberDTO;
import com.example.foret_app_prototype.model.Test;

import java.util.Arrays;
import java.util.List;

public class RecyclerAdapter3 extends RecyclerView.Adapter<RecyclerAdapter3.Adapter3> {

    List<ForetDTO> list;
    Activity activity;
    View holderView;
    MemberDTO memberDTO;

    public RecyclerAdapter3(List<ForetDTO> list, Context context,MemberDTO memberDTO) {
        this.list = list;
        this.activity = (Activity)context;
        this.memberDTO = memberDTO;
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

       // Log.e("[test]","리사이클 어뎁터 3에서 포토 루트?"+foret.getForet_photo());
        //Log.e("[test]","리사이클 어뎁터 3에서 포레명??"+foret.getForet_name());

        String result = foret.getForet_photo().substring(foret.getForet_photo().lastIndexOf("/")+1);
        //Log.e("[test]", "섭스트링값?" +result);
        if(result.equals("null")){
            Glide.with(holderView).load(R.drawable.icon_foret).placeholder(R.drawable.icon)
                    .into(holder.imageView);
        }else {
            Glide.with(holderView).load(foret.getForet_photo())
                    .placeholder(R.drawable.icon)
                    .fallback(R.drawable.icon_foret)
                    .into(holder.imageView);
        }

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
                intent.putExtra("memberDTO", memberDTO);
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
