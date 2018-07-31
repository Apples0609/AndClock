package cn.smiles.andclock.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SSQ500Service {
    /**
     * @param start
     * @param end
     * @param sort  1顺序排列 0倒序排列
     * @return
     */
    @GET("ssq/history/newinc/history.php")
    Call<String> getHtml(@Query("start") String start, @Query("end") String end, @Query("sort") String sort);
}
