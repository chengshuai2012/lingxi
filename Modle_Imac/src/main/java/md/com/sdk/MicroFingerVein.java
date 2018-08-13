package md.com.sdk;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
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

    //------------------------------------------
    public float fImageEnergy=0.0f;//output quality score everytime do a quality;
    public float fEnergyThreshold=0.05f;//inputed quality threshold;
    //------------------------------------------

    //------------------------------------------
    public static final int COLOR_NONE=0;             //关闭指示灯
    public static final int COLOR_GREEN=1;            //绿灯
    public static final int COLOR_RED=2;              //红灯
    static private native void  fvdevSetLed(int h,int color,boolean bFlush);//bFlush为true常亮，为false闪烁，勿重复调用否则闪烁将会不明显
    public void setLed(int index, int color, boolean bFlush){
        if(!mapHandle.containsKey(index))
            return;
        fvdevSetLed(mapHandle.get(index),color,bFlush);
    }
    public void setLed(int color, boolean bFlush){
        setLed(0,color,bFlush);
    }
    //------------------------------------------

    public static native byte[] fvGetImage(byte []img);//return visible decoded imgBytes from gived encdoed invisible imgBytes;

    //设备类型  0: 单侧  1:薄型
    //适配110改为0，适配148时改为1；
    public int  devType=1;

    HashMap<Integer,Integer> mapHandle =new HashMap<>();
    //处理的上下文，可以在里面添加成功Handler handler进行事件处理
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
        UsbDeviceConnection  uc;
    }

    private MicroFingerVein(){
    }

    //打开设备
    public boolean fvdev_open(){
         return fvdev_open(0);
    }
    public boolean fvdev_open(int index){
        int usbHandel=fvdevOpen(index);
        if(usbHandel==0){
            return false;
        }
        mapHandle.put(index,usbHandel);
        return true;
    }
    //获取设备个数
    public static int fvdev_get_count(){
        return fvdevGetCount();
    }
    //获取手指状态   0    没有手指    3手指按压
    public int fvdev_get_state(int index)  {
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

    //质量评估 返回值  0为正常 ，2001
    public static int fv_quality(byte []img){
        return fvQuality(img);
    }
    //新的质量评估扩展接口，对img进行质量评估时将得分通过fenergy[4]数组返回；
    //fenergy[0]：质评结果，fenergy[1]：质评得分，fenergy[2]：fLeakRatio漏光值，fenergy[3]:fPress按压值
    public static int fv_QualityEx(byte[]img,float []fenergy){
        return fvQualityEx(img,fenergy);
    }

    //提取模板
    public static byte[] fv_extract_model(byte []img1, byte[]img2, byte[]img3){
        return fvExtraceFeature(img1,img2,img3);
    }

    //比对
    public static boolean fv_index(byte []feature, int featureNum, byte []img, int []pos, float []score){
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
    public int  getNo(int index){
        if(!mapHandle.containsKey(index))
            return -1;
        int []no=new int[1];
        fvdevGetNo(mapHandle.get(index),no);
        return no[0];
    }
    public int getNo(){return getNo(0);}
    public int setNo(int index,int no){
        if(!mapHandle.containsKey(index))
            return -1;
        return fvdevSetNo(mapHandle.get(index),no);
    }
    public int setNo(int no){
        return setNo(0,no);
    }

    static public  int               debugLevel=0;
    static public  String             debugStr;
    static private native int       fvdevGetCount();
    static private native int       fvdevOpen(int index);
    static private native boolean  fvdevGetState(int h,int []state);
    static private native byte[]    fvdevGrabImage(int h);
    static private native int       fvQuality(byte[]img);
    static private native int       fvQualityEx(byte[]img,float []fenergy);
    static private native byte[]    fvExtraceFeature(byte []img1, byte[]img2, byte[]img3);
    static private native boolean   fvSearchFeature(byte[]feature,int featureNum, byte[]img, int []pos, float []score);
    static private native void      fvdevClose(int h);
    static private native void      fvdevReboot(int h);
    static private native int       fvdevSetNo(int h,int no);
    static private native int       fvdevGetNo(int h,int []no);

    static {
        System.loadLibrary("FingerVein");
    }
}
