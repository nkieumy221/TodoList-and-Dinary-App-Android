package com.example.projectminibook;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTodo extends AppCompatActivity {
    Button btnSave, btnCancel;
    EditText edtTen, edtNd;
    TimePicker timePicker;

    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;
    SharedPreferences preferences;
    String time;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        getSupportActionBar().hide();
        addControls();
        addEvent();
    }

    private void addControls() {
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        edtTen = findViewById(R.id.editTitle);
        edtNd = findViewById(R.id.editContent);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
    }

    private  void addEvent(){
        btnSave.setOnClickListener(v -> {
            thongBao();
            insertTodo();
            Intent i = new Intent(AddTodo.this, ListTodo.class);
            startActivity(i);

        });
        btnCancel.setOnClickListener(v -> finish());
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> time = hourOfDay + ":" + minute);
    }

    private void thongBao() {
        // Intent
        Intent intent = new Intent(AddTodo.this, AlarmReceiver.class);
        int notificationId = 1;
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("message", edtTen.getText().toString());

        // PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                AddTodo.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
        );

        // AlarmManager
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        // Create time.
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, hour);
        startTime.set(Calendar.MINUTE, minute);
        startTime.set(Calendar.SECOND, 0);
        long alarmStartTime = startTime.getTimeInMillis();

        // Set Alarm
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
        Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();

    }

    private void insertTodo(){
        String ten = edtTen.getText().toString();
        String nd = edtNd.getText().toString();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());

        ContentValues contentValues = new ContentValues();
        preferences = getApplication().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        String idus = preferences.getString("ID","");

        contentValues.put("TenNote", ten);
        contentValues.put("Note", nd);
        contentValues.put("Date",formattedDate);
        contentValues.put("Id_user", idus);
        contentValues.put("Time", time);

        database = Database.initDatabase(this,DATABASE_NAME);
        database.insert(" todoList ",null, contentValues );
    }

}