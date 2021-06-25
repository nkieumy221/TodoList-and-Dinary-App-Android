package com.example.projectminibook;

public class DinaryModel {
    private int id;
    public String content;
    public byte[] img;
    public String time;
    public String feel;
    private int idus;
    public String title;

    public DinaryModel(int id, String content, byte[] img, String time, String feel, int idus,String title) {
        this.setId(id);
        this.content = content;
        this.img = img;
        this.time = time;
        this.feel = feel;
        this.idus = idus;
        this.title = title;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdus() {
        return idus;
    }

    public void setIdus(int idus) {
        this.idus = idus;
    }
}
