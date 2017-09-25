package cn.smiles.andclock.entity;

import java.util.Date;

public class MDate {
    public int day;
    public Date date;
    public boolean isToday;
    public boolean isChecked;

    public MDate(Date date) {
        this.date = date;
    }

    public MDate(int day, Date date) {
        this.day = day;
        this.date = date;
    }
}