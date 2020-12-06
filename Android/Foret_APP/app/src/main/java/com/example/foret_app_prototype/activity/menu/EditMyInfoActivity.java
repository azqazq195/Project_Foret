package com.example.foret_app_prototype.activity.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.login.GuideActivity;
import com.example.foret_app_prototype.helper.FileUtils;
import com.example.foret_app_prototype.helper.PhotoHelper;
import com.example.foret_app_prototype.helper.ProgressDialogHelper;
import com.example.foret_app_prototype.model.MemberDTO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class EditMyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    MemberDTO memberDTO;
    TextView textView1, textView2, button1, button2, textView_confirm;
    ImageView profile;
    EditText editText1, editText2, editText3;
    String filePath = null;
    Intent intent;
    String select_si = "";
    String select_gu = "";
    //String select_tag = "";
    String str = "";
    String tag_str_result = "";
    String show = "";
    List<String> selected_tag;
    List<String> region_si;
    List<String> region_gu;
    List<String> member_tag;
    List<String> tag_name;
    List<String> tag_list;
    boolean ischecked = false;
    AsyncHttpClient client;


    MyInfoEditResponse response;
    TagListResponse tagListResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        region_gu = new ArrayList<>();
        region_si = new ArrayList<>();
        member_tag = new ArrayList<>();
        setContentView(R.layout.activity_edit_my_info);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        memberDTO = (MemberDTO) getIntent().getSerializableExtra("memberDTO");

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        textView_confirm = findViewById(R.id.textView_confirm);
        profile = findViewById(R.id.profile);
        client = new AsyncHttpClient();
        //response = new MyInfoEditResponse();
        tagListResponse = new TagListResponse();
        tag_list = new ArrayList<>();
        region_si = new ArrayList<>();
        region_gu = new ArrayList<>();
        member_tag = new ArrayList<>();
        tag_name = new ArrayList<>();

        //각 지역, 태그 리스트에 DB에 저장된 목록 저장
        //client.post("http://34.72.240.24:8085/foret/region/region_list.do", regionListResponse);
        client.post("http://34.72.240.24:8085/foret/tag/tag_list.do", tagListResponse);

        dataSetting();

        profile.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText2.getText().toString().trim().equals(editText3.getText().toString().trim())) {
                    textView_confirm.setTextColor(Color.BLUE);
                    textView_confirm.setText("비밀번호가 일치합니다.");
                }
            }
        });

    }

    private void dataSetting() {

        textView1.setText(memberDTO.getEmail());
        textView2.setText(memberDTO.getId() + "");
        editText1.setText(memberDTO.getNickname());
        button1.setText(getIntent().getStringExtra("region"));
        button2.setText(getIntent().getStringExtra("tag"));
        Glide.with(this).load(memberDTO.getPhoto()).
                fallback(R.drawable.icon2)
                .into(profile);
    }

    @Override //메뉴 설정
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.modify_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override //메뉴 이벤트 처리
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modify:
                if (editText2.getText().toString().trim().equals("") || editText3.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!editText2.getText().toString().trim().equals(editText3.getText().toString().trim())) {
                    return false;
                }
                memberDTO.setPassword(editText2.getText().toString().trim());
                requestModify();
                break;
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile:
                showSelect();
                break;
            case R.id.button1:
                regionDialog();
                break;
            case R.id.button2:
                tagDialog();
                break;
        }
    }

    public void regionDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
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
                button1.setText(show);

            }
        });
        builder.setNegativeButton("취소", null);

        builder.setView(region_view);
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void tagDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View region_view = getLayoutInflater().inflate(R.layout.guide_select_region, null);
        builder.setMessage("태그를 골라주세요.");

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
                    member_tag = selected_tag;
                    Log.d("[TEST]", "member_tag.size() => " + member_tag.size());

                    for (int a = 0; a < member_tag.size(); a++) {
                        show += "#" + member_tag.get(a) + " ";
                        Log.d("[TEST]", "foret_tag.get(a) => " + member_tag.get(a));
                    }
                    button2.setText(show);
                    button2.setVisibility(View.VISIBLE);
                    ischecked = false;
                } else if (member_tag.size() == 0) {
                    Toast.makeText(EditMyInfoActivity.this, "최소 1개의 태그를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("취소", null);

        builder.setView(region_view);
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

                        File file = new File(filePath);
                        Uri uri = null;

                        //카메라앱 호출을 위한 암묵적 인텐트 (action과 uri가 필요하다)
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(EditMyInfoActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);
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
                    Glide.with(this).load(filePath).into(profile);
                    break;
                case 300:
                    String uri = data.getData().toString();
                    String fileName = uri.substring(uri.lastIndexOf("/") + 1);
                    Log.d("[TEST]", "fileName = " + fileName);
                    filePath = FileUtils.getPath(this, data.getData());
                    Log.d("[TEST]", "filePath = " + filePath);
                    Toast.makeText(this, fileName + "을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    Glide.with(this).load(filePath).into(profile);
            }
        }
    }

    private void requestModify() {
        RequestParams params = new RequestParams();

        //실제 태그 작동시 확인해야함
        String[] str_si = new String[region_si.size()];
        String[] str_gu = new String[region_gu.size()];

        for (int a = 0; a < str_si.length; a++) {
            str_si[a] = region_si.get(a);
            str_gu[a] = region_gu.get(a);
            if (a == 0) {
                params.put("region_si", str_si[a]);
                params.put("region_gu", str_gu[a]);
                Log.e("[test]", "리전?" + str_si[a] + "," + str_gu[a]);
            } else {
                params.add("region_si", str_si[a]);
                params.add("region_gu", str_gu[a]);
                Log.e("[test]", "리전?" + str_si[a] + "," + str_gu[a]);
            }

        }
        String[] str_tag = new String[member_tag.size()];
        for (int a = 0; a < str_tag.length; a++) {
            str_tag[a] = member_tag.get(a);
            if (a == 0) {
                params.put("tag", str_tag[a]);
                Log.e("[test]","태그??"+str_tag[a]);
            } else {
                params.add("tag", str_tag[a]);
                Log.e("[test]","태그??"+str_tag[a]);
            }
        }

        String tag = "";
        String region = (memberDTO.getRegion_si().toString() + "," + memberDTO.getRegion_gu().toString()).replace("[", "").replace("]", "");
        for (int a = 0; a < memberDTO.getTag().size(); a++) {
            tag += "#" + memberDTO.getTag().get(a) + " ";
        }


        memberDTO.setPassword(editText2.getText().toString().trim());
        memberDTO.setNickname(editText1.getText().toString().trim());

        params.put("name", memberDTO.getName());
        //params.put("email", memberDTO.getEmail());
        params.put("birth", memberDTO.getBirth());
        params.put("nickname", memberDTO.getNickname());
        params.put("password", memberDTO.getPassword());


        //params.put("region_si", str_si);
        //params.put("region_gu", str_gu);
        //params.put("tag", str_tag);
        params.put("id", memberDTO.getId());
        if (filePath != null) {
            try {
                params.put("photo", new File(filePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        params.setForceMultipartEntityContentType(true);

        final int DEFAULT_TIME = 50 * 1000;
        client.setConnectTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);
        client.setTimeout(DEFAULT_TIME);
        client.setResponseTimeout(DEFAULT_TIME);

        ProgressDialogHelper.getInstance().getProgressbar(this, "정보 수정 진행중.");
        client.post("http://34.72.240.24:8085:8085/foret/member/member_modify.do", params, response);
    }

    class MyInfoEditResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str_json = new String(responseBody);
            try {
                ProgressDialogHelper.getInstance().removeProgressbar();
                JSONObject json = new JSONObject(str_json);
                if (json.getString("memberRT").equals("OK") && json.getString("memberRegionRT").equals("OK")
                        && json.getString("memberTagRT").equals("OK") && json.getString("memberPhotoRT").equals("OK")) {
                    Toast.makeText(EditMyInfoActivity.this, "내 정보 수정이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("memberDTO", memberDTO);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(EditMyInfoActivity.this, "내 정보 수정 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            ProgressDialogHelper.getInstance().removeProgressbar();
            Toast.makeText(EditMyInfoActivity.this, "수정하기 500에러 뜸", Toast.LENGTH_SHORT).show();
        }
    }

    class RegionListResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if (json.getInt("total") != 0) {
                    JSONArray region = json.getJSONArray("region");
                    for (int a = 0; a < region.length(); a++) {
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
            Toast.makeText(EditMyInfoActivity.this, "서버통신 에러", Toast.LENGTH_SHORT).show();
        }
    }

    //서버애서 태그 받아오기
    class TagListResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                if (json.getInt("total") != 0) {
                    JSONArray tag = json.getJSONArray("tag");
                    for (int a = 0; a < tag.length(); a++) {
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
            Toast.makeText(EditMyInfoActivity.this, "서버통신 에러", Toast.LENGTH_SHORT).show();
        }
    }

}