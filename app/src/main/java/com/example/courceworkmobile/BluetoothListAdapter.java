package com.example.courceworkmobile;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.ViewHolder> {

    public int position;
    public List<BluetoothDevice> deviceList;
    public bluetooth_device_list bluetooth_device_list = new bluetooth_device_list();
    public Context context;
    public Intent returnIntent;
    MainActivity mainActivity = new MainActivity();


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;


        public ViewHolder(View v) {
            super(v);
            view = v;
        }

    }

    public BluetoothListAdapter(Context context, List<BluetoothDevice> deviceList){
        this.context=context;
        this.deviceList = deviceList;
    }
    public BluetoothListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //Used to add the custom layouts to the Recycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_devices,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }
    public void onBindViewHolder(ViewHolder holder, final int position){
        //Used to declare and find all the buttons and text views in the custom layout.
        final BluetoothDevice bluetoothDeviceInfo = (BluetoothDevice) deviceList.get(position);
        TextView[] deviceName = new TextView[deviceList.size()];
        deviceName[position] = holder.view.findViewById(R.id.device_name);
        System.out.println(bluetoothDeviceInfo.getName());
        deviceName[position].setText(bluetoothDeviceInfo.getAddress());
        Button[] select = new Button[deviceList.size()];
        select[position] = holder.view.findViewById(R.id.select);

        select[position].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MainActivity.class);
                boolean setnew = true;
                intent.putExtra( "device", bluetoothDeviceInfo);
                intent.putExtra("isTrue", true);
                context.startActivity(intent);
                ((com.example.courceworkmobile.bluetooth_device_list) context).finish();

            }
        });
        this.position = position;




    }


    public int getItemCount(){
        try {
            return deviceList.size();
        }catch (Exception e){
            return 0;
        }
    }





}