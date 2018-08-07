package com.link.cloud.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.link.cloud.R;
import com.link.cloud.bean.CabinetRecord;
import com.link.cloud.greendaodemo.RecordAdapter;

import java.util.List;

/**
 * Created by 30541 on 2018/5/3.
 */

public class CheckUsedRecored extends Dialog{
    private Context mContext;
    private ListView listView;

    RecordAdapter myAdapter;

    public CheckUsedRecored(@NonNull Context context, List<CabinetRecord> recordList, String number) {
        super(context);
        mContext = context;
        initDialog(number,recordList);
    }

    public CheckUsedRecored(@NonNull Context context, int themeResId,String number,List<CabinetRecord> recordList) {
        super(context, R.style.customer_dialog);
        mContext = context;
        initDialog(number,recordList);
    }

    private void initDialog(String number,List<CabinetRecord> recordList) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.open_recored, null);
        setContentView(view);
        listView=(ListView)view.findViewById(R.id.open_recored);
        if (recordList.size()>0) {
            myAdapter = new RecordAdapter(recordList, mContext);
            listView.setAdapter(myAdapter);
        }
    }
}
