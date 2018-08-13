package com.link.cloud.component;



import android.app.PendingIntent;

import android.app.Service;

import android.content.Context;

import android.content.Intent;

import android.graphics.Bitmap;

import android.hardware.usb.UsbDevice;

import android.hardware.usb.UsbManager;

import android.os.Binder;

import android.os.Build;

import android.os.Handler;

import android.os.IBinder;

import android.os.Message;

import android.util.Log;

import android.util.SparseBooleanArray;



import com.link.cloud.utils.CommonUtils;



import md.com.sdk.MicroFingerVein;



public class MdUsbService extends Service {

    private final static String TAG=MdUsbService.class.getSimpleName()+"_DEBUG";

    private final static String ACTION_USB_PERMISSION = "com.android.USB_PERMISSION";

    private MyBinder myBinder=new MyBinder();

    private MicroFingerVein microFingerVein;

    private SparseBooleanArray deviceStates=new SparseBooleanArray();



    private UsbMsgCallback usbMsgCallback;

    public interface UsbMsgCallback{

        /**

         *  当找到目标USB设备时返回其设备信息，如设备生产商名usbManufacturerName，设备名usbDeviceName，以便UI打印相关信息，此方法可以置空；

         */

        void onUsbConnSuccess(String usbManufacturerName, String usbDeviceName);

        /**

         *  当目标USB设备的USB连接断开时回调此方法，异常断开时在此通知UI；

         */

        void onUsbDisconnect();

    }



    private Handler handler=new Handler(new Handler.Callback() {

        @Override

        public boolean handleMessage(Message msg) {

            switch (msg.what) {

                case MicroFingerVein.USB_HAS_REQUST_PERMISSION: {//请求usb授权；

                    Log.e(TAG,"usb has request permission.");

                    UsbDevice usbDevice=(UsbDevice)msg.obj;

                    UsbManager mManager=(UsbManager)getSystemService(Context.USB_SERVICE);

                    PendingIntent mPermissionIntent=PendingIntent.getBroadcast(getApplicationContext(),0, new Intent(ACTION_USB_PERMISSION),0);

                    if(mManager!=null){

                        mManager.requestPermission(usbDevice,mPermissionIntent);

                    }else{

                        Log.e(TAG,"UsbManager is null.");

                    }

                    break;

                }

                case MicroFingerVein.USB_CONNECT_SUCESS: {//打印usb节点信息；

                    Log.e(TAG,"get usb connect info success.");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        UsbDevice usbDevice = (UsbDevice) msg.obj;

                        if(MdUsbService.this.usbMsgCallback!=null){

                            usbMsgCallback.onUsbConnSuccess(usbDevice.getManufacturerName(),usbDevice.getDeviceName());

                        }

                    }

                    break;

                }

                case MicroFingerVein.USB_DISCONNECT: {//usb 连接已断开，通知重连；

                    Log.e(TAG,"usb disconnected.");

                    if(MdUsbService.this.usbMsgCallback!=null){

                        usbMsgCallback.onUsbDisconnect();

                    }

                    break;

                }

                case MicroFingerVein.UsbDeviceConnection: {//接收device连接器对象

                    Log.e(TAG, "usb device connection received OK.");

                    break;

                }

                default: {

                    Log.e(TAG, "undefined msg!(what=" + msg.what + ")");

                    break;

                }

            }

            return false;

        }

    });

    //----------------------------------------------------------------------------------------------

    @Override

    public void onCreate() {

        super.onCreate();

        Log.e(TAG,"onCreate");

        microFingerVein= MicroFingerVein.getInstance(this);

    }

    @Override

    public IBinder onBind(Intent intent) {

        Log.e(TAG,"onBind");

        if(microFingerVein==null){

            microFingerVein= MicroFingerVein.getInstance(MdUsbService.this);

            Log.e(TAG,"microFingerVein is null,try reopen it.");

        }

        return myBinder;

    }

    @Override

    public boolean onUnbind(Intent intent) {

        Log.e(TAG,"onUnbind");

        return false;

    }

    @Override

    public void onDestroy() {

        super.onDestroy();

        if(microFingerVein!=null&& MicroFingerVein.fvdev_get_count()>0){

            int recOpenDevCount=deviceStates.size();

            for(int i=0;i<recOpenDevCount;i++){

                if(deviceStates.valueAt(i)){

                    microFingerVein.close(deviceStates.indexOfKey(i));

                }

            }

        }

        Log.e(TAG,"onDestroy");

    }

    //----------------------------------------------------------------------------------------------

    /**

     * 将传入的加密字节数组保存bmp转换为普通bmp并返回；

     */

    public static Bitmap chg2VisibleBmp(byte []img){

        if(img==null||img.length==0) return null;

        byte[] jpgVisible= MicroFingerVein.fvGetImage(img);

        return CommonUtils.getBitmapByBytes(jpgVisible);

    }

    /**

     *  对图片做质量评估，传入接收结果的数组fenergy[]大小为4时可以接收全部4个质评参数；

     *  fenergy[0]：质评结果，fenergy[1]：质评得分；

     *  fenergy[2]：fLeakRatio漏光值，fenergy[3]:fPress按压值；

     */

    public static int qualityImgEx(byte[]img,float []fenergy){

        return MicroFingerVein.fv_QualityEx(img,fenergy);

    }



    /**

     *  将传入的最多3个图片进行融合并返回融合模版数组；

     */

    public static byte[] extractImgModel(byte []img1, byte[]img2, byte[]img3){

        return MicroFingerVein.fv_extract_model(img1,img2,img3);

    }



    /**

     *  将传入的img提取特征并与由全部数据库指静脉特征首尾相连得到的字节数组feature进行对比，返回值为

     *  基本对比结果，当且仅当返回值为true并且对比得分score[0]>认证阈值时认为认证成功；

     */

    public static boolean fvSearchFeature(byte []feature,int featureNum, byte []img, int []pos, float []score){

        return MicroFingerVein.fv_index(feature,featureNum,img,pos,score);

    }



    public final static int getFvColorNONE(){//关闭指示灯

        return MicroFingerVein.COLOR_NONE;

    }

    public final static int getFvColorGREEN(){//绿灯

        return MicroFingerVein.COLOR_GREEN;

    }

    public final static int getFvColorRED(){//红灯

        return MicroFingerVein.COLOR_RED;

    }

    //----------------------------------------------------------------------------------------------

    public class MyBinder extends Binder {

        //------------------------------------------------------------------------------------------

        /**

         *  返回指静脉操作对象是否已经初始化完成；

         */

        public boolean isMicroFingerVeinOk(){

            return MdUsbService.this.microFingerVein!=null;

        }

        /**

         *  设置一个回调以便调用者获取及响应usb状态信息；

         */

        public void setOnUsbMsgCallback(UsbMsgCallback usbMsgCallback){

            MdUsbService.this.usbMsgCallback=usbMsgCallback;

        }



        /**

         *  打开指定索引的指静脉设备，已打开或打开成功则返回true,其他异常返回false；

         */

        public boolean openDevice(int index){

            if(microFingerVein!=null){

                if(MicroFingerVein.fvdev_get_count()<=0){

                    Log.e(TAG,"device count is 0 when try open device.(deviceIndex="+index+")");

                    deviceStates.clear();

                    return false;

                }

                if(deviceStates.get(index,false)){

                    Log.e(TAG,"device is already opened,no need to reopen.(deviceIndex="+index+")");

                    return true;

                }

                boolean isOpenOk=microFingerVein.fvdev_open(index);

                deviceStates.put(index,isOpenOk);

                return isOpenOk;

            }

            deviceStates.clear();

            microFingerVein= MicroFingerVein.getInstance(MdUsbService.this);

            Log.e(TAG,"open failed,microFingerVein is null,try getting microFingerVein instance again.");

            return false;

        }

        /**

         *  打开索引为0的指静脉设备，同openDevice(int index)；

         */

        public boolean openDevice(){

            return openDevice(0);

        }

        //-----------------------------------------------------------------

        /**

         *  关闭指定索引的设备，设备开关不再需要二次开发客户维护；

         */

        public boolean closeDevice(int index){

            return true;

        }

        /**

         *  关闭指定索引为0的设备；

         */

        public  boolean closeDevice(){

            return closeDevice(0);

        }

        //-----------------------------------------------------------------

        /**

         *  获取当前适配的设备类型，0为110单侧指静脉设备，1为148薄型指静脉设备；

         */

        public int getDeviceType(){

            if(microFingerVein!=null){

                return microFingerVein.devType;

            }

            Log.e(TAG,"microFingerVein is null when get deviceType.");

            return -1;

        }

        //-----------------------------------------------------------------

        /**

         *  获取当前连接的指静脉设备数量；

         */

        public int getDeviceCount(){

            int devCount= MicroFingerVein.fvdev_get_count();

            if(devCount<=0){

                devCount=0;

                deviceStates.clear();

            }

            return devCount;

        }

        //-----------------------------------------------------------------

        /**

         *  根据设备索引index获取设备编号，设备编号可以自定义，索引是设备的唯一标志；

         */

        public int getDeviceNo(int index){

            if(microFingerVein!=null){

                return microFingerVein.getNo(index);

            }

            Log.e(TAG,"microFingerVein is null when getDeviceNo().");

            return -1;

        }

        /**

         *  返回索引为0的设备编号，同getDeviceNo(int index)；

         */

        public int getDeviceNo(){

            return getDeviceNo(0);

        }



        /**

         *  为索引为index的设备设定自定义设备编号no;

         */

        public int setDeviceNo(int index,int no){

            if(microFingerVein!=null){

                return microFingerVein.setNo(index,no);

            }

            Log.e(TAG,"microFingerVein is null when setDeviceNo().");

            return -1;

        }

        /**

         *  为索引为0的设备设定自定义设备编号no;

         */

        public int setDeviceNo(int no){

            return setDeviceNo(0,no);

        }

        //-----------------------------------------------------------------

        /**

         *  返回索引号为index的设备当前按压状态，0无按压，1指腹按压，2指尖按压，3正常按压（指尖指腹都有按压）；

         */

        public int getDeviceTouchState(int index){

            if(microFingerVein!=null){

                return microFingerVein.fvdev_get_state(index);

            }

            Log.e(TAG,"microFingerVein is null when getDeviceTouchState().");

            return -1;

        }

        /**

         *  返回索引号为0的设备当前按压状态，同getDeviceState(int index);；

         */

        public int getDeviceTouchState(){

            return getDeviceTouchState(0);

        }

        //-----------------------------------------------------------------

        /**

         *  设置索引为index的110设备LED的颜色为color,当BFlush为true时常亮，当bFlush为false时间隔约1秒不停闪烁；

         *  若为148设备，则此方法无效；

         */

        public void setDeviceLed(int index,int color,boolean bFlush){

            if(microFingerVein!=null){

                microFingerVein.setLed(index,color,bFlush);

            }

            Log.e(TAG,"microFingerVein is null when setDeviceLed().");

        }

        /**

         *  设置索引为0的110设备LED颜色及状态，同setDeviceLed(int index,int color,boolean bFlush)；

         */

        public void setDeviceLed(int color,boolean bFlush){

            setDeviceLed(0,color,bFlush);

        }

        //-----------------------------------------------------------------

        /**

         *  尝试从索引为index的设备抓取一张图并返回，抓取失败则返回null;

         */

        public byte[] tryGrabImg(int index){

            if(microFingerVein!=null){

                return microFingerVein.fvdev_grab(index);

            }

            Log.e(TAG,"microFingerVein is null when tryGrabImg().");

            return null;

        }

        /**

         *  尝试从索引为0的设备抓取一张图并返回，tryGrabImg(int index)；

         */

        public byte[] tryGrabImg(){

            return tryGrabImg(0);

        }

        //-----------------------------------------------------------------

    }

    //----------------------------------------------------------------------------------------------

}