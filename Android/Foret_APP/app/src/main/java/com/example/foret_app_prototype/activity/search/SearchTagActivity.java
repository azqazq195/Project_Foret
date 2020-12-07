package com.example.foret_app_prototype.activity.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foret_app_prototype.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SearchTagActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText_name;
    ImageView button_cancel;
    Button button_complete;

    AsyncHttpClient client;
    TagMakeResponse tagMakeResponse;
    String url = "http://34.72.240.24:8085/foret/tag/tag_insert.do";

    String tag_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tag);

        // 상태바 색상 변경
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.foret4));

        editText_name = findViewById(R.id.editText_name);
        button_cancel = findViewById(R.id.button_cancel);
        button_complete = findViewById(R.id.button_complete);

        button_cancel.setOnClickListener(this);
        button_complete.setOnClickListener(this);

    }

    private void addTag() {
        tag_name = editText_name.getText().toString().trim();
        if(tag_name.equals("")) {
            Toast.makeText(this, "태그를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        client = new AsyncHttpClient();
        tagMakeResponse = new TagMakeResponse();
        RequestParams params = new RequestParams();
        params.put("tag_name", tag_name);
        client.post(url, params, tagMakeResponse);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel: // 취소
                finish();
                break;
            case R.id.button_complete: // 태그 만들기
                addTag();
                break;

        }
    }


    class TagMakeResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if (RT.equals("OK")) {
                    Toast.makeText(SearchTagActivity.this, "태그를 만들었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    Log.d("[TEST]", "태그 만듦");
                } else {
                    Toast.makeText(SearchTagActivity.this, "이미 존재하는 태그입니다.", Toast.LENGTH_SHORT).show();
                    Log.d("[TEST]", "태그 못만듦");
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(SearchTagActivity.this, "통신 에러", Toast.LENGTH_SHORT).show();
        }
    }
}