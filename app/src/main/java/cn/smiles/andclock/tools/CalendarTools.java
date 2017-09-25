package cn.smiles.andclock.tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.smiles.andclock.entity.MDate;
import cn.smiles.andclock.entity.MMonth;

/**
 * 日历生成工具
 *
 * @author kaifang
 * @date 2017/9/22 17:15
 */
public class CalendarTools {

    private MDate check_start_date;
    private MDate check_end_date;
    private List<MMonth> mcalendars;
    public MCalendarListener listener;

    public interface MCalendarListener {
        void onOperator(int checked_sum);
    }

    public CalendarTools() {
        this(3);
    }

    public CalendarTools(int all_yeah) {
        getThreeYeah(all_yeah);
    }

    public MDate getCheck_start_date() {
        return check_start_date;
    }

    public MDate getCheck_end_date() {
        return check_end_date;
    }

    public List<MMonth> getMcalendars() {
        return mcalendars;
    }

    public void setCheck_date(MDate check_date) {
        if (check_start_date != null && check_end_date != null) {
            check_start_date = check_date;
            check_end_date = null;
        } else {
            if (check_start_date == null) {
                check_start_date = check_date;
            } else {
                if (check_date.date.getTime() > check_start_date.date.getTime()) {
                    check_end_date = check_date;
                } else {
                    check_start_date = null;
                    check_end_date = null;
                }
            }
        }
    }

    /**
     * 获取三年间日期，去年 今年 明年
     */
    private List<MMonth> getThreeYeah(int all_yeah) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        if (all_yeah < 3) all_yeah = 3;
        calendar.add(Calendar.YEAR, all_yeah / 2);
        Date ntime = calendar.getTime();
        calendar.add(Calendar.YEAR, -(all_yeah - 1));
        Date ltime = calendar.getTime();
        mcalendars = getMyCalendars(ltime, ntime);
        return mcalendars;
    }

    /**
     * 获取指定日期与结束日期之间所有日期
     *
     * @param startdate
     * @param enddate
     * @return 返回所有月份
     */
    private List<MMonth> getMyCalendars(Date startdate, Date enddate) {
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
        List<MDate> mdays = null;
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
                    mdays.add(new MDate(null));
                myMonth = new MMonth(cyeah, cmonth, mdays);
                dates.add(myMonth);
            }
            int cday = calendar.get(Calendar.DAY_OF_MONTH);
            Date ctime = calendar.getTime();
            MDate mDate = new MDate(cday, ctime);
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
