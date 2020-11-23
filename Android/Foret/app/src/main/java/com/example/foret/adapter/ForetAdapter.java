package com.example.foret.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.foret.R;
import com.example.foret.model.Foret;

import java.util.List;

public class ForetAdapter extends PagerAdapter {
    private Activity activity;
    private List<Foret> foretList;

    public ForetAdapter(Activity activity, List<Foret> foretList) {
        this.activity = activity;
        this.foretList = foretList;
    }

    // List 객체에 저장된 데이터 개수 리턴
    @Override
    public int getCount() {
        Log.d("[TEST]", "getCount() 호출 ");
        return foretList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        Log.d("[TEST]", "isViewFromObject() 호출 ");
        return view.equals(object);
    }

    // 뷰페이저가 만들어졌을 때 호출됨
    // => 뷰페이저가 호출
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.d("[TEST]", "instantiateItem() position : " + position);
        // main_fragment_home_foret_thum.xml에 설정된 클래스 객체 생성
        View itemView = activity.getLayoutInflater().inflate(R.layout.main_fragment_home_foret_thum, null);
        // 한 페이지에 보여줄 데이터 1개 꺼내기
        final Foret foret = foretList.get(position);

        ImageView image = itemView.findViewById(R.id.image);
        TextView title = itemView.findViewById(R.id.title);
        TextView desc = itemView.findViewById(R.id.desc);

        Glide.with(activity).load(foret.getPhoto_name())
                .placeholder(R.drawable.foret_no_image).into(image);
        title.setText(foret.getGroup_name());
        desc.setText(foret.getGroup_profile());

        // 이벤트 설정
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "'" + foret.getGroup_name()+ "'" + " 포레로 이동",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 컨테이너에 추가 (viewPager에 등록)
        container.addView(itemView, 0);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // viewPager에서 삭제
        Log.d("[TEST]", "destroyItem() position : " + position);
        container.removeView((View)object);
    }
}
