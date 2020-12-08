package com.example.foret_app_prototype.activity.free;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.login.SessionManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class WriteFreeActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText editText_subject, editText_content;
    TextView textView_writer;
    int id;
    AsyncHttpClient client;
    FreeBoardWriteResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_free);

        // 상태바 색상 변경
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.foret4));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText_subject = findViewById(R.id.editText_subject);
        editText_content = findViewById(R.id.editText_content);
        textView_writer = findViewById(R.id.textView_writer);

        SessionManager sessionManager = new SessionManager(this);
        Log.e("[TEST]", sessionManager.getSession()+"출력");
        id = sessionManager.getSession();

        textView_writer.setText("작성자 : "+id);

        client = new AsyncHttpClient();
        response = new FreeBoardWriteResponse();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.complete_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
            case R.id.item_complete :
                if(editText_subject.getText().toString().trim().equals("")||editText_content.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "빈 항목을 채워주세요.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                RequestParams params = new RequestParams();
                params.put("writer", id);
                params.put("foret_id", 0);
                params.put("type", 0);
                params.put("subject", editText_subject.getText().toString().trim());
                params.put("content", editText_content.getText().toString().trim());
                try {
                    params.put("photo", (File) null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                params.setForceMultipartEntityContentType(true);
                client.post("http://34.72.240.24:8085/foret/board/board_insert.do", params, response);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class FreeBoardWriteResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getString("boardRT").equals("OK")) {
                    Toast.makeText(WriteFreeActivity.this, "글 올리기 성공", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(WriteFreeActivity.this, "글 올리기 실패", Toast.LENGTH_SHORT).show();
        }
    }


}