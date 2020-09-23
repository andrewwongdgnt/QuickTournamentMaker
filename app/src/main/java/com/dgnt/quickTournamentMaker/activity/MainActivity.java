package com.dgnt.quickTournamentMaker.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dgnt.quickTournamentMaker.R;
import com.dgnt.quickTournamentMaker.activity.tournament.TournamentActivity;
import com.dgnt.quickTournamentMaker.model.management.Group;
import com.dgnt.quickTournamentMaker.model.management.Person;
import com.dgnt.quickTournamentMaker.model.tournament.Participant;
import com.dgnt.quickTournamentMaker.model.tournament.Seeder;
import com.dgnt.quickTournamentMaker.model.tournament.Tournament;
import com.dgnt.quickTournamentMaker.util.AdsUtil;
import com.dgnt.quickTournamentMaker.util.DatabaseHelper;
import com.dgnt.quickTournamentMaker.util.EmailUtil;
import com.dgnt.quickTournamentMaker.util.LayoutUtil;
import com.dgnt.quickTournamentMaker.util.PreferenceUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends InAppBillingActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final DatabaseHelper dh = new DatabaseHelper(this);
    private Set<Person> personSelectedSet = new HashSet<>();
    private Menu menu;
    AdRequest adRequest = AdsUtil.buildAdRequestWithTestDevices();
    AdView adView;

    public AdView getAdView() {
        if (adView == null)
            adView = (AdView) findViewById(R.id.adView);
        return adView;
    }

    private void getMoney(){
        //TODO I don't know what the fuck to do here.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        handlePersonsFromDB();

        final TextView numOfPlayersSelected_tv = (TextView) findViewById(R.id.numOfPlayersSelected_tv);
        final EditText numOfPlayers_et = (EditText) findViewById(R.id.numOfPlayers_et);
        final ViewGroup personsGroup = (ViewGroup) findViewById(R.id.personsGroup);

        final ToggleButton quickStart_tb = (ToggleButton) findViewById(R.id.quickStart_tb);
        quickStart_tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { //Quick start
                    numOfPlayersSelected_tv.setText(getString(R.string.numberOfPlayers));
                    numOfPlayers_et.setVisibility(View.VISIBLE);
                    personsGroup.setVisibility(View.GONE);
                } else {
                    numOfPlayersSelected_tv.setText(getString(R.string.numberOfPlayersSelected, personSelectedSet.size()));
                    numOfPlayers_et.setVisibility(View.GONE);
                    personsGroup.setVisibility(View.VISIBLE);
                }
            }
        });


        final RadioButton randomSeed_rb = (RadioButton) findViewById(R.id.randomSeed_rb);
        final RadioButton sameSeed_rb = (RadioButton) findViewById(R.id.sameSeed_rb);
        sameSeed_rb.setVisibility(View.GONE);

        final RadioButton elimination_rb = (RadioButton) findViewById(R.id.elimination_rb);
        final RadioButton doubleElimination_rb = (RadioButton) findViewById(R.id.doubleElimination_rb);
        final RadioButton roundRobin_rb = (RadioButton) findViewById(R.id.roundRobin_rb);
        final RadioButton swiss_rb = (RadioButton) findViewById(R.id.swiss_rb);

        LayoutUtil.setUpSeedingEditor(MainActivity.this, findViewById(android.R.id.content));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<Participant> participants = new ArrayList<Participant>();

                if (quickStart_tb.isChecked()) {//Quick start

                    final String numOfPlayers_String = numOfPlayers_et.getText().toString();

                    if (numOfPlayers_String.trim().length() == 0) {
                        Toast.makeText(getApplicationContext(), R.string.numPlayersUnspecifiedMsg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    int numberOfParticipants = 0;
                    try {
                        numberOfParticipants = Integer.parseInt(numOfPlayers_String);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), R.string.notANumberMsg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (numberOfParticipants < 3) {
                        Toast.makeText(getApplicationContext(), R.string.lessThan3Msg, Toast.LENGTH_LONG).show();
                        return;
                    }


                    for (int i = 0; i < numberOfParticipants; i++) {
                        participants.add(new Participant(new Person(getString(R.string.participantDefaultName, i + 1), "")));
                    }

                } else {//full Start
                    final int numberOfParticipants = personSelectedSet.size();

                    if (numberOfParticipants < 3) {
                        Toast.makeText(getApplicationContext(), R.string.lessThan3Msg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (final Person person : personSelectedSet)
                        participants.add(new Participant(person));

                    Collections.sort(participants);
                }


                final boolean randomSeeding = randomSeed_rb.isChecked();

                final String title = ((EditText) findViewById(R.id.tournamentTitle_et)).getText().toString();
                final String description = ((EditText) findViewById(R.id.tournamentDescription_et)).getText().toString();

                final Seeder.Type seedType = randomSeeding ? Seeder.Type.RANDOM : Seeder.Type.CUSTOM;
                final Tournament.TournamentType tournamentType = elimination_rb.isChecked() ? Tournament.TournamentType.ELIMINATION
                        : doubleElimination_rb.isChecked() ? Tournament.TournamentType.DOUBLE_ELIMINATION
                        : roundRobin_rb.isChecked() ? Tournament.TournamentType.ROUND_ROBIN
                        : swiss_rb.isChecked() ? Tournament.TournamentType.SWISS
                        : Tournament.TournamentType.SURVIVAL;

                final String rankingConfig = PreferenceUtil.getRankingConfig( PreferenceManager.getDefaultSharedPreferences(MainActivity.this),tournamentType);
                TournamentActivity.startTournamentActivity(MainActivity.this, 0, seedType, tournamentType, tournamentType, title, description, rankingConfig, participants, null, null, Tournament.NULL_TIME_VALUE, Tournament.NULL_TIME_VALUE, false);

            }
        });

        handleIfIABSetUpFailed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ManagementActivity.REQUEST_CODE)
            handlePersonsFromDB();
    }

    private void handlePersonsFromDB() {
        final ViewGroup personsGroup = (ViewGroup) findViewById(R.id.personsGroup);
        personsGroup.removeAllViews();
        personSelectedSet.clear();

        final ToggleButton quickStart_tb = (ToggleButton) findViewById(R.id.quickStart_tb);
        final TextView numOfPlayersSelected_tv = (TextView) findViewById(R.id.numOfPlayersSelected_tv);
        if (!quickStart_tb.isChecked())
            numOfPlayersSelected_tv.setText(getString(R.string.numberOfPlayersSelected, 0));

        final List<Group> groupList = dh.getAllGroupsWithPersons();


        for (final Group group : groupList) {

            final TextView group_tv = new TextView(this);
            group_tv.setText(group.getName());
            if (Build.VERSION.SDK_INT < 23) {
                group_tv.setTextAppearance(this, android.R.style.TextAppearance_Medium);
            } else {
                group_tv.setTextAppearance(android.R.style.TextAppearance_Medium);
            }
            group_tv.setTypeface(null, Typeface.BOLD);
            group_tv.setPadding(0, 15, 0, 0);
            personsGroup.addView(group_tv);

            for (int p = 0; p < group.getTotalPersons(); p++) {
                final Person person = group.getPersonAt(p);

                final CheckBox person_cb = new CheckBox(this);
                person_cb.setText(person.getName());
                person_cb.setTag(person);
                person_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        final Person person = (Person) buttonView.getTag();
                        if (isChecked)
                            personSelectedSet.add(person);
                        else
                            personSelectedSet.remove(person);

                        numOfPlayersSelected_tv.setText(getString(R.string.numberOfPlayersSelected, personSelectedSet.size()));

                    }
                });

                personsGroup.addView(person_cb);
            }
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        this.menu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions_main, menu);

        resolveDisableAdMenuButton();
        return super.onCreateOptionsMenu(menu);
    }

    private void resolveDisableAdMenuButton() {
        if (menu != null) {
            final MenuItem menuItem = menu.findItem(R.id.action_upgrade);
            if (menuItem != null)
                menuItem.setVisible(!isPremium());

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_upgrade:
                upgrade();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_management) {
            startActivityForResult(new Intent(this, ManagementActivity.class), ManagementActivity.REQUEST_CODE);
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(this, HistoryActivity.class));
        } else if (id == R.id.nav_app_info) {
            startActivity(new Intent(this, AppInfoActivity.class));

        } else if (id == R.id.nav_rate) {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
            if (intent.resolveActivity(getPackageManager()) != null)
                startActivity(intent);
            else
                Toast.makeText(getApplicationContext(), R.string.playStoreNotFound, Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_email) {
            EmailUtil.sendEmail(this, new String[]{"andrew.wong.dgnt@gmail.com"}, getString(R.string.app_name), null, null);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected boolean silentComplainOnStartUp() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isPremium())
            getAdView().destroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isPremium())
            getAdView().pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isPremium())
            getAdView().resume();
    }

    @Override
    protected void handleAfterIABResolution() {
        super.handleAfterIABResolution();

        final AdView adView = getAdView();
        adView.setVisibility(isPremium() ? View.GONE : View.VISIBLE);
        if (!isPremium()) {
            adView.loadAd(adRequest);
        } else {
            adView.destroy();
        }
        resolveDisableAdMenuButton();
    }
}
