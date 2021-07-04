package com.example.projectminibook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class listTodoDay extends AppCompatActivity {
    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;
    Cursor cursor;
    EditText addCv;
    ListView listView;
    TextView dayChoose;
    ArrayList<TodoList> list;
    AdapterTodoList adapter;
    SharedPreferences preferences;
    String idu;
    ImageButton btnAdd;
    String data;

    FloatingActionButton addbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_todo_day);
        getSupportActionBar().hide();
        preferences = getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        idu = preferences.getString("ID","");
        addControls();
        data = getIntent().getExtras().getString("date");
        dayChoose.setText(data);
        if(!idu.isEmpty()) {
            database = Database.initDatabase(this, DATABASE_NAME);
            cursor = database.rawQuery("SELECT * FROM todoList WHERE Id_user = " +idu+ " AND Date = '"+data+"'",null);
            readData();
            addEvent();}
    }

    private void addControls() {
        addCv = findViewById(R.id.btnAddCv);
        listView = (ListView) findViewById(R.id.Todolayout);
        dayChoose = findViewById(R.id.time);
        btnAdd = findViewById(R.id.btnAddate);
        list = new ArrayList<>();
        adapter = new AdapterTodoList(this, list);
        listView.setAdapter(adapter);
    }

    private void addEvent() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTodo();
            }
        });

    }
    public void addTodo(){
        String ten = addCv.getText().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TenNote", ten);
        contentValues.put("Date",data);
        contentValues.put("Id_user", idu);
        database.insert(" todoList ",null, contentValues );
        finish();
        startActivity(getIntent());
    }



    private void readData() {

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            String nd = cursor.getString(3);
            String time = cursor.getString(4);
            boolean check = getBoolean(2);
            int idus = cursor.getInt(5);
            String date = cursor.getString(6);
            list.add(new TodoList(id, ten, nd, date, check,  idus, time));
        }
        adapter.notifyDataSetChanged();
    }


    public boolean getBoolean(int columnIndex) {
        if (cursor.isNull(columnIndex) || cursor.getShort(columnIndex) == 0) {
            return false;
        } else {
            return true;
        }
    }
}