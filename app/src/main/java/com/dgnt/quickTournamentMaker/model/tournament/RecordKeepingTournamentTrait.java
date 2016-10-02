package com.dgnt.quickTournamentMaker.model.tournament;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dgnt.quickTournamentMaker.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Owner on 9/13/2016.
 */
public class RecordKeepingTournamentTrait {

    private Tournament tournament;

    public RecordKeepingTournamentTrait(final Tournament tournament) {
        this.tournament = tournament;
    }

    private RankingFromPriority rankingFromPriority;

    public void setRankingConfigFromPriority(final RankingFromPriority rankingFromPriority) {
        this.rankingFromPriority = rankingFromPriority;
        rankingFromScore = null;
    }

    private RankingFromScore rankingFromScore;

    public void setRankingConfigFromScore(final RankingFromScore rankingFromScore) {
        this.rankingFromScore = rankingFromScore;
        rankingFromPriority = null;
    }

    public void updateParticipantsRecordOnResultChange(final int roundGroupIndex, final int roundIndex, final int matchUpIndex, final MatchUp.MatchUpStatus previousStatus, final MatchUp.MatchUpStatus status) {

        //update participant records based on the result just set.
        final Participant participant1 = tournament.getParticipant(roundGroupIndex, roundIndex, matchUpIndex, 0);
        final Participant participant2 = tournament.getParticipant(roundGroupIndex, roundIndex, matchUpIndex, 1);

        if (status == MatchUp.MatchUpStatus.P1_WINNER) {
            participant1.adjustWinsBy(1);
            participant2.adjustLossesBy(1);
            if (previousStatus == MatchUp.MatchUpStatus.P2_WINNER) {
                participant1.adjustLossesBy(-1);
                participant2.adjustWinsBy(-1);
            } else if (previousStatus == MatchUp.MatchUpStatus.TIE) {
                participant1.adjustTiesBy(-1);
                participant2.adjustTiesBy(-1);
            }
        } else if (status == MatchUp.MatchUpStatus.P2_WINNER) {
            participant2.adjustWinsBy(1);
            participant1.adjustLossesBy(1);
            if (previousStatus == MatchUp.MatchUpStatus.P1_WINNER) {
                participant2.adjustLossesBy(-1);
                participant1.adjustWinsBy(-1);
            } else if (previousStatus == MatchUp.MatchUpStatus.TIE) {
                participant2.adjustTiesBy(-1);
                participant1.adjustTiesBy(-1);
            }
        } else if (status == MatchUp.MatchUpStatus.TIE) {
            participant1.adjustTiesBy(1);
            participant2.adjustTiesBy(1);
            if (previousStatus == MatchUp.MatchUpStatus.P1_WINNER) {
                participant1.adjustWinsBy(-1);
                participant2.adjustLossesBy(-1);
            } else if (previousStatus == MatchUp.MatchUpStatus.P2_WINNER) {
                participant1.adjustLossesBy(-1);
                participant2.adjustWinsBy(-1);
            }
        } else {

            if (previousStatus == MatchUp.MatchUpStatus.P1_WINNER) {
                participant1.adjustWinsBy(-1);
                participant2.adjustLossesBy(-1);
            } else if (previousStatus == MatchUp.MatchUpStatus.P2_WINNER) {
                participant1.adjustLossesBy(-1);
                participant2.adjustWinsBy(-1);
            } else { //Tie case
                participant2.adjustTiesBy(-1);
                participant1.adjustTiesBy(-1);
            }
        }
    }

    public int compareParticipantsBasedOnRecord(Participant lhs, Participant rhs) {

        if (rankingFromPriority==null && rankingFromScore==null)
            setRankingConfigFromPriority(RecordKeepingTournamentTrait.buildRankingFromPriority(RecordKeepingTournamentTrait.RankingFromPriority.DEFAULT_INPUT));


        if (rankingFromPriority != null)
            return compareParticipantsBasedOnRecord_fromPriority(rankingFromPriority, lhs, rhs);
        else
            return compareParticipantsBasedOnRecord_fromScore(rankingFromScore, lhs, rhs);
    }


    public static int compareParticipantsBasedOnRecord_fromPriority(final RankingFromPriority rankingFromPriority, final Participant lhs, final Participant rhs) {
        for (int i = 0; i < 3; i++) {


            final RankPriorityType rankPriorityType;

            switch (i) {
                case 0:
                    rankPriorityType = rankingFromPriority.getFirstPriority();
                    break;
                case 1:
                    rankPriorityType = rankingFromPriority.getSecondPriority();
                    break;
                case 2:
                default:
                    rankPriorityType = rankingFromPriority.getThirdPriority();
            }

            if (rankPriorityType == RankPriorityType.WIN) {
                if (lhs.getWins() > rhs.getWins())
                    return -1;
                else if (rhs.getWins() > lhs.getWins())
                    return 1;
            } else if (rankPriorityType == RankPriorityType.LOSS) {
                if (lhs.getLosses() > rhs.getLosses())
                    return 1;
                else if (rhs.getLosses() > lhs.getLosses())
                    return -1;
            } else { //TIE
                if (lhs.getTies() > rhs.getTies())
                    return -1;
                else if (rhs.getTies() > lhs.getTies())
                    return 1;
            }
        }
        return 0;
    }

    public static int compareParticipantsBasedOnRecord_fromScore(final RankingFromScore rankingFromScore, final Participant lhs, final Participant rhs) {
        final int lhsScore = lhs.getWins() * rankingFromScore.getWinScore() + lhs.getTies() * rankingFromScore.getTieScore() + lhs.getLosses() * rankingFromScore.getLossScore();
        final int rhsScore = rhs.getWins() * rankingFromScore.getWinScore() + rhs.getTies() * rankingFromScore.getTieScore() + rhs.getLosses() * rankingFromScore.getLossScore();

        return rhsScore - lhsScore;
    }

    private Comparator<Participant> rankedParticipantComparator = new Comparator<Participant>() {
        @Override
        public int compare(Participant lhs, Participant rhs) {
            return compareParticipantsBasedOnRecord(lhs, rhs);
        }
    };

    private List<Participant> rankedParticipantList;

    private List<Participant> getRankedParticipantList() {

        if (rankedParticipantList == null) {
            rankedParticipantList = new ArrayList<>();

            for (int matchUpIndex = 0; matchUpIndex < tournament.getRoundAt(0, 0).getTotalMatchUps(); matchUpIndex++) {
                final MatchUp matchUp = tournament.getMatchUp(0, 0, matchUpIndex);
                rankedParticipantList.add(matchUp.getParticipant1());
                rankedParticipantList.add(matchUp.getParticipant2());
            }

        }
        Collections.sort(rankedParticipantList, rankedParticipantComparator);

        return rankedParticipantList;
    }

    public void setUpCurrentRanking(final Ranking ranking) {
        final List<Participant> rankedParticipantList = getRankedParticipantList();

        int rank = 1;
        for (int participantIndex = 0; participantIndex < rankedParticipantList.size(); participantIndex++) {
            final Participant currentParticipant = rankedParticipantList.get(participantIndex);
            //if the next ranked guy has the same rank as the prev ranked guy, then we reduce the rank number before adding him to the ranked list.
            if (participantIndex > 0 && rankedParticipantComparator.compare(rankedParticipantList.get(participantIndex - 1), currentParticipant) == 0)
                rank--;
            ranking.addToKnownRanking(rank, currentParticipant);
            rank++;
        }
    }

    public enum RankPriorityType {
        WIN, TIE, LOSS
    }

    public static RankingFromPriority buildRankingFromPriority(final String input) {


        final RankingFromPriority rankingFromPriority = new RankingFromPriority();

        final String[] priority = input.split(";");
        if (priority.length == 3) {
            for (int i = 0; i < priority.length; i++) {
                final String priority_string = priority[i];
                final RankPriorityType rankingPriorityType;
                if (priority_string.equals("w"))
                    rankingPriorityType = RankPriorityType.WIN;
                else if (priority_string.equals("l"))
                    rankingPriorityType = RankPriorityType.LOSS;
                else //"t"
                    rankingPriorityType = RankPriorityType.TIE;

                if (i == 0)
                    rankingFromPriority.firstPriority = rankingPriorityType;
                else if (i == 1)
                    rankingFromPriority.secondPriority = rankingPriorityType;
                else
                    rankingFromPriority.thirdPriority = rankingPriorityType;
            }
        } else {
            rankingFromPriority.firstPriority = RankPriorityType.WIN;
            rankingFromPriority.secondPriority = RankPriorityType.LOSS;
            rankingFromPriority.thirdPriority = RankPriorityType.TIE;
        }

        return rankingFromPriority;
    }

    public static String getRankingFromPriorityAsString(final RankPriorityType firstPriority, final RankPriorityType secondPriority, final RankPriorityType thirdPriority) {
        final StringBuilder stringBuilder = new StringBuilder();
        String sep = "";
        for (int i = 0; i < 3; i++) {

            final RecordKeepingTournamentTrait.RankPriorityType rankPriorityType;

            switch (i) {
                case 0:
                    rankPriorityType = firstPriority;
                    break;
                case 1:
                    rankPriorityType = secondPriority;
                    break;
                case 2:
                default:
                    rankPriorityType = thirdPriority;
            }

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

        return stringBuilder.toString();
    }

    public static class RankingFromPriority {
        final public static String DEFAULT_INPUT = "w;l;t";

        private RankPriorityType firstPriority;
        private RankPriorityType secondPriority;
        private RankPriorityType thirdPriority;

        private RankingFromPriority() {

        }

        public RankPriorityType getFirstPriority() {
            return firstPriority;
        }

        public RankPriorityType getSecondPriority() {
            return secondPriority;
        }

        public RankPriorityType getThirdPriority() {
            return thirdPriority;
        }
    }

    public static RankingFromScore buildRankingFromScore(final int winScore, final int lossScore, final int tieScore) {
        final RankingFromScore rankingFromScore = new RankingFromScore();


        rankingFromScore.winScore = winScore;
        rankingFromScore.lossScore = lossScore;
        rankingFromScore.tieScore = tieScore;


        return rankingFromScore;
    }

    public static class RankingFromScore {


        private int winScore;
        private int lossScore;
        private int tieScore;

        private RankingFromScore() {

        }


        public int getWinScore() {
            return winScore;
        }

        public int getLossScore() {
            return lossScore;
        }

        public int getTieScore() {
            return tieScore;
        }
    }
}
