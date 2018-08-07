package com.link.cloud.greendaodemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.link.cloud.BaseApplication;
import com.link.cloud.R;
import com.link.cloud.greendao.gen.PersonDao;



import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class TestGreenDAOActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView myListView;


    private List<Person> userList2 = new ArrayList<Person>();

    private PersonDao personDao;
    EditText et_userName;
    EditText et_deleteUserName;
    EditText et_preUserId;
    EditText et_afterUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_green_dao);
        Button btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        Button btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);
        Button btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);
        Button btnDeleteByName = (Button) findViewById(R.id.btn_deteleByName);
        btnDeleteByName.setOnClickListener(this);
        et_preUserId = (EditText) findViewById(R.id.et_preUserId);
        et_afterUserName = (EditText) findViewById(R.id.et_afterName);
        et_userName = (EditText) findViewById(R.id.et_userName);
        myListView = (ListView) findViewById(R.id.my_listView);
        et_deleteUserName = (EditText) findViewById(R.id.et_deteleUserName);
        //初始化数据库
        personDao = BaseApplication.getInstance().getDaoSession().getPersonDao();
//        person  = BaseApplication.getInstance().getDaoSession().getPersonDao();
//        initData();
    }

    void initData() {
//        queryData();
//        myAdapter = new MyAdapter(userList, TestGreenDAOActivity.this);
//        myListView.setAdapter(myAdapter);
    }

    /**
     * 方法名：addData()
     * 方法描述：增加数据
     */

    void addData() {
        try {
            String userName = et_userName.getText().toString().trim();
            if (userName == null || userName.equals("")) {
                Toast.makeText(TestGreenDAOActivity.this, "添加为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            Person mUser1 = new Person();
//            mUser1.setUserType(1);
//            mUser1.setUid("hello");
            mUser1.setName(userName);
//            mUser1.setNumber("1144454745");
//            mUser1.setSex(1);
//            mUser1.setImg("fdsgdsgs");
//            mUser1.setFingermodel("ooooooooooooooooooooooo");
//            mUser1.setCardname("555555");
//            mUser1.setCardnumber("5dafasfads");
//            mUser1.setBegintime("dafsafas");
//            mUser1.setEndtime("5dasfdasf");
            personDao.insert(mUser1);
//            initData();
            Toast.makeText(TestGreenDAOActivity.this, "添加完成", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(TestGreenDAOActivity.this, "添加了相同数据", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 方法名：deleteData()
     * 方法描述：根据姓名删除数据
     */
    void deleteData() {

        String userName = et_deleteUserName.getText().toString().trim();
        if (userName == null || userName.equals("")) {
            Toast.makeText(TestGreenDAOActivity.this, "输入为空！", Toast.LENGTH_SHORT).show();
            return;
        }

//        List<User> listUser = queryDataByName(userName);
//        for (int i = 0; i < listUser.size(); i++) {
//            User user = listUser.get(i);
//            //根据Entity删除数据
//            userDao.delete(user);
//
//            //根据Id删除数据
////        userDao.deleteByKey(user.getId());
//        }

        initData();
        Toast.makeText(TestGreenDAOActivity.this, "删除完成", Toast.LENGTH_SHORT).show();
    }

    /**
     * 方法名：deleteAllData()
     * 方法描述：删除所有数据
     */
    void deleteAllData() {

        initData();
        Toast.makeText(TestGreenDAOActivity.this, "删除完成", Toast.LENGTH_SHORT).show();
    }

    /**
     * 方法名：queryData()
     * 方法描述：查询全部数据
//     */
//    void queryData() {
//        userList = userDao.loadAll();
//    }
//

    /**
     * 方法名：queryDataByName
     * 方法描述：根据姓名查询数据
     *
     * @param name 姓名
     */
//    List<Person> queryDataByName(String name) {
//        QueryBuilder qb = userDao.queryBuilder();
//        List<Person> users = qb.where(PersonDao.Properties.Name.eq(name)).list();
//        //查询姓名为小红且id为1的数据，并以id为升序排列
////        List<User> users = qb.where(qb.and(UserDao.Properties.Name.eq("小红"),UserDao.Properties.Id.eq((long)1)))
////                .orderAsc(UserDao.Properties.Id).list();
//        return users;
//    }

    /**
     * 方法名：updateData()
     * 方法描述：根据id更改数据
     */
    void updateData() {
        String preUserId = et_preUserId.getText().toString().trim();
        String afterUserName = et_afterUserName.getText().toString().trim();
        if(preUserId==null||preUserId.equals("")||afterUserName==null||afterUserName.equals("")) {
            Toast.makeText(TestGreenDAOActivity.this, "输入为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Long userId = Long.parseLong(preUserId);
//        User findUser = userDao.queryBuilder().where(UserDao.Properties.Id.eq(userId)).build().unique();
//        if (findUser != null) {
//            findUser.setName(afterUserName);
//            userDao.update(findUser);
//        }
        Toast.makeText(TestGreenDAOActivity.this, "更改完成", Toast.LENGTH_SHORT).show();
        initData();
    }

    /**
     * 方法名：executeSql()
     * 方法描述：执行sql语句
     */
    void executeSql() {
        try {
            String sql = "select ADD_TEST from USERS_TABLE where userName = '小明'";
            MyGreenDAOApplication.getInstances().getDaoSession().getDatabase().execSQL(sql);
        }catch (Exception e){
            return;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                addData();
                break;
            case R.id.btn_delete:
                deleteAllData();
                break;
            case R.id.btn_update:
                updateData();
                break;
            case R.id.btn_deteleByName:
                deleteData();
                break;
        }
    }
}
