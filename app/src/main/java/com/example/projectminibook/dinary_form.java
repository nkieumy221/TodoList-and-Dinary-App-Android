package com.example.projectminibook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

public class dinary_form extends AppCompatActivity {
    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;
    Cursor cursor;

    TextView addDinary;
    ListView listDnr;
    ArrayList<DinaryModel> list;
    AdapterDinary adapterDinary;
    SharedPreferences preferences;
    String idu;

    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinary_form);
        getSupportActionBar().hide();
        preferences = getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        idu = preferences.getString("ID","");
        addControls();
        navigation();
        if(!idu.isEmpty()) {
            addControls();
            readData();
        }
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void navigation() {
        TextView cldNV = findViewById(R.id.cldNV);
        TextView todoNV = findViewById(R.id.todoNV);
        TextView dnrNV = findViewById(R.id.dnrNV);
        TextView usNV = findViewById(R.id.prNV);
        cldNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dinary_form.this, MainActivity.class);
                startActivity(i);
            }
        });
        todoNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dinary_form.this, ListTodo.class);
                startActivity(i);
            }
        });
        dnrNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dinary_form.this, dinary_form.class);
                startActivity(i);
            }
        });
        usNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dinary_form.this, UserInfor.class);
                startActivity(i);
            }
        });
    }

    private void addControls() {
        listDnr = findViewById(R.id.listDinary);
        list = new ArrayList<>();
        adapterDinary = new AdapterDinary(this,list);
        listDnr.setAdapter(adapterDinary);

        addDinary = findViewById(R.id.addDinary);
        addDinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dinary_form.this, PostDnr.class);
                startActivity(i);
            }
        });

    }
    private void readData() {

        database = Database.initDatabase(this, DATABASE_NAME);
        cursor = database.rawQuery("SELECT * FROM dinary WHERE ID_user = ? ORDER BY ID DESC",new String[]{idu +""});
        list.clear();
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int id = cursor.getInt(0);
            String content = cursor.getString(1);
            String time = cursor.getString(4);
            byte[] anh = cursor.getBlob(2);
            String feel = cursor.getString(6);
            int idu = cursor.getInt(5);
            String tt = cursor.getString(7);
            list.add(new DinaryModel(id,content,anh,time,feel,idu,tt));
        }
        adapterDinary.notifyDataSetChanged();
    }
}