package com.example.projectminibook;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListTodo extends AppCompatActivity {
    TextView btnSum, btnDone, bntMiss;
    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;
    Cursor cursor;
    ListView listView;
    ArrayList<TodoList> list;
    AdapterTodoList adapter;
    SharedPreferences preferences;
    String idu;

    FloatingActionButton addBtn;

    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_todo);
        preferences = getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        idu = preferences.getString("ID","");
        addControls();
        navigation();
        if(!idu.isEmpty()) {
            readData();
            XuliDiem();
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
                Intent i = new Intent(ListTodo.this, MainActivity.class);
                  startActivity(i);
            }
        });
        todoNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListTodo.this, ListTodo.class);
                startActivity(i);
            }
        });
        dnrNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListTodo.this, dinary_form.class);
                startActivity(i);
            }
        });
        usNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListTodo.this, UserInfor.class);
                startActivity(i);
            }
        });
    }

    private void addControls() {
        listView = findViewById(R.id.Todolayout);
        addBtn = findViewById(R.id.fab);
        list = new ArrayList<>();
        adapter = new AdapterTodoList(this, list);
        listView.setAdapter(adapter);

        addBtn.setOnClickListener(v1 -> {
            Intent i = new Intent(this, AddTodo.class);
            int requestCode = 1; // Or some number you choose
            startActivityForResult(i, requestCode);
        });

        btnSum = findViewById(R.id.tong);
        btnDone = findViewById(R.id.done);
        bntMiss = findViewById(R.id.miss);
    }

    private void readData() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        database = Database.initDatabase(this, DATABASE_NAME);
        cursor = database.rawQuery("SELECT * FROM todoList WHERE Id_user = " +idu+ " AND Date = '"+formattedDate+"'",null);
        list.clear();

        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            String nd = cursor.getString(3);
            boolean check = getBoolean(2);
            int idu = cursor.getInt(5);
            String date = cursor.getString(6);
            String time = cursor.getString(4);
            list.add(new TodoList(id, ten, nd, date, check,  idu,time));
        }
        adapter.notifyDataSetChanged();
    }

    private void XuliDiem() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        database = Database.initDatabase(this, DATABASE_NAME);
        cursor = database.rawQuery("SELECT * FROM todoList WHERE Id_user = " +idu+ " AND Date = '"+formattedDate+"'",null);
        int d=0, m=0;
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int point = cursor.getInt(2);
            if(point == 1){
                d +=1;
            }else{
                m +=1;
            }
        }
        btnDone.setText(d+"");
        bntMiss.setText(m+"");
        int t =d+m;
        btnSum.setText(t+"");
        if(t==d && t!=0){
            new AlertDialog.Builder(ListTodo.this)
                    .setTitle("Thật tuyệt vời !!!")
                    .setMessage("Chúc mừng bạn đã hoàn thành hết công việc ngày hôm nay")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
        }
    }

    public boolean getBoolean(int columnIndex) {
        return !cursor.isNull(columnIndex) && cursor.getShort(columnIndex) != 0;
    }


}