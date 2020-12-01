package com.example.foret_app_prototype.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foret_app_prototype.R;

import java.util.ArrayList;
import java.util.List;

public class SearchLayoutActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, TextView.OnEditorActionListener {

    LinearLayout layout_search;
    ImageView button_back, button_searchINTO, button_reset;
    AutoCompleteTextView autoCompleteTextView;
    TextView button_searchGO;
    ListView search_list;
    List<String> autoCompleteList; //자동완성 데이터를 넣어줄 리스트
    //Button button;
    InputMethodManager inputMethodManager; //키보드 컨트롤 매니저
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        layout_search = findViewById(R.id.layout_search);
        button_back = findViewById(R.id.button_back);
        button_searchINTO = findViewById(R.id.button_searchINTO);
        button_reset = findViewById(R.id.button_reset);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        button_searchGO = findViewById(R.id.button_searchGO);
        search_list = findViewById(R.id.search_list);
        autoCompleteList = new ArrayList<String>();

        inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE); //키보드 등 입력받는 방법을 관리하는 Manager객체

        //layout_search.setVisibility(View.GONE);
        setAutoCompleteData(); //자동완성 데이터 세팅 함수
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, autoCompleteList));
        //autoCompleteTextView에 자동완성 데이터들이 담길 어댑터를 연결한다.

        autoCompleteTextView.setOnEditorActionListener(this);
        button_back.setOnClickListener(this);
        button_searchINTO.setOnClickListener(this);
        button_reset.setOnClickListener(this);
        button_searchGO.setOnClickListener(this);
        search_list.setOnItemClickListener(this);
        //button.setOnClickListener(this);

    }

    //자동완성에 사용될 데이터를 리스트에 추가한다.
    private void setAutoCompleteData() {
        for(int a=0; a<25; a++) {
            autoCompleteList.add("자동완성 테스트"+(a+1));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back : //뒤로가기버튼
                layout_search.setVisibility(View.GONE);
                autoCompleteTextView.setText("");
                //hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0) : autoCompleteTextView에 포커스가 있어 키보드가 나와있을 경우 키보드를 내린다.
                inputMethodManager.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
                break;
            case R.id.button_searchINTO :
                //toggleSoftInput : autoCompleteTextView에 포커스가 가며, 키보드 올리기
                autoCompleteTextView.requestFocus(); //autoCompleteTextView에 포커스 주기
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY); //키보드 올리기
                break;
            case R.id.button_searchGO:
                requsetSearch();
                break;
        }
    }

    private void requsetSearch() {
        String search_word = autoCompleteTextView.getText().toString().trim();
        Log.d("[TEST]", search_word);
        if(search_word.equals("")) { //입력검사
            Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        //autoCompleteTextView에 입력된 검색결과 요청하기
        Toast.makeText(this, search_word+"를 검색합니다.", Toast.LENGTH_SHORT).show();
        //검색 성공시 키보드를 내려줘야 한다.
        inputMethodManager.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, position+" / "+id+" 클릭", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH : //바뀐 엔터키 누를때(터치할때)
                requsetSearch();
                break;
            default: //키보드 엔터키 동작(키보드로는 엔터를 두번 눌러야함)
                requsetSearch();
        }
        return false;
    }
}