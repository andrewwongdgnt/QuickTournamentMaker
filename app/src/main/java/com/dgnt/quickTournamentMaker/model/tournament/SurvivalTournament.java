package com.dgnt.quickTournamentMaker.model.tournament;

import com.dgnt.quickTournamentMaker.eventListener.OnMatchUpUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnParticipantUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnTournamentUpdateListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 3/12/2016.
 */
public class SurvivalTournament extends Tournament implements SingleMatchUpTournament{

    public boolean build(final List<Participant> orderedParticipantList, final OnTournamentUpdateListener onTournamentUpdateListener, final OnMatchUpUpdateListener onMatchUpUpdateListener, final OnParticipantUpdateListener onParticipantUpdateListener) {
        final boolean initialStatus = super.build(orderedParticipantList,onTournamentUpdateListener, onMatchUpUpdateListener,onParticipantUpdateListener);

        if (!initialStatus)
            return false;

        //Total participants.
        final int totalParticipants = orderedParticipantList.size()/2;

        //set match ups for remaining rounds.  there will be totalParticipants-1 rounds in total
        for (int currentRoundIndex = 1; currentRoundIndex < totalParticipants - 1; currentRoundIndex++) {

            final List<MatchUp> matchUps = new ArrayList<>();
            for (int i = 0; i < totalParticipants; i++) {
                final int matchUpIndex = i;
                final MatchUp matchUp = new MatchUp(0, currentRoundIndex, matchUpIndex, Participant.NULL_PARTICIPANT, Participant.BYE_PARTICIPANT);
                matchUp.setOnMatchUpUpdateListener(onMatchUpUpdateListener);
                matchUps.add(matchUp);
            }

            rounds.add(new Round(matchUps));
        }


        return true;
    }

    protected boolean doByesExist(final MatchUp matchUp) {


        return false;
    }


    //update match ups with new participants based on the result just set.
    protected void updateTournamentOnResultChange(final int roundGroupIndex, final int roundIndex, final int matchUpIndex, final MatchUp.MatchUpStatus previousStatus, final MatchUp.MatchUpStatus status) {

        for (int currentRoundIndex = roundIndex + 1; currentRoundIndex < getTotalRounds(roundGroupIndex); currentRoundIndex++) {
            final int previousRoundIndex = currentRoundIndex - 1;

            final Round previousRound = getRoundGroupAt(roundGroupIndex).get(previousRoundIndex);
            final MatchUp previousMatchUp = previousRound.getMatchUpAt(matchUpIndex);

            final Round currentRound = getRoundGroupAt(roundGroupIndex).get(currentRoundIndex);
            final MatchUp currentMatchUp = currentRound.getMatchUpAt(matchUpIndex);


            final Participant oldParticipant = matchUpIndex % 2 == 0 ? currentMatchUp.getParticipant1() : currentMatchUp.getParticipant2();
            final int participantIndex = matchUpIndex % 2 == 0 ? 0 : 1;

            final boolean breakThisLoop = setParticipantForMatchUp(roundGroupIndex, currentRoundIndex, matchUpIndex, participantIndex, oldParticipant, previousMatchUp, currentMatchUp);

            if (breakThisLoop) {
                break;
            }
        }
    }

    //return value means the old participant and new participant is the same.
    protected boolean setParticipantForMatchUp(final int roundGroupIndex, final int currentRoundIndex, final int currentMatchUpIndex, final int participantIndex, final Participant oldParticipant, final MatchUp previousMatchUp, final MatchUp currentMatchUp) {

        final Participant newParticipant;

        if (previousMatchUp.getStatus() == MatchUp.MatchUpStatus.P1_WINNER)
            newParticipant = previousMatchUp.getParticipant1();
        else
            newParticipant = Participant.NULL_PARTICIPANT;

        if (oldParticipant != newParticipant) {
            currentMatchUp.setParticipant1(newParticipant);
        } else
            return true;


        return false;
    }

    protected void setUpCurrentRanking(final Ranking ranking) {

        int rank = 1;
        for (int roundIndex = getTotalRounds(0) - 1; roundIndex >= 0; roundIndex--) {

            boolean updateRank = false;
            for (int matchUpIndex = 0; matchUpIndex < getRoundAt(0, roundIndex).getTotalMatchUps(); matchUpIndex++) {
                final Participant participant = getParticipant(0, roundIndex, matchUpIndex, 0);
                final MatchUp matchUp = getMatchUp(0, roundIndex, matchUpIndex);
                if (!participant.isNull() && matchUp.getStatus() == MatchUp.MatchUpStatus.P1_WINNER) {
                    updateRank = ranking.addToKnownRanking(rank, participant) || updateRank;

                }
            }
            if (updateRank)
                rank++;

            if (roundIndex == 0) {
                for (int matchUpIndex = 0; matchUpIndex < getRoundAt(0, roundIndex).getTotalMatchUps(); matchUpIndex++) {
                    final Participant participant = getParticipant(0, roundIndex, matchUpIndex, 0);
                    ranking.addToKnownRanking(rank, participant);
                }
            }
        }

    }

    public TournamentType getType() {
        return TournamentType.SURVIVAL;
    }


}
