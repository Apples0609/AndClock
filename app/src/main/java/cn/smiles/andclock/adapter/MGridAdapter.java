package cn.smiles.andclock.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
public class MGridAdapter extends BaseAdapter implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private final LayoutInflater inflater;
    private final CalendarTools calendarTools;
    private final MMonth month;
    private final Context context;

    public MGridAdapter(Context context, MMonth month, CalendarTools calendarTools) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.calendarTools = calendarTools;
        this.month = month;
    }

    @Override
    public int getCount() {
        return month.dates.size();
    }

    @Override
    public MDate getItem(int position) {
        return month.dates.get(position);
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
            viewHolder.tvDay.setText(null);
            viewHolder.tvNongli.setText(null);
        } else {
            viewHolder.tvDay.setText(String.valueOf(mDate.day));
            viewHolder.tvNongli.setText(mDate.nongli);
        }
        viewHolder.tvDay.setActivated(mDate.isToday);
        convertView.setEnabled(!mDate.isChecked);
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        MDate mDate = getItem(position);
        Toast toast = Toast.makeText(context, mDate.dateInfo, Toast.LENGTH_LONG);
        ((TextView) toast.getView().findViewById(android.R.id.message)).setGravity(Gravity.CENTER_HORIZONTAL);
        toast.show();
        return true;
    }

    class ViewHolder {
        @BindView(R.id.tv_day)
        TextView tvDay;
        @BindView(R.id.tv_day_nongli)
        TextView tvNongli;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
