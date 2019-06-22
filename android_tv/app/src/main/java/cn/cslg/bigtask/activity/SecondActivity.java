package cn.cslg.bigtask.activity;

import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cn.cslg.bigtask.R;
import cn.cslg.bigtask.adapter.ListAdapter;
import cn.cslg.bigtask.bean.Program;
import cn.cslg.bigtask.bean.Programer;
import cn.cslg.bigtask.util.HttpUtil;
import cn.cslg.bigtask.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private AppBarLayout mAppBarLayout;
    private CompactCalendarView mCompactCalendarView;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd EE", Locale.CHINESE);
    private  String NOW_DATE_TIME = "";//空为当天
    private boolean isExpanded = false;
    public DrawerLayout drawerLayout;
    ActionBar actionBar;
    private List<Programer> dataList = new ArrayList<>();
    private String data = "";
    private ListAdapter adapter;
    private ListView listView;
    private TextView date_picker_text_view;
    public String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        toolbar = findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);

        }
        date_picker_text_view = findViewById(R.id.date_picker_text_view);
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
//        drawerLayout.setDrawerListener(toggle);
//        toggle.syncState();

        toolbar.setNavigationOnClickListener(this);
//lisrview放节目信息
        listView = findViewById(R.id.content_list);
        adapter = new cn.cslg.bigtask.adapter.ListAdapter(SecondActivity.this, R.layout.list_item, dataList);
        listView.setAdapter(adapter);
        id = getIntent().getStringExtra("tvid");
        //设置toolbar中的日历
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_main);
        // 设置 CompactCalendarView
        mCompactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        final ImageView arrow = (ImageView) findViewById(R.id.date_picker_arrow);
        // 设置 星期英文
        mCompactCalendarView.setLocale(TimeZone.getDefault(), Locale.ENGLISH);
        mCompactCalendarView.setShouldDrawDaysHeader(true);
        mCompactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                NOW_DATE_TIME = dateFormat.format(dateClicked);
                setTitle(NOW_DATE_TIME);
                setCurrentDate(dateClicked);
                requestProgarammer(id);
                ViewCompat.animate(arrow).rotation(0).start();
                mAppBarLayout.setExpanded(false, true);
                isExpanded = false;

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setTitle(dateFormat.format(firstDayOfNewMonth));
            }
        });
        //设置为当前日期
        setCurrentDate(new Date());
        RelativeLayout datePickerButton = (RelativeLayout) findViewById(R.id.date_picker_button);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    ViewCompat.animate(arrow).rotation(0).start();
                    mAppBarLayout.setExpanded(false, true);
                    isExpanded = false;
                } else {
                    ViewCompat.animate(arrow).rotation(180).start();
                    mAppBarLayout.setExpanded(true, true);
                    isExpanded = true;
                }
            }
        });
    }

    public void setCurrentDate(Date date) {
        date_picker_text_view.setText("选择日期");
        if (mCompactCalendarView != null) {
            mCompactCalendarView.setCurrentDate(date);
        }
        String[] a;
        a= NOW_DATE_TIME.split(" ");
        data = "20" + a[0];
        requestProgarammer(id);
    }

    public void requestProgarammer(String tvid) {
        if (dataList.size() > 0)
            dataList.clear();
        String weatherUrl = "https://api.jisuapi.com/tv/query?appkey=d01a8e809ebebe6f&tvid=" + tvid + "&date=" +data;
//        Log.d("aaa", weatherUrl);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SecondActivity.this, "获取节目信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Program weather = Utility.handlePorgramResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null&&weather.msg.equals("ok")){
                            for (int i = 0; i < weather.result.program.size(); i++) {
                                dataList.add(weather.result.program.get(i));
//                                System.out.println(weather.result.program.get(i).name);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(SecondActivity.this, "获取节目信息失败", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    @Override
    public void onClick(View v) {
//        drawerLayout.openDrawer(GravityCompat.START);
    }
}
