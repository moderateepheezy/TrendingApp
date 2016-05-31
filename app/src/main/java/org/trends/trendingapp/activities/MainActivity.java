package org.trends.trendingapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;

import org.trends.trendingapp.R;
import org.trends.trendingapp.adapters.MyPagerAdapter;
import org.trends.trendingapp.customviews.RobotoTextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String[] mTitles = {"News", "Events", "Trends"};
    private TabLayout tabLayout;
    private ViewPager vp;

    String emails;
    String fullnames;
    String picture;
    static String id;
    public String fbid;
    public String access_tokens;
    public SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settings = getSharedPreferences("KEY_NAME",
                MODE_PRIVATE);
        fbid = settings.getString("fbid", "");

        if(fbid.isEmpty()){
            SignupActivity.checkLogin();
            Intent i = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(i);
            finish();
        }else {
            emails = settings.getString("fbemail", "");
            fullnames = settings.getString("fbname", "");
            id = settings.getString("fbid", "");
            picture = settings.getString("fbimage", "");
            access_tokens = settings.getString("access_token", "");
        }

        initViewPager();
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

    private void initViewPager() {
        vp = (ViewPager) findViewById(R.id.vp);
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mTitles));
    }

    private void initTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(vp);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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

}
