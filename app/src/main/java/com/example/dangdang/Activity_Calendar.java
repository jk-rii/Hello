package com.example.dangdang;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Activity_Calendar extends AppCompatActivity {
    CalendarView calV;
    EditText edtDiary;
    Button btnSave, btnDelete, prevBtnCalendar;
    String fileName;

    File path, file;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        String absolutPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        path = new File(absolutPath + "/diary");
        if (!path.exists()) path.mkdir();

        calV = findViewById(R.id.calV);
        edtDiary = findViewById(R.id.edtDiary);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        // 이전 버튼
        prevBtnCalendar = findViewById(R.id.prevBtnCalendar);
        prevBtnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CalendarIntent = new Intent(Activity_Calendar.this, SubActivity.class);
                startActivity(CalendarIntent);
            }
        });


        calV.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                fileName = Integer.toString(year) + Integer.toString(month + 1) + Integer.toString(dayOfMonth) + ".txt";
                file = new File(path, fileName);
                String str = readDiary(file);
                edtDiary.setText(str);
                btnSave.setEnabled(true);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file.delete();
                try {
                    FileOutputStream outfs = new FileOutputStream(file, true);
                    String str = edtDiary.getText().toString();
                    outfs.write(str.getBytes());
                    outfs.close();
                    Toast.makeText(getApplicationContext(), fileName + "이 저장됨", Toast.LENGTH_SHORT).show();
                    btnSave.setText("수정하기");
                    btnDelete.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "일기가 저장되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    edtDiary.setText("");
                    FileOutputStream outfs = new FileOutputStream(file, true);
                    file.delete();
                    outfs.close();
                    Toast.makeText(getApplicationContext(), fileName + "이 삭제됨", Toast.LENGTH_SHORT).show();
                    btnDelete.setVisibility(View.GONE);
                    btnSave.setText("저장하기");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "일기가 삭제되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    String readDiary(final File file) {
        String diaryStr = null;
        FileInputStream infs;
        try {
            infs = new FileInputStream(file);
            byte[] diary = new byte[infs.available()];
            infs.read(diary);
            infs.close();
            diaryStr = new String(diary);
            btnSave.setText("수정하기");
            btnDelete.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
            edtDiary.setHint("이벤트를 입력하세요");
            btnSave.setText("저장하기");
            btnDelete.setVisibility(View.GONE);
        }
        return diaryStr;
    }
}
