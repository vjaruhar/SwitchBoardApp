package com.example.hrithik.googleauth.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class PreferenceUtility {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;
    Context context;
    private static final String FIRST_LAUNCH = "firstLaunch";
    int MODE = 0;
    private static final String SWITCH_PREFERENCE = "www.avrn.in.switchPreference";

    public PreferenceUtility(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SWITCH_PREFERENCE, MODE);
        spEditor = sharedPreferences.edit();
    }

    public void saveSwitchBoardSwitchesNames(String deviceID, String deviceType, JSONObject switchNames){
        spEditor.putString(deviceID, switchNames.toString());
        spEditor.commit();
    }

    public JSONObject getSwitchBoardSwitchNames(String deviceID){
        String switches = sharedPreferences.getString(deviceID, null);
        if(switches != null){
            try {
                JSONObject switchNamesObject = new JSONObject(switches);
                return switchNamesObject;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return null;

    }

    public void saveSwitchName(String deviceID, String switchName, String key){
        String switches = sharedPreferences.getString(deviceID, null);
        JSONObject switchNamesObject;
        if(switches != null){
            try {
                switchNamesObject = new JSONObject(switches);
                switchNamesObject.put(key, switchName);
                spEditor.putString(deviceID, switchNamesObject.toString());
                spEditor.commit();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            switchNamesObject = new JSONObject();
            try {
                switchNamesObject.put(key, switchName);
                spEditor.putString(deviceID, switchNamesObject.toString());
                spEditor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
