package cn.smiles.andclock.activity;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.smiles.andclock.R;
import cn.smiles.andclock.SmilesApplication;
import cn.smiles.andclock.entity.SSQEntity;
import cn.smiles.andclock.tools.Get500SSQData;

/**
 * 双色球历史开奖
 *
 * @author kaifang
 * @date 2018/7/30 13:35
 */
public class SSQActivity extends AppCompatActivity {

    private ArrayList<SSQEntity> entities;
    private SSQAdapter ssqAdapter;
    private int currYear;
    private RecyclerView rvView;
    private Integer[] sjRed;
    private Integer[] sjBlue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssq);

        rvView = findViewById(R.id.ssq_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvView.setLayoutManager(mLayoutManager);
        rvView.setItemAnimator(new DefaultItemAnimator());
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, mLayoutManager.getOrientation());
//        rvView.addItemDecoration(dividerItemDecoration);
        entities = new ArrayList<>();
        ssqAdapter = new SSQAdapter(entities);
        rvView.setAdapter(ssqAdapter);

        currYear = Calendar.getInstance().get(Calendar.YEAR);
        ArrayList<SSQEntity> data = readData(String.valueOf(currYear));
        entities.addAll(data);
        ssqAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ssq_act, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.top_menu:
                rvView.smoothScrollToPosition(0);
                break;
            case R.id.item1_menu://所有数据
                showAllData();
                break;
            case R.id.item2_menu://指定年份
                showOnlyYear();
                break;
            case R.id.item3_menu://分析当前
                analysisList();
                break;
            case R.id.sj_item1:
                randomOnlyCount(1, false);
                break;
            case R.id.sj_item2:
                randomOnlyCount(3, false);
                break;
            case R.id.sj_item3:
                randomOnlyCount(5, false);
                break;
            case R.id.item5_menu:
                Get500SSQData.querySSQData();
                break;
            case R.id.sj_item13:
                randomOnlyCount(3, true);
                break;
            case R.id.sj_item15:
                randomOnlyCount(5, true);
                break;
        }
        return true;
    }

    private void randomOnlyCount(final int count, final boolean isAna) {
        if (isAna && sjRed == null && sjBlue == null) {
            Toast.makeText(this, "请先分析列表！", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] arrs = new String[count];
        for (int i = 0; i < count; i++) {
            arrs[i] = randomSSQ(isAna);
        }
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("，，，后为蓝球")
                .setItems(arrs, null)
                .setPositiveButton("确定", null)
                .setNeutralButton("再来", ((dialog, which) -> randomOnlyCount(count, isAna)))
                .create().show();
    }

    private String randomSSQ(boolean isAna) {
        LinkedList<Integer> reds = new LinkedList<>();
        for (Integer i = 1; i <= 33; i++) {
            reds.add(i);
        }
        if (isAna) {
            for (Integer itnr : sjRed) {
                addListCot(reds, itnr, 3);
            }
        }
        StringBuilder ssq = new StringBuilder();
        ArrayList<Integer> tempR = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Collections.shuffle(reds);
            Integer red = reds.removeFirst();
            tempR.add(red);
        }
        Collections.sort(tempR);
        for (Integer red : tempR) {
            ssq.append(String.format(Locale.getDefault(), "%02d", red));
            ssq.append("，");
        }
        LinkedList<Integer> blues = new LinkedList<>();
        for (Integer i = 1; i <= 16; i++) {
            blues.add(i);
        }
        if (isAna) {
            for (Integer itnr : sjBlue) {
                addListCot(blues, itnr, 3);
            }
        }
        Collections.shuffle(blues);
        Integer blue = blues.removeFirst();
        ssq.append(String.format(Locale.getDefault(), "，，%02d", blue));
        return ssq.toString();
    }

    private void addListCot(List<Integer> list, Integer itnr, int cot) {
        for (int i = 0; i < cot; i++) {
            list.add(itnr);
        }
    }

    private void showAllData() {
        final ProgressDialog dialog1 = ProgressDialog.show(this, null, "加载中……", true, false);
        new Thread(() -> {
            final ArrayList<SSQEntity> data = readData(null);
            runOnUiThread(() -> {
                entities.clear();
                entities.addAll(data);
                ssqAdapter.notifyDataSetChanged();
                dialog1.dismiss();
            });
        }).start();
    }

    private void showOnlyYear() {
        final String[] sitem = new String[currYear - 2003 + 1];
        for (int i = currYear; i >= 2003; i--) {
            sitem[currYear - i] = String.valueOf(i);
        }
        new AlertDialog.Builder(this)
                .setItems(sitem, (dialog, which) -> {
                    ArrayList<SSQEntity> data2 = readData(sitem[which]);
                    entities.clear();
                    entities.addAll(data2);
                    ssqAdapter.notifyDataSetChanged();
                    rvView.scrollToPosition(0);
                })
                .create().show();
    }

    /**
     * 分析当前列表
     */
    private void analysisList() {
        final ProgressDialog dialog2 = ProgressDialog.show(this, null, "计算中……", true, false);
        new Thread(() -> {
            LinkedHashMap<Integer, Integer> lhMRed = new LinkedHashMap<>();
            for (Integer i = 1; i <= 33; i++) {
                lhMRed.put(i, 0);
            }
            LinkedHashMap<Integer, Integer> lhMBlue = new LinkedHashMap<>();
            for (Integer i = 1; i <= 16; i++) {
                lhMBlue.put(i, 0);
            }
            for (SSQEntity entity : entities) {
                Integer red_1 = Integer.parseInt(entity.getRed_1());
                lhMRed.put(red_1, lhMRed.get(red_1) + 1);
                Integer red_2 = Integer.parseInt(entity.getRed_2());
                lhMRed.put(red_2, lhMRed.get(red_2) + 1);
                Integer red_3 = Integer.parseInt(entity.getRed_3());
                lhMRed.put(red_3, lhMRed.get(red_3) + 1);
                Integer red_4 = Integer.parseInt(entity.getRed_4());
                lhMRed.put(red_4, lhMRed.get(red_4) + 1);
                Integer red_5 = Integer.parseInt(entity.getRed_5());
                lhMRed.put(red_5, lhMRed.get(red_5) + 1);
                Integer red_6 = Integer.parseInt(entity.getRed_6());
                lhMRed.put(red_6, lhMRed.get(red_6) + 1);
                Integer blue_1 = Integer.parseInt(entity.getBlue_1());
                lhMBlue.put(blue_1, lhMBlue.get(blue_1) + 1);
            }
            LinkedHashMap<Integer, Integer> sortReds = sortByValue(lhMRed);
            LinkedHashMap<Integer, Integer> sortBlues = sortByValue(lhMBlue);

            //取排序后前3球
            Integer[] redTemp = sortReds.keySet().toArray(new Integer[sortReds.size()]);
            Integer[] blueTemp = sortBlues.keySet().toArray(new Integer[sortBlues.size()]);
            sjRed = new Integer[3];
            System.arraycopy(redTemp, 0, sjRed, 0, sjRed.length);
            sjBlue = new Integer[3];
            System.arraycopy(blueTemp, 0, sjBlue, 0, sjBlue.length);

            StringBuilder spaceTile = new StringBuilder();
            StringBuilder spaceCon = new StringBuilder();
            for (int i = 0; i < 30; i++) {
                spaceCon.append(" ");
                if (i < 20)
                    spaceTile.append(" ");
            }
            ArrayList<String> lists = new ArrayList<>();
            lists.add(String.format("红球%s出现次数", spaceTile));
            for (Map.Entry<Integer, Integer> entry : sortReds.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();
                lists.add(String.format(Locale.getDefault(), "%02d%s%02d", key, spaceCon, value));
            }
            lists.add(String.format("蓝球%s出现次数", spaceTile));
            for (Map.Entry<Integer, Integer> entry : sortBlues.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();
                lists.add(String.format(Locale.getDefault(), "%02d%s%02d", key, spaceCon, value));
            }
            final String[] strings = lists.toArray(new String[lists.size()]);
            runOnUiThread(() -> {
                new AlertDialog.Builder(this)
                        .setItems(strings, null)
                        .setPositiveButton("确定", null)
                        .setCancelable(false)
                        .create().show();
                dialog2.dismiss();
            });
        }).start();
    }

    /**
     * 查询数据
     *
     * @param syear
     * @return
     */
    public ArrayList<SSQEntity> readData(String syear) {
        File dbFile = SmilesApplication.appContext.getDatabasePath(Get500SSQData.dbFileName);
        if (!dbFile.exists()) {
            runOnUiThread(() -> Toast.makeText(this, "数据库不存在，复制可能失败了", Toast.LENGTH_SHORT).show());
            return null;
        }
        ArrayList<SSQEntity> entities = new ArrayList<>();
        SQLiteDatabase liteDb = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor;
        if (syear == null) {
            cursor = liteDb.rawQuery("select * from ssq_history order by _id desc", null);
        } else {
            String wagr = syear.substring(2) + "%";
            cursor = liteDb.rawQuery("select * from ssq_history where period like ? order by _id desc", new String[]{wagr});
        }
        while (cursor.moveToNext()) {
            SSQEntity ssqEntity = new SSQEntity(cursor);
            entities.add(ssqEntity);
        }
        cursor.close();
        liteDb.close();
        return entities;
    }

    /**
     * Map按value倒序排序
     *
     * @param unsortMap
     * @return
     */
    private LinkedHashMap<Integer, Integer> sortByValue(LinkedHashMap<Integer, Integer> unsortMap) {
        // 1. Convert Map to List of Map
        LinkedList<Map.Entry<Integer, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, (o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        LinkedHashMap<Integer, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }


    public class SSQAdapter extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<SSQEntity> entities;

        private SSQAdapter(ArrayList<SSQEntity> entities) {
            this.entities = entities;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_ssq_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SSQEntity entity = entities.get(position);
            holder.lottery_date.setText(getResources().getString(R.string.lottery_date, entity.getLottery_date()));
            holder.periods.setText(getResources().getString(R.string.periods, entity.getPeriod()));
            holder.red_one.setText(entity.getRed_1());
            holder.red_two.setText(entity.getRed_2());
            holder.red_three.setText(entity.getRed_3());
            holder.red_four.setText(entity.getRed_4());
            holder.red_five.setText(entity.getRed_5());
            holder.red_six.setText(entity.getRed_6());
            holder.blue_one.setText(entity.getBlue_1());
            holder.first_count.setText(entity.getFirst_count());
            holder.first_prize.setText(entity.getFirst_prize());
            holder.second_count.setText(entity.getSecond_count());
            holder.second_prize.setText(entity.getSecond_prize());
        }

        @Override
        public int getItemCount() {
            return entities.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView lottery_date;
        public TextView periods;
        public TextView red_one;
        public TextView red_two;
        public TextView red_three;
        public TextView red_four;
        public TextView red_five;
        public TextView red_six;
        public TextView blue_one;
        public TextView first_count;
        public TextView first_prize;
        public TextView second_count;
        public TextView second_prize;

        private ViewHolder(View itemView) {
            super(itemView);
            lottery_date = itemView.findViewById(R.id.tv_lottery_date);
            periods = itemView.findViewById(R.id.tv_periods);
            red_one = itemView.findViewById(R.id.tv_red_one);
            red_two = itemView.findViewById(R.id.tv_red_two);
            red_three = itemView.findViewById(R.id.tv_red_three);
            red_four = itemView.findViewById(R.id.tv_red_four);
            red_five = itemView.findViewById(R.id.tv_red_five);
            red_six = itemView.findViewById(R.id.tv_red_six);
            blue_one = itemView.findViewById(R.id.tv_blue_one);
            first_count = itemView.findViewById(R.id.tv_first_prize);
            first_prize = itemView.findViewById(R.id.tv_first_bonus);
            second_count = itemView.findViewById(R.id.tv_second_prize);
            second_prize = itemView.findViewById(R.id.tv_second_bonus);
        }
    }
}
