package org.trends.trendingapp.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.trends.trendingapp.R;
import org.trends.trendingapp.TrendingApplication;
import org.trends.trendingapp.adapters.MyPagerAdapter;
import org.trends.trendingapp.customviews.RobotoTextView;
import org.trends.trendingapp.models.User;
import org.trends.trendingapp.gcm.GCMRegistrationIntentService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static String POSITION = "POSITION";
    private BroadcastReceiver mRegistrationBroadCastReceiver;

    public static final String PREF_KEY_FIRST_START = "com.heinrichreimersoftware.materialintro.demo.PREF_KEY_FIRST_START";
    public static final int REQUEST_CODE_INTRO = 1;

    private TabLayout tabLayout;
    private ViewPager vp;
    private MyPagerAdapter myPagerAdapter;

    String emails;
    String fullnames;
    static String id;
    public String access_tokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        showHashKey(getApplicationContext());

        boolean firstStart = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(PREF_KEY_FIRST_START, true);

        User user = TrendingApplication.getInstance().getPrefManager().getUser();


        if(firstStart){
            Intent intent = new Intent(this, WelcomeScreenActivity.class);
            startActivityForResult(intent, REQUEST_CODE_INTRO);
            //finish();
        }


        initViewPager();
        initTabLayout();
        myPagerAdapter.notifyDataSetChanged();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        final View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);

        CircleImageView fb_image = (CircleImageView) headerLayout.findViewById(R.id.fb_image);
        RobotoTextView fb_username = (RobotoTextView) headerLayout.findViewById(R.id.fb_name);
        RobotoTextView fb_email = (RobotoTextView) headerLayout.findViewById(R.id.fb_email);
        Button btnLogin = (Button) headerLayout.findViewById(R.id.btnLogin);
        navigationView.setNavigationItemSelectedListener(this);
        if (TrendingApplication.getInstance().getPrefManager().getUser() != null) {
            btnLogin.setVisibility(View.GONE);
            fb_image.setVisibility(View.VISIBLE);
            fb_username.setVisibility(View.VISIBLE);
            fb_email.setVisibility(View.VISIBLE);
            emails = user.getEmail();
            fullnames = user.getName();
            id = user.getId();
            access_tokens = user.getAccess_token();

            Glide
                    .with(this)
                    .load("https://graph.facebook.com/me/picture?type=normal&method=GET&access_token="+ access_tokens)
                    .placeholder(R.drawable.ic_account_circle_64dp)
                    .fitCenter()
                    .into(fb_image);

            fb_username.setText(fullnames);
            fb_email.setText(emails);
        }
        else{
            btnLogin.setVisibility(View.VISIBLE);
            fb_image.setVisibility(View.GONE);
            fb_username.setVisibility(View.GONE);
            fb_email.setVisibility(View.GONE);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }



        mRegistrationBroadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check type of intent filter
                if(intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_SUCCES)){
                    String token  = intent.getStringExtra("token");
                    Log.d("TokenValue", token);
                    //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                }else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    //Registration error
                }else{

                }
            }
        };

        //Check status of google play service in device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(ConnectionResult.SUCCESS != resultCode){
            //Check type of error
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
              //  Toast.makeText(getApplicationContext(), "google play service is not installed/enabled in this device!", Toast.LENGTH_SHORT).show();
                //No notification
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            }else{
              //  Toast.makeText(getApplicationContext() , "This device does not support Google Play Service!", Toast.LENGTH_SHORT).show();
            }
        }else{
            //Start Service
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivityLog", "onResume");
        LocalBroadcastManager.getInstance(this).
                registerReceiver(mRegistrationBroadCastReceiver,
                        new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCES));
        LocalBroadcastManager.getInstance(this).
                registerReceiver(mRegistrationBroadCastReceiver,
                        new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));

        clearNotifications();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivityLog", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadCastReceiver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void toggleDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }
    private void initViewPager() {
        vp = (ViewPager) findViewById(R.id.vp);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), getApplicationContext(), id);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), getApplicationContext(), id));
    }


    private void initTabLayout() {

        vp.setOffscreenPageLimit(MyPagerAdapter.NUM_ITEMS);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(vp);


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(myPagerAdapter.getTabView(i));
        }
        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getCustomView().findViewById(R.id.tab).setVisibility(View.VISIBLE);

        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getCustomView().setSelected(true);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
                tabLayout.getTabAt(tab.getPosition()).getCustomView().findViewById(R.id.tab).setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                tabLayout.getTabAt(tab.getPosition()).getCustomView().findViewById(R.id.tab).setVisibility(View.GONE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Fab Pressed", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                toggleDrawer();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_read){
            Intent intent = new Intent(getApplicationContext(), ReadActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.favourite) {
            Intent intent = new Intent(getApplicationContext(), AchiveActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.feedback) {
            Intent intent = new Intent(getApplicationContext(), FeedBackActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //some code
        }
        if (requestCode == REQUEST_CODE_INTRO) {
            if (resultCode == RESULT_OK) {
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean(PREF_KEY_FIRST_START, false)
                        .apply();
            }
            else {
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean(PREF_KEY_FIRST_START, true)
                        .apply();
                //User cancelled the intro so we'll finish this activity too.
                finish();
            }
        }

    }



    public static void clearNotifications() {
        NotificationManager notificationManager = (NotificationManager) TrendingApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "org.trends.trendingapp", PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        vp.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    public  void restartSelf() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 500, // one second
                PendingIntent.getActivity(MainActivity.this, 0, getIntent(), PendingIntent.FLAG_ONE_SHOT
                        | PendingIntent.FLAG_CANCEL_CURRENT));
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
