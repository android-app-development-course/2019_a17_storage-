package com.example.storage1.main;

import android.widget.ImageView;
import android.widget.TextView;

public class Card {
    private String name;
    private String loca;
    private byte[] img;

    public Card() {
        img=new byte[] {};
    }

    public Card(String name, String loca,byte[] img) {
        this.name = name;
        this.loca = loca;
        this.img=img;
    }

    public String getName() {
        return name;
    }

    public String getLoca() {
        return loca;
    }

    public byte[] getImg() { return img;}
}
