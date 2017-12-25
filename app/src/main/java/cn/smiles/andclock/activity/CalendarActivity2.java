package cn.smiles.andclock.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smiles.andclock.R;
import cn.smiles.andclock.adapter.MGridAdapter;
import cn.smiles.andclock.entity.MMonth;
import cn.smiles.andclock.tools.CalendarTools;
import cn.smiles.andclock.view.MGridView;

/**
 * 日历activity
 *
 * @author kaifang
 * @date 2017/9/25 14:34
 */
public class CalendarActivity2 extends AppCompatActivity implements CalendarTools.MCalendarListener {

    @BindView(R.id.rc_recycle_view)
    RecyclerView rcView;
    private CalendarTools calendarTools;
    private MenuItem action_sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar2);
        ButterKnife.bind(this);
        calendarTools = new CalendarTools();
        calendarTools.listener = this;
//        MListAdapter listAdapter = new MListAdapter(this, calendarTools);

        rcView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcView.setLayoutManager(mLayoutManager);
        MyAdapter adapter = new MyAdapter(this, calendarTools);
        rcView.setAdapter(adapter);
        rcView.getLayoutManager().scrollToPosition(calendarTools.getMcalendars().size() / 2);
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
                rcView.getLayoutManager().scrollToPosition(calendarTools.getMcalendars().size() / 2);
                break;
            case R.id.action_sum:

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private final List<MMonth> threeYeah;
        private final LayoutInflater inflater;
        private final Context context;
        private final CalendarTools calendarTools;

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_yeah_month)
            TextView tvYeahMonth;
            @BindView(R.id.gv_grid_view)
            MGridView gvGridView;

            public ViewHolder(View v) {
                super(v);
                ButterKnife.bind(this, v);
            }
        }

        public MyAdapter(Context context, CalendarTools calendarTools) {
            inflater = LayoutInflater.from(context);
            this.context = context;
            this.threeYeah = calendarTools.getMcalendars();
            this.calendarTools = calendarTools;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            MMonth month = threeYeah.get(position);
            holder.tvYeahMonth.setText(month.yeah + "年" + month.month + "月");
            MGridAdapter gridAdapter = new MGridAdapter(context, month, calendarTools);
            holder.gvGridView.setAdapter(gridAdapter);
            holder.gvGridView.setOnItemClickListener(gridAdapter);
            holder.gvGridView.setOnItemLongClickListener(gridAdapter);
            month.adapter = gridAdapter;
        }

        @Override
        public int getItemCount() {
            return threeYeah.size();
        }
    }
}
