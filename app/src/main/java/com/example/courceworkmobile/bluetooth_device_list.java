package com.example.courceworkmobile;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Filter;

public class bluetooth_device_list extends AppCompatActivity {
    public MainActivity mainAct = new MainActivity();
    public BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> deviceList = new ArrayList<>();
    public List<BluetoothDevice> cleanDeviceList = new ArrayList<>();
    public Context context = this;
    private BluetoothLeScanner scanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device_list);

        ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES).setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE).setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT).setReportDelay(0).build();


        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        scanner = btAdapter.getBluetoothLeScanner();
        if(scanner != null){
            scanner.startScan(null,scanSettings,scanCallback);
        }





        }

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            deviceList.add(result.getDevice());
            if(deviceList.size()>40){
                scanner.stopScan(scanCallback);

                for(int i=0;i<deviceList.size();i++) {
                    if (!cleanDeviceList.contains(deviceList.get(i))) {
                        cleanDeviceList.add(deviceList.get(i));
                    }

                }
                RecyclerView bluetoothList = findViewById(R.id.bluetoothDeviceList);
                bluetoothList.setLayoutManager(new LinearLayoutManager(context));
                BluetoothListAdapter btla = new BluetoothListAdapter(context, cleanDeviceList);
                bluetoothList.setAdapter(btla);
            }
        }
        @Override
        public void onBatchScanResults(List<ScanResult> results){
            System.out.println("test");
        }
        @Override
        public void onScanFailed(int errorCode){
            scanner.stopScan(scanCallback);
        }
    };


    public void onDestroy() {
        super.onDestroy();
        try {
            scanner.stopScan(scanCallback);
        }catch (Exception e){

        }

    }
    public void onStop(){
        super.onStop();
        try {
            scanner.stopScan(scanCallback);
        }catch (Exception e){

        }
    }

}


