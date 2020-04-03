package com.example.dell.langou;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;


import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.exceptions.DataSupportException;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.qqtheme.framework.widget.WheelView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private TextView income;
    private TextView incomeMonth;
    private TextView sum;
    private TextView month;
    private TextView cost;
    private TextView costMonth;
    private String yearMonth;
    private double cost_month;
    private double income_month;
    private double total;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        income = findViewById(R.id.income);//显示本月收入多少钱
        incomeMonth = findViewById(R.id.income_month);
        sum = findViewById(R.id.sum);
        month = findViewById(R.id.yearMonth);
        cost = findViewById(R.id.cost);//显示本月花费多少钱
        costMonth = findViewById(R.id.cost_month);//显示月数
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        Date date = new Date(System.currentTimeMillis());
        final String yearMonth = simpleDateFormat.format(date);
        Data.setyearMonth(yearMonth);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM");
        Date date1 = new Date(System.currentTimeMillis());
        final String data = simpleDateFormat1.format(date1);
        Data.setData(data);
        month.setText(data);
        final LanGou lanGou = DataSupport.where("yearMonth == ?",yearMonth)
                .findLast(LanGou.class);
        if (lanGou == null){
            Money money = new Money();
            total = 0;
            cost_month = 0;
            income_month = 0;
            money.setTotal(total);
            money.setIncome_month(income_month);
            money.setCost_month(cost_month);
            sum.setText("总收入支出\n" + money.getTotal() + "元");
            cost.setText("本月已花费\n" + money.getCost_month() + "元");
            income.setText("本月已收入\n" +money.getIncome_month() + "元");
            money.save();
        }else{
            final Money money = DataSupport.where("yearMonth ==?",yearMonth)
                    .findLast(Money.class);
            Log.e(TAG,money.toString());
            total = money.getTotal();
            cost_month = money.getCost_month();
            income_month = money.getIncome_month();
            sum.setText("总收入支出\n" + money.getTotal() + "元");
            cost.setText("本月已花费\n" + money.getCost_month() + "元");
            income.setText("本月已收入\n" +money.getIncome_month() + "元");
            List<Bottom>bottomList = DataSupport.where("yearMonth == ?",yearMonth)
                    .find(Bottom.class);
            for (int i = 0;i<bottomList.size();i++) {
                final Bottom bottom = bottomList.get(i);
                Log.e(TAG,bottom.toString());
                final LinearLayout llayout = findViewById(R.id.linear_layout);
                final TextView textView1 = new TextView(MainActivity.this);
                final TextView tv = new TextView(MainActivity.this);
                final TextView tv2 = new TextView(MainActivity.this);
                final LinearLayout linearLayout = new LinearLayout(MainActivity.this);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(30, 30, 30, 0);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                linearLayout.setLayoutParams(layoutParams);
                tv2.setText(bottom.getReason());
                tv2.setTextSize(20);
                tv2.setGravity(Gravity.CENTER);
                tv2.setTextColor(Color.BLACK);

                tv.setTextSize(20);
                if (bottom.isFlag1()) {
                    tv.setText("-" + bottom.getExpand() + "元");
                } else {
                    tv.setText("+" + bottom.getExpand() + "元");
                }

                tv.setGravity(Gravity.CENTER);
                textView1.setText(bottom.getTime());
                textView1.setGravity(Gravity.CENTER);
                linearLayout.addView(tv2);
                linearLayout.addView(tv);

                linearLayout.addView(textView1);
                llayout.addView(linearLayout);
                linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("是否要删除这条记录")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (bottom.isFlag1() == true) {
                                            cost_month -= Integer.parseInt(bottom.getExpand());
                                            cost.setText("本月已花费" + cost_month + "元");
                                            total = income_month - cost_month;
                                            sum.setText("总收入支出" + total + "元");

                                        } else if (bottom.isFlag2() == true) {
                                            income_month -= Integer.parseInt(bottom.getExpand());
                                            income.setText("本月已收入" + income_month + "元");
                                            total = income_month - cost_month;
                                            sum.setText("总收入支出" + total + "元");
                                        }
                                        linearLayout.removeAllViews();
                                        Money updateMoney = DataSupport.where("yearMonth == ?",yearMonth)
                                                .findLast(Money.class);
                                        if(updateMoney == null){
                                            updateMoney = new Money();
                                            updateMoney.setYearMonth(yearMonth);
                                            updateMoney.setTotal(total);
                                            updateMoney.setIncome_month(income_month);
                                            updateMoney.setCost_month(cost_month);
                                            updateMoney.save();
                                            Log.e(TAG, total+"" );
                                        }else{
                                            updateMoney.setTotal(total);
                                            updateMoney.setIncome_month(income_month);
                                            updateMoney.setCost_month(cost_month);
                                            updateMoney.setYearMonth(yearMonth);
                                            Log.e(TAG, total+"    "+yearMonth);
                                            updateMoney.updateAll("yearMonth == ?",yearMonth);
                                            updateMoney.update(updateMoney.getId());
                                            if(total == 0  && income_month == 0 && cost_month == 0)
                                                DataSupport.deleteAll(Money.class,"yearMonth == ?" , yearMonth);
                                        }
                                        bottom.delete();
                                    }
                                                    })
                                .setNegativeButton("取消", null)
                                .show();
                        return true;
                    }

                });

            }
        }


           // }
        //}
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog();

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            final LinearLayout llayout = findViewById(R.id.linear_layout);
            llayout.removeAllViews();
            DataSupport.deleteAll(Bottom.class);
            total = 0;
            income_month = 0;
            cost_month = 0;
            DataSupport.deleteAll(Money.class);
            DataSupport.deleteAll(LanGou.class);
            Money money = new Money();
            money.setCost_month(0);
            money.setIncome_month(0);
            money.setTotal(0);
            money.save();
            sum.setText("总收入支出\n"+total+"元");
            cost.setText("本月已花费\n"+cost_month+"元");
            income.setText("本月已收入\n"+income_month+"元");

            return true;
        }
        if (id == R.id.action_setting2){
            final Calendar selectedDate = Calendar.getInstance();
            Calendar startDate = Calendar.getInstance();
            //startDate.set(2013,1,1);
            Calendar endDate = Calendar.getInstance();
            //endDate.set(2020,1,1);

            //正确设置方式 原因：注意事项有说明
            startDate.set(2013,0,1);
            endDate.set(2050,11,31);

            TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date,View v) {//选中事件回调

                    Log.e(TAG,""+selectedDate.get(Calendar.YEAR) );
                    final LinearLayout llayout = findViewById(R.id.linear_layout);
                    llayout.removeAllViews();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
                    final String yearMonth1 = simpleDateFormat.format(date);
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM");
                    final String data = simpleDateFormat1.format(date);
                    Data.setyearMonth(yearMonth1);
                    Data.setData(data);
                    month.setText(data);
                    Money updataMoney = DataSupport.where("yearMonth == ?",yearMonth1)
                            .findLast(Money.class);
                    if(updataMoney ==null ){//创建一个新账单
                        updataMoney = new Money();
                        total = 0;
                        cost_month = 0;
                        income_month = 0;
                        updataMoney.setTotal(total);
                        updataMoney.setIncome_month(income_month);
                        updataMoney.setCost_month(cost_month);
                        updataMoney.setYearMonth(yearMonth1);
                        sum.setText("总收入支出\n" + updataMoney.getTotal() + "元");
                        cost.setText("本月已花费\n" + updataMoney.getCost_month() + "元");
                        income.setText("本月已收入\n" +updataMoney.getIncome_month() + "元");
                        updataMoney.save();
                    }else{//读取
                        total = updataMoney.getTotal();
                        cost_month = updataMoney.getCost_month();
                        income_month = updataMoney.getIncome_month();
                        sum.setText("总收入支出\n" + updataMoney.getTotal() + "元");
                        cost.setText("本月已花费\n" + updataMoney.getCost_month() + "元");
                        income.setText("本月已收入\n" +updataMoney.getIncome_month() + "元");
                        List<Bottom>bottomList = DataSupport.where("yearMonth == ?",yearMonth1)
                                .find(Bottom.class);
                        for (int i = 0;i<bottomList.size();i++) {
                            final Bottom bottom = bottomList.get(i);
                            Log.e(TAG,bottom.toString());
                            //final LinearLayout llayout = findViewById(R.id.linear_layout);
                            final TextView textView1 = new TextView(MainActivity.this);
                            final TextView tv = new TextView(MainActivity.this);
                            final TextView tv2 = new TextView(MainActivity.this);
                            final LinearLayout linearLayout = new LinearLayout(MainActivity.this);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(30, 30, 30, 0);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            linearLayout.setLayoutParams(layoutParams);
                            tv2.setText(bottom.getReason());
                            tv2.setTextSize(20);
                            tv2.setGravity(Gravity.CENTER);
                            tv2.setTextColor(Color.BLACK);

                            tv.setTextSize(20);
                            if (bottom.isFlag1()) {
                                tv.setText("-" + bottom.getExpand() + "元");
                            } else {
                                tv.setText("+" + bottom.getExpand() + "元");
                            }

                            tv.setGravity(Gravity.CENTER);
                            textView1.setText(bottom.getTime());
                            textView1.setGravity(Gravity.CENTER);
                            linearLayout.addView(tv2);
                            linearLayout.addView(tv);

                            linearLayout.addView(textView1);
                            llayout.addView(linearLayout);
                            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                    dialog.setTitle("是否要删除这条记录")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    if (bottom.isFlag1() == true) {
                                                        cost_month -= Integer.parseInt(bottom.getExpand());
                                                        cost.setText("本月已花费\n" + cost_month + "元");
                                                        total = income_month - cost_month;
                                                        sum.setText("总收入支出\n" + total + "元");

                                                    } else if (bottom.isFlag2() == true) {
                                                        income_month -= Integer.parseInt(bottom.getExpand());
                                                        income.setText("本月已收入\n" + income_month + "元");
                                                        total = income_month - cost_month;
                                                        sum.setText("总收入支出\n" + total + "元");
                                                    }
                                                    linearLayout.removeAllViews();
                                                    Money updateMoney = DataSupport.where("yearMonth == ?",yearMonth1)
                                                            .findLast(Money.class);
                                                    if(updateMoney == null){
                                                        updateMoney = new Money();
                                                        updateMoney.setYearMonth(yearMonth1);
                                                        updateMoney.setTotal(total);
                                                        updateMoney.setIncome_month(income_month);
                                                        updateMoney.setCost_month(cost_month);
                                                        updateMoney.save();
                                                        Log.e(TAG, total+"" );
                                                    }else{
                                                        updateMoney.setTotal(total);
                                                        updateMoney.setIncome_month(income_month);
                                                        updateMoney.setCost_month(cost_month);
                                                        updateMoney.setYearMonth(yearMonth1);
                                                        Log.e(TAG, total+"    "+yearMonth1);
                                                        updateMoney.updateAll("yearMonth == ?",yearMonth1);
                                                        updateMoney.update(updateMoney.getId());
                                                        if(total == 0  && income_month == 0 && cost_month == 0)
                                                            DataSupport.deleteAll(Money.class,"yearMonth == ?" , yearMonth1);
                                                    }
                                                    bottom.delete();
                                                }
                                            })
                                            .setNegativeButton("取消", null)
                                            .show();
                                    return true;
                                }

                            });

                        }
                    }
                    }
            })
                    .setType(new boolean[]{true, true,false,false,false,false})// 默认全部显示
                    .setCancelText("Cancel")//取消按钮文字
                    .setSubmitText("Sure")//确认按钮文字
                    .setContentTextSize(18)//滚轮文字大小
                    .setTitleSize(20)//标题文字大小
                    .setTitleText("Title")//标题文字
                    .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                    .isCyclic(true)//是否循环滚动
                    .setTitleColor(Color.BLACK)//标题文字颜色
                    .setSubmitColor(Color.RED)//确定按钮文字颜色
                    .setCancelColor(Color.RED)//取消按钮文字颜色
                    .setTitleBgColor(0xFFc0c0c0)//标题背景颜色 Night mode
                    .setBgColor(0xFFa9a9a9)//滚轮背景颜色 Night mode
                    .setDate(selectedDate)// 如果不设置的话，默认是系统时间
                    .setRangDate(startDate,endDate)//起始终止年月日设定
                    .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                    .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    .isDialog(true)//是否显示为对话框样式
                    .build();
            pvTime.show();

        }
        if(id == R.id.action_setting3 ){
            List<Money> moneyList = DataSupport.findAll(Money.class);
            View view = getLayoutInflater().inflate(R.layout.account,null);
            double sum = 0;
            double income = 0;
            double cost = 0;
            for (int i = 0;i<moneyList.size();i++) {
                Money money = moneyList.get(i);
                sum += money.getTotal();
                income += money.getIncome_month();
                cost += money.getCost_month();
            }

            final TextView sumTv = view.findViewById(R.id.total_view);
            final TextView incomeTv = view.findViewById(R.id.income_view);
            final TextView costTv = view.findViewById(R.id.cost_view);
            sumTv.setText("总和"+sum+"元");
            incomeTv.setText("总共收入"+income+"元");
            costTv.setText("总共支出"+cost+"元");
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("您的账单总和")
                    .setView(view)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void Dialog(){

        final LinearLayout llayout = findViewById(R.id.linear_layout);
        final TextView tv = new TextView(MainActivity.this);
        final TextView tv2 = new TextView(MainActivity.this);
        final TextView textView = new TextView(MainActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_1,null);
        final View view_click = getLayoutInflater().inflate(R.layout.dialog_wheelview,null);
        final EditText et = view.findViewById(R.id.edit_text);
        final EditText et2 = view.findViewById(R.id.edit_write);
        yearMonth = Data.getyearMonth();
        final String Today = Data.getData();
        et.setHint("请确保输入一个数字");
        et2.setHint("请输入具体事项");
        final RadioButton bt1 = view.findViewById(R.id.radio_button1);
        final RadioButton bt2 = view.findViewById(R.id.radio_button2);
        final List<LanGou> lanGouList = DataSupport.where("yearMonth == ?",yearMonth)
                .find(LanGou.class);
        final Money updateMoney = DataSupport.where("yearMonth == ?",yearMonth)
                .findLast(Money.class);
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this);
        alertdialog.setTitle("请输入消费金额")
                .setView(view)
                .setPositiveButton("确定",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(bt1.isChecked() == true){
                    final Bottom bottom = new Bottom();
                    cost_month += Integer.parseInt(et.getText().toString());
                    cost.setText("本月已花费\n"+cost_month+"元");
                    final boolean flag1 = true;
                    final boolean flag2 = false;

                    final LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(30,25,30,25);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                            bt1.setChecked(false);
                            bt2.setChecked(false);
                            dialog.setTitle("请修改消费数据")
                                    .setView(view_click)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface,int i){
                                            if(bt2.isChecked() == true)
                                                Log.e(TAG, "djfjdfjdjfs" );
                                            if(bt2.getText().toString() == "收入")
                                                Log.e(TAG, "onClick: 4544554554444" );
                                            Log.e(TAG, bt2.getText().toString() );
                                            Log.e(TAG, bt1.getText().toString() );
                                            if(bt1.getText()+"" == "消费")
                                                Log.e(TAG, "onClick: 221231332" );
                                            ((ViewGroup)view_click.getParent()).removeView(view_click);
                                        }
                                    })
                                    .setNegativeButton("取消",null)
                                    .show();
                            //((ViewGroup)view_click.getParent()).removeView(view_click);
                        }
                    });
                    linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                            dialog.setTitle("是否要删除这条记录")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (flag1 == true) {
                                                cost_month -= Integer.parseInt(et.getText().toString());
                                                cost.setText("本月已花费\n" + cost_month + "元");
                                                total = income_month - cost_month;
                                                sum.setText("总收入支出\n" + total + "元");
                                            } else if (flag2 == true) {
                                                income_month -= Integer.parseInt(et.getText().toString());
                                                income.setText("本月已收入\n" + income_month + "元");
                                                total = income_month - cost_month;
                                                sum.setText("总收入支出\n" + total + "元");
                                            }
                                            DataSupport.delete(Bottom.class, bottom.getId());
                                            //bottom.delete();
                                            linearLayout.removeAllViews();
                                            if (updateMoney == null) {
                                                Money updateMoney = new Money();
                                                updateMoney.setCost_month(cost_month);
                                                updateMoney.setIncome_month(income_month);
                                                updateMoney.setTotal(total);
                                                updateMoney.setYearMonth(yearMonth);
                                                updateMoney.save();
                                            } else {
                                                updateMoney.setCost_month(cost_month);
                                                updateMoney.setIncome_month(income_month);
                                                updateMoney.setTotal(total);
                                                updateMoney.updateAll("yearMonth == ?", yearMonth);
                                            }
                                        }
                                    })
                                    .setNegativeButton("取消",null)
                                    .show();
                            return true;
                        }
                    });
                    tv2.setText("你的财富暴毙原因：" + et2.getText().toString());
                    tv2.setTextSize(20);
                    tv2.setGravity(Gravity.CENTER);
                    tv2.setTextColor(Color.BLACK);
                    linearLayout.addView(tv2);
                    tv.setText("-"+et.getText().toString()+"元");
                    tv.setTextSize(20);
                    tv.setGravity(Gravity.CENTER);
                    linearLayout.addView(tv);

                    TextView textView1 = new TextView(MainActivity.this);
                    textView1.setText(Today);
                    textView1.setTextSize(15);
                    textView1.setGravity(Gravity.CENTER);
                    linearLayout.addView(textView1);
                    total = income_month - cost_month;
                    sum.setText("总收入支出\n"+total+"元");
                    llayout.addView(linearLayout);

                    bottom.setExpand(et.getText().toString());
                    bottom.setTime(textView1.getText().toString());
                    bottom.setReason(tv2.getText().toString());
                    bottom.setFlag1(true);
                    bottom.setFlag2(false);
                    bottom.setTime(Today);
                    bottom.setYearMonth(yearMonth);
                    bottom.save();

                    if (lanGouList.size() == 0){
                        LanGou updateLanGou = new LanGou();
                        updateLanGou.setYearMonth(yearMonth);
                        updateLanGou.save();

                        if(updateMoney == null){

                            Money updateMoney = new Money();
                            updateMoney.setTotal(total);
                            updateMoney.setCost_month(cost_month);
                            updateMoney.setIncome_month(income_month);
                            updateMoney.setYearMonth(yearMonth);
                            updateLanGou.setMoney(updateMoney);
                            updateMoney.save();
                            updateLanGou.save();

                            Log.e(TAG,updateMoney.toString());
                        } else{
                            updateMoney.setTotal(total);
                            updateMoney.setCost_month(cost_month);
                            updateMoney.setIncome_month(income_month);
                            updateMoney.setYearMonth(yearMonth);
                            updateMoney.updateAll("yearMonth == ?" , yearMonth);
                            updateLanGou.setMoney(updateMoney);
                            updateLanGou.updateAll("yearMonth == ?",yearMonth);
                            updateLanGou.save();
                            Log.e(TAG, updateMoney.toString());
                            Log.e(TAG, updateLanGou.toString());
                        }

                        //bottom.setMoney(money1);

                        updateLanGou.setYearMonth(yearMonth);
                        updateLanGou.getBottomList().add(bottom);

                        updateLanGou.updateAll("yearMonth == ?" , yearMonth);

                    }else{
                        LanGou updateLanGou = lanGouList.get(0);


                        if(updateMoney == null){

                            Money updateMoney = new Money();
                            updateMoney.setTotal(total);
                            updateMoney.setCost_month(cost_month);
                            updateMoney.setIncome_month(income_month);
                            updateMoney.setYearMonth(yearMonth);
                            updateLanGou.setMoney(updateMoney);
                            updateMoney.save();
                            updateLanGou.save();

                            //.e(TAG,updateMoney.toString());
                        } else{
                            updateMoney.setTotal(total);
                            updateMoney.setCost_month(cost_month);
                            updateMoney.setIncome_month(income_month);
                            updateMoney.setYearMonth(yearMonth);
                            updateMoney.updateAll("yearMonth == ?" , yearMonth);
                            updateLanGou.setMoney(updateMoney);
                            updateLanGou.updateAll("yearMonth == ?",yearMonth);
                            Log.e(TAG, updateMoney.toString());
                            Log.e(TAG,updateLanGou.toString() );
                        }


                        bottom.update(bottom.getId());

                        updateLanGou.setYearMonth(yearMonth);
                        updateLanGou.getBottomList().add(bottom);

                        updateLanGou.updateAll("yearMonth == ?" , yearMonth);

                    }


                }
                if(bt2.isChecked() == true){
                    final Bottom bottom = new Bottom();
                    income_month += Integer.parseInt(et.getText().toString());
                    income.setText("本月已收入\n" + income_month +"元");
                    final boolean flag1 =false;
                    final boolean flag2 =true;
                    final LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(30,25,30,25);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                            dialog.setTitle("是否要删除这条记录")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(flag1 == true){
                                                cost_month -= Integer.parseInt(et.getText().toString());
                                                cost.setText("本月已花费\n" + cost_month + "元");
                                                total =  income_month - cost_month ;
                                                sum.setText("总收入支出\n" + total + "元");

                                            }else if(flag2 == true){
                                                income_month -= Integer.parseInt(et.getText().toString());
                                                income.setText("本月已收入\n" + income_month +"元");
                                                total = income_month -cost_month;
                                                sum.setText("总收入支出\n" + total +"元");
                                            }
                                            linearLayout.removeAllViews();
                                            DataSupport.delete(Bottom.class,bottom.getId());
                                            if (updateMoney == null) {
                                                Money updateMoney = new Money();
                                                updateMoney.setCost_month(cost_month);
                                                updateMoney.setIncome_month(income_month);
                                                updateMoney.setTotal(total);
                                                updateMoney.setYearMonth(yearMonth);
                                                updateMoney.save();
                                            } else {
                                                updateMoney.setCost_month(cost_month);
                                                updateMoney.setIncome_month(income_month);
                                                updateMoney.setTotal(total);
                                                updateMoney.updateAll("yearMonth == ?", yearMonth);
                                            }
                                        }
                                    })
                                    .setNegativeButton("取消",null)
                                    .show();
                            return true;
                        }

                    });
                    tv2.setText("你的财富暴涨原因：" + et2.getText().toString());
                    tv2.setSingleLine(false);
                    tv2.setTextSize(20);
                    tv2.setGravity(Gravity.CENTER);
                    tv2.setTextColor(Color.BLACK);
                    linearLayout.addView(tv2);
                    tv.setText("+"+et.getText().toString() + "元");
                    tv.setTextSize(20);

                    tv.setGravity(Gravity.CENTER);
                    linearLayout.addView(tv);
                    TextView textView1 = new TextView(MainActivity.this);
                    textView1.setText(Today);
                    textView1.setTextSize(15);
                    textView1.setGravity(Gravity.CENTER);
                    linearLayout.addView(textView1);
                    total = income_month - cost_month;
                    sum.setText("总收入支出\n"+total+"元");
                    llayout.addView(linearLayout);


                    bottom.setExpand(et.getText().toString());
                    bottom.setTime(textView1.getText().toString());
                    bottom.setReason(tv2.getText().toString());
                    bottom.setFlag2(true);
                    bottom.setFlag1(false);
                    bottom.setYearMonth(yearMonth);
                    bottom.save();
                    try{
                        List<LanGou> lanGouList = DataSupport.where("yearMonth == ?",yearMonth)
                                .find(LanGou.class);
                        Money updateMoney = DataSupport.where("yearMonth == ?",yearMonth)
                                .findLast(Money.class);
                        if (lanGouList.size() == 0){
                            LanGou updateLanGou = new LanGou();
                            updateLanGou.setYearMonth(yearMonth);
                            updateLanGou.save();
                            //Log.e(TAG, "onClick:1223 " );
                            if(updateMoney == null){
                                Log.e(TAG, "onClick: ");
                                updateMoney = new Money();
                                updateMoney.setTotal(total);
                                updateMoney.setCost_month(cost_month);
                                updateMoney.setIncome_month(income_month);
                                updateMoney.setYearMonth(yearMonth);
                                updateLanGou.setMoney(updateMoney);
                                updateMoney.save();
                                updateLanGou.setMoney(updateMoney);
                                updateLanGou.save();

                                Log.e(TAG,updateMoney.toString());
                            } else{
                                updateMoney.setTotal(total);
                                updateMoney.setCost_month(cost_month);
                                updateMoney.setIncome_month(income_month);
                                updateMoney.setYearMonth(yearMonth);
                                updateMoney.updateAll("yearMonth == ?" , yearMonth);
                                updateLanGou.setMoney(updateMoney);
                                updateLanGou.updateAll("yearMonth == ?",yearMonth);
                                updateLanGou.save();
                                Log.e(TAG, updateMoney.toString());
                                Log.e(TAG, updateLanGou.toString());
                            }

                            //bottom.setMoney(money1);
                            bottom.update(bottom.getId());
                            updateLanGou.getBottomList().add(bottom);
                            updateLanGou.updateAll("yearMonth == ?" , yearMonth);
                        }else{
                            LanGou updateLanGou = lanGouList.get(0);


                            if(updateMoney == null){

                                updateMoney = new Money();
                                updateMoney.setTotal(total);
                                updateMoney.setCost_month(cost_month);
                                updateMoney.setIncome_month(income_month);
                                updateMoney.setYearMonth(yearMonth);
                                updateLanGou.setMoney(updateMoney);
                                updateMoney.save();
                                updateLanGou.save();

                                Log.e(TAG,updateMoney.toString());
                            } else{
                                updateMoney.setTotal(total);
                                updateMoney.setCost_month(cost_month);
                                updateMoney.setIncome_month(income_month);
                                updateMoney.setYearMonth(yearMonth);
                                updateMoney.updateAll("yearMonth == ?" , yearMonth);
                                updateLanGou.setMoney(updateMoney);
                                updateLanGou.updateAll("yearMonth == ?",yearMonth);
                                Log.e(TAG, updateMoney.toString());
                                Log.e(TAG,updateLanGou.toString() );
                            }

                            //bottom.setMoney(money1);
                            bottom.update(bottom.getId());
                            updateLanGou.setYearMonth(yearMonth);
                            updateLanGou.getBottomList().add(bottom);
                            updateLanGou.updateAll("yearMonth == ?" , yearMonth);
                        }
                    }catch (DataSupportException e){
                        e.printStackTrace();
                    }


                }
                if(bt1.isChecked() == false && bt2.isChecked() == false){
                    Toast.makeText(MainActivity.this,"请先选择收入或花费再输入数字",Toast.LENGTH_SHORT).show();
                }
            }
        })
                .setNegativeButton("取消",null)
                .show();
    }
}
