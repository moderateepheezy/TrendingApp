package org.trends.trendingapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import org.trends.trendingapp.R;
import org.trends.trendingapp.models.FeedBack;
import org.trends.trendingapp.models.ReadStatus;
import org.trends.trendingapp.services.RetrofitInterface;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class FeedBackActivity extends AppCompatActivity{

    private static final String TAG = FeedBackActivity.class.getSimpleName();


    private RetrofitInterface restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        setSupportActionBar(toolbar);

        final RatingBar content = (RatingBar) findViewById(R.id.ratingBarContent);
        final RatingBar design = (RatingBar) findViewById(R.id.ratingBarDesign);
        final RatingBar easyOfUse = (RatingBar) findViewById(R.id.ratingBarEase);
        final RatingBar overAll = (RatingBar) findViewById(R.id.ratingBarOverall);

        final EditText generalComment = (EditText) findViewById(R.id.addedcomment);

        final Button sendFeed = (Button) findViewById(R.id.sendFeed);

        LayerDrawable stars = (LayerDrawable) content
                .getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent),
                PorterDuff.Mode.SRC_ATOP); // for filled stars
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.primary),
                PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.primary),
                PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars1 = (LayerDrawable) design
                .getProgressDrawable();
        stars1.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent),
                PorterDuff.Mode.SRC_ATOP); // for filled stars
        stars1.getDrawable(1).setColorFilter(getResources().getColor(R.color.primary),
                PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars1.getDrawable(0).setColorFilter(getResources().getColor(R.color.primary),
                PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars2 = (LayerDrawable) easyOfUse
                .getProgressDrawable();
        stars2.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent),
                PorterDuff.Mode.SRC_ATOP); // for filled stars
        stars2.getDrawable(1).setColorFilter(getResources().getColor(R.color.primary),
                PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars2.getDrawable(0).setColorFilter(getResources().getColor(R.color.primary),
                PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars3 = (LayerDrawable) overAll
                .getProgressDrawable();
        stars3.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent),
                PorterDuff.Mode.SRC_ATOP); // for filled stars
        stars3.getDrawable(1).setColorFilter(getResources().getColor(R.color.primary),
                PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars3.getDrawable(0).setColorFilter(getResources().getColor(R.color.primary),
                PorterDuff.Mode.SRC_ATOP); // for empty stars


            sendFeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String x = generalComment.getText().toString();
                    if(content.getNumStars() == 0 || design.getNumStars() == 0
                            || easyOfUse.getNumStars() == 0 || overAll.getNumStars() == 0 || x.isEmpty()){
                       /* new AlertDialog.Builder(getApplicationContext())
                                .setTitle("Summit FeedBack")
                                .setMessage("Are you sure you want to send this entry?\nSeems the fields are entry.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();*/
                        Toast.makeText(FeedBackActivity.this, "Please, all feeds are important ", Toast.LENGTH_SHORT).show();
                    }else {
                        sendFeedback(x, design.getRating(),
                                overAll.getRating(),
                                easyOfUse.getRating(),content.getRating());

                        design.setRating(0);
                        overAll.setRating(0);
                        content.setRating(0);
                        easyOfUse.setRating(0);

                        generalComment.setText("");
                    }
                }
            });

    }


    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        Log.d(TAG,"--onSupportNavigateUp()--");
        getSupportFragmentManager().popBackStack();
        super.onBackPressed();
        return true;
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
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void sendFeedback(String general, float design,
                              float overall, float ease,float content){

        FeedBack feedback = new FeedBack(content,overall,ease,design,general);
        setupRestClient();

        restApi.sendFeedback(content,overall,ease,design,general, new Callback<ReadStatus>() {
            @Override
            public void success(ReadStatus readStatus, Response response) {
                Log.e("logLike", "Send feedback successful");
                Toast.makeText(FeedBackActivity.this, "Send feedback successful", Toast.LENGTH_SHORT).show();
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
