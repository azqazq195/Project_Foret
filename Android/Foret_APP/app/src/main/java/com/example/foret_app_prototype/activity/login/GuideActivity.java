package com.example.foret_app_prototype.activity.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.activity.foret.EditForetActivity;
import com.example.foret_app_prototype.helper.FileUtils;
import com.example.foret_app_prototype.helper.PhotoHelper;

import java.io.File;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    Button button0, button1, button2, button6;
    TextView button3, button4, button5, textView_region, textView_tag;
    ConstraintLayout layout1, layout2, layout3, layout4, layout5;
    ImageView profile;
    String filePath=null;
    Intent intent;
    int afterBUTTONCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        button0 = findViewById(R.id.button0); //건너뛰기
        button1 = findViewById(R.id.button1); //이전
        button2 = findViewById(R.id.button2); //다음
        button3 = findViewById(R.id.button3); //지역 선택하기
        button4 = findViewById(R.id.button4); //태그 선택하기
        button5 = findViewById(R.id.button5); //프로필 고르기
        button6 = findViewById(R.id.button6); //포레 시작하기
        textView_region = findViewById(R.id.textView_region);
        textView_tag = findViewById(R.id.textView_tag);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2); //지역설정화면
        layout3 = findViewById(R.id.layout3); //지역설정화면
        layout4 = findViewById(R.id.layout4); //프사설정화면
        layout5 = findViewById(R.id.layout5); //포레시작하기
        profile = findViewById(R.id.profile);
        profile.setImageResource(R.drawable.test); //사진이 출력이 안되서 초기세팅해줌

        textView_region.setVisibility(View.GONE);
        textView_tag.setVisibility(View.GONE);
        button1.setVisibility(View.GONE);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
    }

    public void layoutVisible (int buttonResourc, View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button0 : //건너뛰기
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button1 : //이전
                switch (afterBUTTONCount) {
                    case 1 :
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.GONE);
                        button1.setVisibility(View.GONE);
                        afterBUTTONCount=afterBUTTONCount-1;
                        break;
                    case 2 :
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.GONE);
                        afterBUTTONCount=afterBUTTONCount-1;
                        break;
                    case 3 :
                        layout3.setVisibility(View.VISIBLE);
                        layout4.setVisibility(View.GONE);
                        afterBUTTONCount=afterBUTTONCount-1;
                        break;
                    case 4 :
                        layout4.setVisibility(View.VISIBLE);
                        layout5.setVisibility(View.GONE);
                        afterBUTTONCount=afterBUTTONCount-1;
                        break;
                }
                break;
            case R.id.button2 : //다음
                switch (afterBUTTONCount) {
                    case 0 :
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        button1.setVisibility(View.VISIBLE);
                        afterBUTTONCount++;
                        break;
                    case 1 :
                        layout2.setVisibility(View.GONE);
                        layout3.setVisibility(View.VISIBLE);
                        afterBUTTONCount++;
                        break;
                    case 2 :
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.VISIBLE);
                        afterBUTTONCount++;
                        break;
                    case 3 :
                        layout4.setVisibility(View.GONE);
                        layout5.setVisibility(View.VISIBLE);
                        afterBUTTONCount++;
                        button1.setVisibility(View.GONE);
                        button2.setVisibility(View.GONE);
                        break;
                }
                break;
            case R.id.button3 : //지역선택
                regionDialog();
                break;
            case R.id.button4 : //태그선택
                tagDialog();
                break;
            case R.id.button5 : //프로필 설정
                permissionCheck();
                showSelect();
                break;
            case R.id.button6 : //포레시작
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void regionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("지역을 골라주세요.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //확인 버튼 누르면
                //textView_region.setText("선택한 지역들");
                textView_region.setVisibility(View.VISIBLE);
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void tagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("태그를 골라주세요.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //확인 버튼 누르면
                //textView_tag.setText("선택한 태그들");
                textView_tag.setVisibility(View.VISIBLE);
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    private void showSelect() {
        final String [] menu = {"새로 촬영하기", "갤러리에서 가져오기"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: //새로 촬영하기-카메라 호출
                        filePath = PhotoHelper.getInstance().getNewPhotoPath(); //저장할 사진 경로
                        Log.d("[TEST]", "photoPath = "+filePath);

                        File file = new File(filePath);
                        Uri uri = null;

                        //카메라앱 호출을 위한 암묵적 인텐트 (action과 uri가 필요하다)
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(GuideActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);
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
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case 200:
                    Toast.makeText(this, "사진 첨부 완료", Toast.LENGTH_SHORT).show();
                    //촬영 결과물을 MediaStore에 등록한다(갤러리에 저장). MediaStore에 등록하지 않으면 우리 앱에서 만든 파일을 다른 앱에서는 사용할 수 없다.
                    intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(filePath));
                    Log.d("[TEST]", filePath);
                    sendBroadcast(intent);
                    Glide.with(this).load(filePath).into(profile);
                    break;
                case 300 :
                    String uri = data.getData().toString();
                    String fileName = uri.substring(uri.lastIndexOf("/")+1);
                    Log.d("[TEST]", "fileName = "+fileName);
                    filePath= FileUtils.getPath(this, data.getData());
                    Log.d("[TEST]", "filePath = "+filePath);
                    Toast.makeText(this, fileName+"을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    Glide.with(this).load(filePath).into(profile);
            }
        }
    }
}