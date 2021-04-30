package com.example.courceworkmobile;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.textclassifier.ConversationActions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothService {


    private interface MessageConstants{
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;
    }

    public static class ConnectedThread extends Thread{
        private final BluetoothSocket socket;
        private final InputStream inStream;
        private final OutputStream outStream;
        private byte[] buffer;
        private Handler handler;

        public ConnectedThread(BluetoothSocket socket) {
            MainActivity mainAct = new MainActivity();
            this.handler = mainAct.handler;
            this.socket = socket;
            InputStream tmpIN = null;
            OutputStream tmpOUT = null;
            try{
                tmpIN = socket.getInputStream();
            }catch (IOException e){
                System.out.println(e);
            }
            try{
                tmpOUT = socket.getOutputStream();
            }catch (IOException e){
                System.out.println(e);
            }
            inStream=tmpIN;
            outStream=tmpOUT;
        }
        public void run(){
            buffer = new byte[1024];
            int numBytes;
            while(true){
                try{
                    numBytes = inStream.read(buffer);
                    Message readMsg = handler.obtainMessage(MessageConstants.MESSAGE_READ, numBytes, -1, buffer);
                    readMsg.sendToTarget();
                }catch (IOException e){

                }
            }
        }
        public void write(String input){
            try{
                outStream.write(input.getBytes());
                Message writterMsg = handler.obtainMessage(MessageConstants.MESSAGE_WRITE, -1, -1, input.getBytes());
                writterMsg.sendToTarget();
            }catch (IOException e){

                Message writeErrorMsg = handler.obtainMessage(MessageConstants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast", "Cant send data to device");
                writeErrorMsg.setData(bundle);
                handler.sendMessage(writeErrorMsg);
            }
        }
        public void cancel() {
            try{
                socket.close();
            }catch (IOException e){

            }
        }

    }
}

