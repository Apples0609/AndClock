package cn.smiles.andclock.activity;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smiles.andclock.R;
import cn.smiles.andclock.entity.LotteryTypeEntity;
import cn.smiles.andclock.retrofit.LotteryTypeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 彩票
 *
 * @author kaifang
 * @date 2018/2/28 17:25
 */
public class LotteryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lv_lottery_type)
    ListView lvLotteryType;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    private List<String> lotteryStr;
    private ArrayAdapter<String> adapter;
    private SharedPreferences preferences;
    private final String lotteryTYPE = "Lottery_TYPE";
    private final String lotteryDate = "Lottery_DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
        ButterKnife.bind(this);
        setTitle("彩票种类");
        preferences = PreferenceManager.getDefaultSharedPreferences(LotteryActivity.this);

        lotteryStr = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.item_menu, R.id.tv_activity_title, lotteryStr);
        lvLotteryType.setAdapter(adapter);
        lvLotteryType.setOnItemClickListener(this);

        String sp_lottery = preferences.getString(lotteryTYPE, null);
        if (!TextUtils.isEmpty(sp_lottery)) {
            long lo_date = preferences.getLong(lotteryDate, -1);
            if (lo_date != -1 && System.currentTimeMillis() - lo_date < AlarmManager.INTERVAL_DAY * 7) {
                String[] lo_split = sp_lottery.split(",");
                lotteryStr.addAll(Arrays.asList(lo_split));
                pbProgress.setVisibility(View.GONE);
                return;
            }
        }
        requestLotteryTpye();
    }

    private void requestLotteryTpye() {
        //retrofit已经把Json解析封装在内部了 你需要传入你想要的解析工具就行了 默认支持Gson解析
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apicloud.mob.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LotteryTypeService lotterys = retrofit.create(LotteryTypeService.class);
        Call<LotteryTypeEntity> listLottery = lotterys.listLottery("23846606a9520");
        listLottery.enqueue(new Callback<LotteryTypeEntity>() {
            @Override
            public void onResponse(Call<LotteryTypeEntity> call, Response<LotteryTypeEntity> response) {
                pbProgress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    LotteryTypeEntity typeEntity = response.body();
                    if (typeEntity != null) {
                        if ("200".equals(typeEntity.getRetCode())) {
                            List<String> result = typeEntity.getResult();
                            preferences.edit().putString(lotteryTYPE, result.toString().replaceAll("\u0020", "").replaceAll("(\\[)|(\\])", "")).apply();
                            preferences.edit().putLong(lotteryDate, System.currentTimeMillis()).apply();
                            lotteryStr.addAll(result);
                            adapter.notifyDataSetChanged();
                        } else {
                            new AlertDialog.Builder(LotteryActivity.this)
                                    .setCancelable(false)
                                    .setTitle("提示")
                                    .setMessage("返回retCode!=200，msg：\n" + typeEntity.getMsg())
                                    .setPositiveButton("确定", null)
                                    .create().show();
                        }
                    }
                } else {
                    new AlertDialog.Builder(LotteryActivity.this)
                            .setCancelable(false)
                            .setTitle("提示")
                            .setMessage("response.isSuccessful()==false，msg：\n")
                            .setPositiveButton("确定", null)
                            .create().show();
                }
            }

            @Override
            public void onFailure(Call<LotteryTypeEntity> call, Throwable t) {
                pbProgress.setVisibility(View.GONE);
                new AlertDialog.Builder(LotteryActivity.this)
                        .setCancelable(false)
                        .setTitle("提示")
                        .setMessage("拉取彩票种类出错，msg：\n" + t.getMessage())
                        .setPositiveButton("确定", null)
                        .create().show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String lottery = lotteryStr.get(position);
        if (!"双色球".equals(lottery)) {
//            new AlertDialog.Builder(this)
//                    .setCancelable(false)
//                    .setTitle("提示")
//                    .setMessage("只能查询‘双色球’！")
//                    .setPositiveButton("确定", null)
//                    .create().show();
            Toast.makeText(this, "只能查询‘双色球’", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, LotteryInfoActivity.class);
        intent.putExtra("lottery", lottery);
        startActivity(intent);
    }
}
