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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Owner on 10/1/2016.
 */

public class LayoutUtil {


    public static void setUpRankingEditor(final Activity activity, final boolean isSwiss) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);

        final RadioGroup rankingConfig_rg = (RadioGroup) activity.findViewById(R.id.rankingConfig_rg);

        final RadioButton compareRankFromPriority_rb = (RadioButton) activity.findViewById(R.id.compareRankFromPriority_rb);
        final RadioButton compareRankFromScore_rb = (RadioButton) activity.findViewById(R.id.compareRankFromScore_rb);

        final ViewGroup compareRankFromPriorityGroup = (ViewGroup) activity.findViewById(R.id.compareRankFromPriorityGroup);
        final ViewGroup compareRankFromScoreGroup = (ViewGroup) activity.findViewById(R.id.compareRankFromScoreGroup);
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
        final PreferenceUtil.RankPriorityType[] rankPriorityTypes = PreferenceUtil.getRankPriority(sharedPreferences, prefKey_rankPriority);
        final TextView priority1_tv = (TextView) activity.findViewById(R.id.priority1_tv);
        setUpRankingEditor_Priority(activity, sharedPreferences, prefKey_rankPriority, rankPriorityTypes[0], priority1_tv);
        final TextView priority2_tv = (TextView) activity.findViewById(R.id.priority2_tv);
        setUpRankingEditor_Priority(activity, sharedPreferences, prefKey_rankPriority, rankPriorityTypes[1], priority2_tv);
        final TextView priority3_tv = (TextView) activity.findViewById(R.id.priority3_tv);
        setUpRankingEditor_Priority(activity, sharedPreferences, prefKey_rankPriority, rankPriorityTypes[2], priority3_tv);

        final Button winScoreAdd_btn = (Button) activity.findViewById(R.id.winScoreAdd_btn);
        final EditText winScore_et = (EditText) activity.findViewById(R.id.winScore_et);
        final Button winScoreSubtract_btn = (Button) activity.findViewById(R.id.winScoreSubtract_btn);
        setUpRankingEditor_Score(activity, "w", isSwiss ? PreferenceUtil.PREF_SWISS_RANK_WIN_SCORE_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANK_WIN_SCORE_KEY, winScoreAdd_btn, winScore_et, winScoreSubtract_btn);

        final Button tieScoreAdd_btn = (Button) activity.findViewById(R.id.tieScoreAdd_btn);
        final EditText tieScore_et = (EditText) activity.findViewById(R.id.tieScore_et);
        final Button tieScoreSubtract_btn = (Button) activity.findViewById(R.id.tieScoreSubtract_btn);
        setUpRankingEditor_Score(activity, "t", isSwiss ? PreferenceUtil.PREF_SWISS_RANK_TIE_SCORE_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANK_TIE_SCORE_KEY, tieScoreAdd_btn, tieScore_et, tieScoreSubtract_btn);

        final Button lossScoreAdd_btn = (Button) activity.findViewById(R.id.lossScoreAdd_btn);
        final EditText lossScore_et = (EditText) activity.findViewById(R.id.lossScore_et);
        final Button lossScoreSubtract_btn = (Button) activity.findViewById(R.id.lossScoreSubtract_btn);
        setUpRankingEditor_Score(activity, "l", isSwiss ? PreferenceUtil.PREF_SWISS_RANK_LOSS_SCORE_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANK_LOSS_SCORE_KEY, lossScoreAdd_btn, lossScore_et, lossScoreSubtract_btn);

    }

    private static TextView prioritySelectedTextView;

    private static void setUpRankingEditor_Priority(final Activity activity, final SharedPreferences sharedPreferences, final String prefKey, final PreferenceUtil.RankPriorityType rankPriorityType, final TextView priority_tv) {

        priority_tv.setText(getProperPriorityName(activity,rankPriorityType));
        priority_tv.setTag(rankPriorityType);
        priority_tv.setTextColor(ContextCompat.getColor(activity,R.color.defaultStroke));
        priority_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prioritySelectedTextView == null) {
                    priority_tv.setTextColor(ContextCompat.getColor(activity,R.color.winStroke));
                    prioritySelectedTextView = priority_tv;
                } else {

                    final PreferenceUtil.RankPriorityType rankPriorityType_selected = (PreferenceUtil.RankPriorityType) prioritySelectedTextView.getTag();
                    final PreferenceUtil.RankPriorityType rankPriorityType_new = (PreferenceUtil.RankPriorityType) priority_tv.getTag();

                    priority_tv.setText(getProperPriorityName(activity,rankPriorityType_selected));
                    priority_tv.setTag(rankPriorityType_selected);

                    prioritySelectedTextView.setText(getProperPriorityName(activity,rankPriorityType_new));
                    prioritySelectedTextView.setTag(rankPriorityType_new);
                    prioritySelectedTextView.setTextColor(ContextCompat.getColor(activity,R.color.defaultStroke));

                    prioritySelectedTextView = null;
                }

                final TextView priority1_tv = (TextView) activity.findViewById(R.id.priority1_tv);
                final PreferenceUtil.RankPriorityType rankPriorityType1 = (PreferenceUtil.RankPriorityType) priority1_tv.getTag();
                final TextView priority2_tv = (TextView) activity.findViewById(R.id.priority2_tv);
                final PreferenceUtil.RankPriorityType rankPriorityType2 = (PreferenceUtil.RankPriorityType) priority2_tv.getTag();
                final TextView priority3_tv = (TextView) activity.findViewById(R.id.priority3_tv);
                final PreferenceUtil.RankPriorityType rankPriorityType3 = (PreferenceUtil.RankPriorityType) priority3_tv.getTag();

                PreferenceUtil.setRankPriority(sharedPreferences, prefKey, new PreferenceUtil.RankPriorityType[]{rankPriorityType1, rankPriorityType2, rankPriorityType3});

            }
        });
    }

    private static String getProperPriorityName(final Context context, final PreferenceUtil.RankPriorityType rankPriorityType){
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
