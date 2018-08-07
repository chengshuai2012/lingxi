package com.link.cloud.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.link.cloud.constant.Constant;
import com.link.cloud.BaseApplication;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shaozy on 2016/8/13.
 */
public class Utils {
    /**
     * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11","yyyy-MM-dd","yyyy年MM月dd日").
     *
     * @param date       String 想要格式化的日期
     * @param oldPattern String 想要格式化的日期的现有格式
     * @param newPattern String 想要格式化成什么格式
     * @return String
     */
    public static String stringPattern(String date, String oldPattern, String newPattern) {
        if (date == null || oldPattern == null || newPattern == null)
            return "";
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern);        // 实例化模板对象
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern);        // 实例化模板对象
        Date d = null;
        try {
            d = sdf1.parse(date);   // 将给定的字符串中的日期提取出来
        } catch (Exception e) {
            Logger.e("转换出错:" + e.getMessage());
            return date;
        }
        return sdf2.format(d);
    }
        // 两次点击按钮之间的点击间隔不能少于1000毫秒
        private static final int MIN_CLICK_DELAY_TIME = 600;
        private static long lastClickTime;
        public static boolean isFastClick() {
            boolean flag = false;
            long curClickTime = System.currentTimeMillis();
            if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                flag = true;
            }
            lastClickTime = curClickTime;
            return flag;
        }
    private static Toast mToast=null;
    public static void showPromptToast(Context context, String promptWord) {
        if (mToast == null) {
            mToast = Toast.makeText(context, promptWord,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(promptWord);
        }
        mToast.show();
    }
    /**
     * 字节数组转换为十六进制字符串
     *
     * @param b
     *            byte[] 需要转换的字节数组
     * @return String 十六进制字符串
     */
    public static final String byte2hex(byte b[]) {
        if (b == null) {
            throw new IllegalArgumentException(
                    "Argument b ( byte array ) is null! ");
        }
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
    /**
     * 将长整型数字转换为日期格式的字符串
     *
     * @param time
     * @param format
     * @return
     */
    public static String long2DateString(long time, String format) {
        if (time > 0l) {
            if (isEmpty(format)) format = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sf = new SimpleDateFormat(format);
            Date date = new Date(time);
            return sf.format(date);
        }
        return "";
    }
    //计算字符串MD5值
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equalsIgnoreCase(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 签名算法
     * 1）公共头请求参数按照字符串大小顺序顺序排序:key = value + .... key = value.。例如：将foo=1,bar=2,baz=3 排序为bar=2,baz=3,foo=1
     * 2）参数名和参数值链接后，得到拼装字符串bar=2baz=3foo=1
     * 3）将AppSecret拼接到参数字符串尾进行md5加密后，再转化成字符串，格式是：byte2hex(md5(key1=value12key2=vlues2... AppSecret))
     */
    public static String generateSign(String version, String appKey, String dateTime) {
        String sign = null;
        StringBuilder sb = new StringBuilder("");
        String[] args = {"code=" + version,"datetime=" + dateTime,"key=" + appKey };
        for (String str : args) {
            sb.append(str);
        }
        sb.append(getMetaData(Constant.APP_SECRET));
//        Logger.e(sb.toString()+"android=============");
        try {
            sign = toMD5HexStr(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            Logger.e(e.getMessage());
            sign = "";
        } catch (UnsupportedEncodingException e) {
            Logger.e(e.getMessage());
            sign = "";
        } catch (Exception e) {
            Logger.e(e.getMessage());
            sign = "";
        }
        return sign;
    }

    public static String toMD5HexStr(String from) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] srcBytes = from.getBytes();
        md5.update(srcBytes);
        byte[] resultBytes = md5.digest();

        StringBuffer md5StrBuff = new StringBuffer();
        //将加密后的byte数组转换为十六进制的字符串,否则的话生成的字符串会乱码
        for (int i = 0; i < resultBytes.length; i++) {
            if (Integer.toHexString(0xFF & resultBytes[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & resultBytes[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & resultBytes[i]));
            }
        }
        return md5StrBuff.toString();
    }

    public static String getMetaData(String key) {
        Application application = BaseApplication.getInstance();
        ApplicationInfo appInfo = null;
        try {
            appInfo = application.getPackageManager().getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * MD5加密
     *
     * @param message 要进行MD5加密的字符串
     * @return 加密结果为32位字符串
     */
    public static String getMD5(String message) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(message.getBytes("UTF-8"));

            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                else
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        Logger.d("Utils=========="+md5StrBuff.toString().toUpperCase());
        return md5StrBuff.toString().toUpperCase();//字母大写
    }

    public static String byteArraytoString(byte[] bDataBuf, short sDataLen) {
        String strDataLogTemp = new String(bDataBuf);
        String strDataLog = strDataLogTemp.substring(0, sDataLen);
        return strDataLog;
    }

    /**
     * 产生指定范围内的随机数(仅限非负数)
     *
     * @param min        最小范围
     * @param containMin 是否包括这个最小范围(true:包括;false:不包括)
     * @param max        最大范围
     * @param containMax 是否包括这个最大范围(true:包括;false:不包括)
     * @return 正常情况:>=0  异常情况:-1
     */
    public static String generateVerificationCode(int min, boolean containMin, int max, boolean containMax) {
        if (min < 0 || max < 0) {
            return "";
        }
        if (min > max) {
            max = max ^ min;
            min = max ^ min;
            max = max ^ min;
        }
        Random random = new Random();
        if (containMin == true && containMax == true) {
            //产生min-max之间的随机数(包括min和max，即[min,max])
            return String.format("%04d", random.nextInt(max - min + 1) + min);
        } else if (containMin == true && containMax == false) {
            //产生min-max之间的随机数(包括min不包括max，即[min,max))
            return String.format("%04d", random.nextInt(max - min) + min);
        } else if (containMin == false && containMax == false) {
            //产生min-max之间的随机数(不包括min也不包括max，即(min,max))
            max = max - 1;
            return String.format("%04d", random.nextInt(max - min) + min + 1);
        } else {
            //产生min-max之间的随机数(不包括min包括max，即(min,max])
            min = min + 1;
            return String.format("%04d", random.nextInt(max - min + 1) + min);
        }
    }

    /**
     * 验证手机号是否符合大陆的标准格式
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNumberValid(String mobiles) {
        Pattern p = Pattern.compile("^1[3|4|5|6|7|8|9]\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static String getCurrentDeviceID() {
        Type resultType = new TypeToken<String>() {
        }.getType();
        try {
            return Reservoir.get(Constant.KEY_DEVICE_ID, resultType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean shortcut2DesktopCreated() {
        Type resultType = new TypeToken<Boolean>() {
        }.getType();
        try {
            return Reservoir.get(Constant.EXTRAS_SHORTCUT, resultType);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void refreshRegUserCnt(int tag) {
        ReservoirUtils.getInstance().refresh(Constant.KEY_REG_USER_COUNT, tag);
    }

    public static int getRegUserCnt() {
        Type resultType = new TypeToken<Integer>() {
        }.getType();
        try {
            return Reservoir.get(Constant.KEY_REG_USER_COUNT, resultType);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
