package com.example.dangdang;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IDPW_Found extends AppCompatActivity {

    LinearLayout layout_id, layout_pw;
    Button btn_idfind, btn_pwfind, prevBtnAccount;
    RadioGroup rg_idpw;
    RadioButton rb_id, rb_pw;
    EditText edt_id_name, edt_id_NumberF, edt_id_NumberB, edt_pw_id, edt_pw_name, edt_pw_NumberF, edt_pw_NumberB;
    DatabaseOpenHelper helper;
    SQLiteDatabase db;
    int version = 1;

    String sql;
    Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idpwfound);

        layout_id = findViewById(R.id.layout_id);
        layout_pw = findViewById(R.id.layout_pw);
        btn_idfind = findViewById(R.id.btn_idfind);
        btn_pwfind = findViewById(R.id.btn_pwfind);
        rb_id = findViewById(R.id.rb_id);
        rb_pw = findViewById(R.id.rb_pw);
        edt_id_name = findViewById(R.id.edt_id_name);
        edt_id_NumberF = findViewById(R.id.edt_id_NumberF);
        edt_id_NumberB = findViewById(R.id.edt_id_NumberB);
        edt_pw_id = findViewById(R.id.edt_pw_id);
        edt_pw_name = findViewById(R.id.edt_pw_name);
        edt_pw_NumberF = findViewById(R.id.edt_pw_NumberF);
        edt_pw_NumberB = findViewById(R.id.edt_pw_NumberB);
        rg_idpw = findViewById(R.id.rg_idpw);
        prevBtnAccount = findViewById(R.id.prevBtnAccount);

        helper = new DatabaseOpenHelper(IDPW_Found.this, DatabaseOpenHelper.db, null, version);
        db = helper.getWritableDatabase();
        prevBtnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foundIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(foundIntent);
            }
        });

        rg_idpw.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_id) {
                    layout_id.setVisibility(View.VISIBLE);
                    layout_pw.setVisibility(View.INVISIBLE);
                } else {
                    layout_id.setVisibility(View.INVISIBLE);
                    layout_pw.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_idfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());

                String c_name = edt_id_name.getText().toString();
                String c_phone = "010" + edt_id_NumberF.getText().toString() + edt_id_NumberB.getText().toString();

                String id = "";
                String phone = "";

                sql = "Select c_id,c_phone From " + helper.Customer + " WHERE c_phone ='" + c_phone + "' AND c_name='" + c_name + "' ";
                cursor = db.rawQuery(sql, null);

                while (cursor.moveToNext()) {
                    id = cursor.getString(0);
                    phone = cursor.getString(1);
                }

                if (cursor.getCount() == 0) {
                    Toast.makeText(IDPW_Found.this, "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    edt_id_name.setText("");
                    edt_id_NumberF.setText("");
                    edt_id_NumberB.setText("");
                    dlg.setTitle("아이디는 " + id + " 입니다.");
                    dlg.setPositiveButton("확인", null);
                    dlg.show();
                }

            }
        });

        btn_pwfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                String c_name = edt_pw_name.getText().toString();
                String c_phone = "010" + edt_pw_NumberF.getText().toString() + edt_pw_NumberB.getText().toString();
                String c_id = edt_pw_id.getText().toString();

                String id = "";
                String phone = "";

                sql = "Select c_id,c_phone From " + helper.Customer + " WHERE c_phone ='" + c_phone + "' AND c_id='" + c_id + "' ";
                cursor = db.rawQuery(sql, null);

                while (cursor.moveToNext()) {
                    id = cursor.getString(0);
                    phone = cursor.getString(1);
                }

                if (cursor.getCount() == 0) {
                    Toast.makeText(IDPW_Found.this, "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    final String id2 = id;

                    dlg.setTitle("비밀번호 변경");
                    dlg.setMessage("비밀번호 입력");
                    final EditText edt = new EditText(IDPW_Found.this);
                    dlg.setView(edt);

                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String pw_changed = edt.getText().toString();

                            Intent in = new Intent(getApplicationContext(), MainActivity.class);
                            db.execSQL(" UPDATE Customer SET c_pw = '" + pw_changed + "' WHERE c_id = '" + id2 + "' ");
                            Toast.makeText(IDPW_Found.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(in);
                        }
                    });
                    dlg.show();
                }
            }
        });


    }
}
