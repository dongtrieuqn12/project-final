package com.example.user.bk_android.TableList;

public class Table_waiting {
    String bienso,time;
    Double vantoc,khoangcach;

    public Table_waiting(){

    }

    public Table_waiting(String bienso, String time, Double vantoc, Double khoangcach) {
        this.bienso = bienso;
        this.time = time;


        this.vantoc = vantoc;
        this.khoangcach = khoangcach;

    }

    public String getBienso() {
        return bienso;
    }

    public void setBienso(String bienso) {
        this.bienso = bienso;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getVantoc() {
        return vantoc;
    }

    public void setVantoc(Double vantoc) {
        this.vantoc = vantoc;
    }

    public Double getKhoangcach() {
        return khoangcach;
    }

    public void setKhoangcach(Double khoangcach) {
        this.khoangcach = khoangcach;
    }
}
