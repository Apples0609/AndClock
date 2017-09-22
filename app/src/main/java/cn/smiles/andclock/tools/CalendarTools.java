package cn.smiles.andclock.tools;

import java.text.SimpleDateFormat;
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

    public static int all_yeah = 3;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());

    /**
     * 获取三年间日期，去年 今年 明年
     */
    public static List<MMonth> getThreeYeah() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
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
    public static List<MMonth> getMyCalendars(Date startdate, Date enddate) {
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
        while (calendar.getTime().before(enddate)) {
            String month = String.format(Locale.getDefault(), "%02d", (calendar.get(Calendar.MONTH) + 1));
            if (myMonth == null || !dates.get(dates.size() - 1).month.equals(month)) {
                String yeah = String.valueOf(calendar.get(Calendar.YEAR));
                mdays = new ArrayList<>();
                myMonth = new MMonth(yeah, month, mdays);
                dates.add(myMonth);
            }
            mdays.add(new MMonth.MDate(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

}
