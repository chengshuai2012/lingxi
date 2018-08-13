package com.link.cloud.utils;

import com.link.cloud.bean.Person;

import java.util.List;

import io.realm.Realm;

/**
 * Created by 49488 on 2018/8/13.
 */

public class FindAll {
    static  Realm realm;
    public static List<Person> getAll(){

        if(realm==null){
            realm = Realm.getDefaultInstance();
        }
       return realm.copyFromRealm(realm.where(Person.class).findAll());
    }

}
