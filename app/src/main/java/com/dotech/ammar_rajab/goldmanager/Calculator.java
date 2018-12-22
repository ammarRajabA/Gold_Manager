package com.dotech.ammar_rajab.goldmanager;

/**
 * Created by ammar-rajab on 04/04/2018.
 */

public class Calculator {
    public Double OZ_to_GR(Double oz){
        Double c=31.1034807;
        return Math.round((oz/c)*100.0)/100.0;
    }
}
