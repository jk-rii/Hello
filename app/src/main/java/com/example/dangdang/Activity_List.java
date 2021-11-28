package com.example.dangdang;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Activity_List extends AppCompatActivity {

    Button prevBtnTalk, btn_Write;
    EditText etxt_search;
    ListView listV;
    Intent intent;
    String date, title;
    TextView datetime, b_title, userID;
    Spinner search;

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase db;
    Cursor cursor;

    boolean focus = false;
    String changeText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        prevBtnTalk = findViewById(R.id.prevBtnTalk);
        listV = findViewById(R.id.listV);
        etxt_search = findViewById(R.id.etxt_search);
        btn_Write = findViewById(R.id.btn_Write);

        String[] b_search = { "지역", "제목", "아이디", "거리"};

        search = (Spinner) findViewById(R.id.search);

        ArrayAdapter search_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, b_search);
        search.setAdapter(search_adapter);

        registerForContextMenu(listV);

        intent = getIntent();
        final int c_num = intent.getIntExtra("c_num", 1);
        final String c_id = intent.getStringExtra("c_id");

        helper = new DatabaseOpenHelper(this, DatabaseOpenHelper.db, null, version);
        db = helper.getWritableDatabase();

        //이전버튼
        prevBtnTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(Activity_List.this, SubActivity.class);
                startActivity(listIntent);
            }
        });

        //검색 버튼
        search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //글쓰기 버튼
        btn_Write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent WriteIntent = new Intent(getApplicationContext(), WriteActivity.class);
                WriteIntent.putExtra("c_num", c_num);
                WriteIntent.putExtra("c_id", c_id);
                startActivity(WriteIntent);
            }
        });


        etxt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                focus = false;
                changeText = "";
                onResume(); // 검색했을때 리스트뷰 갱신 갱신
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                focus = true;
                changeText = etxt_search.getText().toString();
                onResume();
            }

            @Override
            public void afterTextChanged(Editable s) {
                focus = true;
                changeText = etxt_search.getText().toString();
                onResume();
            }
        });

        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                datetime = view.findViewById(R.id.datetime);
                b_title = view.findViewById(R.id.title);
                date = datetime.getText().toString();
                title = b_title.getText().toString();
                userID = view.findViewById(R.id.userID);
                String user = userID.getText().toString();
                Intent popupIntent = new Intent(getApplicationContext(), PopupActivity.class);
                popupIntent.putExtra("c_num", c_num);
                popupIntent.putExtra("c_id", c_id);
                popupIntent.putExtra("userID", user);
                System.out.println("1234567890 : " + user);
                //intent.putExtra("date", date);
                popupIntent.putExtra("title", title);
                startActivity(popupIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        intent = getIntent();
        final int c_num = intent.getIntExtra("c_num", 1);
        SQLiteDatabase db = openOrCreateDatabase("walk", Context.MODE_PRIVATE, null); // DB open
        if (focus == false) {
            cursor = db.rawQuery("SELECT b_num as _id,b_title,b_date,b_distance,c_id FROM Board", null); //쿼리불러와서 cursor에 저장
        } else if (search.getSelectedItem().toString() == "제목") {
            cursor = db.rawQuery("SELECT b_num as _id,b_title,b_date,b_distance,c_id FROM Board where b_title  like '%" + changeText + "%'", null);
        } else if (search.getSelectedItem().toString() == "지역") {
            cursor = db.rawQuery("SELECT b_num as _id,b_title,b_date,b_distance,c_id FROM Board where b_addr  like '%" + changeText + "%'", null);
        } else if (search.getSelectedItem().toString() == "아이디") {
            cursor = db.rawQuery("SELECT b_num as _id,b_title,b_date,b_distance,c_id FROM Board where c_id  like '%" + changeText + "%'", null);
        } else if (search.getSelectedItem().toString() == "거리") {
            cursor = db.rawQuery("SELECT b_num as _id,b_title,b_date,b_distance,c_id FROM Board where b_distance  like '%" + changeText + "%'", null);
        }
        cursor.moveToNext(); //커서 처음으로 보내고
        String[] from = new String[]{"b_title", "c_id", "b_date", "b_distance"}; //가져올 DB의 필드 이름
        int[] to = new int[]{R.id.title, R.id.userID, R.id.datetime, R.id.distance}; //각각 대응되는 xml의 TextView의 id
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(listV.getContext(), R.layout.list_item, cursor, from, to);
        listV.setAdapter(adapter);
        db.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cursor != null)
            cursor.close();
        db.close();
    }

}
