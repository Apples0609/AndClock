package cn.smiles.andclock.tools;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.smiles.andclock.SmilesApplication;
import cn.smiles.andclock.entity.SSQEntity;
import cn.smiles.andclock.retrofit.SSQ500Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * https://datachart.500.com/ssq/history/newinc/history.php?start=18080&end=18200&sort=1
 * 获取500彩票网双色球数据
 *
 * @author kaifang
 * @date 2018/7/31 9:44
 */
public class Get500SSQData {

    public final static String dbFileName = "ssq.db";

    private static SharedPreferences dsp;
    private static String curDate;
    private static int jiShu = 0;

    /**
     * 获取500html数据
     *
     * @param st
     * @param ed
     */
    private static void retrofitHtml(String st, String ed) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://datachart.500.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        SSQ500Service service = retrofit.create(SSQ500Service.class);
        Call<String> call = service.getHtml(st, ed, "1");
        try {
            Response<String> response = call.execute();
            String body = response.body();
            if (body != null) {
                System.out.println(body);
                insertSSQDB(body);
                dsp.edit().putString("SaveDBTime", curDate).apply();
            }
            jiShu = 0;
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof SocketTimeoutException) {
                jiShu++;
                if (jiShu < 3)
                    querySSQData();
            } else {
                String em = "获取500双色球数据错误，" + e.getMessage();
                SmilesApplication.handler.post(() -> Toast.makeText(SmilesApplication.appContext, em, Toast.LENGTH_SHORT).show());
            }
        }
    }

    private static void insertSSQDB(String html) {
        Document doc = Jsoup.parse(html);
        Element tbody = doc.selectFirst("tbody#tdata");
        Elements elements = tbody.select("tr.t_tr1");
        if (!elements.isEmpty()) {
            SQLiteDatabase liteDb = SQLiteDatabase.openOrCreateDatabase(SmilesApplication.appContext.getDatabasePath(dbFileName), null);
            liteDb.beginTransaction();
            for (Element element : elements) {
                Elements tags = element.getElementsByTag("td");
                SSQEntity entity = new SSQEntity();
                int i = 0;
                entity.setPeriod(tags.get(i++).text().trim());
                entity.setRed_1(tags.get(i++).text().trim());
                entity.setRed_2(tags.get(i++).text().trim());
                entity.setRed_3(tags.get(i++).text().trim());
                entity.setRed_4(tags.get(i++).text().trim());
                entity.setRed_5(tags.get(i++).text().trim());
                entity.setRed_6(tags.get(i++).text().trim());
                entity.setBlue_1(tags.get(i++).text().trim());
                entity.setHappy_sunday(tags.get(i++).text().trim());
                entity.setPool_prize(tags.get(i++).text().trim());
                entity.setFirst_count(tags.get(i++).text().trim());
                entity.setFirst_prize(tags.get(i++).text().trim());
                entity.setSecond_count(tags.get(i++).text().trim());
                entity.setSecond_prize(tags.get(i++).text().trim());
                entity.setTotal_prize(tags.get(i++).text().trim());
                entity.setLottery_date(tags.get(i).text().trim());
                liteDb.insert("ssq_history", null, entity.insertDB());
            }
            liteDb.setTransactionSuccessful();
            liteDb.endTransaction();
            liteDb.close();
            SmilesApplication.handler.post(() -> Toast.makeText(SmilesApplication.appContext, "双色球开奖更新成功~", Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * 插入最新双色球开奖数据
     */
    public static void querySSQData() {
        new Thread(() -> {
            boolean b = copyDB();
            if (b) {
                dsp = PreferenceManager.getDefaultSharedPreferences(SmilesApplication.appContext);
                String oldDate = dsp.getString("SaveDBTime", null);
                curDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
                if (oldDate == null || !curDate.equals(oldDate)) {
                    SQLiteDatabase liteDb = SQLiteDatabase.openOrCreateDatabase(SmilesApplication.appContext.getDatabasePath(dbFileName), null);
                    Cursor cursor = liteDb.rawQuery("select max(period) from ssq_history;", null);
                    cursor.moveToFirst();
                    final String startPeriod = String.valueOf(Integer.parseInt(cursor.getString(0)) + 1);
                    cursor.close();
                    liteDb.close();
                    final String endPeriod = curDate.substring(2, 4) + "300";
                    retrofitHtml(startPeriod, endPeriod);
                }
                dsp = null;
                curDate = null;
            }
        }).start();
    }

    /**
     * 复制数据库到db目录
     */
    private static boolean copyDB() {
        try {
            File dbFile = SmilesApplication.appContext.getDatabasePath(dbFileName);
            if (!dbFile.exists()) {
                if (!dbFile.getParentFile().exists()) {
                    boolean b = dbFile.getParentFile().mkdir();
                    System.out.println(b);
                }
                InputStream inputStream = SmilesApplication.appContext.getAssets().open(dbFileName);
                OutputStream outputStream = new FileOutputStream(dbFile);
                byte[] buffer = new byte[1024 * 8];
                int numOfBytesToRead;
                while ((numOfBytesToRead = inputStream.read(buffer)) > 0)
                    outputStream.write(buffer, 0, numOfBytesToRead);
                inputStream.close();
                outputStream.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
