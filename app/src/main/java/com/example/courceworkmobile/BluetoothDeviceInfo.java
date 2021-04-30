package com.example.courceworkmobile;

public class BluetoothDeviceInfo{
    public String deviceName;
    public String deviceHardware;
    public String code;

    public BluetoothDeviceInfo(){}

    public BluetoothDeviceInfo(String deviceName, String deviceHardware){
        this.deviceName=deviceName;
        this.deviceHardware=deviceHardware;
    }

    public String getDeviceName(){
        return deviceName;
    }
    public String getDeviceHardware() {return deviceHardware;}
    public String getCode() {return code;}

}
