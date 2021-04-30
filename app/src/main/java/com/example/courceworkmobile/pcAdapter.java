package com.example.courceworkmobile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class pcAdapter extends RecyclerView.Adapter<pcAdapter.ViewHolder> {

    public int position;
    public Button[] goButton;
    public Button[] delButton;
    private Context context;
    private List<Object> deviceList;
    private List<String> deviceName;
    private List<String> deviceStatus;
    public MainActivity mainActivity = new MainActivity();




    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;


        public ViewHolder(View v) {
            super(v);
            view = v;
        }

    }
    public pcAdapter(Context context, List<Object> list){
        this.context=context;
        this.deviceList = list;
    }

    public pcAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //Used to add the custom layouts to the Recycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pc_menu_fragment,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
    public void onBindViewHolder(ViewHolder holder, final int position){
        //Used to declare and find all the buttons and text views in the custom layout
        Button[] onoffButton = new Button[deviceList.size()];
        onoffButton[position] = holder.view.findViewById(R.id.onoffButton);
        Button[] delButton = new Button[deviceList.size()];
        delButton[position] = holder.view.findViewById(R.id.select);
        TextView[] deviceNameTxt = new TextView[deviceList.size()];
        deviceNameTxt[position] = holder.view.findViewById(R.id.pcName);
        TextView[] pcStatusTxt = new TextView[deviceList.size()];
        pcStatusTxt[position] = holder.view.findViewById(R.id.pcStatus);
        final BluetoothDeviceInfo btDI = (BluetoothDeviceInfo) deviceList.get(position);
        deviceNameTxt[position].setText(btDI.getDeviceName());
        this.position = position;

        onoffButton[position].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MainActivity.class);
                intent.putExtra("deviceName",btDI.getDeviceName());
                intent.putExtra("deviceHardware",btDI.getDeviceHardware());
                context.startActivity(intent);
            }
        });




            }


    public int getItemCount(){
        try {
            return deviceList.size();
        }catch (Exception e){
            return 0;
        }
    }




}