package com.dgnt.quickTournamentMaker.activity.tournament;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dgnt.quickTournamentMaker.R;
import com.dgnt.quickTournamentMaker.activity.DirectoryPickerActivity;
import com.dgnt.quickTournamentMaker.activity.InAppBillingActivity;
import com.dgnt.quickTournamentMaker.adapter.ColorAdapter;
import com.dgnt.quickTournamentMaker.adapter.RankingAdapter;
import com.dgnt.quickTournamentMaker.eventListener.OnMatchUpUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnParticipantUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnRoundUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnTournamentUpdateListener;
import com.dgnt.quickTournamentMaker.layout.TournamentLayout;
import com.dgnt.quickTournamentMaker.model.history.HistoricalMatchUp;
import com.dgnt.quickTournamentMaker.model.history.HistoricalRound;
import com.dgnt.quickTournamentMaker.model.history.HistoricalTournament;
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp;
import com.dgnt.quickTournamentMaker.model.tournament.Participant;
import com.dgnt.quickTournamentMaker.model.tournament.ParticipantCoordinates;
import com.dgnt.quickTournamentMaker.model.tournament.Ranking;
import com.dgnt.quickTournamentMaker.model.tournament.RecordKeepingTournament;
import com.dgnt.quickTournamentMaker.model.tournament.Round;
import com.dgnt.quickTournamentMaker.model.tournament.Seeder;
import com.dgnt.quickTournamentMaker.model.tournament.SingleMatchUpTournament;
import com.dgnt.quickTournamentMaker.model.tournament.Tournament;
import com.dgnt.quickTournamentMaker.task.ExportTournamentTask;
import com.dgnt.quickTournamentMaker.util.AdsUtil;
import com.dgnt.quickTournamentMaker.util.DatabaseHelper;
import com.dgnt.quickTournamentMaker.util.TournamentUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract public class TournamentActivity extends InAppBillingActivity implements OnRoundUpdateListener, OnMatchUpUpdateListener, OnParticipantUpdateListener, OnTournamentUpdateListener {

    public static final String INTENT_HISTORICAL_ROUNDS_KEY = "historicalRoundsKey";
    public static final String INTENT_HISTORICAL_MATCH_UPS_KEY = "historicalMatchUpsKey";
    public static final String INTENT_PARTICIPANTS_KEY = "participantsKey";
    public static final String INTENT_TOURNAMENT_TITLE_KEY = "tournamentTitleKey";
    public static final String INTENT_TOURNAMENT_DESCRIPTION_KEY = "tournamentDescriptionKey";
    public final static String INTENT_TOURNAMENT_TYPE_KEY = "tournamentTypeKey";
    public static final String INTENT_TOURNAMENT_CREATION_TIME_KEY = "tournamentCreationTimeKey";
    public static final String INTENT_TOURNAMENT_LAST_MODIFIED_TIME_KEY = "tournamentLastModifiedTimeKey";
    public static final String INTENT_TOURNAMENT_IS_REBUILT_KEY = "tournamentIsRebuiltKey";

    private static final int DESCRIPTION_MAX_CHARACTERS_PER_LINE = 30;
    private static final int DESCRIPTION_TOTAL_MAX_CHARACTERS = 100;

    protected Tournament tournament;

    protected Map<String, ViewGroup> participantGroupMap = new HashMap<>();
    protected Map<Round, TextView> roundTextViewMap = new HashMap<>();
    protected Map<String, ViewGroup> matchUpGroupMap = new HashMap<>();

    private LinearLayout matchUps_ll;
    private LinearLayout roundHeaders_ll;

    private DatabaseHelper db;

    private boolean unSavedChanges;

    private Menu menu;
    private View tournamentView_root;
    protected TournamentLayout tournamentLayout;
    private TextView description_tv;

    AdRequest adRequest = AdsUtil.buildAdRequestWithTestDevices();
    AdView adView;

    public AdView getAdView() {
        if (adView == null)
            adView = (AdView) findViewById(R.id.adView);
        return adView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        findAndSetAllViewIds();
        createTournament();
        setUpTournamentLayout();
        buildTournamentAndDisplay();
        setValuesToTournament();
        setValuesToRounds();
        setValuesToMatchUps();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(TournamentUtil.getProperTournamentDrawable(tournament.getType(), true));
        actionBar.setDisplayUseLogoEnabled(true);

        setUnSavedChanges(getIntent().getBooleanExtra(INTENT_TOURNAMENT_IS_REBUILT_KEY, false));
        handleIfIABSetUpFailed();
    }

    private DatabaseHelper getDb() {
        if (db == null)
            db = new DatabaseHelper(this);
        return db;
    }

    private void findAndSetAllViewIds() {
        tournamentView_root = findViewById(R.id.tournamentView_root);
        tournamentLayout = (TournamentLayout) findViewById(R.id.tournamentLayout);

        description_tv = (TextView) findViewById(R.id.description_tv);
        description_tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openTournamentEditor();
                return false;
            }
        });

    }

    abstract protected void createTournament();


    protected void setUpTournamentLayout() {
        matchUps_ll = new LinearLayout(this);
        matchUps_ll.setOrientation(LinearLayout.HORIZONTAL);
        roundHeaders_ll = new LinearLayout(this);
        roundHeaders_ll.setOrientation(LinearLayout.HORIZONTAL);

        final LinearLayout headerAndMatchups = new LinearLayout(this);
        headerAndMatchups.setOrientation(LinearLayout.VERTICAL);


        headerAndMatchups.addView(roundHeaders_ll);
        headerAndMatchups.addView(matchUps_ll);

        tournamentLayout.addView(headerAndMatchups);
    }

    protected void buildTournamentAndDisplay() {

        if (tournament == null) {
            finish();
            return;
        }

        final List<Participant> orderedParticipantList = getIntent().getParcelableArrayListExtra(INTENT_PARTICIPANTS_KEY);

        tournament.build(orderedParticipantList, this, this, this);

        if (!tournament.isBuilt()) {

            return;
        }

        //displaying the match ups for each round
        for (int roundGroupIndex = 0; roundGroupIndex < tournament.getTotalRoundGroups(); roundGroupIndex++) {

            final ViewGroup viewGroup = getProperViewGroup(roundGroupIndex);

            for (int roundIndex = 0; roundIndex < tournament.getTotalRounds(roundGroupIndex); roundIndex++) {
                final Round round = tournament.getRoundAt(roundGroupIndex, roundIndex);

                final LinearLayout roundGroup = new LinearLayout(this);
                roundGroup.setOrientation(LinearLayout.VERTICAL);

                for (int matchUpIndex = 0; matchUpIndex < round.getTotalMatchUps(); matchUpIndex++) {
                    roundGroup.addView(buildMatchUpAndParticipantVisual(roundGroup, roundGroupIndex, roundIndex, matchUpIndex));
                }
                viewGroup.addView(roundGroup);
            }
        }

        displayRoundHeaders();

    }

    protected void displayRoundHeaders() {
        displayRoundHeaders(roundHeaders_ll, 0);
    }

    protected void displayRoundHeaders(final LinearLayout roundHeaders_ll, final int roundGroup) {
        for (int roundIndex = 0; roundIndex < tournament.getTotalRounds(roundGroup); roundIndex++) {

            final Round round = tournament.getRoundAt(roundGroup, roundIndex);
            round.setOnRoundUpdateListener(this);

            final TextView roundHeader_tv = new TextView(this);
            if (Build.VERSION.SDK_INT < 23) {
                roundHeader_tv.setTextAppearance(this, android.R.style.TextAppearance_Medium);
            } else {
                roundHeader_tv.setTextAppearance(android.R.style.TextAppearance_Medium);
            }
            final int matchUpIndicatorWidth = (int) getResources().getDimension(R.dimen.matchUpIndicator_width);
            final int textWidth = (int) getResources().getDimension(R.dimen.participant_tv_width) + matchUpIndicatorWidth;
            final int hPadding = (int) getResources().getDimension(R.dimen.participant_tv_height);
            final int textHeight = (int) getResources().getDimension(R.dimen.participant_tv_height);
            roundHeader_tv.setWidth(textWidth + hPadding + hPadding);
            roundHeader_tv.setPadding(hPadding, 0, hPadding + matchUpIndicatorWidth, 0);
            roundHeader_tv.setSingleLine();
            roundHeader_tv.setEllipsize(TextUtils.TruncateAt.END);
            roundHeader_tv.setGravity(Gravity.CENTER);
            roundHeader_tv.setHeight(textHeight);
            roundHeader_tv.setTag(getResources().getString(R.string.roundHeader, roundIndex + 1));
            roundHeader_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    openRoundOptions(round);
                    return false;
                }

            });

            roundTextViewMap.put(round, roundHeader_tv);
            round.setTitle(getResources().getString(R.string.roundHeader, roundIndex + 1));
            round.setNote("");
            round.setColor(TournamentUtil.DEFAULT_DISPLAY_COLOR);
            if (roundHeaders_ll != null) {
                roundHeaders_ll.addView(roundHeader_tv);
            }
        }
    }

    protected ViewGroup buildMatchUpAndParticipantVisual(final ViewGroup roundGroup, final int roundGroupIndex, final int roundIndex, final int matchUpIndex) {
        final MatchUp matchUp = tournament.getMatchUp(roundGroupIndex, roundIndex, matchUpIndex);

        final ViewGroup layout_matchUp = (ViewGroup) getLayoutInflater().inflate(tournament instanceof SingleMatchUpTournament ? R.layout.component_single_matchup : R.layout.component_matchup, null);
        matchUpGroupMap.put(matchUp.getKey(), layout_matchUp);

        final List<Triple<ViewGroup, TextView, TextView>> participantViewList = new ArrayList<>();

        final ViewGroup participant1Group = (ViewGroup) layout_matchUp.findViewById(tournament instanceof SingleMatchUpTournament ? R.id.participantGroup : R.id.participant1Group);
        final TextView participant1_tv = (TextView) layout_matchUp.findViewById(tournament instanceof SingleMatchUpTournament ? R.id.participant_tv : R.id.participant1_tv);
        final TextView participant1Record_tv = (TextView) layout_matchUp.findViewById(tournament instanceof SingleMatchUpTournament ? R.id.participantRecord_tv : R.id.participant1Record_tv);
        participantViewList.add(new ImmutableTriple<>(participant1Group, participant1_tv, participant1Record_tv));

        final ViewGroup participant2Group;
        if (!(tournament instanceof SingleMatchUpTournament)) {
            participant2Group = (ViewGroup) layout_matchUp.findViewById(R.id.participant2Group);
            final TextView participant2_tv = (TextView) layout_matchUp.findViewById(R.id.participant2_tv);
            final TextView participant2Record_tv = (TextView) layout_matchUp.findViewById(R.id.participant2Record_tv);
            participantViewList.add(new ImmutableTriple<>(participant2Group, participant2_tv, participant2Record_tv));
        } else {
            participant2Group = null;
        }

        updateParticipantGroupDrawables(matchUp, participant1Group, participant2Group);

        for (int participantIndex = 0; participantIndex < participantViewList.size(); participantIndex++) {
            final Triple<ViewGroup, TextView, TextView> triple = participantViewList.get(participantIndex);
            final ViewGroup participantViewGroup = triple.getLeft();
            final TextView participant_tv = triple.getMiddle();
            final TextView participantRecord_tv = triple.getRight();

            participantViewGroup.setOnClickListener(new ParticipantClickListener(tournament, roundGroupIndex, roundIndex, matchUpIndex, participantIndex));
            participantViewGroup.setOnLongClickListener(new MatchUpLongClickListener(tournament, roundGroupIndex, roundIndex, matchUpIndex));

            participantGroupMap.put(matchUp.getKey(participantIndex), participantViewGroup);

            setUpParticipantTextView(participant_tv, participantRecord_tv, tournament.getParticipant(roundGroupIndex, roundIndex, matchUpIndex, participantIndex));
        }

        final int padding = (int) getResources().getDimension(R.dimen.participant_tv_height);
        final int paddingHorizontal = padding;
        final int paddingVertical = getMatchUpVerticalPadding(roundGroupIndex, roundIndex, padding);

        layout_matchUp.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

        return layout_matchUp;
    }

    protected ViewGroup getProperViewGroup(final int roundGroupIndex) {
        return matchUps_ll;
    }

    protected void setUpParticipantTextView(final TextView participant_tv, final TextView participantRecord_tv, final Participant participant) {
        final String resolvedDisplayName = TournamentUtil.resolveParticipantDisplayName(this, participant);

        participant_tv.setText(participant.getNote() == null || participant.getNote().trim().length() == 0 ? resolvedDisplayName : "*" + resolvedDisplayName);
        participantRecord_tv.setVisibility(View.GONE);

        participant_tv.setTextColor(participant.getColor());
        participantRecord_tv.setTextColor(participant.getColor());

        if (tournament instanceof RecordKeepingTournament) {

            if (!participant.isNormal()) {
                participantRecord_tv.setVisibility(View.INVISIBLE);
            } else {
                participantRecord_tv.setText(participant.getRecordAsDisplay());
                participantRecord_tv.setVisibility(View.VISIBLE);
            }
        }

    }

    protected int getMatchUpVerticalPadding(final int roundGroupIndex, final int roundIndex, final int basePaddingInPixel) {
        return basePaddingInPixel;
    }

    protected void updateParticipantGroupDrawables(final MatchUp matchUp, final ViewGroup participant1Group, final ViewGroup participant2Group) {
        final MatchUp.MatchUpStatus matchUpStatus = matchUp.getStatus();
        final int strokeWidth = TournamentUtil.dpToPixels(this, (int) getResources().getDimension(R.dimen.participant_default_stroke_width));

        switch (matchUpStatus) {
            case P1_WINNER:
                participant1Group.setBackgroundResource(tournament instanceof SingleMatchUpTournament ? R.drawable.p_general_win : R.drawable.p1_win);
                if (!(tournament instanceof SingleMatchUpTournament)) {
                    participant2Group.setBackgroundResource(R.drawable.p2_default);
                    ((GradientDrawable) participant2Group.getBackground()).setStroke(strokeWidth, matchUp.getColor());
                }
                break;
            case P2_WINNER:
                if (!(tournament instanceof SingleMatchUpTournament)) {
                    participant1Group.setBackgroundResource(R.drawable.p1_default);
                    ((GradientDrawable) participant1Group.getBackground()).setStroke(strokeWidth, matchUp.getColor());

                    participant2Group.setBackgroundResource(R.drawable.p2_win);
                }
                break;
            case TIE:
                if (!(tournament instanceof SingleMatchUpTournament)) {
                    participant1Group.setBackgroundResource(R.drawable.p1_win);
                    participant2Group.setBackgroundResource(R.drawable.p2_win);
                }
                break;
            case DEFAULT:
            default:
                participant1Group.setBackgroundResource(tournament instanceof SingleMatchUpTournament ? R.drawable.p_general_default : R.drawable.p1_default);
                ((GradientDrawable) participant1Group.getBackground()).setStroke(strokeWidth, matchUp.getColor());
                if (!(tournament instanceof SingleMatchUpTournament)) {
                    participant2Group.setBackgroundResource(R.drawable.p2_default);
                    ((GradientDrawable) participant2Group.getBackground()).setStroke(strokeWidth, matchUp.getColor());
                }
                break;
        }

    }

    @Override
    public void onRoundUpdate(final Round round) {

        final TextView roundHeader_tv = roundTextViewMap.get(round);
        if (roundHeader_tv != null) {

            roundHeader_tv.setTextColor(round.getColor());
            final String defaultRoundHeader = roundHeader_tv.getTag().toString();
            final String resolvedTitle = round.getTitle() == null || round.getTitle().trim().length() == 0 ? defaultRoundHeader : round.getTitle();
            roundHeader_tv.setText(round.getNote() == null || round.getNote().trim().length() == 0 ? resolvedTitle : "*" + resolvedTitle);
            setUnSavedChanges(true);
        }
    }

    @Override
    public void onMatchUpUpdate(final MatchUp matchUp) {

        final List<Pair<TextView, TextView>> participantViewList = new ArrayList<>();

        final ViewGroup participant1Group = participantGroupMap.get(matchUp.getKey(0));
        if (participant1Group == null)
            return;
        final TextView participant1_tv = (TextView) participant1Group.findViewById(tournament instanceof SingleMatchUpTournament ? R.id.participant_tv : R.id.participant1_tv);
        final TextView participant1Record_tv = (TextView) participant1Group.findViewById(tournament instanceof SingleMatchUpTournament ? R.id.participantRecord_tv : R.id.participant1Record_tv);
        participantViewList.add(new Pair<>(participant1_tv, participant1Record_tv));

        final ViewGroup participant2Group;
        if (!(tournament instanceof SingleMatchUpTournament)) {
            participant2Group = participantGroupMap.get(matchUp.getKey(1));
            if (participant2Group == null)
                return;
            final TextView participant2_tv = (TextView) participant2Group.findViewById(R.id.participant2_tv);
            final TextView participant2Record_tv = (TextView) participant2Group.findViewById(R.id.participant2Record_tv);
            participantViewList.add(new Pair<>(participant2_tv, participant2Record_tv));
        } else {
            participant2Group = null;
        }

        updateParticipantGroupDrawables(matchUp, participant1Group, participant2Group);

        for (int participantIndex = 0; participantIndex < participantViewList.size(); participantIndex++) {
            final Pair<TextView, TextView> triple = participantViewList.get(participantIndex);
            final TextView participant_tv = triple.first;
            final TextView participantRecord_tv = triple.second;

            setUpParticipantTextView(participant_tv, participantRecord_tv, participantIndex == 0 ? matchUp.getParticipant1() : matchUp.getParticipant2());
        }

        final ViewGroup matchUpGroup = matchUpGroupMap.get(matchUp.getKey());
        updateMatchUpVisual(matchUpGroup, matchUp);
        setUnSavedChanges(true);
    }

    protected void updateMatchUpVisual(final ViewGroup matchUpGroup, final MatchUp matchUp) {
        final TextView matchUpIndicator_tv = (TextView) matchUpGroup.findViewById(R.id.matchUpIndicator_tv);
        matchUpIndicator_tv.setTextColor(matchUp.getColor());
        matchUpIndicator_tv.setVisibility(matchUp.getNote() == null || matchUp.getNote().trim().length() == 0 ? View.INVISIBLE : View.VISIBLE);
    }


    @Override
    public void onParticipantUpdate(final Participant participant) {
        if (participant.isNormal()) {
            final Set<ParticipantCoordinates> participantCoordinates = tournament.getParticipantCoordinates(participant);
            for (final ParticipantCoordinates participantCoordinate : participantCoordinates) {

                final ViewGroup participantGroup = participantGroupMap.get(participantCoordinate.getKey());

                if (participantGroup != null) {
                    //we don't know which participant (1 or 2) the variable "participant" is referring to, so we don't know which text views to get
                    // However, we know it has to be one of them.  So we can just check for null to eliminate.
                    // If the tournament is a SingleMatchUpTournament, then we know which text view to get.
                    TextView participant_tv = (TextView) participantGroup.findViewById(tournament instanceof SingleMatchUpTournament ? R.id.participant_tv : R.id.participant1_tv);
                    if (participant_tv == null)
                        participant_tv = (TextView) participantGroup.findViewById(R.id.participant2_tv);

                    TextView participantRecord_tv = (TextView) participantGroup.findViewById(tournament instanceof SingleMatchUpTournament ? R.id.participantRecord_tv : R.id.participant1Record_tv);
                    if (participantRecord_tv == null)
                        participantRecord_tv = (TextView) participantGroup.findViewById(R.id.participant2Record_tv);

                    setUpParticipantTextView(participant_tv, participantRecord_tv, participant);
                }
            }
            tournament.setRankStateDirty();
            setUnSavedChanges(true);
        }
    }

    protected void setValuesToRounds() {
        final List<HistoricalRound> roundList = getIntent().getParcelableArrayListExtra(INTENT_HISTORICAL_ROUNDS_KEY);
        if (roundList != null) {
            for (final HistoricalRound historicalRound : roundList) {

                final int roundGroupIndex = historicalRound.getRoundGroupIndex();
                final int roundIndex = historicalRound.getRoundIndex();

                final Round round = tournament.getRoundAt(roundGroupIndex, roundIndex);

                round.setTitle(historicalRound.getTitle());
                round.setNote(historicalRound.getNote());
                round.setColor(historicalRound.getColor());

            }
        }

    }

    protected void setValuesToMatchUps() {
        final List<HistoricalMatchUp> matchUpList = getIntent().getParcelableArrayListExtra(INTENT_HISTORICAL_MATCH_UPS_KEY);

        if (matchUpList != null) {
            for (final HistoricalMatchUp historicalMatchUp : matchUpList) {

                final int roundGroupIndex = historicalMatchUp.getRoundGroupIndex();
                final int roundIndex = historicalMatchUp.getRoundIndex();
                final int matchUpIndex = historicalMatchUp.getMatchUpIndex();

                final MatchUp matchUp = tournament.getMatchUp(roundGroupIndex, roundIndex, matchUpIndex);

                matchUp.setNote(historicalMatchUp.getNote());
                matchUp.setColor(historicalMatchUp.getColor());

                final MatchUp.MatchUpStatus matchUpStatus = historicalMatchUp.getMatchUpStatus();

                //set predetermined results
                if (matchUpStatus == MatchUp.MatchUpStatus.P1_WINNER || matchUpStatus == MatchUp.MatchUpStatus.TIE)
                    tournament.toggleResult(roundGroupIndex, roundIndex, matchUpIndex, 0);

                //This shouldn't occur if the tournament is a SingleMatchUpTournament, but check just in case
                if (!(tournament instanceof SingleMatchUpTournament) && (matchUpStatus == MatchUp.MatchUpStatus.P2_WINNER || matchUpStatus == MatchUp.MatchUpStatus.TIE))
                    tournament.toggleResult(roundGroupIndex, roundIndex, matchUpIndex, 1);


            }
        }


    }

    protected void setValuesToTournament() {
        final long creationTimeInEpoch_intent = getIntent().getLongExtra(INTENT_TOURNAMENT_CREATION_TIME_KEY, Tournament.NULL_TIME_VALUE);
        final long creationTimeInEpoch = creationTimeInEpoch_intent != Tournament.NULL_TIME_VALUE ? creationTimeInEpoch_intent : System.currentTimeMillis();
        final long lastModifiedTimeInEpoch = getIntent().getLongExtra(INTENT_TOURNAMENT_LAST_MODIFIED_TIME_KEY, Tournament.NULL_TIME_VALUE);

        final String titleFromIntent = getIntent().getStringExtra(INTENT_TOURNAMENT_TITLE_KEY);
        final String title = titleFromIntent != null && titleFromIntent.trim().length() > 0 ? titleFromIntent : TournamentUtil.createDefaultTitle(this, TournamentUtil.tournamentTypeToString(this, tournament.getType()), creationTimeInEpoch);

        final String description = getIntent().getStringExtra(INTENT_TOURNAMENT_DESCRIPTION_KEY);


        tournament.setTitle(title);
        tournament.setDescription(description);
        tournament.setCreationTimeInEpoch(creationTimeInEpoch);
        tournament.setLastModifiedTimeInEpoch(lastModifiedTimeInEpoch);

    }


    private void openTournamentEditor() {

        final ViewGroup layout_tournament_editor = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_tournament_editor, null);

        final EditText tournamentTitle_et = (EditText) layout_tournament_editor.findViewById(R.id.tournamentTitle_et);
        tournamentTitle_et.setText(tournament.getTitle());

        final EditText tournamentDescription_et = (EditText) layout_tournament_editor.findViewById(R.id.tournamentDescription_et);
        tournamentDescription_et.setText(tournament.getDescription());

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout_tournament_editor);
        builder.setTitle(getString(R.string.editTournament));
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                tournament.setTitle(tournamentTitle_et.getText().toString());
                tournament.setDescription(tournamentDescription_et.getText().toString());

            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    private void openTournamentRebuilder() {

        final ViewGroup layout_tournament_rebuilder = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_tournament_rebuilder, null);

        final TextView seedOptions_tv = (TextView) layout_tournament_rebuilder.findViewById(R.id.seedOptions_tv);
        final RadioGroup seedOptions_rg = (RadioGroup) layout_tournament_rebuilder.findViewById(R.id.seedOptions_rg);

        final RadioButton elimination_rb = (RadioButton) layout_tournament_rebuilder.findViewById(R.id.elimination_rb);
        final RadioButton doubleElimination_rb = (RadioButton) layout_tournament_rebuilder.findViewById(R.id.doubleElimination_rb);
        final RadioButton roundRobin_rb = (RadioButton) layout_tournament_rebuilder.findViewById(R.id.roundRobin_rb);
        final RadioButton swiss_rb = (RadioButton) layout_tournament_rebuilder.findViewById(R.id.swiss_rb);
        final RadioButton survival_rb = (RadioButton) layout_tournament_rebuilder.findViewById(R.id.survival_rb);

        survival_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                seedOptions_tv.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                seedOptions_rg.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            }
        });


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout_tournament_rebuilder);
        builder.setTitle(getString(R.string.editTournament));
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                final RadioButton randomSeed_rb = (RadioButton) layout_tournament_rebuilder.findViewById(R.id.randomSeed_rb);
                final RadioButton customSeed_rb = (RadioButton) layout_tournament_rebuilder.findViewById(R.id.customSeed_rb);
                final Seeder.Type seedType = randomSeed_rb.isChecked() ? Seeder.Type.RANDOM : (customSeed_rb.isChecked() ? Seeder.Type.CUSTOM : Seeder.Type.SAME);


                final Tournament.TournamentType tournamentType = elimination_rb.isChecked() ? Tournament.TournamentType.ELIMINATION
                        : doubleElimination_rb.isChecked() ? Tournament.TournamentType.DOUBLE_ELIMINATION
                        : roundRobin_rb.isChecked() ? Tournament.TournamentType.ROUND_ROBIN
                        : swiss_rb.isChecked() ? Tournament.TournamentType.SWISS
                        : Tournament.TournamentType.SURVIVAL;

                startTournamentActivity(TournamentActivity.this, 0, seedType, tournament.getType(), tournamentType, tournament.getTitle(), tournament.getDescription(), (ArrayList<Participant>) tournament.getSeededParticipants(), null, null, tournament.getCreationTimeInEpoch(), Tournament.NULL_TIME_VALUE, true);
                finish();

            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    private boolean isNewlyCreated() {
        return tournament.getLastModifiedTimeInEpoch() == Tournament.NULL_TIME_VALUE;
    }

    private void saveTournament() {

        final boolean isNewlyCreated = isNewlyCreated();
        tournament.setLastModifiedTimeInEpoch(System.currentTimeMillis());

        if (isNewlyCreated)
            getDb().addTournament(tournament);
        else
            getDb().updateTournament(tournament);

        Toast.makeText(getApplicationContext(), getString(R.string.saveTournamentSuccessfulMsg), Toast.LENGTH_LONG).show();

    }

    private void saveAsTournament() {
        tournament.setCreationTimeInEpoch(System.currentTimeMillis());
        tournament.setLastModifiedTimeInEpoch(Tournament.NULL_TIME_VALUE);
        saveTournament();

    }

    private void openCurrentRankings() {


        final Map<String, List<Ranking.RankHolder>> rankingMap = new HashMap<>();
        rankingMap.put(getString(R.string.knownRanking), tournament.getCurrentRanking().getKnownRankings());
        rankingMap.put(getString(R.string.unknownRanking), tournament.getCurrentRanking().getUnknownRankings());

        final RankingAdapter rankingAdapter = new RankingAdapter(this, Arrays.asList(getString(R.string.knownRanking), getString(R.string.unknownRanking)), rankingMap);
        final ExpandableListView ranking_elv = new ExpandableListView(this);

        ranking_elv.setAdapter(rankingAdapter);
        ranking_elv.expandGroup(0);
        ranking_elv.expandGroup(1);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(ranking_elv);
        builder.setTitle(getString(R.string.currentRanking));
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {

            }
        });
        builder.create().show();
    }

    private void openRoundList() {
        final List<Pair<String, Round>> roundList = new ArrayList<>();
        for (int roundGroupIndex = 0; roundGroupIndex < tournament.getTotalRoundGroups(); roundGroupIndex++) {

            for (int roundIndex = 0; roundIndex < tournament.getTotalRounds(roundGroupIndex); roundIndex++) {
                final Round round = tournament.getRoundAt(roundGroupIndex, roundIndex);
                final String displayTitle = getFullRoundTitle(round, roundGroupIndex);
                roundList.add(new Pair<>(displayTitle, round));
            }
        }
        final String[] roundNames = new String[roundList.size()];
        for (int i = 0; i < roundList.size(); i++) {
            roundNames[i] = roundList.get(i).first;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.roundSelectionHint));
        builder.setItems(roundNames, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int itemIndex) {
                openRoundOptions(roundList.get(itemIndex).second);

            }
        });
        builder.create().show();
    }

    protected String getFullRoundTitle(final Round round, final int roundGroupIndex) {
        return round.getTitle();
    }

    private void openMatchUpList() {
        final List<Pair<String, MatchUp>> matchUpList = new ArrayList<>();
        for (int roundGroupIndex = 0; roundGroupIndex < tournament.getTotalRoundGroups(); roundGroupIndex++) {

            for (int roundIndex = 0; roundIndex < tournament.getTotalRounds(roundGroupIndex); roundIndex++) {
                for (int matchUpIndex = 0; matchUpIndex < tournament.getTotalMatchUps(roundGroupIndex, roundIndex); matchUpIndex++) {
                    final MatchUp matchUp = tournament.getMatchUp(roundGroupIndex, roundIndex, matchUpIndex);
                    final String displayTitle = getFullMatchUpTitle(roundGroupIndex, roundIndex, matchUpIndex);
                    matchUpList.add(new Pair<>(displayTitle, matchUp));
                }
            }
        }
        final String[] matchUpNames = new String[matchUpList.size()];
        for (int i = 0; i < matchUpList.size(); i++) {
            matchUpNames[i] = matchUpList.get(i).first;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.matchUpSelectionHint));
        builder.setItems(matchUpNames, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int itemIndex) {
                openMatchUpOptions(matchUpList.get(itemIndex).second);

            }
        });
        builder.create().show();
    }

    protected String getFullMatchUpTitle(final int roundGroupIndex, final int roundIndex, final int matchUpIndex) {
        final Round round = tournament.getRoundAt(roundGroupIndex, roundIndex);
        final MatchUp matchUp = tournament.getMatchUp(roundGroupIndex, roundIndex, matchUpIndex);
        final Participant participant1 = matchUp.getParticipant1();
        final Participant participant2 = matchUp.getParticipant2();


        if (participant1.isNormal() && participant2.isNormal())
            return getString(R.string.participant1VsParticipant2, participant1.getDisplayName(), participant2.getDisplayName(), round.getTitle());
        else if (participant1.isNormal() && participant2.isBye())
            return getString(R.string.participantWithABye, participant1.getDisplayName(), round.getTitle());
        else if (participant1.isBye() && participant2.isNormal())
            return getString(R.string.participantWithABye, participant2.getDisplayName(), round.getTitle());
        else if (participant1.isNormal() && participant2.isNull())
            return getString(R.string.participantVsNoOne, participant1.getDisplayName(), round.getTitle());
        else if (participant1.isNull() && participant2.isNormal())
            return getString(R.string.participantVsNoOne, participant2.getDisplayName(), round.getTitle());
        return getString(R.string.emptyMatchUp, matchUpIndex + 1, round.getTitle());
    }

    private void openParticipantList() {
        final List<Participant> participantList = tournament.getNormalSortedParticipants();
        final String[] participantNames = new String[participantList.size()];

        for (int i = 0; i < participantList.size(); i++) {
            participantNames[i] = participantList.get(i).getDisplayName();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.participantSelectionHint));
        builder.setItems(participantNames, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int itemIndex) {
                openParticipantOptions(participantList.get(itemIndex));

            }
        });
        builder.create().show();
    }

    private ColorAdapter colorAdapter;
    private Map<String, Integer> colorMap;

    private ColorAdapter getColorAdapter() {
        if (colorAdapter == null || colorMap == null) {

            final String[] colorNames = getResources().getStringArray(R.array.colorOptionsNames);
            final int[] colors = getResources().getIntArray(R.array.colorOptions);

            colorMap = new HashMap<>();
            for (int i = 0; i < colorNames.length; i++) {
                colorMap.put(colorNames[i], colors[i]);
            }
            colorAdapter = new ColorAdapter(this, R.layout.color_item, colorNames, colorMap);
        }
        return colorAdapter;
    }

    private int getIndexOfColorArray(int color) {
        final int[] colors = getResources().getIntArray(R.array.colorOptions);
        for (int c = 0; c < colors.length; c++) {
            if (colors[c] == color)
                return c;
        }
        return 0;

    }


    private void openParticipantOptions(final Participant participant) {

        if (participant == null)
            return;

        final ViewGroup layout_participant_editor = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_participant_editor, null);

        final EditText participantDisplayName_et = (EditText) layout_participant_editor.findViewById(R.id.participantDisplayName_et);
        participantDisplayName_et.setText(participant.getDisplayName());
        participantDisplayName_et.setHint(getString(R.string.editPlayDisplayNameHint, participant.getName()));


        final EditText participantNote_et = (EditText) layout_participant_editor.findViewById(R.id.participantNote_et);
        participantNote_et.setText(participant.getNote());

        final Spinner participantColor_spinner = (Spinner) layout_participant_editor.findViewById(R.id.participantColor_spinner);
        participantColor_spinner.setAdapter(getColorAdapter());
        participantColor_spinner.setSelection(getIndexOfColorArray(participant.getColor()));

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout_participant_editor);
        builder.setTitle(getString(R.string.editing, participant.getDisplayName()));
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                participant.setDisplayName(participantDisplayName_et.getText().toString());
                participant.setNote(participantNote_et.getText().toString());
                participant.setColor(colorMap.get(participantColor_spinner.getSelectedItem().toString()));
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();

    }

    private void openRoundOptions(final Round round) {

        final ViewGroup layout_round_editor = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_round_editor, null);

        final EditText roundTitle_et = (EditText) layout_round_editor.findViewById(R.id.roundTitle_et);
        roundTitle_et.setText(round.getTitle());

        final EditText roundNote_et = (EditText) layout_round_editor.findViewById(R.id.roundNote_et);
        roundNote_et.setText(round.getNote());

        final Spinner roundColor_spinner = (Spinner) layout_round_editor.findViewById(R.id.roundColor_spinner);
        roundColor_spinner.setAdapter(getColorAdapter());
        roundColor_spinner.setSelection(getIndexOfColorArray(round.getColor()));

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout_round_editor);
        builder.setTitle(getString(R.string.editingThisRound));
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                round.setTitle(roundTitle_et.getText().toString());
                round.setNote(roundNote_et.getText().toString());
                round.setColor(colorMap.get(roundColor_spinner.getSelectedItem().toString()));
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();

    }

    private void openMatchUpOptions(final MatchUp matchUp) {

        final ViewGroup layout_matchup_editor = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_matchup_editor, null);

        final EditText matchUpNote_et = (EditText) layout_matchup_editor.findViewById(R.id.matchUpNote_et);
        matchUpNote_et.setText(matchUp.getNote());

        final Spinner matchUpColor_spinner = (Spinner) layout_matchup_editor.findViewById(R.id.matchUpColor_spinner);
        matchUpColor_spinner.setAdapter(getColorAdapter());
        matchUpColor_spinner.setSelection(getIndexOfColorArray(matchUp.getColor()));

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout_matchup_editor);
        builder.setTitle(getString(R.string.editingThisMatchUp));
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                matchUp.setNote(matchUpNote_et.getText().toString());
                matchUp.setColor(colorMap.get(matchUpColor_spinner.getSelectedItem().toString()));
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();

    }

    private void choosePathToExportTournament() {

        //Create default folder
        final String appFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name);
        File folder = new File(appFolder);
        if (!folder.exists()) {
            folder.mkdir();
        }

        Intent intent = new Intent(this, DirectoryPickerActivity.class);
        startActivityForResult(intent, DirectoryPickerActivity.DIRECTORY_PICKER_DEFAULT_REQUEST_CODE);
    }

    private void exportTournament(final String path) {
        new ExportTournamentTask(this, tournamentView_root, tournament.getTitle(), path).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DirectoryPickerActivity.DIRECTORY_PICKER_DEFAULT_REQUEST_CODE && resultCode == RESULT_OK) {
            final String path = (String) data.getExtras().get(DirectoryPickerActivity.CHOSEN_DIRECTORY);
            exportTournament(path);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        this.menu = menu;


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions_tournament, menu);

        setUnSavedChanges(unSavedChanges);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                handleActivityFinish();
                return true;
            case R.id.action_saveTournament:
                saveTournament();
                return true;
            case R.id.action_currentRanking:
                openCurrentRankings();
                return true;
            case R.id.action_saveAsTournament:
                saveAsTournament();
                return true;
            case R.id.action_exportTournament:
                choosePathToExportTournament();
                return true;
            case R.id.action_editTournament:
                openTournamentEditor();
                return true;
            case R.id.action_rebuildTournament:
                openTournamentRebuilder();
                return true;
            case R.id.action_editARound:
                openRoundList();
                return true;
            case R.id.action_editAMatchUp:
                openMatchUpList();
                return true;
            case R.id.action_editAParticipant:
                openParticipantList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTournamentTitleChange(String title) {
        setTitle(title);
        setUnSavedChanges(true);
    }

    @Override
    public void onTournamentDescriptionChange(String description) {

        description_tv.setVisibility(description == null || description.trim().length() == 0 ? View.GONE : View.VISIBLE);
        description_tv.setText(TournamentUtil.shortenDescription(description, DESCRIPTION_MAX_CHARACTERS_PER_LINE, DESCRIPTION_TOTAL_MAX_CHARACTERS));
        setUnSavedChanges(true);

    }

    @Override
    public void onTournamentCreationTimeInEpochChange(long epoch) {
        setUnSavedChanges(false);
    }

    @Override
    public void onTournamentLastModifiedTimeInEpochChange(long epoch) {
        setUnSavedChanges(false);
    }

    public static void startTournamentActivity(final Activity startingActivity, final int requestCode, final Seeder.Type seedType, final HistoricalTournament historicalTournament) {
        startTournamentActivity(
                startingActivity,
                requestCode,
                seedType,
                historicalTournament.getType(),
                historicalTournament.getType(),
                historicalTournament.getName(),
                historicalTournament.getNote(),
                (ArrayList<Participant>) historicalTournament.getParticipantList(),
                (ArrayList<HistoricalRound>) historicalTournament.getRoundList(),
                (ArrayList<HistoricalMatchUp>) historicalTournament.getMatchUpList(),
                historicalTournament.getCreationTimeInEpoch(),
                historicalTournament.getLastModifiedTimeInEpoch(),
                false
        );
    }

    private void setUnSavedChanges(final boolean value) {
        unSavedChanges = value;
        if (menu != null) {
            final MenuItem menuItem = menu.findItem(R.id.action_saveTournament);
            if (menuItem != null)
                menuItem.setIcon(ResourcesCompat.getDrawable(getResources(), unSavedChanges ? R.drawable.ic_save_warning : R.drawable.ic_save, null));
        }
    }

    @Override
    public void onBackPressed() {
        handleActivityFinish();
    }

    private void handleActivityFinish() {

        if (unSavedChanges) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.unSavedChangesWarningMsg));
            builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    saveTournament();
                    finish();
                }
            });
            builder.setNegativeButton(getString(R.string.dontSave), new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    finish();
                }
            });
            builder.setNeutralButton(getString(android.R.string.cancel), null);
            builder.create().show();
        } else {
            finish();
        }


    }

    private static Seeder.SeedFillType getSeedFileType(final Tournament.TournamentType tournamentType) {

        switch (tournamentType) {
            case ELIMINATION:
            case DOUBLE_ELIMINATION:
                return Seeder.SeedFillType.POWER_OF_2;
            case SWISS:
            case ROUND_ROBIN:
                return Seeder.SeedFillType.EVEN;
            case SURVIVAL:
            default:
                return Seeder.SeedFillType.ALWAYS;
        }
    }

    public static void startTournamentActivity(final Activity startingActivity, final int requestCode, final Seeder.Type seedType, final Tournament.TournamentType tournamentType_original, final Tournament.TournamentType tournamentType, final String title, final String description,
                                               final ArrayList<Participant> participantList, final ArrayList<HistoricalRound> roundList, final ArrayList<HistoricalMatchUp> matchUpList, final long creationTimeInEpoch, final long lastModifiedTimeInEpoch, final boolean rebuilt) {

        final Class clazz;
        final Seeder.SeedFillType seedFillType_original = getSeedFileType(tournamentType_original);
        final Seeder.SeedFillType seedFillType = getSeedFileType(tournamentType);

        switch (tournamentType) {
            case ELIMINATION:
                clazz = EliminationTournamentActivity.class;
                break;
            case DOUBLE_ELIMINATION:
                clazz = DoubleEliminationTournamentActivity.class;
                break;
            case ROUND_ROBIN:
                clazz = RoundRobinTournamentActivity.class;
                break;
            case SWISS:
                clazz = SwissTournamentActivity.class;
                break;
            case SURVIVAL:
            default:
                clazz = SurvivalTournamentActivity.class;
                break;
        }

        final Seeder seeder = new Seeder(participantList, seedFillType);
        if (seedFillType == Seeder.SeedFillType.ALWAYS || seedFillType != seedFillType_original)
            seeder.clean();
        if (tournamentType == Tournament.TournamentType.SURVIVAL) {
            seeder.sort();
        } else if (seedType == Seeder.Type.RANDOM) {
            seeder.clean();
            seeder.randomize();
        }
        seeder.seed();


        final Intent intent = new Intent(startingActivity, seedType == Seeder.Type.RANDOM || seedType == Seeder.Type.SAME || tournamentType == Tournament.TournamentType.SURVIVAL ? clazz : SeedingActivity.class);
        intent.putExtra(TournamentActivity.INTENT_TOURNAMENT_TITLE_KEY, title);
        intent.putExtra(TournamentActivity.INTENT_TOURNAMENT_DESCRIPTION_KEY, description);
        intent.putParcelableArrayListExtra(TournamentActivity.INTENT_HISTORICAL_ROUNDS_KEY, roundList);
        intent.putParcelableArrayListExtra(TournamentActivity.INTENT_HISTORICAL_MATCH_UPS_KEY, matchUpList);
        intent.putParcelableArrayListExtra(TournamentActivity.INTENT_PARTICIPANTS_KEY, participantList);
        intent.putExtra(TournamentActivity.INTENT_TOURNAMENT_CREATION_TIME_KEY, creationTimeInEpoch);
        intent.putExtra(TournamentActivity.INTENT_TOURNAMENT_LAST_MODIFIED_TIME_KEY, lastModifiedTimeInEpoch);
        intent.putExtra(TournamentActivity.INTENT_TOURNAMENT_IS_REBUILT_KEY, rebuilt);
        if (seedType == Seeder.Type.CUSTOM && tournamentType != Tournament.TournamentType.SURVIVAL)
            intent.putExtra(INTENT_TOURNAMENT_TYPE_KEY, tournamentType);
        startingActivity.startActivityForResult(intent, requestCode);
    }

    protected class ParticipantClickListener implements View.OnClickListener {

        private Tournament tournament;
        private int roundGroupIndex;
        private int roundIndex;
        private int matchUpIndex;
        private int participantIndex;

        protected ParticipantClickListener(final Tournament tournament, final int roundGroupIndex, final int roundIndex, final int matchUpIndex, final int participantIndex) {
            this.tournament = tournament;
            this.roundGroupIndex = roundGroupIndex;
            this.roundIndex = roundIndex;
            this.matchUpIndex = matchUpIndex;
            this.participantIndex = participantIndex;
        }

        @Override
        public void onClick(View v) {

            tournament.toggleResult(roundGroupIndex, roundIndex, matchUpIndex, participantIndex);
        }

    }


    protected class MatchUpLongClickListener implements View.OnLongClickListener {


        private Tournament tournament;
        private int roundGroupIndex;
        private int roundIndex;
        private int matchUpIndex;


        protected MatchUpLongClickListener(final Tournament tournament, final int roundGroupIndex, final int roundIndex, final int matchUpIndex) {
            this.tournament = tournament;
            this.roundGroupIndex = roundGroupIndex;
            this.roundIndex = roundIndex;
            this.matchUpIndex = matchUpIndex;
        }


        @Override
        public boolean onLongClick(final View v) {

            final Participant participant1 = tournament.getParticipant(roundGroupIndex, roundIndex, matchUpIndex, 0);
            final Participant participant2 = tournament.getParticipant(roundGroupIndex, roundIndex, matchUpIndex, 1);
            final MatchUp matchUp = tournament.getMatchUp(roundGroupIndex, roundIndex, matchUpIndex);

            final Map<Integer, Object> indexToObjectMap = new HashMap<>();

            final List<String> optionsList = new ArrayList<>();
            if (!participant1.isBye() && !participant1.isNull()) {
                optionsList.add(participant1.getDisplayName());
            }
            if (!participant2.isBye() && !participant2.isNull()) {
                optionsList.add(participant2.getDisplayName());
            }
            if (optionsList.size() == 0) {
                openMatchUpOptions(matchUp);
            } else {
                optionsList.add(getString(R.string.thisMatchUp));

                final String[] options_arr = optionsList.toArray(new String[optionsList.size()]);
                for (int i = 0; i < options_arr.length; i++) {
                    indexToObjectMap.put(i, options_arr[i]);
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(getString(R.string.edit));

                builder.setItems(options_arr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int itemIndex) {
                        if (itemIndex == options_arr.length - 1) {
                            openMatchUpOptions(matchUp);
                        } else if (options_arr.length == 3) {
                            if (itemIndex == 0) {
                                openParticipantOptions(participant1);
                            } else {
                                openParticipantOptions(participant2);
                            }
                        } else if (options_arr.length == 2) {
                            if (!participant1.isBye() && !participant1.isNull()) {
                                openParticipantOptions(participant1);
                            } else {
                                openParticipantOptions(participant2);

                            }
                        }

                    }
                });

                builder.create().show();
            }
            return false;
        }
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
    }

}
