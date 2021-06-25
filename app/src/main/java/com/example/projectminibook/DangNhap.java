package com.example.projectminibook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DangNhap extends AppCompatActivity {
    EditText edtUs, edtPass;
    Button dangnhap, dangki;
    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;
    Cursor cursor;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("userdetails",  Context.MODE_PRIVATE);
        setContentView(R.layout.activity_dang_nhap);

        addControls();
        addEvents();

    }

    private void addEvents() {
        database = Database.initDatabase(this, DATABASE_NAME);
        dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DangNhap.this , Dangky.class);
                startActivity(i);

            }
        });

        dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUs.getText().toString();
                String password = edtPass.getText().toString();

                cursor = database.rawQuery("SELECT * FROM account",null);
                while (cursor.moveToNext()){
                    String dataTen = cursor.getString(1);
                    String dataPass = cursor.getString(2);

                    if(dataTen.equals(username) && dataPass.equals(password)){
                        int id = cursor.getInt(0);
                        String tenTk = cursor.getString(1);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("ID", id+"");
                        editor.putString("username", tenTk);
                        editor.apply();
                        Intent i = new Intent(DangNhap.this, MainActivity.class);
                        startActivity(i);
                    }
                }
                cursor.moveToFirst();
                cursor.close();
            }
        });
    }

    private void addControls() {
        edtUs = findViewById(R.id.usernameEdt);
        edtPass = findViewById(R.id.password);
        dangnhap = findViewById(R.id.btnDangnhap);
        dangki = findViewById(R.id.btnDangki);
    }
}