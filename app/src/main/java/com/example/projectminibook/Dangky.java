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
                String pass2 = edtPass2.getText().toString();

                ContentValues contentValues = new ContentValues();
                contentValues.put("username", username);
                contentValues.put("password", password);
                cursor = database.rawQuery("SELECT * FROM account WHERE username = '" +username+ "'", null);
                if(validate() == 4 ){

                    if(cursor.getCount()>0) {
                        Toast.makeText(Dangky.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                    }else{
                        database = Database.initDatabase(Dangky.this,DATABASE_NAME);
                        database.insert(" account ",null, contentValues );
                        Intent intent = new Intent( Dangky.this, DangNhap.class);
                        Toast.makeText(Dangky.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }

            }
        });
    }

    //This method is used to validate input given by user
    public int validate() {
        boolean valid = false;
        int i = 0;

        //Get values from EditText fields
        String username = edtUs.getText().toString();
        String password = edtPass.getText().toString();
        String password2 = edtPass2.getText().toString();

        //Handling validation for UserName field
        if (username.isEmpty()) {
            valid = false;
            Toast.makeText(this, "Please enter valid username!", Toast.LENGTH_SHORT).show();
        } else {
            if (username.length() > 5) {
                valid = true;
                i += 1;
            } else {
                valid = false;
                Toast.makeText(this, "Username is to short!", Toast.LENGTH_SHORT).show();
            }
        }

        //Handling validation for Password field
         if (password.isEmpty() ) {
            valid = false;
            Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
        } else {
            if (password.length() > 5) {
                valid = true;
                i += 1;
            } else {
                valid = false;
                Toast.makeText(this, "Pass quá ngắn", Toast.LENGTH_SHORT).show();
            }
        }

        if (password2.isEmpty() ) {
            valid = false;
            Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
        } else {
            if (password.length() > 5) {
                valid = true;
                i += 1;
            } else {
                valid = false;
                Toast.makeText(this, "Pass quá ngắn", Toast.LENGTH_SHORT).show();
            }
        }

        if(password.equals(password2)) {
            valid = true;
            i += 1;
        }else {
            valid = false;
            Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
        }


        return i;
    }
}