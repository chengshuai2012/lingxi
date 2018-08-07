package com.link.cloud.greendaodemo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by 30541 on 2018/2/26.
 */
@Entity
public class Person {
    //设置Id,为Long类型,并将其设置为自增
    private Long id;
    private String uid;
    private String name;
    private String number;
    private int sex;
    private String pos;
    private String img;
    private String fingerId;
    private String cardname;
    private String cardnumber;
    private String begintime;
    private String endtime;
    private String feature;
    @Generated(hash = 1411349337)
    public Person(Long id, String uid, String name, String number, int sex,
            String pos, String img, String fingerId, String cardname,
            String cardnumber, String begintime, String endtime, String feature) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.number = number;
        this.sex = sex;
        this.pos = pos;
        this.img = img;
        this.fingerId = fingerId;
        this.cardname = cardname;
        this.cardnumber = cardnumber;
        this.begintime = begintime;
        this.endtime = endtime;
        this.feature = feature;
    }
    @Generated(hash = 1024547259)
    public Person() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNumber() {
        return this.number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public int getSex() {
        return this.sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public String getPos() {
        return this.pos;
    }
    public void setPos(String pos) {
        this.pos = pos;
    }
    public String getImg() {
        return this.img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getFingerId() {
        return this.fingerId;
    }
    public void setFingerId(String fingerId) {
        this.fingerId = fingerId;
    }
    public String getCardname() {
        return this.cardname;
    }
    public void setCardname(String cardname) {
        this.cardname = cardname;
    }
    public String getCardnumber() {
        return this.cardnumber;
    }
    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }
    public String getBegintime() {
        return this.begintime;
    }
    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }
    public String getEndtime() {
        return this.endtime;
    }
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
    public String getFeature() {
        return this.feature;
    }
    public void setFeature(String feature) {
        this.feature = feature;
    }


}
