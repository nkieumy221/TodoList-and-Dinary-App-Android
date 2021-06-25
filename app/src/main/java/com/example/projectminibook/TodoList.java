package com.example.projectminibook;

public class TodoList {
    private int id;
    public String ten;
    public String noidung;
    public String date;
    public boolean check;
    public int id_us;
    public String time;

    public TodoList(int id, String ten, String noidung,String date, boolean check, int id_us,String time) {
        this.setId(id);
        this.ten = ten;
        this.noidung = noidung;
        this.date = date;
        this.check = check;
        this.id_us = id_us;
        this.time =time;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
