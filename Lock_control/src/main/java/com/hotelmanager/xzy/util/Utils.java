package com.hotelmanager.xzy.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Utils {
	private static String filePath = "/sdcard/Logs/";
	public static String readFromFile(File f) {

		FileInputStream is;
		int count = 0;
		byte[] b = null;
		String str = null;
		try {
			is = new FileInputStream(f);
			while ((count = is.available()) > 0) {
				b = new byte[count];
				is.read(b);
			}
			str = new String(b, "gbk");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return str.trim().replace(" ", "");
		return str;
	}

	/**
	 * 十六进制的字符串转换为byte数组，比如"16 9C 00 00 00 AA 30";
	 **/
	public static byte[] conver16HexToByte(String hex16Str) {

		// 出去中间所有的空格
		String str = hex16Str.trim().replace(" ", "");
		char[] c = str.toCharArray();
		byte[] b = new byte[c.length / 2];
		for (int i = 0; i < b.length; i++) {
			int pos = i * 2;
			b[i] = (byte) ("0123456789ABCDEF".indexOf(c[pos]) << 4 | "0123456789ABCDEF".indexOf(c[pos + 1]));
		}
		return b;
	}

	/**
	 * 从assets目录中复制整个文件夹内容
	 *
	 * @param context
	 *            Context 使用CopyFiles类的Activity
	 * @param oldPath
	 *            String 原文件路径 如：/aa
	 * @param newPath
	 *            String 复制后路径 如：xx:/bb/cc
	 */
	public static void copyFilesFromassets(Context context, String oldPath, String newPath) {
		try {
			String fileNames[] = context.getAssets().list(oldPath);// 获取assets目录下的所有文件及目录名
			if (fileNames.length > 0) {// 如果是目录
				File file = new File(newPath);
				file.mkdirs();// 如果文件夹不存在，则递归
				for (String fileName : fileNames) {
					copyFilesFromassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
				}
			} else {// 如果是文件
				InputStream is = context.getAssets().open(oldPath);
				FileOutputStream fos = new FileOutputStream(new File(newPath));
				byte[] buffer = new byte[1024];
				int byteCount = 0;
				while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
																// buffer字节
					fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
				}
				fos.flush();// 刷新缓冲区
				is.close();
				fos.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 如果捕捉到错误则通知UI线程
			// MainActivity.handler.sendEmptyMessage(COPY_FALSE);
		}
	}

	// 将结束的数据写入到文件，路径SD卡sprt路径�??
	public static void write2File(byte[] recvData, String fileName, boolean isHexdata) {

		byte[] data = null;
		String sdState = Environment.getExternalStorageState();
		if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
			Log.w("sprt", "没有SD卡！");
			return;
		}
		File path = new File(filePath);
		if (!path.exists()) {
			Log.d("sprt", "creating the path:" + filePath);
			path.mkdir();
		}
		File file = new File(filePath, fileName);
		if (!file.exists()) {
			Log.d("sprt", "creating the file:" + fileName);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String str = bytesToHexString(recvData, recvData.length);
		if (isHexdata) {
			try {
				data = str.getBytes("gbk");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			data = recvData;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, true);
			fos.write(data);
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 
	 * @Description: TODO
	 * @param
	 * @return String
	 */
	public static String bytesToHexString(byte[] src, int datalength) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < datalength; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv).append(" ");
		}
		return stringBuilder.toString();
	}

}
