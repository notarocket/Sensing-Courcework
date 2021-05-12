package com.example.courceworkmobile;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;

import android.view.View;


import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import static android.bluetooth.BluetoothGatt.GATT_READ_NOT_PERMITTED;
import static android.bluetooth.BluetoothGatt.GATT_SUCCESS;
import static android.bluetooth.BluetoothGatt.GATT_WRITE_NOT_PERMITTED;


public class MainActivity extends AppCompatActivity {
    public BluetoothDevice device;
    public List<String> deviceName = new ArrayList<>();
    public List<String> deviceHardware = new ArrayList<>();
    public RecyclerView pcrecyclerView;
    private RecyclerView.Adapter pcAdapter;
    public static Handler handler;
    private List<BluetoothGattService> services = new ArrayList<>();
    private List<BluetoothGattCharacteristic> characteristics = new ArrayList<>();
    public List<String> pcStatusList = new ArrayList<>();
    private Context context = this;
    private int position;
    private String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(
                this,
                new String[]
                        {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        }, 0);

        SharedPreferences sharedPreferences = getSharedPreferences("AddressDatabase", Context.MODE_PRIVATE);
        try {

            String tempStr;
            String[] tempdeviceHardware = sharedPreferences.getString("1234", "ERROR").split("=v=");
            String[] tempdeviceName = sharedPreferences.getString("12345", "ERROR").split("=xwvwx=");
            for (int i = 0; i < tempdeviceHardware.length; i++) {
                deviceHardware.add(tempdeviceHardware[i]);
                deviceName.add(tempdeviceName[i]);
            }
            if(deviceName.get(0) == ""){
                deviceName.remove(0);
                if(deviceHardware.get(0)==""){
                    deviceHardware.remove(0);
                }
            }
            deviceName.remove("ERROR");
            deviceHardware.remove("ERROR");
            if(pcStatusList.size() < 1){
                for(int i = 0; i<deviceHardware.size();i++) {
                    pcStatusList.add("unknown");
                }
            }
        } catch (Exception e) {

        }






    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);
        try{
            int delNum = getIntent().getIntExtra("delNum", -1);
            deviceName.remove(delNum);
            deviceHardware.remove(delNum);
            pcStatusList.remove(delNum);
            getIntent().removeExtra("delNum");
            pcAdapter.notifyDataSetChanged();
        }catch (Exception e){

        }
        boolean isTrue = getIntent().getBooleanExtra("isTrue",false);

        if(isTrue == true) {
            getIntent().removeExtra("isTrue");
            try {
                code = getIntent().getStringExtra("code");
                device = (BluetoothDevice) getIntent().getParcelableExtra("device");
                position = getIntent().getIntExtra("position",-1);
                getIntent().removeExtra("position");

                if(!getIntent().getBooleanExtra("notNew",false)==true) {
                    getIntent().removeExtra("notNew");
                    deviceHardware.add(device.getAddress());
                    deviceName.add("default");


                }
                BluetoothGatt btGatt = device.connectGatt(this, true, btCallback, BluetoothDevice.TRANSPORT_LE);

            } catch (Exception e) {
                System.out.println("test");
            }
        }

        pcrecyclerView = findViewById(R.id.recyclerView);
        pcrecyclerView.setLayoutManager(new LinearLayoutManager(context));
        pcAdapter = new pcAdapter(context, deviceName, deviceHardware,pcStatusList);
        pcrecyclerView.setAdapter(pcAdapter);


    }

    @Override
    protected void onStop() {
        super.onStop();
        String addressString = "";
        String nameString = "";
        try {
            for (int i = 0; i < deviceHardware.size(); i++) {
                addressString += deviceHardware.get(i) + "=v=";
            }
            for (int i = 0; i < deviceName.size(); i++) {
                nameString += deviceName.get(i) + "=xwvwx=";
            }

            SharedPreferences sharedPreferences = getSharedPreferences("AddressDatabase", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("1234");
            editor.remove("12345");
            editor.putString("1234", addressString);
            editor.putString("12345", nameString);
            editor.commit();
        }catch (Exception e){

        }
    }


    public void addPc(View view) {
        Intent btDevices = new Intent(MainActivity.this, bluetooth_device_list.class);
        startActivityForResult(btDevices, 1);
    }

    public void onOff(View view) {

        //send signal code goes here


    }


    private final BluetoothGattCallback btCallback = new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            if (status == GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt.discoverServices();

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    gatt.close();
                } else {

                }
            } else {
                gatt.close();
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (gatt.getServices() == null) {

            } else {
                services = gatt.getServices();
                for (int i = 0; i < services.size(); i++) {
                    if (services.get(i).getUuid().toString() == "a629a60a-d1c2-4f05-ad60-a8240c73a6ee") {
                        characteristics = services.get(i).getCharacteristics();
                    }
                }

                turnPcOn(gatt,code);


            }


        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == GATT_SUCCESS) {
                byte[] bytes= characteristic.getValue();
                String pcStatus = null;
                String input = Character.toString((char) bytes[0]);
                if(input.equals("y")){
                    pcStatus = "Pc ON";
                }else if(input.equals("n")){
                    pcStatus = "Pc failed to turn on";
                }else if(input.equals("c")){
                    pcStatus = "Incorrect Code";
                }else if(input.equals("o")){
                    pcStatus = "Pc OFF";
                }else{
                    pcStatus = "Unknown Error";
                }
                try {
                    pcStatusList.set(position, pcStatus);
                }catch (Exception e){

                }
                position=-1;
                gatt.close();
                updateRecyclerView();
            } else if (status == GATT_READ_NOT_PERMITTED) {

            } else {
                System.out.println(status);
            }
        }
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status){
            if (status == GATT_SUCCESS) {


                getTestValue(gatt);
            } else if (status == GATT_WRITE_NOT_PERMITTED) {
                System.out.println("test");
            } else {
                System.out.println(status);
            }
        }
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic){
            System.out.println("test");
        }



    };

    private void getTestValue(BluetoothGatt gatt) {
        try {
            BluetoothGattCharacteristic pcStatus = gatt.getService(UUID.fromString("a629a60a-d1c2-4f05-ad60-a8240c73a6ee")).getCharacteristic(UUID.fromString("2d2fa4ac-b22b-11eb-8529-0242ac130003"));
            gatt.readCharacteristic(pcStatus);
            System.out.println("test");
        } catch (Exception e) {
            System.out.println("Test");
        }
    }

    private void turnPcOn(BluetoothGatt gatt, String code){
        try{
            byte[] bytes = (code).getBytes();


            BluetoothGattCharacteristic pcOnOff = gatt.getService(UUID.fromString("a629a60a-d1c2-4f05-ad60-a8240c73a6ee")).getCharacteristic(UUID.fromString("39df25ce-b22b-11eb-8529-0242ac130003"));
            pcOnOff.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            pcOnOff.setValue(bytes);
            gatt.writeCharacteristic(pcOnOff);
        }catch (Exception e){

        }


    }
    private void updateRecyclerView(){

        pcAdapter.notifyDataSetChanged();
    }

    }








