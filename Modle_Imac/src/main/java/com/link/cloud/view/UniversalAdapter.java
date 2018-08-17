package com.link.cloud.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.bean.RetrunLessons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ls on 2017/11/25.
 */

public class UniversalAdapter extends RecyclerView.Adapter<UniversalAdapter.UniversalViewHolder> {
    public ArrayList<RetrunLessons.DataBean.LessonInfoBean> mData;
    public Context context;
    PositionChangedLister listener;
    public UniversalAdapter(ArrayList<RetrunLessons.DataBean.LessonInfoBean> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }
    public interface PositionChangedLister{
        void postionChanged( String id,List<RetrunLessons.DataBean.LessonInfoBean.CardInfoBean> cardInfo,String cardname);
    }
    public void setPostionListener(PositionChangedLister listener){
        this.listener=listener;
    }
    @Override
    public UniversalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.muti_card_item, null);
        UniversalViewHolder holder = new UniversalViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UniversalViewHolder holder, int position) {
        holder.card_num.setText(context.getString(R.string.lesson_name)+mData.get(position).getLessonName());
        
        holder.value_date.setText(context.getString(R.string.lesson_time)+mData.get(position).getLessonDate());
        if(listener!=null){
            listener.postionChanged(mData.get(position).getLessonId(),mData.get(position).getCardInfo(),mData.get(position).getLessonName());
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class UniversalViewHolder extends RecyclerView.ViewHolder {
        TextView name ,tel,card_num,value_cout,value_date;

        public UniversalViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.name);
            tel= (TextView) itemView.findViewById(R.id.tel);
            card_num= (TextView) itemView.findViewById(R.id.card_num);
            value_cout= (TextView) itemView.findViewById(R.id.value_cout);
            value_date= (TextView) itemView.findViewById(R.id.value_date);
        }
    }
}