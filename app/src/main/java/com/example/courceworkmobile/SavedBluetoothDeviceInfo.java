package com.example.courceworkmobile;

public class SavedBluetoothDeviceInfo {
    public String deviceName;
    public String deviceHardware;
    public String deviceCode;

    public SavedBluetoothDeviceInfo(){}

    public SavedBluetoothDeviceInfo(String deviceName, String deviceHardware){
        this.deviceName=deviceName;
        this.deviceHardware=deviceHardware;
    }

    public String getDeviceName(){
        return deviceName;
    }
    public String getDeviceHardware() {return deviceHardware;}
    public String getDeviceCode() {return deviceCode;}

}
