package com.dotech.ammar_rajab.goldmanager;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class GoldFragment extends Fragment {

    private Calculator calc=new Calculator();
    private Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("https://data-asg.goldprice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    private GoldApiService goldApiService=retrofit.create(GoldApiService.class);
    private Observable<GoldData> observable=goldApiService.getGoldPrices();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gold, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        connectAndGetGoldPrices(view);
    }

    public void connectAndGetGoldPrices(final View view){

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoldData>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull GoldData goldData) {
                        ProgressBar loading=(ProgressBar) view.findViewById(R.id.loading);
                        ImageView stat=(ImageView) view.findViewById(R.id.stat);
                        TextView data=(TextView) view.findViewById(R.id.ounceGold);
                        TextView dataGr=(TextView) view.findViewById(R.id.gramGold);
                        ImageView arrow=(ImageView) view.findViewById(R.id.change);
                        TextView changeLabel=(TextView) view.findViewById(R.id.changeLabel);
                        SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout) getActivity().findViewById(R.id.swipeRefreshLayout);

                        Double changeNum=0.0;
                        changeNum=goldData.items.get(0).chgXau;

                        data.setText(String.valueOf(goldData.items.get(0).xauPrice)+" USD");
                        dataGr.setText(String.valueOf(calc.OZ_to_GR(goldData.items.get(0).xauPrice))+" USD");
                        if (changeNum>0){
                            arrow.setVisibility(View.VISIBLE);
                            arrow.setColorFilter(0xFF00FF00);
                            arrow.setRotation(0);
                            changeLabel.setTextColor(0xFF0AEE0A);
                            changeLabel.setText("+ "+String.valueOf(changeNum));
                        }
                        else{
                            arrow.setVisibility(View.VISIBLE);
                            arrow.setColorFilter(0xFFEE0A0A);
                            arrow.setRotation(180);
                            changeLabel.setTextColor(0xFFFF0000);
                            changeLabel.setText(String.valueOf(changeNum));
                        }

                        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(getContext());
                        if (prefs.getBoolean("switch_chang_load",true)){
                            Picasso.with(getContext())
                                    .load("https://goldprice.org/charts/gold-price-performance-USD_x.png")
                                    .into(stat);
                        }
                        loading.setVisibility(View.INVISIBLE);
                        stat.setVisibility(View.VISIBLE);
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
