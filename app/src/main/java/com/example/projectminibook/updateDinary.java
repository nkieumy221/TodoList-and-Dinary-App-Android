package com.example.projectminibook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class updateDinary extends AppCompatActivity {
    Button postUp;
    EditText contentPost, chosephoto, feeling, title;
    ImageView imgV;
    int id =-1;
    final int RESQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_dinary);
        addControls();
        addEvent();
        unitUI();
    }

    private void addControls() {
        title = findViewById(R.id.title);
        postUp = findViewById(R.id.post);
        contentPost = findViewById(R.id.contentUd);
        chosephoto = findViewById(R.id.photo);
        feeling = findViewById(R.id.feel);
        imgV = (ImageView) findViewById(R.id.img_post);
    }

    private void unitUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("ID",-1);
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM dinary WHERE ID = ?",new String[]{id +""});
        cursor.moveToFirst();
        String tt = cursor.getString(7);
        String ct = cursor.getString(1);
        String time = cursor.getString(4);
        byte[] anh = cursor.getBlob(2);
        String cx = cursor.getString(6);

        Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0, anh.length);

        title.setText(tt);
        imgV.setImageBitmap(bitmap);
        contentPost.setText(ct);
        feeling.setText(cx);

    }
    private  void addEvent(){
        postUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertBlog();
            }
        });
        chosephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(updateDinary.this);
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
        });
        feeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFeeling();
            }
        });
    }

    private void insertBlog() {
        String nd = contentPost.getText().toString();
        String cx = feeling.getText().toString();
        byte[] anh = getByteArrayFromImageView(imgV);
        String tt = title.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("content", nd);
        contentValues.put("img", anh);
        contentValues.put("feel", cx);
        contentValues.put("local", tt);

        database = Database.initDatabase(this,DATABASE_NAME);
        database.update("dinary", contentValues, " ID = ?", new String[] {id + ""});
        database.close();
        Intent i = new Intent(updateDinary.this, dinary_form.class);
        startActivity(i);

    }

    private void chooseFeeling() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.emoticon);
        builderSingle.setTitle("Select One Name:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Hạnh phúc");
        arrayAdapter.add("Vui");
        arrayAdapter.add("Buồn");
        arrayAdapter.add("Chán nản");
        arrayAdapter.add("Hoang mang");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                feeling.setText(strName);
            }
        });
        builderSingle.show();
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
                    imgV.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == RESQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgV.setImageBitmap(bitmap);
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

}