package com.link.cloud.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.base.MessageResponse;
import com.link.cloud.bean.LessonResponse;
//import com.link.cloud.bean.UserUID;
import com.link.cloud.constant.Constant;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.view.ProgressHUD;
import com.orhanobut.logger.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/3.
 */
public class LessonFragment_Message extends BaseFragment{
//
//    @Bind(R.id.lesson_menber_img)
//    ImageView menber_img;
//    @Bind(R.id.lesson_coach_img)
//    ImageView coach_img;
//    @Bind(R.id.elm_menber_name)
//    TextView menber_name;
//    @Bind(R.id.elm_menber_phone)
//    TextView menber_phone;
//    @Bind(R.id.elm_menber_sex)
//    TextView menber_sex;
//    @Bind(R.id.elm_coach_name)
//    TextView coach_name;
//    @Bind(R.id.elm_lesson_name)
//    TextView lesson_name;
//    @Bind(R.id.elm_lesson_time)
//    TextView lesson_time;
//    @Bind(R.id.lesson_next)
//    Button nextbtn;
    MessageResponse message;
    String messagestr;

    private LessonResponse lesson;
    public static LessonFragment_Message newInstance(LessonResponse lesson) {
        LessonFragment_Message fragment = new LessonFragment_Message();
        Bundle args = new Bundle();
        args.putSerializable(Constant.EXTRAS_LESSON_INFO, lesson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onInvisible() {
    }
    @Override
    protected void onVisible() {
    }
    @Override
    protected int getLayoutId() {
        return 0;
    }
    @Override
    protected void initData() {
    }
    @Override
    protected void initListeners() {
    }
    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        messagestr = getActivity().getIntent().getStringExtra("elminitaLesson");
        if (messagestr != null) {
            toJsonArray(messagestr);
//            lesson_name.setText(message.getLessonName());
//            coach_name.setText(message.getCoachName());
//            menber_phone.setText(message.getMemberTel());
//            lesson_time.setText(message.getLessonDate());
//            menber_name.setText(message.getMemberName());
            Logger.e("EliminateLEssonInfo:initViews" + messagestr);
        }
    }

    public MessageResponse toJsonArray(String json){
        try {
            message=new MessageResponse();
            JSONArray arr = new JSONArray(json);
            JSONObject tem=arr.getJSONObject(0);
            message.setAppid(tem.getString("appid"));
            message.setDeviceID(tem.getString("deviceID"));
            message.setEliminateId(tem.getString("eliminateId"));
            message.setMemberid(tem.getString("memberId"));
            message.setMemberName(tem.getString("memberName"));
            message.setMemberTel(tem.getString("memberTel"));
            message.setCoachid(tem.getString("coachId"));
            message.setCoachName(tem.getString("coachName"));
//            message.setClerkId(tem.getString("clerkId"));
//            message.setClerkName(tem.getString("clerkName"));
            message.setLessonName(tem.getString("lessonName"));
            message.setLessonDate(tem.getString("lessonDate"));
//            userUID.setMemberUID(tem.getString("memberId"));
//            userUID.setCoachUID(tem.getString("coachId"));
//            userUID.setClerkUID(tem.getString("clerkId"));
			Logger.e("NewMainActivity"+message.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return message;
    }
//    @OnClick(R.id.lesson_next)
//    public void OnClick(View view){
//        if (messagestr!=null){
//            LessonFragment_test lessonFragment_test=LessonFragment_test.newInstance(message);
//            ((EliminateLessonMainFragment)getParentFragment()).addFragment(lessonFragment_test,1);
//        }else {
//            ProgressHUD.dismissProgress(getActivity(),false,"您还未预约课程，请联系管理员");
//        }
//
//    }

}
