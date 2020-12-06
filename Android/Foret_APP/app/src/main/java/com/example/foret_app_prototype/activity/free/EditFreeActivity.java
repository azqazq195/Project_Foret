package com.example.foret_app_prototype.activity.free;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.login.SessionManager;
import com.example.foret_app_prototype.model.ForetBoard;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class EditFreeActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText editText_content, editText_subject;
    TextView textView_writer;
    int id ;
    AsyncHttpClient client;
    FreeBoardEditResponse response;
    ForetBoard foretBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_free);
        toolbar = findViewById(R.id.cosutom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foretBoard = (ForetBoard) getIntent().getSerializableExtra("foretBoard");

        editText_subject = findViewById(R.id.editText_subject);
        editText_content = findViewById(R.id.editText_content);
        textView_writer = findViewById(R.id.textView_writer);

        editText_subject.setText(foretBoard.getSubject());
        editText_content.setText(foretBoard.getContent());
        textView_writer.setText("작성자 : "+foretBoard.getId());

        SessionManager sessionManager = new SessionManager(this);
        id = sessionManager.getSession();
        client = new AsyncHttpClient();
        response = new FreeBoardEditResponse();

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
                    return false;
                }
                foretBoard.setSubject(editText_subject.getText().toString().trim());
                foretBoard.setContent(editText_content.getText().toString().trim());
                RequestParams params = new RequestParams();
                params.put("id", foretBoard.getId());
                params.put("writer", foretBoard.getWriter());
                params.put("type", 0);
                params.put("hit", foretBoard.getHit());
                params.put("subject", editText_subject.getText().toString().trim());
                params.put("content", editText_content.getText().toString().trim());
                params.setForceMultipartEntityContentType(true);
                client.post("http://34.72.240.24:8085/foret/board/board_modify.do", params, response);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class FreeBoardEditResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getString("boardRT").equals("OK")) {
                    Toast.makeText(EditFreeActivity.this, "수정 완료했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(EditFreeActivity.this, "글 수정 실패", Toast.LENGTH_SHORT).show();
        }
    }
}