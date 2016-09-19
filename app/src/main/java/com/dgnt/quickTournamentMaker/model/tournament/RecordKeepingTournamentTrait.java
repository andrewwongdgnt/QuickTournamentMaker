package com.dgnt.quickTournamentMaker.model.tournament;

import com.dgnt.quickTournamentMaker.util.TournamentUtil;

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

    private Comparator<Participant> rankedParticipantComparator = new Comparator<Participant>() {
        @Override
        public int compare(Participant lhs, Participant rhs) {
            return TournamentUtil.compareParticipantsBasedOnRecord(lhs, rhs);
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
}
