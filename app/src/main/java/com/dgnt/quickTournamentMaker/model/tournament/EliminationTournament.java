package com.dgnt.quickTournamentMaker.model.tournament;

import com.dgnt.quickTournamentMaker.eventListener.OnMatchUpUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnParticipantUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnTournamentUpdateListener;
import com.dgnt.quickTournamentMaker.util.TournamentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 3/12/2016.
 */
public class EliminationTournament extends Tournament {

    protected boolean isInitialConfigGood(final List<Participant> orderedParticipantList) {
        final boolean initialStatus = super.isInitialConfigGood(orderedParticipantList);

        if (!initialStatus)
            return false;

        //Total participants (including byes).
        final int totalParticipants = orderedParticipantList.size();

        //  This should be a power of 2. If not, stop.
        if (!TournamentUtil.isPowerOf2(totalParticipants))
            return false;

        return true;
    }

    public boolean build(final List<Participant> orderedParticipantList, final OnTournamentUpdateListener onTournamentUpdateListener, final OnMatchUpUpdateListener onMatchUpUpdateListener, final OnParticipantUpdateListener onParticipantUpdateListener) {
        final boolean initialStatus = super.build(orderedParticipantList,onTournamentUpdateListener, onMatchUpUpdateListener,onParticipantUpdateListener);

        if (!initialStatus)
            return false;

        //Total participants (including byes).
        final int totalParticipants = orderedParticipantList.size();

        //set match ups for remaining rounds. there will be totalParticipants log 2 rounds in total. EG with 16 participants, 16 log 2 = 4, so 4 rounds in total
        for (int remainingParticipants = totalParticipants / 2; remainingParticipants >= 2; remainingParticipants *= 0.5) {

            final int roundIndex = (int) (Math.log(totalParticipants / remainingParticipants) / Math.log(2));

            final List<MatchUp> matchUps = new ArrayList<>();
            for (int i = 0; i < remainingParticipants / 2; i++) {
                final int matchUpIndex = i ;
                final MatchUp matchUp = new MatchUp(0, roundIndex, matchUpIndex, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT);
                matchUp.setOnMatchUpUpdateListener(onMatchUpUpdateListener);
                matchUps.add(matchUp);

            }

            rounds.add(new Round(matchUps));
        }

        //for all match ups with a bye, set the other participant as the winner
        //- this only occurs in round 1
        settleStatusesFromByes(0, 0);


        return true;
    }




    //update match ups with new participants based on the result just set.  This can cascade into other rounds.
    protected void updateTournamentOnResultChange(final int roundGroupIndex, final int roundIndex, final int matchUpIndex, final MatchUp.MatchUpStatus previousStatus, final MatchUp.MatchUpStatus status) {

        for (int currentRoundIndex = roundIndex + 1, previousMatchUpIndex = matchUpIndex; currentRoundIndex < getTotalRounds(roundGroupIndex); currentRoundIndex++, previousMatchUpIndex *= 0.5) {
            final int currentMatchUpIndex = previousMatchUpIndex / 2;
            final int previousRoundIndex = currentRoundIndex - 1;

            final Round previousRound = getRoundGroupAt(roundGroupIndex).get(previousRoundIndex);
            final MatchUp previousMatchUp = previousRound.getMatchUpAt(previousMatchUpIndex);

            final Round currentRound = getRoundGroupAt(roundGroupIndex).get(currentRoundIndex);
            final MatchUp currentMatchUp = currentRound.getMatchUpAt(currentMatchUpIndex);

            final Participant oldParticipant = previousMatchUpIndex % 2 == 0 ? currentMatchUp.getParticipant1() : currentMatchUp.getParticipant2();
            final int participantIndex = previousMatchUpIndex % 2 == 0 ? 0 : 1;

            final boolean breakThisLoop = setParticipantForMatchUp(roundGroupIndex, currentRoundIndex, currentMatchUpIndex, participantIndex, oldParticipant, previousMatchUp, currentMatchUp);

            if (breakThisLoop)
                break;
        }
    }

    //return value means the old participant and new participant is the same.
    protected boolean setParticipantForMatchUp(final int roundGroupIndex, final int currentRoundIndex, final int currentMatchUpIndex, final int participantIndex, final Participant oldParticipant, final MatchUp previousMatchUp, final MatchUp currentMatchUp) {

        final Participant newParticipant;

        if (previousMatchUp.getStatus() == MatchUp.MatchUpStatus.P1_WINNER)
            newParticipant = previousMatchUp.getParticipant1();
        else if (previousMatchUp.getStatus() == MatchUp.MatchUpStatus.P2_WINNER)
            newParticipant = previousMatchUp.getParticipant2();
        else
            newParticipant = Participant.NULL_PARTICIPANT;

        if (oldParticipant != newParticipant) {
            if (participantIndex == 0)
                currentMatchUp.setParticipant1(newParticipant);
            else
                currentMatchUp.setParticipant2(newParticipant);
        } else
            return true;


        return false;
    }

    protected int getBracketIndexToDetermineLoserRanking() {
        return 0;
    }

    protected int getBracketIndexToDetermineWinnerRanking() {
        return 0;
    }

    protected int calculateLoserRank(final int roundIndex) {
        return getTotalRounds(getBracketIndexToDetermineLoserRanking()) - roundIndex + 1;
    }

    protected void setUpCurrentRanking(final Ranking ranking) {

        for (int roundGroupIndex = 0; roundGroupIndex < getTotalRoundGroups(); roundGroupIndex++) {
            for (int roundIndex = 0; roundIndex < getTotalRounds(roundGroupIndex); roundIndex++) {
                final Round round = getRoundAt(roundGroupIndex, roundIndex);
                for (int matchUpIndex = 0; matchUpIndex < round.getTotalMatchUps(); matchUpIndex++) {
                    final MatchUp matchUp = round.getMatchUpAt(matchUpIndex);
                    final Participant participant1 = matchUp.getParticipant1();
                    final Participant participant2 = matchUp.getParticipant2();
                    if (matchUp.getStatus() == MatchUp.MatchUpStatus.DEFAULT) {
                        ranking.addToUnknownRanking(participant1);
                        ranking.addToUnknownRanking(participant2);

                    }


                    if (roundGroupIndex == getBracketIndexToDetermineLoserRanking()) {
                        final int rank = calculateLoserRank(roundIndex);
                        if (matchUp.getStatus() == MatchUp.MatchUpStatus.P1_WINNER) {
                            ranking.addToKnownRanking(rank, participant2);
                        }
                        if (matchUp.getStatus() == MatchUp.MatchUpStatus.P2_WINNER) {
                            ranking.addToKnownRanking(rank, participant1);
                        }
                    }

                    if (roundGroupIndex == getBracketIndexToDetermineWinnerRanking() && roundIndex == getTotalRounds(roundGroupIndex) - 1) {
                        if (matchUp.getStatus() == MatchUp.MatchUpStatus.P1_WINNER) {
                            ranking.addToKnownRanking(1, participant1);
                        }
                        if (matchUp.getStatus() == MatchUp.MatchUpStatus.P2_WINNER) {
                            ranking.addToKnownRanking(1, participant2);
                        }
                    }

                }
            }
        }
    }

    public TournamentType getType(){
        return TournamentType.ELIMINATION;
    }


}
