package com.link.cloud.activity;

import android.app.Service;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import md.com.sdk.MicroFingerVein;

public class MdUsbService extends Service {
    private final static String TAG=MdUsbService.class.getSimpleName()+"_DEBUG";
    private final static String ACTION_USB_PERMISSION = "com.android.USB_PERMISSION";
    private MyBinder myBinder=new MyBinder();
    private MicroFingerVein microFingerVein;
//    private Handler handler=new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            switch (msg.what) {
//                case MicroFingerVein.USB_HAS_REQUST_PERMISSION: {//请求usb授权；
//                    Log.e(TAG,"usb has request permission.");
//                    UsbDevice usbDevice=(UsbDevice)msg.obj;
//                    UsbManager mManager=(UsbManager)getSystemService(Context.USB_SERVICE);
//                    PendingIntent mPermissionIntent = PendingIntent.getBroadcast(getApplicationContext(),0, new Intent(ACTION_USB_PERMISSION), 0);
//                    if(mManager==null){
//                        mManager=(UsbManager)getSystemService(Context.USB_SERVICE);
//                    }
//                    mManager.requestPermission(usbDevice,mPermissionIntent);
//                    break;
//                }
//                case MicroFingerVein.USB_CONNECT_SUCESS: {//打印usb节点信息；
//                    Log.e(TAG,"get usb connect info success.");
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        UsbDevice usbDevice = (UsbDevice) msg.obj;
//                        if(MdUsbService.this.usbMsgCallback!=null){
//                            usbMsgCallback.onUsbConnSuccess(usbDevice.getManufacturerName(),usbDevice.getDeviceName());
//                        }
//                    }
//                    break;
//                }
//                case MicroFingerVein.USB_DISCONNECT: {//usb 连接已断开；
//                    Log.e(TAG,"usb disconnected.");
//                    if(MdUsbService.this.usbMsgCallback!=null){
//                        usbMsgCallback.onUsbDisconnect();
//                    }
//                    break;
//                }
//                case MicroFingerVein.UsbDeviceConnection: {//接收device连接器对象；
//                    Log.e(TAG, "usb device connection OK.");
//                    if (msg.obj!=null) {
//                        if(MdUsbService.this.usbMsgCallback!=null){
//                            usbMsgCallback.onUsbDeviceConnection((UsbDeviceConnection)msg.obj);
//                        }
//                    }
//                    break;
//                }
//                default: {
//                    Log.e(TAG, "undefined msg!(what=" + msg.what + ")");
//                    break;
//                }
//            }
//            return false;
//        }
//    });

    private UsbMsgCallback usbMsgCallback;
    public interface UsbMsgCallback{
        /***
         *  when find a new usb device;
         *  @param usbManufacturerName manufacturer name of the new usb device;
         *  @param usbDeviceName device name of the new usb device;
         */
        void onUsbConnSuccess(String usbManufacturerName, String usbDeviceName);
        /**
         *  when the md usb device disconnected;
         */
        void onUsbDisconnect();
        /**
         *  when connect md device success;
         */
        void onUsbDeviceConnection(UsbDeviceConnection usbDevConn);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"onCreate");
        microFingerVein= MicroFingerVein.getInstance(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG,"onBind");
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
        Log.e(TAG,"onDestroy");
    }

    public class MyBinder extends Binder {
        /**
         *  return a MicroFingerVein object for operate md device;
         */
        public MicroFingerVein getMicroFingerVeinInstance(){
            return MdUsbService.this.microFingerVein;
        }
        /**
         *  set a UsbMsgCallback object for the service to callback custom operating of usb msg;
         */
        public void setOnUsbMsgCallback(UsbMsgCallback usbMsgCallback){
            MdUsbService.this.usbMsgCallback=usbMsgCallback;
        }
    }
}
