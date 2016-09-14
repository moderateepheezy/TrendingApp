package org.trends.trendingapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import org.trends.trendingapp.R;
import org.trends.trendingapp.TrendingApplication;
import org.trends.trendingapp.models.FeedBack;
import org.trends.trendingapp.models.ReadStatus;
import org.trends.trendingapp.models.User;
import org.trends.trendingapp.services.RetrofitInterface;

import java.util.Set;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    private SwitchCompat linda;
    private SwitchCompat bella;
    private SwitchCompat punch;
    private SwitchCompat pulse;

    private Button suggest;
    private EditText suggest_box;

    RetrofitInterface restApi;


    private String fbid, builString;

    User user = TrendingApplication.getInstance().getPrefManager().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linda = (SwitchCompat) findViewById(R.id.lindaSwitch);
        bella = (SwitchCompat) findViewById(R.id.bellaSwitch);
        punch = (SwitchCompat) findViewById(R.id.punchSwitch);
        pulse = (SwitchCompat) findViewById(R.id.pulseSwitch);

        suggest = (Button) findViewById(R.id.suggest);
        suggest_box = (EditText) findViewById(R.id.sugesst_box);
        builString = suggest_box.getText().toString();

        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(suggest_box.getText().toString());
                suggest_box.setText("");
            }
        });

        loadChangestate("linda", linda);
        loadChangestate("bella", bella);
        loadChangestate("punch", punch);
        loadChangestate("pulse", pulse);

        if(user != null) {
            fbid = user.getId();
            punch.setEnabled(true);
            pulse.setEnabled(true);
            bella.setEnabled(true);
            linda.setEnabled(true);

            punch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setChangestate("punch", isChecked);
                }
            });

            pulse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setChangestate("pulse", isChecked);
                }
            });

            bella.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setChangestate("bella", isChecked);
                }
            });

            linda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setChangestate("linda", isChecked);
                }
            });
        }else{
            punch.setEnabled(false);
            pulse.setEnabled(false);
            bella.setEnabled(false);
            linda.setEnabled(false);

        }
    }

    public void sendEmail(String build){
        if(!build.isEmpty()){
            sendFeedback(build, 1, 1, 1,1);
        }else {
            Toast.makeText(SettingsActivity.this, "The input feild should not be empty! ", Toast.LENGTH_SHORT).show();
        }
    }

    public void setChangestate(String preName, boolean isChecked){
        SharedPreferences sharedPreferences = getSharedPreferences(preName,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preName, isChecked);
        editor.apply();
    }

    private void loadChangestate(String prefName, SwitchCompat switchService){
        SharedPreferences sharedPreferences = getSharedPreferences(prefName, MODE_PRIVATE);
        boolean  state = sharedPreferences.getBoolean(prefName, true);
        switchService.setChecked(state);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        Log.d(TAG,"--onSupportNavigateUp()--");
        getSupportFragmentManager().popBackStack();
        super.onBackPressed();
        return true;
    }

    private void sendFeedback(String general, float design,
                              float overall, float ease,float content){
        setupRestClient();

        restApi.sendFeedback(content,overall,ease,design,general, new Callback<ReadStatus>() {
            @Override
            public void success(ReadStatus readStatus, Response response) {
                Log.e("logLike", "Send feedback successful");
                Toast.makeText(SettingsActivity.this, "Send feedback successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("logLike", "Send feedback not successful \t" + error.toString() + "\t" + error.getUrl());
            }
        });
    }


    private void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint("http://voice.atp-sevas.com")
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();

        restApi = restAdapter.create(RetrofitInterface.class);
    }

}
