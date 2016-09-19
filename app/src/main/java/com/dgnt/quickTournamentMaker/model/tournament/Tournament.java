package com.dgnt.quickTournamentMaker.model.tournament;

import com.dgnt.quickTournamentMaker.eventListener.OnMatchUpUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnParticipantUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnTournamentUpdateListener;
import com.dgnt.quickTournamentMaker.model.IKeyable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Owner on 3/13/2016.
 */
public abstract class Tournament implements IKeyable {

    public enum TournamentType {
        ELIMINATION, DOUBLE_ELIMINATION, ROUND_ROBIN, SWISS, SURVIVAL
    }

    public abstract TournamentType getType();

    final public static long NULL_TIME_VALUE = -1;

    protected String title;

    public void setTitle(final String title) {
        this.title = title;
        dispatchTournamentTitleUpdateEvent(title);
    }

    public String getTitle() {
        return title;
    }


    protected String description;

    public void setDescription(final String description) {
        this.description = description;
        dispatchTournamentDescriptionUpdateEvent(description);
    }

    public String getDescription() {
        return description;
    }

    protected long creationTimeInEpoch;

    public void setCreationTimeInEpoch(final long creationTimeInEpoch) {
        this.creationTimeInEpoch = creationTimeInEpoch;
        dispatchTournamentCreationTimeInEpochUpdateEvent(creationTimeInEpoch);
    }

    public long getCreationTimeInEpoch() {
        return creationTimeInEpoch;
    }

    protected long lastModifiedTimeInEpoch;

    public void setLastModifiedTimeInEpoch(final long lastModifiedTimeInEpoch) {
        this.lastModifiedTimeInEpoch = lastModifiedTimeInEpoch;
        dispatchTournamentLastModifiedTimeInEpochUpdateEvent(lastModifiedTimeInEpoch);
    }

    public long getLastModifiedTimeInEpoch() {
        return lastModifiedTimeInEpoch;
    }

    private List<Participant> seededParticipantList;

    public List<Participant> getSeededParticipants() {
        return seededParticipantList;
    }


    private OnTournamentUpdateListener onTournamentUpdateListener;

    public void dispatchTournamentTitleUpdateEvent(final String title) {
        if (onTournamentUpdateListener != null) {
            onTournamentUpdateListener.onTournamentTitleChange(title);
        }
    }

    public void dispatchTournamentDescriptionUpdateEvent(final String description) {
        if (onTournamentUpdateListener != null) {
            onTournamentUpdateListener.onTournamentDescriptionChange(description);
        }
    }

    public void dispatchTournamentCreationTimeInEpochUpdateEvent(final long epoch) {
        if (onTournamentUpdateListener != null) {
            onTournamentUpdateListener.onTournamentCreationTimeInEpochChange(epoch);
        }
    }

    public void dispatchTournamentLastModifiedTimeInEpochUpdateEvent(final long epoch) {
        if (onTournamentUpdateListener != null) {
            onTournamentUpdateListener.onTournamentLastModifiedTimeInEpochChange(epoch);
        }
    }

    protected List<Round> rounds;

    public boolean build(final List<Participant> orderedParticipants) {
        return build(orderedParticipants, null, null,null);
    }

    public boolean build(final List<Participant> orderedParticipantList, final OnTournamentUpdateListener onTournamentUpdateListener, final OnMatchUpUpdateListener onMatchUpUpdateListener, final OnParticipantUpdateListener onParticipantUpdateListener) {


        if (!isInitialConfigGood(orderedParticipantList))
            return false;

        final List<MatchUp> round1Pairing = new ArrayList<>();

        for (int i = 0; i < orderedParticipantList.size(); i += 2) {
            final int roundIndex = 0;
            final int matchUpIndex = i / 2;

            final Participant participant1 = orderedParticipantList.get(i);
            participant1.setOnParticipantUpdateListener(onParticipantUpdateListener);

            final Participant participant2 = orderedParticipantList.get(i+1);
            participant2.setOnParticipantUpdateListener(onParticipantUpdateListener);

            final MatchUp matchUp = new MatchUp(0, roundIndex, matchUpIndex, participant1, participant2);
            matchUp.setOnMatchUpUpdateListener(onMatchUpUpdateListener);

            round1Pairing.add(matchUp);

        }

        seededParticipantList = new ArrayList<>();
        seededParticipantList.addAll(orderedParticipantList);

        rounds = new ArrayList<>();

        //Round 1 must exist
        final Round round1 = new Round(round1Pairing);
        rounds.add(round1);

        this.onTournamentUpdateListener = onTournamentUpdateListener;

        return true;
    }

    //this gets called when build gets called.  It is to determine if we should even bother building the tournament based on the initial pairing.
    protected boolean isInitialConfigGood(final List<Participant> orderedParticipantList) {
        //Total participants (including byes).
        final int totalParticipants = orderedParticipantList.size();

        //need at least 4 total participants
        if (totalParticipants < 4)
            return false;

        //need to be even amount
        if (totalParticipants % 2 != 0)
            return false;

        return true;

    }

    protected void settleStatusesFromByes(final int roundGroupIndex, final int roundIndex) {
        for (int matchUpIndex = 0; matchUpIndex < getRoundAt(roundGroupIndex, roundIndex).getTotalMatchUps(); matchUpIndex++) {
            final MatchUp matchUp = getMatchUp(roundGroupIndex, roundIndex, matchUpIndex);
            if (doByesExist(matchUp)) {
                if (matchUp.getParticipant2().isBye()) {
                    setResult(roundGroupIndex, roundIndex, matchUpIndex, matchUp.getStatus(), MatchUp.MatchUpStatus.P1_WINNER);
                } else {
                    setResult(roundGroupIndex, roundIndex, matchUpIndex, matchUp.getStatus(), MatchUp.MatchUpStatus.P2_WINNER);
                }


            }
        }
    }

    public void toggleResult(final int roundGroupIndex, final int roundIndex, final int matchUpIndex, final int participantIndex) {
        final MatchUp matchUp = getMatchUp(roundGroupIndex, roundIndex, matchUpIndex);
        if (doByesExist(matchUp))
            return;

        setRankStateDirty();

        final MatchUp.MatchUpStatus currentStatus = matchUp.getStatus();
        final MatchUp.MatchUpStatus newStatus = getNewMatchUpStatus(currentStatus, participantIndex);
        if (newStatus != null)
            setResult(roundGroupIndex, roundIndex, matchUpIndex, currentStatus, newStatus);
    }

    protected MatchUp.MatchUpStatus getNewMatchUpStatus(final MatchUp.MatchUpStatus currentStatus, final int participantIndex) {
        //Based on one winner only
        if (currentStatus == MatchUp.MatchUpStatus.DEFAULT) {
            if (participantIndex == 0) {
                return MatchUp.MatchUpStatus.P1_WINNER;
            } else {
                return MatchUp.MatchUpStatus.P2_WINNER;
            }
        } else if (currentStatus == MatchUp.MatchUpStatus.P1_WINNER) {
            if (participantIndex == 0) {
                return MatchUp.MatchUpStatus.DEFAULT;
            } else {
                return MatchUp.MatchUpStatus.P2_WINNER;
            }
        } else if (currentStatus == MatchUp.MatchUpStatus.P2_WINNER) {
            if (participantIndex == 0) {
                return MatchUp.MatchUpStatus.P1_WINNER;
            } else {
                return MatchUp.MatchUpStatus.DEFAULT;
            }
        }

        return null;
    }

    protected boolean doByesExist(final MatchUp matchUp) {

        //If there is a bye in the match up, then don't allow anything to happen to the match up status.
        final Participant participant1 = matchUp.getParticipant1();
        final Participant participant2 = matchUp.getParticipant2();
        if (participant1.isBye() || participant2.isBye())
            return true;

        return false;
    }


    private void setResult(final int roundGroupIndex, final int roundIndex, final int matchUpIndex, final MatchUp.MatchUpStatus previousStatus, final MatchUp.MatchUpStatus status) {
        if (previousStatus == status) {
            return;
        }

        final Round round = getRoundGroupAt(roundGroupIndex).get(roundIndex);
        final MatchUp matchUp = round.getMatchUpAt(matchUpIndex);
        matchUp.setStatus(status);

        updateTournamentOnResultChange(roundGroupIndex, roundIndex, matchUpIndex, previousStatus, status);

    }

    abstract protected void updateTournamentOnResultChange(final int roundGroupIndex, final int roundIndex, final int matchUpIndex, final MatchUp.MatchUpStatus previousStatus, final MatchUp.MatchUpStatus status);

    public int getTotalMatchUps(final int roundGroupIndex, final int roundIndex) {
        return getRoundAt(roundGroupIndex, roundIndex).getTotalMatchUps();
    }

    public MatchUp getMatchUp(final int roundGroupIndex, final int roundIndex, final int matchUpIndex) {

        final Round round = getRoundGroupAt(roundGroupIndex).get(roundIndex);
        final MatchUp matchUp = round.getMatchUpAt(matchUpIndex);
        return matchUp;

    }

    public MatchUp.MatchUpStatus getMatchUpStatus(final int roundGroupIndex, final int roundIndex, final int matchUpIndex) {
        return getMatchUp(roundGroupIndex, roundIndex, matchUpIndex).getStatus();
    }


    public Participant getParticipant(final int roundGroupIndex, final int roundIndex, final int matchUpIndex, int participantIndex) {
        final MatchUp matchUp = getMatchUp(roundGroupIndex, roundIndex, matchUpIndex);
        return participantIndex == 0 ? matchUp.getParticipant1() : matchUp.getParticipant2();

    }

    public List<Round> getRoundGroupAt(final int roundGroupIndex) {
        return rounds;
    }

    public int getTotalRoundGroups() {
        return 1;
    }

    public Round getRoundAt(final int roundGroupIndex, final int roundIndex) {
        return getRoundGroupAt(roundGroupIndex).get(roundIndex);
    }

    public int getTotalRounds(final int roundGroupIndex) {
        return getRoundGroupAt(roundGroupIndex).size();
    }

    private List<Participant> normalSortedParticipantList;

    public List<Participant> getNormalSortedParticipants() {
        if (normalSortedParticipantList == null) {
            normalSortedParticipantList = new ArrayList<>();
            for (int matchUpIndex = 0; matchUpIndex < getRoundAt(0, 0).getTotalMatchUps(); matchUpIndex++) {
                final Participant participant1 = getMatchUp(0, 0, matchUpIndex).getParticipant1();
                if (participant1.isNormal())
                    normalSortedParticipantList.add(participant1);

                final Participant participant2 = getMatchUp(0, 0, matchUpIndex).getParticipant2();
                if (participant2.isNormal())
                    normalSortedParticipantList.add(participant2);
            }
            Collections.sort(normalSortedParticipantList);
        }
        return normalSortedParticipantList;
    }


    public Set<ParticipantCoordinates> getParticipantCoordinates(final Participant participant) {

        //TODO naive way of getting all the positions of a particular participant.  Consider building up a map.
        final Set<ParticipantCoordinates> participantCoordinates = new HashSet<>();

        for (int roundGroupIndex = 0; roundGroupIndex < getTotalRoundGroups(); roundGroupIndex++) {
            for (int roundIndex = 0; roundIndex < getTotalRounds(roundGroupIndex); roundIndex++) {
                for (int matchUpIndex = 0; matchUpIndex < getRoundAt(roundGroupIndex, roundIndex).getTotalMatchUps(); matchUpIndex++) {
                    for (int participantIndex = 0; participantIndex < 2; participantIndex++) {
                        final Participant participantToCompare = getParticipant(roundGroupIndex, roundIndex, matchUpIndex, participantIndex);
                        if (participantToCompare.equals(participant)) {
                            participantCoordinates.add(new ParticipantCoordinates(roundGroupIndex, roundIndex, matchUpIndex, participantIndex));
                        }
                    }
                }
            }
        }
        return participantCoordinates;
    }

    protected Ranking ranking;

    public Ranking getCurrentRanking() {
        if (ranking == null) {
            ranking = new Ranking();

            setUpCurrentRanking(ranking);
        }
        return ranking;
    }

    abstract protected void setUpCurrentRanking(final Ranking ranking);

    public void setRankStateDirty() {
        ranking = null;
    }

    public boolean isBuilt() {
        return rounds != null;
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
                    sb.append(rounds.get(j).getMatchUpAt(i)).append(" | ");
            }
            sb.append("\n");
        }
        return sb.toString();

    }

    @Override
    public String getKey() {
        return Long.toString(getCreationTimeInEpoch());
    }
}
