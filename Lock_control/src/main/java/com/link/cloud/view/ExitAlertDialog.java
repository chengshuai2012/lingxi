package com.link.cloud.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.R;

public class ExitAlertDialog extends Dialog {
    private Context mContext;
    private TextView texttilt;
    LinearLayout edit_layout;
    LinearLayout button_layout;
    public ExitAlertDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        initDialog();
    }

    public ExitAlertDialog(Context context) {
        super(context, R.style.customer_dialog);
        mContext = context;
        initDialog();
    }
    private void initDialog() {
        View view = View.inflate(mContext,R.layout.dialog_exit_confirm, null);
        setContentView(view);
        texttilt=(TextView)view.findViewById(R.id.text_title);
        edit_layout=(LinearLayout)view.findViewById(R.id.edit_layout);
        button_layout=(LinearLayout)view.findViewById(R.id.button_layout);
    }
    @Override
    public void show() {
        edit_layout.setVisibility(View.GONE);
        button_layout.setVisibility(View.GONE);
        texttilt.setText(R.string.sysn_data_img);
        super.show();
    }

}