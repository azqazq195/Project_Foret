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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.helper.FileUtils;
import com.example.foret_app_prototype.helper.PhotoHelper;
import com.example.foret_app_prototype.model.Foret;
import com.example.foret_app_prototype.model.Member;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class EditForetActivity extends AppCompatActivity implements View.OnClickListener {
    Member member;
    Foret foret;

    AsyncHttpClient client;
    EditForetResponse editForetResponse;
    String url = "";

    ImageView profile, button_close, button_tag_edit, button_region_edit, button_member_edit;
    Button button_complete;
    TextView textView1, textView_tag, textView_region, textView_member, textView_master, textView_birth;
    EditText editText_intro, textView_name;

    Intent intent;
    File file;
    String filePath = null;

    String select_si = "";
    String select_gu = "";
    String select_tag = "";
    String str = "";
    String show = "";
    int max_member_count = 0;

    List<String> region_si;
    List<String> region_gu;
    List<String> foret_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_foret);

        getFindId();
        foret = (Foret) getIntent().getSerializableExtra("foret");

        dataSetting();

    }

    private void getFindId() {
        profile = findViewById(R.id.profile);
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

        region_si = new ArrayList<>();
        region_gu = new ArrayList<>();
        foret_tag = new ArrayList<>();

        textView1.setOnClickListener(this);
        button_close.setOnClickListener(this);
        button_member_edit.setOnClickListener(this);
        button_tag_edit.setOnClickListener(this);
        button_region_edit.setOnClickListener(this);
        button_complete.setOnClickListener(this);
    }

    private void dataSetting() {
//        Glide.with(this).load(foret.getForet_photo()).into(profile);
        profile.setImageResource(foret.getForetImage());
        textView_name.setText(foret.getName());
        textView_tag.setText(Arrays.toString(foret.getForet_tag()));
        textView_region.setText(Arrays.toString(foret.getForet_region()));
        textView_member.setText(foret.getMember().length + "/" + foret.getMax_member());
        textView_master.setText("포레 리더 : " + foret.getLeader());
        textView_birth.setText(foret.getReg_date());
        editText_intro.setText(foret.getIntroduce());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView1 : // 사진수정 - 이 텍스트뷰를 누르면 텍스트뷰 밑에 수정된 사진이 보인다
                showSelect();
                break;
            case R.id.button_close : // x 버튼
                finish();
                break;
            case R.id.button_tag_edit : // 태그 수정
                tagSelectDialog();
                break;
            case R.id.button_region_edit : // 지역 수정
                regionSelectDialog();
                break;
            case R.id.button_member_edit : // 최대 인원 수정
                memberSelectDialog();
                break;
            case R.id.button_complete : // 수정완료
                modify();
                break;
        }
    }

    private void modify() {
        String edit_name = textView_name.getText().toString();
        String edit_intro = editText_intro.getText().toString();
        if(edit_name.equals("")) {
            Toast.makeText(this, "포레 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(edit_intro.equals("")) {
            Toast.makeText(this, "포레 소개를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] str_si = new String[region_si.size()];
        String[] str_gu = new String[region_gu.size()];
        for(int a=0; a<str_si.length; a++) {
            str_si[a] = region_si.get(a);
            str_gu[a] = region_gu.get(a);
        }

        String[] str_tag = new String[foret_tag.size()];
        for(int a=0; a<str_tag.length; a++) {
            str_tag[a] = foret_tag.get(a);
        }

        client = new AsyncHttpClient();
        editForetResponse = new EditForetResponse();
        RequestParams params = new RequestParams();

        params.put("leader_id", member.getId());
        params.put("name", edit_name);
        params.put("introduce", edit_intro);
        params.put("max_member", max_member_count);
        params.put("tag", str_tag);
        params.put("region_si", str_si);
        params.put("region_gu", str_gu);
        if(file != null) {
            params.put("photo", filePath);
        }
        params.put("id", foret.getId());
        client.post(url, params, editForetResponse);
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
                            uri = FileProvider.getUriForFile(EditForetActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);
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
                    Log.d("[TEST]", "fileName = " + fileName);
                    filePath= FileUtils.getPath(this, data.getData());
                    Log.d("[TEST]", "filePath = " + filePath);
                    Toast.makeText(this, fileName + "을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    Glide.with(this).load(filePath).into(profile);
            }
        }
    }

    private void modifyCheck() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("수정 하시겠습니까?");
        builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "포레를 수정했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void memberSelectDialog() { //결과 : 인원수 정보 텍스트뷰에 출력
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View region_view = getLayoutInflater().inflate(R.layout.guide_select_region, null);
        builder.setTitle("최대 인원수를 선택해주세요.");

        max_member_count = 0;

        Spinner spinner_max_member = region_view.findViewById(R.id.spinner_max_member);
        TextView selected_view = region_view.findViewById(R.id.selected_view);

        spinner_max_member.setVisibility(View.VISIBLE);
        spinner_max_member.setSelection((foret.getMax_member()-1));


        spinner_max_member.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("[TEST]", "foret.getMember().length => " + foret.getMember().length);
                if(foret.getMember().length < (position+1)) {
                    max_member_count = position+1;
                    Log.d("[TEST]", "max_member_count => " + max_member_count);
                } else {
                    spinner_max_member.setSelection((foret.getMax_member()-1));
                    selected_view.setText("현재 소속된 인원수보다 작습니다.\n" +
                            "현재 소속된 인원수 : " + foret.getMember().length);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //확인 버튼 누르면
                textView_member.setText(foret.getMember().length + "/" + max_member_count);
            }
        });
        builder.setNegativeButton("취소", null);

        builder.setView(region_view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void regionSelectDialog() { // 결과 : 지역 정보 텍스트뷰에 출력
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
                select_si = (String) parent.getSelectedItem();
                if(position != 0 && !select_si.equals("")) {
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
                if(position != 0 && !select_gu.equals("")) {
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
                //확인 버튼 누르면
                if (str.equals("")) {
                    textView_region.setText("등록된 지역이 없습니다.");
                } else if(region_si.size() != region_gu.size()) {
                    for(int a=0; a<region_si.size(); a++) {
                        show += region_gu.get(a) + " ";
                    }
                }
                for (int a=0; a<region_si.size(); a++) {
                    show += region_si.get(a) + " " + region_gu.get(a) + ", ";
                    Log.d("[TEST]", "region_si.get(a) => " + region_si.get(a));
                    Log.d("[TEST]", "region_gu.get(a) => " + region_gu.get(a));

                    textView_region.setText(show.substring(0, show.length()-2));
                }
            }
        });
        builder.setNegativeButton("취소", null);

        builder.setView(region_view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void tagSelectDialog() { //결과 : 태그 정보 텍스트뷰에 출력
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
                    foret_tag.add(select_tag);
                    Log.d("[TEST]", "foret_tag.size() => " + foret_tag.size());
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
                //확인 버튼 누르면
                if(str.equals("")) {
                    textView_tag.setText("등록된 태그가 없습니다.");
                } else {
                    for (int a = 0; a < foret_tag.size(); a++) {
                        show += "#" + foret_tag.get(a) + " ";
                        Log.d("[TEST]", "foret_tag.get(a) => " + foret_tag.get(a));
                    }
                    textView_tag.setText(show);
                }
            }
        });
        builder.setNegativeButton("취소", null);

        builder.setView(region_view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    class EditForetResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            try {
                JSONObject json = new JSONObject(str);
                String rt = json.getString("rt");
                if(rt.equals("OK")) {
                    modifyCheck();
                } else {
                    Toast.makeText(EditForetActivity.this, "포레를 수정하지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(EditForetActivity.this, "통신 실패", Toast.LENGTH_SHORT).show();
        }
    }

}