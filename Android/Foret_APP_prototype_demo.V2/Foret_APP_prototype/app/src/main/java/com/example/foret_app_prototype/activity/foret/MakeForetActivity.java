package com.example.foret_app_prototype.activity.foret;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.helper.FileUtils;
import com.example.foret_app_prototype.helper.PhotoHelper;

import java.io.File;

public class MakeForetActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView image_View_picture, button_cancel;
    EditText editText_name, editText_member, editText_intro;
    Button button_complete, button_picture;
    TextView button_region, button_tag;
    String filePath=null;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_foret);
        image_View_picture = findViewById(R.id.imageView_picture);
        button_cancel = findViewById(R.id.button_cancel);
        editText_name = findViewById(R.id.editText_name);
        editText_member = findViewById(R.id.editText_member);
        editText_intro = findViewById(R.id.editText_intro);
        button_complete = findViewById(R.id.button_complete);
        button_picture = findViewById(R.id.button_picture);
        button_region = findViewById(R.id.button_region);
        button_tag = findViewById(R.id.button_tag);

        button_cancel.setOnClickListener(this);
        button_picture.setOnClickListener(this);
        button_complete.setOnClickListener(this);
        button_region.setOnClickListener(this);
        button_tag.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_complete : //완료(확인 버튼)
                if(editText_name.getText().toString().trim().equals("")|| editText_intro.getText().toString().trim().equals("")||editText_member.getText().toString().trim().equals("")||
                        button_region.getText().toString().trim().equals("지역 고르기")||button_tag.getText().toString().trim().equals("태그 고르기")) {
                    Toast.makeText(this, "빈 항목이 없도록 채워주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, "포레를 만들었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ViewForetActivity.class);
                intent.putExtra("포레정보DTO", "포레정보DTO");
                startActivity(intent);
                finish();
                break;
            case R.id.button_cancel : //취소 버튼
                finish();
                break;
            case R.id.button_region : //지역 고르기
                regionDialog();
                break;
            case R.id.button_tag : //태그 고르기
                tagDialog();
                break;
            case R.id.button_picture : //사진 고르기
                permissionCheck();
                showSelect();
                break;
        }
    }

    public void regionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("지역을 골라주세요.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //확인 버튼 누르면
                button_region.setText("선택한 지역들");
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void tagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("태그를 골라주세요.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //확인 버튼 누르면
                button_tag.setText("선택한 태그들");
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
                            uri = FileProvider.getUriForFile(MakeForetActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);
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
                    Glide.with(this).load(filePath).into(image_View_picture);
                    break;
                case 300 :
                    String uri = data.getData().toString();
                    String fileName = uri.substring(uri.lastIndexOf("/")+1);
                    Log.d("[TEST]", "fileName = "+fileName);
                    filePath= FileUtils.getPath(this, data.getData());
                    Log.d("[TEST]", "filePath = "+filePath);
                    Toast.makeText(this, fileName+"을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    Glide.with(this).load(filePath).into(image_View_picture);
            }
        }
    }

}