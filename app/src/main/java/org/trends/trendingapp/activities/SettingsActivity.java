package org.trends.trendingapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.trends.trendingapp.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    SwitchCompat linda;
    SwitchCompat bella;
    SwitchCompat punch;
    SwitchCompat pulse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linda = (SwitchCompat) findViewById(R.id.lindaSwitch);
        bella = (SwitchCompat) findViewById(R.id.bellaSwitch);
        punch = (SwitchCompat) findViewById(R.id.punchSwitch);
        pulse = (SwitchCompat) findViewById(R.id.pulseSwitch);

        loadChangestate("linda", linda);
        loadChangestate("bella", bella);
        loadChangestate("punch", punch);
        loadChangestate("pulse", pulse);

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

}
