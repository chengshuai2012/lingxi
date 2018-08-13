package com.link.cloud.bean;



import java.io.Serializable;



public class MdDevice implements Serializable{

    private String name;

    private int no;

    private int index;



    public MdDevice(){

        this.name="MdDevice";

        this.no=0;

        this.index=-1;

    }



    public void setName(String name) {

        this.name=name;

    }

    public String getName(){

        return name;

    }



    public void setNo(int no){

        this.no=no;

    }

    public int getNo() {

        return no;

    }



    public void setIndex(int index) {

        this.index=index;

    }

    public int getIndex(){

        return this.index;

    }

}