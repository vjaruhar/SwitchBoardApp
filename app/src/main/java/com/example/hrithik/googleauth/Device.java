package com.example.hrithik.googleauth;

public class Device {
    private String belongsTo;
    private String deviceName;

    public Device(String deviceName, String belongsTo) {
        this.belongsTo = belongsTo;
        this.deviceName = deviceName;
    }
    public Device(){

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
