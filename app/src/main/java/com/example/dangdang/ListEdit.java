package com.example.dangdang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class ListEdit extends Activity {

    EditText mod_title, mod_contents;
    Button mod_edit;
    String b_title, b_contents, e_title, e_contents;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_edit);

        mod_title = findViewById(R.id.mod_title);
        mod_contents = findViewById(R.id.mod_contents);
        mod_edit = findViewById(R.id.mod_edit);

        Intent editIntent = getIntent();
        b_title = editIntent.getStringExtra("b_title");
        b_contents = editIntent.getStringExtra("b_contents");

        mod_title.setText(b_title);
        mod_contents.setText(b_contents);


        mod_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupEdit();
               // ((PopupActivity) PopupActivity.popupActivity).onResume();
                finish();
               ((PopupActivity) PopupActivity.popupActivity).finish();


            }
        });
    }
    private void popupEdit() {
        e_title = mod_title.getText().toString();
        e_contents = mod_contents.getText().toString();
        System.out.println("SQLite eeeeeeeeeeeeeee : " + e_title);
        SQLiteDatabase db = openOrCreateDatabase("walk", Context.MODE_PRIVATE, null); // DB open
        String sql = "update Board set b_title = '" + e_title + "', b_contents = '" + e_contents + "'where b_title = '" + b_title + "'";
        db.execSQL(sql);
        db.close();
    }
}
