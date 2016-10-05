package com.dgnt.quickTournamentMaker.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dgnt.quickTournamentMaker.R;
import com.dgnt.quickTournamentMaker.model.history.HistoricalTournament;
import com.dgnt.quickTournamentMaker.model.tournament.Participant;
import com.dgnt.quickTournamentMaker.model.tournament.RecordKeepingTournamentTrait;
import com.dgnt.quickTournamentMaker.model.tournament.Tournament;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Owner on 10/1/2016.
 */

public class LayoutUtil {

    public static void setUpSeedingEditor(final Context context, final View rootView) {
        final TextView seedOptions_tv = (TextView) rootView.findViewById(R.id.seedOptions_tv);
        final RadioGroup seedOptions_rg = (RadioGroup) rootView.findViewById(R.id.seedOptions_rg);

        final ViewGroup rankingConfig_group = (ViewGroup) rootView.findViewById(R.id.rankingConfig_group);
        final RadioGroup rankingConfig_rg = (RadioGroup) rootView.findViewById(R.id.rankingConfig_rg);

        rankingConfig_group.setVisibility(View.GONE);
        rankingConfig_rg.setVisibility(View.GONE);


        final RadioGroup tournamentType_rg = (RadioGroup) rootView.findViewById(R.id.tournamentType_rg);

        tournamentType_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                final RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);

                final boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked) {
                    if (checkedId == R.id.roundRobin_rb || checkedId == R.id.swiss_rb) {
                        rankingConfig_group.setVisibility(View.VISIBLE);
                        rankingConfig_rg.setVisibility(View.VISIBLE);
                        LayoutUtil.setUpRankingEditor(context, rootView, checkedId == R.id.swiss_rb);
                    } else {
                        rankingConfig_group.setVisibility(View.GONE);
                        rankingConfig_rg.setVisibility(View.GONE);
                    }


                    seedOptions_tv.setVisibility(checkedId == R.id.survival_rb ? View.GONE : View.VISIBLE);
                    seedOptions_rg.setVisibility(checkedId == R.id.survival_rb ? View.GONE : View.VISIBLE);
                }

            }
        });
    }

    private static void setUpRankingEditor(final Context context, final View rootView, final boolean isSwiss) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        final ImageView rankingConfigHelp_iv = (ImageView) rootView.findViewById(R.id.rankingConfigHelp_iv);
        rankingConfigHelp_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getString(R.string.rankConfigurationHelpMsg, context.getString(isSwiss ? R.string.rankConfigurationForSwissHelpMsg : R.string.rankConfigurationForRoundRobinHelpMsg)));
                builder.setPositiveButton(context.getString(android.R.string.ok), null);
                builder.create().show();
            }
        });

        final RadioGroup rankingConfig_rg = (RadioGroup) rootView.findViewById(R.id.rankingConfig_rg);

        final RadioButton compareRankFromPriority_rb = (RadioButton) rootView.findViewById(R.id.compareRankFromPriority_rb);
        final RadioButton compareRankFromScore_rb = (RadioButton) rootView.findViewById(R.id.compareRankFromScore_rb);

        final ViewGroup compareRankFromPriorityGroup = (ViewGroup) rootView.findViewById(R.id.compareRankFromPriorityGroup);
        final ViewGroup compareRankFromScoreGroup = (ViewGroup) rootView.findViewById(R.id.compareRankFromScoreGroup);
        rankingConfig_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                final RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);

                final boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked) {
                    if (checkedId == R.id.compareRankFromPriority_rb) {
                        compareRankFromPriorityGroup.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                        compareRankFromScoreGroup.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                        PreferenceUtil.setRankingBasedOnPriority(sharedPreferences, isSwiss ? PreferenceUtil.PREF_SWISS_RANKING_CONFIG_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANKING_CONFIG_KEY, true);

                    } else {
                        compareRankFromPriorityGroup.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                        compareRankFromScoreGroup.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                        PreferenceUtil.setRankingBasedOnPriority(sharedPreferences, isSwiss ? PreferenceUtil.PREF_SWISS_RANKING_CONFIG_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANKING_CONFIG_KEY, false);

                    }

                }

            }
        });

        final boolean isRankingBasedOnPriority = PreferenceUtil.isRankingBasedOnPriority(sharedPreferences, isSwiss ? PreferenceUtil.PREF_SWISS_RANKING_CONFIG_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANKING_CONFIG_KEY);
        if (isRankingBasedOnPriority) {
            compareRankFromPriority_rb.setChecked(true);
            compareRankFromScore_rb.setChecked(false);
        } else {
            compareRankFromPriority_rb.setChecked(false);
            compareRankFromScore_rb.setChecked(true);
        }

        prioritySelectedTextView = null;
        final RecordKeepingTournamentTrait.RankingFromPriority rankingFromPriority = PreferenceUtil.getRankPriority(sharedPreferences, isSwiss);

        final TextView priority1_tv = (TextView) rootView.findViewById(R.id.priority1_tv);
        setUpRankingEditor_Priority(context, rootView, sharedPreferences, isSwiss, rankingFromPriority.getFirstPriority(), priority1_tv);

        final TextView priority2_tv = (TextView) rootView.findViewById(R.id.priority2_tv);
        setUpRankingEditor_Priority(context, rootView, sharedPreferences, isSwiss, rankingFromPriority.getSecondPriority(), priority2_tv);

        final TextView priority3_tv = (TextView) rootView.findViewById(R.id.priority3_tv);
        setUpRankingEditor_Priority(context, rootView, sharedPreferences, isSwiss, rankingFromPriority.getThirdPriority(), priority3_tv);


        final RecordKeepingTournamentTrait.RankingFromScore rankingFromScore = PreferenceUtil.getRankScore(sharedPreferences, isSwiss);

        final Button winScoreAdd_btn = (Button) rootView.findViewById(R.id.winScoreAdd_btn);
        final EditText winScore_et = (EditText) rootView.findViewById(R.id.winScore_et);
        final Button winScoreSubtract_btn = (Button) rootView.findViewById(R.id.winScoreSubtract_btn);
        setUpRankingEditor_Score(sharedPreferences, rootView, RecordKeepingTournamentTrait.RecordType.WIN, isSwiss, rankingFromScore.getWinScore(), winScoreAdd_btn, winScore_et, winScoreSubtract_btn);

        final Button lossScoreAdd_btn = (Button) rootView.findViewById(R.id.lossScoreAdd_btn);
        final EditText lossScore_et = (EditText) rootView.findViewById(R.id.lossScore_et);
        final Button lossScoreSubtract_btn = (Button) rootView.findViewById(R.id.lossScoreSubtract_btn);
        setUpRankingEditor_Score(sharedPreferences, rootView, RecordKeepingTournamentTrait.RecordType.LOSS, isSwiss, rankingFromScore.getLossScore(), lossScoreAdd_btn, lossScore_et, lossScoreSubtract_btn);

        final Button tieScoreAdd_btn = (Button) rootView.findViewById(R.id.tieScoreAdd_btn);
        final EditText tieScore_et = (EditText) rootView.findViewById(R.id.tieScore_et);
        final Button tieScoreSubtract_btn = (Button) rootView.findViewById(R.id.tieScoreSubtract_btn);
        setUpRankingEditor_Score(sharedPreferences, rootView, RecordKeepingTournamentTrait.RecordType.TIE, isSwiss, rankingFromScore.getTieScore(), tieScoreAdd_btn, tieScore_et, tieScoreSubtract_btn);

    }

    private static TextView prioritySelectedTextView;

    private static void setUpRankingEditor_Priority(final Context context, final View rootView, final SharedPreferences sharedPreferences, final boolean isSwiss, final RecordKeepingTournamentTrait.RecordType recordType, final TextView priority_tv) {

        priority_tv.setText(getProperPriorityName(context, recordType));
        priority_tv.setTag(recordType);
        priority_tv.setTextColor(ContextCompat.getColor(context, R.color.defaultStroke));
        priority_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prioritySelectedTextView == null) {
                    priority_tv.setTextColor(ContextCompat.getColor(context, R.color.winStroke));
                    prioritySelectedTextView = priority_tv;
                } else {

                    final RecordKeepingTournamentTrait.RecordType recordType_selected = (RecordKeepingTournamentTrait.RecordType) prioritySelectedTextView.getTag();
                    final RecordKeepingTournamentTrait.RecordType recordType_new = (RecordKeepingTournamentTrait.RecordType) priority_tv.getTag();

                    priority_tv.setText(getProperPriorityName(context, recordType_selected));
                    priority_tv.setTag(recordType_selected);

                    prioritySelectedTextView.setText(getProperPriorityName(context, recordType_new));
                    prioritySelectedTextView.setTag(recordType_new);
                    prioritySelectedTextView.setTextColor(ContextCompat.getColor(context, R.color.defaultStroke));

                    prioritySelectedTextView = null;
                }

                final TextView priority1_tv = (TextView) rootView.findViewById(R.id.priority1_tv);
                final RecordKeepingTournamentTrait.RecordType recordType1 = (RecordKeepingTournamentTrait.RecordType) priority1_tv.getTag();
                final TextView priority2_tv = (TextView) rootView.findViewById(R.id.priority2_tv);
                final RecordKeepingTournamentTrait.RecordType recordType2 = (RecordKeepingTournamentTrait.RecordType) priority2_tv.getTag();
                final TextView priority3_tv = (TextView) rootView.findViewById(R.id.priority3_tv);
                final RecordKeepingTournamentTrait.RecordType recordType3 = (RecordKeepingTournamentTrait.RecordType) priority3_tv.getTag();

                PreferenceUtil.setRankPriority(sharedPreferences, isSwiss, recordType1, recordType2, recordType3);

            }
        });
    }

    private static String getProperPriorityName(final Context context, final RecordKeepingTournamentTrait.RecordType recordType) {
        switch (recordType) {
            case WIN:
                return context.getString(R.string.win);
            case LOSS:
                return context.getString(R.string.loss);
            case TIE:
            default:
                return context.getString(R.string.tie);
        }
    }

    private static void setUpRankingEditor_Score(final SharedPreferences sharedPreferences, final View rootView, final RecordKeepingTournamentTrait.RecordType recordType, final boolean isSwiss, final int score, final Button add_btn, final EditText score_et, final Button subtract_btn) {

        if (!myTextWatcherMap.containsKey(recordType))
            myTextWatcherMap.put(recordType, new MyTextWatcher());
        final MyTextWatcher myTextWatcher = myTextWatcherMap.get(recordType);

        score_et.removeTextChangedListener(myTextWatcher);
        score_et.setText(String.valueOf(score));
        myTextWatcher.setSharedPreferences(sharedPreferences);
        myTextWatcher.setSwiss(isSwiss);
        myTextWatcher.setRootView(rootView);
        score_et.addTextChangedListener(myTextWatcher);

        add_btn.setTag(1);
        subtract_btn.setTag(-1);

        final View.OnClickListener myOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int increment = (Integer) v.getTag();

                final RecordKeepingTournamentTrait.RankingFromScore rankingFromScore = PreferenceUtil.getRankScore(sharedPreferences, isSwiss);
                final int scoreToUse;
                switch (recordType) {
                    case WIN:
                        scoreToUse = rankingFromScore.getWinScore();
                        break;
                    case LOSS:
                        scoreToUse = rankingFromScore.getLossScore();
                        break;
                    case TIE:
                    default:
                        scoreToUse = rankingFromScore.getTieScore();
                }
                score_et.setText(String.valueOf(scoreToUse + increment));
            }
        };

        add_btn.setOnClickListener(myOnClickListener);
        subtract_btn.setOnClickListener(myOnClickListener);
    }

    final private static Map<RecordKeepingTournamentTrait.RecordType, MyTextWatcher> myTextWatcherMap = new HashMap<>();


    private static class MyTextWatcher implements TextWatcher {


        private SharedPreferences sharedPreferences;

        private void setSharedPreferences(final SharedPreferences sharedPreferences) {
            this.sharedPreferences = sharedPreferences;
        }

        private boolean isSwiss;

        private void setSwiss(final boolean swiss) {
            this.isSwiss = swiss;
        }

        private View rootView;

        private void setRootView(final View rootView) {
            this.rootView = rootView;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (sharedPreferences != null && rootView != null) {
                final TextView winScore_et = (TextView) rootView.findViewById(R.id.winScore_et);
                final int winScore = winScore_et.getText().toString().trim().equals("") ? 0 : Integer.parseInt(winScore_et.getText().toString());
                final TextView lossScore_et = (TextView) rootView.findViewById(R.id.lossScore_et);
                final int lossScore = lossScore_et.getText().toString().trim().equals("") ? 0 : Integer.parseInt(lossScore_et.getText().toString());
                final TextView tieScore_et = (TextView) rootView.findViewById(R.id.tieScore_et);
                final int tieScore = tieScore_et.getText().toString().trim().equals("") ? 0 : Integer.parseInt(tieScore_et.getText().toString());
                PreferenceUtil.setRankScore(sharedPreferences, isSwiss, winScore, lossScore, tieScore);
            }
        }
    }


    public static void openTournamentInfo(final Activity activity, final HistoricalTournament historicalTournament) {
        final ViewGroup layout_tournament_info = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.layout_tournament_info, null);

        final TextView tournamentTitle_tv = (TextView) layout_tournament_info.findViewById(R.id.tournamentTitle_tv);
        tournamentTitle_tv.setText(historicalTournament.getName());

        final TextView tournamentDescription_tv = (TextView) layout_tournament_info.findViewById(R.id.tournamentDescription_tv);
        tournamentDescription_tv.setText(historicalTournament.getNote());

        final TextView tournamentParticipants_tv = (TextView) layout_tournament_info.findViewById(R.id.tournamentParticipants_tv);
        tournamentParticipants_tv.setText(getNiceParticipantList(historicalTournament.getNormalSortedParticipantList()));

        final TextView tournamentType_tv = (TextView) layout_tournament_info.findViewById(R.id.tournamentType_tv);
        tournamentType_tv.setText(TournamentUtil.tournamentTypeToString(activity, historicalTournament.getType()));

        final TextView rankingConfigHeader_tv = (TextView) layout_tournament_info.findViewById(R.id.rankingConfigHeader_tv);
        final TextView rankingConfig_tv = (TextView) layout_tournament_info.findViewById(R.id.rankingConfig_tv);
        if (!historicalTournament.isRecordKeepingTournament()) {
            rankingConfigHeader_tv.setVisibility(View.GONE);
            rankingConfig_tv.setVisibility(View.GONE);
        } else {
            rankingConfigHeader_tv.setVisibility(View.VISIBLE);
            rankingConfig_tv.setVisibility(View.VISIBLE);

            final Pair<RecordKeepingTournamentTrait.RankingFromPriority, RecordKeepingTournamentTrait.RankingFromScore> pair = TournamentUtil.getRankingConfigObjects(historicalTournament.getRankingConfig());
            final RecordKeepingTournamentTrait.RankingFromPriority rankingFromPriority = pair.getLeft();
            final RecordKeepingTournamentTrait.RankingFromScore rankingFromScore = pair.getRight();

            if (rankingFromPriority != null) {
                rankingConfigHeader_tv.setText(activity.getString(R.string.rankConfigurationBasedOnPriority));
                final String[] priorityArr = new String[3];
                for (int i = 0; i < 3; i++) {
                    final RecordKeepingTournamentTrait.RecordType recordType;
                    if (i == 0)
                        recordType = rankingFromPriority.getFirstPriority();
                    else if (i == 1)
                        recordType = rankingFromPriority.getSecondPriority();
                    else
                        recordType = rankingFromPriority.getThirdPriority();

                    priorityArr[i] = getProperPriorityName(activity, recordType);

                }
                rankingConfig_tv.setText(activity.getString(R.string.priorityInfo, priorityArr[0], priorityArr[1], priorityArr[2]));
            } else if (rankingFromScore != null) {

                rankingConfigHeader_tv.setText(activity.getString(R.string.rankConfigurationBasedOnScoring));
                rankingConfig_tv.setText(activity.getString(R.string.scoringInfo, activity.getString(R.string.win), rankingFromScore.getWinScore()
                        , activity.getString(R.string.loss), rankingFromScore.getLossScore()
                        , activity.getString(R.string.tie), rankingFromScore.getTieScore()));
            } else {
                rankingConfigHeader_tv.setVisibility(View.GONE);
                rankingConfig_tv.setVisibility(View.GONE);
            }

        }

        final TextView tournamentCreationDate_tv = (TextView) layout_tournament_info.findViewById(R.id.tournamentCreationDate_tv);
        tournamentCreationDate_tv.setText(historicalTournament.getCreationTimeInEpoch() == Tournament.NULL_TIME_VALUE ? activity.getString(R.string.na) : historicalTournament.getCreationTimeAsDate());

        final TextView tournamentLastModifiedDate_tv = (TextView) layout_tournament_info.findViewById(R.id.tournamentLastModifiedDate_tv);
        tournamentLastModifiedDate_tv.setText(historicalTournament.getLastModifiedTimeInEpoch() == Tournament.NULL_TIME_VALUE ? activity.getString(R.string.na) : historicalTournament.getLastModifiedTimeAsDate());


        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(layout_tournament_info);
        builder.setTitle(activity.getString(R.string.tournamentInfo));
        builder.setPositiveButton(activity.getString(android.R.string.ok), null);
        builder.create().show();
    }

    private static String getNiceParticipantList(final List<Participant> participantList) {
        final StringBuilder sb = new StringBuilder();
        String newLine = "";
        for (final Participant participant : participantList) {
            sb.append(newLine).append(participant.getDisplayName());
            newLine = "\n";
        }

        return sb.toString();
    }
}
