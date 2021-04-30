package com.example.courceworkmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.CENTER;


public class MainActivity extends AppCompatActivity {
    public String deviceName;
    public String deviceHardware;
    public BluetoothAdapter bluetoothAdapter = null;
    public RecyclerView pcrecyclerView;
    private RecyclerView.Adapter pcAdapter;
    public List<Object> deviceList = new ArrayList<>();
    public static Handler handler;
    private final static int CONNECTING_STATUS = 1;
    private final static int MESSAGE_READ = 2;
    public static BluetoothService.ConnectedThread connectedThread;
    public boolean codeAcpt = false;
    public static CreateConnectThread createConnectedThread;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      pcrecyclerView = findViewById(R.id.recyclerView);
      pcrecyclerView.setLayoutManager(new LinearLayoutManager(this));
      pcAdapter = new pcAdapter(this, deviceList);
      pcrecyclerView.setAdapter(pcAdapter);


      handler = new Handler(Looper.getMainLooper()) {
          @Override
          public void handleMessage(Message msg){
              switch (msg.what){
                  case CONNECTING_STATUS:
                      switch(msg.arg1){
                          case 1:

                              break;
                          case -1:

                              break;
                      }
                      break;

                  case MESSAGE_READ:
                      String arduinoMsg = msg.obj.toString(); // Read message from Arduino
                      switch (arduinoMsg.toLowerCase()){
                          case "pc on":

                              break;
                          case "pc off":

                              break;

                          case "codeRequest":

                              break;

                          case "codeAccepted":
                              codeAcpt = true;
                              break;
                          case "codeRejected":
                              codeAcpt = false;
                              break;

                      }
                      break;

              }
          }
      };

    }
     @Override
     protected void onStart(){
            super.onStart();
            setContentView(R.layout.activity_main);
            deviceName= getIntent().getStringExtra("deviceName");
            deviceHardware = getIntent().getStringExtra("deviceHardware");
         if (this.deviceName != null && this.deviceHardware != null) {
             bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
             createConnectedThread = new CreateConnectThread(bluetoothAdapter, deviceHardware);
             createConnectedThread.start();



         }
        }
     @Override
     protected  void onStop(){
      super.onStop();

     }

    public void sendBack(String deviceName, String deviceHardware){
        this.deviceName = deviceName;
        this.deviceHardware = deviceHardware;
    }



    public void addPc(View view){
        Intent btDevices = new Intent(MainActivity.this, bluetooth_device_list.class);
        startActivityForResult(btDevices,1);
    }
    public void onOff(View view){

        //send signal code goes here


    }

    public void popup(View view){
      LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
      final View layout = inflater.inflate(R.layout.code_entry, null);
      int w = LinearLayout.LayoutParams.WRAP_CONTENT;
      final PopupWindow pcPopup = new PopupWindow(layout, w,w, true);
      pcPopup.setTouchable(true);
      pcPopup.showAtLocation(layout,CENTER,0,0);
      final Button codeSnd = layout.findViewById(R.id.codeButton);
      final EditText codeEntry = layout.findViewById(R.id.codeEntrynum);
      codeSnd.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String deviceCode = codeEntry.getText().toString();
              verifyCode(deviceCode);
          }
      });


    }
    private void verifyCode(String deviceCode){
        connectedThread.write(deviceCode);
        LinearLayout layout = new LinearLayout(this);
        PopupWindow popup = new PopupWindow();


        deviceList.add(new BluetoothDeviceInfo(deviceName,deviceHardware));
        pcrecyclerView = findViewById(R.id.recyclerView);
        pcrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pcAdapter = new pcAdapter(this, deviceList);
        pcrecyclerView.setAdapter(pcAdapter);

    }

}


