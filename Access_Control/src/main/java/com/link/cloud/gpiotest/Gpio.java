package com.link.cloud.gpiotest;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Gpio {

	private static String exportPath;

	private static String directionPath;

	private static String permissionGpio;

	public static void gpioInt(String gpio_number) {
		exportPath = "echo " + gpio_number + " > /sys/class/gpio/export";
		directionPath = "echo out > " + " /sys/class/gpio/gpio" + gpio_number
				+ "/direction";
		permissionGpio = "chmod 0777 /sys/class/gpio/gpio"+gpio_number+"/value";
		chmod(exportPath);
		chmod(directionPath);
		chmod(permissionGpio);
	}

	public static int get(String paramString) {
		File localFile = new File("/sys/class/gpio/gpio" + paramString
				+ "/value");
		if (!localFile.exists())
			System.out.println(paramString + " not exist!");
		while (true) {
			try {
				FileReader localFileReader = new FileReader(localFile);
				char[] arrayOfChar = new char[1];
				int i = localFileReader.read(arrayOfChar, 0, 1);
				localFileReader.close();
				if (i == 1) {
					int j = arrayOfChar[0];
					if (j == 48)
						return 0;
					return 1;
				}
			} catch (FileNotFoundException localFileNotFoundException) {
				localFileNotFoundException.printStackTrace();
				return -1;
			} catch (IOException localIOException) {
				localIOException.printStackTrace();
			}
		}
	}

	public static int set(String paramString, int paramInt) {
		File localFile = new File("/sys/class/gpio/gpio" + paramString + "/value");
		if (!localFile.exists()) {
			System.out.println(paramString + "not exist!");
			return -1;
		}
		try {
			FileWriter localFileWriter = new FileWriter(localFile);
			localFileWriter.write(paramInt);
			localFileWriter.flush();
			localFileWriter.close();
			return 0;
		} catch (FileNotFoundException localFileNotFoundException) {
			//while (true)
				localFileNotFoundException.printStackTrace();
				return -1;
		} catch (IOException localIOException) {
			//while (true)
				localIOException.printStackTrace();
				return -1;
		}
	}
	public static void chmod(String instruct) {
		try {
			Process process = null;
			DataOutputStream os = null;
			process = Runtime.getRuntime().exec("su");
			//process.waitFor();
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(instruct);
			os.flush();
			os.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}