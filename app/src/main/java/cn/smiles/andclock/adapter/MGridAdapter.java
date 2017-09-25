package cn.smiles.andclock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smiles.andclock.R;
import cn.smiles.andclock.entity.MDate;
import cn.smiles.andclock.entity.MMonth;
import cn.smiles.andclock.tools.CalendarTools;

/**
 * 日历月份item 适配器
 *
 * @author kaifang
 * @date 2017/9/25 14:33
 */
public class MGridAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private final LayoutInflater inflater;
    private final List<MDate> month;
    private final CalendarTools calendarTools;

    public MGridAdapter(Context context, List<MDate> month, CalendarTools calendarTools) {
        inflater = LayoutInflater.from(context);
        this.month = month;
        this.calendarTools = calendarTools;
    }

    @Override
    public int getCount() {
        return month.size();
    }

    @Override
    public MDate getItem(int position) {
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
        MDate mDate = getItem(position);
        if (mDate.date == null) {
            viewHolder.tvDay.setActivated(false);
            viewHolder.tvDay.setText(null);
            viewHolder.tvDay.setSelected(false);
        } else {
            viewHolder.tvDay.setActivated(mDate.isToday);
            viewHolder.tvDay.setText(String.valueOf(mDate.day));
            viewHolder.tvDay.setSelected(mDate.isChecked);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MDate mDate = getItem(position);
        if (mDate.date != null) {
            calendarTools.setCheck_date(mDate);
            MDate start_date = calendarTools.getCheck_start_date();
            MDate end_date = calendarTools.getCheck_end_date();
            List<MMonth> mcalendars = calendarTools.getMcalendars();
            int checked_sum = 0;
            for (MMonth mcalendar : mcalendars) {
                if (mcalendar.adapter != null) {
                    for (MDate date : mcalendar.dates) {
                        if (date.date == null) continue;
                        if (start_date == null && end_date == null) {
                            date.isChecked = false;
                        } else {
                            if (start_date != null && end_date != null) {
                                date.isChecked = date.date.getTime() >= start_date.date.getTime()
                                        && date.date.getTime() <= end_date.date.getTime();
                            } else {
                                date.isChecked = date == mDate;
                            }
                        }
                        if (date.isChecked) checked_sum++;
                        mcalendar.adapter.notifyDataSetChanged();
                    }
                }
            }
            if (calendarTools.listener != null) calendarTools.listener.onOperator(checked_sum);
        }
    }

    class ViewHolder {
        @BindView(R.id.tv_day)
        TextView tvDay;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
