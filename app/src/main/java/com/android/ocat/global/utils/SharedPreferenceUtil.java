package com.android.ocat.global.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPreferenceUtil {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferenceUtil(String filename, Context context) {
        sharedPreferences = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public Object getObject(String key, Class clz) {
        String str = sharedPreferences.getString(key, "");
        Gson gson = new Gson();
        return gson.fromJson(str, clz);
    }

    public void delete(String key) {
        editor.remove(key).commit();
    }

    public void clear() {
        editor.clear().commit();
    }
}
