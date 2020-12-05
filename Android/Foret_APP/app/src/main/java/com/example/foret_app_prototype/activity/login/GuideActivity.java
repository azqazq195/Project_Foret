package com.example.foret_app_prototype.activity.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;

import com.example.foret_app_prototype.activity.foret.EditForetActivity;
import com.example.foret_app_prototype.helper.CalendarHelper;

import com.example.foret_app_prototype.helper.FileUtils;
import com.example.foret_app_prototype.helper.PhotoHelper;
import com.example.foret_app_prototype.helper.ProgressDialogHelper;
import com.example.foret_app_prototype.model.Member;
import com.example.foret_app_prototype.model.MemberDTO;
import com.example.foret_app_prototype.model.ModelUser;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {
    Member member;

    Button button0, button1, button2, button6;
    TextView button3, button4, button5, textView_region, textView_tag;
    ConstraintLayout layout1, layout2, layout3, layout4, layout5;
    ImageView profile;
    String filePath = null;
    Intent intent;
    int afterBUTTONCount = 0;

    String select_si = "";
    String select_gu = "";
    String select_tag = "";
    String str = "";
    String show = "";

    List<String> region_si;
    List<String> region_gu;
    List<String> member_tag;

    String name, nickname, birth, email, pw2;
    File file;
    Uri uri;
    AsyncHttpClient client;
    Activity activity;
    Context context;
    String downloadUri;
    int member_id;
    String deviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        client = new AsyncHttpClient();
        activity = this;
        context = this;
        button0 = findViewById(R.id.button0); // 건너뛰기
        button1 = findViewById(R.id.button1); // 이전
        button2 = findViewById(R.id.button2); // 다음
        button3 = findViewById(R.id.button3); // 지역 선택하기
        button4 = findViewById(R.id.button4); // 태그 선택하기
        button5 = findViewById(R.id.button5); // 프로필 고르기
        button6 = findViewById(R.id.button6); // 포레 시작하기
        textView_region = findViewById(R.id.textView_region);
        textView_tag = findViewById(R.id.textView_tag);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2); // 지역설정화면
        layout3 = findViewById(R.id.layout3); // 지역설정화면
        layout4 = findViewById(R.id.layout4); // 프사설정화면
        layout5 = findViewById(R.id.layout5); // 포레시작하기
        profile = findViewById(R.id.profile);

      //  profile.setImageResource(R.drawable.foret); // 사진이 출력이 안되서 초기세팅해줌

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

        name = getIntent().getStringExtra("name");
        nickname = getIntent().getStringExtra("nickname");
        birth = getIntent().getStringExtra("birth");
        email = getIntent().getStringExtra("email");
        pw2 = getIntent().getStringExtra("pw2");

        region_si = new ArrayList<>();
        region_gu = new ArrayList<>();
        member_tag = new ArrayList<>();

    }

    public void layoutVisible(int buttonResourc, View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button0: // 건너뛰기
                intent = new Intent(this, MainActivity.class);

                startActivity(intent);
                finish();
                break;
            case R.id.button1: // 이전
                switch (afterBUTTONCount) {
                    case 1:
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.GONE);
                        button1.setVisibility(View.GONE);
                        afterBUTTONCount = afterBUTTONCount - 1;
                        break;
                    case 2:
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.GONE);
                        afterBUTTONCount = afterBUTTONCount - 1;
                        break;
                    case 3:
                        layout3.setVisibility(View.VISIBLE);
                        layout4.setVisibility(View.GONE);
                        afterBUTTONCount = afterBUTTONCount - 1;
                        break;
                    case 4:
                        layout4.setVisibility(View.VISIBLE);
                        layout5.setVisibility(View.GONE);
                        afterBUTTONCount = afterBUTTONCount - 1;
                        break;
                }
                break;
            case R.id.button2: // 다음
                switch (afterBUTTONCount) {
                    case 0:
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        button1.setVisibility(View.VISIBLE);
                        afterBUTTONCount++;
                        break;
                    case 1:
                        layout2.setVisibility(View.GONE);
                        layout3.setVisibility(View.VISIBLE);
                        afterBUTTONCount++;
                        break;
                    case 2:
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.VISIBLE);
                        afterBUTTONCount++;
                        break;
                    case 3:
                        layout4.setVisibility(View.GONE);
                        layout5.setVisibility(View.VISIBLE);
                        afterBUTTONCount++;
                        button1.setVisibility(View.GONE);
                        button2.setVisibility(View.GONE);
                        break;
                }
                break;
            case R.id.button3: // 지역선택
                regionDialog();
                break;
            case R.id.button4: // 태그선택
                tagDialog();
                break;
            case R.id.button5: // 프로필 설정
                permissionCheck();
                showSelect();
                break;
            case R.id.button6: // 포레시작

                tryToSignUp();

                /*
                 * intent = new Intent(this, MainActivity.class); startActivity(intent);
                 * finish();
                 */
                break;
        }
    }

    public void regionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View region_view = getLayoutInflater().inflate(R.layout.guide_select_region, null);
        builder.setTitle("지역을 선택해주세요.");

        str = "";
        show = "";

        Spinner spinner_si = region_view.findViewById(R.id.spinner_si);
        Spinner spinner_gu = region_view.findViewById(R.id.spinner_gu);
        TextView selected_view = region_view.findViewById(R.id.selected_view);

        spinner_si.setVisibility(View.VISIBLE);
        spinner_si.setSelection(0);

        spinner_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("[TEST]", "region_si position => " + position);
                select_si = (String) parent.getSelectedItem();
                if (position != 0 && !select_si.equals("")) {
                    Log.d("[TEST]", "select_si => " + select_si);
                    region_si.add(select_si);
                }

                ArrayAdapter guAdapter;
                switch (position) {
                    case 1:
                        guAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.seuol_gu,
                                R.layout.support_simple_spinner_dropdown_item);
                        spinner_gu.setAdapter(guAdapter);
                        break;
                    case 2:
                        guAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gyeonggi_si,
                                R.layout.support_simple_spinner_dropdown_item);
                        spinner_gu.setAdapter(guAdapter);
                        break;
                    case 3:
                        guAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.daejeon_gu,
                                R.layout.support_simple_spinner_dropdown_item);
                        spinner_gu.setAdapter(guAdapter);
                        break;
                    case 4:
                        guAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gangwon_si,
                                R.layout.support_simple_spinner_dropdown_item);
                        spinner_gu.setAdapter(guAdapter);
                        break;
                    case 5:
                        guAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gwangju_gu,
                                R.layout.support_simple_spinner_dropdown_item);
                        spinner_gu.setAdapter(guAdapter);
                        break;
                    case 6:
                        guAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.busan_gu,
                                R.layout.support_simple_spinner_dropdown_item);
                        spinner_gu.setAdapter(guAdapter);
                        break;
                    case 7:
                        guAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.jeju_si,
                                R.layout.support_simple_spinner_dropdown_item);
                        spinner_gu.setAdapter(guAdapter);
                        break;
                }
                spinner_gu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_view.setText("최소 한개의 지역을 등록하세요.");
            }
        });

        spinner_gu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("[TEST]", "region_gu position => " + position);
                select_gu = (String) parent.getSelectedItem();
                if (position != 0 && !select_gu.equals("")) {
                    Log.d("[TEST]", "select_gu => " + select_gu);
                    region_gu.add(select_gu);
                    str += select_si + " " + select_gu + "\n";
                    selected_view.setText(str);
                    spinner_si.setSelection(0);
                    spinner_gu.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("[TEST]", "region_si.size() => " + region_si.size());
                Log.d("[TEST]", "region_gu.size() => " + region_gu.size());
                // 확인 버튼 누르면
                for (int a = 0; a < region_si.size(); a++) {
                    show += region_si.get(a) + " " + region_gu.get(a) + "\n";
                    Log.d("[TEST]", "region_si.get(a) => " + region_si.get(a));
                    Log.d("[TEST]", "region_gu.get(a) => " + region_gu.get(a));
                }
                textView_region.setText(show);
                textView_region.setVisibility(View.VISIBLE);
            }
        });
        builder.setNegativeButton("취소", null);

        builder.setView(region_view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void tagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View region_view = getLayoutInflater().inflate(R.layout.guide_select_region, null);
        builder.setMessage("태그를 골라주세요.");

        str = "";
        show = "";

        Spinner spinner_tag = region_view.findViewById(R.id.spinner_tag);
        TextView selected_view = region_view.findViewById(R.id.selected_view);
        spinner_tag.setVisibility(View.VISIBLE);

        spinner_tag.setSelection(0);

        spinner_tag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    select_tag = (String) parent.getSelectedItem();
                    member_tag.add(select_tag);
                    Log.d("[TEST]", "foret_tag.size() => " + member_tag.size());
                    str += "#" + select_tag + " ";
                    selected_view.setText(str);
                    spinner_tag.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 확인 버튼 누르면
                for (int a = 0; a < member_tag.size(); a++) {
                    show += "#" + member_tag.get(a) + " ";
                    Log.d("[TEST]", "foret_tag.get(a) => " + member_tag.get(a));
                }
                textView_tag.setText(show);
                textView_tag.setVisibility(View.VISIBLE);
            }
        });
        builder.setNegativeButton("취소", null);

        builder.setView(region_view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void permissionCheck() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.CAMERA},
                        100);
            }
        }
    }

    private void showSelect() {
        final String[] menu = {"새로 촬영하기", "갤러리에서 가져오기"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 새로 촬영하기-카메라 호출
                        filePath = PhotoHelper.getInstance().getNewPhotoPath(); // 저장할 사진 경로
                        Log.d("[TEST]", "photoPath = " + filePath);
                        file = new File(filePath);
                        uri = null;

                        // 카메라앱 호출을 위한 암묵적 인텐트 (action과 uri가 필요하다)
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(GuideActivity.this,
                                    getApplicationContext().getPackageName() + ".fileprovider", file);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        Log.d("[TEST]", "uri : " + uri.toString());

                        // 저장할 경로를 파라미터로 설정
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        intent.putExtra(AUDIO_SERVICE, false);

                        // 카메라 앱 호출
                        startActivityForResult(intent, 200);
                        break;
                    case 1: // 갤러리에서 가져오기-갤러리 호출
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*"); // 모든 이미지 표시
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        startActivityForResult(intent, 300); // 선택된 파일을 돌려받아야함
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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    Toast.makeText(this, "사진 첨부 완료", Toast.LENGTH_SHORT).show();
                    // 촬영 결과물을 MediaStore에 등록한다(갤러리에 저장). MediaStore에 등록하지 않으면 우리 앱에서 만든 파일을 다른 앱에서는
                    // 사용할 수 없다.
                    intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(filePath));
                    Log.d("[TEST]", filePath);
                    sendBroadcast(intent);
                    Glide.with(this).load(filePath).into(profile);
                    break;
                case 300:
                    String uri1 = data.getData().toString();
                    String fileName = uri1.substring(uri1.lastIndexOf("/") + 1);
                    Log.d("[TEST]", "fileName = " + fileName);
                    filePath = FileUtils.getPath(this, data.getData());
                    file = new File(filePath);
                    Log.d("[TEST]", "filePath = " + filePath);
                    Toast.makeText(this, fileName + "을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    Glide.with(this).load(filePath).into(profile);
                    uri = data.getData();

            }
        }
    }

    // 여기서부터
    private void tryToSignUp() {
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("email", email);
        params.put("password", pw2);
        params.put("birth", birth);
        params.put("nickname", nickname);

        String[] str_si = new String[region_si.size()];
        String[] str_gu = new String[region_gu.size()];

        for (int a = 0; a < str_si.length; a++) {
            str_si[a] = region_si.get(a);
            str_gu[a] = region_gu.get(a);
            if(a==0){
                params.put("region_si","서울시" );
                params.put("region_gu", "강남구");
            }else {
                params.add("region_si", "성남시");
                params.add("region_gu", "분당구");
            }

        }
        String[] str_tag = new String[member_tag.size()];
        for (int a = 0; a < str_tag.length; a++) {
            str_tag[a] = member_tag.get(a);

            if(a==0){
                params.put("tag","태그1" );
            }else {
                params.add("tag", "태그2");
            }
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e("[test]", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        deviceToken = task.getResult();
                    }
                });

        params.put("device_Token", deviceToken);
        Log.e("[test]", name + ", " + email + ", " + pw2 + ", " + birth + ", " + nickname);
        String url = "http://34.72.240.24:8085/foret/member/member_insert.do";
       //String url = "http://192.168.219.100:8085/foret/member/member_insert.do";
        //String url = "http://192.168.0.180:8085/foret/member/member_insert.do";
        try {
            if (file != null)
                params.put("photo", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 멀티파트리퀘스트 형태로 보내는 메서드
        params.setForceMultipartEntityContentType(true);

        final int DEFAULT_TIME = 50*1000;
        client.setConnectTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);
        client.setTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);
        client.post(url, params, new Response(activity));
        ProgressDialogHelper.getInstance().getProgressbar(this, "가입 진행중.");
    }

    private class Response extends AsyncHttpResponseHandler {
        Activity activity;

        public Response(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String result = new String(responseBody);
            Log.e("[test]", "온석세스 진입");
            try {
                JSONObject json = new JSONObject(result);
                String memberRT = json.getString("memberRT");
                String memberTagRT = json.getString("memberTagRT");
                String memberRegionRT = json.getString("memberRegionRT");
                String memberPhotoRT = json.getString("memberPhotoRT");
                member_id = Integer.parseInt(json.getString("member_id"));

                if (memberRT.equals("OK")) {
                    Toast.makeText(
                            activity, "결과\n memberRT : " + memberRT + "\nmemberTagRT : " + memberTagRT
                                    + "\n memberRegionRT : " + memberRegionRT + "\n memberPhotoRT : " + memberPhotoRT,
                            Toast.LENGTH_LONG).show();

                    ModelUser modelUser = new ModelUser();
                    String timestamp = CalendarHelper.getInstance().getCurrentTimeFull();
                    modelUser.setEmail(email);
                    modelUser.setUser_id(pw2); // 원래는 유저 id가 들어가야 함.
                    modelUser.setNickname(nickname);
                    modelUser.setJoineddate(timestamp);

                    //유저 등록
                    registerUser(modelUser);

                } else {
                    Toast.makeText(activity, "등록 실패..", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.e("[test]", "온페일 진입");
            Toast.makeText(activity, "통신실패, 원인 : " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // 신규 등록 이메일 + 유저 Id를 통해
    public void registerUser(final ModelUser chatuser) {
        Log.e("[test]", "유저등록시작");
        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(chatuser.getEmail(), chatuser.getUser_id())
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Log.e("[test]", "유저등록성공");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // 유저 정보 얻기
                            String email = user.getEmail();
                            String uid = user.getUid();
                            // 해쉬멥에 담아서 저장
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("nickname", chatuser.getNickname());

                            hashMap.put("user_id", chatuser.getUser_id());
                            hashMap.put("joineddate", chatuser.getJoineddate());

                            Log.e("[test]", "DB 유저 데이터 업로드" + chatuser.getJoineddate() + "/ photoroot?" + downloadUri);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            // 파이어 베이스에 유저 등록하기
                            DatabaseReference reference = database.getReference("Users");
                            // 유저를 헤쉬맵을 통해 등록하기
                            reference.child(uid).setValue(hashMap);

                            //세션등록
                            SessionManager sessionManager = new SessionManager(GuideActivity.this);
                            MemberDTO memberDTO = new MemberDTO();
                            memberDTO.setEmail(email);
                            memberDTO.setPassword(pw2);
                            memberDTO.setId(member_id);
                            sessionManager.saveSession(memberDTO);

                            //파이어 베이스 이미지 생성
                            sendImageMessage(uri);
                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // 사용중인 이메일이 있을때 나옴.
                Toast.makeText(context, "Fail : " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    // 파베에 내 이미지 보내기기
    private void sendImageMessage(Uri image_rui) {
        Log.e("[test]", "이미지 등록 시작");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String timeStamp = "" + System.currentTimeMillis();
        String fileNameAndPath = "profileImages/"+user.getUid()+ "_post_" + timeStamp;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_rui);
            ByteArrayOutputStream baos = null;
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
            byte[] data = baos.toByteArray();
            StorageReference ref = FirebaseStorage.getInstance().getReference()
                    .child(fileNameAndPath + image_rui.getLastPathSegment());
            ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e("[test]", "이미지등록성공");
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    downloadUri = uriTask.getResult().toString();

                    Log.e("[test]", "이미지등록종료");

                    addphotopathinfirebase();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.e("[test]", "이미지등록 실패");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //이미지 주소 설정
    private void addphotopathinfirebase() {

        FirebaseAuth currentUseruser = FirebaseAuth.getInstance();
        final String userUid = currentUseruser.getUid();
        DatabaseReference userAcitive = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
        HashMap<String, Object> photopathupdate = new HashMap<>();
        photopathupdate.put("photoRoot", downloadUri);
        userAcitive.updateChildren(photopathupdate);

        ProgressDialogHelper.getInstance().removeProgressbar();
        //넘기기
        intent = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}