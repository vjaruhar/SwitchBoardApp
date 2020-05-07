package com.example.hrithik.googleauth.models;

public class SpeedAndDimmerModel {
    private boolean is_speed, is_dimmer;
    private int speedControlValue, dimmerValue;
    private String deviceType, device_no;

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }

    public boolean isIs_speed() {
        return is_speed;
    }

    public void setIs_speed(boolean is_speed) {
        this.is_speed = is_speed;
    }

    public boolean isIs_dimmer() {
        return is_dimmer;
    }

    public void setIs_dimmer(boolean is_dimmer) {
        this.is_dimmer = is_dimmer;
    }

    public int getSpeedControlValue() {
        return speedControlValue;
    }

    public void setSpeedControlValue(int speedControlValue) {
        this.speedControlValue = speedControlValue;
    }

    public int getDimmerValue() {
        return dimmerValue;
    }

    public void setDimmerValue(int dimmerValue) {
        this.dimmerValue = dimmerValue;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
