package com.example.courceworkmobile;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

import static com.example.courceworkmobile.MainActivity.connectedThread;

public class CreateConnectThread extends Thread {

    private BluetoothSocket socket;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice btd;
    public CreateConnectThread(BluetoothAdapter bta, String address){
        this.btd = bta.getRemoteDevice(address);
        UUID id = btd.getUuids()[0].getUuid();
        BluetoothSocket tmp = null;

        try {
            tmp = btd.createInsecureRfcommSocketToServiceRecord(id);
        }catch (IOException e) {

        }
        socket = tmp;
    }
    public void run(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.cancelDiscovery();
        try{
            socket.connect();
        }catch (IOException e){
            try{
                socket.close();
            }catch (IOException e2){

            }
            return;


        }

        connectedThread = new BluetoothService.ConnectedThread (socket);
        connectedThread.run();

    }



    public void cancel(){
        try{
            socket.close();
        }catch (IOException e){

        }
    }
}