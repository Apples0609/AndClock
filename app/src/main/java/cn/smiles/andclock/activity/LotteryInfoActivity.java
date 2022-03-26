package cn.smiles.andclock.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smiles.andclock.R;
import cn.smiles.andclock.entity.LotterySSQEntity;
import cn.smiles.andclock.retrofit.LotteryInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LotteryInfoActivity extends AppCompatActivity implements OnPhotoTapListener {

    @BindView(R.id.tv_lottery_date)
    TextView tvLotteryDate;
    @BindView(R.id.tv_award_deadline)
    TextView tvAwardDeadline;
    @BindView(R.id.tv_periods)
    TextView tvPeriods;
    @BindView(R.id.tv_red_one)
    TextView tvRedOne;
    @BindView(R.id.tv_red_two)
    TextView tvRedTwo;
    @BindView(R.id.tv_red_three)
    TextView tvRedThree;
    @BindView(R.id.tv_red_four)
    TextView tvRedFour;
    @BindView(R.id.tv_red_five)
    TextView tvRedFive;
    @BindView(R.id.tv_red_six)
    TextView tvRedSix;
    @BindView(R.id.tv_blue_one)
    TextView tvBlueOne;
    @BindView(R.id.tv_first_prize)
    TextView tvFirstPrize;
    @BindView(R.id.tv_first_bonus)
    TextView tvFirstBonus;
    @BindView(R.id.tv_second_prize)
    TextView tvSecondPrize;
    @BindView(R.id.tv_second_bonus)
    TextView tvSecondBonus;
    @BindView(R.id.tv_third_prize)
    TextView tvThirdPrize;
    @BindView(R.id.tv_third_bonus)
    TextView tvThirdBonus;
    @BindView(R.id.tv_fourth_prize)
    TextView tvFourthPrize;
    @BindView(R.id.tv_fourth_bonus)
    TextView tvFourthBonus;
    @BindView(R.id.tv_fifth_prize)
    TextView tvFifthPrize;
    @BindView(R.id.tv_fifth_bonus)
    TextView tvFifthBonus;
    @BindView(R.id.tv_six_prize)
    TextView tvSixPrize;
    @BindView(R.id.tv_six_bonus)
    TextView tvSixBonus;
    @BindView(R.id.tv_sales_quantity)
    TextView tvSalesQuantity;
    @BindView(R.id.tv_jackpot)
    TextView tvJackpot;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tv_ssq_rule)
    TextView tvSsqRule;
    @BindView(R.id.iv_ssq_rule)
    PhotoView ivSsqRule;
    private String lottery;
    private TextView[] tvsBall;
    private TextView[] tvsPrize;
    private TextView[] tvsBonus;
    private String yearLottery;
    private int periodLottery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_info);
        ButterKnife.bind(this);
        lottery = getIntent().getStringExtra("lottery");
        setTitle(lottery);
        tvsBall = new TextView[]{tvRedOne, tvRedTwo, tvRedThree, tvRedFour, tvRedFive, tvRedSix, tvBlueOne};
        tvsPrize = new TextView[]{tvFirstPrize, tvSecondPrize, tvThirdPrize, tvFourthPrize, tvFifthPrize, tvSixPrize};
        tvsBonus = new TextView[]{tvFirstBonus, tvSecondBonus, tvThirdBonus, tvFourthBonus, tvFifthBonus, tvSixBonus};

        runALottery("");
//        Uri uri = new Uri.Builder()
//                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
//                .path(String.valueOf(R.drawable.ssq_rule))
//                .build();
//        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.ssq_rule).build();
        ivSsqRule.setImageResource(R.drawable.ssq_rule);
        ivSsqRule.setOnPhotoTapListener(this);
    }

    private void runALottery(String period) {
        pbLoading.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apicloud.mob.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LotteryInfoService lotteryService = retrofit.create(LotteryInfoService.class);
        Call<LotterySSQEntity> lotteryInfo = lotteryService.getSSQLottery("23846606a9520", lottery, period);
        lotteryInfo.enqueue(new Callback<LotterySSQEntity>() {
            @Override
            public void onResponse(Call<LotterySSQEntity> call, Response<LotterySSQEntity> response) {
                pbLoading.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    LotterySSQEntity lotteryInfo = response.body();
                    if (lotteryInfo != null) {
                        if ("200".equals(lotteryInfo.getRetCode())) {
                            LotterySSQEntity.ResultBean result = lotteryInfo.getResult();
                            String awardDateTime = result.getAwardDateTime();
                            try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                Date date = dateFormat.parse(awardDateTime);
                                SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年MM月dd HH:mm", Locale.getDefault());
                                String dateStr = dateFormat2.format(date);
                                tvLotteryDate.setText(getResources().getString(R.string.lottery_date, dateStr));
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                calendar.add(Calendar.DAY_OF_MONTH, 60);
                                calendar.set(Calendar.HOUR_OF_DAY, 0);
                                calendar.set(Calendar.MINUTE, 0);
                                String dateStr2 = dateFormat2.format(calendar.getTime());
                                tvAwardDeadline.setText(getResources().getString(R.string.award_deadline, dateStr2));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String period = result.getPeriod();
                            yearLottery = period.substring(0, 4);
                            periodLottery = Integer.parseInt(period.substring(4));
                            tvPeriods.setText(getResources().getString(R.string.periods, period));
                            int pool = result.getPool();
                            DecimalFormat format = new DecimalFormat(",###");
                            tvJackpot.setText(getResources().getString(R.string.jackpot, format.format(pool)));
                            int sales = result.getSales();
                            String quantity = format.format(sales);
                            int saleNote = sales / 2;
                            String quantity2 = format.format(saleNote);
                            tvSalesQuantity.setText(getResources().getString(R.string.sales_quantity, quantity, quantity2));
                            List<String> lotteryNumber = result.getLotteryNumber();
                            for (int i = 0; i < lotteryNumber.size(); i++) {
                                String ball = lotteryNumber.get(i);
                                tvsBall[i].setText(ball);
                            }
                            List<LotterySSQEntity.ResultBean.LotteryDetailsBean> lotteryDetails = result.getLotteryDetails();
                            for (int i = 0; i < lotteryDetails.size(); i++) {
                                LotterySSQEntity.ResultBean.LotteryDetailsBean detailsBean = lotteryDetails.get(i);
                                int awardNumber = detailsBean.getAwardNumber();
                                tvsPrize[i].setText(format.format(awardNumber));
                                int awardPrice = detailsBean.getAwardPrice();
                                tvsBonus[i].setText(format.format(awardPrice));
                            }
                        } else {
                            new AlertDialog.Builder(LotteryInfoActivity.this)
                                    .setCancelable(false)
                                    .setTitle("提示")
                                    .setMessage("返回retCode!=200，msg：\n" + lotteryInfo.getMsg())
                                    .setPositiveButton("确定", null)
                                    .create().show();
                        }
                    }
                } else {
                    new AlertDialog.Builder(LotteryInfoActivity.this)
                            .setCancelable(false)
                            .setTitle("提示")
                            .setMessage("response.isSuccessful()==false，msg：\n")
                            .setPositiveButton("确定", null)
                            .create().show();
                }
            }

            @Override
            public void onFailure(Call<LotterySSQEntity> call, Throwable t) {
                pbLoading.setVisibility(View.GONE);
                new AlertDialog.Builder(LotteryInfoActivity.this)
                        .setTitle("提示")
                        .setMessage("请求发生错误了，msg：\n" + t.getMessage())
                        .setPositiveButton("确定", null)
                        .create().show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lottery_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (pbLoading.getVisibility() == View.VISIBLE) return true;
        switch (item.getItemId()) {
            case R.id.myeah_select:
                Calendar curr = Calendar.getInstance();
                int year = curr.get(Calendar.YEAR);
                List<String> years = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    years.add(String.valueOf(year - i));
                }
                final String[] yearArrs = years.toArray(new String[years.size()]);
                new AlertDialog.Builder(this)
                        .setTitle("选择年份")
                        .setItems(yearArrs, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                yearLottery = yearArrs[which];

                                runALottery(yearLottery + getPeriod(periodLottery));
                            }
                        })
                        .create().show();
                break;
            case R.id.mfront_result:
                int period = periodLottery - 1;
                if (period < 1) {
                    new AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setTitle("提示")
                            .setMessage("没有上一期了！！！")
                            .setPositiveButton("确定", null)
                            .create().show();
                    break;
                }
                runALottery(yearLottery + getPeriod(period));
                break;
            case R.id.mback_result:
                int period2 = periodLottery + 1;
                runALottery(yearLottery + getPeriod(period2));
                break;
        }
        return true;
    }

    private String getPeriod(int period) {
        return String.format(Locale.getDefault(), "%03d", period);
    }

    @OnClick({R.id.tv_ssq_rule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_ssq_rule:
                ivSsqRule.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        ivSsqRule.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (ivSsqRule.getVisibility() == View.VISIBLE) {
            ivSsqRule.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }
}
