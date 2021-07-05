package com.example.projectminibook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class updateUser extends AppCompatActivity {
    ImageView avtIv;
    Button btnsave, btncancel;
    TextView change;
    EditText edtName, edtBd, edtPhone, edtHb;
    String id ;
    final int RESQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        getSupportActionBar().hide();
        preferences = getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        id = preferences.getString("ID","");
        addControls();
        initUI();
        addEvents();
    }

    private void initUI() {
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM profile WHERE ID = ?",new String[]{id +""});
        cursor.moveToFirst();
        String ten = cursor.getString(1);
        String telephone = cursor.getString(2);
        String hb = cursor.getString(3);
        String bd =cursor.getString(4);
        byte[] anh = cursor.getBlob(5);
        Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0, anh.length);

        avtIv.setImageBitmap(bitmap);
        edtName.setText(ten);
        edtHb.setText(hb);
        edtBd.setText(bd);
        edtPhone.setText(telephone);
    }

    private void addControls() {
        avtIv = findViewById(R.id.avtImg);
        change = findViewById(R.id.editAvt);
        edtName = findViewById(R.id.usnamTv);
        edtBd = findViewById(R.id.birthday);
        edtPhone = findViewById(R.id.tel);
        edtHb = findViewById(R.id.hobb);
        btncancel = findViewById(R.id.btnCancel);
        btnsave = findViewById(R.id.btnSave);
    }

    private void addEvents() {
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture();
            }
        });
        edtBd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDay();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUser();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cancel();
            }
        });
    }

    private void insertUser() {
        String ten = edtName.getText().toString();
        String snh = edtBd.getText().toString();
        String sdt = edtPhone.getText().toString();
        String sthich = edtHb.getText().toString();
        byte[] anh = getByteArrayFromImageView(avtIv);

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", ten);
        contentValues.put("telephone", sdt);
        contentValues.put("hobby", sthich);
        contentValues.put("birthday", snh);
        contentValues.put("avatar", anh);

        database = Database.initDatabase(this,DATABASE_NAME);
        database.update("profile", contentValues, " ID = ?", new String[] {id + ""});
        database.close();
        Intent i = new Intent(updateUser.this, UserInfor.class);
        startActivity(i);
    }

    private void picture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(updateUser.this);
        //thiết lập tiêu đề cho Dialog
        builder.setTitle("Choose Picture");

        builder.setIcon(R.mipmap.ic_launcher);

        builder.setPositiveButton("Chọn hình ảnh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choosePhoto();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(updateDinary.this, "Đây là setNeutralButton Dialog", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Chụp ảnh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePicture();
            }
        });

        builder.create().show();
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESQUEST_TAKE_PHOTO);
    }

    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    avtIv.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == RESQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                avtIv.setImageBitmap(bitmap);
            }

        }
    }

    private byte[] getByteArrayFromImageView(ImageView imgv){
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void selectDay() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int ngay = calendar.get(java.util.Calendar.DATE);
        int thang = calendar.get(java.util.Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        edtBd.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },
                nam, thang, ngay);
        datePickerDialog.show();
    }

    private void Cancel() {
        Intent intent = new Intent(this, UserInfor.class);
        startActivity(intent);
    }

}