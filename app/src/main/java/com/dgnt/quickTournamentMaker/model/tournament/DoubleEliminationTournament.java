package com.dgnt.quickTournamentMaker.model.tournament;

import com.dgnt.quickTournamentMaker.eventListener.OnMatchUpUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnParticipantUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnTournamentUpdateListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Owner on 3/13/2016.
 */
public class DoubleEliminationTournament extends EliminationTournament {
    private List<Round> loserRounds;
    private List<Round> finalRounds;

    public boolean build(final List<Participant> orderedParticipantList, final OnTournamentUpdateListener onTournamentUpdateListener, final OnMatchUpUpdateListener onMatchUpUpdateListener, final OnParticipantUpdateListener onParticipantUpdateListener) {
        final boolean initialStatus = super.build(orderedParticipantList,onTournamentUpdateListener, onMatchUpUpdateListener,onParticipantUpdateListener);

        if (!initialStatus)
            return false;


        //loser bracket

        loserRounds = new ArrayList<>();

        //for all the winner bracket rounds except the last, we need to add 2 loser rounds. For each pair of loser rounds, it will contain half the match ups as the winner bracket rounds
        for (int roundIndex_winner = 0; roundIndex_winner < rounds.size() - 1; roundIndex_winner++) {

            //do this twice
            for (int i = 0; i < 2; i++) {
                final int roundIndex_loser = roundIndex_winner*2 + i;
                final List<MatchUp> matchUps = new ArrayList<>();
                for (int j = 0; j < rounds.get(roundIndex_winner).getTotalMatchUps() / 2; j++) {
                    final int matchUpIndex = j;
                    final MatchUp matchUp = new MatchUp(1, roundIndex_loser, matchUpIndex, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT);
                    matchUp.setOnMatchUpUpdateListener(onMatchUpUpdateListener);
                    matchUps.add(matchUp);
                }
                loserRounds.add(new Round(matchUps));
            }

        }

        //final bracket

        finalRounds = new ArrayList<>();

        //in the final rounds, we need at least 1 round with 1 match up for the winner bracket participant vs the loser bracket participant
        final MatchUp mandatoryMatchUp = new MatchUp(2, 0, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT);
        mandatoryMatchUp.setOnMatchUpUpdateListener(onMatchUpUpdateListener);
        final Round mandatoryRound = new Round(Arrays.asList(mandatoryMatchUp));

        //we need an extra round in case the loser bracket participant beats the winner bracket participant in the previous match up
        final MatchUp extraMatchUp = new MatchUp(2, 1, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT);
        extraMatchUp.setOnMatchUpUpdateListener(onMatchUpUpdateListener);
        final Round extraRound = new Round(Arrays.asList(extraMatchUp));

        finalRounds.add(mandatoryRound);
        finalRounds.add(extraRound);

        //for all match ups with a bye, set the other participant as the winner
        //- this only occurs in round 1 of the winner bracket and may happen in round 1 and 2 of the loser bracket
        settleStatusesFromByes(0, 0);
        settleStatusesFromByes(1, 0);
        settleStatusesFromByes(1, 1);

        return true;
    }

    protected void settleStatusesFromByes(final int roundGroupIndex, final int roundIndex) {
        if (getRoundGroupAt(0) != null && getRoundGroupAt(1) != null && getRoundGroupAt(2) != null) {
            super.settleStatusesFromByes(roundGroupIndex, roundIndex);
        }
    }

    protected void updateTournamentOnResultChange(final int roundGroupIndex, final int roundIndex, final int matchUpIndex, final MatchUp.MatchUpStatus previousStatus, final MatchUp.MatchUpStatus status) {

        if (roundGroupIndex == 0) {
            super.updateTournamentOnResultChange(roundGroupIndex, roundIndex, matchUpIndex,previousStatus,status);

            for (int currentRoundIndex_winnerBracket = roundIndex, currentMatchUpIndex_winnerBracket = matchUpIndex; currentRoundIndex_winnerBracket < getTotalRounds(0); currentRoundIndex_winnerBracket++, currentMatchUpIndex_winnerBracket *= 0.5) {

                final Round round_winnerBracket = getRoundGroupAt(0).get(currentRoundIndex_winnerBracket);
                final MatchUp matchUp_winnerBracket = round_winnerBracket.getMatchUpAt(currentMatchUpIndex_winnerBracket);

                final int currentRoundIndex_loserBracket = currentRoundIndex_winnerBracket == 0 ? 0 : currentRoundIndex_winnerBracket * 2 - 1;
                final Round round_loserBracket = getRoundGroupAt(1).get(currentRoundIndex_loserBracket);

                final int currentMatchUpIndex_loserBracket;

                //for the first round, the corresponding loser match ups is at location matchUpIndex/2
                if (currentRoundIndex_winnerBracket == 0)
                    currentMatchUpIndex_loserBracket = currentMatchUpIndex_winnerBracket / 2;

                    //otherwise, we alternate between starting from the first match up of the corresponding loser round and the last
                else if (currentRoundIndex_winnerBracket % 2 == 1)
                    currentMatchUpIndex_loserBracket = round_loserBracket.getTotalMatchUps() - currentMatchUpIndex_winnerBracket - 1;
                else
                    currentMatchUpIndex_loserBracket = currentMatchUpIndex_winnerBracket;

                final MatchUp matchUp_loserBracket = round_loserBracket.getMatchUpAt(currentMatchUpIndex_loserBracket);

                final Participant oldParticipant;
                final Participant newParticipant;

                //if this is not the first round, then we always set participant 1 of the loser match up as the participant from the corresponding winner match up

                //otherwise, we alternate
                if (currentRoundIndex_winnerBracket != 0 || currentMatchUpIndex_winnerBracket % 2 == 0) {

                    oldParticipant = matchUp_loserBracket.getParticipant1();


                    if (matchUp_winnerBracket.getStatus() == MatchUp.MatchUpStatus.P1_WINNER) {
                        newParticipant = matchUp_winnerBracket.getParticipant2();
                    } else if (matchUp_winnerBracket.getStatus() == MatchUp.MatchUpStatus.P2_WINNER) {
                        newParticipant = matchUp_winnerBracket.getParticipant1();
                    } else {
                        newParticipant = Participant.NULL_PARTICIPANT;
                    }


                    if (oldParticipant != newParticipant)
                        matchUp_loserBracket.setParticipant1(newParticipant);
                    else
                        break;

                } else {
                    oldParticipant = matchUp_loserBracket.getParticipant2();

                    if (matchUp_winnerBracket.getStatus() == MatchUp.MatchUpStatus.P1_WINNER) {
                        newParticipant = matchUp_winnerBracket.getParticipant2();
                    } else if (matchUp_winnerBracket.getStatus() == MatchUp.MatchUpStatus.P2_WINNER) {
                        newParticipant = matchUp_winnerBracket.getParticipant1();
                    } else {
                        newParticipant = Participant.NULL_PARTICIPANT;
                    }


                    if (oldParticipant != newParticipant)
                        matchUp_loserBracket.setParticipant2(newParticipant);
                    else
                        break;
                }


                updateTournamentOnResultChange(1, currentRoundIndex_loserBracket, currentMatchUpIndex_loserBracket,previousStatus,status);
            }
        } else if (roundGroupIndex == 1) {

            for (int currentRoundIndex = roundIndex + 1, previousMatchUpIndex = matchUpIndex; currentRoundIndex < getTotalRounds(roundGroupIndex); currentRoundIndex++, previousMatchUpIndex = currentRoundIndex % 2 == 0 ? previousMatchUpIndex : previousMatchUpIndex / 2) {
                final int currentMatchUpIndex = currentRoundIndex % 2 == 0 ? previousMatchUpIndex / 2 : previousMatchUpIndex;
                final int previousRoundIndex = currentRoundIndex - 1;

                final Round previousRound = getRoundGroupAt(roundGroupIndex).get(previousRoundIndex);
                final MatchUp previousMatchUp = previousRound.getMatchUpAt(previousMatchUpIndex);

                final Round currentRound = getRoundGroupAt(roundGroupIndex).get(currentRoundIndex);
                final MatchUp currentMatchUp = currentRound.getMatchUpAt(currentMatchUpIndex);


                final Participant oldParticipant = previousRoundIndex % 2 == 0 || previousMatchUpIndex % 2 == 1 ? currentMatchUp.getParticipant2() : currentMatchUp.getParticipant1();
                final int participantIndex = previousRoundIndex % 2 == 0 || previousMatchUpIndex % 2 == 1 ? 1 : 0;

                final boolean breakThisLoop = setParticipantForMatchUp(roundGroupIndex, currentRoundIndex, currentMatchUpIndex, participantIndex, oldParticipant, previousMatchUp, currentMatchUp);

                if (breakThisLoop)
                    break;
            }
        }

        {//to reduce complexity, just always cascade update the final brackets.  Since it is just 2 rounds for that bracket, it's not a big deal

            //we sort of break a rule we created here.

            //when we cascade update participants for the final bracket, we start the cascade from the roundIndex instead of roundIndex+1.
            //It's just easier that way

            final int lastRoundIndex_winnerBracket = getTotalRounds(0) - 1;
            final Round lastRound_winnerBracket = getRoundGroupAt(0).get(lastRoundIndex_winnerBracket);
            final MatchUp lastMatchUp_winnerBracket = lastRound_winnerBracket.getMatchUpAt(0);

            if (lastMatchUp_winnerBracket.getStatus() == MatchUp.MatchUpStatus.P1_WINNER) {
                getMatchUp1FromFinalBracket().setParticipant1(lastMatchUp_winnerBracket.getParticipant1());
            } else if (lastMatchUp_winnerBracket.getStatus() == MatchUp.MatchUpStatus.P2_WINNER) {
                getMatchUp1FromFinalBracket().setParticipant1(lastMatchUp_winnerBracket.getParticipant2());
            } else {
                getMatchUp1FromFinalBracket().setParticipant1(Participant.NULL_PARTICIPANT);
            }


            final int lastRoundIndex_loserBracket = getTotalRounds(1) - 1;
            final Round lastRound_loserBracket = getRoundGroupAt(1).get(lastRoundIndex_loserBracket);
            final MatchUp lastMatchUp_loserBracket = lastRound_loserBracket.getMatchUpAt(0);

            if (lastMatchUp_loserBracket.getStatus() == MatchUp.MatchUpStatus.P1_WINNER) {
                getMatchUp1FromFinalBracket().setParticipant2(lastMatchUp_loserBracket.getParticipant1());
            } else if (lastMatchUp_loserBracket.getStatus() == MatchUp.MatchUpStatus.P2_WINNER) {
                getMatchUp1FromFinalBracket().setParticipant2(lastMatchUp_loserBracket.getParticipant2());
            } else {
                getMatchUp1FromFinalBracket().setParticipant2(Participant.NULL_PARTICIPANT);
            }


            if (getMatchUp1FromFinalBracket().getStatus() == MatchUp.MatchUpStatus.P1_WINNER || getMatchUp1FromFinalBracket().getStatus() == MatchUp.MatchUpStatus.DEFAULT) {
                getMatchUp2FromFinalBracket().setParticipant1(Participant.NULL_PARTICIPANT);
                getMatchUp2FromFinalBracket().setParticipant2(Participant.NULL_PARTICIPANT);
            } else {
                getMatchUp2FromFinalBracket().setParticipant1(getMatchUp1FromFinalBracket().getParticipant1());
                getMatchUp2FromFinalBracket().setParticipant2(getMatchUp1FromFinalBracket().getParticipant2());
            }
        }

    }


    public List<Round> getRoundGroupAt(final int roundGroupIndex) {
        return roundGroupIndex == 0 ? rounds : roundGroupIndex == 1 ? loserRounds : finalRounds;
    }

    public int getTotalRoundGroups() {
        return 3;
    }

    public boolean isBuilt() {
        return rounds != null && loserRounds != null && finalRounds != null;
    }

    private MatchUp getMatchUp1FromFinalBracket() {
        return getRoundGroupAt(2).get(0).getMatchUpAt(0);
    }

    private MatchUp getMatchUp2FromFinalBracket() {
        return getRoundGroupAt(2).get(1).getMatchUpAt(0);
    }

    protected int getBracketIndexToDetermineLoserRanking() {
        return 1;
    }

    protected int getBracketIndexToDetermineWinnerRanking() {
        return 2;
    }

    protected int calculateLoserRank(final int roundIndex) {
        return super.calculateLoserRank(roundIndex) + 1;
    }

    protected void setUpCurrentRanking(final Ranking ranking) {
        super.setUpCurrentRanking(ranking);
        if (getMatchUp1FromFinalBracket().getStatus() == MatchUp.MatchUpStatus.P1_WINNER) {
            ranking.addToKnownRanking(1, getMatchUp1FromFinalBracket().getParticipant1());
            ranking.addToKnownRanking(2, getMatchUp1FromFinalBracket().getParticipant2());
        }
        if (getMatchUp2FromFinalBracket().getStatus() == MatchUp.MatchUpStatus.P1_WINNER) {
            ranking.addToKnownRanking(2, getMatchUp2FromFinalBracket().getParticipant2());
        }
        if (getMatchUp2FromFinalBracket().getStatus() == MatchUp.MatchUpStatus.P2_WINNER) {
            ranking.addToKnownRanking(2, getMatchUp2FromFinalBracket().getParticipant1());
        }
    }

    public TournamentType getType(){
        return TournamentType.DOUBLE_ELIMINATION;
    }

    public String toString() {
        if (!isBuilt())
            return "Not built";
        final StringBuilder sb = new StringBuilder();
        final int maxMatchUp = rounds.get(0).getTotalMatchUps();

        for (int i = 0; i < maxMatchUp; i++) {
            for (int j = 0; j < rounds.size(); j++) {
                final Round round = rounds.get(j);
                if (round.getTotalMatchUps() > i)
                    sb.append(round.getMatchUpAt(i)).append(" | ");
            }
            sb.append("\n");
        }

        sb.append("\n");
        for (int i = 0; i < maxMatchUp; i++) {
            for (int j = 0; j < loserRounds.size(); j++) {
                final Round round = loserRounds.get(j);
                if (round.getTotalMatchUps() > i)
                    sb.append(round.getMatchUpAt(i)).append(" | ");
            }
            sb.append("\n");
        }

        sb.append("\n");
        for (int i = 0; i < maxMatchUp; i++) {
            for (int j = 0; j < finalRounds.size(); j++) {
                final Round round = finalRounds.get(j);
                if (round.getTotalMatchUps() > i)
                    sb.append(round.getMatchUpAt(i)).append(" | ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
