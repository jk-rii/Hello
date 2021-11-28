package com.example.dangdang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PopupActivity extends Activity {

    TextView distance, time, addr, rating, title, contents, age, species, gender, weight, zoom;
    ImageView popup_image;
    Button popupDelete, popupEdit;
    Intent intent;

    String b_title, c_idu, c_id, user;

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase db;
    Cursor cursor;

    public static Activity popupActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_popup);
        popupActivity = this;

        title = findViewById(R.id.popup_title);
        distance = findViewById(R.id.popup_distance);
        time = findViewById(R.id.popup_time);
        addr = findViewById(R.id.popup_gps);
        rating = findViewById(R.id.popup_rate);
        contents = findViewById(R.id.popup_contents);
        popup_image = findViewById(R.id.popup_image);
        age = findViewById(R.id.age);
        species = findViewById(R.id.species);
        gender = findViewById(R.id.gender);
        weight = findViewById(R.id.weight);
        zoom = findViewById(R.id.zoom);
        popupDelete = findViewById(R.id.popupDelete);
        popupEdit = findViewById(R.id.popupEdit);

       /* intent = getIntent();
        final int c_num = intent.getIntExtra("c_num",1);
        c_idi = intent.getStringExtra("c_id");*/

        c_idu = ((MainActivity) MainActivity.getId).id;

        popup_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zoomIntent = new Intent(PopupActivity.this, ZoomActivity.class);
                String path = zoom.getText().toString();
                //System.out.println("SQLite : "+ path);
                zoomIntent.putExtra("path", path);
                startActivity(zoomIntent);
            }
        });

        popupDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDelete();
            }
        });

        popupEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.equals(c_idu)) {
                    Intent EditIntent = new Intent(getApplicationContext(), ListEdit.class);
                    EditIntent.putExtra("b_title", b_title);
                    EditIntent.putExtra("b_contents", contents.getText().toString());
                    startActivity(EditIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "아이디가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        popupIntent();
    }

    private void popupDelete() {
        if (user.equals(c_idu)) {
            SQLiteDatabase db = openOrCreateDatabase("walk", Context.MODE_PRIVATE, null); // DB open
            String sql = "delete from Board where b_title = '" + b_title + "'";
            db.execSQL(sql);
            db.close();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "아이디가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupIntent() {
        Intent popupIntent = getIntent();
        int c_num = popupIntent.getIntExtra("c_num", 1);
        if (popupIntent.getSerializableExtra("c_num") != null) {
            c_id = popupIntent.getStringExtra("c_id");
            b_title = popupIntent.getStringExtra("title");
            user = popupIntent.getStringExtra("userID");
            DB_Board();
        }
    }

    private void DB_Board() {
        SQLiteDatabase db = openOrCreateDatabase("walk", Context.MODE_PRIVATE, null); // DB open
        String sql = "select b_title,b_contents,b_image,b_rating,b_addr,b_distance,b_time,b_age, b_species, b_gender, b_weight from Board where b_title = '" + b_title + "'";

        cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            title.setText(cursor.getString(0));
            contents.setText(cursor.getString(1));
            popup_image.setImageURI(Uri.parse(cursor.getString(2)));
            rating.setText(cursor.getString(3) + "점");
            addr.setText(cursor.getString(4));
            distance.setText(cursor.getString(5));
            time.setText(cursor.getString(6));
            age.setText(cursor.getString(7));
            species.setText(cursor.getString(8));
            gender.setText(cursor.getString(9));
            weight.setText(cursor.getString(10));
            zoom.setText(cursor.getString(2));
        }
        cursor.close();
        db.close();
    }

    public void mOnClose(View v) {
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);
        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cursor != null)
            cursor.close();
    }
}
