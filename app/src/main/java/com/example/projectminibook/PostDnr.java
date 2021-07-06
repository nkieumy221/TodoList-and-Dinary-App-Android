package com.example.projectminibook;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import java.util.Calendar;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class PostDnr extends AppCompatActivity {
    EditText noidung,  title;
    TextView photos, feel;
    Button post;
    final int RESQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;
    ImageView imageView;

    SharedPreferences preferences;
    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;
    String idu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_dnr);
        getSupportActionBar().hide();
        addControls();
        addEvent();
    }

    private void addControls() {
        post = findViewById(R.id.dang);
        noidung = findViewById(R.id.content_post);
        photos = findViewById(R.id.photo);
        feel = findViewById(R.id.feel);
        imageView = (ImageView) findViewById(R.id.imagePost);
        title = findViewById(R.id.tieude);
    }

    private  void addEvent(){
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postBlog();
            }
        });
        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PostDnr.this);
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
        feel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFeeling();
            }
        });

    }

    private void postBlog() {
        String nd = noidung.getText().toString();
        String cx = feel.getText().toString();
        byte[] anh = getByteArrayFromImageView(imageView);
        String tt = title.getText().toString();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(" HH:mm dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());

        preferences = getApplication().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        idu = preferences.getString("ID","");
        ContentValues contentValues = new ContentValues();
        contentValues.put("content", nd);
        contentValues.put("time", formattedDate);
        contentValues.put("img", anh);
        contentValues.put("feel", cx);
        contentValues.put("local", tt);
        contentValues.put("ID_user", idu);

        database = Database.initDatabase(this,DATABASE_NAME);
        database.insert("dinary",null, contentValues);
        Intent i = new Intent(PostDnr.this, dinary_form.class);
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
        arrayAdapter.add("Mệt mỏi");

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
                feel.setText(strName);
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
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == RESQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
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