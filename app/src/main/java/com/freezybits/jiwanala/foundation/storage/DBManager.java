package com.freezybits.jiwanala.foundation.storage;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.freezybits.jiwanala.R;

import java.util.Map;
import java.util.Set;

public class DBManager {
    private static DBManager instance;
    protected Application application;
    protected SharedPreferences sharedPreferences;

    public DBManager(Application app) {
        this.application = app;
    }

    public static DBManager getInstance(Application app) {
        if (DBManager.instance == null) {
            DBManager.instance = new DBManager(app);
        }
        return DBManager.instance;
    }

    public Context getContext() {
        return this.application.getApplicationContext();
    }

    protected String getContextStringResourceKey(int key) {
        return this.application.getString(key);
    }

    protected SharedPreferences getSharedPreferences() {
        if (this.sharedPreferences == null) {
            this.sharedPreferences = this.application.getSharedPreferences(
                    getContextStringResourceKey(R.string.db_key_name),
                    this.getContext().MODE_PRIVATE
            );
        }
        return this.sharedPreferences;
    }

    public boolean addPreference(String key, String value) {
        SharedPreferences.Editor edit = this.getSharedPreferences().edit();
        edit.putString(key, value);
        return edit.commit();
    }

    public boolean addPreference(int key, String value) {
        return this.addPreference(getContextStringResourceKey(key), value);
    }

    public boolean addPreference(String key, Boolean value) {
        SharedPreferences.Editor edit = this.getSharedPreferences().edit();
        edit.putBoolean(key, value);
        return edit.commit();
    }

    public boolean addPreference(int key, Boolean value) {
        return this.addPreference(getContextStringResourceKey(key), value);
    }

    public boolean addPreference(String key, Float value) {
        SharedPreferences.Editor edit = this.getSharedPreferences().edit();
        edit.putFloat(key, value);
        return edit.commit();
    }

    public boolean addPreference(int key, Float value) {
        return this.addPreference(getContextStringResourceKey(key), value);
    }

    public boolean addPreference(String key, Integer value) {
        SharedPreferences.Editor edit = this.getSharedPreferences().edit();
        edit.putInt(key, value);
        return edit.commit();
    }

    public boolean addPreference(int key, Integer value) {
        return this.addPreference(getContextStringResourceKey(key), value);
    }

    public boolean addPreference(String key, Long value) {
        SharedPreferences.Editor edit = this.getSharedPreferences().edit();
        edit.putLong(key, value);
        return edit.commit();
    }

    public boolean addPreference(int key, Long value) {
        return this.addPreference(getContextStringResourceKey(key), value);
    }

    public boolean addPreference(String key, Set<String> value) {
        SharedPreferences.Editor edit = this.getSharedPreferences().edit();
        edit.putStringSet(key, value);
        return edit.commit();
    }

    public boolean addPreference(int key, Set<String> value) {
        return this.addPreference(getContextStringResourceKey(key), value);
    }

    public String getPreference(String key, String defValue) {
        return this.getSharedPreferences().getString(key, defValue);
    }

    public String getPreference(int key, String defValue) {
        return this.getPreference(getContextStringResourceKey(key), defValue);
    }

    public Boolean getPreference(String key, Boolean defValue) {
        return this.getSharedPreferences().getBoolean(key, defValue);
    }

    public Boolean getPreference(int key, Boolean defValue) {
        return this.getPreference(getContextStringResourceKey(key), defValue);
    }

    public Float getPreference(String key, Float defValue) {
        return this.getSharedPreferences().getFloat(key, defValue);
    }

    public Float getPreference(int key, Float defValue) {
        return this.getPreference(getContextStringResourceKey(key), defValue);
    }

    public Integer getPreference(String key, Integer defValue) {
        return this.getSharedPreferences().getInt(key, defValue);
    }

    public Integer getPreference(int key, Integer defValue) {
        return this.getPreference(getContextStringResourceKey(key), defValue);
    }

    public Long getPreference(String key, Long defValue) {
        return this.getSharedPreferences().getLong(key, defValue);
    }

    public Long getPreference(int key, Long defValue) {
        return this.getPreference(getContextStringResourceKey(key), defValue);
    }

    public Set<String> getPreference(String key, Set<String> defValue) {
        return this.getSharedPreferences().getStringSet(key, defValue);
    }

    public Set<String> getPreference(int key, Set<String> defValue) {
        return this.getPreference(getContextStringResourceKey(key), defValue);
    }

    public Map<String, ?> getPreferences() {
        return this.getSharedPreferences().getAll();
    }
}