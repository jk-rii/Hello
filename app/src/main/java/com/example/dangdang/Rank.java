package com.example.dangdang;

import android.content.Context;
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

import org.w3c.dom.Text;

public class Rank extends AppCompatActivity {

    Button btn_Count, btn_Distance;
    ListView listV1,listV2;
    TextView txt_b;

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase db;
    Cursor cursor;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        btn_Count = findViewById(R.id.btn_Count);
        btn_Distance = findViewById(R.id.btn_Distance);
        listV1 = findViewById(R.id.listV1);
        listV2 = findViewById(R.id.listV2);
        txt_b = findViewById(R.id.txt_b);

        btn_Count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listV2.setVisibility(View.INVISIBLE);
                listV1.setVisibility(View.VISIBLE);

                txt_b.setText("횟수");


                int[] img = {R.drawable.rank1,R.drawable.rank2,R.drawable.rank3,R.drawable.rank4,R.drawable.rank5,R.drawable.rank6,R.drawable.rank7,R.drawable.rank8,R.drawable.rank9,R.drawable.rank10};
                SQLiteDatabase db = openOrCreateDatabase("walk", Context.MODE_PRIVATE,null); // DB open
                cursor = db.rawQuery("SELECT r_num as _id,c_id,COUNT(*) as count FROM Rank group by c_id ORDER BY COUNT(*) DESC " ,null); //쿼리불러와서 cursor에 저장
                cursor.moveToNext(); //커서 처음으로 보내고
                String[] from = new String[]{"c_id","count"}; //가져올 DB의 필드 이름
                int[] to = new int[]{R.id.txt_id,R.id.txt_a}; //각각 대응되는 xml의 TextView의 id
                final SimpleCursorAdapter adapter = new SimpleCursorAdapter(listV1.getContext(), R.layout.list_rank, cursor, from, to);
                listV1.setAdapter(adapter);
                db.close();
            }
        });

        btn_Distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listV2.setVisibility(View.VISIBLE);
                listV1.setVisibility(View.INVISIBLE);

                txt_b.setText("거리");

                SQLiteDatabase db = openOrCreateDatabase("walk", Context.MODE_PRIVATE,null); // DB open
                cursor = db.rawQuery("SELECT r_num as _id,c_id,SUM(r_distance) as sum FROM Rank group by c_id ORDER BY SUM(r_distance) DESC" ,null); //쿼리불러와서 cursor에 저장
                cursor.moveToNext(); //커서 처음으로 보내고
                String[] from = new String[]{"c_id","sum"}; //가져올 DB의 필드 이름
                int[] to = new int[]{R.id.txt_id}; //각각 대응되는 xml의 TextView의 id
                final SimpleCursorAdapter adapter = new SimpleCursorAdapter(listV2.getContext(), R.layout.list_rank, cursor, from, to);
                listV2.setAdapter(adapter);
                db.close();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
