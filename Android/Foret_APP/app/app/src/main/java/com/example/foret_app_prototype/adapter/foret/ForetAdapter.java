package com.example.foret_app_prototype.adapter.foret;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.model.Foret;

import java.util.List;

public class ForetAdapter extends PagerAdapter {
    private Activity activity;
    private List<Foret> foretList;

    // 리스너 객체 참조를 저장하는 변수
    private OnClickListener clickListener = null;

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
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        Log.d("[TEST]", "instantiateItem() position : " + position);
        // main_fragment_home_foret_thum.xml에 설정된 클래스 객체 생성
        View itemView = activity.getLayoutInflater().inflate(R.layout.fragment_main_home_foret_thum, null);
        // 한 페이지에 보여줄 데이터 1개 꺼내기
        final Foret foret = foretList.get(position);

        ImageView image = itemView.findViewById(R.id.image);
        TextView title = itemView.findViewById(R.id.title);
        TextView desc = itemView.findViewById(R.id.desc);

//        Glide.with(activity).load(foret.getPhoto_name())
//                .placeholder(R.drawable.noimage)
//                .into(image);
        image.setImageResource(foret.getForetImage());
        title.setText(foret.getName());
        desc.setText(foret.getIntroduce());

        // 이벤트 설정
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(v, foret);
                }
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

        public interface OnClickListener {
            void onClick(View v, Foret foret);
        }

        // OnClickListener 객체 참조를 어댑터에 전달하는 메서드
        public void setOnClickListener(OnClickListener clickListener) {
            this.clickListener = clickListener;
        }
}
