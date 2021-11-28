package com.example.dangdang;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddProfile_ContactAdapter extends CursorAdapter {
    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase db;
    Cursor cursor;
    String[] str;
    int[] ints;

    ImageView img_profile, profile_Image;
    TextView txt_Image;


    public AddProfile_ContactAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View v = View.inflate(context, R.layout.addprofile_item, null);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        /*helper = new DatabaseOpenHelper(context,DatabaseOpenHelper.db,null,version);
        db = helper.getWritableDatabase();*/

        img_profile = (ImageView) view.findViewById(R.id.img_profile);
        profile_Image = (ImageView) view.findViewById(R.id.profile_Image);
        txt_Image = (TextView) view.findViewById(R.id.txt_Image);
        String imagePath = txt_Image.getText().toString();
        Uri imageUri = Uri.parse(imagePath);
        img_profile.setImageURI(imageUri);
        profile_Image.setImageURI(imageUri);
    }
}
