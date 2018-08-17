package com.link.cloud.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.bean.RetrunLessons;

import java.util.List;

/**
 * Created by ls on 2017/11/25.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.UniversalViewHolder> {
    public List<RetrunLessons.DataBean.LessonInfoBean.CardInfoBean> mData;
    public Context context;
    PositionChangedLister listener;
    public CardAdapter(List<RetrunLessons.DataBean.LessonInfoBean.CardInfoBean> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }
    public interface PositionChangedLister{
        void postionChanged(String cardNo);
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
        holder.value_date.setText(context.getString(R.string.value_date)+mData.get(position).getCardDate()+"");
        holder.value_cout.setText(context.getString(R.string.lesson_type)+mData.get(position).getCardType()+"");
        holder.name.setText(context.getString(R.string.card_name)+mData.get(position).getCardName()+"");
        holder.tel.setText(context.getString(R.string.rest_time)+mData.get(position).getCardTimes()+"");
        holder.card_num.setText(context.getString(R.string.card_num)+mData.get(position).getCardNo()+"");
        if(listener!=null){
            listener.postionChanged(mData.get(position).getCardNo());
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