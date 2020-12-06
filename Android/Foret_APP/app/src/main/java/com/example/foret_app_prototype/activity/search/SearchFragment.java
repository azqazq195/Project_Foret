package com.example.foret_app_prototype.activity.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.activity.foret.MakeForetActivity;
import com.example.foret_app_prototype.activity.foret.ViewForetActivity;
import com.example.foret_app_prototype.adapter.RecyclerAdapter2;
import com.example.foret_app_prototype.adapter.RecyclerAdapter3;
import com.example.foret_app_prototype.adapter.search.SearchAdapter;
import com.example.foret_app_prototype.model.ForetDTO;
import com.example.foret_app_prototype.model.Test;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SearchFragment extends Fragment implements View.OnClickListener,
        TextView.OnEditorActionListener, AdapterView.OnItemClickListener {

    Toolbar toolbar;
    MainActivity activity;
    LinearLayout layout_search;
    ImageView button_back, button1, button_searchINTO, button_reset;
    Button button2, button3, button4, button5, button6, button7, button8;
    RecyclerView recyclerView1, recyclerView2;
    FloatingActionButton button9;
    SearchAdapter searchAdapter;
    RecyclerAdapter2 adapter2;
    RecyclerAdapter3 adapter3;
    Context context;
    AutoCompleteTextView autoCompleteTextView;
    TextView button_searchGO;
    ListView search_listView;
    List<String> autoCompleteList; //자동완성 데이터를 넣어줄 리스트
    List<ForetDTO> search_resultList;
    //Button button;
    InputMethodManager inputMethodManager; //키보드 컨트롤 매니저
    Intent intent;

    List<String> tag_name;
    List<String> foret_name;

    RecommandListResponse recommandListResponse;

    AsyncHttpClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        toolbar = (androidx.appcompat.widget.Toolbar) rootView.findViewById(R.id.search_toolbar);
        activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(null);
        setHasOptionsMenu(true);
        context = getContext();
        layout_search = rootView.findViewById(R.id.layout_search);
        button_back = rootView.findViewById(R.id.button_back);
        button1 = rootView.findViewById(R.id.button1); //내 관심태그 설정 페이지로 이동(햄버거 메뉴에 넣을 예정)
        button2 = rootView.findViewById(R.id.button2); //인기태그1
        button3 = rootView.findViewById(R.id.button3); //인기태그2
        button4 = rootView.findViewById(R.id.button4); //인기태그3
        button5 = rootView.findViewById(R.id.button5); //인기태그4
        button6 = rootView.findViewById(R.id.button6); //인기태그5
        button7 = rootView.findViewById(R.id.button7); //포레 만들기 화면으로 이동
        button8 = rootView.findViewById(R.id.button8); //갱신
        button9 = rootView.findViewById(R.id.button9); //갱신
        recyclerView1 = rootView.findViewById(R.id.recyclerView1); //관심태그 목록
        recyclerView2 = rootView.findViewById(R.id.recyclerView2); //인기포레 목록
        button_searchINTO = rootView.findViewById(R.id.button_searchINTO);
        button_reset = rootView.findViewById(R.id.button_reset);
        autoCompleteTextView = rootView.findViewById(R.id.autoCompleteTextView);
        button_searchGO = rootView.findViewById(R.id.button_searchGO);
        search_listView = rootView.findViewById(R.id.search_list); //검색 결과가 출력될 리스트뷰
        tag_name = new ArrayList<>();
        foret_name = new ArrayList<>();
        search_resultList = new ArrayList<>();
        searchAdapter = new SearchAdapter(activity, R.layout.recycle_item3, search_resultList);


        autoCompleteList = new ArrayList<String>(); //자동완성에 사용할 데이터

        client = new AsyncHttpClient();
        recommandListResponse = new RecommandListResponse();

        inputMethodManager = (InputMethodManager)activity.getSystemService(INPUT_METHOD_SERVICE); //키보드 등 입력받는 방법을 관리하는 Manager객체

        layout_search.setVisibility(View.GONE);

        autoCompleteTextView.setOnEditorActionListener(this);
        button_back.setOnClickListener(this);
        button_searchINTO.setOnClickListener(this);
        button_reset.setOnClickListener(this);
        button_searchGO.setOnClickListener(this);
        search_listView.setOnItemClickListener(this);

        recyclerView1.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        search_listView.setAdapter(searchAdapter);

        testData1();
        testData2();

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        search_listView.setOnItemClickListener(this);

        button_back.setOnClickListener(this);

        client.post("", recommandListResponse);

        return rootView;
    }

    //자동완성에 사용될 데이터를 리스트에 추가한다
    private void setAutoCompleteData() {
        client.post("http://34.72.240.24:8085/foret/region/tag_list.do", new TagListResponse4());
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, autoCompleteList));
        //autoCompleteTextView에 자동완성 데이터들이 담길 어댑터를 연결한다.
    }

    private void testData2() {
        int resource[] = {R.drawable.icon, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4,
                R.drawable.icon5, R.drawable.icon, R.drawable.icon4, R.drawable.icon2, R.drawable.icon5};
        for (int a = 0; a < resource.length; a++) {
            Test test = new Test();
            test.setResource(resource[a]);
            test.setTest1("포레 이름");
            test.setTest2("#태그1, #태그2, #태그3...");
            test.setTest3("여러분 화이팅 합시다 할수있어요!!");
            test.setTest4("20-12-02");
            test.setTest5("지역 : 성수동, 용산구");
          //  list2.add(test);
        }//
       // adapter3 = new RecyclerAdapter3(list2, activity);
        recyclerView2.setAdapter(adapter3);
    }

    private void testData1() {
        String testArray[] = {"디자인", "코딩", "영어", "다이어트", "조깅", "성수동"};
        for (int a = 0; a < testArray.length; a++) {
            Test test = new Test();
            test.setTest1(testArray[a]);
         //   list1.add(test);
        }
       // adapter2 = new RecyclerAdapter2(list1, activity);
        recyclerView1.setAdapter(adapter2);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_fragment_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_search:
                layout_search.setVisibility(View.VISIBLE);
                break;
            case R.id.item_menu: //햄버거 메뉴 열기
                DrawerLayout container = activity.findViewById(R.id.container);
                container.openDrawer(GravityCompat.END);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button1: //내 관심 설정페이지로 이동
                Toast.makeText(activity, "내 관심 설정 페이지로 이동", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2: //인기태그1 검색화면으로 이동
                intent = new Intent(activity, SearchResultActivity.class);
                intent.putExtra("tag", "공부");
                activity.startActivity(intent);
                break;
            case R.id.button3: //인기태그2 검색화면으로 이동
                intent = new Intent(activity, SearchResultActivity.class);
                intent.putExtra("tag", "코딩");
                activity.startActivity(intent);
                break;
            case R.id.button4: //인기태그3 검색화면으로 이동
                intent = new Intent(activity, SearchResultActivity.class);
                intent.putExtra("tag", "영어");
                activity.startActivity(intent);
                break;
            case R.id.button5: //인기태그4 검색화면으로 이동
                intent = new Intent(activity, SearchResultActivity.class);
                intent.putExtra("tag", "아이패드");
                activity.startActivity(intent);
                break;
            case R.id.button6: //인기태그5 검색화면으로 이동
                intent = new Intent(activity, SearchResultActivity.class);
                intent.putExtra("tag", "영어회화");
                activity.startActivity(intent);
                break;
            case R.id.button7: //포레 만들기 화면으로 이동
                goToMakeNewForet();
                break;
            case R.id.button8: //갱신버튼
                Toast.makeText(activity, "누르면 항목이 바껴요", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button9: //포레 만들기 화면으로 이동
                goToMakeNewForet();
                break;
            case R.id.button_back:
                layout_search.setVisibility(View.GONE);
                break;
            case R.id.button_searchINTO :
                //toggleSoftInput : autoCompleteTextView에 포커스가 가며, 키보드 올리기
                autoCompleteTextView.requestFocus(); //autoCompleteTextView에 포커스 주기
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY); //키보드 올리기
                break;
            case R.id.button_searchGO:
                requsetSearch(); //서버에 검색 요청
                break;
        }
    }

    //검색결과 요청
    private void requsetSearch() {
        String search_word = autoCompleteTextView.getText().toString().trim();
        if(search_word.equals("")) { //입력검사
            Toast.makeText(activity, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        //autoCompleteTextView에 입력된 검색결과 요청하기
        Toast.makeText(activity, search_word+"를 검색합니다.", Toast.LENGTH_SHORT).show();
        //검색 성공시 키보드를 내려줘야 한다.
        inputMethodManager.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
    }

    private void goToMakeNewForet() {
        Intent intent = new Intent(activity, MakeForetActivity.class);
        activity.startActivity(intent);
    }

    //검색결과 레이아웃에 출력된 것을 누르면 이동
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ForetDTO foretDTO = searchAdapter.getItem(position);
        Intent intent = new Intent(activity, ViewForetActivity.class);
        intent.putExtra("foret_id", foretDTO.getForet_id());
        startActivity(intent);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH :
                requsetSearch();
                break;
            default: //키보드로 엔터를 쳤을때
                requsetSearch();
        }
        return false;
    }

    //추천포레 리스트
    class RecommandListResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "서버통신 에러 추천목록 못가져옴", Toast.LENGTH_SHORT).show();
        }
    }

    //자동완성용 모든 태그 목록 불러오기
    class TagListResponse4 extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if (json.getInt("total") != 0) {
                    JSONArray tag = json.getJSONArray("tag");
                    for (int a = 0; a < tag.length(); a++) {
                        JSONObject object = tag.getJSONObject(a);
                        tag_name.add(object.getString("tag_name"));
                        autoCompleteList.add(object.getString("tag_name"));
                    }
                    final int DEFAULT_TIME = 20*1000;
                    client.setConnectTimeout(DEFAULT_TIME);
                    client.setResponseTimeout(DEFAULT_TIME);
                    client.setTimeout(DEFAULT_TIME);
                    client.setResponseTimeout(DEFAULT_TIME);
                    client.post("http://34.72.240.24:8085/foret/foret/foret_name_list.do", new ForetNameListResponse());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "서버통신 에러 태그 이름 못가져옴", Toast.LENGTH_SHORT).show();
        }
    }

    class ForetNameListResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if (json.getInt("total") != 0) {
                    JSONArray foret = json.getJSONArray("foret");
                    for (int a = 0; a < foret.length(); a++) {
                        JSONObject object = foret.getJSONObject(a);
                        foret_name.add(object.getString("foret_name"));
                        autoCompleteList.add(object.getString("foret_name"));
                    }
                    autoCompleteTextView.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, autoCompleteList));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "서버통신 에러 포레이름 못가져옴", Toast.LENGTH_SHORT).show();
        }
    }
}