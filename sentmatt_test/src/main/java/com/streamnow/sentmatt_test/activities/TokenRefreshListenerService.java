package com.streamnow.sentmatt_test.activities;

import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Miguel Angel on 27/07/2016.
 */
public class TokenRefreshListenerService extends InstanceIDListenerService {


    @Override
    public void onTokenRefresh() {
        Log.i("Log","Token refresh");
        //Intent intent = new Intent(this, RegistrationIntentService.class);
        //startService(intent);
    }
}
