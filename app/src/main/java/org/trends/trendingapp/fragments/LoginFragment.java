package org.trends.trendingapp.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.trends.trendingapp.R;
import org.trends.trendingapp.TrendingApplication;
import org.trends.trendingapp.activities.MainActivity;
import org.trends.trendingapp.adapters.MyPagerAdapter;
import org.trends.trendingapp.models.User;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by SimpuMind on 8/9/16.
 */
public class LoginFragment extends SlideFragment {

    public static CallbackManager callbackmanager;

    private Button fbbutton, skipLogin;
    private static String facebook_id, f_name, email_id;

    private Handler loginHandler = new Handler();


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        facebookSDKInitialize();
        View root = inflater.inflate(R.layout.activity_signup, container, false);

        checkLogin();

        fbbutton = (Button)root.findViewById(R.id.facebookd);

        skipLogin = (Button) root.findViewById(R.id.skipLogin);
        skipLogin.setVisibility(View.INVISIBLE);

        User user = TrendingApplication.getInstance().getPrefManager().getUser();
        if(user != null){
            fbbutton.setText("Welcome");
            fbbutton.setEnabled(false);
        }

        fbbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Call private method
                onFblogin();
            }
        });

        return root;
    }


    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getActivity());
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

                            User user = new User(facebook_id,
                                    f_name,
                                    email_id, login_result.getAccessToken().getToken());

                            TrendingApplication.getInstance().getPrefManager().storeUser(user);
                            Intent i = getActivity().getPackageManager()
                                    .getLaunchIntentForPackage( getActivity().getPackageName() );
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            fbbutton.setText("Welcome");
                            fbbutton.setEnabled(false);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean canGoForward() {
        return true;
    }

    public static void checkLogin(){
        LoginManager.getInstance().logOut();
    }


}
