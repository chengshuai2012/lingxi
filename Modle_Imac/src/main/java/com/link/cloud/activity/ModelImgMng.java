package com.link.cloud.activity;

/**
 * 管理认证或建模过程用到的几张图片及其特征值的存取；
 */
class ModelImgMng {
    private final String TAG=ModelImgMng.class.getSimpleName()+"_DEBUG";
    private byte[] img1;
    private byte[] feature1;

    private byte[] img2;
    private byte[] feature2;

    private byte[] img3;
    private byte[] feature3;

    public ModelImgMng(){
        reset();
    }

    public void reset() {
        feature1=null;
        feature2=null;
        feature3=null;
        img1=null;
        img2=null;
        img3=null;
    }

    public boolean isAllImgDataOk(){
        return img1==null||img2==null||img3==null;
    }

    public void setImg1(final byte[] img1) {
        this.img1=img1;
    }
    public byte[] getImg1() {
        return this.img1;
    }
    public void setFeature1(final  byte[] feature1) {
        this.feature1=feature1;
    }
    public byte[] getFeature1() {
        return this.feature1;
    }

    public void setImg2(final byte[] img2) {
        this.img2=img2;
    }
    public byte[] getImg2() {
        return this.img2;
    }
    public void setFeature2(final  byte[] feature2) {
        this.feature2=feature2;
    }
    public byte[] getFeature2() {
        return this.feature2;
    }

    public void setImg3(final byte[] img3) {
        this.img3=img3;
    }
    public byte[] getImg3() {
        return this.img3;
    }
    public void setFeature3(final  byte[] feature3) {
        this.feature3=feature3;
    }
    public byte[] getFeature3() {
        return this.feature3;
    }
}