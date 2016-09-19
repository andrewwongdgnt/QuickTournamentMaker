package com.dgnt.quickTournamentMaker.model.history;

import android.content.SharedPreferences;

import com.dgnt.quickTournamentMaker.model.tournament.Tournament;
import com.dgnt.quickTournamentMaker.util.PreferenceUtil;

import org.joda.time.DateTime;

/**
 * Created by Owner on 7/22/2016.
 */
public class HistoricalFilters {

    private boolean filterOnElimination;
    private boolean filterOnDoubleElimination;
    private boolean filterOnRoundRobin;
    private boolean filterOnSwiss;
    private boolean filterOnSurvival;

    private int minParticipants;
    private int maxParticipants;
    private long earliestCreatedEpoch;
    private long latestCreatedEpoch;
    private long earliestModifiedEpoch;
    private long latestModifiedEpoch;
    public HistoricalFilters(final boolean filterOnElimination, final boolean filterOnDoubleElimination, final boolean filterOnRoundRobin, final boolean filterOnSwiss, final boolean filterOnSurvival,
                             final int minParticipants, final int maxParticipants, final long earliestCreatedEpoch, final long latestCreatedEpoch, final long earliestModifiedEpoch, final long latestModifiedEpoch){

        this.filterOnElimination = filterOnElimination;
        this.filterOnDoubleElimination = filterOnDoubleElimination;
        this.filterOnRoundRobin = filterOnRoundRobin;
        this.filterOnSwiss = filterOnSwiss;
        this.filterOnSurvival = filterOnSurvival;

        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.earliestCreatedEpoch = earliestCreatedEpoch;
        this.latestCreatedEpoch = latestCreatedEpoch;
        this.earliestModifiedEpoch = earliestModifiedEpoch;
        this.latestModifiedEpoch = latestModifiedEpoch;

    }

    public boolean isFilterOnElimination() {
        return filterOnElimination;
    }

    public boolean isFilterOnDoubleElimination() {
        return filterOnDoubleElimination;
    }

    public boolean isFilterOnRoundRobin() {
        return filterOnRoundRobin;
    }

    public boolean isFilterOnSwiss() {
        return filterOnSwiss;
    }

    public boolean isFilterOnSurvival() {
        return filterOnSurvival;
    }

    public int getMinParticipants() {
        return minParticipants;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public long getEarliestCreatedEpoch() {
        return earliestCreatedEpoch;
    }

    public long getLatestCreatedEpoch() {
        return latestCreatedEpoch;
    }

    public long getEarliestModifiedEpoch() {
        return earliestModifiedEpoch;
    }

    public long getLatestModifiedEpoch() {
        return latestModifiedEpoch;
    }

    public static HistoricalFilters build(final SharedPreferences sharedPreferences){
        final boolean filterOnElimination = PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.ELIMINATION);
        final boolean filterOnDoubleElimination = PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.DOUBLE_ELIMINATION);
        final boolean filterOnRoundRobin = PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.ROUND_ROBIN);
        final boolean filterOnSwiss = PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.SWISS);
        final boolean filterOnSurvival = PreferenceUtil.isTournamentTypeFilteredOn(sharedPreferences, Tournament.TournamentType.SURVIVAL);

        final int minParticipants = PreferenceUtil.isMinParticipantFilterAllowed(sharedPreferences) ? PreferenceUtil.getMinParticipantFilter(sharedPreferences) : -1;
        final int maxParticipants = PreferenceUtil.isMaxParticipantFilterAllowed(sharedPreferences) ? PreferenceUtil.getMaxParticipantFilter(sharedPreferences) : -1;

        final long earliestCreatedEpoch = PreferenceUtil.isEarliestCreatedEpochFilterAllowed(sharedPreferences) ? PreferenceUtil.getEarliestCreatedEpochFilter(sharedPreferences) : -1;
        long latestCreatedEpoch = PreferenceUtil.isLatestCreatedEpochFilterAllowed(sharedPreferences) ? PreferenceUtil.getLatestCreatedEpochFilter(sharedPreferences) : -1;

        if (latestCreatedEpoch>=0) {
            final DateTime dt = new DateTime(latestCreatedEpoch);
            latestCreatedEpoch =  dt.plusDays(1).getMillis();
        }

        final long earliestModifiedEpoch = PreferenceUtil.isEarliestModifiedEpochFilterAllowed(sharedPreferences) ? PreferenceUtil.getEarliestModifiedEpochFilter(sharedPreferences) : -1;
        long latestModifiedEpoch = PreferenceUtil.isLatestModifiedEpochFilterAllowed(sharedPreferences) ? PreferenceUtil.getLatestModifiedEpochFilter(sharedPreferences) : -1;

        if (latestModifiedEpoch>=0) {
            final DateTime dt = new DateTime(latestModifiedEpoch);
            latestModifiedEpoch =  dt.plusDays(1).getMillis();
        }

        return new HistoricalFilters(filterOnElimination,filterOnDoubleElimination,filterOnRoundRobin,filterOnSwiss,filterOnSurvival,minParticipants,maxParticipants,earliestCreatedEpoch,latestCreatedEpoch,earliestModifiedEpoch,latestModifiedEpoch);
    }
}
