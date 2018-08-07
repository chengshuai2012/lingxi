package com.link.cloud.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.link.cloud.BaseApplication;
import com.link.cloud.R;
import com.link.cloud.greendao.gen.PersonDao;
import com.link.cloud.greendaodemo.Person;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 30541 on 2018/3/3.
 */

public class Sign_select_activity extends Activity {
    @Bind(R.id.sign_finger_page)
    TextView layou_finger;
    @Bind(R.id.sign_code_page)
    TextView layou_code;
    @Bind(R.id.sign_face_page)
    TextView layou_face;
    Intent intent;
    PersonDao personDao;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sign_select);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
@OnClick({R.id.sign_face_page,R.id.sign_finger_page,R.id.sign_code_page})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_finger_page:
                intent = new Intent();
                intent.setClass(Sign_select_activity.this,SigeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                finish();
//                personDao = BaseApplication.getInstances().getDaoSession().getPersonDao();
//                try {
//                    Person mUser1 = new Person();
//                    mUser1.setUserType(1);
//                    mUser1.setUid("hello");
//                    mUser1.setName("1111111111");
//                    mUser1.setNumber("1144454745");
//                    mUser1.setSex(1);
//                    mUser1.setImg("fdsgdsgs");
//                    mUser1.setFingermodel("ooooooooooooooooooooooo");
//                    mUser1.setCardname("555555");
//                    mUser1.setCardnumber("5dafasfads");
//                    mUser1.setBegintime("dafsafas");
//                    mUser1.setEndtime("5dasfdasf");
//                    personDao.insert(mUser1);
//                    personDao.loadAll();
//                    Toast.makeText(Sign_select_activity.this, "添加完成", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    Logger.d("Errormessage"+e.getMessage());
//                    Toast.makeText(Sign_select_activity.this, "添加了相同数据"+e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.sign_code_page:

                break;
            case R.id.sign_face_page:

                break;
    }
}

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
