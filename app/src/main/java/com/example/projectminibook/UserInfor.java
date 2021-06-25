package com.example.projectminibook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class UserInfor extends AppCompatActivity {
    ImageView avt;
    TextView name,snh,sdt, sthich, btnLogout, btnEditIF;

    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;
    Cursor cursor;
    SharedPreferences preferences;
    String id;

    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infor);
        preferences = getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        id = preferences.getString("ID","");
        navigation();
        if(!id.isEmpty()) {
            addControls();
            show();
            addEvent();

        }else{
            btnLogout = findViewById(R.id.dangxuat);
            btnLogout.setText("Đăng nhập");
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(UserInfor.this, DangNhap.class);
                    startActivity(i);
                }
            });
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
                Intent i = new Intent(UserInfor.this, MainActivity.class);
                startActivity(i);
            }
        });
        todoNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserInfor.this, ListTodo.class);
                startActivity(i);
            }
        });
        dnrNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserInfor.this, dinary_form.class);
                startActivity(i);
            }
        });
        usNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserInfor.this, UserInfor.class);
                startActivity(i);
            }
        });
    }

    private void addControls() {
        avt = findViewById(R.id.avtImg);
        name = findViewById(R.id.usnamTv);
        snh = findViewById(R.id.birthday);
        sdt = findViewById(R.id.tel);
        sthich = findViewById(R.id.hobb);
        btnLogout =findViewById(R.id.dangxuat);
        btnEditIF =findViewById(R.id.EditInf);
    }

    private void show() {
        database = Database.initDatabase(this, DATABASE_NAME);
        cursor = database.rawQuery("SELECT * FROM profile WHERE ID = ?",new String[]{id +""});
        cursor.moveToFirst();

        String ten = cursor.getString(1);
        String telephone = cursor.getString(2);
        String hb = cursor.getString(3);
        String bd =cursor.getString(4);
        byte[] anh = cursor.getBlob(5);
        Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0, anh.length);

        avt.setImageBitmap(bitmap);
        name.setText(ten);
        sthich.setText(hb);
        snh.setText(bd);
        sdt.setText(telephone);

    }

    private void addEvent() {
        btnEditIF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfor();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        SharedPreferences.Editor spreferencesEditor = preferences.edit();
        spreferencesEditor.remove("ID"); //we are removing prodId by key
        spreferencesEditor.commit();
        Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void editInfor() {
        Intent i = new Intent(UserInfor.this, updateUser.class);
        startActivity(i);
    }


}