package com.dotech.ammar_rajab.goldmanager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrencyData {

    @SerializedName("data")
    @Expose
    public Data data;
    @SerializedName("_expanded")
    @Expose
    public Boolean expanded;

}