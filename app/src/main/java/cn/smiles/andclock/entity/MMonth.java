package cn.smiles.andclock.entity;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * 日历 日实体类
 *
 * @author kaifang
 * @date 2017/9/25 14:34
 */
public class MMonth {
    public int yeah;
    public int month;
    public List<MDate> dates;
    public BaseAdapter adapter;

    public MMonth() {
    }

    public MMonth(int yeah, int month, List<MDate> dates) {
        this.yeah = yeah;
        this.month = month;
        this.dates = dates;
    }

}
