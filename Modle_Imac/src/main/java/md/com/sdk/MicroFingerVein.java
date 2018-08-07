package md.com.sdk;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Handler;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/12/17.
 */

@SuppressWarnings("ALL")
public class MicroFingerVein {
    //需要给usb授权
    public static  final   int   USB_HAS_REQUST_PERMISSION=100;
    //连接成功
    public static  final    int  USB_CONNECT_SUCESS=101;
    //连接断开
    public static  final    int  USB_DISCONNECT=102;

    public static  final    int UsbDeviceConnection=103;
    public float fImageEnergy=0.0f;//output
    public float fEnergyThreshold=0.04f;//input

    //设备类型    0: 单侧  1:薄型
    public int     devType=1;
    HashMap<Integer,Integer> mapHandle =new HashMap<>();
    //处理的上下文，可以在里面添加成功Handler handler进行事件处理
    //Activity ctx=null;
    UsbManager mUsbManager;
    private static MicroFingerVein instance;
    public static MicroFingerVein getInstance(Context _ctx){
        if(instance != null){
            if(instance.ctx != _ctx){
                instance = null;
            }
        }
        if(instance == null){
            instance = new MicroFingerVein(_ctx);
        }
        return  instance;
    }
    private Context ctx;
    public MicroFingerVein(Context _ctx){
        ctx=_ctx;
        mUsbManager=(UsbManager)ctx.getSystemService(Context.USB_SERVICE);
        android.hardware.usb.UsbDeviceConnection uc;
    }
    private MicroFingerVein(){
    }
    //打开设备
    public boolean   fvdev_open(){
         return fvdev_open(0);
    }
    public boolean  fvdev_open(int index){
        int usbHandel = fvdevOpen(index);
        if(usbHandel==0){
            return false;
        }
        mapHandle.put(index,usbHandel);
        return true;
    }
    //获取设备个数
    public int   fvdev_get_count(){
        return fvdevGetCount();
    }
    //获取手指状态   0    没有手指    3手指按压
    public int  fvdev_get_state(int index)  {
        if(!mapHandle.containsKey(index))
            return 0;
        int []state=new int[1];
        if(!fvdevGetState(mapHandle.get(index),state)){
            if (ctx != null){
                Class ctxClass=ctx.getClass();
                try {
                    Field f = ctxClass.getDeclaredField("handler");
                    f.setAccessible(true);
                    Handler h=(Handler) f.get(ctx);
                    h.sendEmptyMessage(USB_DISCONNECT);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return 0;
        }
        return state[0];
    }
    public int  fvdev_get_state()  {
        return fvdev_get_state(0);
    }

    //采集
    public  byte[] fvdev_grab(int index){
        if(!mapHandle.containsKey(index))
            return null;
        return fvdevGrabImage(mapHandle.get(index));
    }
    public  byte[] fvdev_grab(){
        return fvdev_grab(0);
    }
    //质量评估   0为正常
    public int  fv_quality(byte []img){
        return fvQuality(img);
    }
    //提取模板
    public byte[] fv_extract_model(byte []img1, byte[]img2, byte[]img3){
        return fvExtraceFeature(img1,img2,img3);
    }
    //比对
    public boolean fv_index(byte []feature, int featureNum, byte []img, int []pos, float []score){
        return fvSearchFeature(feature,featureNum,img,pos,score);
    }
    //关闭
    public void close(int index){
        if(!mapHandle.containsKey(index))
            return ;
        fvdevClose(mapHandle.get(index));
        mapHandle.remove(index);
    }
    public void close(){
        fvdevClose(0);
    }
    public void reboot(int index){
        if(!mapHandle.containsKey(index))
            return ;
        fvdevReboot(mapHandle.get(index));
    }
    public void reboot(){
        reboot(0);
    }
    public int   getNo(int index){
        if(!mapHandle.containsKey(index))
            return -1;
        int []no=new int[1];
        fvdevGetNo(mapHandle.get(index),no);
        return no[0];
    }
    public int   getNo(){return getNo(0);}
    public int   setNo(int index,int no){
        if(!mapHandle.containsKey(index))
            return -1;
        return fvdevSetNo(mapHandle.get(index),no);
    }
    public int   setNo(int no){
        return setNo(0,no);
    }
    static  public int   debugLevel=0;
    static  public String debugStr;
    static  private native int  fvdevGetCount();
    static  private native int       fvdevOpen(int index);
    //static  private native boolean  fvdevOpenWithFd(int fd);
    static  private native boolean  fvdevGetState(int h,int []state);
    //static  public native byte []  fvdevGrabImage();
    static private native byte[] fvdevGrabImage(int h);
    static  private native  int     fvQuality(byte[]img);
    static  private native byte[]  fvExtraceFeature(byte []img1, byte[]img2, byte[]img3);
    static private  native boolean  fvSearchFeature(byte[]feature,int featureNum, byte[]img, int []pos, float []score);
    static private native void      fvdevClose(int h);
    static private native void      fvdevReboot(int h);
    static private native int       fvdevSetNo(int h,int no);
    static private native int       fvdevGetNo(int h,int []no);
    static {
        System.loadLibrary("FingerVein");
    }
}
