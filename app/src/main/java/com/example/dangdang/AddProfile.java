package com.example.dangdang;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddProfile extends AppCompatActivity {

    Button btnAdd;
    ListView listV;
    Intent intent;
    TextView txt_Gender, txt_Species, txt_Age, txt_Weight, txt_Character, txt_Image;
    String c_id;

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;
    Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addprofile);

        btnAdd = findViewById(R.id.btnAdd);
        listV = findViewById(R.id.listV);

        //subActivity 인텐트

        txt_Gender = findViewById(R.id.txt_Gender);
        txt_Species = findViewById(R.id.txt_Species);
        txt_Age = findViewById(R.id.txt_Age);
        txt_Weight = findViewById(R.id.txt_Weight);
        txt_Character = findViewById(R.id.txt_Character);
        txt_Image = findViewById(R.id.txt_Image);


        intent = getIntent();
        final int c_num = intent.getIntExtra("c_num", 1);

        helper = new DatabaseOpenHelper(this, DatabaseOpenHelper.db, null, version);
        database = helper.getWritableDatabase();

        //이전 버튼
        Button prevBtnAdd = findViewById(R.id.prevBtnAdd);
        prevBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ProfileIntent = new Intent(AddProfile.this, SubActivity.class);
                startActivity(ProfileIntent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(getApplicationContext(), DogPlus.class);
                //add.putExtra("c_num", c_num);
                startActivity(add);
            }
        });

        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView txt_Name;
                txt_Name = view.findViewById(R.id.txt_Name);
                String name;
                name = txt_Name.getText().toString();

                Intent ProIntent = new Intent(getApplicationContext(), SubActivity.class);
                ProIntent.putExtra("name", name);
                ProIntent.putExtra("c_num", c_num);
                startActivity(ProIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        intent = getIntent();
        final int c_num = intent.getIntExtra("c_num", 1);

        c_id = ((MainActivity) MainActivity.getId).id;

        SQLiteDatabase db = openOrCreateDatabase("walk", Context.MODE_PRIVATE, null); // DB open
        //cursor = db.rawQuery("SELECT d_num as _id,d_name,d_gender,d_age,d_weight,d_species,d_character,d_imageUri FROM Dog where c_num = '" + c_num + "'", null); //쿼리불러와서 cursor에 저장
        cursor = db.rawQuery("SELECT d_num as _id,d_name,d_gender,d_age,d_weight,d_species,d_character,d_imageUri FROM Dog where c_id = '" + c_id + "'", null); //쿼리불러와서 cursor에 저장
        cursor.moveToNext(); //커서 처음으로 보내고
        final String[] from = new String[]{"d_name", "d_gender", "d_age", "d_weight", "d_species", "d_character", "d_imageUri"}; //가져올 DB의 필드 이름
        int[] to = new int[]{R.id.txt_Name, R.id.txt_Gender, R.id.txt_Age, R.id.txt_Weight, R.id.txt_Species, R.id.txt_Character, R.id.img_profile}; //각각 대응되는 xml의 TextView의 id
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(listV.getContext(), R.layout.addprofile_item, cursor, from, to);
        listV.setAdapter(adapter);
        db.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cursor != null)
            cursor.close();
    }

}

