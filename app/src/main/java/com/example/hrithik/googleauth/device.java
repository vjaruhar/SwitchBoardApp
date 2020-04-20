package com.example.hrithik.googleauth;

public class device {
public String belongsTo;
public String deviceName;

    public device(String deviceName, String belongsTo) {
        this.belongsTo = belongsTo;
        this.deviceName = deviceName;
    }
    public device(){

    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
