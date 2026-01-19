package com.ashgan.bezeqDemo.Util;

public class Util {

    public static double formatToTwoDecimals(double value) {
        return Double.parseDouble(String.format("%.2f", value));
    }
}
