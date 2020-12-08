package com.example.foret_app_prototype.activity.foret;

import android.Manifest;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.login.GuideActivity;
import com.example.foret_app_prototype.activity.login.SessionManager;
import com.example.foret_app_prototype.activity.notify.APIService;
import com.example.foret_app_prototype.helper.CalendarHelper;
import com.example.foret_app_prototype.helper.FileUtils;
import com.example.foret_app_prototype.helper.PhotoHelper;
import com.example.foret_app_prototype.helper.ProgressDialogHelper;
import com.example.foret_app_prototype.model.Foret;
import com.example.foret_app_prototype.model.Member;
import com.example.foret_app_prototype.model.MemberDTO;
import com.example.foret_app_prototype.model.ModelUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
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

public class MakeForetActivity extends AppCompatActivity implements View.OnClickListener {
    Member member;

    AsyncHttpClient client;
    ForetResponse foretResponse;
    String url = "http://34.72.240.24:8085/foret/foret/foret_insert.do";
    //String url = "http://192.168.0.180:8085/foret/foret/foret_insert.do";
    ImageView image_View_picture, button_cancel;
    EditText editText_name, editText_member, editText_intro;
    Button button_complete, button_picture;
    TextView button_region, button_tag;
    String filePath = null;
    Intent intent;
    File file;
    MemberDTO memberDTO;
    String select_si = "";
    String select_gu = "";
    String select_tag = "";
    String str = "";
    String show = "";
    List<String> selected_tag;
    List<String> region_si;
    List<String> region_gu;
    List<String> foret_tag;
    List<String> tag_name;
    List<String> tag_list;
    boolean ischecked = false;

    boolean isFinishMakeChat;
    boolean isFinishMakeDB;
    boolean isFinishMakeImage;


    //파이어 베이스 채팅방 리소스
    private FirebaseAuth firebaseAuth;
    ModelUser user;
    String userUid;
    Foret foret;
    String group_name;

    int leader_id;
    String name;
    int max_member;
    String introduce;
    Context context;
    String downloadUri;
    Uri uri;

    String foret_id;

    //RegionListResponse3 regionListResponse;
    TagListResponse3 tagListResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_foret);

        // 상태바 색상 변경
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.foret4));

        context = this;
        image_View_picture = findViewById(R.id.imageView_picture);
        button_cancel = findViewById(R.id.button_cancel);
        editText_name = findViewById(R.id.editText_name);
        editText_member = findViewById(R.id.editText_member);
        editText_intro = findViewById(R.id.editText_intro);
        button_complete = findViewById(R.id.button_complete);
        button_picture = findViewById(R.id.button_picture);
        button_region = findViewById(R.id.button_region);
        button_tag = findViewById(R.id.button_tag);

        memberDTO = (MemberDTO)getIntent().getSerializableExtra("memberDTO");

        region_si = new ArrayList<>();
        region_gu = new ArrayList<>();
        foret_tag = new ArrayList<>();
        tag_name = new ArrayList<>();
        tag_list = new ArrayList<>();
        button_cancel.setOnClickListener(this);
        button_picture.setOnClickListener(this);
        button_complete.setOnClickListener(this);
        button_region.setOnClickListener(this);
        button_tag.setOnClickListener(this);
        selected_tag = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();

        client = new AsyncHttpClient();
        foretResponse = new ForetResponse();
        //regionListResponse = new RegionListResponse3();
        tagListResponse = new TagListResponse3();

        //각 지역, 태그 리스트에 DB에 저장된 목록 저장
        //client.post("http://34.72.240.24:8085/foret/region/region_list.do", regionListResponse);
        client.post("http://34.72.240.24:8085/foret/tag/tag_list.do", tagListResponse);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_complete: //완료(확인 버튼)

                if(file == null){
                    Toast.makeText(context,"포레 대표 사진을 선택해주세요.",Toast.LENGTH_LONG).show();
                }else{
                    foretInsert();
                }

                break;
            case R.id.button_cancel: //취소 버튼
                finish();
                break;
            case R.id.button_region: //지역 고르기
                regionDialog();
                break;
            case R.id.button_tag: //태그 고르기
                tagDialog();
                break;
            case R.id.button_picture: //사진 고르기
                permissionCheck();
                showSelect();
                break;
        }
    }

    private void foretInsert() {
         name = editText_name.getText().toString().trim();
        max_member = Integer.parseInt(editText_member.getText().toString().trim());
        introduce = editText_intro.getText().toString().trim();

        if (name.equals("")) {
            Toast.makeText(this, "포레 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (max_member == 0) {
            Toast.makeText(this, "최소 1명이상을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (introduce.equals("")) {
            Toast.makeText(this, "포레 소개를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams params = new RequestParams();

        String[] str_si = new String[region_si.size()];
        String[] str_gu = new String[region_gu.size()];

        for (int a = 0; a < str_si.length; a++) {
            str_si[a] = region_si.get(a);
            str_gu[a] = region_gu.get(a);
            if (a == 0) {
                params.put("region_si", str_si[a]);
                params.put("region_gu", str_gu[a]);
                Log.e("[test]","리전?"+str_si[a]+","+str_gu[a]);
            } else {
                params.add("region_si", str_si[a]);
                params.add("region_gu", str_gu[a]);
                Log.e("[test]","리전?"+str_si[a]+","+str_gu[a]);
            }

        }
        String[] str_tag = new String[foret_tag.size()];
        for (int a = 0; a < str_tag.length; a++) {
            str_tag[a] = foret_tag.get(a);

            if (a == 0) {
                params.put("tag", str_tag[a]);
                Log.e("[test]","태그??"+str_tag[a]);
            } else {
                params.add("tag", str_tag[a]);
                Log.e("[test]","태그??"+str_tag[a]);
            }
        }

        SessionManager sessionManager = new SessionManager(this);
        leader_id = sessionManager.getSession();

        params.put("leader_id", leader_id);
        params.put("name", name);
        params.put("introduce", introduce);
        params.put("max_member", max_member);
       // params.put("tag", str_tag);
       // params.put("region_si", str_si);
       // params.put("region_gu", str_gu);
        try {
            if (file != null)
                params.put("photo", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.setForceMultipartEntityContentType(true);
        final int DEFAULT_TIME = 20*1000;
        client.setConnectTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);
        client.setTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);
        client.post(url, params, foretResponse);


        ProgressDialogHelper.getInstance().getProgressbar(this,"포레 생성중");

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
                        guAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.seuol_gu, R.layout.support_simple_spinner_dropdown_item);
                        spinner_gu.setAdapter(guAdapter);
                        break;
                    case 2:
                        guAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gyeonggi_si, R.layout.support_simple_spinner_dropdown_item);
                        spinner_gu.setAdapter(guAdapter);
                        break;
                    case 3:
                        guAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.daejeon_gu, R.layout.support_simple_spinner_dropdown_item);
                        spinner_gu.setAdapter(guAdapter);
                        break;
                    case 4:
                        guAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gangwon_si, R.layout.support_simple_spinner_dropdown_item);
                        spinner_gu.setAdapter(guAdapter);
                        break;
                    case 5:
                        guAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gwangju_gu, R.layout.support_simple_spinner_dropdown_item);
                        spinner_gu.setAdapter(guAdapter);
                        break;
                    case 6:
                        guAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.busan_gu, R.layout.support_simple_spinner_dropdown_item);
                        spinner_gu.setAdapter(guAdapter);
                        break;
                    case 7:
                        guAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.jeju_si, R.layout.support_simple_spinner_dropdown_item);
                        spinner_gu.setAdapter(guAdapter);
                        break;
                }
                spinner_gu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                //확인 버튼 누르면
                for (int a = 0; a < region_si.size(); a++) {
                    show += region_si.get(a) + " " + region_gu.get(a) + "\n";
                    Log.d("[TEST]", "region_si.get(a) => " + region_si.get(a));
                    Log.d("[TEST]", "region_gu.get(a) => " + region_gu.get(a));
                }
                button_region.setText(show);
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
        builder.setMessage("태그를 선택해주세요.");

        str = "";
        show = "";
        ischecked = false;
        selected_tag = new ArrayList<>();

        Spinner spinner_tag = region_view.findViewById(R.id.spinner_tag);
        TextView selected_view = region_view.findViewById(R.id.selected_view);

        ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tag_list);
        spinner_tag.setVisibility(View.VISIBLE);
        spinner_tag.setAdapter(adapter);

        spinner_tag.setSelection(0);

        spinner_tag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Log.d("[TEST]", "position => " + position);
                    String select_tag = (String) parent.getSelectedItem();
                    selected_tag.add(select_tag);
                    str += "#" + select_tag + " ";
                    selected_view.setText(str);
                    spinner_tag.setSelection(0);
                    ischecked = true;
                }
                Log.d("[TEST]", "ischecked => " + ischecked);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 확인 버튼 누르면
                if (ischecked) {
                    foret_tag = selected_tag;
                    Log.d("[TEST]", "member_tag.size() => " + foret_tag.size());

                    for (int a = 0; a < foret_tag.size(); a++) {
                        show += "#" + foret_tag.get(a) + " ";
                        Log.d("[TEST]", "foret_tag.get(a) => " + foret_tag.get(a));
                    }
                    button_tag.setText(show);
                    button_tag.setVisibility(View.VISIBLE);
                    ischecked = false;
                } else if (foret_tag.size() == 0) {
                    Toast.makeText(MakeForetActivity.this, "최소 1개의 태그를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("취소", null);

        builder.setView(region_view);
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
        final String[] menu = {"새로 촬영하기", "갤러리에서 가져오기"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: //새로 촬영하기-카메라 호출
                        filePath = PhotoHelper.getInstance().getNewPhotoPath(); //저장할 사진 경로
                        Log.d("[TEST]", "photoPath = " + filePath);

                        file = new File(filePath);
                        uri = null;

                        //카메라앱 호출을 위한 암묵적 인텐트 (action과 uri가 필요하다)
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(MakeForetActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        Log.d("[TEST]", "uri : " + uri.toString());

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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    Toast.makeText(this, "사진 첨부 완료", Toast.LENGTH_SHORT).show();
                    //촬영 결과물을 MediaStore에 등록한다(갤러리에 저장). MediaStore에 등록하지 않으면 우리 앱에서 만든 파일을 다른 앱에서는 사용할 수 없다.
                    intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(filePath));
                    Log.d("[TEST]", filePath);
                    sendBroadcast(intent);
                    Glide.with(this).load(filePath).into(image_View_picture);
                    break;
                case 300:
                    String uri1 = data.getData().toString();
                    uri = data.getData();
                    String fileName = uri1.substring(uri1.lastIndexOf("/") + 1);
                    Log.d("[TEST]", "fileName = " + fileName);
                    filePath = FileUtils.getPath(this, data.getData());
                    Log.d("[TEST]", "filePath = " + filePath);
                    file = new File(filePath);
                    Toast.makeText(this, fileName + "을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    Glide.with(this).load(filePath).into(image_View_picture);
            }
        }
    }


    class ForetResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.e("[test]","통신 성공");
            ProgressDialogHelper.getInstance().removeProgressbar();

            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String rt = json.getString("foretRT");
                String foretPhotoRT =json.getString("foretPhotoRT");
                String foretMemberRT =json.getString("foretMemberRT");
                String foretRegionRT =json.getString("foretRegionRT");
                String foretTagRT =json.getString("foretTagRT");

                foret_id = json.getString("foret_id");

                //포레 멤버로 가입입

               Toast.makeText(context,"결과? \n foretPhotoRT : "+foretPhotoRT+"\n foretMemberRT : "+foretMemberRT +"\n foretRegionRT : "+foretRegionRT
                +"\n foretTagRT : "+ foretTagRT,Toast.LENGTH_SHORT).show();
                Log.e("[test]","결과? foretPhotoRT : "+foretPhotoRT+", foretMemberRT : "+foretMemberRT +", foretRegionRT : "+foretRegionRT
                        +", foretTagRT : "+ foretTagRT);
                if (rt.equals("OK")) {
                    //파이어 베이스용 데이터 삽입
                    foret = new Foret();
                    foret.setName(name);
                    foret.setIntroduce(introduce);
                    foret.setMax_member(max_member);
                    if (file != null) {
                        foret.setForet_photo(filePath);
                    }
                    foret.setForet_photo(filePath);
                    String makeForetTime = CalendarHelper.getInstance().getCurrentTimeFull();
                    foret.setReg_date(makeForetTime);
                    DatabaseReference userName = FirebaseDatabase.getInstance().getReference("Users");
                    Log.e("[test]","포레 생성중");
                    ProgressDialogHelper.getInstance().removeProgressbar();

                    if(!uri.equals("")||uri!=null){
                        sendImageMessage(uri);
                    }else {
                        isFinishMakeImage = true;
                    }

                    userName.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Log.e("[test]","레퍼런스?"+userName.getRef().toString());
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Log.e("[test]","firebaseAuth.getCurrentUser().getUid()  : "+firebaseAuth.getCurrentUser().getUid());

                                if (firebaseAuth.getCurrentUser().getUid().equals(ds.child("uid").getValue())) {
                                    user = ds.getValue(ModelUser.class);
                                    foret.setLeader(user.getNickname());
                                    Log.e("[test]","user.getNickname() : "+user.getNickname());
                                    Log.e("[test]",user.getNickname());


                                    ProgressDialogHelper.getInstance().getProgressbar(context,"채팅방 생성중입니다.");
                                    Log.e("[test]","파이어 베이스에 포레 생성중");
                                    Log.e("[test]"," 자료들 확인 : "+foret.getName()+foret.getIntroduce());
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("GroupName", foret.getName());
                                    if(!uri.equals("")||uri!=null){
                                        hashMap.put("GroupPhoto",downloadUri );
                                    }
                                    hashMap.put("GroupLeader", user.getUid()); //안들어가있음
                                    hashMap.put("GroupDescription", foret.getIntroduce());
                                    //hashMap.put("GroupId", ""+foret.getGroup_no());
                                    hashMap.put("GroupMaxMember", "" + foret.getMax_member());
                                    hashMap.put("GroupCurrentJoinedMember", "1");
                                    hashMap.put("Group_date_issued", foret.getReg_date());

                                    //그룹 항목 만들기
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups").child(foret.getName());
                                    ref.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            isFinishMakeDB = true;
                                            //유저 정보 얻로드 -- 안들어가있음

                                            HashMap<String, String> hashMap1 = new HashMap<>();
                                            hashMap1.put("participantName", user.getNickname());
                                            hashMap1.put("uid", "" + firebaseAuth.getCurrentUser().getUid());
                                            hashMap1.put("joinedDate", "" + System.currentTimeMillis());

                                            ref.child("participants").child(user.getUid()).setValue(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "포레와 채팅방 생성 성공!", Toast.LENGTH_LONG).show();
                                                    ProgressDialogHelper.getInstance().removeProgressbar();
                                                    isFinishMakeChat = true;

                                                    checkUploadStatus();

                                                }
                                            })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            ProgressDialogHelper.getInstance().removeProgressbar();
                                                            Toast.makeText(context, "유저 정보 업로드 실패", Toast.LENGTH_LONG).show();
                                                            checkUploadStatus();
                                                        }
                                                    });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("[test]","그룹생성 실패 원인 : "+e.getMessage());
                                            ProgressDialogHelper.getInstance().removeProgressbar();
                                            checkUploadStatus();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("[test]","error " +error.getMessage());
                        }
                    });

                } else {
                    Toast.makeText(MakeForetActivity.this, "포레를 만들지 못했습니다.", Toast.LENGTH_SHORT).show();
                    checkUploadStatus();
                }
                checkUploadStatus();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(MakeForetActivity.this, "통신 실패", Toast.LENGTH_SHORT).show();
            Log.e("[test]", error.getMessage()+"/ "+statusCode);
            checkUploadStatus();
        }
    }
    /*
    class RegionListResponse3 extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getInt("total") != 0) {
                    JSONArray region = json.getJSONArray("region");
                    for (int a=0; a<region.length(); a++) {
                        JSONObject object = region.getJSONObject(a);
                        region_si.add(object.getString("region_si"));
                        region_gu.add(object.getString("region_gu"));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(MakeForetActivity.this, "서버통신 에러", Toast.LENGTH_SHORT).show();
        }
    }
    */
    class TagListResponse3 extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if(json.getInt("total") != 0) {
                    JSONArray tag = json.getJSONArray("tag");
                    for (int a=0; a<tag.length(); a++) {
                        JSONObject object = tag.getJSONObject(a);
                        tag_list.add(object.getString("tag_name"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(MakeForetActivity.this, "서버통신 에러", Toast.LENGTH_SHORT).show();
        }
    }


    //이미지 보내기
    private void sendImageMessage(Uri image_rui) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String timeStamp = "" + System.currentTimeMillis();
        String fileNameAndPath = "GroupImages/" + "post_" + timeStamp + " by " + user.getUid() + " file : ";

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_rui);
            ByteArrayOutputStream baos = null;
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(fileNameAndPath + image_rui.getLastPathSegment());
            ref.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ProgressDialogHelper.getInstance().removeProgressbar();
                            Toast.makeText(context, "업로드 성공", Toast.LENGTH_LONG).show();
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                             downloadUri = uriTask.getResult().toString();
                            isFinishMakeImage = true;

                            checkUploadStatus();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ProgressDialogHelper.getInstance().removeProgressbar();
                            Toast.makeText(context, "업로드 실패", Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    void checkUploadStatus(){
        if(isFinishMakeChat=true&&isFinishMakeDB&&isFinishMakeImage){
            Intent intent = new Intent(context, ViewForetActivity.class);
            intent.putExtra("memberDTO",memberDTO);
            intent.putExtra("foret_id",foret_id);
            Log.e("[test]","메익 포레에서 만들어진 아이디?"+foret_id);
            startActivity(intent);
            finish(); // 현재 액티비티 종료
        }else{
            Toast.makeText(context,"모든 업로드 실패 각 상태 \n image? "+String.valueOf(isFinishMakeImage)
                    +"\n Chat?"+String.valueOf(isFinishMakeChat)+"\n DB?"+String.valueOf(isFinishMakeDB),Toast.LENGTH_SHORT).show();
        }
    }
}