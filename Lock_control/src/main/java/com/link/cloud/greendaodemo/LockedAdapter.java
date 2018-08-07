package com.link.cloud.greendaodemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.bean.CabinetNumber;

import java.util.ArrayList;
import java.util.List;

public class LockedAdapter extends BaseAdapter {
    List<CabinetNumber> userList = new ArrayList<CabinetNumber>();
    LayoutInflater layoutInflater;
    Context context;
    ViewHolder viewHolder;

    public LockedAdapter(List userList, Context context) {
        this.userList = userList;
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
    }
    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public CabinetNumber getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.lock_list_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.lock_route = (TextView) convertView.findViewById(R.id.lock_route);
            viewHolder.lock_lockplate=(TextView)convertView.findViewById(R.id.lock_lockplate);
            viewHolder.lock_cabinetNumber=(TextView)convertView.findViewById(R.id.lock_cabinetNumber);
            viewHolder.lock_Stating=(TextView)convertView.findViewById(R.id.lock_Stating);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        CabinetNumber user = userList.get(position);
        viewHolder.lock_route.setText(user.getCircuitNumber());
        viewHolder.lock_lockplate.setText(user.getCabinetLockPlate());
        viewHolder.lock_cabinetNumber.setText(user.getCabinetNumber());
        viewHolder.lock_Stating.setText(user.getIsUser());
        return convertView;
    }
    class ViewHolder {
        TextView lock_route;
        TextView lock_lockplate;
        TextView lock_cabinetNumber;
        TextView lock_Stating;
    }
}
