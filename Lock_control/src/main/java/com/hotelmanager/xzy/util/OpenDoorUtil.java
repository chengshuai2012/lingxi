package com.hotelmanager.xzy.util;

import android.util.Log;



/**
 * Created by lenovo on 2017/5/24.
 */
public class OpenDoorUtil {
    //byte[0]-byte[9] 开多门0-9门
    private byte[] door_tem={0x01,0x02,0x04,0x08,0x10,0x20,0x40, (byte) 0x80,0x01,0x02};
    private static final String TAG="hotelmanager";
    /**
     * 付款开门（开单门）
     */
    public  byte[] openOneDoor(int serialnum,int door){
        //mSerialmanager
        byte[] bytes=new byte[7];
        bytes[0]=0X55;
        bytes[1]=0X01;
        bytes[2]= (byte) 0XA0;
        bytes[3]= decimalToBinary(serialnum);
        bytes[4]=decimalToBinary(door);
        bytes[5]= (byte) (bytes[1]^bytes[2]^bytes[3]^bytes[4]);
        bytes[6]= (byte) 0XEE;
        Log.e(TAG, "openOneDoor: Utils="+  Utils.bytesToHexString(bytes,bytes.length));
        return  bytes;
    }
    /**
     * 全开
     */
    public byte[] openAllDoor(){
        byte[] bytes=new byte[7];
        bytes[0]=0x55;
        bytes[1]=0x01;
        bytes[2]= (byte) 0XA2;
        bytes[3]= (byte)0X03;
        bytes[4]=(byte)0XFF;
        bytes[5]= (byte)0X5F ;
        bytes[6]= (byte) 0xEE;
        Log.e(TAG, "openOneDoor: Utils="+  Utils.bytesToHexString(bytes,bytes.length));
        return  bytes;
    }
    /**
     * 补货开门（开多门）
     */
    public byte[] openMulDoor(String[] datalist){
        byte[] data=new byte[7];
            data[0]=0x55;
            data[1]=0x02;
            data[2]= (byte) 0xA1;
          //  data[3]=decimalToBinary(datalist.size());
            data[3]=getByte4(datalist);
            data[4]=getByte5(datalist);
            data[5]= (byte) (data[1]^data[2]^data[3]^data[4]);
            data[6]= (byte) 0xAA;
        Log.i(TAG, "openMulDoor: Utils="+  Utils.bytesToHexString(data,data.length));
            return data;
    }
    private byte getByte5(String[] datalist  ){
        byte[] data=new byte[9];
        for (int i=0;i<9;i++){
            data[i]=0x00;
        }
        for (int i=0;i<datalist.length;i++) {
            String sw=datalist[i];
            Log.i(TAG, "openMulDoor: getByte5=sw="+  sw.substring(10));
            if (sw.substring(10).equals("009")) {

            } else if (sw.substring(10).equals("010")) {

            }else if (sw.substring(10).equals("001")) {
                data[0]=0x01;
            }else if (sw.substring(10).equals("002")) {
                data[1]=0x02;
            }else if (sw.substring(10).equals("003")) {
                data[2]=0x04;
            }else if (sw.substring(10).equals("004")) {
                data[3]=0x08;
            }else if (sw.substring(10).equals("005")) {
                data[4]=0x10;
            }else if (sw.substring(10).equals("006")) {
                data[5]=0x20;
            }else if (sw.substring(10).equals("007")) {
                data[6]=0x40;
            }else if (sw.substring(10).equals("008")) {
                data[7]= (byte) 0x80;
            }
        }
        data[8]= (byte) (data[0]^data[1]^data[2]^data[3]^data[4]^data[5]^data[6]^data[7]);
        Log.i(TAG, "openMulDoor: getByte5="+  Utils.bytesToHexString(data,data.length));
        return data[8];
    }
    private byte  getByte4(String[] datalist  ) {
        byte[] bytes = {0x00, 0x00, 0x00};
        for (int i=0;i<datalist.length;i++) {
            String sw=datalist[i];
            Log.i(TAG, "openMulDoor: getByte4=sw="+  sw.substring(10));
            if (sw.substring(10).equals("009")) {
                Log.i(TAG, "openMulDoor: getByte4=sw=11"+  sw);
                bytes[0] = 0x01;
            } else if (sw.substring(10).equals("010")) {
                Log.i(TAG, "openMulDoor: getByte4=sw=22"+ sw);
                bytes[1] = 0x02;
            }
        }
        bytes[2] = (byte) (bytes[0] ^ bytes[1]);
        Log.i(TAG, "openMulDoor: getByte4="+  Utils.bytesToHexString(bytes,bytes.length));
        return bytes[2];
    }
        //十进制转换为字节
        private byte decimalToBinary(int count){
            byte[] data=new byte[1];
            switch (count) {
                case 1:
                    data[0]=0x01;
                    break;
                case 2:
                    data[0]=0x02;
                    break;
                case 3:
                    data[0]=0x03;
                    break;
                case 4:
                    data[0]=0x04;
                    break;
                case 5:
                    data[0]=0x05;
                    break;
                case 6:
                    data[0]=0x06;
                    break;
                case 7:
                    data[0]=0x07;
                    break;
                case 8:
                    data[0]=0x08;
                    break;
                case 9:
                    data[0]=0x09;
                    break;
                case 10:
                    data[0]=0x0A;
                    break;
            }
            return data[0];
}
}
