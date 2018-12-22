package com.dotech.ammar_rajab.goldmanager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ammar-rajab on 04/04/2018.
 */

public class Item {

    @SerializedName("curr")
    @Expose
    public String curr;
    @SerializedName("xauPrice")
    @Expose
    public Double xauPrice;
    @SerializedName("xagPrice")
    @Expose
    public Double xagPrice;
    @SerializedName("chgXau")
    @Expose
    public Double chgXau;
    @SerializedName("chgXag")
    @Expose
    public Double chgXag;
    @SerializedName("pcXau")
    @Expose
    public Double pcXau;
    @SerializedName("pcXag")
    @Expose
    public Double pcXag;
    @SerializedName("xauClose")
    @Expose
    public Double xauClose;
    @SerializedName("xagClose")
    @Expose
    public Double xagClose;

}