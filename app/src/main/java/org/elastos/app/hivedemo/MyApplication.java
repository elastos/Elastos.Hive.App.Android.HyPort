package org.elastos.app.hivedemo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


public class MyApplication extends Application {
    private static Context mContext;

    private Handler mHandler;
    private static SimpleCarrier sSimpleCarrier;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        mHandler = new CarrierHandler();
        sSimpleCarrier = SimpleCarrier.getInstance(this, mHandler);
    }

    public class CarrierHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SimpleCarrier.ONREADY: {
                    //Carrier is ready
                    Toast.makeText(MyApplication.this, "Carrier is ready!", Toast.LENGTH_SHORT).show();
                    break;
                }
                case SimpleCarrier.SENDINGDATA: {
                    String fileId = (String) msg.obj;
                    Toast.makeText(MyApplication.this, "Sending Data Start...", Toast.LENGTH_SHORT).show();
                    sendData(fileId);
                    break;
                }
                case SimpleCarrier.SHOWINGFILEPATH: {
                    String filePath = (String) msg.obj;
                    Toast.makeText(MyApplication.this, ("Received file: " + filePath), Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

    private void sendData(final String fileId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sSimpleCarrier.sendData(fileId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static Context getInstance() {
        return mContext;
    }
}
