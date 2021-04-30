package com.example.courceworkmobile;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanFilter;
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

public class bluetooth_device_list extends AppCompatActivity {
    public MainActivity mainAct = new MainActivity();
    public BluetoothAdapter bluetoothAdapter;
    public List<Object> deviceList = new ArrayList<>();
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device_list);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, 2);
        }
        ScanFilter scanFilter = new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(ENVIROMENTAL_SERVICE_UUID.toString())).build();
            RecyclerView bluetoothList = findViewById(R.id.bluetoothDeviceList);
            bluetoothList.setLayoutManager(new LinearLayoutManager(this));
            BluetoothListAdapter btla = new BluetoothListAdapter(this, deviceList);
            bluetoothList.setAdapter(btla);
        }




    private final BroadcastReceiver recevier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardware = device.getAddress();
            }
        }
    };
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(recevier);
    }

}


