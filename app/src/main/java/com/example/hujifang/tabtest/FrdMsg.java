package com.example.hujifang.tabtest;

import java.io.Serializable;

public class FrdMsg implements Serializable{
    private int frdImage;
    private String name;
    private String hint;
    FrdMsg(int frdImage, String name, String hint){
        this.frdImage = frdImage;
        this.name = name;
        this.hint = hint;
    }
    public int getFrdImage(){
        return frdImage;
    }
    public String getName(){
        return name;
    }
    public String getHint(){
        return hint;
    }
    public void setHint(String hint){
        this.hint = hint;
    }
}
