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
import com.example.foret.model.Foret;
import com.example.foret.model.ForetBoard;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainFragment extends Fragment
        implements ViewPager.OnPageChangeListener, View.OnClickListener {
    ViewPager viewPager;
    List<Foret> foretList;
    ForetAdapter foretAdapter;
    Integer[] colors = null;
    ArgbEvaluator evaluator = new ArgbEvaluator();

    ImageView imageAd;

    ArrayList<ForetBoard> foretBoardList;
    BoardAdapter boardAdapter;
    RecyclerView recyclerView;

    AsyncHttpClient client;
    ForetBoardResponse foretBoardResponse;
    String url_foretBoard = "http://192.168.0.2:8081/project/foret/foret_board_list.do";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.main_fragment_home, container, false);
        Log.d("[TEST]", "메인프래그먼트 진입 ");
        foretList = new ArrayList<>();
        foretBoardList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            foretList = (List<Foret>) bundle.getSerializable("foretList");
            Log.d("[TEST]", "foretList => " + foretList.size());
            foretBoardList = (ArrayList<ForetBoard>) bundle.getSerializable("foretBoardList");
            Log.d("[TEST]", "foretBoardList => " + foretBoardList.size());
        }
        foretAdapter = new ForetAdapter(getActivity(), foretList);
        Log.d("[TEST]", "foretAdapter = new ForetAdapter(this, list) 다음 ");

        viewPager = rootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(foretAdapter);
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
        boardAdapter = new BoardAdapter(foretBoardList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(boardAdapter);

        // 아이템 로드
        boardAdapter.setItems(foretBoardList);

        imageAd = rootView.findViewById(R.id.imageAd);
        imageAd.setOnClickListener(this);

        boardAdapter.setOnItemClickListener(new BoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("[TEST]", "position => " + position);

                switch (position) {
                    case 0:
                        Toast.makeText(getActivity(),
                                foretBoardList.get(position).getBoard_subject()+ " 이동",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(),
                                foretBoardList.get(position).getBoard_subject()+ " 이동",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(),
                                foretBoardList.get(position).getBoard_subject()+ " 이동",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(),
                                foretBoardList.get(position).getBoard_subject()+ " 이동",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getActivity(),
                                foretBoardList.get(position).getBoard_subject()+ " 이동",
                                Toast.LENGTH_SHORT).show();
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position < (foretAdapter.getCount() -1) && position < (colors.length - 1)) {
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
        RequestParams param = new RequestParams();
        switch (position) {
            case 0 :
                client = new AsyncHttpClient();
                foretBoardResponse = new ForetBoardResponse();
                param.put("group_no", position+1);
                client.post(url_foretBoard, param, foretBoardResponse);
                break;
            case 1 :
                client = new AsyncHttpClient();
                foretBoardResponse = new ForetBoardResponse();
                param.put("group_no", position+1);
                client.post(url_foretBoard, param, foretBoardResponse);
                break;
            case 2 :
                client = new AsyncHttpClient();
                foretBoardResponse = new ForetBoardResponse();
                param.put("group_no", position+1);
                client.post(url_foretBoard, param, foretBoardResponse);
                break;
            case 3 :
                client = new AsyncHttpClient();
                foretBoardResponse = new ForetBoardResponse();
                param.put("group_no", position+1);
                client.post(url_foretBoard, param, foretBoardResponse);
                break;
            case 4 :
                client = new AsyncHttpClient();
                foretBoardResponse = new ForetBoardResponse();
                param.put("group_no", position+1);
                client.post(url_foretBoard, param, foretBoardResponse);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("[TEST]", "onPageScrollStateChanged 호출 : " + state);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "광고 이동", Toast.LENGTH_SHORT).show();
    }

    class ForetBoardResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            Log.d("[TEST]", "ForetBoard 통신 시작");
            foretBoardList.clear();
        }

        @Override
        public void onFinish() {
            Log.d("[TEST]", "ForetBoard 통신 종료");
            recyclerView.setAdapter(boardAdapter);
            boardAdapter.setItems(foretBoardList);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String rt = json.getString("rt");
                int total = json.getInt("total");
                JSONArray item = json.getJSONArray("item");

                for (int i = 0; i < item.length(); i++) {
                    JSONObject temp = item.getJSONObject(i);
                    ForetBoard foretBoard = new ForetBoard();
                    foretBoard.setGroup_no(temp.getInt("group_no"));
                    foretBoard.setBoard_no(temp.getInt("board_no"));
                    foretBoard.setBoard_type(temp.getInt("board_type"));
                    foretBoard.setBoard_writer(temp.getString("board_writer"));
                    foretBoard.setBoard_subject(temp.getString("board_subject"));
                    foretBoard.setBoard_content(temp.getString("board_content"));
                    foretBoard.setBoard_hit(temp.getInt("board_hit"));
                    foretBoard.setBoard_like_count(temp.getInt("board_like_count"));
                    foretBoard.setBoard_comment_count(temp.getInt("board_comment_count"));
                    foretBoard.setBoard_writed_date(temp.getString("board_writed_date"));
                    foretBoard.setBoard_edited_date(temp.getString("board_edited_date"));

                    String photo_name = temp.getString("photo_name");
                    if (photo_name.equals("")) photo_name = "0";
                    foretBoard.setPhoto_name(photo_name);

                    // list에 저장
                    foretBoardList.add(foretBoard);
                    Log.d("[TEST]", "foretBoardList 통신 성공 => " + foretBoardList.size());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getActivity(), "ForetBoard 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }
}