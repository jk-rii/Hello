package com.example.dangdang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    EditText etxtID, etxtPW;
    TextView txtAccount, txtFoundID;

    String id;

    Intent in;

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;
    String sql;
    Cursor cursor;

    public static Context getId;
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        getId = this;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        //초기화
        btnLogin = findViewById(R.id.btnLogin);
        etxtID = findViewById(R.id.etxtID);
        etxtPW = findViewById(R.id.etxtPW);
        txtAccount = findViewById(R.id.txtAccount);
        txtFoundID = findViewById(R.id.txtFoundID);

        helper = new DatabaseOpenHelper(MainActivity.this, DatabaseOpenHelper.db, null, version);
        database = helper.getWritableDatabase();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c_id = etxtID.getText().toString();
                String c_pw = etxtPW.getText().toString();

                if (c_id.length() == 0 || c_pw.length() == 0) {
                    //아이디와 비밀번호는 필수 입력사항입니다.
                    Toast.makeText(MainActivity.this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }

                sql = "Select c_id From " + helper.Customer + " WHERE c_id = '" + c_id + "'";
                cursor = database.rawQuery(sql, null);

                if (cursor.getCount() != 1) {
                    Toast.makeText(MainActivity.this, "존재하지않는 아이디입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                sql = "SELECT c_id,c_pw,c_num FROM " + helper.Customer + " WHERE c_id = '" + c_id + "'";
                cursor = database.rawQuery(sql, null);

                cursor.moveToNext();
                if (!c_pw.equals(cursor.getString(1))) {
                    Toast.makeText(MainActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "로그인성공", Toast.LENGTH_SHORT).show(); //로그인성공
                    Intent in = new Intent(getApplicationContext(), SubActivity.class);
                    in.putExtra("c_num", cursor.getInt(2));
                    in.putExtra("c_id", cursor.getString(0));
                    in.putExtra("c_pw", cursor.getString(1));
                    startActivity(in);
                    id = cursor.getString(0);
                    finish();
                }
                cursor.close();

            }
        });


        txtFoundID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), IDPW_Found.class);
                startActivity(in);
            }
        });

        txtAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                in = new Intent(getApplicationContext(), Account.class);
                startActivity(in);

            }
        });



    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    ((TMapAPI) TMapAPI.gps).setGps();

                } else {
                    Log.d("locationTest", "동의거부함");
                }
                return;
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cursor != null)
            cursor.close();
    }
}
