package com.link.cloud.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.link.cloud.R;


public class HorizontalListViewAdapter extends BaseAdapter {
	String lessonId,lessonName,lessonDate;
	String coach,memberName,memberTel;
	private Context mContext;
	private LayoutInflater mInflater;
	private ImageView imageView;
	Bitmap iconBitmap;
	private int selectIndex = -1;
	public HorizontalListViewAdapter(Context context,int selectIndex,String coach,String memberName,String memberTel,String lessonId,String lessonName, String lessonDate){
		this.mContext = context;
		this.coach=coach;
		this.memberName=memberName;
		this.selectIndex=selectIndex;
		this.memberTel=memberTel;
		this.lessonId=lessonId;
		this.lessonName=lessonName;
		this.lessonDate=lessonDate;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return 1;
	}
	@Override
	public Object getItem(int position) {
		return position;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
//			convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
//			holder.select=(ImageView)convertView.findViewById(R.id.ischecked);
//			holder.memberName=(TextView)convertView.findViewById(R.id.elm_menber_name);
//			holder.memberTel=(TextView)convertView.findViewById(R.id.elm_menber_phone);
//			holder.coach=(TextView)convertView.findViewById(R.id.elm_coach_name);
//			holder.lessonName=(TextView)convertView.findViewById(R.id.elm_lesson_name);
//			holder.lessonDate=(TextView)convertView.findViewById(R.id.elm_lesson_time);
			holder.select.setVisibility(View.VISIBLE);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
//		if(position == selectIndex){
//			holder.select.setVisibility(View.VISIBLE);
//		}else{
//			holder.select.setVisibility(View.INVISIBLE);
//		}
		holder.memberName.setText(memberName);
		holder.coach.setText(coach);
		holder.memberTel.setText(memberTel);
		holder.lessonName.setText(lessonName);
		holder.lessonDate.setText(lessonDate);
		return convertView;
	}
	private static class ViewHolder {
		private TextView memberName,memberTel,coach,lessonName,lessonDate;
		private ImageView select;
	}
//	private Bitmap getPropThumnail(int id){
//		Drawable d = mContext.getResources().getDrawable(id);
//		Bitmap b = BitmapUtil.drawableToBitmap(d);
////		Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
//		int w = mContext.getResources().getDimensionPixelOffset(R.dimen.thumnail_default_width);
//		int h = mContext.getResources().getDimensionPixelSize(R.dimen.thumnail_default_height);
//		Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);
//		return thumBitmap;
//	}
	public void setSelectIndex(int i){
		selectIndex = i;
	}
}