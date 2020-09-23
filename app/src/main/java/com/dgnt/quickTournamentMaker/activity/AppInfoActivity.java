package com.dgnt.quickTournamentMaker.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.dgnt.quickTournamentMaker.R;

public class AppInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTextViewAsHtml((TextView) findViewById(R.id.eliminationInfoText_tv), getString(R.string.eliminationInfoText));
        setTextViewAsHtml((TextView) findViewById(R.id.doubleEliminationInfoText_tv), getString(R.string.doubleEliminationInfoText));
        setTextViewAsHtml((TextView) findViewById(R.id.roundRobinInfoText_tv), getString(R.string.roundRobinInfoText));
        setTextViewAsHtml((TextView) findViewById(R.id.swissInfoText_tv), getString(R.string.swissInfoText));
        setTextViewAsHtml((TextView) findViewById(R.id.survivalInfoText_tv), getString(R.string.survivalInfoText));


    }

    private void setTextViewAsHtml(final TextView textView, final String string){
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(Html.fromHtml(string));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
