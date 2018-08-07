package com.link.cloud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 30541 on 2018/3/8.
 */

public class Lockdata extends ResultResponse{
    @SerializedName("data")
    User_Lessage_Lock lockdata;

    public void setLockdata(User_Lessage_Lock lockdata) {
        this.lockdata = lockdata;
    }

    public User_Lessage_Lock getLockdata() {
        return lockdata;
    }

    public  class User_Lessage_Lock  {
        @SerializedName("uid")
        String uid;
        @SerializedName("name")
        String name;
        @SerializedName("numberType")
        int numberType;
        @SerializedName("numberValue")
        String numberValue;
        @SerializedName("cabinetNumber")
        String cabinetnumber;

        public void setUid(String uid) {
            this.uid = uid;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNumberType(int numberType) {
            this.numberType = numberType;
        }

        public void setNumberValue(String numberValue) {
            this.numberValue = numberValue;
        }

        public void setCabinetnumber(String cabinetnumber) {
            this.cabinetnumber = cabinetnumber;
        }

        public String getUid() {
            return uid;
        }

        public String getName() {
            return name;
        }

        public int getNumberType() {
            return numberType;
        }

        public String getNumberValue() {
            return numberValue;
        }

        public String getCabinetnumber() {
            return cabinetnumber;
        }
    }
}
