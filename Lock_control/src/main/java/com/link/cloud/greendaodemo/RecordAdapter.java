package com.link.cloud.greendaodemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.bean.CabinetRecord;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends BaseAdapter {
    List<CabinetRecord> userList = new ArrayList<CabinetRecord>();
    LayoutInflater layoutInflater;
    ListView mylistView;
    Context context;
    ViewHolder viewHolder;

    public RecordAdapter(List userList, Context context) {
        this.userList = userList;
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public CabinetRecord getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.record_list_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            viewHolder.tv_phone=(TextView)convertView.findViewById(R.id.tv_user_phone);
            viewHolder.tv_stating=(TextView)convertView.findViewById(R.id.tv_Stating);
            viewHolder.tv_cabinetnum=(TextView)convertView.findViewById(R.id.tv_cabinetNumber);
            viewHolder.tv_opentime=(TextView)convertView.findViewById(R.id.tv_open_time);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        CabinetRecord user = userList.get(position);
        viewHolder.tv_name.setText(user.getMemberName());
        viewHolder.tv_phone.setText(user.getPhoneNum());
        viewHolder.tv_cabinetnum.setText(user.getCabinetNumber());
        viewHolder.tv_stating.setText(user.getCabinetStating());
        viewHolder.tv_opentime.setText(user.getOpentime());
        return convertView;
    }
    class ViewHolder {
        TextView tv_name;
        TextView tv_phone;
        TextView tv_cabinetnum;
        TextView tv_stating;
        TextView tv_opentime;
    }
}
