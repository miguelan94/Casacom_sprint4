package com.streamnow.europaallee.activities;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RelativeLayout;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.streamnow.europaallee.R;
import com.streamnow.europaallee.datamodel.LDEvents;
import com.streamnow.europaallee.lib.LDConnection;
import com.streamnow.europaallee.utils.Lindau;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Miguel Angel on 27/07/2016.
 */
public class RegistrationIntentService extends IntentService {

    private static String SENDER_ID = "782670365451";
    private String token;
    private Handler handler;
    public RegistrationIntentService(){
        super(SENDER_ID);
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        try {


            InstanceID instanceID = InstanceID.getInstance(RegistrationIntentService.this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            System.out.println("Sender ID: " + getString((R.string.gcm_defaultSenderId)));
            Log.i("TOKEN", "GCM Registration Token: " + token);
            if (handler.getLooper().getThread().isAlive()) {
                handler.post(new Thread_());
            }
        } catch (Exception e) {
            Log.d("Fail token", "Failed to complete token refresh", e);


        }
    }

    public class Thread_ implements Runnable {

        @Override
        public void run() {
            RequestParams requestParams = new RequestParams();
            requestParams.add("access_token", Lindau.getInstance().getCurrentSessionUser().accessToken);
            requestParams.add("app_id", getString(R.string.gcm_defaultSenderId));
            requestParams.add("platform", "android");
            requestParams.add("DeviceToken", token);
            LDConnection.get("/setDeviceToken", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i("Log", "response-->" + response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                    System.out.println("onFailure throwable: " + throwable.toString() + " status code = " + statusCode);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    if(errorResponse != null){
                        System.out.println("onFailure json" + errorResponse.toString());
                    }


                }
            });
        }
    }

    private void sendRegistrationToServer(String token){
        RequestParams requestParams = new RequestParams();
        requestParams.add("access_token",token);
        requestParams.add("user_id", "1021");
        requestParams.add("text","test");
        LDConnection.get("sendPushNotification",requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                try
                {
                   System.out.println("response: " + response.getString("status"));
                }
                catch( JSONException e )
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable)
            {
                System.out.println("push notifications onFailure throwable: " + throwable.toString() + " status code = " + statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                if(errorResponse != null){
                    System.out.println("push notifications onFailure json" + errorResponse.toString());
                }

            }
        });

    }

}
