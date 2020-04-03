package com.example.dell.langou;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {
    private static String yearMonth ="";
    private static String data = "";
    public static String getyearMonth() {
        return yearMonth;
    }

    public static void setyearMonth(String yearMonth) {
        Data.yearMonth = yearMonth;
    }
    public static String getData(){
        return data;
    }
    public  static void  setData(String data){
        Data.data = data;
    }
}
