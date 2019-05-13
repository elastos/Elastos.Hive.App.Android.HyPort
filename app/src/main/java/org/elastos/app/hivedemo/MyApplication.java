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
            }
        }
    }

    public static Context getInstance() {
        return mContext;
    }
}
