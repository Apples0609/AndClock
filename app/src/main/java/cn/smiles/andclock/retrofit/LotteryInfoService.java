package cn.smiles.andclock.retrofit;

import cn.smiles.andclock.entity.LotteryDLTEntity;
import cn.smiles.andclock.entity.LotterySSQEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 双色球
 *
 * @author kaifang
 * @date 2018/3/2 12:17
 */
public interface LotteryInfoService {
    @GET("/lottery/query")
    Call<LotterySSQEntity> getSSQLottery(@Query("key") String key, @Query("name") String name, @Query("period") String period);

    @GET("/lottery/query")
    Call<LotteryDLTEntity> getDLTLottery(@Query("key") String key, @Query("name") String name, @Query("period") String period);
}
