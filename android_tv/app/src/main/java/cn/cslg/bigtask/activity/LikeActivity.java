package cn.cslg.bigtask.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.cslg.bigtask.R;
import cn.cslg.bigtask.db.Like;

public class LikeActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<String> idList = new ArrayList<>();
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("我的收藏");
        listView = (ListView) findViewById(R.id.listview);
        init();
        adapter = new ArrayAdapter<>(LikeActivity.this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tvid = idList.get(position);
                Intent intent = new Intent(LikeActivity.this, SecondActivity.class);
                intent.putExtra("tvid", tvid);
                startActivity(intent);
            }
        });


    }

    private void init() {
        if(dataList.size()>0)
            dataList.clear();
        List<Like> favors = DataSupport.findAll(Like.class);
        for(int i =0 ;i<favors.size();i++){
                dataList.add(favors.get(i).getTvname());
                idList.add(favors.get(i).getTvid());
                Log.d("aaa",(favors.get(i).getTvname())+"");
        }
    }
}
