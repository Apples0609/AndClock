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
import cn.smiles.andclock.entity.MMonth;
import cn.smiles.andclock.tools.CalendarTools;

public class MGridAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private final LayoutInflater inflater;
    private final List<MMonth.MDate> month;
    private final CalendarTools calendarTools;

    public MGridAdapter(Context context, List<MMonth.MDate> month, CalendarTools calendarTools) {
        inflater = LayoutInflater.from(context);
        this.month = month;
        this.calendarTools = calendarTools;
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
        MMonth.MDate mDate = getItem(position);
        if (mDate.date == null) {
            viewHolder.tvDay.setText(null);
            viewHolder.tvDay.setSelected(false);
        } else {
            if (mDate.isToday) {
                viewHolder.tvDay.setEnabled(false);
            } else {
                viewHolder.tvDay.setEnabled(true);
            }
            viewHolder.tvDay.setText(String.valueOf(mDate.day));
            viewHolder.tvDay.setSelected(mDate.isChecked);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MMonth.MDate mDate = getItem(position);
        if (mDate.date != null) {
            mDate.isChecked = !mDate.isChecked;
            notifyDataSetChanged();
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
