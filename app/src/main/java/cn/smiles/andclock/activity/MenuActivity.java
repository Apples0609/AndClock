package cn.smiles.andclock.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dtr.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smiles.andclock.R;

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lv_menu)
    ListView lvMenu;
    private List<MenuEntity> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        setTitle("主菜单");

        datas = new ArrayList<>();
        datas.add(new MenuEntity("舒尔特方格", GameActivity.class));
        datas.add(new MenuEntity("APP列表", AllInstallAppActivity.class));
        datas.add(new MenuEntity("扫描二维码", CaptureActivity.class));
        datas.add(new MenuEntity("打砖块游戏", BrickActivity.class));
        datas.add(new MenuEntity("彩票查询", LotteryActivity.class));
        datas.add(new MenuEntity("简单日历", CalendarActivity.class));
        datas.add(new MenuEntity("Web浏览器", WebViewActivity.class));
        datas.add(new MenuEntity("安卓辅助", GoHomeActivity.class));
        datas.add(new MenuEntity("=测试学习=", TestStudyActivity.class));
        MenuAdapter adapter = new MenuAdapter(this, datas);
        lvMenu.setAdapter(adapter);
        lvMenu.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MenuEntity entity = datas.get(position);
        Intent intent = new Intent(this, entity.clazz);
        startActivity(intent);
    }

    class MenuAdapter extends BaseAdapter {

        private final LayoutInflater inflater;
        private final List<MenuEntity> datas;

        private MenuAdapter(Context context, List<MenuEntity> datas) {
            inflater = LayoutInflater.from(context);
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public MenuEntity getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = inflater.inflate(R.layout.item_menu, parent, false);
            MenuEntity entity = getItem(position);
            TextView aTitle = (TextView) convertView.findViewById(R.id.tv_activity_title);
            aTitle.setText(entity.title);
            return convertView;
        }
    }

    class MenuEntity {
        String title;
        Class<? extends Activity> clazz;

        private MenuEntity(String title, Class<? extends Activity> clazz) {
            this.title = title;
            this.clazz = clazz;
        }
    }
}
