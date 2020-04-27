package com.example.hrithik.googleauth.models;

public class SwitchStatusModel {
    private String switchName, switchStatus;
    private boolean is_switch, is_speed, is_dimmer;
    private long speedValue, dimmerValue;

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public String getSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(String switchStatus) {
        this.switchStatus = switchStatus;
    }

    public boolean isIs_switch() {
        return is_switch;
    }

    public void setIs_switch(boolean is_switch) {
        this.is_switch = is_switch;
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

    public long getSpeedValue() {
        return speedValue;
    }

    public void setSpeedValue(long speedValue) {
        this.speedValue = speedValue;
    }

    public long getDimmerValue() {
        return dimmerValue;
    }

    public void setDimmerValue(long dimmerValue) {
        this.dimmerValue = dimmerValue;
    }
}
