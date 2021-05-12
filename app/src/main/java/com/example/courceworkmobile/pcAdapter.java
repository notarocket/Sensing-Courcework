package com.example.courceworkmobile;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class pcAdapter extends RecyclerView.Adapter<pcAdapter.ViewHolder> {

    public int position;
    public Button[] goButton;
    public Button[] delButton;
    private Context context;
    private BluetoothLeScanner scanner;
    private List<String> deviceName;
    private List<String> deviceHardware;
    private String code;
    private List<String> pcStatus;
    int inT;





    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;


        public ViewHolder(View v) {
            super(v);
            view = v;
        }

    }
    public pcAdapter(Context context, List<String> deviceName,List<String> deviceHardware, List<String> pcStatus){
    try {
        if (deviceName.get(0) == "") {
            deviceName.remove(0);
            if (deviceHardware.get(0) == "") {
                deviceHardware.remove(0);
            }
        }
    }catch (Exception e){

    }
        this.context=context;
        this.deviceName = deviceName;
        this.deviceHardware = deviceHardware;
        this.pcStatus = pcStatus;

    }

    public pcAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //Used to add the custom layouts to the Recycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pc_menu_fragment,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
    public void onBindViewHolder(ViewHolder holder, final int position){
        //Used to declare and find all the buttons and text views in the custom layout
        Button[] onoffButton = new Button[deviceHardware.size()];
        onoffButton[position] = holder.view.findViewById(R.id.onoffButton);
        Button[] delButton = new Button[deviceHardware.size()];
        delButton[position] = holder.view.findViewById(R.id.select);
        TextView[] deviceNameTxt = new TextView[deviceHardware.size()];
        deviceNameTxt[position] = holder.view.findViewById(R.id.pcName);
        TextView[] pcStatusTxt = new TextView[deviceHardware.size()];
        pcStatusTxt[position] = holder.view.findViewById(R.id.pcStatus);
        final EditText[] codeEntry = new EditText[deviceHardware.size()];
        codeEntry[position] = holder.view.findViewById(R.id.codeEntry);
        deviceNameTxt[position].setText(deviceName.get(position));
        try {
            pcStatusTxt[position].setText(pcStatus.get(position));
        }catch (Exception e){
            pcStatusTxt[position].setText("Unknown");
        }
        this.position = position;

        onoffButton[position].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inT = position;
                code = codeEntry[position].getText().toString();
                if(code != null) {
                    try {
                        scanner.stopScan(scanCallback);
                    }catch (Exception e){

                    }
                    findDevice(deviceHardware.get(position));
                    Intent intent = new Intent(context, MainActivity.class);


                }else{

                }
            }
        });

        delButton[position].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MainActivity.class);
                inT = position;
                intent.putExtra("delNum", inT);
                context.startActivity(intent);
        }
        });




            }


    public int getItemCount(){
        try {
            return deviceHardware.size();
        }catch (Exception e){
            return 0;
        }
    }

    public void findDevice(String address){



        ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setCallbackType(ScanSettings.CALLBACK_TYPE_FIRST_MATCH).setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE).setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT).setReportDelay(0).build();
        List<ScanFilter> filter = new ArrayList<ScanFilter>();
        filter.add(new ScanFilter.Builder().setDeviceAddress(address).build());
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        scanner = btAdapter.getBluetoothLeScanner();
        if(scanner != null){
            scanner.startScan(filter,scanSettings,scanCallback);
        }





    }

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            BluetoothDevice device = (result.getDevice());
            if(device != null){
                scanner.stopScan(scanCallback);
                final BluetoothDevice bluetoothDeviceInfo = (BluetoothDevice) device;
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra( "device", bluetoothDeviceInfo);
                intent.putExtra("isTrue", true);
                intent.putExtra("notNew", true);
                intent.putExtra("position", inT);
                intent.putExtra("code",code);
                context.startActivity(intent);

            }else{

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




}