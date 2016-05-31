package org.trends.trendingapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.trends.trendingapp.R;

import java.util.Arrays;

public class SignupActivity extends AppCompatActivity {

    public static CallbackManager callbackmanager;

    private Button fbbutton;
    private String facebook_id, f_name,  profile_image, email_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_signup);

        fbbutton = (Button) findViewById(R.id.facebookd);

        fbbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Call private method
                onFblogin();
            }
        });
    }

    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackmanager = CallbackManager.Factory.create();
    }

    protected void getUserInfo(final LoginResult login_result){

        GraphRequest data_request = GraphRequest.newMeRequest(
                login_result.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        Log.d("Hessd", "complete" + "ss");
                        try {
                            facebook_id = object.getString("id");
                            f_name = object.getString("name");
                            email_id = object.getString("email");
                            profile_image = object.getString("picture");
                            //Start new activity or use this info in your project.
                            //Log.d("Hessd", gender + "ss");
                            SharedPreferences settings = getApplicationContext().getSharedPreferences("KEY_NAME",
                                    getApplicationContext().MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("fbid", facebook_id);
                            editor.putString("fbname", f_name);
                            editor.putString("fbemail", email_id);
                            editor.putString("fbimage", profile_image);
                            editor.putString("access_token", login_result.getAccessToken().getToken());
                            editor.commit();
                            editor.apply();

                            Intent i = new Intent(SignupActivity.this, MainActivity.class);
                            //progress.dismiss();
                            startActivity(i);
                            finish();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            //  e.printStackTrace();
                        }
                        Log.d("Hesssd", f_name + "ss");
                    }
                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
        data_request.executeAsync();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackmanager.onActivityResult(requestCode, resultCode, data);
        Log.e("dbbbata", data.toString());
    }

    private void onFblogin()
    {

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        getUserInfo(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("TAG_CANCEL", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("TAG_ERROR", error.toString());
                    }
                });
    }


    public static void checkLogin(){
        LoginManager.getInstance().logOut();
    }
}
