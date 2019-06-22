package cn.cslg.bigtask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cn.cslg.bigtask.R;
import cn.cslg.bigtask.bean.Programer;


public class ListAdapter  extends ArrayAdapter<Programer> {
    private int resourceId;

    public ListAdapter(Context context, int textViewResourceId,
                       List<Programer> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Programer cityItem = getItem(position); // 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.location = (TextView) view.findViewById (R.id.location);
            viewHolder.admin_area = (TextView) view.findViewById (R.id.admin_area);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.location.setText("节目名称:"+cityItem.name);
        viewHolder.admin_area.setText("开始时间:"+cityItem.starttime);
        return view;
    }

    class ViewHolder {
        TextView location;
        TextView admin_area;

    }
}
