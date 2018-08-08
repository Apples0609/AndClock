package cn.smiles.andclock.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.game_rule)
    TextView gameRule;
    private int itemWidth;
    private List<String> itemData;
    private int tcount;
    private int timeCount;
    private MyRecyclerViewAdapter adapter;

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

    @OnClick({R.id.game_rule, R.id.btn_start})
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
