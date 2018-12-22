package com.dotech.ammar_rajab.goldmanager;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ammar-rajab on 04/04/2018.
 */

public interface GoldApiService {
    @GET("dbXRates/USD")
    Observable<GoldData> getGoldPrices();

    @GET("dbXRates/USD")
    Call<GoldData> getGoldPricesCall();
}
