package com.example.dangdang;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String db = "walk";

    public static final String Customer = "Customer";


    private static final String customer = "CREATE TABLE Customer (c_num integer primary key AutoIncrement,c_name varchar2(20),c_id varchar2(20),c_pw varchar2(20),c_phone varchar2(20),c_state varchar2(10))";
    private static final String dog = "CREATE TABLE Dog (d_num integer primary key AutoIncrement,c_num integer,d_name varchar2(20),d_gender varchar2(10), d_species varchar2(20), d_age varchar2(10), d_weight varchar2(10), d_character varchar2(20), d_imageUri varchar(20), c_id varchar(20),foreign key(c_num) REFERENCES Customer(c_num) ON DELETE CASCADE, foreign key(c_id) REFERENCES Customer(c_id) ON DELETE CASCADE)";
    public static final String board = "CREATE TABLE Board(b_num integer primary key AutoIncrement,b_title varchar2(20),b_contents blob,b_image varchar2(30),b_age varchar2(20),b_species varchar2(40),b_gender varchar2(20),b_weight varchar2(20),b_rating varchar(10),b_addr varchar(30),b_date varchar(30),b_distance varchar(20),b_time varchar(20),c_num integer,c_id varchar(20),foreign key(c_num) REFERENCES Customer(c_num) ON DELETE CASCADE,foreign key(c_id) REFERENCES Customer(c_id) ON DELETE CASCADE)";
    public static final String rank = "CREATE TABLE Rank(r_num integer primary key AutoIncrement,r_distance integer,r_start varchar(20),r_end varchar(20),location varchar(40),c_num integer,c_id varchar(20),foreign key(c_num) REFERENCES Customer(c_num) ON DELETE CASCADE, foreign key(c_id) REFERENCES Customer(c_id) ON DELETE CASCADE)";


    public DatabaseOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) { //생성자
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Foreign key 사용하기위해 적어야됨(기본적으로 OFF상태)
        if (!db.isReadOnly()) {
            //Enable foreign key constraints
            db.execSQL("PRAGMA foreign_key=ON;");
        }
        try {
            db.execSQL(customer);
            db.execSQL(dog);
            db.execSQL(board);
            db.execSQL(rank);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS  Customer");
        db.execSQL("DROP TABLE IF EXISTS  Dog");
        db.execSQL("DROP TABLE IF EXISTS  Board");
        db.execSQL("DROP TABLE IF EXISTS  Rank");
        onCreate(db);
    }

    public void insertUser(SQLiteDatabase db, String c_name, String c_id, String c_pw, String c_phone, String c_state) {
        db.beginTransaction();
        try {
            String sql = "INSERT INTO Customer(c_name,c_id,c_pw,c_phone,c_state)" + "values('" + c_name + "','" + c_id + "','" + c_pw + "','" + c_phone + "','" + c_state + "')";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void insertDog(SQLiteDatabase db, int c_num, String d_name, String d_gender, String d_species, String d_age, String d_weight, String d_character, String d_imageUri, String c_id) {
        db.beginTransaction();
        try {
            String sql = "INSERT INTO Dog(c_num,d_name,d_gender,d_species,d_age, d_weight, d_character, d_imageUri, c_id)" + "values('" + c_num + "','" + d_name + "','" + d_gender + "','" + d_species + "','" + d_age + "','" + d_weight + "','" + d_character + "','" + d_imageUri + "','" + c_id + "')";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void insertBoard(SQLiteDatabase db, String b_title, String b_contents, String b_image, String b_age, String b_species, String b_gender, String b_weight, String b_rating, String b_addr, String b_date, String b_distance, String b_time, int c_num, String c_id) {
        db.beginTransaction();
        try {
            String sql = "INSERT INTO Board(b_title,b_contents,b_image,b_age,b_species,b_gender,b_weight,b_rating,b_addr,b_date,b_distance,b_time,c_num,c_id)" + "values('" + b_title + "','" + b_contents + "','" + b_image + "','" + b_age + "','" + b_species + "','" + b_gender + "','" + b_weight + "','" + b_rating + "','" + b_addr + "','" + b_date + "','" + b_distance + "','" + b_time + "','" + c_num + "','" + c_id + "')";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void insertRank(SQLiteDatabase db, int r_distance, String r_start, String r_end, String location, int c_num, String c_id) {
        db.beginTransaction();
        try {
            String sql = "INSERT INTO Rank(r_distance,r_start,r_end,location,c_num,c_id)" + "values('" + r_distance + "','" + r_start + "','" + r_end+ "','" + location + "','" + c_num + "','" + c_id + "')";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


}
