package com.dgnt.quickTournamentMaker.util;

import android.content.Context;

import com.dgnt.quickTournamentMaker.R;
import com.dgnt.quickTournamentMaker.model.history.HistoricalMatchUp;
import com.dgnt.quickTournamentMaker.model.history.HistoricalRound;
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp;
import com.dgnt.quickTournamentMaker.model.tournament.Participant;
import com.dgnt.quickTournamentMaker.model.tournament.RecordKeepingTournament;
import com.dgnt.quickTournamentMaker.model.tournament.RecordKeepingTournamentTrait;
import com.dgnt.quickTournamentMaker.model.tournament.Round;
import com.dgnt.quickTournamentMaker.model.tournament.Tournament;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by Owner on 3/13/2016.
 */
public class TournamentUtil {

    public static boolean isPowerOf2(final int candidate) {
        return candidate > 0 && (candidate & (candidate - 1)) == 0;
    }

    public static int nextPowerOf2(final int candidate) {
        int n = candidate;
        n--;
        n |= n >> 1;   // Divide by 2^k for consecutive doublings of k up to 32,
        n |= n >> 2;   // and then or the results.
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        n++;           // The result is a number of 1 bits equal to the number
        // of bits in the original number, plus 1. That's the
        // next highest power of 2.

        return n;
    }

    public static int dpToPixels(final Context context, final int sizeInDp) {
        float scale = context.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp * scale + 0.5f);
        return dpAsPixels;
    }

    public static String shortenDescription(final String description, final int maxCharactersPerLine, final int maxTotalCharacters) {
        if (description == null)
            return description;

        final int resolvedMaxCharactersPerLine = maxCharactersPerLine > maxTotalCharacters ? maxTotalCharacters : maxCharactersPerLine;

        StringBuilder sb = new StringBuilder(description);

        int i = 0;
        while ((i = sb.indexOf(" ", i + resolvedMaxCharactersPerLine)) != -1) {
            sb.replace(i, i + 1, "\n");
        }

        if (sb.toString().length() > maxTotalCharacters) {
            // "\u2026" is the unicode for ellipses
            return sb.toString().substring(0, maxTotalCharacters) + "\u2026";
        }
        return sb.toString();
    }

    public static String createDefaultTitle(final Context context, final String tournamentType, final long CreationTimeInEpoch) {
        return context.getResources().getString(R.string.defaultTitle, tournamentType, epochToDate(CreationTimeInEpoch));

    }

    public static String epochToDate(final long epoch) {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(epoch);
    }

    public static List<HistoricalRound> buildRoundList(final Tournament tournament) {
        final List<HistoricalRound> roundList = new ArrayList<>();
        for (int roundGroupIndex = 0; roundGroupIndex < tournament.getTotalRoundGroups(); roundGroupIndex++) {
            for (int roundIndex = 0; roundIndex < tournament.getTotalRounds(roundGroupIndex); roundIndex++) {
                final Round round = tournament.getRoundAt(roundGroupIndex, roundIndex);
                roundList.add(new HistoricalRound(roundGroupIndex, roundIndex, round.getTitle(), round.getNote(), round.getColor()));

            }
        }

        return roundList;
    }

    public static List<HistoricalMatchUp> buildMatchUpList(final Tournament tournament) {
        final List<HistoricalMatchUp> matchUpList = new ArrayList<>();
        for (int roundGroupIndex = 0; roundGroupIndex < tournament.getTotalRoundGroups(); roundGroupIndex++) {
            for (int roundIndex = 0; roundIndex < tournament.getTotalRounds(roundGroupIndex); roundIndex++) {
                for (int matchUpIndex = 0; matchUpIndex < tournament.getTotalMatchUps(roundGroupIndex, roundIndex); matchUpIndex++) {
                    final MatchUp matchUp = tournament.getMatchUp(roundGroupIndex, roundIndex, matchUpIndex);
                    matchUpList.add(new HistoricalMatchUp(roundGroupIndex, roundIndex, matchUpIndex, matchUp.getNote(), matchUp.getColor(), matchUp.getStatus()));
                }
            }
        }

        return matchUpList;
    }

    public static int getNormalParticipantCount(final List<Participant> participantList) {
        int count = 0;
        for (final Participant participant : participantList) {
            if (participant.isNormal())
                count++;
        }
        return count;
    }

    public static String tournamentTypeToString(final Context context, final Tournament.TournamentType tournamentType) {
        switch (tournamentType) {
            case ELIMINATION:
                return context.getString(R.string.elimination);
            case DOUBLE_ELIMINATION:
                return context.getString(R.string.doubleElimination);
            case ROUND_ROBIN:
                return context.getString(R.string.roundRobin);
            case SWISS:
                return context.getString(R.string.swiss);
            case SURVIVAL:
            default:
                return context.getString(R.string.survival);
        }
    }

    public static final int DEFAULT_DISPLAY_COLOR = 0xff000000;

    public static String resolveParticipantDisplayName(final Context context, final Participant participant) {
        return participant.isNormal() ? participant.getDisplayName() : participant.isBye() ? context.getString(R.string.byeDefaultName) : "";
    }

    public static int getProperTournamentDrawable(final Tournament.TournamentType tournamentType, final boolean forActionBar) {
        switch (tournamentType) {
            case ELIMINATION:
                return forActionBar ? R.drawable.ic_elimination_actionbar : R.drawable.ic_elimination;
            case DOUBLE_ELIMINATION:
                return forActionBar ? R.drawable.ic_double_elimination_actionbar : R.drawable.ic_double_elimination;
            case ROUND_ROBIN:
                return forActionBar ? R.drawable.ic_round_robin_actionbar : R.drawable.ic_round_robin;
            case SURVIVAL:
                return forActionBar ? R.drawable.ic_survival_actionbar : R.drawable.ic_survival;
            case SWISS:
            default:
                return forActionBar ? R.drawable.ic_swiss_actionbar : R.drawable.ic_swiss;
        }
    }

    public static Pair<RecordKeepingTournamentTrait.RankingFromPriority, RecordKeepingTournamentTrait.RankingFromScore> getRankingConfigObjects(final String rankingConfig) {
        RecordKeepingTournamentTrait.RankingFromPriority rankingFromPriority = null;
        RecordKeepingTournamentTrait.RankingFromScore rankingFromScore = null;

        String[] arr = rankingConfig == null ? new String[0] : rankingConfig.split(";");
        if (arr.length == 3) {
            if (arr[0].matches("[wlt]")) {
                rankingFromPriority = RecordKeepingTournamentTrait.buildRankingFromPriority(rankingConfig);
            } else {
                rankingFromScore = RecordKeepingTournamentTrait.buildRankingFromScore(rankingConfig);
            }
        } else {
            rankingFromPriority = RecordKeepingTournamentTrait.buildRankingFromPriority(RecordKeepingTournamentTrait.RankingFromPriority.DEFAULT_INPUT);
        }

        return new ImmutablePair<>(rankingFromPriority,rankingFromScore);
    }
}
