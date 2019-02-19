package com.freezybits.jiwanala.foundation.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.freezybits.jiwanala.BuildConfig;

import java.util.Map;
import java.util.Set;

public class DBManager {
    private static DBManager instance;
    protected Context context;
    SharedPreferences preferences;

    DBManager(Context context, SharedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;
    }

    public static DBManager getInstance(Context context) {
        if (DBManager.instance == null) {
            SharedPreferences shared = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
            DBManager.instance = new DBManager(context, shared);
        }
        return DBManager.instance;
    }

    protected String getContextStringResourceKey(int key) {
        return context.getString(key);
    }

    public SharedPreferences getSharedPreference() {
        return this.preferences;
    }

    public SharedPreferences.Editor getPreferenceEditor() {
        return this.preferences.edit();
    }

    public void addPreference(String key, String value) {
        this.getPreferenceEditor().putString(key, value);
    }

    public void addPreference(int key, String value) {
        this.getPreferenceEditor().putString(getContextStringResourceKey(key), value);
    }

    public void addPreference(String key, Boolean value) {
        this.getPreferenceEditor().putBoolean(key, value);
    }

    public void addPreference(int key, Boolean value) {
        this.getPreferenceEditor().putBoolean(getContextStringResourceKey(key), value);
    }

    public void addPreference(String key, Float value) {
        this.getPreferenceEditor().putFloat(key, value);
    }

    public void addPreference(int key, Float value) {
        this.getPreferenceEditor().putFloat(getContextStringResourceKey(key), value);
    }

    public void addPreference(String key, Integer value) {
        this.getPreferenceEditor().putInt(key, value);
    }

    public void addPreference(int key, Integer value) {
        this.getPreferenceEditor().putInt(getContextStringResourceKey(key), value);
    }

    public void addPreference(String key, Long value) {
        this.getPreferenceEditor().putLong(key, value);
    }

    public void addPreference(int key, Long value) {
        this.getPreferenceEditor().putLong(getContextStringResourceKey(key), value);
    }

    public void addPreference(String key, Set<String> value) {
        this.getPreferenceEditor().putStringSet(key, value);
    }

    public void addPreference(int key, Set<String> value) {
        this.getPreferenceEditor().putStringSet(getContextStringResourceKey(key), value);
    }

    public String getPreference(String key, String defValue) {
        return this.getSharedPreference().getString(key, defValue);
    }

    public String getPreference(int key, String defValue) {
        return this.getSharedPreference().getString(
                getContextStringResourceKey(key),
                defValue
        );
    }

    public Boolean getPreference(String key, Boolean defValue) {
        return this.getSharedPreference().getBoolean(key, defValue);
    }

    public Boolean getPreference(int key, Boolean defValue) {
        return this.getSharedPreference().getBoolean(
                getContextStringResourceKey(key),
                defValue
        );
    }

    public Float getPreference(String key, Float defValue) {
        return this.getSharedPreference().getFloat(key, defValue);
    }

    public Float getPreference(int key, Float defValue) {
        return this.getSharedPreference().getFloat(
                getContextStringResourceKey(key),
                defValue
        );
    }

    public Integer getPreference(String key, Integer defValue) {
        return this.getSharedPreference().getInt(key, defValue);
    }

    public Integer getPreference(int key, Integer defValue) {
        return this.getSharedPreference().getInt(
                getContextStringResourceKey(key),
                defValue
        );
    }

    public Long getPreference(String key, Long defValue) {
        return this.getSharedPreference().getLong(key, defValue);
    }

    public Long getPreference(int key, Long defValue) {
        return this.getSharedPreference().getLong(
                getContextStringResourceKey(key),
                defValue
        );
    }

    public Set<String> getPreference(String key, Set<String> defValue) {
        return this.getSharedPreference().getStringSet(key, defValue);
    }

    public Set<String> getPreference(int key, Set<String> defValue) {
        return this.getSharedPreference().getStringSet(
                getContextStringResourceKey(key),
                defValue
        );
    }

    public Map<String, ?> getPreferences() {
        return this.getSharedPreference().getAll();
    }
}
