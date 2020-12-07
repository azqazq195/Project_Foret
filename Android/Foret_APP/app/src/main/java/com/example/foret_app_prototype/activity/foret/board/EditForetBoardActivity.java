package com.example.foret_app_prototype.activity.foret.board;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.helper.FileUtils;
import com.example.foret_app_prototype.helper.PhotoHelper;
import com.example.foret_app_prototype.model.ForetBoardDTO;
import com.example.foret_app_prototype.model.MemberDTO;
import com.example.foret_app_prototype.model.ReadForetDTO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class EditForetBoardActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    // 데이터
    MemberDTO memberDTO;
    ReadForetDTO readForetDTO;

    AsyncHttpClient client;
    EditForetBoardResponse editForetBoardResponse;
    String url = "";

    File file;
    Spinner spinner_board_type;
    TextView textView_writer;
    EditText editText_subject, editText_content;
    ImageView imageView0, imageView1, imageView2, imageView3, imageView4;
    Button button12;

    // 스피너 인덱스
    int selectedIndex = 0;

    Intent intent;
    String filePath;

    int type = 0;
    int image_count = 0;
    String[] str_boardImage = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_foret_board);

        // 상태바 색상 변경
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.foret4));

        Toolbar toolbar = findViewById(R.id.cosutom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기존 타이틀 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        readForetDTO = (ReadForetDTO) getIntent().getSerializableExtra("readForetDTO");
//        memberDTO = (MemberDTO) getIntent().getSerializableExtra("memberDTO");

        getFindbyId(); // 객체 초기화

        dataSetting(); // 데이터 세팅

    }

    private void dataSetting() {
        if(readForetDTO.getType() == 2) {
            type = 1;
        } else if (readForetDTO.getType() == 4) {
            type = 2;
        } else {
            Toast.makeText(this, "언노운", Toast.LENGTH_SHORT).show();
        }
        spinner_board_type.setSelection(type);
        textView_writer.setText(readForetDTO.getWriter_nickname());
        editText_subject.setText(readForetDTO.getSubject());
        editText_content.setText(readForetDTO.getContent());
        if(readForetDTO.getPhoto().size() > 0) { // 사진이 있으면
            for(int a=0; a<readForetDTO.getPhoto().size(); a++) {
                str_boardImage[a] = readForetDTO.getPhoto().get(a);
            }
        }
        Log.d("[TEST]", "사진 랭스 : " + str_boardImage.length);
//        if(str_boardImage.length == 1) {
//            Glide.with(this).load(readForetDTO.getPhoto().get(0)).into(imageView0);
//            image_count = 1;
//        } else if (str_boardImage.length == 2) {
//            Glide.with(this).load(readForetDTO.getPhoto().get(0)).into(imageView0);
//            Glide.with(this).load(readForetDTO.getPhoto().get(1)).into(imageView1);
//            image_count = 2;
//        } else if (str_boardImage.length == 3) {
//            Glide.with(this).load(readForetDTO.getPhoto().get(0)).into(imageView0);
//            Glide.with(this).load(readForetDTO.getPhoto().get(1)).into(imageView1);
//            Glide.with(this).load(readForetDTO.getPhoto().get(2)).into(imageView2);
//            image_count = 3;
//        } else if (str_boardImage.length == 3) {
//            Glide.with(this).load(readForetDTO.getPhoto().get(0)).into(imageView0);
//            Glide.with(this).load(readForetDTO.getPhoto().get(1)).into(imageView1);
//            Glide.with(this).load(readForetDTO.getPhoto().get(2)).into(imageView2);
//            Glide.with(this).load(readForetDTO.getPhoto().get(3)).into(imageView3);
//            image_count = 4;
//        } else if (str_boardImage.length == 5) {
//            Glide.with(this).load(readForetDTO.getPhoto().get(0)).into(imageView1);
//            Glide.with(this).load(readForetDTO.getPhoto().get(1)).into(imageView1);
//            Glide.with(this).load(readForetDTO.getPhoto().get(2)).into(imageView2);
//            Glide.with(this).load(readForetDTO.getPhoto().get(3)).into(imageView3);
//            Glide.with(this).load(readForetDTO.getPhoto().get(4)).into(imageView4);
//            image_count = 5;
//        }
    }

    private void getFindbyId() {
        spinner_board_type = findViewById(R.id.spinner_board_type);
        textView_writer = findViewById(R.id.textView_writer);
        editText_subject = findViewById(R.id.editText_subject);
        editText_content = findViewById(R.id.editText_content);
        imageView0 = findViewById(R.id.imageView0);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        button12 = findViewById(R.id.button12);

        button12.setOnClickListener(this);
        imageView0.setOnClickListener(this);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);

        spinner_board_type.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button12:
                if(image_count == 5) {
                    Toast.makeText(this, "최대 5개까지 등록할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                permissionCheck();
                showSelect();
                break;
            case R.id.imageView0:
                if(image_count == 1) {
                    deleteImage();
                }
                break;
            case R.id.imageView1:
                if(image_count == 2) {
                    deleteImage();
                }
                break;
            case R.id.imageView2:
                if(image_count == 3) {
                    deleteImage();
                }
                break;
            case R.id.imageView3:
                if(image_count == 4) {
                    deleteImage();
                }
                break;
            case R.id.imageView4:
                if(image_count == 5) {
                    deleteImage();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //spinner의 선택된 위치 가져오기
        if(spinner_board_type.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "게시판 타입을 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else if(spinner_board_type.getSelectedItemPosition() == 1) {
            selectedIndex = 2;
        } else if(spinner_board_type.getSelectedItemPosition() == 2) {
            selectedIndex = 4;
        }
        Log.d("[TEST]", "selectedIndex+1 => " + (selectedIndex));
    }

    private void modify() {
        String subject = editText_subject.getText().toString().trim();
        String content = editText_content.getText().toString().trim();
        if(subject.equals("")) {
            Toast.makeText(this, "제목을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(content.equals("")) {
            Toast.makeText(this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(selectedIndex == 0) {
            Toast.makeText(this, "게시판 타입을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        url = "http://34.72.240.24:8085/foret/board/board_modify.do";
        client = new AsyncHttpClient();
        editForetBoardResponse = new EditForetBoardResponse();
        RequestParams params = new RequestParams();

        params.put("id", readForetDTO.getId());
        params.put("writer", readForetDTO.getWriter());
        params.put("type", selectedIndex);
        params.put("subject", subject);
        params.put("content", content);

        if(image_count > 0) {
            for(int a=0; a<str_boardImage.length; a++) {
                params.put("photo", str_boardImage[a]);
                Log.d("[TEST]", "포토 테스트 => " + str_boardImage[a]);
            }
        }
        client.post(url, params, editForetBoardResponse);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // 툴바 메뉴 버튼 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_complete : // 완료 버튼
                modify();
                break;
            case android.R.id.home : // 뒤로가기 버튼
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void modifyCheck() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("수정 하시겠습니까?");
        builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "게시글을 수정했습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditForetBoardActivity.this, ReadForetBoardActivity.class);
                startActivity(intent);
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

                        file = new File(filePath);
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

    private void permissionCheck() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_MEDIA_LOCATION,
                                Manifest.permission.CAMERA}, 100);
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
            Glide.with(this).load(filePath).into(imageView0);
            image_count = 1;
        } else if (image_count == 1) {
            str_boardImage[1] = filePath;
            Glide.with(this).load(filePath).into(imageView1);
            image_count = 2;
        } else if (image_count == 2) {
            str_boardImage[2] = filePath;
            Glide.with(this).load(filePath).into(imageView2);
            image_count = 3;
        } else if (image_count == 3) {
            str_boardImage[3] = filePath;
            Glide.with(this).load(filePath).into(imageView3);
            image_count = 4;
        } else if (image_count == 4) {
            str_boardImage[4] = filePath;
            Glide.with(this).load(filePath).into(imageView4);
            image_count = 5;
        }
        Log.d("[TEST]", "str_boardImage.length() => " + str_boardImage.length);
    }

    private void deleteImage() {
        if(image_count == 5) {
            str_boardImage[4] = "";
            imageView4.setImageResource(R.drawable.picture);
            image_count = 4;
        } else if (image_count == 4) {
            str_boardImage[3] = "";
            imageView3.setImageResource(R.drawable.picture);
            image_count = 3;
        } else if (image_count == 3) {
            str_boardImage[2] = "";
            imageView2.setImageResource(R.drawable.picture);
            image_count = 2;
        } else if (image_count == 2) {
            str_boardImage[1] = "";
            imageView1.setImageResource(R.drawable.picture);
            image_count = 1;
        } else if (image_count == 1) {
            str_boardImage[0] = "";
            imageView0.setImageResource(R.drawable.picture);
            image_count = 0;
        }
        Log.d("[TEST]", "str_boardImage.length() => " + str_boardImage.length);
    }

    class EditForetBoardResponse extends AsyncHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            Log.d("[TEST]", "EditForetBoardResponse onStart() 호출");
        }

        @Override
        public void onFinish() {
            super.onFinish();
            Log.d("[TEST]", "EditForetBoardResponse onFinish() 호출");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String rt = json.getString("rt");
                if(rt.equals("OK")) {
                    modifyCheck();
                } else {
                    Toast.makeText(EditForetBoardActivity.this, "게시판을 수정하지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(EditForetBoardActivity.this, "EditForetBoardResponse 통신 실패", Toast.LENGTH_SHORT).show();
        }
    }
}