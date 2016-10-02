package com.dgnt.quickTournamentMaker.util;

import android.content.SharedPreferences;

import com.dgnt.quickTournamentMaker.model.history.HistoricalTournament;
import com.dgnt.quickTournamentMaker.model.tournament.Tournament;

import org.apache.commons.lang3.StringUtils;

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

    public enum RankPriorityType {
        WIN, TIE, LOSS
    }

    public static RankPriorityType[] getRankPriority(final SharedPreferences sharedPreferences, final String prefKey) {
        final String[] priority = sharedPreferences.getString(prefKey, "w;l;t").split(";");
        final RankPriorityType[] rankPriorityTypes = new RankPriorityType[priority.length];
        for (int i = 0; i < priority.length; i++) {
            final String s = priority[i];
            if (s.equals("w"))
                rankPriorityTypes[i] = RankPriorityType.WIN;
            else if (s.equals("l"))
                rankPriorityTypes[i] = RankPriorityType.LOSS;
            else //"t"
                rankPriorityTypes[i] = RankPriorityType.TIE;
        }
        return rankPriorityTypes;
    }

    public static void setRankPriority(final SharedPreferences sharedPreferences, final String prefKey, final RankPriorityType[] rankPriorityTypes) {
        final StringBuilder stringBuilder = new StringBuilder();
        String sep = "";
        for (final RankPriorityType rankPriorityType : rankPriorityTypes) {

            stringBuilder.append(sep);
            switch (rankPriorityType) {
                case WIN:
                    stringBuilder.append("w");
                    break;
                case LOSS:
                    stringBuilder.append("l");
                    break;
                case TIE:
                default:
                    stringBuilder.append("t");
            }
            sep = ";";
        }

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(prefKey, stringBuilder.toString());
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

    public static void setRankScore(final SharedPreferences sharedPreferences, final String prefKey, final String winScore) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(prefKey, getIntFromString(winScore, 0));
        editor.commit();
    }

    private static int getIntFromString(final String s, final int defaultNum) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {

        }
        return defaultNum;
    }
}
