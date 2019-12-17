package com.example.storage1.goods;

public class Goods {
    private String label;
    private String value;

    public Goods(String label, String value) {
        this.value = value;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String s) { this.value=s;}
}
