package com.dotech.ammar_rajab.goldmanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyFragment extends Fragment {
    private Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("https://www.bloomberg.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    private CurrencyApiService currencyApiService=retrofit.create(CurrencyApiService.class);
    private Observable<CurrencyData> observable=currencyApiService.getCurrData();
    public Double USD2EUR=0.0;
    public Double EUR2USD=0.0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currency, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        connectAndGetGoldPrices(view);
        prepareEditTexts(view);
    }

    public void prepareEditTexts(View view){
        final EditText USDNum=(EditText) view.findViewById(R.id.USDinNum);
        final EditText EURNum=(EditText) view.findViewById(R.id.EURinNum);
        USDNum.addTextChangedListener(new TextWatcher() {
            String result="";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0){
                    if (!EURNum.isFocused() && USDNum.isFocused())
                        EURNum.setText(String.valueOf(Double.parseDouble(s.toString())*EUR2USD));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EURNum.addTextChangedListener(new TextWatcher() {
            String result="";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0){
                    if (!USDNum.isFocused() && EURNum.isFocused())
                        USDNum.setText(String.valueOf(Double.parseDouble(s.toString())*USD2EUR));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void connectAndGetGoldPrices(final View view){
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CurrencyData>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull CurrencyData currData) {
                        ProgressBar loading=(ProgressBar) view.findViewById(R.id.loading);
                        TextView EUR=(TextView) view.findViewById(R.id.EURCurr);
                        TextView USD=(TextView) view.findViewById(R.id.USDCurr);
                        SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout) getActivity().findViewById(R.id.swipeRefreshLayout);

                        EUR.setText(String.valueOf(currData.data.eUR.uSD)+" USD");
                        USD.setText(String.valueOf(currData.data.uSD.eUR)+" EUR");
                        USD2EUR=currData.data.eUR.uSD;
                        EUR2USD=currData.data.uSD.eUR;
                        loading.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
