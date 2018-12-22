package com.dotech.ammar_rajab.goldmanager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ammar-rajab on 04/04/2018.
 */
public class GoldData {

    @SerializedName("ts")
    @Expose
    public Long ts;
    @SerializedName("tsj")
    @Expose
    public Long tsj;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("items")
    @Expose
    public List<Item> items = null;

}