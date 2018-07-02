package com.example.user.bk_android.TableList;

import android.widget.CheckBox;
import android.widget.TextView;

public class TableXe50 {
    Boolean check;
    String string;

    public  TableXe50 (){

    }
    public TableXe50(Boolean check, String string) {
        this.check = check;
        this.string = string;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
