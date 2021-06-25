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
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView btnSum, btnDone, bntMiss, intTD, intDNR;
    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;
    Cursor cursor;
    com.applandeo.materialcalendarview.CalendarView calendarView;
    SharedPreferences preferences;
    String idu;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        idu = preferences.getString("ID","");
        addControls();
        navigation();
        if(!idu.isEmpty()) {
            XuliDiem();
            addEvent();
            try {
                CheckDay();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void navigation() {
        TextView cldNV = findViewById(R.id.cldNV);
        TextView todoNV = findViewById(R.id.todoNV);
        TextView dnrNV = findViewById(R.id.dnrNV);
        TextView usNV = findViewById(R.id.prNV);
//        cldNV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Calendar.this, Calendar.class);
//                  startActivity(i);
//            }
//        });
        todoNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListTodo.class);
                startActivity(i);
            }
        });
        dnrNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, dinary_form.class);
                startActivity(i);
            }
        });
        usNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserInfor.class);
                startActivity(i);
            }
        });
    }


    private void addControls() {
        btnSum = findViewById(R.id.tong);
        btnDone = findViewById(R.id.done);
        bntMiss = findViewById(R.id.miss);
        intTD = findViewById(R.id.intTodo);
        intDNR = findViewById(R.id.intDnr);
        calendarView = findViewById(R.id.calendarView);
    }

    private void XuliDiem() {
        Calendar c = Calendar.getInstance();
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
    }

    private void addEvent() {
        intTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddTodo.class);
                startActivity(i);
            }
        });
        intDNR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PostDnr.class);
                startActivity(i);
            }
        });
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NotNull EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                Intent i = new Intent(MainActivity.this, listTodoDay.class);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setCalendar(clickedDayCalendar);
                String selectedDates = sdf.format(clickedDayCalendar.getTime());
                i.putExtra("date", selectedDates);
                startActivity(i);
            }
        });

    }

    public void CheckDay() throws ParseException {
        database = Database.initDatabase(this, DATABASE_NAME);
        cursor = database.rawQuery("SELECT * FROM todoList WHERE Id_user = " +idu,null);
        List<EventDay> events = new ArrayList<>();

        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            String date = cursor.getString(6);

            Cursor s = database.rawQuery("SELECT * FROM todoList WHERE Id_user = " +idu+ " AND Date = '"+date+"'",null);
            int d=0, m=0;
            if(s.moveToFirst()){
                do{
                    int point = s.getInt(2);
                    if(point == 1){
                        d +=1;
                    }else{
                        m +=1;
                    }
                }while(s.moveToNext());
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date da = sdf.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(da);
            if(d==d+m){
                events.add(new EventDay(cal, R.drawable.check));
            }else{
                events.add(new EventDay(cal, R.drawable.star));
            }

            calendarView.setEvents(events);

        }
    }

}