package com.example.foret_app_prototype.activity.free;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.foret_app_prototype.R;

public class ReadFreeActivity extends AppCompatActivity implements OnClickListener {

    Toolbar toolbar_writer, toolbar_noWriter;
    TextView textView_writer, textView_like, textView_subject, textView_date, textView_seq,
            textView_reply, textView_content, textView_comment;
    ToggleButton likeButton;
    ImageView button_cancel; //답글 닫기
    EditText editText_comment;
    RecyclerView recyclerView;
    Button button_input; //답글 달기 버튼
    String member_ID = "91989202";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_free);
        //툴바 설정
        toolbar_writer = findViewById(R.id.toolbar_writer);
        toolbar_noWriter = findViewById(R.id.toolbar_noWriter);
        setSupportActionBar(toolbar_writer); //테스트용이라 작성자용 툴바만 설정
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textView_writer = findViewById(R.id.textView_writer);
        textView_like = findViewById(R.id.textView_like); //좋아요 개수
        textView_subject = findViewById(R.id.textView_subject);
        textView_date = findViewById(R.id.textView_date);
        textView_seq = findViewById(R.id.textView_seq);
        textView_reply = findViewById(R.id.textView_reply);
        textView_comment = findViewById(R.id.textView_comment);
        textView_content = findViewById(R.id.textView_content);
        likeButton = findViewById(R.id.likeButton);
        button_cancel = findViewById(R.id.button_cancel);
        editText_comment = findViewById(R.id.editText_comment);
        recyclerView = findViewById(R.id.recyclerView);
        button_input = findViewById(R.id.button_input);

        button_cancel.setOnClickListener(this);
        button_input.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
            case R.id.item_modify :
                intent = new Intent(this, EditFreeActivity.class);
                intent.putExtra("수정할 데이터", "수정할 데이터");
                startActivity(intent);
                break;
            case R.id.item_delete :
                showDeleteDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("삭제 하시겠습니까?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //삭제하기
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {

    }
}