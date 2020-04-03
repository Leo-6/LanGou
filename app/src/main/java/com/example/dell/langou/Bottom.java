package com.example.dell.langou;

import androidx.annotation.NonNull;

import org.apache.commons.collections4.CollectionUtils;
import org.litepal.crud.DataSupport;


import java.util.List;

public class Bottom extends DataSupport {


    private long id;
    private String time  ;
    private String yearMonth;
    private String expand;
    private Money money;
    private String reason;
    boolean flag1;
    boolean flag2;
    private LanGou lanGou;
    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }



    public void setId(long id) {
        this.id = id;
    }

    public LanGou getLanGou() {
        return lanGou;
    }

    public void setLanGou(LanGou lanGou) {
        this.lanGou = lanGou;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Money getMoney() {
        String linkId = this.getClass().getSimpleName().toLowerCase();
        List<Money> list =DataSupport.where(linkId+"_id=?",String.valueOf(id)).find(Money.class);
        if(CollectionUtils.isEmpty(list)){
            money = null;
        }else{
            money = list.get(0);
        }
        return money;
    }

    public void setMoney(Money money) {
        money.save();
        this.money = money;
    }
    public boolean isFlag1() {
        return flag1;
    }

    public void setFlag1(boolean flag1) {
        this.flag1 = flag1;
    }

    public boolean isFlag2() {
        return flag2;
    }

    public void setFlag2(boolean flag2) {
        this.flag2 = flag2;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    @NonNull
    @Override
    public String toString() {
        return time+"  "+expand+"  "+reason+"  ";
    }
}

