package cn.smiles.andclock.entity;

import java.util.Date;
import java.util.List;

public class MMonth {
    public int yeah;
    public int month;
    public List<MDate> dates;

    public MMonth() {
    }

    public MMonth(int yeah, int month, List<MDate> dates) {
        this.yeah = yeah;
        this.month = month;
        this.dates = dates;
    }

    public static class MDate {
        public boolean isChecked;
        public int day;
        public Date date;
        public boolean isToday;

        public MDate(Date date) {
            this.date = date;
        }

        public MDate(int day, Date date) {
            this.day = day;
            this.date = date;
        }
    }
}
