package com.example.foret.Activity.main;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.foret.R;
import com.example.foret.adapter.BoardAdapter;
import com.example.foret.adapter.ForetAdapter;
import com.example.foret.model.Board;
import com.example.foret.model.Foret;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment
        implements ViewPager.OnPageChangeListener, View.OnClickListener {
    ViewPager viewPager;
    ForetAdapter adapter;
    List<Foret> list;
    Integer[] colors = null;
    ArgbEvaluator evaluator = new ArgbEvaluator();

    BoardAdapter boardAdapter = new BoardAdapter();
    RecyclerView recyclerView;

    ImageView imageAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.main_fragment_home, container, false);
        list = new ArrayList<>();
        addData();
        adapter = new ForetAdapter(getActivity(), list);
        Log.d("[TEST]", "adapter = new ForetAdapter(this, list) 다음 ");
        viewPager = rootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        Log.d("[TEST]", "viewPager.setAdapter(adapter) 다음 ");

        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color5)
        };
        colors = colors_temp;

        viewPager.setOnPageChangeListener(this);

        // recycleView 초기화
        recyclerView = rootView.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(boardAdapter);

        // 아이템 로드
        boardAdapter.setItems(addBoard1());

        imageAd = rootView.findViewById(R.id.imageAd);
        imageAd.setOnClickListener(this);

        boardAdapter.setOnItemClickListener(new BoardAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("[TEST]", "position => " + position);

                switch (position) {
                    case 0:
                        Toast.makeText(getActivity(), "게시글1 이동", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "게시글2 이동", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "게시글3 이동", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "게시글4 이동", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getActivity(), "게시글5 이동", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
            viewPager.setBackgroundColor(
                    (Integer) evaluator.evaluate(
                            positionOffset,
                            colors[position],
                            colors[position + 1]
                    )
            );
        }
        else {
            viewPager.setBackgroundColor(colors[colors.length - 1]);
        }
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("[TEST]", "onPageSelected 호출 : " + position);
        recyclerView.setAdapter(boardAdapter);
        switch (position) {
            case 0 :
                boardAdapter.setItems(addBoard1());
                break;
            case 1 :
                boardAdapter.setItems(addBoard2());
                break;
            case 2 :
                boardAdapter.setItems(addBoard3());
                break;
            case 3 :
                boardAdapter.setItems(addBoard4());
                break;
            case 4 :
                boardAdapter.setItems(addBoard5());
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("[TEST]", "onPageScrollStateChanged 호출 : " + state);
    }

    private void addData() {
        list.add(new Foret("모임1", "모임내용1", R.drawable.iuu));
        list.add(new Foret("모임2", "모임내용2", R.drawable.ui));
        list.add(new Foret("모임3", "모임내용3", R.drawable.iu10));
        list.add(new Foret("모임4", "모임내용4", R.drawable.iu5));
        list.add(new Foret("모임5", "모임내용5", R.drawable.iu4));
    }

    private ArrayList<Board> addBoard1() {
        ArrayList<Board> item1 = new ArrayList<>();
        item1.add(new Board(R.drawable.iu4,"포레1게시글1", "게시글내용1게시글내용1게시글내용1게시글내용1게시글내용1게시글내용1게시글내용1게시글내용1게시글내용1게시글내용1게시글내용1게시글내용1게시글내용1게시글내용1게시글내용1게시글내용1"));
        item1.add(new Board(R.drawable.iuu,"포레1게시글2", "게시글내용2"));
        item1.add(new Board(R.drawable.iu10,"포레1게시글3", "게시글내용3"));
        item1.add(new Board(R.drawable.iu5,"포레1게시글4", "게시글내용4"));
        item1.add(new Board(R.drawable.ui,"포레1게시글5", "게시글내용5"));
        return item1;
    }

    private ArrayList<Board> addBoard2() {
        ArrayList<Board> item2 = new ArrayList<>();
        item2.add(new Board(R.drawable.iu5,"포레2게시글1", "게시글내용1"));
        item2.add(new Board(R.drawable.iu10,"포레2게시글2", "게시글내용2"));
        item2.add(new Board(R.drawable.iuu,"포레2게시글3", "게시글내용3"));
        item2.add(new Board(R.drawable.iu4,"포레2게시글4", "게시글내용4"));
        item2.add(new Board(R.drawable.ui,"포레2게시글5", "게시글내용5"));
        return item2;
    }

    private ArrayList<Board> addBoard3() {
        ArrayList<Board> item3 = new ArrayList<>();
        item3.add(new Board(R.drawable.iu10,"포레3게시글1", "게시글내용1"));
        item3.add(new Board(R.drawable.iu4,"포레3게시글2", "게시글내용2"));
        item3.add(new Board(R.drawable.iu5,"포레3게시글3", "게시글내용3"));
        item3.add(new Board(R.drawable.iuu,"포레3게시글4", "게시글내용4"));
        item3.add(new Board(R.drawable.ui,"포레3게시글5", "게시글내용5"));
        return item3;
    }

    private ArrayList<Board> addBoard4() {
        ArrayList<Board> item4 = new ArrayList<>();
        item4.add(new Board(R.drawable.ui,"포레4게시글1", "게시글내용1"));
        item4.add(new Board(R.drawable.iu5,"포레4게시글2", "게시글내용2"));
        item4.add(new Board(R.drawable.iu4,"포레4게시글3", "게시글내용3"));
        item4.add(new Board(R.drawable.iuu,"포레4게시글4", "게시글내용4"));
        item4.add(new Board(R.drawable.iu10,"포레4게시글5", "게시글내용5"));
        return item4;
    }

    private ArrayList<Board> addBoard5() {
        ArrayList<Board> item5 = new ArrayList<>();
        item5.add(new Board(R.drawable.iuu,"포레5게시글1", "게시글내용1"));
        item5.add(new Board(R.drawable.iu4,"포레5게시글2", "게시글내용2"));
        item5.add(new Board(R.drawable.iu5,"포레5게시글3", "게시글내용3"));
        item5.add(new Board(R.drawable.iu10,"포레5게시글4", "게시글내용4"));
        item5.add(new Board(R.drawable.ui,"포레5게시글5", "게시글내용5"));
        return item5;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "광고 이동", Toast.LENGTH_SHORT).show();
    }
}