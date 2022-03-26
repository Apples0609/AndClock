package cn.smiles.andclock.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.smiles.andclock.databinding.ActivityApkinstallBinding;

public class APKInstallActivity extends AppCompatActivity {

    private LinkedList<String> linkedList;
    private List<Map<String, String>> data;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityApkinstallBinding binding = ActivityApkinstallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        linkedList = new LinkedList<>();
        data = new ArrayList<>();
        adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,
                new String[]{"item1", "item2"}, new int[]{android.R.id.text1, android.R.id.text2});
        binding.lvList.setAdapter(adapter);

        String directory = Environment.getExternalStorageDirectory().getAbsolutePath();
        showItems(directory);
        binding.lvList.setOnItemClickListener((adapterView, view, i, l) -> {
            Map<String, String> map = data.get(i);
            String item2 = map.get("item2");
            if ("目录".equals(item2)) {
                String item3 = Objects.requireNonNull(map.get("item3"));
                showItems(item3);
                linkedList.add(map.get("item4"));
            } else {
                String item1 = map.get("item1");
                assert item1 != null;
                if (item1.toLowerCase().endsWith(".apk")) {
                    String path = Objects.requireNonNull(map.get("item3"));
                    /*Intent intent = new Intent(Intent.ACTION_VIEW);
                    File apkFile = new File(path);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", apkFile);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    startActivity(intent);*/
                    installApk(path);
                }
            }
        });
    }

    /**
     * 下载成功后安装apk
     *
     * @param path 安装文件
     */
    public void installApk(String path) {
        //此处file应为apk下载的完整路径文件，这里下载在了外部存储的私有缓存文件夹下，
        // 你可以选择其他存储地址如：外置SD卡等，但是要注意与file_paths.xml中<external-cache-path>，<files-path>......里的
        //path="myApkFile/" 属性共享的文件夹目录对应一致
        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //判读版本是否在7.0以上
        // 参数1 上下文, 参数2 Provider主机地址 和清单配置文件中保持一致
        // 参数2 = android:authorities="应用包名.fileprovider"属性值
        // 参数3 = 上一步中共享的apk文件
        Uri apkUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void showItems(String pf) {
        File file = new File(pf);
        File[] files1 = file.listFiles(pathname -> !pathname.getName().startsWith("."));
        if (files1 == null || files1.length == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("空目录")
                    .setPositiveButton("确定", null)
                    .create().show();
            return;
        }
        Arrays.sort(files1, (o1, o2) -> Long.compare(o2.lastModified(), o1.lastModified()));
        data.clear();
        for (File f : files1) {
            System.out.println(f.getName());
            LinkedHashMap<String, String> m = new LinkedHashMap<>();
            m.put("item1", f.getName());
            m.put("item2", f.isDirectory() ? "目录" : "文件");
            m.put("item3", f.getAbsolutePath());
            m.put("item4", pf);
            data.add(m);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (linkedList.isEmpty()) {
            super.onBackPressed();
        } else {
            String s = linkedList.removeLast();
            showItems(s);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}