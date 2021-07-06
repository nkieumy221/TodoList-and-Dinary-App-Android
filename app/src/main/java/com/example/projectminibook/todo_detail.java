package com.example.projectminibook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class todo_detail extends AppCompatActivity {

    TextView nameTodo, note, time, date, check, back;
    private int notificationId = 1;

    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;
    int id =-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        getSupportActionBar().hide();
        addControls();
        initUI();
    }

    private void addControls() {
        nameTodo = findViewById(R.id.tenCongviec);
        note = findViewById(R.id.ghiChu);
        time = findViewById(R.id.thoiGianNhac);
        date = findViewById(R.id.ngayThucHien);
        check = findViewById(R.id.tienDo);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListTodo.class);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        //lấy tt cập nhật
        Intent intent = getIntent();
        id = intent.getIntExtra("ID",-1);
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM todoList WHERE ID = ?",new String[]{id +""});
        cursor.moveToFirst();
        String ten = cursor.getString(1);
        String nd = cursor.getString(3);
        String ngay = cursor.getString(6);
        String thoigian = cursor.getString(4);
        int checked = cursor.getInt(2);
        if(checked == 0) {
            check.setText("Chưa hoàn thành");
        } else {
            check.setText("Đã hoàn thành");
        }
        nameTodo.setText(ten);
        note.setText(nd);
        time.setText(thoigian);
        date.setText(ngay);
    }

}