package cn.smiles.andclock.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.IntStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smiles.andclock.R;
import cn.smiles.andclock.SmilesApplication;
import cn.smiles.andclock.adapter.ItemClickListener;

public class GameActivity extends Activity implements ItemClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.schulte_grid)
    RecyclerView schulteGrid;
    @BindView(R.id.switch2)
    Switch switch2;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_stop)
    Button btnStop;
    @BindView(R.id.game_rule)
    TextView gameRule;
    private int itemWidth;
    private List<String> itemData;
    private int tcount;
    private int timeCount;
    private MyRecyclerViewAdapter adapter;
    private SharedPreferences sps;
    private Set<String> historyTime = new LinkedHashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        switch2.setOnCheckedChangeListener(this);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int numberOfColumns = 5;
        itemWidth = metrics.widthPixels / numberOfColumns;

        itemData = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            itemData.add(String.valueOf(i));
        }
        Collections.shuffle(itemData);
        // set up the RecyclerView
        schulteGrid.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, itemData);
        adapter.setClickListener(this);
        schulteGrid.setAdapter(adapter);

        gameRule.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        gameRule.getPaint().setAntiAlias(true);
        sps = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> times = sps.getStringSet("historyTime", null);
        if (times != null) {
            historyTime.addAll(times);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (btnStart.isEnabled()) {
            Toast.makeText(this, "先点开始！", Toast.LENGTH_SHORT).show();
            return;
        }
        int ct = Integer.parseInt(itemData.get(position));
        if (tcount == 25) {
            SmilesApplication.handler.removeCallbacks(timer);
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("结束")
                    .setMessage("总用时：" + textView5.getTag() + " 秒")
                    .setPositiveButton("确定", null)
                    .create().show();
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
            historyTime.add(textView5.getTag().toString());
            sps.edit().putStringSet("historyTime", historyTime).apply();
            double total = 0;
            for (String t : historyTime) {
                double v = Double.parseDouble(t);
                total += v;
            }
            String avgTime = String.format(Locale.getDefault(), "%.1f秒", total / historyTime.size());
            sps.edit().putString("avgTime", avgTime).apply();
        } else if (ct == tcount) {
            tcount++;
        }
    }

    Runnable timer = new Runnable() {
        @Override
        public void run() {
            timeCount++;
            double t = (timeCount * 100.0) / 1000;
            String text = String.format(Locale.getDefault(), "%.1f", t);
            textView5.setTag(text);
            textView5.setText(String.format("%s 秒", text));
            SmilesApplication.handler.postDelayed(timer, 100);
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            textView5.setVisibility(View.VISIBLE);
        } else {
            textView5.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.game_rule, R.id.btn_start, R.id.btn_stop, R.id.textView5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.game_rule:
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage(getResources().getString(R.string.schulte_grid_rule_text))
                        .setPositiveButton("确定", null)
                        .create().show();
                break;
            case R.id.btn_start:
                Collections.shuffle(itemData);
                adapter.notifyDataSetChanged();
                tcount = 1;
                timeCount = 0;
                SmilesApplication.handler.postDelayed(timer, 100);
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
                break;
            case R.id.btn_stop:
                SmilesApplication.handler.removeCallbacks(timer);
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                break;
            case R.id.textView5:
                if (historyTime.isEmpty()) return;
                final String[] s1 = historyTime.toArray(new String[0]);
                String[] s2 = IntStream.rangeClosed(1, s1.length)
                        .mapToObj(i -> s1[s1.length - i])
                        .map(s -> s + "秒")
                        .toArray(String[]::new);
                System.out.println(historyTime);
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("历史用时      平均：" + sps.getString("avgTime", null))
                        .setItems(s2, null)
                        .setPositiveButton("确定", null)
                        .create().show();
                break;
        }
    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private List<String> mData;
        private LayoutInflater mInflater;
        private ItemClickListener mClickListener;

        // data is passed into the constructor
        MyRecyclerViewAdapter(Context context, List<String> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_schulte_grid, parent, false);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = itemWidth;
            params.width = itemWidth;
            view.setLayoutParams(params);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.myTextView.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView myTextView;

            ViewHolder(View itemView) {
                super(itemView);
                myTextView = (TextView) itemView.findViewById(R.id.info_text);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        // allows clicks events to be caught
        void setClickListener(ItemClickListener itemClickListener) {
            this.mClickListener = itemClickListener;
        }

    }

}
