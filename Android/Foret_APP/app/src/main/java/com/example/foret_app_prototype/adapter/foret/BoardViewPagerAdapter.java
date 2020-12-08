package com.example.foret_app_prototype.adapter.foret;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.model.ForetBoardDTO;
import com.example.foret_app_prototype.model.MemberDTO;

import java.util.List;

//보드뷰 어답터에서사용/ 리드포레보드사용
public class BoardViewPagerAdapter extends PagerAdapter {
    private Activity activity;
    private String[] photo;

    public BoardViewPagerAdapter(Activity activity, String[] photo) {
        this.activity = activity;
        this.photo = photo;
    }

    @Override
    public int getCount() {
        return photo.length;
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
        View itemView = activity.getLayoutInflater().inflate(R.layout.board_view_pager, null);
        // 한 페이지에 보여줄 데이터 1개 꺼내기
        String str = photo[position];
        ImageView board_view_pager = itemView.findViewById(R.id.board_view_pager);

        Glide.with(activity).load(str)
                .placeholder(R.drawable.sss).into(board_view_pager);

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
