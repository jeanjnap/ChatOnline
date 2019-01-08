package com.jeanjnap.chat.Util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesUtil {

    private Context context;
    private SharedPreferences preferences;

    public PreferencesUtil(Context context) {
        this.context = context;
    }

    public boolean putString(String key, String value){
        try {
            preferences = context.getSharedPreferences(Constants.PREFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(key, value);
            editor.apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getString(String key){
        preferences = context.getSharedPreferences(Constants.PREFERENCES,MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    public boolean putBoolean(Context context, String key, boolean value){
        try {
            preferences = context.getSharedPreferences(Constants.PREFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putBoolean(key, value);
            editor.apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean getBoolean(String key){
        preferences = context.getSharedPreferences(Constants.PREFERENCES,MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public void putInt (String key, int value) {
        try {
            preferences = context.getSharedPreferences(Constants.PREFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putInt(key, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getInt (String key) {
        preferences = context.getSharedPreferences(Constants.PREFERENCES,MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }
}
