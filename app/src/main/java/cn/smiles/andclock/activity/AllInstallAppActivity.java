package cn.smiles.andclock.activity;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.smiles.andclock.R;
import cn.smiles.andclock.adapter.MyRecyclerViewAdapter;
import cn.smiles.andclock.tools.Ktools;

public class AllInstallAppActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private View progressBar;
    private List<ApplicationInfo> allPackages;
    private MyRecyclerViewAdapter mAdapter;
    private int sortFlag = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_install_app);

        mRecyclerView = findViewById(R.id.rc_all_apps);
        progressBar = findViewById(R.id.progressBar);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        allPackages = new ArrayList<>();
        // specify an adapter (see also next example)
        mAdapter = new MyRecyclerViewAdapter(AllInstallAppActivity.this, allPackages);
        mRecyclerView.setAdapter(mAdapter);

        loadApps();

    }

    private void loadApps() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            final List<ApplicationInfo> allPackages1 = Ktools.getAllPackages(AllInstallAppActivity.this, sortFlag);
            runOnUiThread(() -> {
                allPackages.clear();
                allPackages.addAll(allPackages1);
                progressBar.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_app_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
            case R.id.app_sum:
                Toast.makeText(this, "应用总数：" + allPackages.size(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.app_sort:
                mLayoutManager.scrollToPosition(0);
                sortFlag--;
                if (sortFlag < 0)
                    sortFlag = 3;
                loadApps();
                String tipT = "不排序";
                switch (sortFlag) {
                    case 1:
                        tipT = "用户安装app在前";
                        break;
                    case 2:
                        tipT = "系统自带app在前";
                        break;
                    case 3:
                        tipT = "可启动运行APP在前";
                        break;
                }
                Toast.makeText(this, tipT, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
