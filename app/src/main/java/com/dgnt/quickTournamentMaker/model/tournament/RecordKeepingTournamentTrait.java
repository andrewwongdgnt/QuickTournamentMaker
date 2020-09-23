package com.dgnt.quickTournamentMaker.model.tournament;

import com.dgnt.quickTournamentMaker.util.TournamentUtil;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Owner on 9/13/2016.
 */
public class RecordKeepingTournamentTrait {

    public enum RecordType {
        WIN, TIE, LOSS
    }

    private Tournament tournament;

    public RecordKeepingTournamentTrait(final Tournament tournament) {
        this.tournament = tournament;
    }

    private RankingFromPriority rankingFromPriority;
    private RankingFromScore rankingFromScore;

    public void setRankingConfig(final String rankingConfig) {

        final Pair<RankingFromPriority, RankingFromScore> pair = TournamentUtil.getRankingConfigObjects(rankingConfig);
        rankingFromPriority = pair.getLeft();
        rankingFromScore = pair.getRight();
    }

    public String getRankingConfig() {
        if (rankingFromPriority == null && rankingFromScore == null) {
            setRankingConfig(RecordKeepingTournamentTrait.RankingFromPriority.DEFAULT_INPUT);
        }
        if (rankingFromPriority != null) {
            return rankingFromPriority.toString();
        } else {
            return rankingFromScore.toString();
        }
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

        if (rankingFromPriority == null && rankingFromScore == null) {
            setRankingConfig(RecordKeepingTournamentTrait.RankingFromPriority.DEFAULT_INPUT);
        }

        if (rankingFromPriority != null) {
            return compareParticipantsBasedOnRecord_fromPriority(rankingFromPriority, lhs, rhs);
        } else {
            return compareParticipantsBasedOnRecord_fromScore(rankingFromScore, lhs, rhs);
        }
    }


    public static int compareParticipantsBasedOnRecord_fromPriority(final RankingFromPriority rankingFromPriority, final Participant lhs, final Participant rhs) {
        for (int i = 0; i < 3; i++) {


            final RecordType recordType;

            switch (i) {
                case 0:
                    recordType = rankingFromPriority.getFirstPriority();
                    break;
                case 1:
                    recordType = rankingFromPriority.getSecondPriority();
                    break;
                case 2:
                default:
                    recordType = rankingFromPriority.getThirdPriority();
            }

            if (recordType == RecordType.WIN) {
                if (lhs.getWins() > rhs.getWins())
                    return -1;
                else if (rhs.getWins() > lhs.getWins())
                    return 1;
            } else if (recordType == RecordType.LOSS) {
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


    public static RankingFromPriority buildRankingFromPriority(final String input) {


        final RankingFromPriority rankingFromPriority = new RankingFromPriority();

        String[] priority = input == null ? new String[0] : input.split(";");
        if (priority.length != 3) {
            priority = RankingFromPriority.DEFAULT_INPUT.split(";");
        }
        for (int i = 0; i < priority.length; i++) {
            final String priority_string = priority[i];
            final RecordType recordType;
            if (priority_string.equals("w"))
                recordType = RecordType.WIN;
            else if (priority_string.equals("l"))
                recordType = RecordType.LOSS;
            else //"t"
                recordType = RecordType.TIE;

            if (i == 0)
                rankingFromPriority.firstPriority = recordType;
            else if (i == 1)
                rankingFromPriority.secondPriority = recordType;
            else
                rankingFromPriority.thirdPriority = recordType;
        }


        return rankingFromPriority;
    }

    public static String getRankingFromPriorityAsString(final RecordType firstPriority, final RecordType secondPriority, final RecordType thirdPriority) {
        final StringBuilder stringBuilder = new StringBuilder();
        String sep = "";
        for (int i = 0; i < 3; i++) {

            final RecordType recordType;

            switch (i) {
                case 0:
                    recordType = firstPriority;
                    break;
                case 1:
                    recordType = secondPriority;
                    break;
                case 2:
                default:
                    recordType = thirdPriority;
            }

            stringBuilder.append(sep);
            switch (recordType) {
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

        private RecordType firstPriority;
        private RecordType secondPriority;
        private RecordType thirdPriority;

        private RankingFromPriority() {

        }

        public RecordType getFirstPriority() {
            return firstPriority;
        }

        public RecordType getSecondPriority() {
            return secondPriority;
        }

        public RecordType getThirdPriority() {
            return thirdPriority;
        }

        public String toString() {
            return getRankingFromPriorityAsString(firstPriority, secondPriority, thirdPriority);
        }
    }

    public static RankingFromScore buildRankingFromScore(final String input) {
        final RankingFromScore rankingFromScore = new RankingFromScore();

        String[] scores = input == null ? new String[0] : input.split(";");

        if (scores.length != 3) {
            scores = RankingFromScore.DEFAULT_INPUT.split(";");
        }
        for (int i = 0; i < scores.length; i++) {
            final String score_string = scores[i];

            int score = 0;
            try {
                score = Integer.parseInt(score_string);
            } catch (NumberFormatException e) {

            }

            if (i == 0)
                rankingFromScore.winScore = score;
            else if (i == 1)
                rankingFromScore.lossScore = score;
            else
                rankingFromScore.tieScore = score;
        }

        return rankingFromScore;
    }

    public static String getRankingFromScoreAsString(final int winScore, final int lossScore, final int tieScore) {
        return winScore + ";" + lossScore + ";" + tieScore;
    }

    public static class RankingFromScore {

        final public static String DEFAULT_INPUT = "2;0;1";

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

        public String toString() {
            return getRankingFromScoreAsString(winScore, lossScore, tieScore);
        }
    }
}
