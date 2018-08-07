package com.link.cloud.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.link.cloud.R;
import com.link.cloud.bean.CabinetNumber;
import com.link.cloud.greendaodemo.LockedAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 30541 on 2018/5/3.
 */

public class LockMessage extends Dialog{
    private Context mContext;
    private ListView listView;

    private List<CabinetNumber> recordList = new ArrayList<CabinetNumber>();
    private List recordstate=new ArrayList();
    LockedAdapter myAdapter;
    public LockMessage(@NonNull Context context,List<CabinetNumber>  CabinetNumberList) {
        super(context);
        mContext = context;
        recordList=CabinetNumberList;
        initDialog(recordList);
    }

    public LockMessage(@NonNull Context context, int themeResId,List<CabinetNumber> recordList) {
        super(context, R.style.customer_dialog);
        mContext = context;
        recordList=recordList;
        initDialog(recordList);
    }
    private void initDialog(List<CabinetNumber> recordList) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lock_message, null);
        setContentView(view);
        listView=(ListView)view.findViewById(R.id.lock_message);
        myAdapter=new LockedAdapter(recordList,mContext);
        listView.setAdapter(myAdapter);
    }
}
