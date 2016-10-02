package com.dgnt.quickTournamentMaker.util;

import android.app.Activity;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dgnt.quickTournamentMaker.R;
import com.dgnt.quickTournamentMaker.activity.MainActivity;
import com.dgnt.quickTournamentMaker.model.tournament.RecordKeepingTournamentTrait;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Owner on 10/1/2016.
 */

public class LayoutUtil {

    public static void setUpSeedingEditor(final Context context, final View rootView){
        final TextView seedOptions_tv = (TextView) rootView.findViewById(R.id.seedOptions_tv);
        final RadioGroup seedOptions_rg = (RadioGroup) rootView.findViewById(R.id.seedOptions_rg);

        final TextView rankingConfig_tv = (TextView) rootView.findViewById(R.id.rankingConfig_tv);
        final RadioGroup rankingConfig_rg = (RadioGroup) rootView.findViewById(R.id.rankingConfig_rg);

        rankingConfig_tv.setVisibility(View.GONE);
        rankingConfig_rg.setVisibility(View.GONE);

        final RadioGroup tournamentType_rg = (RadioGroup) rootView.findViewById(R.id.tournamentType_rg);

        tournamentType_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                final RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);

                final boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked) {
                    if (checkedId == R.id.roundRobin_rb || checkedId == R.id.swiss_rb) {
                        rankingConfig_tv.setVisibility(View.VISIBLE);
                        rankingConfig_rg.setVisibility(View.VISIBLE);
                        LayoutUtil.setUpRankingEditor(context, rootView,checkedId == R.id.swiss_rb);
                    } else {
                        rankingConfig_tv.setVisibility(View.GONE);
                        rankingConfig_rg.setVisibility(View.GONE);
                    }


                    seedOptions_tv.setVisibility(checkedId == R.id.survival_rb ? View.GONE : View.VISIBLE);
                    seedOptions_rg.setVisibility(checkedId == R.id.survival_rb ? View.GONE : View.VISIBLE);
                }

            }
        });
    }

    private static void setUpRankingEditor(final Context context, final View rootView,final boolean isSwiss) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

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
        final String prefKey_rankPriority = isSwiss ? PreferenceUtil.PREF_SWISS_RANK_PRIORITY_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANK_PRIORITY_KEY;
        final RecordKeepingTournamentTrait.RankingFromPriority rankingFromPriority = PreferenceUtil.getRankPriority(sharedPreferences, prefKey_rankPriority);
        final TextView priority1_tv = (TextView) rootView.findViewById(R.id.priority1_tv);
        setUpRankingEditor_Priority(context, rootView,sharedPreferences, prefKey_rankPriority, rankingFromPriority.getFirstPriority(), priority1_tv);
        final TextView priority2_tv = (TextView) rootView.findViewById(R.id.priority2_tv);
        setUpRankingEditor_Priority(context,rootView, sharedPreferences, prefKey_rankPriority, rankingFromPriority.getSecondPriority(), priority2_tv);
        final TextView priority3_tv = (TextView) rootView.findViewById(R.id.priority3_tv);
        setUpRankingEditor_Priority(context,rootView, sharedPreferences, prefKey_rankPriority, rankingFromPriority.getThirdPriority(), priority3_tv);

        final Button winScoreAdd_btn = (Button) rootView.findViewById(R.id.winScoreAdd_btn);
        final EditText winScore_et = (EditText) rootView.findViewById(R.id.winScore_et);
        final Button winScoreSubtract_btn = (Button) rootView.findViewById(R.id.winScoreSubtract_btn);
        setUpRankingEditor_Score(context, "w", isSwiss ? PreferenceUtil.PREF_SWISS_RANK_WIN_SCORE_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANK_WIN_SCORE_KEY, winScoreAdd_btn, winScore_et, winScoreSubtract_btn);

        final Button tieScoreAdd_btn = (Button) rootView.findViewById(R.id.tieScoreAdd_btn);
        final EditText tieScore_et = (EditText) rootView.findViewById(R.id.tieScore_et);
        final Button tieScoreSubtract_btn = (Button) rootView.findViewById(R.id.tieScoreSubtract_btn);
        setUpRankingEditor_Score(context, "t", isSwiss ? PreferenceUtil.PREF_SWISS_RANK_TIE_SCORE_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANK_TIE_SCORE_KEY, tieScoreAdd_btn, tieScore_et, tieScoreSubtract_btn);

        final Button lossScoreAdd_btn = (Button) rootView.findViewById(R.id.lossScoreAdd_btn);
        final EditText lossScore_et = (EditText) rootView.findViewById(R.id.lossScore_et);
        final Button lossScoreSubtract_btn = (Button) rootView.findViewById(R.id.lossScoreSubtract_btn);
        setUpRankingEditor_Score(context, "l", isSwiss ? PreferenceUtil.PREF_SWISS_RANK_LOSS_SCORE_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANK_LOSS_SCORE_KEY, lossScoreAdd_btn, lossScore_et, lossScoreSubtract_btn);

    }

    private static TextView prioritySelectedTextView;

    private static void setUpRankingEditor_Priority(final Context context, final View rootView, final SharedPreferences sharedPreferences, final String prefKey, final RecordKeepingTournamentTrait.RankPriorityType rankPriorityType, final TextView priority_tv) {

        priority_tv.setText(getProperPriorityName(context,rankPriorityType));
        priority_tv.setTag(rankPriorityType);
        priority_tv.setTextColor(ContextCompat.getColor(context,R.color.defaultStroke));
        priority_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prioritySelectedTextView == null) {
                    priority_tv.setTextColor(ContextCompat.getColor(context,R.color.winStroke));
                    prioritySelectedTextView = priority_tv;
                } else {

                    final RecordKeepingTournamentTrait.RankPriorityType rankPriorityType_selected = (RecordKeepingTournamentTrait.RankPriorityType) prioritySelectedTextView.getTag();
                    final RecordKeepingTournamentTrait.RankPriorityType rankPriorityType_new = (RecordKeepingTournamentTrait.RankPriorityType) priority_tv.getTag();

                    priority_tv.setText(getProperPriorityName(context,rankPriorityType_selected));
                    priority_tv.setTag(rankPriorityType_selected);

                    prioritySelectedTextView.setText(getProperPriorityName(context,rankPriorityType_new));
                    prioritySelectedTextView.setTag(rankPriorityType_new);
                    prioritySelectedTextView.setTextColor(ContextCompat.getColor(context,R.color.defaultStroke));

                    prioritySelectedTextView = null;
                }

                final TextView priority1_tv = (TextView) rootView.findViewById(R.id.priority1_tv);
                final RecordKeepingTournamentTrait.RankPriorityType rankPriorityType1 = (RecordKeepingTournamentTrait.RankPriorityType) priority1_tv.getTag();
                final TextView priority2_tv = (TextView) rootView.findViewById(R.id.priority2_tv);
                final RecordKeepingTournamentTrait.RankPriorityType rankPriorityType2 = (RecordKeepingTournamentTrait.RankPriorityType) priority2_tv.getTag();
                final TextView priority3_tv = (TextView) rootView.findViewById(R.id.priority3_tv);
                final RecordKeepingTournamentTrait.RankPriorityType rankPriorityType3 = (RecordKeepingTournamentTrait.RankPriorityType) priority3_tv.getTag();

                PreferenceUtil.setRankPriority(sharedPreferences, prefKey, rankPriorityType1,rankPriorityType2,rankPriorityType3);

            }
        });
    }

    private static String getProperPriorityName(final Context context, final RecordKeepingTournamentTrait.RankPriorityType rankPriorityType){
        switch (rankPriorityType) {
            case WIN:
                return context.getString(R.string.win);
            case LOSS:
                return context.getString(R.string.loss);
            case TIE:
            default:
                return context.getString(R.string.tie);
        }
    }

    private static void setUpRankingEditor_Score(final Context context, final String type, final String prefKey, final Button add_btn, final EditText score_et, final Button subtract_btn) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (!myTextWatcherMap.containsKey(type))
            myTextWatcherMap.put(type, new MyTextWatcher());
        final MyTextWatcher myTextWatcher = myTextWatcherMap.get(type);

        score_et.removeTextChangedListener(myTextWatcher);
        score_et.setText(String.valueOf(PreferenceUtil.getRankScore(sharedPreferences, prefKey)));
        myTextWatcher.setSharedPreferences(sharedPreferences);
        myTextWatcher.setPrefKey(prefKey);
        score_et.addTextChangedListener(myTextWatcher);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score_et.setText(String.valueOf(PreferenceUtil.getRankScore(sharedPreferences, prefKey) + 1));
            }
        });

        subtract_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score_et.setText(String.valueOf(PreferenceUtil.getRankScore(sharedPreferences, prefKey) - 1));
            }
        });
    }

    final private static Map<String, MyTextWatcher> myTextWatcherMap = new HashMap<>();

    private static class MyTextWatcher implements TextWatcher {


        private SharedPreferences sharedPreferences;

        private void setSharedPreferences(final SharedPreferences sharedPreferences) {
            this.sharedPreferences = sharedPreferences;
        }

        private String prefKey;

        private void setPrefKey(final String prefKey) {
            this.prefKey = prefKey;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (sharedPreferences != null && prefKey != null)
                PreferenceUtil.setRankScore(sharedPreferences, prefKey, s.toString());
        }
    }
}
