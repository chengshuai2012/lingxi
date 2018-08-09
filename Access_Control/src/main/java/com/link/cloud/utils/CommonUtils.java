package com.link.cloud.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {
    private final static String TAG=CommonUtils.class.getSimpleName()+"_DEBUG";

    /**
     *  返回app的版本号，即定义在build.gradle(Module:app)中的versionName标签值；
     */
    public static String getAppVersionName(Context context){
        try {
            PackageInfo info=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e){
            Log.e(TAG,"get app version name failed."+e.toString());
            return "";
        }
    }

    /**
     *  返回sd卡路径，如“storage/sdcard0”注意结尾不带分隔符；
     */
    public static String getSdCardDir(){
        String dir=null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dir=Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            dir=Environment.getDataDirectory().getAbsolutePath();
        }
        return dir;
    }

    /**
     *  将bt1，bt2两个字节数组按顺序连接成一个大字节数组并返回；
     */
    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    public final static long CLICK_INTERVAL_USUAL=1500L;
    private static long lastClickTime=0L;
    public final static boolean ignoreRepeatClick(){
        return ignoreRepeatClick(CLICK_INTERVAL_USUAL);
    }
    /**
     *  忽略指定时间间隔内的高频重复的点击；
     */
    public final static boolean ignoreRepeatClick(long clickInterval){
        if(System.currentTimeMillis()-lastClickTime<clickInterval){
            Log.e(TAG,"invalid repeat click,ignore it.");
            return true;
        }
        lastClickTime=System.currentTimeMillis();
        return false;
    }

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    /**
     *  返回一个yyyy_MM_dd_HH_mm_ss的日期字符串
     */
    public static String getSysDateTimeStr(long currentTimeMillis){
        Date date = new Date(currentTimeMillis);
        return  simpleDateFormat.format(date);
    }

    /**
     *  写字符串到文件中
     */
    public static boolean writeString2File(final String fileName,final String cnt) {
        return writeByte2File(fileName,cnt.getBytes());
    }

    /**
     * 将字节数组内容写入到占位文件
     */
    public static boolean writeByte2File(final String fileName,final byte[] frag) {
        File file=new File(fileName);
        if(file.exists()) file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writeByte2File(file,0,frag,0,frag.length);
    }

    /**
     * 将字节数组内容写入到占位文件
     */
    public static boolean writeByte2File(File file, long fragStartOffset, byte[] frag, int fragOffset, int fragSize){
        try {
            if(!file.exists()){
                Log.e(TAG,"writeFileFragment():Occupy file not exist.");
                return false;
            }
            RandomAccessFile raFile=new RandomAccessFile(file,"rw");
            raFile.seek(fragStartOffset);
            raFile.write(frag,fragOffset,fragSize);
            raFile.close();
            Log.e(TAG,"save a file:"+file.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG,"writeFileFragment():IOException when save file fragment."+e.toString());
            return false;
        }
        return true;
    }

    /**
     * 删除空目录
     * dir 将要删除的目录路径
     */
    public static void deleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("successfully deleted empty directory: " + dir);
        } else {
            System.out.println("failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * dir 将要删除的文件目录
     * boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    public static boolean deleteDir(String dir) {
        File filedri=new File(dir);
        return deleteDir(filedri);
    }

    /**
     * bytes 一个完整jpg文件的字节数组；<Br/>
     * sampleSize 缩放比例，小于1时视为1，返回1/sampleSize大小的bmp图片；<Br/>
     * 从完整jpg文件的字节数组中获取图片并以bmp格式返回；
     */
    public static Bitmap getBitmapByBytes(byte[] bytes, int sampleSize){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        options.inJustDecodeBounds=false;
        options.inSampleSize=sampleSize;
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
    }
}
