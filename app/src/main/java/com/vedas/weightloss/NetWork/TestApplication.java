package com.vedas.weightloss.NetWork;

import android.app.Application;

/**
 * Created by mypc on 3/6/2018.
 */

public class TestApplication extends Application
{
    private static TestApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized TestApplication getInstance() {
        return mInstance;
    }

    public void setConnectionListener(ConnectionReceiver.ConnectionReceiverListener listener) {
        ConnectionReceiver.connectionReceiverListener = listener;
    }
}
