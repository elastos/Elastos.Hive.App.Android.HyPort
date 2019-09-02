package org.elastos.app.hivedemo.base;

import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDexApplication;

import org.elastos.app.hivedemo.R;
import org.elastos.app.hivedemo.ui.activity.SimpleCarrier;
import org.elastos.app.hivedemo.utils.ToastUtils;
import org.elastos.app.hivedemo.utils.Utils;

public class HiveApplication extends MultiDexApplication {
    private Handler mHandler;
    private static SimpleCarrier sSimpleCarrier;

    @Override
    public void onCreate() {
        Utils.init(this);
        super.onCreate();

        mHandler = new CarrierHandler();
        sSimpleCarrier = SimpleCarrier.getInstance(this, mHandler);
    }

    public class CarrierHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SimpleCarrier.ONREADY: {
                    ToastUtils.showShortToastSafe(R.string.carrier_ready);
                    break;
                }
            }
        }
    }

    public SimpleCarrier getCarrier(){
        return sSimpleCarrier ;
    }
}
