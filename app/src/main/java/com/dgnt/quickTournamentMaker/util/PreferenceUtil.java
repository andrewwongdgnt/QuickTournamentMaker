package com.dgnt.quickTournamentMaker.util;

import android.content.SharedPreferences;

import com.dgnt.quickTournamentMaker.model.history.HistoricalTournament;
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

    public static String getTournamentTypeFilterKey(final Tournament.TournamentType type){
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
}
