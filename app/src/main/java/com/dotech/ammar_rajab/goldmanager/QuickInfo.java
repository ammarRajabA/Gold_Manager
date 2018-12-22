package com.dotech.ammar_rajab.goldmanager;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Implementation of App Widget functionality.
 */
public class QuickInfo extends AppWidgetProvider {
    private static Calculator calc=new Calculator();
    private static Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("https://data-asg.goldprice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    private static GoldApiService goldApiService=retrofit.create(GoldApiService.class);

    private static Retrofit retrofitCurr=new Retrofit.Builder()
            .baseUrl("https://www.bloomberg.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    private static CurrencyApiService currencyApiService=retrofitCurr.create(CurrencyApiService.class);

    private static final String SYNC_CLICKED    = "automaticWidgetSyncButtonClick";

    static void updateAppWidget(final Context context,final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {
        final RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.quick_info);

        Call<GoldData> call = goldApiService.getGoldPricesCall();
        call.enqueue(new Callback<GoldData>() {
            @Override
            public void onResponse(Call<GoldData> call, Response<GoldData> response) {
                Bitmap upArrow= BitmapFactory.decodeResource(context.getResources(),R.drawable.uparrow);
                Bitmap downArrow= BitmapFactory.decodeResource(context.getResources(),R.drawable.downarrow);

                // Gold
                views.setTextViewText(R.id.GoldGramWidgetText,String.valueOf(calc.OZ_to_GR(response.body().items.get(0).xauPrice))+" USD");
                Double changeNum=0.0;
                changeNum=response.body().items.get(0).chgXau;
                if (changeNum>0){
                    views.setBitmap(R.id.GoldArrowWidget,"setImageBitmap",upArrow);
                }
                else{
                    views.setBitmap(R.id.GoldArrowWidget,"setImageBitmap",downArrow);
                }

                // Silver
                views.setTextViewText(R.id.SilverGramWidgetText,String.valueOf(calc.OZ_to_GR(response.body().items.get(0).xagPrice))+" USD");
                changeNum=response.body().items.get(0).chgXag;
                if (changeNum>0){
                    views.setBitmap(R.id.SilverArrowWidget,"setImageBitmap",upArrow);
                }
                else{
                    views.setBitmap(R.id.SilverArrowWidget,"setImageBitmap",downArrow);
                }

                // Currency image
                Bitmap currency= BitmapFactory.decodeResource(context.getResources(),R.drawable.curr);
                views.setBitmap(R.id.USDEURWidget,"setImageBitmap",currency);

                Bitmap refresh= BitmapFactory.decodeResource(context.getResources(),R.drawable.refresh);
                views.setBitmap(R.id.refresh,"setImageBitmap",refresh);
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void onFailure(Call<GoldData> call, Throwable t) {

            }
        });

        // Currency
        Call<CurrencyData> currCall = currencyApiService.getCurrDataCall();
        currCall.enqueue(new Callback<CurrencyData>() {
            @Override
            public void onResponse(Call<CurrencyData> call, Response<CurrencyData> response) {
                views.setViewVisibility(R.id.refreshing,View.VISIBLE);
                views.setViewVisibility(R.id.refresh, View.INVISIBLE);
                views.setProgressBar(R.id.refreshing,100,0,true);
                appWidgetManager.updateAppWidget(appWidgetId, views);

                views.setTextViewText(R.id.USDEURWidgetText,String.valueOf(response.body().data.uSD.eUR)+" EUR");
                appWidgetManager.updateAppWidget(appWidgetId, views);

                views.setViewVisibility(R.id.refreshing,View.INVISIBLE);
                views.setViewVisibility(R.id.refresh, View.VISIBLE);
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void onFailure(Call<CurrencyData> call, Throwable t) {

            }
        });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.quick_info);
        watchWidget = new ComponentName(context, QuickInfo.class);

        remoteViews.setOnClickPendingIntent(R.id.refresh, getPendingSelfIntent(context, SYNC_CLICKED,appWidgetIds));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context,Intent intent){
        // TODO Auto-generated method stub
        super.onReceive(context, intent);

        if (SYNC_CLICKED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            onUpdate(context, appWidgetManager, intent.getExtras().getIntArray("QuickInfo_IDs"));
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action, int[] ids) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        intent.putExtra("QuickInfo_IDs",ids);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

