package com.example.user.bk_android.TableList;

public class Table {
    int image;
    String string1;
    String string2;

    public Table(){}

    public Table(int image, String string1, String string2) {
        this.image = image;
        this.string1 = string1;
        this.string2 = string2;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }
}
