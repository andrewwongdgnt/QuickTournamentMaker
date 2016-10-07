package com.dgnt.quickTournamentMaker.model.tournament;

import android.support.v4.util.Pair;

import com.dgnt.quickTournamentMaker.eventListener.OnMatchUpUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnParticipantUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnTournamentUpdateListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Owner on 3/12/2016.
 */
public class SwissTournament extends Tournament implements RecordKeepingTournament {

    public void setRankingConfig(final String rankingConfig) {
        getRecordKeepingTournamentTrait().setRankingConfig(rankingConfig);
    }
    public String getRankingConfig() {
        return getRecordKeepingTournamentTrait().getRankingConfig();
    }
    private RecordKeepingTournamentTrait recordKeepingTournamentTrait;

    protected RecordKeepingTournamentTrait getRecordKeepingTournamentTrait() {
        if (recordKeepingTournamentTrait == null)
            recordKeepingTournamentTrait = new RecordKeepingTournamentTrait(this);
        return recordKeepingTournamentTrait;
    }

    private TiesAllowedTournamentTrait tiesAllowedTournamentTrait;

    protected TiesAllowedTournamentTrait getTiesAllowedTournamentTrait() {
        if (tiesAllowedTournamentTrait == null)
            tiesAllowedTournamentTrait = new TiesAllowedTournamentTrait(this);
        return tiesAllowedTournamentTrait;
    }

    public boolean build(final List<Participant> orderedParticipantList, final OnTournamentUpdateListener onTournamentUpdateListener, final OnMatchUpUpdateListener onMatchUpUpdateListener, final OnParticipantUpdateListener onParticipantUpdateListener) {
        final boolean initialStatus = super.build(orderedParticipantList, onTournamentUpdateListener, onMatchUpUpdateListener, onParticipantUpdateListener);

        if (!initialStatus)
            return false;

        //Total participants (including byes).
        final int totalParticipants = orderedParticipantList.size();

        for (int matchUpIndex = 0; matchUpIndex < totalParticipants / 2; matchUpIndex++) {
            final MatchUp matchUp = getMatchUp(0, 0, matchUpIndex);
            getMatchUpHistory().add(getPairKey(matchUp.getParticipant1(), matchUp.getParticipant2()));
        }

        //set match ups for remaining rounds. There will be totalParticipants - 1 rounds in total. EG with 6 participants, 6 - 1 = 5, so 5 rounds in total
        for (int roundIndex = 1; roundIndex < totalParticipants - 1; roundIndex++) {

            final List<MatchUp> matchUps = new ArrayList<>();
            for (int matchUpIndex = 0; matchUpIndex < totalParticipants / 2; matchUpIndex++) {
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

    protected MatchUp.MatchUpStatus getNewMatchUpStatus(final MatchUp.MatchUpStatus currentStatus, final int participantIndex) {
        return getTiesAllowedTournamentTrait().getNewMatchUpStatus(currentStatus, participantIndex);
    }

    protected void updateTournamentOnResultChange(final int roundGroupIndex, final int roundIndex, final int matchUpIndex, final MatchUp.MatchUpStatus previousStatus, final MatchUp.MatchUpStatus status) {
        getRecordKeepingTournamentTrait().updateParticipantsRecordOnResultChange(roundGroupIndex, roundIndex, matchUpIndex, previousStatus, status);
        createNewRound(roundGroupIndex, roundIndex);
    }

    //update match ups of next round with new participants based on previous round.  The update can only happen if all match ups of the previous round has been resolved.
    protected void createNewRound(final int roundGroupIndex, final int roundIndex) {

        //if not last round
        if (roundIndex < getTotalRounds(roundGroupIndex) - 1) {

            for (int roundIndexToPurge = roundIndex + 1; roundIndexToPurge < getTotalRounds(roundGroupIndex); roundIndexToPurge++) {

                boolean allParticipantsAlreadyNull = true;
                for (int matchUpIndexToPurge = 0; matchUpIndexToPurge < getTotalMatchUps(roundGroupIndex, roundIndexToPurge); matchUpIndexToPurge++) {
                    final MatchUp matchUpToPurge = getMatchUp(roundGroupIndex, roundIndexToPurge, matchUpIndexToPurge);

                    final Participant participant1 = matchUpToPurge.getParticipant1();
                    final Participant participant2 = matchUpToPurge.getParticipant2();

                    if (!participant1.isNull() || !participant2.isNull())
                        allParticipantsAlreadyNull = false;

                    getMatchUpHistory().remove(getPairKey(participant1, participant2));

                    final MatchUp.MatchUpStatus matchUpStatus = matchUpToPurge.getStatus();
                    if (matchUpStatus == MatchUp.MatchUpStatus.P1_WINNER) {
                        participant1.adjustWinsBy(-1);
                        participant2.adjustLossesBy(-1);
                    } else if (matchUpStatus == MatchUp.MatchUpStatus.P2_WINNER) {
                        participant2.adjustWinsBy(-1);
                        participant1.adjustLossesBy(-1);
                    } else if (matchUpStatus == MatchUp.MatchUpStatus.TIE) {
                        participant1.adjustTiesBy(-1);
                        participant2.adjustTiesBy(-1);
                    }

                    matchUpToPurge.setParticipant1(Participant.NULL_PARTICIPANT);
                    matchUpToPurge.setParticipant2(Participant.NULL_PARTICIPANT);
                }

                if (allParticipantsAlreadyNull)
                    break;
            }


            final boolean areAllMatchUpsResolved = areAllMatchUpsResolvedAndNotNull(roundGroupIndex, roundIndex);

            if (areAllMatchUpsResolved) {

                final List<Participant> rankedParticipantList = getRankedParticipantByRecordAndKeyList();

                final List<Pair<Participant, Participant>> pairList = getProperPairing(rankedParticipantList, getMatchUpHistory());


                for (int nextMatchUpIndex = 0; nextMatchUpIndex < pairList.size(); nextMatchUpIndex++) {
                    final Pair<Participant, Participant> pair = pairList.get(nextMatchUpIndex);
                    final Participant participant1 = pair.first;
                    final Participant participant2 = pair.second;

                    getMatchUpHistory().add(getPairKey(participant1, participant2));

                    final MatchUp nextMatchUp = getMatchUp(roundGroupIndex, roundIndex + 1, nextMatchUpIndex);
                    nextMatchUp.setParticipant1(participant1);
                    nextMatchUp.setParticipant2(participant2);

                    if (participant1.isBye())
                        nextMatchUp.setStatus(MatchUp.MatchUpStatus.P2_WINNER);
                    else if (participant2.isBye())
                        nextMatchUp.setStatus(MatchUp.MatchUpStatus.P1_WINNER);

                    final MatchUp.MatchUpStatus matchUpStatus = nextMatchUp.getStatus();
                    if (matchUpStatus == MatchUp.MatchUpStatus.P1_WINNER) {
                        participant1.adjustWinsBy(1);
                        participant2.adjustLossesBy(1);
                    } else if (matchUpStatus == MatchUp.MatchUpStatus.P2_WINNER) {
                        participant2.adjustWinsBy(1);
                        participant1.adjustLossesBy(1);
                    } else if (matchUpStatus == MatchUp.MatchUpStatus.TIE) {
                        participant1.adjustTiesBy(1);
                        participant2.adjustTiesBy(1);
                    }
                }

                createNewRound(roundGroupIndex, roundIndex + 1);

            }
        }
    }

    private Set<String> matchUpHistory;

    private Set<String> getMatchUpHistory() {
        if (matchUpHistory == null) {
            matchUpHistory = new HashSet<>();
        }
        return matchUpHistory;
    }

    private boolean areAllMatchUpsResolvedAndNotNull(final int roundGroupIndex, final int roundIndex) {
        final int totalMatchUps = getRoundAt(roundGroupIndex, roundIndex).getTotalMatchUps();

        for (int matchUpIndex = 0; matchUpIndex < totalMatchUps; matchUpIndex++) {
            final MatchUp matchUp = getMatchUp(0, roundIndex, matchUpIndex);
            if (matchUp.isDefaultStatus() || matchUp.getParticipant1().isNull() || matchUp.getParticipant2().isNull())
                return false;
        }
        return true;
    }

    private Comparator<Participant> rankedParticipantComparatorForRecordAndKey = new Comparator<Participant>() {
        @Override
        public int compare(Participant lhs, Participant rhs) {

            final int difference = getRecordKeepingTournamentTrait().compareParticipantsBasedOnRecord(lhs, rhs);
            if (difference != 0)
                return difference;

            if (lhs.isNormal() && !rhs.isNormal()) {
                return -1;
            } else if (!lhs.isNormal() && rhs.isNormal()) {
                return 1;
            }

            return lhs.getKey().compareTo(rhs.getKey());
        }
    };

    private List<Participant> rankedParticipantListByRecordAndKey;

    private List<Participant> getRankedParticipantByRecordAndKeyList() {
        if (rankedParticipantListByRecordAndKey == null) {
            rankedParticipantListByRecordAndKey = new ArrayList<>();

            for (int matchUpIndex = 0; matchUpIndex < getRoundAt(0, 0).getTotalMatchUps(); matchUpIndex++) {
                final MatchUp matchUp = getMatchUp(0, 0, matchUpIndex);
                rankedParticipantListByRecordAndKey.add(matchUp.getParticipant1());
                rankedParticipantListByRecordAndKey.add(matchUp.getParticipant2());
            }

        }
        Collections.sort(rankedParticipantListByRecordAndKey, rankedParticipantComparatorForRecordAndKey);

        return rankedParticipantListByRecordAndKey;
    }

    protected void setUpCurrentRanking(final Ranking ranking) {
        getRecordKeepingTournamentTrait().setUpCurrentRanking(ranking);
    }

    public TournamentType getType() {
        return TournamentType.SWISS;
    }


    private static String getPairKey(final Participant participant1, final Participant participant2) {
        if (participant1.getKey().compareTo(participant2.getKey()) <= 0)
            return participant1.getKey() + participant2.getKey();
        else
            return participant2.getKey() + participant1.getKey();
    }


    private static List<Pair<Participant, Participant>> getProperPairing(final List<Participant> rankedParticipantList, final Set<String> matchUpHistory) {

        if (rankedParticipantList.size() == 2) {
            final Participant participant1 = rankedParticipantList.get(0);
            final Participant participant2 = rankedParticipantList.get(1);
            if (!matchUpHistory.contains(getPairKey(participant1, participant2))) {
                final Pair pair = new Pair(participant1, participant2);
                final List<Pair<Participant, Participant>> pairList = new ArrayList<>();
                pairList.add(pair);
                return pairList;
            } else {
                return null;
            }
        } else if (rankedParticipantList.size() > 2 && rankedParticipantList.size() % 2 == 0) {

            final Participant participant1 = rankedParticipantList.get(0);
            for (int i = 1; i < rankedParticipantList.size(); i++) {

                final Participant participant2 = rankedParticipantList.get(i);

                if (!matchUpHistory.contains(getPairKey(participant1, participant2))) {
                    final List<Participant> subRankedParticipantList = new ArrayList<>();
                    for (int j = 1; j < rankedParticipantList.size(); j++) {
                        if (j != i)
                            subRankedParticipantList.add(rankedParticipantList.get(j));
                    }

                    final List<Pair<Participant, Participant>> subProperPairing = getProperPairing(subRankedParticipantList, matchUpHistory);
                    if (subProperPairing != null) {
                        final Pair pair = new Pair(participant1, participant2);

                        subProperPairing.add(0, pair);
                        return subProperPairing;
                    }
                }
            }
            return null;


        } else {
            return null;
        }
    }
}
