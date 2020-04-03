package com.example.dell.langou;

import androidx.annotation.NonNull;

import org.apache.commons.collections4.CollectionUtils;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class Money extends DataSupport {
    private double cost_month = 0;
    private double income_month = 0;
    private double total = 0;
    private String cost;
    private String income;
    private Long id;
    private LanGou lanGou;
    private String yearMonth;

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public LanGou getLanGou() {
        return lanGou;
    }

    public void setLanGou(LanGou lanGou) {
        this.lanGou = lanGou;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getCost_month() {
        return cost_month;
    }

    public void setCost_month(double cost_month) {
        this.cost_month = cost_month;
    }

    public double getIncome_month() {
        return income_month;
    }

    public void setIncome_month(double income_month) {
        this.income_month = income_month;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    @NonNull
    @Override
    public String toString() {
        return cost +" "+ cost_month +" "+ income +" "+ income_month +" "+total +" "+ id ;
    }
}
