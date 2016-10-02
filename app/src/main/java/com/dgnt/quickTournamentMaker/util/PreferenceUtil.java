package com.dgnt.quickTournamentMaker.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dgnt.quickTournamentMaker.model.history.HistoricalTournament;
import com.dgnt.quickTournamentMaker.model.tournament.RecordKeepingTournament;
import com.dgnt.quickTournamentMaker.model.tournament.RecordKeepingTournamentTrait;
import com.dgnt.quickTournamentMaker.model.tournament.Tournament;

/**
 * Created by Owner on 7/18/2016.
 */
public class PreferenceUtil {

    public final static String PREF_HISTORICAL_TOURNAMENT_SORT_KEY = "historicalTournamentSortKey";

    public static HistoricalTournament.Sort getHistoricalTournamentSort(final SharedPreferences sharedPreferences) {
        final HistoricalTournament.Sort defaultSort = HistoricalTournament.Sort.CREATION_DATE_NEWEST;

        try {
            final String sort_string = sharedPreferences.getString(PREF_HISTORICAL_TOURNAMENT_SORT_KEY, defaultSort.name());
            final HistoricalTournament.Sort sort = HistoricalTournament.Sort.valueOf(sort_string);
            return sort;
        } catch (Exception e) {
            return defaultSort;
        }
    }

    public final static String PREF_HISTORICAL_TOURNAMENT_VIEW_KEY = "historicalTournamentViewKey";

    public static HistoricalTournament.View getHistoricalTournamentViewMode(final SharedPreferences sharedPreferences) {
        final HistoricalTournament.View defaultView = HistoricalTournament.View.BASIC;

        try {
            final String view_string = sharedPreferences.getString(PREF_HISTORICAL_TOURNAMENT_VIEW_KEY, defaultView.name());
            final HistoricalTournament.View view = HistoricalTournament.View.valueOf(view_string);
            return view;
        } catch (Exception e) {
            return defaultView;
        }
    }

    private final static String PREF_FILTER_TOURNAMENT_TYPE_KEY = "filterTournamentTypeKey";

    public static String getTournamentTypeFilterKey(final Tournament.TournamentType type) {
        return PREF_FILTER_TOURNAMENT_TYPE_KEY + type.name();
    }

    public static boolean isTournamentTypeFilteredOn(final SharedPreferences sharedPreferences, final Tournament.TournamentType type) {
        return sharedPreferences.getBoolean(getTournamentTypeFilterKey(type), false);
    }

    public final static String PREF_FILTER_MIN_PARTICIPANT_KEY = "filterMinimumParticipantKey";

    public static int getMinParticipantFilter(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getInt(PREF_FILTER_MIN_PARTICIPANT_KEY, 0);
    }

    public final static String PREF_FILTER_MIN_PARTICIPANT_ALLOWED_KEY = "filterMinimumParticipantAllowedKey";

    public static boolean isMinParticipantFilterAllowed(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(PREF_FILTER_MIN_PARTICIPANT_ALLOWED_KEY, false);
    }

    public final static String PREF_FILTER_MAX_PARTICIPANT_KEY = "filterMaximumParticipantKey";

    public static int getMaxParticipantFilter(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getInt(PREF_FILTER_MAX_PARTICIPANT_KEY, 0);
    }

    public final static String PREF_FILTER_MAX_PARTICIPANT_ALLOWED_KEY = "filterMaximumParticipantAllowedKey";

    public static boolean isMaxParticipantFilterAllowed(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(PREF_FILTER_MAX_PARTICIPANT_ALLOWED_KEY, false);
    }

    public final static String PREF_FILTER_EARLIEST_CREATED_EPOCH_KEY = "filterEarliestCreatedEpochKey";

    public static long getEarliestCreatedEpochFilter(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getLong(PREF_FILTER_EARLIEST_CREATED_EPOCH_KEY, -1);
    }

    public final static String PREF_FILTER_EARLIEST_CREATED_EPOCH_ALLOWED_KEY = "filterEarliestCreatedEpochAllowedKey";

    public static boolean isEarliestCreatedEpochFilterAllowed(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(PREF_FILTER_EARLIEST_CREATED_EPOCH_ALLOWED_KEY, false);
    }

    public final static String PREF_FILTER_LATEST_CREATED_EPOCH_KEY = "filterLatestCreatedEpochKey";

    public static long getLatestCreatedEpochFilter(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getLong(PREF_FILTER_LATEST_CREATED_EPOCH_KEY, -1);
    }

    public final static String PREF_FILTER_LATEST_CREATED_EPOCH_ALLOWED_KEY = "filterLatestCreatedEpochAllowedKey";

    public static boolean isLatestCreatedEpochFilterAllowed(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(PREF_FILTER_LATEST_CREATED_EPOCH_ALLOWED_KEY, false);
    }

    public final static String PREF_FILTER_EARLIEST_MODIFIED_EPOCH_KEY = "filterEarliestModifiedEpochKey";

    public static long getEarliestModifiedEpochFilter(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getLong(PREF_FILTER_EARLIEST_MODIFIED_EPOCH_KEY, -1);
    }

    public final static String PREF_FILTER_EARLIEST_MODIFIED_EPOCH_ALLOWED_KEY = "filterEarliestModifiedEpochAllowedKey";

    public static boolean isEarliestModifiedEpochFilterAllowed(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(PREF_FILTER_EARLIEST_MODIFIED_EPOCH_ALLOWED_KEY, false);
    }

    public final static String PREF_FILTER_LATEST_MODIFIED_EPOCH_KEY = "filterLatestModifiedEpochKey";

    public static long getLatestModifiedEpochFilter(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getLong(PREF_FILTER_LATEST_MODIFIED_EPOCH_KEY, -1);
    }

    public final static String PREF_FILTER_LATEST_MODIFIED_EPOCH_ALLOWED_KEY = "filterLatestModifiedEpochAllowedKey";

    public static boolean isLatestModifiedEpochFilterAllowed(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(PREF_FILTER_LATEST_MODIFIED_EPOCH_ALLOWED_KEY, false);
    }

    public final static String PREF_SWISS_RANKING_CONFIG_KEY = "rankSwissRankingConfigKey";
    public final static String PREF_ROUND_ROBIN_RANKING_CONFIG_KEY = "rankRoundRobinRankingConfigKey";

    public static boolean isRankingBasedOnPriority(final SharedPreferences sharedPreferences, final String prefKey) {
        return sharedPreferences.getBoolean(prefKey, true);
    }

    public static void setRankingBasedOnPriority(final SharedPreferences sharedPreferences, final String prefKey, final boolean value) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(prefKey, value);
        editor.commit();
    }

    public final static String PREF_SWISS_RANK_PRIORITY_KEY = "rankSwissPriorityKey";
    public final static String PREF_ROUND_ROBIN_RANK_PRIORITY_KEY = "rankRoundRobinPriorityKey";


    public static RecordKeepingTournamentTrait.RankingFromPriority getRankPriority(final SharedPreferences sharedPreferences, final String prefKey) {
        return RecordKeepingTournamentTrait.buildRankingFromPriority(sharedPreferences.getString(prefKey, RecordKeepingTournamentTrait.RankingFromPriority.DEFAULT_INPUT));
    }

    public static void setRankPriority(final SharedPreferences sharedPreferences, final String prefKey, final RecordKeepingTournamentTrait.RankPriorityType firstPriority, final RecordKeepingTournamentTrait.RankPriorityType secondPriority, final RecordKeepingTournamentTrait.RankPriorityType thirdPriority) {

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(prefKey, RecordKeepingTournamentTrait.getRankingFromPriorityAsString(firstPriority,secondPriority,thirdPriority));
        editor.commit();
    }

    public final static String PREF_SWISS_RANK_WIN_SCORE_KEY = "rankSwissWinScoreKey";
    public final static String PREF_SWISS_RANK_LOSS_SCORE_KEY = "rankSwissLossScoreKey";
    public final static String PREF_SWISS_RANK_TIE_SCORE_KEY = "rankSwissTieScoreKey";

    public final static String PREF_ROUND_ROBIN_RANK_WIN_SCORE_KEY = "rankRoundRobinWinScoreKey";
    public final static String PREF_ROUND_ROBIN_RANK_LOSS_SCORE_KEY = "rankRoundRobinLossScoreKey";
    public final static String PREF_ROUND_ROBIN_RANK_TIE_SCORE_KEY = "rankRoundRobinTieScoreKey";

    public static int getRankScore(final SharedPreferences sharedPreferences, final String prefKey) {
        return sharedPreferences.getInt(prefKey, 0);
    }

    public static void setRankScore(final SharedPreferences sharedPreferences, final String prefKey, final String score) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(prefKey, getIntFromString(score, 0));
        editor.commit();
    }

    private static int getIntFromString(final String s, final int defaultNum) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {

        }
        return defaultNum;
    }

    public static void setRankingConfig(final SharedPreferences sharedPreferences, final RecordKeepingTournament recordKeepingTournament, final boolean isSwiss){
        final boolean isRankingBasedOnPriority = PreferenceUtil.isRankingBasedOnPriority(sharedPreferences, isSwiss ? PreferenceUtil.PREF_SWISS_RANKING_CONFIG_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANKING_CONFIG_KEY);
        if (isRankingBasedOnPriority)
            recordKeepingTournament.setRankingConfigFromPriority(PreferenceUtil.getRankPriority(sharedPreferences, isSwiss ? PreferenceUtil.PREF_SWISS_RANK_PRIORITY_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANK_PRIORITY_KEY));
        else {
            final int winScore = PreferenceUtil.getRankScore(sharedPreferences, isSwiss ? PreferenceUtil.PREF_SWISS_RANK_WIN_SCORE_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANK_WIN_SCORE_KEY);
            final int lossScore = PreferenceUtil.getRankScore(sharedPreferences, isSwiss ? PreferenceUtil.PREF_SWISS_RANK_LOSS_SCORE_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANK_LOSS_SCORE_KEY);
            final int tieScore = PreferenceUtil.getRankScore(sharedPreferences, isSwiss ? PreferenceUtil.PREF_SWISS_RANK_TIE_SCORE_KEY : PreferenceUtil.PREF_ROUND_ROBIN_RANK_TIE_SCORE_KEY);
            final RecordKeepingTournamentTrait.RankingFromScore rankingFromScore = RecordKeepingTournamentTrait.buildRankingFromScore(winScore, lossScore, tieScore);

            recordKeepingTournament.setRankingConfigFromScore(rankingFromScore);
        }
    }
}
