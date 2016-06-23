package org.trends.trendingapp.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import org.trends.trendingapp.R;
import org.trends.trendingapp.TrendingApplication;
import org.trends.trendingapp.adapters.MyPagerAdapter;
import org.trends.trendingapp.customviews.Fab;
import org.trends.trendingapp.customviews.RobotoTextView;
import org.trends.trendingapp.gcm.GCMPushReceiverService;
import org.trends.trendingapp.models.User;
import org.trends.trendingapp.gcm.GCMRegistrationIntentService;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private BroadcastReceiver mRegistrationBroadCastReceiver;

    private final String[] mTitles = {"News", "Events", "Trends"};
    private TabLayout tabLayout;
    private ViewPager vp;

    String emails;
    String fullnames;
    static String id;
    public String access_tokens;


    private MaterialSheetFab materialSheetFab;
    private int statusBarColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        User user = TrendingApplication.getInstance().getPrefManager().getUser();

        if (TrendingApplication.getInstance().getPrefManager().getUser() == null) {
            SignupActivity.checkLogin();
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        }else {
            emails = user.getEmail();
            fullnames = user.getName();
            id = user.getId();
            access_tokens = user.getAccess_token();
        }

        initViewPager();
        setupFab();
        initTabLayout();


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
        navigationView.setNavigationItemSelectedListener(this);

        Glide
                .with(this)
                .load("https://graph.facebook.com/me/picture?type=normal&method=GET&access_token="+ access_tokens)
                .placeholder(R.drawable.ic_account_circle_64dp)
                .fitCenter()
                .into(fb_image);

        fb_username.setText(fullnames);
        fb_email.setText(emails);


        mRegistrationBroadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check type of intent filter
                if(intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_SUCCES)){
                    String token  = intent.getStringExtra("token");
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
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        } else {
            super.onBackPressed();
        }
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
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
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mTitles));
    }

    private void initTabLayout() {

        vp.setOffscreenPageLimit(MyPagerAdapter.NUM_ITEMS);
        updatePage(vp.getCurrentItem());

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(vp);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updatePage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupFab() {

        Fab fab = (Fab) findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.colorAccent);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet event listener
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Save current status bar color
                statusBarColor = getStatusBarColor();
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            @Override
            public void onHideSheet() {
                // Restore status bar color
                setStatusBarColor(statusBarColor);
            }
        });

        // Set material sheet item click listeners
        findViewById(R.id.fab_sheet_item_recording).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_reminder).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_photo).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_note).setOnClickListener(this);
    }

    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getWindow().getStatusBarColor();
        }
        return 0;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Fab Pressed", Toast.LENGTH_SHORT).show();
        materialSheetFab.hideSheet();
    }

    /**
     * Called when the selected page changes.
     *
     * @param selectedPage selected page
     */
    private void updatePage(int selectedPage) {
        updateFab(selectedPage);
    }

    /**
     * Updates the FAB based on the selected page
     *
     * @param selectedPage selected page
     */
    private void updateFab(int selectedPage) {
        switch (selectedPage) {
            case MyPagerAdapter.NEWS_POST:
                materialSheetFab.showFab();
                break;
            case MyPagerAdapter.EVENT_POST:
                materialSheetFab.hideSheetThenFab();
                break;
            case MyPagerAdapter.TREND_POST:
            default:
                materialSheetFab.hideSheetThenFab();
                break;
        }
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
        } else if (id == R.id.nav_share) {

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
    }


    public static void clearNotifications() {
        NotificationManager notificationManager = (NotificationManager) TrendingApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
