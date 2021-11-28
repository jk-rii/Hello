package com.example.dangdang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.widget.RatingBar;
import android.widget.TextView;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;


public class WriteActivity extends AppCompatActivity {

    Uri uri;

    String b_title, b_contents, b_species, b_age, b_gender, b_weight, b_addr, b_date, b_distance, b_time, b_image, b_ratings, c_id;
    String d_name;

    private static final String DB_TABLE = "Board";


    private Button submit;
    private TextView star, etxt_distance, etxt_time, etxt_speices, etxt_age, etxt_gender, etxt_weight;
    private EditText etxt_title, etxt_contents, etxt_addr;
    private String str;
    ImageView etxt_image;
    RatingBar rating;
    Intent intent;
    TextView plus_content;

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;
    String sql;
    Cursor cursor;

    private final int GET_GALLERY_IMAGE = 200;
    String file_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_write);

        etxt_image = findViewById(R.id.etxt_image);
        submit = findViewById(R.id.submit);
        rating = findViewById(R.id.rating);
        etxt_title = findViewById(R.id.etxt_title);
        etxt_contents = findViewById(R.id.etxt_contents);
        etxt_time = findViewById(R.id.etxt_time);
        etxt_distance = findViewById(R.id.etxt_distance);
        etxt_addr = findViewById(R.id.etxt_addr);
        etxt_age = findViewById(R.id.etxt_age);
        etxt_speices = findViewById(R.id.etxt_speices);
        etxt_gender = findViewById(R.id.etxt_gender);
        etxt_weight = findViewById(R.id.etxt_weight);
        plus_content = findViewById(R.id.plus_content);

        star = findViewById(R.id.star);

        intent = getIntent();
        final int c_num = intent.getIntExtra("c_num", 1);
        //final String c_id = intent.getStringExtra("c_id");

        helper = new DatabaseOpenHelper(WriteActivity.this, DatabaseOpenHelper.db, null, version);
        database = helper.getWritableDatabase();

        //이전 버튼
        Button prevBtnWrite = findViewById(R.id.prevBtnWrite);
        prevBtnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent writeIntent = new Intent(WriteActivity.this, Activity_List.class);
                startActivity(writeIntent);
            }
        });

        //현재시간 가져오기
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyy-MM-dd  HH:mm");
        final String b_date = simpleDate.format(mDate);

        // 별점
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                star.setText(String.valueOf(rating));
                b_ratings = star.getText().toString();
            }
        });

        etxt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        // 추가정보 입력
        plus_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_Info();
            }
        });

        ShareIntent();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                b_title = etxt_title.getText().toString();
                b_contents = etxt_contents.getText().toString();
                b_addr = etxt_addr.getText().toString();
                b_species = etxt_speices.getText().toString();
                b_age = etxt_age.getText().toString();
                b_distance = etxt_distance.getText().toString();
                b_time = etxt_time.getText().toString();
                b_image = file_path;

                b_gender = etxt_gender.getText().toString();
                b_weight = etxt_weight.getText().toString();

                c_id = ((MainActivity) MainActivity.getId).id;

                Toast.makeText(getApplicationContext(), c_id, Toast.LENGTH_SHORT).show();

                helper = new DatabaseOpenHelper(WriteActivity.this, DatabaseOpenHelper.db, null, version);
                database = helper.getWritableDatabase();
                helper.insertBoard(database, b_title, b_contents, b_image, b_age, b_species, b_gender, b_weight, b_ratings, b_addr, b_date, b_distance, b_time, c_num, c_id);

                Intent BoardIntent = new Intent(WriteActivity.this, Activity_List.class);
                BoardIntent.putExtra("c_num", c_num);
                BoardIntent.putExtra("c_id", c_id);
                startActivity(BoardIntent);

                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void ShareIntent() {
        Intent sharingIntent = getIntent();
        if (sharingIntent.getSerializableExtra("Img") != null) {
            String Img = sharingIntent.getStringExtra("Img");
            String distance = sharingIntent.getStringExtra("distance");
            String time = sharingIntent.getStringExtra("time");
            String address = sharingIntent.getStringExtra("address");
            Uri imgUri = Uri.parse(Img);
            file_path = Img;
            UriToBitmap(imgUri);
            etxt_image.setImageURI(imgUri);
            etxt_distance.setText(distance);
            etxt_time.setText(time);
            etxt_addr.setText(address);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            etxt_image.setImageURI(selectedImageUri);
            file_path = uri_path(selectedImageUri); //content 주소를 file주소로 변경
        }
    }// 갤러리

    private String uri_path(Uri uri) {
        String res = null;
        String[] image_data = {MediaStore.Images.Media.DATA};
        Cursor cur = getContentResolver().query(uri, image_data, null, null, null);
        if (cur.moveToFirst()) {
            int col = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cur.getString(col);
        }
        cur.close();
        return res;
    }

    private void UriToBitmap(Uri imgUri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            etxt_image.setImageBitmap(bitmap);
            //System.out.println("SQLite_비트맵 이미지 확인 : " + bitmap);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void add_Info() {
        String getName = ((SubActivity) SubActivity.mContext).setName;
        /*System.out.println("SQLite -------:: " + getName);*/
        d_name = getName;
        DB_Dog();
    }

    private void DB_Dog() {
        SQLiteDatabase db = openOrCreateDatabase("walk", Context.MODE_PRIVATE, null); // DB open
        String sql = "select d_name,d_gender,d_age,d_weight,d_species from Dog where d_name ='" + d_name + "'";
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            etxt_gender.setText(cursor.getString(1));
            etxt_age.setText(cursor.getString(2) + "살");
            etxt_weight.setText(cursor.getString(3) + "kg");
            etxt_speices.setText(cursor.getString(4));
        }
        cursor.close();
        db.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cursor != null)
            cursor.close();
    }

}
