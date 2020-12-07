package com.example.foret_app_prototype.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;

import java.util.List;

public class AdapterImageForGuide extends PagerAdapter {

    Activity activity;
    List<String> list;

    public AdapterImageForGuide(Activity activity, List<String> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
           return  view.equals(object);
    }
    // 뷰페이저가 만들어졌을 때 호출됨
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.page_item, null);

        String item = list.get(position);


        ImageView imageView = itemView.findViewById(R.id.imageView8);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"번호 : "+item,Toast.LENGTH_SHORT).show();
            }
        });
        Glide.with(itemView).load(item).fallback(R.drawable.icon).into(imageView);
        container.addView(itemView, 0);
        return itemView;
    }

    //페이지 삭제하는 메서드
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
