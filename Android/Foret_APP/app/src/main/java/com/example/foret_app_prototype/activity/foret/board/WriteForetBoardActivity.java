package com.example.foret_app_prototype.activity.foret.board;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.foret.ViewForetActivity;
import com.example.foret_app_prototype.activity.notify.APIService;
import com.example.foret_app_prototype.activity.notify.Client;
import com.example.foret_app_prototype.activity.notify.Data;
import com.example.foret_app_prototype.activity.notify.Response;
import com.example.foret_app_prototype.activity.notify.Sender;
import com.example.foret_app_prototype.activity.notify.Token;
import com.example.foret_app_prototype.helper.FileUtils;
import com.example.foret_app_prototype.helper.PhotoHelper;
import com.example.foret_app_prototype.helper.ProgressDialogHelper;
import com.example.foret_app_prototype.model.MemberDTO;
import com.example.foret_app_prototype.model.ModelUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;

public class WriteForetBoardActivity extends AppCompatActivity implements View.OnClickListener {
    MemberDTO memberDTO;

    EditText editText_content, editText_subject;
    TextView textView_writer;
    Spinner spinner;
    ImageView imageView0, imageView1, imageView2, imageView3, imageView4;
    Button button;

    String filePath = null;
    Intent intent;

    // 이미지 카운트
    int image_count = 0;

    AsyncHttpClient client;
    WriteBoardResponse writeBoardResponse;
    String url = "";

    int foret_id = 0;

    // 보낼 데이터
    String[] str_boardImage = new String[5];
    File[] file = new File[5];
    int type = 0;
    String subject = "";
    String content = "";

    String foretname;
    String myNickName;

    APIService apiService;
    boolean notify = false;
    String hisUid, myUid;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_foret_board);


        // 상태바 색상 변경
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.foret4));
        context = this;

        Toolbar toolbar = findViewById(R.id.cosutom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기존 타이틀 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        getFindbyId(); // 객체 초기화

        foret_id = getIntent().getIntExtra("foret_id", 0);
        Log.d("[TEST]", "넘어온 포레 아디 => " + foret_id);
        memberDTO = (MemberDTO) getIntent().getSerializableExtra("memberDTO");
        Log.d("[TEST]", "넘어온 멤버 아디 => " + memberDTO.getId());

        textView_writer.setText(memberDTO.getNickname());
        foretname = getIntent().getStringExtra("foretname");
        myNickName = memberDTO.getNickname();

        // 푸쉬 알림 생성
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //Toast.makeText(WriteForetBoardActivity.this, "게시판 타입을 설정해주세요.", Toast.LENGTH_SHORT).show();
                        type = 4 ;  //일반
                        break;
                    case 1:
                        type = 3; // 일정
                        break;
                    case 2:
                        type = 1; // 공지
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getFindbyId() {
        editText_content = findViewById(R.id.editText_content);
        editText_subject = findViewById(R.id.editText_subject);
        textView_writer = findViewById(R.id.textView_writer);
        spinner = findViewById(R.id.spinner_board_type);
        imageView0 = findViewById(R.id.imageView0);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        button = findViewById(R.id.button12);

        button.setOnClickListener(this);
        imageView0.setOnClickListener(this);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complete_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_complete: // 완료 버튼
                if (inputBoard()) {
                    writeCheck();
                }
                return true;
            case android.R.id.home: // 뒤로가기 버튼
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void putParams() {
        url = "http://34.72.240.24:8085/foret/board/board_insert.do";
        client = new AsyncHttpClient();
        writeBoardResponse = new WriteBoardResponse();
        RequestParams params = new RequestParams();

//        params.put("writer", memberDTO.getId());
        params.put("writer", memberDTO.getId());
        params.put("foret_id", foret_id);
        params.put("type", type);
        params.put("subject", subject);
        params.put("content", content);

        Log.e("[test]--포레 글쓰기", "writer" + memberDTO.getId());
        Log.e("[test]--포레 글쓰기", "foret_id" + memberDTO.getId());
        Log.e("[test]--포레 글쓰기", "type" + type);
        Log.e("[test]--포레 글쓰기", "content" + content);
        Log.e("[test]--포레 글쓰기", "subject" + subject);

            for (int a = 1; a <= image_count; a++) {
                try {
                    Log.e("[test]","file size?"+file.length);
                    if (file[a] != null) {
                        params.put("photo", file[a]);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Log.d("[TEST]", "포토 테스트 => " + str_boardImage[a]);
            }

        params.setForceMultipartEntityContentType(true);
        final int DEFAULT_TIME = 20*1000;
        client.setConnectTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);
        client.setTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);
        client.post(url, params, writeBoardResponse);
    }

    private boolean inputBoard() {
        subject = editText_subject.getText().toString().trim();
        content = editText_content.getText().toString();
        if (subject.equals("")) {
            Toast.makeText(this, "제목을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (content.equals("")) {
            Toast.makeText(this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (type == 0) {
            Toast.makeText(this, "게시판 타입을 설정하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void writeCheck() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("등록 하시겠습니까?");
        builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                putParams();
                ProgressDialogHelper.getInstance().getProgressbar(context,"등록중입니다.");
                //Toast.makeText(getApplicationContext(), "게시글을 등록했습니다.", Toast.LENGTH_SHORT).show();
                //
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button12:
                if (image_count == 5) {
                    Toast.makeText(this, "최대 5개까지 등록할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                permissionCheck();
                showSelect();
                break;
            case R.id.imageView0:
                if (image_count == 0) {
                    deleteImage();
                }
                break;
            case R.id.imageView1:
                if (image_count == 1) {
                    deleteImage();
                }
                break;
            case R.id.imageView2:
                if (image_count == 2) {
                    deleteImage();
                }
                break;
            case R.id.imageView3:
                if (image_count == 3) {
                    deleteImage();
                }
                break;
            case R.id.imageView4:
                if (image_count == 4) {
                    deleteImage();
                }
                break;
        }
    }

    private void showSelect() {
        final String[] menu = {"갤러리에서 가져오기"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    /*
                    case 0: //새로 촬영하기-카메라 호출
                        filePath = PhotoHelper.getInstance().getNewPhotoPath(); //저장할 사진 경로
                        Log.d("[TEST]", "photoPath = " + filePath);
                        file[image_count+1] = new File(filePath);
                        Uri uri = null;

                        //카메라앱 호출을 위한 암묵적 인텐트 (action과 uri가 필요하다)
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(WriteForetBoardActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file[image_count]);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.fromFile(file[image_count+1]);
                        }
                        Log.d("[TEST]", "uri : " + uri.toString());

                        //저장할 경로를 파라미터로 설정
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        intent.putExtra(AUDIO_SERVICE, false);

                        //카메라 앱 호출
                        startActivityForResult(intent, 200);
                        break;

                     */
                    case 0: //갤러리에서 가져오기-갤러리 호출
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*"); //모든 이미지 표시
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        startActivityForResult(intent, 300); //선택된 파일을 돌려받아야함
                        break;
                }
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false); // 확인하기
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    Toast.makeText(this, "사진 첨부 완료", Toast.LENGTH_SHORT).show();
                    //촬영 결과물을 MediaStore에 등록한다(갤러리에 저장). MediaStore에 등록하지 않으면 우리 앱에서 만든 파일을 다른 앱에서는 사용할 수 없다.
                    intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(filePath));
                    Log.d("[TEST]", filePath);
                    sendBroadcast(intent);
                    insertImage();
                    break;
                case 300:
                    String uri = data.getData().toString();
                    String fileName = uri.substring(uri.lastIndexOf("/") + 1);
                    Log.d("[TEST]", "fileName = " + fileName);
                    filePath = FileUtils.getPath(this, data.getData());
                    file[image_count+1] = new File(filePath);
                    Log.d("[TEST]", "filePath = " + filePath);
                    Toast.makeText(this, fileName + "을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    insertImage();
            }
        }
    }

    private void insertImage() {
        if (image_count == 0) {
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
        if (image_count == 5) {
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

    class WriteBoardResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String boardRT = json.getString("boardRT");
                String boardPhotoRT = json.getString("boardPhotoRT");
                if (boardRT.equals("OK")) {
                    Toast.makeText(context,"등록 성공!",Toast.LENGTH_LONG).show();

                    //파이어 베이스 연동
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    myUid = user.getUid();
                    String timestamp = "" + System.currentTimeMillis();

                    hisUid = "";

                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Groups");
                    ref2.child(foretname).child("participants").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                hisUid = ds.getKey();

                                notify = true;
                                if (!myUid.equals(ds.getKey())) {
                                    //알림설정
                                    updateNewItem("GROUP_NEW_ITEM", myUid, hisUid, "내 포레에 새로운 글이 등록되었습니다.", "" + System.currentTimeMillis());

                                    String msg = "";
                                    //설정
                                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
                                    database.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            ModelUser user = snapshot.getValue(ModelUser.class);

                                            if (notify) {
                                                sendNotification(hisUid, user.getNickname(), msg);
                                                ProgressDialogHelper.getInstance().removeProgressbar();
                                            }
                                            notify = false;
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            ProgressDialogHelper.getInstance().removeProgressbar();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            ProgressDialogHelper.getInstance().removeProgressbar();
                        }
                    });


                } else {
                    Toast.makeText(WriteForetBoardActivity.this, "등록하지 못했습니다.", Toast.LENGTH_SHORT).show();
                    ProgressDialogHelper.getInstance().removeProgressbar();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(WriteForetBoardActivity.this, "통신 실패", Toast.LENGTH_SHORT).show();
            ProgressDialogHelper.getInstance().removeProgressbar();
        }
    }


    // 온라인 상태 만들기
    private void updateuserActiveStatusOn() {
        FirebaseUser currentUseruser = FirebaseAuth.getInstance().getCurrentUser();
        final String userUid = currentUseruser.getUid();
        DatabaseReference userAcitive = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
        HashMap<String, Object> onlineStatus = new HashMap<>();
        onlineStatus.put("onlineStatus", "online");
        onlineStatus.put("listlogined_date", "현재 접속중");
        userAcitive.updateChildren(onlineStatus);
    }

    // 오프라인 상태 만들기
    private void updateuserActiveStatusOff() {
        FirebaseUser currentUseruser = FirebaseAuth.getInstance().getCurrentUser();
        final String userUid = currentUseruser.getUid();
        DatabaseReference userAcitive = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
        HashMap<String, Object> onlineStatus = new HashMap<>();
        onlineStatus.put("onlineStatus", "offline");

        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.KOREAN);
        cal.setTimeInMillis(Long.parseLong(String.valueOf(System.currentTimeMillis())));
        String dateTime = DateFormat.format("yy/MM/dd hh:mm aa", cal).toString();

        onlineStatus.put("listlogined_date", "Last Seen at : " + dateTime);
        userAcitive.updateChildren(onlineStatus);
    }

    // 유저 접송 상태
    private void checkUserStatus() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            updateuserActiveStatusOn();
        } else {
            // 유저가 로그인 안한 상태
            Toast.makeText(this, "오프라인 상태입니다.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // 새글데이터
    public void updateNewItem(String type, String sender, String receiver, String content, String time) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notify");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", type);
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("content", content);
        hashMap.put("time", time);
        hashMap.put("isSeen", false);

        ref.child(receiver).push().setValue(hashMap);
    }

    // 알림 발송 설정.
    private void sendNotification(String hisUid, String nickname, String message) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUid); // 상대 찾기
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    // 상대 토큰값 토큰화 하기
                    Token token = ds.getValue(Token.class);

                    // 데이터 셋팅
                    Data data = new Data(myUid, message, foretname + "의 포레 알림", hisUid,
                            R.drawable.foret_logo);

                    // 보내는 사람 셋팅
                    Sender sender = new Sender(data, token.getToken());
                    // 발송
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(
                                        Call<Response> call,
                                        retrofit2.Response<com.example.foret_app_prototype.activity.notify.Response> response) {
                                    // Toast.makeText(ChatActivity.this,""+response.message(),Toast.LENGTH_LONG).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {
                                    ProgressDialogHelper.getInstance().removeProgressbar();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ProgressDialogHelper.getInstance().removeProgressbar();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }
}