package cn.smiles.andclock.entity;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * 双色球历史开奖实体
 *
 * @author kaifang
 * @date 2018/7/30 13:35
 */
public class SSQEntity {
    private Integer _id;
    private String period;// 期号
    private String red_1;// 红球
    private String red_2;
    private String red_3;
    private String red_4;
    private String red_5;
    private String red_6;
    private String blue_1;// 蓝球
    private String happy_sunday;// 快乐星期天
    private String pool_prize;// 奖池奖金(元)
    private String first_count;// 一等奖 注数
    private String first_prize;// 一等奖 奖金(元)
    private String second_count;// 二等奖 注数
    private String second_prize;// 二等奖 奖金(元)
    private String total_prize;// 总投注额(元)
    private String lottery_date;// 开奖日期

    @Override
    public String toString() {
        return "_id=" + _id + '\n' +
                "period=" + period + '\n' +
                "red_1=" + red_1 + '\n' +
                "red_2=" + red_2 + '\n' +
                "red_3=" + red_3 + '\n' +
                "red_4=" + red_4 + '\n' +
                "red_5=" + red_5 + '\n' +
                "red_6=" + red_6 + '\n' +
                "blue_1=" + blue_1 + '\n' +
                "happy_sunday=" + happy_sunday + '\n' +
                "pool_prize=" + pool_prize + '\n' +
                "first_count=" + first_count + '\n' +
                "first_prize=" + first_prize + '\n' +
                "second_count=" + second_count + '\n' +
                "second_prize=" + second_prize + '\n' +
                "total_prize=" + total_prize + '\n' +
                "lottery_date=" + lottery_date;
    }

    public SSQEntity() {
    }

    public SSQEntity(Cursor cursor) {
        int i = 0;
        set_id(cursor.getInt(i++));
        setPeriod(cursor.getString(i++));
        setRed_1(cursor.getString(i++));
        setRed_2(cursor.getString(i++));
        setRed_3(cursor.getString(i++));
        setRed_4(cursor.getString(i++));
        setRed_5(cursor.getString(i++));
        setRed_6(cursor.getString(i++));
        setBlue_1(cursor.getString(i++));
        setHappy_sunday(cursor.getString(i++));
        setPool_prize(cursor.getString(i++));
        setFirst_count(cursor.getString(i++));
        setFirst_prize(cursor.getString(i++));
        setSecond_count(cursor.getString(i++));
        setSecond_prize(cursor.getString(i++));
        setTotal_prize(cursor.getString(i++));
        setLottery_date(cursor.getString(i));
    }

    public ContentValues insertDB() {
        ContentValues values = new ContentValues();
        values.put("period", getPeriod());
        values.put("red_1", getRed_1());
        values.put("red_2", getRed_2());
        values.put("red_3", getRed_3());
        values.put("red_4", getRed_4());
        values.put("red_5", getRed_5());
        values.put("red_6", getRed_6());
        values.put("blue_1", getBlue_1());
        values.put("happy_sunday", getHappy_sunday());
        values.put("pool_prize", getPool_prize());
        values.put("first_count", getFirst_count());
        values.put("first_prize", getFirst_prize());
        values.put("second_count", getSecond_count());
        values.put("second_prize", getSecond_prize());
        values.put("total_prize", getTotal_prize());
        values.put("lottery_date", getLottery_date());
        return values;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getRed_1() {
        return red_1;
    }

    public void setRed_1(String red_1) {
        this.red_1 = red_1;
    }

    public String getRed_2() {
        return red_2;
    }

    public void setRed_2(String red_2) {
        this.red_2 = red_2;
    }

    public String getRed_3() {
        return red_3;
    }

    public void setRed_3(String red_3) {
        this.red_3 = red_3;
    }

    public String getRed_4() {
        return red_4;
    }

    public void setRed_4(String red_4) {
        this.red_4 = red_4;
    }

    public String getRed_5() {
        return red_5;
    }

    public void setRed_5(String red_5) {
        this.red_5 = red_5;
    }

    public String getRed_6() {
        return red_6;
    }

    public void setRed_6(String red_6) {
        this.red_6 = red_6;
    }

    public String getBlue_1() {
        return blue_1;
    }

    public void setBlue_1(String blue_1) {
        this.blue_1 = blue_1;
    }

    public String getHappy_sunday() {
        return happy_sunday;
    }

    public void setHappy_sunday(String happy_sunday) {
        this.happy_sunday = happy_sunday;
    }

    public String getPool_prize() {
        return pool_prize;
    }

    public void setPool_prize(String pool_prize) {
        this.pool_prize = pool_prize;
    }

    public String getFirst_count() {
        return first_count;
    }

    public void setFirst_count(String first_count) {
        this.first_count = first_count;
    }

    public String getFirst_prize() {
        return first_prize;
    }

    public void setFirst_prize(String first_prize) {
        this.first_prize = first_prize;
    }

    public String getSecond_count() {
        return second_count;
    }

    public void setSecond_count(String second_count) {
        this.second_count = second_count;
    }

    public String getSecond_prize() {
        return second_prize;
    }

    public void setSecond_prize(String second_prize) {
        this.second_prize = second_prize;
    }

    public String getTotal_prize() {
        return total_prize;
    }

    public void setTotal_prize(String total_prize) {
        this.total_prize = total_prize;
    }

    public String getLottery_date() {
        return lottery_date;
    }

    public void setLottery_date(String lottery_date) {
        this.lottery_date = lottery_date;
    }
}
