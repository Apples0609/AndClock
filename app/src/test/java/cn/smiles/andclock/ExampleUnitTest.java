package cn.smiles.andclock;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test1() throws Exception {
        Date date = new Date(1503974076060l);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = format.format(date);
        System.out.println(s);
    }


    @Test
    public void test2() throws Exception {
        int all_yeah = 3;

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        calendar.add(Calendar.YEAR, all_yeah / 2);
        Date ntime = calendar.getTime();
        calendar.add(Calendar.YEAR, -(all_yeah - 1));
        Date ltime = calendar.getTime();
//        List<Date> list = getDaysBetweenDates(ltime, ntime);
//        for (Date date : list) {
//            System.out.println(dateFormat.format(date));
//        }
//        System.out.println(list.size());


        List<MMonth> myCalendars = getMyCalendars(ltime, ntime);
        for (MMonth myCalendar : myCalendars) {
            System.out.println(myCalendar.yeah + "-" + myCalendar.month + "-" + dates2String(myCalendar.dates));
        }
        System.out.println(myCalendars.size());
    }

    String dates2String(List<Date> dates) {
        StringBuilder sbr = new StringBuilder();
        sbr.append('[');
        for (Date date : dates) {
            sbr.append(dateFormat2.format(date));
            sbr.append(',');
        }
        sbr.deleteCharAt(sbr.length() - 1);
        sbr.append(']');
        return sbr.toString();
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd(u)", Locale.getDefault());

    public List<Date> getDaysBetweenDates(Date startdate, Date enddate) {
        if (startdate == null || enddate == null) return null;
        if (startdate.getTime() > enddate.getTime()) {
            Date temp = startdate;
            startdate = enddate;
            enddate = temp;
        }
        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(startdate);
        while (calendar.getTime().before(enddate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

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
        List<Date> mdays = null;
        while (calendar.getTime().before(enddate)) {
            String month = String.format(Locale.getDefault(), "%02d", (calendar.get(Calendar.MONTH) + 1));
            if (myMonth == null || !dates.get(dates.size() - 1).month.equals(month)) {
                String yeah = String.valueOf(calendar.get(Calendar.YEAR));
                mdays = new ArrayList<>();
                myMonth = new MMonth(yeah, month, mdays);
                dates.add(myMonth);
            }
            mdays.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    class MMonth {
        String yeah;
        String month;
        List<Date> dates;

        public MMonth() {
        }

        public MMonth(String yeah, String month, List<Date> dates) {
            this.yeah = yeah;
            this.month = month;
            this.dates = dates;
        }
    }

}