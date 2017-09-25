package cn.smiles.andclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smiles.andclock.adapter.MListAdapter;
import cn.smiles.andclock.tools.CalendarTools;

/**
 * 日历activity
 *
 * @author kaifang
 * @date 2017/9/25 14:34
 */
public class CalendarActivity extends AppCompatActivity implements CalendarTools.MCalendarListener {

    @BindView(R.id.lv_list_view)
    ListView lvListView;
    private CalendarTools calendarTools;
    private MenuItem action_sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        calendarTools = new CalendarTools();
        calendarTools.listener = this;
        MListAdapter listAdapter = new MListAdapter(this, calendarTools);
        lvListView.setAdapter(listAdapter);
        lvListView.setSelection(calendarTools.getMcalendars().size() / 2);
    }

    @Override
    public void onOperator(int checked_sum) {
        if (action_sum != null)
            action_sum.setTitle("已选" + checked_sum + "天");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar_item, menu);
        action_sum = menu.findItem(R.id.action_sum);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_today:
                lvListView.setSelection(calendarTools.getMcalendars().size() / 2);
                break;
            case R.id.action_sum:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
