package cn.smiles.andclock.entity;

import java.util.Date;
import java.util.List;

public class MMonth {
    public String yeah;
    public String month;
    public List<MDate> dates;

    public MMonth() {
    }

    public MMonth(String yeah, String month, List<MDate> dates) {
        this.yeah = yeah;
        this.month = month;
        this.dates = dates;
    }

    public static class MDate {
        public boolean isChecked;
        public Date date;

        public MDate(Date date) {
            this.date = date;
        }

        public MDate(boolean isChecked, Date date) {
            this.isChecked = isChecked;
            this.date = date;
        }
    }
}
