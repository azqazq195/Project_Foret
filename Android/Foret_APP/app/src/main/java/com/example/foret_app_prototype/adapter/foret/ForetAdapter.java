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
import com.example.foret_app_prototype.model.HomeForetDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ForetAdapter extends PagerAdapter {
    private Activity activity;
    private List<HomeForetDTO> homeForetDTOList;
    String path;
//    private List<ForetDTO> foretDTOList;

    // 리스너 객체 참조를 저장하는 변수
    private OnClickListener clickListener = null;

    public ForetAdapter(Activity activity, List<HomeForetDTO> homeForetDTOList) {
        this.activity = activity;
        this.homeForetDTOList = homeForetDTOList;
    }

    // List 객체에 저장된 데이터 개수 리턴
    @Override
    public int getCount() {
        Log.d("[TEST]", "getCount() 호출 ");
        return homeForetDTOList.size();
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
        final HomeForetDTO homeForetDTO = homeForetDTOList.get(position);

        ImageView image = itemView.findViewById(R.id.image);

        String name = homeForetDTO.getName();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups").child(name);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("[test]", "name?" + name);
                path = snapshot.child("GroupPhoto").getValue()+"";
               Log.e("[test]", "path?" + path);
                Glide.with(activity).load(path)
                        .fallback(R.drawable.icon_defalut)
                        .into(image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        // 이벤트 설정
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(v, homeForetDTO);
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
        container.removeView((View) object);
    }

    public interface OnClickListener {
        void onClick(View v, HomeForetDTO homeForetDTO);
    }

    // OnClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
}