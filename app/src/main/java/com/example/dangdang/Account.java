package com.example.dangdang;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Account extends AppCompatActivity {

    Button btn_Finish, btn_idcheck, btn_idcheckon, prevBtnJoin;
    EditText etxt_Name, etxt_ID, etxt_PW, etxt_NumberF, etxt_NumberB, etxt_RPW;
    TextView rule1, rule2, txt_pwchecked;
    Boolean check = false;

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;
    String sql;
    Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        btn_Finish = findViewById(R.id.btn_Finish);
        etxt_ID = findViewById(R.id.etxt_ID);
        etxt_PW = findViewById(R.id.etxt_PW);
        etxt_Name = findViewById(R.id.etxt_Name);
        etxt_NumberF = findViewById(R.id.etxt_NumberF);
        etxt_NumberB = findViewById(R.id.etxt_NumberB);
        rule1 = findViewById(R.id.rule1);
        rule2 = findViewById(R.id.rule2);
        btn_idcheck = findViewById(R.id.btn_idcheck);
        txt_pwchecked = findViewById(R.id.txt_checked);
        etxt_RPW = findViewById(R.id.etxt_RPW);
        btn_idcheckon = findViewById(R.id.btn_idcheckon);

        btn_idcheck.setVisibility(View.VISIBLE);


        helper = new DatabaseOpenHelper(Account.this, DatabaseOpenHelper.db, null, version);
        database = helper.getWritableDatabase();

        // 뒤로가기
        prevBtnJoin = findViewById(R.id.prevBtnJoin);
        prevBtnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, MainActivity.class);
                startActivity(intent);
            }
        });


        btn_Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String c_name = etxt_Name.getText().toString();
                String c_id = etxt_ID.getText().toString();
                String c_pw = etxt_PW.getText().toString();
                String c_rpw = etxt_RPW.getText().toString();
                String c_phone = "010" + etxt_NumberF.getText().toString() + etxt_NumberB.getText().toString();
                String c_state = "on";


                if (c_name.equals("") && c_pw.equals("") && c_phone.equals("")) {
                    Toast.makeText(Account.this, "회원정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (c_name.equals("") || c_pw.equals("") || c_phone.equals("")) {
                    Toast.makeText(Account.this, "회원정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {

                    if (check == false) {
                        Toast.makeText(Account.this, "중복확인을 해주세요", Toast.LENGTH_SHORT).show();
                    } else if (!etxt_PW.getText().toString().equals(etxt_RPW.getText().toString())) {
                        txt_pwchecked.setText("비밀번호가 일치하지않습니다.");
                    } else if (etxt_NumberF.getText().toString().length() < 4) {
                        Toast.makeText(Account.this, "휴대폰 번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                    } else if (etxt_NumberB.getText().toString().length() < 4) {
                        Toast.makeText(Account.this, "휴대폰 번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        helper.insertUser(database, c_name, c_id, c_pw, c_phone, c_state);
                        Toast.makeText(Account.this, "가입완료", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(in);
                        finish();
                        txt_pwchecked.setText("");
                        check = false;
                    }

                }

            }
        });


        rule1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                dlg.setTitle("이용약관");

                String ruletxt2 = "제14조(이용계약의 성립 등) ① \"이용자\"는 \"회사\"가 제공하는 다음 또는 이와 유사한 절차에 의하여 이용신청을 합니다. \"회사\"는 계약 체결 전에 각 호의 사항에 관하여 \"이용자\"가 정확하게 이해하고 실수 또는 착오 없이 거래할 수 있도록 정보를 제공합니다.\n" +
                        "1. \"콘텐츠\" 목록의 열람 및 선택\n" +
                        "2. 성명, 주소, 전화번호(또는 이동전화번호), 전자우편주소 등의 입력\n" +
                        "3. 약관내용, 청약철회가 불가능한 \"콘텐츠\"에 대해 \"회사\"가 취한 조치에 관련한 내용에 대한 확인\n" +
                        "4. 이 약관에 동의하고 위 제3호의 사항을 확인하거나 거부하는 표시(예, 마우스 클릭)\n" +
                        "5. \"콘텐츠\"의 이용신청에 관한 확인 또는 \"회사\"의 확인에 대한 동의" +
                        "6. 결제방법의 선택\n" +
                        "② \"회사\"는 \"이용자\"의 이용신청이 다음 각 호에 해당하는 경우에는 승낙하지 않거나 승낙을 유보할 수 있습니다.\n" +
                        "1. 실명이 아니거나 타인의 명의를 이용한 경우\n" +
                        "2. 허위의 정보를 기재하거나, \"회사\"가 제시하는 내용을 기재하지 않은 경우\n" +
                        "3. 미성년자가 청소년보호법에 의해서 이용이 금지되는 \"콘텐츠\"를 이용하고자 하는 경우\n" +
                        "4. 서비스 관련 설비의 여유가 없거나, 기술상 또는 업무상 문제가 있는 경우\n" +
                        "③ \"회사\"의 승낙이 제16조 제1항의 수신확인통지형태로 \"이용자\"에게 도달한 시점에 계약이 성립한 것으로 봅니다.\n" +
                        "④ \"회사\"의 승낙의 의사표시에는 \"이용자\"의 이용신청에 대한 확인 및 서비스제공 가능여부, 이용신청의 정정·취소 등에 관한 정보 등을 포함합니다.";

                dlg.setMessage(ruletxt2);
                dlg.setPositiveButton("확인", null);
                dlg.show();
            }
        });

        rule2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                dlg.setTitle("개인정보처리방침");

                String ruletxt1 = "제1조(목적) 이 약관은 회사가 온라인으로 제공하는 디지털콘텐츠(이하 \"콘텐츠\"라고 한다) 및 제반서비스의 이용과 관련하여 회사와 이용자와의 권리, 의무 및 책임사항 등을 규정함을 목적으로 합니다.\n" +
                        " 제2조(정의) 이 약관에서 사용하는 용어의 정의는 다음과 같습니다.\n" +
                        "1. \"회사\"라 함은 \"콘텐츠\" 산업과 관련된 경제활동을 영위하는 자로서 콘텐츠 및 제반서비스를 제공하는 자를 말합니다.\n" +
                        "2. \"이용자\"라 함은 \"회사\"의 사이트에 접속하여 이 약관에 따라 \"회사\"가 제공하는 \"콘텐츠\" 및 제반서비스를 이용하는 회원 및 비회원을 말합니다.\n" +
                        "3. \"회원\"이라 함은 \"회사\"와 이용계약을 체결하고 \"이용자\" 아이디(ID)를 부여받은 \"이용자\"로서 \"회사\"의 정보를 지속적으로 제공받으며 \"회사\"가 제공하는 서비스를 지속적으로 이용할 수 있는 자를 말합니다.\n" +
                        "4. \"비회원\"이라 함은 \"회원\"이 아니면서 \"회사\"가 제공하는 서비스를 이용하는 자를 말합니다.\n" +
                        "5. \"콘텐츠\"라 함은 정보통신망이용촉진 및 정보보호 등에 관한 법률 제2조 제1항 제1호의 규정에 의한 정보통신망에서 사용되는 부호·문자·음성·음향·이미지 또는 영상 등으로 표현된 자료 또는 정보로서, 그 보존 및 이용에 있어서 효용을 높일 수 있도록 전자적 형태로 제작 또는 처리된 것을 말합니다.\n" +
                        "6. \"아이디(ID)\"라 함은 \"회원\"의 식별과 서비스이용을 위하여 \"회원\"이 정하고 \"회사\"가 승인하는 문자 또는 숫자의 조합을 말합니다.\n" +
                        "7. \"비밀번호(PASSWORD)\"라 함은 \"회원\"이 부여받은 \"아이디\"와 일치되는 \"회원\"임을 확인하고 비밀보호를 위해 \"회원\" 자신이 정한 문자 또는 숫자의 조합을 말합니다.";

                dlg.setMessage(ruletxt1);
                dlg.setPositiveButton("확인", null);
                dlg.show();
            }
        });

        btn_idcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String c_name = etxt_Name.getText().toString();
                String c_id = etxt_ID.getText().toString();
                String c_pw = etxt_PW.getText().toString();
                String c_rpw = etxt_RPW.getText().toString();
                String c_phone = "010" + etxt_NumberF.getText().toString() + etxt_NumberB.getText().toString();
                String state = "";

                sql = "Select c_id,c_state From " + helper.Customer + " WHERE c_id = '" + c_id + "'";
                cursor = database.rawQuery(sql, null);

                while (cursor.moveToNext()) {
                    state = cursor.getString(1);
                }

                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                if (etxt_ID.getText().toString().equals("")) {
                    dlg.setTitle("아이디를 입력해주세요.");
                    dlg.setPositiveButton("확인", null);
                    dlg.show();
                } else if (cursor.getCount() != 0) {
                    dlg.setTitle("이미 사용중이거나 탈퇴한 아이디입니다.");
                    dlg.setPositiveButton("확인", null);
                    dlg.show();
                    etxt_ID.setText("");
                } else {
                    dlg.setTitle("현재 아이디는 사용가능합니다.");
                    dlg.setPositiveButton("확인", null);
                    dlg.show();
                    btn_idcheckon.setVisibility(View.VISIBLE);
                    btn_idcheck.setVisibility(View.INVISIBLE);
                    btn_idcheckon.setEnabled(false);
                    etxt_ID.setBackgroundColor(Color.LTGRAY);
                    etxt_ID.setEnabled(false);
                    check = true; //중복확인이 됬으면 true값 반환
                }

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cursor != null)
            cursor.close();
    }
}
