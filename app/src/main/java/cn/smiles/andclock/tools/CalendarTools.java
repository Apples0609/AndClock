package cn.smiles.andclock.tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.smiles.andclock.entity.MMonth;

/**
 * 日历生成工具
 *
 * @author kaifang
 * @date 2017/9/22 17:15
 */
public class CalendarTools {

    private int all_yeah = 3;
    private Date check_start_date;
    private Date check_end_date;

    public CalendarTools() {
    }

    public CalendarTools(int all_yeah) {
        this.all_yeah = all_yeah;
    }

    public Date getCheck_start_date() {
        return check_start_date;
    }

    public void setCheck_start_date(Date check_start_date) {
        this.check_start_date = check_start_date;
    }

    public Date getCheck_end_date() {
        return check_end_date;
    }

    public void setCheck_end_date(Date check_end_date) {
        this.check_end_date = check_end_date;
    }

    /**
     * 获取三年间日期，去年 今年 明年
     */
    public List<MMonth> getThreeYeah() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        if (all_yeah < 3) all_yeah = 3;
        calendar.add(Calendar.YEAR, all_yeah / 2);
        Date ntime = calendar.getTime();
        calendar.add(Calendar.YEAR, -(all_yeah - 1));
        Date ltime = calendar.getTime();
        return getMyCalendars(ltime, ntime);
    }

    /**
     * 获取指定日期与结束日期之间所有日期
     *
     * @param startdate
     * @param enddate
     * @return 返回所有月份
     */
    public List<MMonth> getMyCalendars(Date startdate, Date enddate) {
        if (startdate == null || enddate == null) return null;
        if (startdate.getTime() > enddate.getTime()) {
            Date temp = startdate;
            startdate = enddate;
            enddate = temp;
        }
        List<MMonth> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(startdate);
        MMonth myMonth = null;
        List<MMonth.MDate> mdays = null;
        Calendar today = Calendar.getInstance(Locale.getDefault());
        int tyeah = today.get(Calendar.YEAR);
        int tmonth = today.get(Calendar.MONTH) + 1;
        int tday = today.get(Calendar.DAY_OF_MONTH);
        while (calendar.getTime().before(enddate)) {
            int cmonth = calendar.get(Calendar.MONTH) + 1;
            int cyeah = calendar.get(Calendar.YEAR);
            if (myMonth == null || dates.get(dates.size() - 1).month != cmonth) {
                mdays = new ArrayList<>();
                int iweek = calendar.get(Calendar.DAY_OF_WEEK);
                for (int i = 1; i < iweek; i++)
                    mdays.add(new MMonth.MDate(null));
                myMonth = new MMonth(cyeah, cmonth, mdays);
                dates.add(myMonth);
            }
            int cday = calendar.get(Calendar.DAY_OF_MONTH);
            Date time = calendar.getTime();
            MMonth.MDate mDate = new MMonth.MDate(cday, time);
            if (tyeah == cyeah
                    && tmonth == cmonth
                    && tday == cday)
                mDate.isToday = true;
            mdays.add(mDate);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

}
