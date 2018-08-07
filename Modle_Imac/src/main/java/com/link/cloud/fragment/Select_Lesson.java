package com.link.cloud.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.link.cloud.R;
import com.link.cloud.bean.LessonMessage;
import com.link.cloud.bean.LessonResponse;
import com.link.cloud.bean.Member;
import com.link.cloud.bean.RetrunLessons;
import com.link.cloud.bean.SignUserdata;
import com.link.cloud.constant.Constant;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.ui.HorizontalListViewAdapter;
import com.link.cloud.ui.RollListView;
import com.orhanobut.logger.Logger;

import butterknife.Bind;

/**
 * Created by 30541 on 2018/3/11.
 */

public class Select_Lesson extends BaseFragment {
    @Bind(R.id.lessonmessageInfo)
    RollListView horizontalListView;
    @Bind(R.id.lessonLayout)
    LinearLayout lessonLayout;
    @Bind(R.id.selectLesson)
    LinearLayout selectLesson;
    @Bind(R.id.up_lesson)
    Button up_lesson;
    @Bind(R.id.next_lesson)
    Button next_lesson;
    String[] lessonId,lessonName,lessonDate;
    int num,indext;
    RetrunLessons lessonResponse;
    private int selectPosition = 0;//用于记录用户选择的变量
    private HorizontalListViewAdapter hListViewAdapter;
    public  String lessonnum=new String();
    View.OnClickListener clickListener;
    public static Select_Lesson newInstance(RetrunLessons lessonResponse) {
        Logger.e("SignFragment_Two-------newInstance");
        Select_Lesson fragment = new Select_Lesson();
        Bundle args = new Bundle();
        args.putSerializable(Constant.EXTRAS_ELIMINATE_INFO, lessonResponse);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initListeners() {
    }
    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            lessonResponse = (RetrunLessons) bundle.getSerializable(Constant.EXTRAS_MEMBER);
        }
        num=lessonResponse.getLessonResponse().getLessonInfo().length;
        lessonId=new String[num];
        lessonName=new String[num];
        lessonDate=new String[num];
        for (int i=0;i<num;i++) {
            lessonId[i]=lessonResponse.getLessonResponse().getLessonInfo()[i].getLessonId();
            lessonName[i]=lessonResponse.getLessonResponse().getLessonInfo()[i].getLessonName();
            lessonDate[i]=lessonResponse.getLessonResponse().getLessonInfo()[i].getLessonDate();
        }
        lessonnum=lessonResponse.getLessonResponse().getLessonInfo()[0].getLessonId();
        hListViewAdapter=new HorizontalListViewAdapter(getContext(),selectPosition,lessonResponse.getLessonResponse().getCoach(),lessonResponse.getLessonResponse().getMembername(),lessonResponse.getLessonResponse().getMemberphone(),lessonId[indext],lessonName[indext],lessonDate[indext]);
        horizontalListView.setAdapter(hListViewAdapter);
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                hListViewAdapter.setSelectIndex(position);
                selectPosition = position;
                lessonnum=lessonResponse.getLessonResponse().lessonInfo[position].getLessonId();
                hListViewAdapter.notifyDataSetChanged();
            }
        });
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId()) {
                    case R.id.up_lesson:
                        uplesson();
                        Logger.e("eliminateSuccess========="+"R.id.up_lesson");
                        break;
                    case R.id.next_lesson:
                        nextlesson();
                        Logger.e("eliminateSuccess========="+"next_lesson");
                        break;
                }
            }
        };
        up_lesson.setOnClickListener(clickListener);
        next_lesson.setOnClickListener(clickListener);
        checkButton();
        Logger.e("eliminateSuccess========="+lessonResponse.toString());
    }
    private Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                Logger.e("LessonFragment" + "handle" + lessonResponse.toString());
                hListViewAdapter = new HorizontalListViewAdapter(getContext(), indext, lessonResponse.getLessonResponse().getCoach(), lessonResponse.getLessonResponse().getMembername(), lessonResponse.getLessonResponse().getMemberphone(), lessonId[indext], lessonName[indext], lessonDate[indext]);
                horizontalListView.setAdapter(hListViewAdapter);
                hListViewAdapter.notifyDataSetChanged();
                horizontalListView.invalidate();
            }
        }
    };
    public void checkButton(){
        Logger.e("eliminateSuccess========="+"checkButton"+indext+ "num:"+num);
        if (num>1){
            if (indext==0){
                Logger.e("eliminateSuccess=indext==0="+"checkButton"+indext+ "num:"+num);
                up_lesson.setEnabled(false);
                up_lesson.setBackgroundResource(R.drawable.no_up);
                next_lesson.setBackgroundResource(R.drawable.next_btn_bge);
                next_lesson.setEnabled(true);
            }else if (indext<num-1&&indext!=0){
                Logger.e("eliminateSuccess=indext<num-1&&indext!=0="+"checkButton"+indext+ "num:"+num);
                up_lesson.setBackgroundResource(R.drawable.up_btn_bg);
                up_lesson.setEnabled(true);
                next_lesson.setBackgroundResource(R.drawable.next_btn_bge);
                next_lesson.setEnabled(true);
            }else if (indext==num-1&indext!=0){
                Logger.e("eliminateSuccess=indext==num-1&indext!=0="+"checkButton"+indext+ "num:"+num);
                up_lesson.setBackgroundResource(R.drawable.up_btn_bg);
                up_lesson.setEnabled(true);
                next_lesson.setEnabled(false);
                next_lesson.setBackgroundResource(R.drawable.no_next);
            }
        }else if (num==1){
            up_lesson.setBackgroundResource(R.drawable.no_up);
            next_lesson.setBackgroundResource(R.drawable.no_next);
            next_lesson.setEnabled(false);
            up_lesson.setEnabled(false);
        }
    }
    public void uplesson(){
        indext--;
        lessonnum=lessonResponse.getLessonResponse().lessonInfo[indext].getLessonId();
        Logger.e("eliminateSuccess========="+"uplesson:"+(indext));
//        hListViewAdapter=new HorizontalListViewAdapter(getContext(),indext-1,lessonResponse.getCoach(),lessonResponse.getMembername(),lessonResponse.getMemberphone(),lessonId[indext],lessonName[indext],lessonDate[indext]);
//        horizontalListView.setAdapter(hListViewAdapter);
//        hListViewAdapter.notifyDataSetChanged();
        Logger.e("eliminateSuccess========="+"uplesson"+lessonResponse.toString());
        Message msg=mHandler.obtainMessage();
        msg.what=2;
        mHandler.sendMessage(msg);
        checkButton();
    }
    public void nextlesson(){
        indext++;
        lessonnum=lessonResponse.getLessonResponse().lessonInfo[indext].getLessonId();
        Logger.e("eliminateSuccess========="+"nextlesson"+"nextlesson:"+indext);
//        hListViewAdapter=new HorizontalListViewAdapter(getContext(),indext+1,lessonResponse.getCoach(),lessonResponse.getMembername(),lessonResponse.getMemberphone(),lessonId[indext],lessonName[indext],lessonDate[indext]);
//        horizontalListView.setAdapter(hListViewAdapter);
//        hListViewAdapter.notifyDataSetChanged();
        Logger.e("eliminateSuccess========="+lessonResponse.toString());
        Message msg=mHandler.obtainMessage();
        msg.what=2;
        mHandler.sendMessage(msg);
        checkButton();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.lesson_layout;
    }

    @Override
    protected void onVisible() {

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void initData() {

    }
}
