package cn.cslg.bigtask.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.cslg.bigtask.R;
import cn.cslg.bigtask.activity.MainActivity;
import cn.cslg.bigtask.activity.SecondActivity;
import cn.cslg.bigtask.db.Like;
import cn.cslg.bigtask.bean.Channel;
import cn.cslg.bigtask.util.HttpUtil;
import cn.cslg.bigtask.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChannelFragment extends Fragment {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<String> idList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestChannel();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tvid  = idList.get(position);
                if (getActivity() instanceof MainActivity) {
                    Intent intent = new Intent(getActivity(),SecondActivity.class);
                    intent.putExtra("tvid",tvid);
                    startActivity(intent);
                } else if (getActivity() instanceof SecondActivity) {
                    SecondActivity activity = (SecondActivity) getActivity();
                    activity.drawerLayout.closeDrawers();
                    activity.id = tvid;
                    activity.requestProgarammer(tvid);
                }


            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Like like = new Like();
                MainActivity activity = (MainActivity) getActivity();
                like.setTvname(dataList.get(position));
                like.setTvid(idList.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setTitle("收藏")
                        .setMessage("确认收藏？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                like.save();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
                return true;
            }
        });
    }


    private void requestChannel() {
        String weatherUrl = "https://api.jisuapi.com/tv/channel?appkey=d01a8e809ebebe6f";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取频道信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Channel weather = Utility.handleChannelResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.msg)) {
                            for (int i=432;i<weather.result.size();i++){
                                dataList.add(weather.result.get(i).name);
                                idList.add(weather.result.get(i).tvid);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "获取频道信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
