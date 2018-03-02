package cn.smiles.andclock;

import org.junit.Test;

import java.util.Calendar;
import java.util.Locale;

public class Test2 {

    @Test
    public void test1() throws Exception {
        Calendar today = Calendar.getInstance();

        System.out.println(String.format(Locale.getDefault(), "%03d", 2));
    }

}
