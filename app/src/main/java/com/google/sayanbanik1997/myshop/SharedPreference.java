package com.google.sayanbanik1997.myshop;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;
    SharedPreference(Context context){
        sharedPreferences = context.getSharedPreferences("sharedPreferenceFile", MODE_PRIVATE);
        spEditor = sharedPreferences.edit();
    }

    protected void setData(String tag, String data){
        spEditor.putString(tag, data);
        spEditor.commit();
    }
    protected String getData(String tag){
        return sharedPreferences.getString(tag, null);
    }
    protected boolean ifExists(String tag){
        return sharedPreferences.contains(tag);
    }
    protected  void  removeData(String tag ) {
        spEditor.remove(tag);
        spEditor.commit();
    }
}
