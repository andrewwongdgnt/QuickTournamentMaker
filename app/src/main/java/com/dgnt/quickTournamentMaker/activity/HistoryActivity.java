package com.dgnt.quickTournamentMaker.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.SpannableString;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dgnt.quickTournamentMaker.R;
import com.dgnt.quickTournamentMaker.activity.tournament.TournamentActivity;
import com.dgnt.quickTournamentMaker.adapter.HistoryAdapter;
import com.dgnt.quickTournamentMaker.model.history.HistoricalFilters;
import com.dgnt.quickTournamentMaker.model.history.HistoricalTournament;
import com.dgnt.quickTournamentMaker.model.tournament.Seeder;
import com.dgnt.quickTournamentMaker.model.tournament.Tournament;
import com.dgnt.quickTournamentMaker.util.DatabaseHelper;
import com.dgnt.quickTournamentMaker.util.LayoutUtil;
import com.dgnt.quickTournamentMaker.util.PreferenceUtil;

import org.joda.time.DateTime;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private final static int HISTORY_REQUEST_CODE = 1;
    private final static int START_TOURNAMENT_FROM_FILE_REQUEST_CODE = 2;

    private DatabaseHelper db;

    private HistoryAdapter historyAdapter;
    private ListView historicalTournaments_lv;

    private List<HistoricalTournament> historicalTournamentList = new ArrayList<>();
    private List<HistoricalTournament> allHistoricalTournamentList = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    private TextView searchClearPrompt_tv;
    private TextView filtersApplied_tv;
    private TextView resultInformation_tv;

    private String searchTerm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        historyAdapter = new HistoryAdapter(this, R.layout.history_item, historicalTournamentList);

        historicalTournaments_lv = (ListView) findViewById(R.id.historicalTournaments_lv);
        historicalTournaments_lv.setAdapter(historyAdapter);
        historicalTournaments_lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        historicalTournaments_lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.actions_history_cab, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                historyAdapter.notifyDataSetChanged();

                final int checkedCount = historicalTournaments_lv.getCheckedItemCount();

                MenuItem editItem = menu.findItem(R.id.action_edit);
                editItem.setVisible(checkedCount <= 1);

                MenuItem moreInfoItem = menu.findItem(R.id.action_tournamentInfo);
                moreInfoItem.setVisible(checkedCount <= 1);

                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_edit:
                        openTournamentEditor();
                        return true;
                    case R.id.action_tournamentInfo:
                        openTournamentInfo();
                        return true;
                    case R.id.action_delete:
                        deleteTournament();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = historicalTournaments_lv.getCheckedItemCount();
                mode.setTitle(String.valueOf(checkedCount));
                mode.invalidate();


            }
        });

        historicalTournaments_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final HistoricalTournament historicalTournament = historicalTournamentList.get(position);
                startTournament(historicalTournament);
            }
        });

        searchClearPrompt_tv = (TextView) findViewById(R.id.searchClearPrompt_tv);
        searchClearPrompt_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTerm = null;
                updateTournamentListViewFromDB();


            }
        });

        filtersApplied_tv = (TextView) findViewById(R.id.filtersApplied_tv);

        resultInformation_tv = (TextView) findViewById(R.id.resultInformation_tv);
        resultInformation_tv.setText(historicalTournamentList.size() == 0 ? getString(R.string.noResultsMsg) : getString(R.string.historicalItemHintMsg));

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(final Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {


            searchTerm = intent.getStringExtra(SearchManager.QUERY);


            //if user_query_spannable is null, then it is from voice search
            //source: http://stackoverflow.com/questions/36846352/how-to-determine-if-a-search-was-performed-through-text-input-or-voice-recogniti
            final SpannableString user_query_spannable = (SpannableString) intent.getCharSequenceExtra(SearchManager.USER_QUERY);
            final boolean isFromVoiceSearch = user_query_spannable == null;

            if (isFromVoiceSearch)
                Toast.makeText(this, "\"" + searchTerm + "\"", Toast.LENGTH_LONG).show();
        } else {
            searchTerm = null;
        }
        updateTournamentListViewFromDB();

    }

    private DatabaseHelper getDb() {
        if (db == null)
            db = new DatabaseHelper(this);
        return db;
    }

    private void startTournament(final HistoricalTournament historicalTournament) {

        TournamentActivity.startTournamentActivity(HistoryActivity.this, HISTORY_REQUEST_CODE, Seeder.Type.SAME, historicalTournament);
    }

    private void deleteTournament() {

        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.deleteTournamentMsg, historicalTournaments_lv.getCheckedItemCount()))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        final SparseBooleanArray sparseBooleanArray = historicalTournaments_lv.getCheckedItemPositions();

                        int numOfDeletedTournaments = 0;
                        for (int i = historicalTournamentList.size() - 1; i >= 0; i--) {
                            final boolean checked = sparseBooleanArray.get(i);
                            if (checked) {
                                getDb().deleteTournament(historicalTournamentList.get(i).getCreationTimeInEpoch());
                                historicalTournaments_lv.setItemChecked(i, false);
                                numOfDeletedTournaments++;
                            }
                        }

                        updateTournamentListViewFromDB();

                        Toast.makeText(HistoryActivity.this, getString(R.string.deleteTournamentSuccessfulMsg, numOfDeletedTournaments), Toast.LENGTH_LONG).show();

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();

    }


    private HistoricalTournament getAndUnCheckHistoricalTournament(final ListView historicalTournaments_lv) {
        if (historicalTournaments_lv == null)
            return null;

        final SparseBooleanArray sparseBooleanArray = historicalTournaments_lv.getCheckedItemPositions();

        for (int i = 0; i < historicalTournamentList.size(); i++) {
            final boolean checked = sparseBooleanArray.get(i);
            if (checked) {
                historicalTournaments_lv.setItemChecked(i, false);
                return historicalTournamentList.get(i);
            }
        }

        return null;
    }

    private View.OnClickListener dateClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {

            final DatePicker datePicker = new DatePicker(v.getContext());

            final EditText et = (EditText) v;
            final long epoch = et.getTag() == null ? -1 : (Long) et.getTag();
            if (epoch >= 0) {
                final DateTime dt = new DateTime(epoch);
                datePicker.updateDate(dt.getYear(), dt.getMonthOfYear() - 1, dt.getDayOfMonth());
            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

            builder.setView(datePicker);

            builder.setPositiveButton(v.getContext().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {


                    final DateTime dt = new DateTime(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(), 0, 0, 0, 0);

                    final long milli = dt.getMillis();
                    final String dateFormat = DateFormat.getDateInstance(DateFormat.SHORT).format(milli);
                    et.setText(dateFormat);
                    et.setTag(milli);
                    dialogInterface.cancel();
                }
            });
            builder.setNegativeButton(v.getContext().getString(android.R.string.cancel), null);

            builder.create().show();
        }
    };

    private static CompoundButton.OnCheckedChangeListener getFilterOnCheckedChangeListener(final EditText editText) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editText.setEnabled(isChecked);
            }
        };
    }

    private void openFilterOptions() {

        final View layout_filter_options = LayoutInflater.from(this).inflate(R.layout.layout_filter_options, null);

        final CheckBox filterOnElimination_cb = (CheckBox) layout_filter_options.findViewById(R.id.filterOnElimination_cb);
        filterOnElimination_cb.setChecked(PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.ELIMINATION));

        final CheckBox filterOnDoubleElimination_cb = (CheckBox) layout_filter_options.findViewById(R.id.filterOnDoubleElimination_cb);
        filterOnDoubleElimination_cb.setChecked(PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.DOUBLE_ELIMINATION));

        final CheckBox filterOnRoundRobin_cb = (CheckBox) layout_filter_options.findViewById(R.id.filterOnRoundRobin_cb);
        filterOnRoundRobin_cb.setChecked(PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.ROUND_ROBIN));

        final CheckBox filterOnSwiss_cb = (CheckBox) layout_filter_options.findViewById(R.id.filterOnSwiss_cb);
        filterOnSwiss_cb.setChecked(PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.SWISS));

        final CheckBox filterOnSurvival_cb = (CheckBox) layout_filter_options.findViewById(R.id.filterOnSurvival_cb);
        filterOnSurvival_cb.setChecked(PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.SURVIVAL));

        final EditText minParticipant_et = (EditText) layout_filter_options.findViewById(R.id.minParticipant_et);
        final int minParticipants = PreferenceUtil.getMinParticipantFilter(sharedPreferences);
        minParticipant_et.setText(minParticipants < 0 ? "" : String.valueOf(minParticipants));

        final CheckBox minParticipant_cb = (CheckBox) layout_filter_options.findViewById(R.id.minParticipant_cb);
        minParticipant_cb.setOnCheckedChangeListener(getFilterOnCheckedChangeListener(minParticipant_et));
        minParticipant_cb.setChecked(PreferenceUtil.isMinParticipantFilterAllowed(sharedPreferences));

        final EditText maxParticipant_et = (EditText) layout_filter_options.findViewById(R.id.maxParticipant_et);
        final int maxParticipants = PreferenceUtil.getMaxParticipantFilter(sharedPreferences);
        maxParticipant_et.setText(maxParticipants < 0 ? "" : String.valueOf(maxParticipants));

        final CheckBox maxParticipant_cb = (CheckBox) layout_filter_options.findViewById(R.id.maxParticipant_cb);
        maxParticipant_cb.setOnCheckedChangeListener(getFilterOnCheckedChangeListener(maxParticipant_et));
        maxParticipant_cb.setChecked(PreferenceUtil.isMaxParticipantFilterAllowed(sharedPreferences));

        final EditText earliestCreatedDate_et = (EditText) layout_filter_options.findViewById(R.id.earliestCreatedDate_et);
        final long earliestCreatedDate = PreferenceUtil.getEarliestCreatedEpochFilter(sharedPreferences);
        earliestCreatedDate_et.setText(earliestCreatedDate < 0 ? "" : DateFormat.getDateInstance(DateFormat.SHORT).format(earliestCreatedDate));
        earliestCreatedDate_et.setTag(earliestCreatedDate);
        earliestCreatedDate_et.setOnClickListener(dateClickListener);

        final CheckBox earliestCreatedDate_cb = (CheckBox) layout_filter_options.findViewById(R.id.earliestCreatedDate_cb);
        earliestCreatedDate_cb.setOnCheckedChangeListener(getFilterOnCheckedChangeListener(earliestCreatedDate_et));
        earliestCreatedDate_cb.setChecked(PreferenceUtil.isEarliestCreatedEpochFilterAllowed(sharedPreferences));

        final EditText latestCreatedDate_et = (EditText) layout_filter_options.findViewById(R.id.latestCreatedDate_et);
        final long latestCreatedEpoch = PreferenceUtil.getLatestCreatedEpochFilter(sharedPreferences);
        latestCreatedDate_et.setText(latestCreatedEpoch < 0 ? "" : DateFormat.getDateInstance(DateFormat.SHORT).format(latestCreatedEpoch));
        latestCreatedDate_et.setTag(latestCreatedEpoch);
        latestCreatedDate_et.setOnClickListener(dateClickListener);

        final CheckBox latestCreatedDate_cb = (CheckBox) layout_filter_options.findViewById(R.id.latestCreatedDate_cb);
        latestCreatedDate_cb.setOnCheckedChangeListener(getFilterOnCheckedChangeListener(latestCreatedDate_et));
        latestCreatedDate_cb.setChecked(PreferenceUtil.isLatestCreatedEpochFilterAllowed(sharedPreferences));

        final EditText earliestModifiedDate_et = (EditText) layout_filter_options.findViewById(R.id.earliestModifiedDate_et);
        final long earliestModifiedDate = PreferenceUtil.getEarliestModifiedEpochFilter(sharedPreferences);
        earliestModifiedDate_et.setText(earliestModifiedDate < 0 ? "" : DateFormat.getDateInstance(DateFormat.SHORT).format(earliestModifiedDate));
        earliestModifiedDate_et.setTag(earliestModifiedDate);
        earliestModifiedDate_et.setOnClickListener(dateClickListener);

        final CheckBox earliestModifiedDate_cb = (CheckBox) layout_filter_options.findViewById(R.id.earliestModifiedDate_cb);
        earliestModifiedDate_cb.setOnCheckedChangeListener(getFilterOnCheckedChangeListener(earliestModifiedDate_et));
        earliestModifiedDate_cb.setChecked(PreferenceUtil.isEarliestModifiedEpochFilterAllowed(sharedPreferences));

        final EditText latestModifiedDate_et = (EditText) layout_filter_options.findViewById(R.id.latestModifiedDate_et);
        final long latestModifiedEpoch = PreferenceUtil.getLatestModifiedEpochFilter(sharedPreferences);
        latestModifiedDate_et.setText(latestModifiedEpoch < 0 ? "" : DateFormat.getDateInstance(DateFormat.SHORT).format(latestModifiedEpoch));
        latestModifiedDate_et.setTag(latestModifiedEpoch);
        latestModifiedDate_et.setOnClickListener(dateClickListener);

        final CheckBox latestModifiedDate_cb = (CheckBox) layout_filter_options.findViewById(R.id.latestModifiedDate_cb);
        latestModifiedDate_cb.setOnCheckedChangeListener(getFilterOnCheckedChangeListener(latestModifiedDate_et));
        latestModifiedDate_cb.setChecked(PreferenceUtil.isLatestModifiedEpochFilterAllowed(sharedPreferences));

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout_filter_options);

        builder.setTitle(getResources().getString(R.string.filter));

        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {

                final SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean(PreferenceUtil.getTournamentTypeFilterKey(Tournament.TournamentType.ELIMINATION), filterOnElimination_cb.isChecked());
                editor.putBoolean(PreferenceUtil.getTournamentTypeFilterKey(Tournament.TournamentType.DOUBLE_ELIMINATION), filterOnDoubleElimination_cb.isChecked());
                editor.putBoolean(PreferenceUtil.getTournamentTypeFilterKey(Tournament.TournamentType.ROUND_ROBIN), filterOnRoundRobin_cb.isChecked());
                editor.putBoolean(PreferenceUtil.getTournamentTypeFilterKey(Tournament.TournamentType.SWISS), filterOnSwiss_cb.isChecked());
                editor.putBoolean(PreferenceUtil.getTournamentTypeFilterKey(Tournament.TournamentType.SURVIVAL), filterOnSurvival_cb.isChecked());

                editor.putBoolean(PreferenceUtil.PREF_FILTER_MAX_PARTICIPANT_ALLOWED_KEY, maxParticipant_cb.isChecked());
                final String maxParticipants_string = maxParticipant_et.getText().toString();
                editor.putInt(PreferenceUtil.PREF_FILTER_MAX_PARTICIPANT_KEY, maxParticipants_string.trim().equals("") ? -1 : Integer.parseInt(maxParticipants_string));
                editor.putBoolean(PreferenceUtil.PREF_FILTER_MIN_PARTICIPANT_ALLOWED_KEY, minParticipant_cb.isChecked());
                final String minParticipants_string = minParticipant_et.getText().toString();
                editor.putInt(PreferenceUtil.PREF_FILTER_MIN_PARTICIPANT_KEY, minParticipants_string.trim().equals("") ? -1 : Integer.parseInt(minParticipants_string));

                editor.putBoolean(PreferenceUtil.PREF_FILTER_EARLIEST_CREATED_EPOCH_ALLOWED_KEY, earliestCreatedDate_cb.isChecked());
                editor.putLong(PreferenceUtil.PREF_FILTER_EARLIEST_CREATED_EPOCH_KEY, earliestCreatedDate_et.getTag() == null ? -1 : (Long) earliestCreatedDate_et.getTag());
                editor.putBoolean(PreferenceUtil.PREF_FILTER_LATEST_CREATED_EPOCH_ALLOWED_KEY, latestCreatedDate_cb.isChecked());
                editor.putLong(PreferenceUtil.PREF_FILTER_LATEST_CREATED_EPOCH_KEY, latestCreatedDate_et.getTag() == null ? -1 : (Long) latestCreatedDate_et.getTag());

                editor.putBoolean(PreferenceUtil.PREF_FILTER_EARLIEST_MODIFIED_EPOCH_ALLOWED_KEY, earliestModifiedDate_cb.isChecked());
                editor.putLong(PreferenceUtil.PREF_FILTER_EARLIEST_MODIFIED_EPOCH_KEY, earliestModifiedDate_et.getTag() == null ? -1 : (Long) earliestModifiedDate_et.getTag());
                editor.putBoolean(PreferenceUtil.PREF_FILTER_LATEST_MODIFIED_EPOCH_ALLOWED_KEY, latestModifiedDate_cb.isChecked());
                editor.putLong(PreferenceUtil.PREF_FILTER_LATEST_MODIFIED_EPOCH_KEY, latestModifiedDate_et.getTag() == null ? -1 : (Long) latestModifiedDate_et.getTag());

                editor.commit();
                updateTournamentListViewFromDB();

            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), null);
        final AlertDialog alert = builder.create();
        alert.show();


    }

    private void openTournamentInfo() {
        final HistoricalTournament historicalTournament = getAndUnCheckHistoricalTournament(historicalTournaments_lv);

        LayoutUtil.openTournamentInfo(this, historicalTournament);

    }


    private void openTournamentEditor() {

        final HistoricalTournament historicalTournament = getAndUnCheckHistoricalTournament(historicalTournaments_lv);

        final ViewGroup layout_tournament_editor = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_tournament_editor, null);

        final EditText tournamentTitle_et = (EditText) layout_tournament_editor.findViewById(R.id.tournamentTitle_et);
        tournamentTitle_et.setText(historicalTournament.getName());

        final EditText tournamentDescription_et = (EditText) layout_tournament_editor.findViewById(R.id.tournamentDescription_et);
        tournamentDescription_et.setText(historicalTournament.getNote());

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(layout_tournament_editor);
        builder.setTitle(getString(R.string.editTournament));
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                getDb().updateTournament(historicalTournament.getCreationTimeInEpoch(), System.currentTimeMillis(), historicalTournament.getType(), tournamentTitle_et.getText().toString(), tournamentDescription_et.getText().toString(), historicalTournament.getRoundList(), historicalTournament.getMatchUpList(), historicalTournament.getParticipantList(), historicalTournament.getRankingConfig());

                updateTournamentListViewFromDB();

            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), null);
        builder.create().show();
    }

    private boolean areFiltersApplied() {
        if (PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.ELIMINATION))
            return true;
        if (PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.DOUBLE_ELIMINATION))
            return true;
        if (PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.ROUND_ROBIN))
            return true;
        if (PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.SWISS))
            return true;
        if (PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.SURVIVAL))
            return true;

        if (PreferenceUtil.isMinParticipantFilterAllowed(sharedPreferences) && PreferenceUtil.getMinParticipantFilter(sharedPreferences) > 0)
            return true;
        if (PreferenceUtil.isMaxParticipantFilterAllowed(sharedPreferences) && PreferenceUtil.getMaxParticipantFilter(sharedPreferences) > 0)
            return true;

        if (PreferenceUtil.isEarliestCreatedEpochFilterAllowed(sharedPreferences) && PreferenceUtil.getEarliestCreatedEpochFilter(sharedPreferences) > 0)
            return true;
        if (PreferenceUtil.isLatestCreatedEpochFilterAllowed(sharedPreferences) && PreferenceUtil.getLatestCreatedEpochFilter(sharedPreferences) > 0)
            return true;


        if (PreferenceUtil.isEarliestModifiedEpochFilterAllowed(sharedPreferences) && PreferenceUtil.getEarliestModifiedEpochFilter(sharedPreferences) > 0)
            return true;
        if (PreferenceUtil.isLatestModifiedEpochFilterAllowed(sharedPreferences) && PreferenceUtil.getLatestModifiedEpochFilter(sharedPreferences) > 0)
            return true;

        return false;
    }

    private void updateTournamentListViewFromDB() {

        historicalTournamentList.clear();
        allHistoricalTournamentList.clear();
        historicalTournamentList.addAll(getDb().getAllTournaments(PreferenceUtil.getHistoricalTournamentSort(sharedPreferences), searchTerm, HistoricalFilters.build(sharedPreferences)));
        allHistoricalTournamentList.addAll(historicalTournamentList);

        historyAdapter.setSearchTerm(searchTerm);
        historyAdapter.notifyDataSetChanged();

        resultInformation_tv.setText(allHistoricalTournamentList.size() == 0 ? getString(R.string.noResultsMsg) : getString(R.string.historicalItemHintMsg));

        filtersApplied_tv.setVisibility(areFiltersApplied() ? View.VISIBLE : View.GONE);

        searchClearPrompt_tv.setVisibility(searchTerm != null && searchTerm.trim().length() > 0 ? View.VISIBLE : View.GONE);
        searchClearPrompt_tv.setText(Html.fromHtml("<u>" + getString(R.string.clearSearch, searchTerm) + "</u>"));

        final SparseBooleanArray sparseBooleanArray = historicalTournaments_lv.getCheckedItemPositions();

        for (int i = historicalTournamentList.size() - 1; i >= 0; i--) {
            final boolean checked = sparseBooleanArray.get(i);
            if (checked) {

                historicalTournaments_lv.setItemChecked(i, false);
            }
        }
    }


    private void openSortingOptions() {
        final View layout_sorting_options = LayoutInflater.from(this).inflate(R.layout.layout_sorting_options, null);


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout_sorting_options);

        builder.setTitle(getResources().getString(R.string.sortOptionsTitle));

        builder.setPositiveButton(getString(android.R.string.cancel), null);
        final AlertDialog alert = builder.create();
        alert.show();

        final RadioButton sortCreationTimeNewest_rb = (RadioButton) layout_sorting_options.findViewById(R.id.sortCreationTimeNewest_rb);
        sortCreationTimeNewest_rb.setTag(HistoricalTournament.Sort.CREATION_DATE_NEWEST.name());
        sortCreationTimeNewest_rb.setChecked(PreferenceUtil.getHistoricalTournamentSort(sharedPreferences) == HistoricalTournament.Sort.CREATION_DATE_NEWEST);

        final RadioButton sortCreationTimeOldest_rb = (RadioButton) layout_sorting_options.findViewById(R.id.sortCreationTimeOldest_rb);
        sortCreationTimeOldest_rb.setTag(HistoricalTournament.Sort.CREATION_DATE_OLDEST.name());
        sortCreationTimeOldest_rb.setChecked(PreferenceUtil.getHistoricalTournamentSort(sharedPreferences) == HistoricalTournament.Sort.CREATION_DATE_OLDEST);

        final RadioButton sortLastModifiedTimeNewest_rb = (RadioButton) layout_sorting_options.findViewById(R.id.sortLastModifiedTimeNewest_rb);
        sortLastModifiedTimeNewest_rb.setTag(HistoricalTournament.Sort.LAST_MODIFIED_DATE_NEWEST.name());
        sortLastModifiedTimeNewest_rb.setChecked(PreferenceUtil.getHistoricalTournamentSort(sharedPreferences) == HistoricalTournament.Sort.LAST_MODIFIED_DATE_NEWEST);

        final RadioButton sortLastModifiedTimeOldest_rb = (RadioButton) layout_sorting_options.findViewById(R.id.sortLastModifiedTimeOldest_rb);
        sortLastModifiedTimeOldest_rb.setTag(HistoricalTournament.Sort.LAST_MODIFIED_DATE_OLDEST.name());
        sortLastModifiedTimeOldest_rb.setChecked(PreferenceUtil.getHistoricalTournamentSort(sharedPreferences) == HistoricalTournament.Sort.LAST_MODIFIED_DATE_OLDEST);

        final RadioButton sortName_rb = (RadioButton) layout_sorting_options.findViewById(R.id.sortName_rb);
        sortName_rb.setTag(HistoricalTournament.Sort.NAME.name());
        sortName_rb.setChecked(PreferenceUtil.getHistoricalTournamentSort(sharedPreferences) == HistoricalTournament.Sort.NAME);

        final RadioButton sortNameReversed_rb = (RadioButton) layout_sorting_options.findViewById(R.id.sortNameReversed_rb);
        sortNameReversed_rb.setTag(HistoricalTournament.Sort.NAME_REVERSED.name());
        sortNameReversed_rb.setChecked(PreferenceUtil.getHistoricalTournamentSort(sharedPreferences) == HistoricalTournament.Sort.NAME_REVERSED);

        final RadioButton sortTournamentType_rb = (RadioButton) layout_sorting_options.findViewById(R.id.sortTournamentType_rb);
        sortTournamentType_rb.setTag(HistoricalTournament.Sort.TOURNAMENT_TYPE.name());
        sortTournamentType_rb.setChecked(PreferenceUtil.getHistoricalTournamentSort(sharedPreferences) == HistoricalTournament.Sort.TOURNAMENT_TYPE);

        final RadioButton sortTournamentTypeReversed_rb = (RadioButton) layout_sorting_options.findViewById(R.id.sortTournamentTypeReversed_rb);
        sortTournamentTypeReversed_rb.setTag(HistoricalTournament.Sort.TOURNAMENT_TYPE_REVERSED.name());
        sortTournamentTypeReversed_rb.setChecked(PreferenceUtil.getHistoricalTournamentSort(sharedPreferences) == HistoricalTournament.Sort.TOURNAMENT_TYPE_REVERSED);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PreferenceUtil.PREF_HISTORICAL_TOURNAMENT_SORT_KEY, v.getTag().toString());
                editor.commit();
                updateTournamentListViewFromDB();
                alert.cancel();
            }

        };
        sortCreationTimeNewest_rb.setOnClickListener(onClickListener);
        sortCreationTimeOldest_rb.setOnClickListener(onClickListener);
        sortLastModifiedTimeNewest_rb.setOnClickListener(onClickListener);
        sortLastModifiedTimeOldest_rb.setOnClickListener(onClickListener);
        sortName_rb.setOnClickListener(onClickListener);
        sortNameReversed_rb.setOnClickListener(onClickListener);
        sortTournamentType_rb.setOnClickListener(onClickListener);
        sortTournamentTypeReversed_rb.setOnClickListener(onClickListener);

    }

    private void openViewingOptions() {
        final View layout_viewing_options = LayoutInflater.from(this).inflate(R.layout.layout_viewing_options, null);


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout_viewing_options);

        builder.setTitle(getResources().getString(R.string.viewMode));

        builder.setPositiveButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();

        final RadioButton viewAsMinimal_rb = (RadioButton) layout_viewing_options.findViewById(R.id.viewAsMinimal_rb);
        viewAsMinimal_rb.setTag(HistoricalTournament.View.MINIMAL.name());
        viewAsMinimal_rb.setChecked(PreferenceUtil.getHistoricalTournamentViewMode(sharedPreferences) == HistoricalTournament.View.MINIMAL);

        final RadioButton viewAsBasic_rb = (RadioButton) layout_viewing_options.findViewById(R.id.viewAsBasic_rb);
        viewAsBasic_rb.setTag(HistoricalTournament.View.BASIC.name());
        viewAsBasic_rb.setChecked(PreferenceUtil.getHistoricalTournamentViewMode(sharedPreferences) == HistoricalTournament.View.BASIC);

        final RadioButton viewAsDetailed_rb = (RadioButton) layout_viewing_options.findViewById(R.id.viewAsDetailed_rb);
        viewAsDetailed_rb.setTag(HistoricalTournament.View.DETAILED.name());
        viewAsDetailed_rb.setChecked(PreferenceUtil.getHistoricalTournamentViewMode(sharedPreferences) == HistoricalTournament.View.DETAILED);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PreferenceUtil.PREF_HISTORICAL_TOURNAMENT_VIEW_KEY, v.getTag().toString());
                editor.commit();
                historyAdapter.notifyDataSetChanged();

                alert.cancel();
            }

        };
        viewAsMinimal_rb.setOnClickListener(onClickListener);
        viewAsBasic_rb.setOnClickListener(onClickListener);
        viewAsDetailed_rb.setOnClickListener(onClickListener);

    }


    private void choosePathToStartTournament() {

        Intent intent = new Intent(this, DirectoryPickerActivity.class);
        intent.putExtra(DirectoryPickerActivity.FILE_PICKER, true);

        startActivityForResult(intent, START_TOURNAMENT_FROM_FILE_REQUEST_CODE);
    }

    private void startTournamentFromFilePath(final String path) {

        final File file = new File(path);

        final StringBuilder stringBuilder = new StringBuilder();

        try {
            final BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            br.close();

            final String content = stringBuilder.toString();

            final HistoricalTournament historicalTournament = Tournament.JsonHelper.fromJson(content);

            startTournament(historicalTournament);
        } catch (Exception e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(e instanceof JSONException ? getString(R.string.corruptFileMsg) : getString(R.string.startTournamentFromFileFail, e.getMessage()));
            builder.setPositiveButton(getString(android.R.string.ok), null);
            builder.create().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == HISTORY_REQUEST_CODE) {
            updateTournamentListViewFromDB();
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == START_TOURNAMENT_FROM_FILE_REQUEST_CODE) {
                final String path = (String) data.getExtras().get(DirectoryPickerActivity.CHOSEN_DIRECTORY);
                startTournamentFromFilePath(path);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions_history, menu);


        // Get the SearchView and set the searchable configuration
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_startTournamentFromFile:
                choosePathToStartTournament();
                return true;
            case R.id.action_sort:
                openSortingOptions();
                return true;
            case R.id.action_filter:
                openFilterOptions();
                return true;
            case R.id.action_viewAs:
                openViewingOptions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
