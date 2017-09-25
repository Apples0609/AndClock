package cn.smiles.andclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smiles.andclock.adapter.MListAdapter;
import cn.smiles.andclock.entity.MMonth;
import cn.smiles.andclock.tools.CalendarTools;

public class CalendarActivity extends AppCompatActivity {

    @BindView(R.id.lv_list_view)
    ListView lvListView;
    private CalendarTools calendarTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        calendarTools = new CalendarTools();
        List<MMonth> threeYeah = calendarTools.getThreeYeah();
        MListAdapter listAdapter = new MListAdapter(this, threeYeah, calendarTools);
        lvListView.setAdapter(listAdapter);
        lvListView.setSelection(threeYeah.size() / 2);
    }


}
