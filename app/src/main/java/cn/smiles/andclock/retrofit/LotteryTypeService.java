package cn.smiles.andclock.retrofit;

import cn.smiles.andclock.entity.LotteryTypeEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LotteryTypeService {
    @GET("/lottery/list")
    Call<LotteryTypeEntity> listLottery(@Query("key") String key);
}
