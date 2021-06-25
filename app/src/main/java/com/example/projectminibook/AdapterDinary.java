package com.example.projectminibook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AdapterDinary extends BaseAdapter {
    Activity context;
    ArrayList<DinaryModel> list;
    DinaryModel dinary;
    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;
    Cursor cursor,c;
    SharedPreferences preferences;


    public AdapterDinary(Activity context, ArrayList<DinaryModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        TextView content,time,feel,tt;
        ImageView img;
        ImageButton btnmore;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView ==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dinary_row,null);
            viewHolder = new ViewHolder();
            viewHolder.content = convertView.findViewById(R.id.content_post);
            viewHolder.time = convertView.findViewById(R.id.time_post);
            viewHolder.img = convertView.findViewById(R.id.imgPost);
            viewHolder.feel = convertView.findViewById(R.id.local);
            viewHolder.btnmore = convertView.findViewById(R.id.more);
            viewHolder.tt = convertView.findViewById(R.id.tieude);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        dinary = list.get(position);
        viewHolder.tt.setText(dinary.title);
        viewHolder.content.setText(dinary.content);
        viewHolder.time.setText(dinary.time);
        viewHolder.feel.setText("Đang cảm thấy " + dinary.feel);
        int id = dinary.getId();

        Bitmap avt_img = BitmapFactory.decodeByteArray(dinary.img,0,dinary.img.length);
        viewHolder.img.setImageBitmap(avt_img);


        viewHolder.btnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.inflate(R.menu.more_menu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itEdit:
                                Intent intent = new Intent(context, updateDinary.class);
                                intent.putExtra("ID", id);
                                context.startActivity(intent);
                                break;
                            case R.id.itDel:
                                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                                builder.setIcon(android.R.drawable.ic_delete);
                                builder.setTitle("Xác nhận xóa");
                                builder.setMessage("Bạn có chắc chắn muốn xóa công việc này không?");
                                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        delete(id);
                                    }
                                });
                                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                break;
                        }
                        return true;
                    }
                });
            }
        });
        return convertView;
    }

    private void delete(int id) {
        database = Database.initDatabase(context, DATABASE_NAME);
        database.delete("dinary","ID = ?",new String[]{id +""});
        setUp();
    }

    public void setUp(){

        database = Database.initDatabase(context, DATABASE_NAME);
        preferences = context.getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        String idus = preferences.getString("ID","");
        cursor = database.rawQuery("SELECT * FROM dinary WHERE ID_user = ?",new String[]{idus +""});
        list.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String content = cursor.getString(1);
            String time = cursor.getString(4);
            byte[] anh = cursor.getBlob(2);
            String feel = cursor.getString(6);
            int idu = cursor.getInt(5);
            String tt = cursor.getString(7);
            list.add(new DinaryModel(id,content,anh,time,feel,idu,tt));
        }
        notifyDataSetChanged();
    }

}
