package com.example.projectminibook;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class updateTodo extends AppCompatActivity {
    ImageButton btnSave, btnCancel;
    EditText edtTen, edtNd ;
    TimePicker timePicker;
    private int notificationId = 1;

    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;
    int id =-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_todo);
        addControls();
        initUI();
        addEvent();
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
        edtTen.setText(ten);
        edtNd.setText(nd);

    }

    private void addControls() {
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        edtTen = findViewById(R.id.editTitle);
        edtNd = findViewById(R.id.editContent);
        timePicker = findViewById(R.id.timePicker);

    }

    private  void addEvent(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thongbao();
                update();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
    private void thongbao() {
        // Intent
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("message", edtTen.getText().toString());

        // PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                updateTodo.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
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
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();

    }


    private void update(){
        String ten = edtTen.getText().toString();
        String nd = edtNd.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("TenNote", ten);
        contentValues.put("Note", nd);


        database = Database.initDatabase(this,DATABASE_NAME);
        database.update(" todoList ", contentValues, " ID = ?", new String[] {id + ""});
        database.close();
        Intent i = new Intent(updateTodo.this, ListTodo.class);
        startActivity(i);
    }

    private void cancel(){
       finish();
    }


}