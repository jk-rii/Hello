package com.example.dangdang;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DogPlus extends AppCompatActivity {

    EditText edtName, edtSpecies, edtAge, edtWeight, edtCharacter;
    RadioGroup rgGender;
    RadioButton rdoMan, rdoWoman;
    Button btnAdd, prevBtnPlus;
    String d_name, d_speices, d_age, d_weight, d_character, d_gender, d_imageUri, c_id;
    Intent intent;

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;
    Cursor cursor;

    ImageView edtImage;

    private final int GET_GALLERY_IMAGE = 200;
    String file_path;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogplus);

        edtName = findViewById(R.id.edtName);
        edtSpecies = findViewById(R.id.edtSpecies);
        edtAge = findViewById(R.id.edtAge);
        edtWeight = findViewById(R.id.edtWeight);
        edtCharacter = findViewById(R.id.edtCharacter);
        rgGender = findViewById(R.id.rgGender);
        rdoMan = findViewById(R.id.rdoMan);
        rdoWoman = findViewById(R.id.rdoWoman);
        btnAdd = findViewById(R.id.btnAdd);

        prevBtnPlus = findViewById(R.id.prevBtnPlus);
        prevBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DogPlus.this, AddProfile.class);
                startActivity(intent);
            }
        });


        edtImage = findViewById(R.id.edtImage);


        helper = new DatabaseOpenHelper(DogPlus.this, DatabaseOpenHelper.db, null, version);
        database = helper.getWritableDatabase();

        intent = getIntent();
        final int c_num = intent.getIntExtra("c_num", 1);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent id = getIntent();
                //c_id = id.getStringExtra("c_id");
                c_id  = ((MainActivity) MainActivity.getId).id;
                System.out.println("과연 아이디를 가져왔을까까요?"+c_id);

                d_name = edtName.getText().toString();
                d_speices = edtSpecies.getText().toString();
                d_age = edtAge.getText().toString();
                d_weight = edtWeight.getText().toString();
                d_character = edtCharacter.getText().toString();
                d_imageUri = file_path;

                if (rgGender.getCheckedRadioButtonId() == R.id.rdoMan) {
                    d_gender = "남자";
                } else {
                    d_gender = "여자";
                }
                helper.insertDog(database, c_num, d_name, d_gender, d_speices, d_age, d_weight, d_character, d_imageUri, c_id);
                finish();

            }
        });

        edtImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            edtImage.setImageURI(selectedImageUri);
            file_path = uri_path(selectedImageUri); //content 주소를 file주소로 변경
            //System.out.println("선택한 이미지 주소 content : " + selectedImageUri);
            System.out.println("선택한 이미지 주소 file : " + file_path);
        }
    }

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

    @Override
    protected void onStop() {
        super.onStop();
        if (cursor != null)
            cursor.close();
    }
}
