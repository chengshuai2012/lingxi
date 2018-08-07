package com.link.cloud.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.link.cloud.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonUtil {


	public static JSONArray objectToArray(Object object) throws JSONException {
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		gsonb.setDateFormat(Constant.BASE_DATA_FORMAT);
		return new JSONArray(gson.toJson(object));
	}

	/**
	 * <code>toBean</code>
	 *
	 * @param <T>
	 * @param jsonString
	 * @param beanclass
	 * @return
	 * @description: TODO(json字符串转化为类)
	 */
	public static <T> T toBean(String jsonString, Class<T> beanclass) {
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.setDateFormat(Constant.BASE_DATA_FORMAT);
		Gson gson = gsonb.create();
		return gson.fromJson(jsonString, beanclass);
	}

	public static <T> T toBean(String jsonString, Type t) {
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.setDateFormat(Constant.BASE_DATA_FORMAT);
		Gson gson = gsonb.create();
		try {
			return gson.fromJson(jsonString, t);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * <code>toBean</code>
	 *
	 * @param <T>
	 * @param object
	 * @param beanclass
	 * @return
	 * @description: TODO(json对象转化为类)
	 */
	public static <T> T toBean(JSONObject object, Class<T> beanclass) {
		return toBean(object.toString(), beanclass);
	}

	/**
	 * 把字符串转换成JSON对象
	 *
	 * @param string
	 * @return
	 */
	public static JSONObject toJSONObject(String string) {
		try {
			return new JSONObject(string);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String toJson(Object obj) {
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.setDateFormat(Constant.BASE_DATA_FORMAT);
		Gson gson = gsonb.create();
		return gson.toJson(obj);
	}

	/**
	 * 把字符串转换成JSON数组对象
	 *
	 * @param string
	 * @return
	 */
	public static JSONArray toJSONArray(String string) {
		try {
			return new JSONArray(string);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Map json2Object(String jsonStr) {
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(jsonStr);
			Iterator<String> nameItr = jsonObj.keys();
			String name;
			Map<String, Object> outMap = new HashMap<String, Object>();
			while (nameItr.hasNext()) {
				name = nameItr.next();
				outMap.put(name, jsonObj.getString(name));
			}
			return outMap;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}


}
