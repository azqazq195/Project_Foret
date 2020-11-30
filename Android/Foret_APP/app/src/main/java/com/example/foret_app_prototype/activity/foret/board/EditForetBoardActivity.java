package com.example.foret_app_prototype.activity.foret.board;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.helper.FileUtils;
import com.example.foret_app_prototype.helper.PhotoHelper;
import com.example.foret_app_prototype.model.ForetBoard;
import com.example.foret_app_prototype.model.Member;

import java.io.File;

public class EditForetBoardActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    // 데이터
    Member member;
    ForetBoard foretBoard;

    Spinner spinner_board_type;
    TextView textView_writer;
    EditText editText_subject, editText_content;
    ImageView[] imageView = new ImageView[5];

    // 스피너 인덱스
    int selectedIndex = 0;

    Intent intent = null;
    String filePath = null;

    int image_count = 0;
    String[] str_boardImage = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_foret_board);

        Toolbar toolbar = findViewById(R.id.cosutom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기존 타이틀 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        foretBoard = (ForetBoard) getIntent().getSerializableExtra("foretBoard");

        getFindbyId(); // 객체 초기화

        spinner_board_type.setSelection((foretBoard.getType()-1));
        textView_writer.setText(foretBoard.getWriter());
        editText_subject.setText(foretBoard.getSubject());
        editText_content.setText(foretBoard.getContent());
        for(int i=0; i<foretBoard.getBoard_photo().length; i++) {
            imageView[i].setImageResource(foretBoard.getBoradImage());
        }
    }

    private void getFindbyId() {
        spinner_board_type = findViewById(R.id.spinner_board_type);
        textView_writer = findViewById(R.id.textView_writer);
        editText_subject = findViewById(R.id.editText_subject);
        editText_content = findViewById(R.id.editText_content);
        imageView[0] = findViewById(R.id.imageView0);
        imageView[1] = findViewById(R.id.imageView1);
        imageView[2] = findViewById(R.id.imageView2);
        imageView[3] = findViewById(R.id.imageView3);
        imageView[4] = findViewById(R.id.imageView4);
        for (int i=0; i<imageView.length; i++) {
            Log.d("[TEST]", "imageView.length => " + imageView.length);
            Log.d("[TEST]", "foretBoard.getBoard_photo().length => " + foretBoard.getBoard_photo().length);
            imageView[i].setOnClickListener(this);
        }
        spinner_board_type.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView0:
                image_count = 0;
                showSelect();
                break;
            case R.id.imageView1:
                image_count = 1;
                showSelect();
                break;
            case R.id.imageView2:
                image_count = 2;
                showSelect();
                break;
            case R.id.imageView3:
                image_count = 3;
                showSelect();
                break;
            case R.id.imageView4:
                image_count = 4;
                showSelect();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //spinner의 선택된 위치 가져오기
        selectedIndex = spinner_board_type.getSelectedItemPosition();
        Log.d("[TEST]", "selectedIndex+1 => " + (selectedIndex+1));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // 툴바 메뉴 버튼 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_complete : // 완료 버튼
                if(getComplete()) {
                    modifyCheck();
                }
                break;
            case android.R.id.home : // 뒤로가기 버튼
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean getComplete() {
        String subject = editText_subject.getText().toString().trim();
        String content = editText_content.getText().toString().trim();
        if(subject.equals("")) {
            Toast.makeText(this, "제목을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(content.equals("")) {
            Toast.makeText(this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        foretBoard.setType(selectedIndex+1);
        foretBoard.setSubject(subject);
        foretBoard.setContent(content);
        return true;
    }

    private void modifyCheck() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("수정 하시겠습니까?");
        builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "게시글을 수정했습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("member", member);
                intent.putExtra("foretBoard", foretBoard);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showSelect() {
        final String [] menu = {"새로 촬영하기", "갤러리에서 가져오기"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: //새로 촬영하기-카메라 호출
                        filePath = PhotoHelper.getInstance().getNewPhotoPath(); //저장할 사진 경로
                        Log.d("[TEST]", "photoPath = " + filePath);

                        File file = new File(filePath);
                        Uri uri = null;

                        //카메라앱 호출을 위한 암묵적 인텐트 (action과 uri가 필요하다)
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(EditForetBoardActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        Log.d("[TEST]", "uri : "+uri.toString());

                        //저장할 경로를 파라미터로 설정
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        intent.putExtra(AUDIO_SERVICE, false);

                        //카메라 앱 호출
                        startActivityForResult(intent, 200);
                        break;
                    case 1: //갤러리에서 가져오기-갤러리 호출
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*"); //모든 이미지 표시
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        startActivityForResult(intent, 300); //선택된 파일을 돌려받아야함
                        break;
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    Toast.makeText(this, "사진 첨부 완료", Toast.LENGTH_SHORT).show();
                    //촬영 결과물을 MediaStore에 등록한다(갤러리에 저장). MediaStore에 등록하지 않으면 우리 앱에서 만든 파일을 다른 앱에서는 사용할 수 없다.
                    intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(filePath));
                    Log.d("[TEST]", filePath);
                    sendBroadcast(intent);
                    insertImage();
                    break;
                case 300 :
                    String uri = data.getData().toString();
                    String fileName = uri.substring(uri.lastIndexOf("/")+1);
                    Log.d("[TEST]", "fileName = " + fileName);
                    filePath = FileUtils.getPath(this, data.getData());
                    Log.d("[TEST]", "filePath = " + filePath);
                    Toast.makeText(this, fileName+"을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    insertImage();
            }
        }
    }

    // 메뉴 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 옵션 메뉴 추가
        getMenuInflater().inflate(R.menu.complete_toolbar, menu);
        return true;
    }

    private void insertImage() {
        if(image_count == 0) {
            str_boardImage[0] = filePath;
            Glide.with(this).load(filePath).into(imageView[0]);
        } else if (image_count == 1) {
            str_boardImage[1] = filePath;
            Glide.with(this).load(filePath).into(imageView[1]);
        } else if (image_count == 2) {
            str_boardImage[2] = filePath;
            Glide.with(this).load(filePath).into(imageView[2]);
        } else if (image_count == 3) {
            str_boardImage[3] = filePath;
            Glide.with(this).load(filePath).into(imageView[3]);
        } else if (image_count == 4) {
            str_boardImage[4] = filePath;
            Glide.with(this).load(filePath).into(imageView[4]);
        }
        Log.d("[TEST]", "str_boardImage.length() => " + str_boardImage.length);
    }
}