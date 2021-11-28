package com.example.dangdang;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class SubActivity extends AppCompatActivity {

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;
    String sql;
    Cursor cursor;

    ImageView profile_Image;
    Button btn_Calendar, btn_GPS, btn_Chat, btn_Weather, btn_Edit, btnLogout,btn_Rank;
    Intent intent;

    //캘린더
    Date currentTime = Calendar.getInstance().getTime();
    SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
    //SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());

    String weekDay = weekdayFormat.format(currentTime);
    String day = dayFormat.format(currentTime);
    String setName;

    TextView WeekTxt, DayTxt, RandomTxt;
    TextView dog_Name, dog_Age, dog_Species, dog_Gender, dog_Weight, dog_Character, dog_Image, user_id;

    //디데이
    private TextView ddayText;
    private TextView resultText;
    private Button dateButton;

    private int tYear;           //오늘 연월일 변수
    private int tMonth;
    private int tDay;

    private int dYear = 1;        //디데이 연월일 변수
    private int dMonth = 1;
    private int dDay = 1;

    private long d;
    private long t;
    private long r;

    private int resultNumber = 0;
    static final int DATE_DIALOG_ID = 0;

    public String d_name, c_id, now_id;
    public static Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        mContext = this;

        user_id = findViewById(R.id.user_id);
        getUser();
        userIntent();

        //초기화
        profile_Image = findViewById(R.id.profile_Image);
        btn_Edit = findViewById(R.id.Btn_Edit);
        btn_Calendar = findViewById(R.id.btn_Calendar);
        btn_GPS = findViewById(R.id.btn_GPS);
        btn_Chat = findViewById(R.id.btn_Chat);
        btn_Weather = findViewById(R.id.btn_Weather);
        btnLogout = findViewById(R.id.btnLogout);

        //디데이 버튼
        ddayText = (TextView) findViewById(R.id.dday);
        resultText = (TextView) findViewById(R.id.result);
        dateButton = (Button) findViewById(R.id.dateButton);

        //사진 동그라미
        profile_Image.setBackground(new ShapeDrawable(new OvalShape()));
        profile_Image.setClipToOutline(true);

        //프로필
        dog_Name = findViewById(R.id.dog_Name);
        dog_Age = findViewById(R.id.dog_Age);
        dog_Species = findViewById(R.id.dog_Species);
        dog_Gender = findViewById(R.id.dog_Gender);
        dog_Weight = findViewById(R.id.dog_Weight);
        dog_Character = findViewById(R.id.dog_Character);
        dog_Image = findViewById(R.id.dog_Image);
        btn_Rank = findViewById(R.id.btn_rank);

        //intent 값 받아오기
        intent = getIntent();
        final int c_num = intent.getIntExtra("c_num",1);
        final String c_id = intent.getStringExtra("c_id");

        // 프로필 이미지 눌렀을때 동작코드
        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EditIntent = new Intent(getApplicationContext(), AddProfile.class);
                startActivity(EditIntent);
            }
        });


        // Calendar버튼 눌렀을때 이미지 코드
        btn_Calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CalendarIntent = new Intent(getApplicationContext(), Activity_Calendar.class);
                startActivity(CalendarIntent);
            }
        });

        WeekTxt = (TextView) findViewById(R.id.Weektxt);
        WeekTxt.setText(weekDay);
        DayTxt = (TextView) findViewById(R.id.Daytxt);
        DayTxt.setText(day);

        //GoogleMap 버튼 눌렀을때 이미지 코드
        btn_GPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GpsIntent = new Intent(getApplicationContext(), TMapAPI.class);
                GpsIntent.putExtra("c_num",c_num);
                GpsIntent.putExtra("c_id",c_id);
                startActivity(GpsIntent);
            }
        });

        btn_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ListIntent = new Intent(getApplicationContext(), Activity_List.class);
                ListIntent.putExtra("c_num",c_num);
                ListIntent.putExtra("c_id",c_id);
                startActivity(ListIntent);
            }
        });

        btn_Weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent WeatherIntent = new Intent(getApplicationContext(), Weather.class);
                WeatherIntent.putExtra("c_num",c_num);
                WeatherIntent.putExtra("c_id",c_id);
                startActivity(WeatherIntent);
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(0);//----------------
            }
        }); //디데이 end

        btn_Rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), Rank.class);
                startActivity(in);
            }
        });

        Random();
        dDay();
        getProfile();
        ProIntent();
        SharedPreferences();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_logout(v);
            }
        });

    }//oncreate end

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor userEditor = user.edit();
        userEditor.putString("c_id", user_id.getText().toString());
        userEditor.apply();

        SharedPreferences prefer = getSharedPreferences(c_id + "days", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefer.edit();
        editor.putString("dDay", ddayText.getText().toString());
        editor.putString("result", resultText.getText().toString());
        editor.apply(); // 마지막을 변경한 값으로 저장

        SharedPreferences profile = getSharedPreferences(c_id + "dogs", MODE_PRIVATE);
        SharedPreferences.Editor profile_editor = profile.edit();
        profile_editor.putString("d_name", dog_Name.getText().toString());
        profile_editor.putString("d_age", dog_Age.getText().toString());
        profile_editor.putString("d_species", dog_Species.getText().toString());
        profile_editor.putString("d_gender", dog_Gender.getText().toString());
        profile_editor.putString("d_weight", dog_Weight.getText().toString());
        profile_editor.putString("d_character", dog_Character.getText().toString());
        profile_editor.putString("d_image", dog_Image.getText().toString());
        profile_editor.apply();

        if (cursor != null)
            cursor.close();
    }

    private void SharedPreferences() {
        //마지막 값 저장 얻어오기
        SharedPreferences prefer = getSharedPreferences(c_id + "days", MODE_PRIVATE);
        if (prefer != null) {
            String d_day = prefer.getString("dDay", "");
            String result_day = prefer.getString("result", "");
            ddayText.setText(d_day);
            resultText.setText(result_day);
        }
    }

    private void getProfile() {
        SharedPreferences profile = getSharedPreferences(c_id + "dogs", MODE_PRIVATE);
        if (profile != null) {
            setName = profile.getString("d_name", "");
            String setAge = profile.getString("d_age", "");
            String setSpecies = profile.getString("d_species", "");
            String setGender = profile.getString("d_gender", "");
            String setWeight = profile.getString("d_weight", "");
            String setCharacter = profile.getString("d_character", "");
            String setImage = profile.getString("d_image", "");
            Uri setImageUri = Uri.parse(setImage);
            dog_Name.setText(setName);
            dog_Age.setText(setAge);
            dog_Species.setText(setSpecies);
            dog_Gender.setText(setGender);
            dog_Weight.setText(setWeight);
            dog_Character.setText(setCharacter);
            dog_Image.setText(setImage);
            profile_Image.setImageURI(setImageUri);
            d_name = setName;
            /*System.out.println("SQLite_save : " + setName);*/
        }
    }

    private void getUser() {
        SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
        String use = user.getString("c_id", "");
        user_id.setText(use);
        now_id = use;
    }

    private void ProIntent() {
        Intent ProIntent = getIntent();
        int c_num = ProIntent.getIntExtra("c_num", 1);
        d_name = ProIntent.getStringExtra("name");
        if (ProIntent.getSerializableExtra("c_num") != null) {
            DB_Dog();
        }
    }

    private void userIntent() {
        Intent in = getIntent();
        c_id = in.getStringExtra("c_id");
        if (in.getSerializableExtra("c_id") != null) {
            user_id.setText(c_id);
        } else {
            c_id = now_id;
            System.out.println("user now user** : " + now_id);
        }
    }

    private void DB_Dog() {
        SQLiteDatabase db = openOrCreateDatabase("walk", Context.MODE_PRIVATE, null); // DB open
        String sql = "select d_name,d_gender,d_age,d_weight,d_species,d_character,d_imageUri from Dog where d_name ='" + d_name + "'";

        cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            dog_Name.setText(cursor.getString(0));
            dog_Gender.setText(cursor.getString(1));
            dog_Age.setText(cursor.getString(2) + "살");
            dog_Weight.setText(cursor.getString(3) + "kg");
            dog_Species.setText(cursor.getString(4));
            dog_Character.setText(cursor.getString(5));
            profile_Image.setImageURI(Uri.parse(cursor.getString(6)));
            dog_Image.setText(cursor.getString(6));
        }
        cursor.close();
        db.close();
    }


    private void Random() {
        //랜덤 문자출력
        RandomTxt = (TextView) findViewById(R.id.randomTxt);
        String[] array = getResources().getStringArray(R.array.tips);

        Random rnd = new Random();
        //int i = rnd.nextInt(7);
        RandomTxt.setText(array[rnd.nextInt(7) + 1]);
    }

    private void dDay() {
        //한국어 설정 (ex: date picker)
        Locale.setDefault(Locale.KOREAN);

        Calendar today = Calendar.getInstance();              //현재 날짜 불러옴
        tYear = today.get(Calendar.YEAR);
        tMonth = today.get(Calendar.MONTH);
        tDay = today.get(Calendar.DAY_OF_MONTH);

        Calendar Dday = Calendar.getInstance();
        Dday.set(dYear, dMonth, dDay);

        t = today.getTimeInMillis();                 //오늘 날짜를 밀리타임으로 바꿈
        d = Dday.getTimeInMillis();              //디데이날짜를 밀리타임으로 바꿈
        r = (d - t) / (24 * 60 * 60 * 1000);                 //디데이 날짜에서 오늘 날짜를 뺀 값을 '일'단위로 바꿈

        resultNumber = (int) r + 1;
        updateDisplay();
    }

    //디데이 계산 함수
    private void updateDisplay() {
        ddayText.setText(String.format("%d년 %d월 %d일부터", dYear, dMonth + 1, dDay));
        if (resultNumber > 0) {
            resultText.setText(String.format("D-%d", resultNumber));
        } else if (resultNumber == 0) {
            resultText.setText("D-Day");
        } else {
            int absR = Math.abs(resultNumber - 1);
            resultText.setText(String.format("%d일째", absR));
        }

    }//디데이 날짜가 오늘날짜보다 뒤에오면 '-', 앞에오면 '+'를 붙인다

    private DatePickerDialog.OnDateSetListener dDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            dYear = year;
            dMonth = monthOfYear;
            dDay = dayOfMonth;
            final Calendar dCalendar = Calendar.getInstance();
            dCalendar.set(dYear, dMonth, dDay);

            d = dCalendar.getTimeInMillis();
            r = (d - t) / (24 * 60 * 60 * 1000);

            resultNumber = (int) r;
            updateDisplay();
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG_ID) {
            return new DatePickerDialog(this, dDateSetListener, tYear, tMonth, tDay);
        }
        return null;
    }

    //글쓰기에 정보 주기
    public void addInfo() {
        d_name = setName;
    }

    //로그아웃
    public void btn_logout(View v) {
        new AlertDialog.Builder(this)
                .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(SubActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }


}
