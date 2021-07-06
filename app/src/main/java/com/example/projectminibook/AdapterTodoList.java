package com.example.projectminibook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterTodoList extends BaseAdapter {
    Activity context;
    ArrayList<TodoList> list;
    final String DATABASE_NAME = "DatabaseNote.db";
    SQLiteDatabase database;
    Cursor cursor;
    SharedPreferences preferences;
    String idu;

    public AdapterTodoList(Activity context, ArrayList<TodoList> list) {
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

    TextView title;
    TextView time;
    ImageButton suaCv,xoaCv;
    CheckBox check;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.todo_row, null);
        title = v.findViewById(R.id.title);
        suaCv = v.findViewById(R.id.suaCv);
        check = v.findViewById(R.id.check);
        xoaCv = v.findViewById(R.id.xoaCv);
        time = v.findViewById(R.id.time);

        TodoList todo = list.get(position);
        title.setText(todo.ten);
        check.setChecked(todo.check);
        time.setText(todo.time);
        int id = todo.getId();

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Toast.makeText(context, "Đã hoàn thành", Toast.LENGTH_SHORT).show();
                    updateStatus(id,1);

                }else{
                    Toast.makeText(context, "Chưa hoàn thành", Toast.LENGTH_SHORT).show();
                    updateStatus(id,0); }
                Intent i = new Intent(context, ListTodo.class);
                context.startActivity(i);
            }
        });

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, todo_detail.class);
                intent.putExtra("ID",id);
                context.startActivity(intent);
            }
        });

        suaCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, updateTodo.class);
                intent.putExtra("ID",id);
                context.startActivity(intent);
            }
        });

        xoaCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa công việc này không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(todo.getId());
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return v;
    }



    private void delete(int id) {
        database = Database.initDatabase(context, DATABASE_NAME);
        database.delete("todolist","ID = ?",new String[]{id +""});
        setUp();
    }

    public void updateStatus(int id , int status){
        database = Database.initDatabase(context, DATABASE_NAME);
        ContentValues values = new ContentValues();
        values.put("CheckNote", status);
        database.update("todoList" , values , "ID=?" , new String[] {id + ""});

    }

    public void setUp(){
        preferences = context.getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        idu = preferences.getString("ID","");
        java.util.Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        cursor = database.rawQuery("SELECT * FROM todoList WHERE Id_user = " +idu+ " AND Date = '"+formattedDate+"'",null);
        list.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int idTd = cursor.getInt(0);
            String ten = cursor.getString(1);
            String nd = cursor.getString(3);
            String date = cursor.getString(6);
            boolean check = getBoolean(2);
            int idu = cursor.getInt(5);
            String time = cursor.getString(4);
            list.add(new TodoList(idTd, ten, nd, date, check, idu,time));
        }
        notifyDataSetChanged();
        
    }

    public boolean getBoolean(int columnIndex) {
        if (cursor.isNull(columnIndex) || cursor.getShort(columnIndex) == 0) {
            return false;
        } else {
            return true;
        }
    }




}
