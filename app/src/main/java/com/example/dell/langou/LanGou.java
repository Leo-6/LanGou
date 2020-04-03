package com.example.dell.langou;

import androidx.annotation.NonNull;

import org.apache.commons.collections4.CollectionUtils;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class LanGou extends DataSupport {
    private  long id;
    private String yearMonth;//自己创建的外键
    private Money money;

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    private List<Bottom>bottomList = new ArrayList<Bottom>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public List<Bottom> getBottomList() {
        String linkId = this.getClass().getSimpleName().toLowerCase();
        List<Bottom>bottomList = DataSupport.where(linkId+"_id=?",String.valueOf(id)).find(Bottom.class);
        if(bottomList == null){
            bottomList = new ArrayList<>();
        }
        return bottomList;
    }

    public void setBottomList(List<Bottom> bottomList) {
        /*if(!CollectionUtils.isEmpty(bottomList)){
            DataSupport.saveAll(bottomList);
        }*/
        this.bottomList = bottomList;
    }

    @NonNull
    @Override
    public String toString() {
        return  id+"  "+yearMonth+" "+DataSupport.findLast(Money.class).toString();
    }
}
