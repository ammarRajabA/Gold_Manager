package com.dotech.ammar_rajab.goldmanager;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ammar-rajab on 19/04/2018.
 */

public interface CurrencyApiService {
    @GET("markets/api/security/currency/cross-rates/USD,EUR")
    Observable<CurrencyData> getCurrData();

    @GET("markets/api/security/currency/cross-rates/USD,EUR")
    Call<CurrencyData> getCurrDataCall();
}
