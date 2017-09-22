package cn.smiles.andclock;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smiles.andclock.entity.MMonth;
import cn.smiles.andclock.tools.CalendarTools;

public class CalendarActivity extends AppCompatActivity {

    @BindView(R.id.lv_list_view)
    ListView lvListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        List<MMonth> threeYeah = CalendarTools.getThreeYeah();
        MListAdapter listAdapter = new MListAdapter(this, threeYeah);
        lvListView.setAdapter(listAdapter);
    }

    class MListAdapter extends BaseAdapter {

        private final List<MMonth> threeYeah;
        private final LayoutInflater inflater;
        private final Context context;

        public MListAdapter(Context context, List<MMonth> threeYeah) {
            this.context = context;
            this.threeYeah = threeYeah;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return threeYeah.size();
        }

        @Override
        public MMonth getItem(int position) {
            return threeYeah.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            MMonth month = getItem(position);
            viewHolder.tvYeahMonth.setText(month.yeah + "年" + month.month + "月");
            MGridAdapter gridAdapter = new MGridAdapter(context, month.dates);
            viewHolder.gvGridView.setAdapter(gridAdapter);
            viewHolder.gvGridView.setOnItemClickListener(gridAdapter);
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tv_yeah_month)
            TextView tvYeahMonth;
            @BindView(R.id.gv_grid_view)
            GridView gvGridView;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }

        }
    }

    class MGridAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private final LayoutInflater inflater;
        private final List<MMonth.MDate> month;

        public MGridAdapter(Context context, List<MMonth.MDate> month) {
            inflater = LayoutInflater.from(context);
            this.month = month;
        }

        @Override
        public int getCount() {
            return month.size();
        }

        @Override
        public MMonth.MDate getItem(int position) {
            return month.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            MMonth.MDate date = getItem(position);
            viewHolder.tvDay.setText(CalendarTools.dateFormat.format(date.date));
            viewHolder.tvDay.setSelected(date.isChecked);
            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MMonth.MDate mDate = getItem(position);
            mDate.isChecked = !mDate.isChecked;
            notifyDataSetChanged();
        }

        class ViewHolder {
            @BindView(R.id.tv_day)
            TextView tvDay;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
