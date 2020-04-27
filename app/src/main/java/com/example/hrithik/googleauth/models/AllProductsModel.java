package com.example.hrithik.googleauth.models;

import androidx.annotation.NonNull;

public class AllProductsModel {
    String deviceName, has;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getHas() {
        return has;
    }

    public void setHas(String has) {
        this.has = has;
    }

    @NonNull
    @Override
    public String toString() {
        return deviceName;
    }
}
