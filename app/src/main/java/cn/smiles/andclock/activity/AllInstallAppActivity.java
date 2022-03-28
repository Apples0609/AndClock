package cn.smiles.andclock.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import cn.smiles.andclock.R;
import cn.smiles.andclock.adapter.MyRecyclerViewAdapter;
import cn.smiles.andclock.tools.Ktools;

public class AllInstallAppActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private LinearLayoutManager mLayoutManager;
    private View progressBar;
    private List<ApplicationInfo> allPackages;
    List<ApplicationInfo> packages;
    private MyRecyclerViewAdapter mAdapter;
    private int sortFlag = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_install_app);

        RecyclerView mRecyclerView = findViewById(R.id.rc_all_apps);
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

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        allPackages = new ArrayList<>();
        packages = new ArrayList<>();
        // specify an adapter (see also next example)
        mAdapter = new MyRecyclerViewAdapter(AllInstallAppActivity.this, packages);
        mRecyclerView.setAdapter(mAdapter);

        loadApps();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadApps() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            final List<ApplicationInfo> allPackages1 = Ktools.getAllPackages(AllInstallAppActivity.this, sortFlag);
            runOnUiThread(() -> {
                packages.addAll(allPackages1);
                allPackages.addAll(allPackages1);
                progressBar.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_app_menu, menu);

        //https://stackoverflow.com/questions/21585326/implementing-searchview-in-action-bar
        /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);*/

        MenuItem searchItem = menu.findItem(R.id.app_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("关键字");
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(true);
        searchItem.setOnActionExpandListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == R.id.app_sum) {
            Toast.makeText(this, "应用总数：" + allPackages.size(), Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.app_sort) {
            mLayoutManager.scrollToPosition(0);
            sortFlag--;
            if (sortFlag < 0)
                sortFlag = 3;
            sortPackages();
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
        } else if (itemId == R.id.app_search) {
            Toast.makeText(this, "app_search", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 排序所有已安装APP包名
     *
     * @return
     */
    @SuppressLint("NotifyDataSetChanged")
    public void sortPackages() {
        PackageManager pm = getPackageManager();
        switch (sortFlag) {
            case 1:
                allPackages.sort(Comparator.comparingInt(o -> (o.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM))));
                break;
            case 2:
                allPackages.sort((o1, o2) -> Integer.compare((o2.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)),
                        (o1.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM))));
                break;
            case 3:
                allPackages.sort((o1, o2) -> {
                    Intent launchIntentForPackage1 = pm.getLaunchIntentForPackage(o1.packageName);
                    Intent launchIntentForPackage2 = pm.getLaunchIntentForPackage(o2.packageName);
                    if (launchIntentForPackage2 == null) {
                        return (launchIntentForPackage1 == null) ? 0 : -1;
                    }
                    if (launchIntentForPackage1 == null) {
                        return 1;
                    }
                    return 2;
                });
                break;
        }
        packages.addAll(allPackages);
        mAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onQueryTextSubmit(String query) {
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> tep = new ArrayList<>();
        for (ApplicationInfo info : allPackages) {
            String name = info.loadLabel(pm).toString();
            String pName = info.packageName;
            if (name.contains(query) || pName.contains(query)) {
                tep.add(info);
            }
        }
        if (tep.isEmpty()) {
            Toast.makeText(this, "搜索结果为空", Toast.LENGTH_LONG).show();
            return true;
        }
        packages.clear();
        packages.addAll(tep);
        mAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        packages.clear();
        packages.addAll(allPackages);
        mAdapter.notifyDataSetChanged();
        return true;
    }
}
