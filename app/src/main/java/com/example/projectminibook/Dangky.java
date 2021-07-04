package com.example.projectminibook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Dangky extends AppCompatActivity {
    LinearLayout manhinh;
    EditText edtUs,edtPass,edtPass2;
    Button dangki;
    TextView dangnhap;

    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);
        getSupportActionBar().hide();
        addControls();
        addEvents();
    }
    private void addControls() {
        edtUs = findViewById(R.id.usernameEdt);
        edtPass = findViewById(R.id.password);
        edtPass2 = findViewById(R.id.password2);
        dangnhap = findViewById(R.id.backtoDN);
        dangki = findViewById(R.id.btnDangki);
    }


    private void addEvents() {
        database = Database.initDatabase(this, DATABASE_NAME);
        dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(Dangky.this ,DangNhap.class);

                startActivity(i);

            }
        });
        dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUs.getText().toString();
                String password = edtPass.getText().toString();
                String password2 = edtPass2.getText().toString();

                ContentValues contentValues = new ContentValues();
                contentValues.put("username", username);
                contentValues.put("password", password);
                cursor = database.rawQuery("SELECT * FROM account where username = " +username, null);
                if(cursor.getCount()>0)
                {
                    new AlertDialog.Builder(Dangky.this)
                            .setTitle("Title")
                            .setMessage("Tên tài khoản đã tồn tại")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }).show();
                }else{
                    if (username.length() > 0 && password.length() > 0) {
                        if(password.equals(password2)){
                            database = Database.initDatabase(Dangky.this,DATABASE_NAME);
                            database.insert(" account ",null, contentValues );
                            Intent intent = new Intent( Dangky.this, DangNhap.class);
                            Toast.makeText(Dangky.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }else{
                            new AlertDialog.Builder(Dangky.this)
                                    .setTitle("Title")
                                    .setMessage("Mật khẩu không trùng khớp")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }).show();
                        }
                    }else{
                        new AlertDialog.Builder(Dangky.this)
                                .setTitle("Title")
                                .setMessage("Không được để trống")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                }
            }
        });
    }
}