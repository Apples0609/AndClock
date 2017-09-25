package cn.smiles.andclock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smiles.andclock.R;
import cn.smiles.andclock.entity.MMonth;
import cn.smiles.andclock.tools.CalendarTools;
import cn.smiles.andclock.view.MGridView;

/**
 * 日历list列表适配器
 *
 * @author kaifang
 * @date 2017/9/25 14:33
 */
public class MListAdapter extends BaseAdapter {

    private final List<MMonth> threeYeah;
    private final LayoutInflater inflater;
    private final Context context;
    private final CalendarTools calendarTools;

    public MListAdapter(Context context, CalendarTools calendarTools) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.threeYeah = calendarTools.getMcalendars();
        this.calendarTools = calendarTools;
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
        MGridAdapter gridAdapter = new MGridAdapter(context, month.dates, calendarTools);
        viewHolder.gvGridView.setAdapter(gridAdapter);
        viewHolder.gvGridView.setOnItemClickListener(gridAdapter);
        month.adapter = gridAdapter;
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_yeah_month)
        TextView tvYeahMonth;
        @BindView(R.id.gv_grid_view)
        MGridView gvGridView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
