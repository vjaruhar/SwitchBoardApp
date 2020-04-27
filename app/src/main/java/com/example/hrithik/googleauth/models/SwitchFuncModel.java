package com.example.hrithik.googleauth.models;

public class SwitchFuncModel {
    private String switchName, extDeviceName, has;
    private boolean has_device;

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public String getExtDeviceName() {
        return extDeviceName;
    }

    public void setExtDeviceName(String extDeviceName) {
        this.extDeviceName = extDeviceName;
    }

    public String getHas() {
        return has;
    }

    public void setHas(String has) {
        this.has = has;
    }

    public boolean isHas_device() {
        return has_device;
    }

    public void setHas_device(boolean has_device) {
        this.has_device = has_device;
    }
}
