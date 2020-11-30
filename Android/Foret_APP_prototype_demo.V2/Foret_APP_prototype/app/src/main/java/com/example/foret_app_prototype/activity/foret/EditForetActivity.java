package com.example.foret_app_prototype.activity.foret;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.helper.FileUtils;
import com.example.foret_app_prototype.helper.PhotoHelper;

import java.io.File;

public class EditForetActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView profile, button_close, button_tag_edit, button_region_edit, button_member_edit;
    Button button_complete;
    TextView textView1, textView_name, textView_tag, textView_region, textView_member, textView_master, textView_birth;
    EditText editText_intro;
    Intent intent;
    String filePath=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_foret);
        profile = findViewById(R.id.profile);
        profile.setImageResource(R.drawable.foret_logo); //이미지뷰에서 초기 이미지가 에뮬에서 자꾸 안나와서 쓴 코드
        textView_name = findViewById(R.id.textView_name);
        textView_tag = findViewById(R.id.textView_tag);
        textView_region = findViewById(R.id.textView_region);
        textView_member = findViewById(R.id.textView_member);
        textView_master = findViewById(R.id.textView_master);
        textView_birth = findViewById(R.id.textView_birth);
        textView1 = findViewById(R.id.textView1);
        button_close = findViewById(R.id.button_close);
        button_tag_edit = findViewById(R.id.button_tag_edit);
        button_region_edit = findViewById(R.id.button_region_edit);
        button_complete = findViewById(R.id.button_complete);
        button_member_edit = findViewById(R.id.button_member_edit);
        editText_intro = findViewById(R.id.editText_intro);
        dataSetting();

        textView1.setOnClickListener(this);
        button_close.setOnClickListener(this);
        button_tag_edit.setOnClickListener(this);
        button_region_edit.setOnClickListener(this);
        button_complete.setOnClickListener(this);
    }

    private void dataSetting() {
        //Glide.with(this).load(filePath).into(profile);
        textView_name.setText("선미님 착하당");
        textView_tag.setText("#선미님, #최고, #착함");
        textView_region.setText("성수동");
        textView_member.setText("가입한인원수/최대인원수");
        textView_master.setText("포레 마스터 : SANDY");
        textView_birth.setText("2020.12.02");
        String intro = "";
        for (int a=0; a<=6; a++) {
            intro += "세상에서 제일 이뿐 선미님 ";
        }
        editText_intro.setText(intro);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView1 : //사진수정 - 이 텍스트뷰를 누르면 텍스트뷰 밑에 수정된 사진이 보인다
                showSelect();
                break;
            case R.id.button_close :
                finish();
                break;
            case R.id.button_tag_edit :
                tagSelectDialog();
                break;
            case R.id.button_region_edit :
                regionSelectDialog();
                break;
            case R.id.button_member_edit :
                memberSelectDialog();
                break;
            case R.id.button_complete : //수정완료
                finish();
                break;
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
                            uri = FileProvider.getUriForFile(EditForetActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);
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

    private void memberSelectDialog() { //결과 : 인원수 정보 텍스트뷰에 출력
        Toast.makeText(this, "인원수 조정 다이어로그로 띄울거에요", Toast.LENGTH_SHORT).show();
    }

    private void regionSelectDialog() { //결과 : 지역 정보 텍스트뷰에 출력
        Toast.makeText(this, "지역 선택하는 다이어로그로 띄울거에요", Toast.LENGTH_SHORT).show();
    }

    private void tagSelectDialog() { //결과 : 태그 정보 텍스트뷰에 출력
        Toast.makeText(this, "결과 선택하는 다이어로그로 띄울거에요", Toast.LENGTH_SHORT).show();
    }
}