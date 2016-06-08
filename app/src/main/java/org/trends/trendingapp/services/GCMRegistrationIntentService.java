package org.trends.trendingapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.trends.trendingapp.R;

/**
 * Created by SimpuMind on 6/8/16.
 */
public class GCMRegistrationIntentService extends IntentService{

    public static final String REGISTRATION_SUCCES = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GCMRegistrationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();
    }

    private void registerGCM(){

        Intent  registrationComplete = null;
        String token = null;

        try{
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.w("GCMRegIntentService", "token: " + token);
            //notify to UI that registration complete success
            registrationComplete = new Intent(REGISTRATION_SUCCES);
            registrationComplete.putExtra("token", token);
        }catch (Exception e){
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }

        //Send Request
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}
