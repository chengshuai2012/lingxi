package com.link.cloud.utils;

import android.util.Log;

import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKVersion;
import com.guo.android_extend.java.ExtInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gqj3375 on 2017/7/11
 */

public class FaceDB {
	private final String TAG = this.getClass().toString();

	public static String appid = "DFag11mhwPxQqpEbRecdqPNh9mrQYNCf33E2GPbsBbCs";
	public static String ft_key = "6JfS8ANnccfB3mVkRud6Hq5JKMKncxLkRmzDqes5tbjx";
	public static String fd_key = "6JfS8ANnccfB3mVkRud6Hq5RUkayoGzzq5zkteU8yYU9";
	public static String fr_key = "6JfS8ANnccfB3mVkRud6Hq5v8Mdev8NZ2zVqUNoWKNKv";
	public static String ag_key = "6JfS8ANnccfB3mVkRud6Hq6ATAA2euXGfkRiKNA3394N";
	public static String sx_key = "6JfS8ANnccfB3mVkRud6Hq6HcZR9X3ZpLcraZWqnSfHh";
	String mDBPath;
	AFR_FSDKEngine mFREngine;
	AFR_FSDKVersion mFRVersion;
	boolean mUpgrade;
	//public List<AFR_FSDKFace> mFaceList= new ArrayList<>();;
	public Map<String, AFR_FSDKFace> mFaceList = new HashMap<>();

	public FaceDB(String path) {
		mDBPath = path;
		mFRVersion = new AFR_FSDKVersion();
		mUpgrade = false;
		mFREngine = new AFR_FSDKEngine();
		AFR_FSDKError error = mFREngine.AFR_FSDK_InitialEngine(FaceDB.appid, FaceDB.fr_key);
		if (error.getCode() != AFR_FSDKError.MOK) {
			Log.e(TAG, "AFR_FSDK_InitialEngine fail! error code :" + error.getCode());
		} else {
			mFREngine.AFR_FSDK_GetVersion(mFRVersion);
			Log.d(TAG, "AFR_FSDK_GetVersion=" + mFRVersion.toString());
		}
	}

	public boolean loadFaces() {
		mFaceList.clear();
		try {
			File dir = new File(mDBPath);//文件夹dir
			File[] files = dir.listFiles();//文件夹下的所有文件或文件夹
			if (files == null) {
				return false;
			} else {
				for (int i = 0; i < files.length; i++) {
					Log.d(TAG, "load name:" + files[i].getName() + "'s face feature data.");
					FileInputStream fs = new FileInputStream(files[i].getAbsolutePath());
					ExtInputStream bos = new ExtInputStream(fs);
					AFR_FSDKFace afr = null;
					do {
						if (afr != null) {
							if (mUpgrade) {
								//upgrade data.
							}
							//mFaceList.add(afr);
							mFaceList.put(files[i].getName().substring(0, files[i].getName().indexOf(".")), afr);
						}
						afr = new AFR_FSDKFace();
					} while (bos.readBytes(afr.getFeatureData()));
					bos.close();
					fs.close();
				}
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
