package com.example.foret_app_prototype.activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.foret_app_prototype.R;
import com.example.foret_app_prototype.activity.MainActivity;
import com.example.foret_app_prototype.model.Member;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinUsActivity extends AppCompatActivity implements View.OnClickListener {
    Member member;

    Toolbar toolbar;

    LinearLayout layout1, layout2, layout3, layout4, layout5, layout6;
    EditText editText1, editText2, editText3, editText4, editText5, editText6;
    TextView textView1, textView2, textView3, textView4, textView5;
    ImageView show_pw, logo, check1, check2, check3, check4, check5, check6;
    Intent intent = null;

    int birth_length = 8; // 생년월일 자리수 체크
    int pw_length = 6; // 비밀번호 최소자리수
    int pw_length2 = 12; // 비밀번호 최대자리수

    String name;
    String nickname;
    String birth;
    String email;
    String pw;

    String nameValidation = "^[A-z|가-힣]([A-z|가-힣]*)$";
    String nickValidation = "^[A-z|가-힣|0-9]([A-z|가-힣|0-9]*)$";
    String emailValidation = "^[A-z|0-9]([A-z|0-9]*)(@)([A-z]*)(\\.)([a-zA-Z]){2,3}$";
    String pwValidation = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{6,12}$";

    int name_eq = 0;     // 이름 체크
    int nick_eq = 0;     // 닉네임 체크
    int birty_eq = 0;    // 생일 체크
    int email_eq = 0;    // 이멜 체크
    int pw_eq = 0;       // 비번 체크
    int pw2_eq = 0;      // 비번확인 체크
    int check_count = 0; // 체크 카운트

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_us);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기존 타이틀 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        getFind(); // 객체 초기화

        editText1.requestFocus(); // 이름 포커스
        checkName();

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void checkName() {
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                name = editText1.getText().toString().trim();
                if(name.matches(nameValidation) && s.length() > 0) {
                    textView1.setText("이름을 입력했습니다.");
                    textView1.setTextColor(Color.parseColor("#FF0000FF"));
                    check1.setVisibility(View.VISIBLE);
                } else {
                    textView1.setText("잘못된 이름입니다.");
                    textView1.setTextColor(Color.parseColor("#FF0000"));
                    check1.setVisibility(View.INVISIBLE);
                }
                if(check1.getVisibility() == View.VISIBLE) {
                    name_eq = 1;
                    Log.d("[TEST]", "[NAME]name_eq => " + name_eq);
                } else {
                    name_eq = 0;
                    Log.d("[TEST]", "[NAME]name_eq => " + name_eq);
                }
                Log.d("[TEST]", "name => " + name);
                Log.d("[TEST]", "name.matches(emailValidation) => " + name.matches(nameValidation));
                Log.d("[TEST]", "s.length() => " + s.length());
            }
        });
    }

    private void editName() {
        name = editText1.getText().toString().trim();
        if(name.equals("")) {
            textView1.setText("이름을 입력해주세요.");
            textView1.setTextColor(Color.parseColor("#FF0000"));
            return;
        }
        check_count++;
        Log.d("[TEST]", "[name]check_count => " + check_count);
        animation = new AlphaAnimation(0, 1);
        animation.setDuration(1000);
        layout2.startAnimation(animation);
        layout2.setVisibility(View.VISIBLE);
        editText2.requestFocus();
        checkNick();
    }

    private void checkNick() {
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                nickname = editText2.getText().toString().trim();
                if(nickname.matches(nickValidation) && s.length() > 0) {
                    textView2.setText("사용 가능한 닉네임입니다.");
                    textView2.setTextColor(Color.parseColor("#FF0000FF"));
                    check2.setVisibility(View.VISIBLE);
                } else {
                    textView2.setText("사용할 수 없는 닉네임입니다.");
                    textView2.setTextColor(Color.parseColor("#FF0000"));
                    check2.setVisibility(View.INVISIBLE);
                }
                if(check2.getVisibility() == View.VISIBLE) {
                    nick_eq = 1;
                    Log.d("[TEST]", "[NICK]nick_eq => " + nick_eq);
                } else {
                    nick_eq = 0;
                    Log.d("[TEST]", "[NICK]nick_eq => " + nick_eq);
                }
                Log.d("[TEST]", "nickname => " + nickname);
                Log.d("[TEST]", "nickname.matches(emailValidation) => " + nickname.matches(nickValidation));
                Log.d("[TEST]", "s.length() => " + s.length());
            }
        });
    }

    private void editNick() {
        nickname = editText2.getText().toString().trim();
        if(nickname.equals("")) {
            textView2.setText("닉네임을 입력해주세요.");
            textView2.setTextColor(Color.parseColor("#FF0000"));
            return;
        }
        check_count++;
        Log.d("[TEST]", "[nick]check_count => " + check_count);
        animation = new AlphaAnimation(0, 1);
        animation.setDuration(1000);
        layout3.startAnimation(animation);
        layout3.setVisibility(View.VISIBLE);
        editText3.requestFocus();
        checkBirth();
    }

    private void checkBirth() {
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                birth = editText3.getText().toString().trim();
                if(birth_length == s.length()) {
                    textView3.setText("생년월일을 입력했습니다.");
                    textView3.setTextColor(Color.parseColor("#FF0000FF"));
                    check3.setVisibility(View.VISIBLE);
                } else {
                    textView3.setText("생년월일 8자리를 입력해주세요.");
                    textView3.setTextColor(Color.parseColor("#FF0000"));
                    check3.setVisibility(View.INVISIBLE);
                }
                if(check3.getVisibility() == View.VISIBLE) {
                    birty_eq = 1;
                    Log.d("[TEST]", "[BIRTH]birty_eq => " + birty_eq);
                } else {
                    birty_eq = 0;
                    Log.d("[TEST]", "[BIRTH]birty_eq => " + birty_eq);
                }
                Log.d("[TEST]", "birth => " + birth);
                Log.d("[TEST]", "s.length() => " + s.length());
            }
        });
    }

    private void editBirth() {
        birth = editText3.getText().toString().trim();
        if(birth.equals("")) {
            textView3.setText("생년월일을 입력해주세요.");
            textView3.setTextColor(Color.parseColor("#FF0000"));
            return;
        } else if(birth.length() < birth_length) {
            textView3.setText("생년월일을 8자리를 입력해주세요.");
            textView3.setTextColor(Color.parseColor("#FF0000"));
            return;
        }
        check_count++;
        Log.d("[TEST]", "[birth]check_count => " + check_count);
        animation = new AlphaAnimation(0, 1);
        animation.setDuration(1000);
        layout4.startAnimation(animation);
        layout4.setVisibility(View.VISIBLE);
        editText4.requestFocus();
        checkEmail();
    }


    private void checkEmail() {
        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                email = editText4.getText().toString().trim();
                if(email.matches(emailValidation) && s.length() > 0) {
                    textView4.setText("사용 가능한 이메일입니다.");
                    textView4.setTextColor(Color.parseColor("#FF0000FF"));
                    check4.setVisibility(View.VISIBLE);
                } else {
                    textView4.setText("이메일 형식으로 입력해주세요.");
                    textView4.setTextColor(Color.parseColor("#FF0000"));
                    check4.setVisibility(View.INVISIBLE);
                }
                if(check4.getVisibility() == View.VISIBLE) {
                    email_eq = 1;
                    Log.d("[TEST]", "[EMAIL]email_eq => " + email_eq);
                } else {
                    email_eq = 0;
                    Log.d("[TEST]", "[EMAIL]email_eq => " + email_eq);
                }
                Log.d("[TEST]", "email => " + email);
                Log.d("[TEST]", "email.matches(emailValidation) => " + email.matches(emailValidation));
                Log.d("[TEST]", "s.length() => " + s.length());
            }
        });
    }

    private void editEmail() {
        email = editText4.getText().toString().trim();
        if (email.equals("")) {
            textView4.setText("이메일을 입력해주세요.");
            textView4.setTextColor(Color.parseColor("#FF0000"));
            return;
        } else if (!email.matches(emailValidation)) {
            textView4.setText("이메일 형식으로 입력해주세요.");
            textView4.setTextColor(Color.parseColor("#FF0000"));
            return;
        }
        check_count++;
        Log.d("[TEST]", "[email]check_count => " + check_count);
        animation = new AlphaAnimation(0, 1);
        animation.setDuration(1000);
        layout5.startAnimation(animation);
        layout5.setVisibility(View.VISIBLE);
        editText5.requestFocus();
        checkPw();
    }

    private void checkPw() {
        editText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                pw = editText5.getText().toString().trim();
                if(pw.matches(pwValidation) && s.length() > 0) {
                    textView5.setText("사용 가능한 비밀번호입니다.");
                    textView5.setTextColor(Color.parseColor("#FF0000FF"));
                    check5.setVisibility(View.VISIBLE);
                } else {
                    textView5.setText("영문+숫자+특수문자를 포함하세요.");
                    textView5.setTextColor(Color.parseColor("#FF0000"));
                    check5.setVisibility(View.INVISIBLE);
                }
                if(check5.getVisibility() == View.VISIBLE) {
                    pw_eq = 1;
                    Log.d("[TEST]", "[PW]pw_eq => " + pw_eq);
                } else {
                    pw_eq = 0;
                    Log.d("[TEST]", "[PW]pw_eq => " + pw_eq);
                }
                Log.d("[TEST]", "pw => " + pw);
                Log.d("[TEST]", "pw.matches(emailValidation) => " + pw.matches(pwValidation));
                Log.d("[TEST]", "s.length() => " + s.length());
            }
        });
    }

    private void editPw() {
        pw = editText5.getText().toString().trim();
        Log.d("[TEST]", "pw.length() => " + pw.length());
        if(pw.equals("")) {
            textView5.setText("비밀번호를 입력해주세요.");
            textView5.setTextColor(Color.parseColor("#FF0000"));
            return;
        } else if(pw.length() < pw_length) {
            textView5.setText("최소 6자이상 입력해주세요.");
            textView5.setTextColor(Color.parseColor("#FF0000"));
            return;
        } else if(pw.length() > pw_length2) {
            textView5.setText("최대 12자이하 입력해주세요.");
            textView5.setTextColor(Color.parseColor("#FF0000"));
            return;
        } else if(!pw.matches(pwValidation)) {
            textView5.setText("영문+숫자+특수문자를 포함하세요.");
            textView5.setTextColor(Color.parseColor("#FF0000"));
            return;
        }
        check_count++;
        Log.d("[TEST]", "[pw]check_count => " + check_count);
        animation = new AlphaAnimation(0, 1);
        animation.setDuration(1000);
        layout6.startAnimation(animation);
        layout6.setVisibility(View.VISIBLE);
        editText6.requestFocus();
        checkpw2();
    }

    private void checkpw2() {
        editText6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pw2 = editText6.getText().toString().trim();
                if(pw2.equals(pw)) {
                    textView5.setText("비밀번호를 확인했습니다.");
                    textView5.setTextColor(Color.parseColor("#FF0000FF"));
                    check6.setVisibility(View.VISIBLE);
                } else {
                    textView5.setText("비밀번호를 확인해주세요.");
                    textView5.setTextColor(Color.parseColor("#FF0000"));
                    check6.setVisibility(View.INVISIBLE);
                }
                if(check6.getVisibility() == View.VISIBLE) {
                    pw2_eq = 1;
                    Log.d("[TEST]", "[PW]pw_eq => " + pw_eq);
                } else {
                    pw2_eq = 0;
                    Log.d("[TEST]", "[PW]pw_eq => " + pw_eq);
                }
                Log.d("[TEST]", "s.length() => " + s.length());
            }
        });
    }

    private void editPw2() {
        String pw2 = editText6.getText().toString().trim();
        if (!pw2.equals(pw)) {
            textView5.setText("비밀번호가 다릅니다.");
            textView5.setTextColor(Color.parseColor("#FF0000"));
            return;
        }
        check_count++;
        Log.d("[TEST]", "[pw2]check_count => " + check_count);
        inputCheck();
    }

    private boolean checkJoin() {
        if(check_count != 6) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_pw:
                if (show_pw.getTag().equals("0")) { //비밀번호 안 보이고 있던 상황
                    show_pw.setTag("1");
                    show_pw.setImageResource(R.drawable.pw_show); //켜져있는 아이콘으로 바꾼다.
                    //비밀번호를 보이게 한다.
                    editText5.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    editText6.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else { //비밀번호 보이고 있던 상황
                    show_pw.setTag("0");
                    show_pw.setImageResource(R.drawable.pw); //꺼져있는 아이콘으로 바꾼다.
                    //비밀번호를 가린다.
                    editText5.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editText6.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }
                //커서 맨 뒤로
                editText5.setSelection(editText5.getText().length());
                editText6.setSelection(editText6.getText().length());
        }
    }

    // 메뉴 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 수행 표시줄에 항목이 있는 경우 이 항목이 추가됨.
        getMenuInflater().inflate(R.menu.join_us_menu, menu);
        return true;
    }

    // 메뉴 버튼 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("[TEST]", "[이벤트]check_count => " + check_count);
        switch (item.getItemId()){
            case R.id.next: // 다음
                switch (check_count) {
                    case 0:
                        editName();
                        break;
                    case 1:
                        editNick();
                        break;
                    case 2:
                        editBirth();
                        break;
                    case 3:
                        editEmail();
                        break;
                    case 4:
                        editPw();
                        break;
                    case 5:
                        editPw2();
                        break;
                    case 6:
                        inputCheck();
                }
                break;
            case android.R.id.home: // 뒤로가기 버튼
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inputCheck() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("입력하신 정보가 맞습니까?");
        builder.setPositiveButton("다음", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(checkJoin()) {
                    intent = new Intent(getApplicationContext(), GuideActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.setNegativeButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void getFind() {
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);
        layout5 = findViewById(R.id.layout5);
        layout6 = findViewById(R.id.layout6);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        logo = findViewById(R.id.logo);
        show_pw = findViewById(R.id.show_pw);
        check1 = findViewById(R.id.check1);
        check2 = findViewById(R.id.check2);
        check3 = findViewById(R.id.check3);
        check4 = findViewById(R.id.check4);
        check5 = findViewById(R.id.check5);
        check6 = findViewById(R.id.check6);

        show_pw.setOnClickListener(this);
    }
}