package com.link.cloud.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.link.cloud.R;
import com.link.cloud.bean.SignUserdata;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.ui.HorizontalListViewAdapter;
import com.link.cloud.ui.RollListView;
import com.orhanobut.logger.Logger;

import butterknife.Bind;

/**
 * Created by 30541 on 2018/5/28.
 */

public class LessonFragmenttest_1 extends BaseFragment {
    @Bind(R.id.lessonmessageInfo)
    RollListView horizontalListView;
    String[] lessonId,lessonName,lessonDate;
    private HorizontalListViewAdapter hListViewAdapter;
    View.OnClickListener clickListener;
    public static LessonFragmenttest_1 newInstance() {
        Logger.e("SignFragment_Two-------newInstance");
        LessonFragmenttest_1 fragment = new LessonFragmenttest_1();
//        Bundle args = new Bundle();
//        args.putString("membername",userdata.getSigndata().getUserInfo().getName());
//        args.putInt("sex",userdata.getSigndata().getUserInfo().getSex());
//        args.putInt("usertype",userdata.getSigndata().getUserInfo().getUserType());
//        args.putString("userphone",userdata.getSigndata().getUserInfo().getPhone());
//        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        for (int i=0;i<1;i++) {
            lessonId[i]="222222";
            lessonName[i]="高温瑜伽";
            lessonDate[i]="2018/05/28-2018/05/28";
        }
        hListViewAdapter=new HorizontalListViewAdapter(getContext(),1,"张教练","王大壮","13022222222",lessonId[0],lessonName[0],lessonDate[0]);
        horizontalListView.setAdapter(hListViewAdapter);
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                hListViewAdapter.setSelectIndex(position);
//                selectPosition = position;
//                lessonnum=lessonResponse.getLessonResponse().lessonInfo[position].getLessonId();
//                hListViewAdapter.notifyDataSetChanged();
            }
        });
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId()) {
                    case R.id.up_lesson:
//                        uplesson();
                        Logger.e("eliminateSuccess========="+"R.id.up_lesson");
                        break;
                    case R.id.next_lesson:
//                        nextlesson();
                        Logger.e("eliminateSuccess========="+"next_lesson");
                        break;
                }
            }
        };
//        up_lesson.setOnClickListener(clickListener);
//        next_lesson.setOnClickListener(clickListener);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.lesson_layout;
    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void onVisible() {

    }
}
