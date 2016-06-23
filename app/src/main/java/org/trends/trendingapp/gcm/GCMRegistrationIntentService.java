package org.trends.trendingapp.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.trends.trendingapp.R;
import org.trends.trendingapp.services.RetrofitInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SimpuMind on 6/8/16.
 */
public class GCMRegistrationIntentService extends IntentService{

    public static final String REGISTRATION_SUCCES = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";
    public static final String TAG = "GCMTOKEN";

    public GCMRegistrationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();
    }

    private void registerGCM(){

        SharedPreferences sharedPreferences = getSharedPreferences("GCM", Context.MODE_PRIVATE); //Define shared reference file name
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Intent  registrationComplete = null;
        String token = null;

        try{
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.w("GCMRegIntentService", "token: " + token);
            //notify to UI that registration complete success
            registrationComplete = new Intent(REGISTRATION_SUCCES);
            registrationComplete.putExtra("token", token);

            String oldToken = sharedPreferences.getString(TAG, ""); //Return "" When error or key not exists

            //Only request to save token when token is new
            if(!"".equals(token) && !oldToken.equals(token)){
                sendTokenToServer(token, "trending");
                //save new token to shared reference
                editor.putString(TAG, token);
                editor.commit();
            }else{
                Log.w("GCMRegistrationService", "Old token");
            }
        }catch (Exception e){
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }

        //Send Request
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }


    private void sendTokenToServer(String token, String app_name){
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("https://radiant-dusk-70788.herokuapp.com") //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        RetrofitInterface api = adapter.create(RetrofitInterface.class);

        api.setUserGCMToken(token, app_name, new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {
                BufferedReader reader = null;

                //An string to store output from the server
                String output = "";

                try {
                    //Initializing buffered reader
                    reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                    //Reading the output in the string
                    output = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.w("RequesResponseTo Output", output);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w("RequesResponseTo", error.toString());
            }
        });

    }
}
