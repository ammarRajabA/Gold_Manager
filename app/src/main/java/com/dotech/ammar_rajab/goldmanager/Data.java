package com.dotech.ammar_rajab.goldmanager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("USD")
    @Expose
    public USD uSD;
    @SerializedName("EUR")
    @Expose
    public EUR eUR;

}
