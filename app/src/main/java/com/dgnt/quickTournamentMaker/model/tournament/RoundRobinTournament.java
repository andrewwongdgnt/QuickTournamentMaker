package com.dgnt.quickTournamentMaker.model.tournament;

import com.dgnt.quickTournamentMaker.eventListener.OnMatchUpUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnParticipantUpdateListener;
import com.dgnt.quickTournamentMaker.eventListener.OnTournamentUpdateListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Owner on 3/13/2016.
 */
public class RoundRobinTournament extends Tournament implements RecordKeepingTournament {


    private RecordKeepingTournamentTrait recordKeepingTournamentTrait;
    protected RecordKeepingTournamentTrait getRecordKeepingTournamentTrait() {
        if (recordKeepingTournamentTrait ==null)
            recordKeepingTournamentTrait = new RecordKeepingTournamentTrait(this);
        return recordKeepingTournamentTrait;
    }

    private TiesAllowedTournamentTrait tiesAllowedTournamentTrait;
    protected TiesAllowedTournamentTrait getTiesAllowedTournamentTrait() {
        if (tiesAllowedTournamentTrait==null)
            tiesAllowedTournamentTrait = new TiesAllowedTournamentTrait(this);
        return tiesAllowedTournamentTrait;
    }

    private Map<Participant, Set<ParticipantCoordinates>> participantCoordinatesMap = new HashMap<>();

    public boolean build(final List<Participant> orderedParticipantList, final OnTournamentUpdateListener onTournamentUpdateListener, final OnMatchUpUpdateListener onMatchUpUpdateListener, final OnParticipantUpdateListener onParticipantUpdateListener) {
        final boolean initialStatus = super.build(orderedParticipantList,onTournamentUpdateListener, onMatchUpUpdateListener,onParticipantUpdateListener);

        if (!initialStatus)
            return false;

        //set up coordinates for round 1
        for (int matchUpIndex = 0; matchUpIndex < getRoundAt(0, 0).getTotalMatchUps(); matchUpIndex++) {
            addToParticipantCoordinates(getRoundAt(0, 0).getMatchUpAt(matchUpIndex).getParticipant1(), new ParticipantCoordinates(0, 0, matchUpIndex, 0));
            addToParticipantCoordinates(getRoundAt(0, 0).getMatchUpAt(matchUpIndex).getParticipant2(), new ParticipantCoordinates(0, 0, matchUpIndex, 1));
        }


        //Total participants (including byes).
        final int totalParticipants = orderedParticipantList.size();

        //set match ups for remaining rounds. There will be totalParticipants - 1 rounds in total. EG with 6 participants, 6 - 1 = 5, so 5 rounds in total
        for (int roundIndex = 1; roundIndex < totalParticipants - 1; roundIndex++) {

            //Round robin scheduling works like this
            //for a given match up, the participants will come from a match up in the previous round.


            final List<MatchUp> matchUps = new ArrayList<>();
            for (int j = 0; j < totalParticipants; j += 2) {

                final int matchUpIndex = j / 2;

                final Round previousRound = rounds.get(roundIndex - 1);

                final int participant1Index = j;
                final int participant2Index = j + 1;

                Participant currentParticipant1;
                Participant currentParticipant2;
                //if we are dealing with the first match up:
                // - the first participant is the first participant as the previous match up with the same index
                // - the second participant is the first participant as the previous match up 1 index higher
                if (participant1Index == 0 && participant2Index == 1) {
                    currentParticipant1 = previousRound.getMatchUpAt(0).getParticipant1();
                    currentParticipant2 = previousRound.getMatchUpAt(1).getParticipant1();


                }
                //if we are dealing with the last match up:
                // - the first participant is the second participant as the previous match up with the same index
                // - the second participant is the second participant as the previous match up 1 index lower
                else if (j == totalParticipants - 2) {
                    currentParticipant1 = previousRound.getMatchUpAt(matchUpIndex).getParticipant2();
                    currentParticipant2 = previousRound.getMatchUpAt(matchUpIndex - 1).getParticipant2();

                }
                //if we are dealing with any other match ups:
                // - the first participant is the first participant as the previous match up 1 index higher
                // - the second participant is the second participant as the previous match up 1 index lower
                else {
                    currentParticipant1 = previousRound.getMatchUpAt(matchUpIndex + 1).getParticipant1();
                    currentParticipant2 = previousRound.getMatchUpAt(matchUpIndex - 1).getParticipant2();

                }

                final MatchUp matchUp = new MatchUp(0, roundIndex, matchUpIndex, currentParticipant1, currentParticipant2);
                matchUp.setOnMatchUpUpdateListener(onMatchUpUpdateListener);
                matchUps.add(matchUp);

                addToParticipantCoordinates(currentParticipant1, new ParticipantCoordinates(0, roundIndex, matchUpIndex, 0));
                addToParticipantCoordinates(currentParticipant2, new ParticipantCoordinates(0, roundIndex, matchUpIndex, 1));
            }

            rounds.add(new Round(matchUps));
        }


        return true;
    }


    protected void addToParticipantCoordinates(final Participant participant, final ParticipantCoordinates participantCoordinates) {
        if (participantCoordinatesMap.get(participant) == null) {
            participantCoordinatesMap.put(participant, new HashSet<ParticipantCoordinates>());
        }
        final Set<ParticipantCoordinates> participantCoordinatesSet = participantCoordinatesMap.get(participant);
        participantCoordinatesSet.add(participantCoordinates);
    }

    protected MatchUp.MatchUpStatus getNewMatchUpStatus(final MatchUp.MatchUpStatus currentStatus, final int participantIndex) {
        return getTiesAllowedTournamentTrait().getNewMatchUpStatus(currentStatus,participantIndex);
    }

    protected void updateTournamentOnResultChange(final int roundGroupIndex, final int roundIndex, final int matchUpIndex, final MatchUp.MatchUpStatus previousStatus, final MatchUp.MatchUpStatus status) {
        getRecordKeepingTournamentTrait().updateParticipantsRecordOnResultChange(roundGroupIndex,roundIndex,matchUpIndex,previousStatus,status);
    }
    public Set<ParticipantCoordinates> getParticipantCoordinates(final Participant participant) {
        return participantCoordinatesMap.get(participant);
    }

    protected void setUpCurrentRanking(final Ranking ranking) {
        getRecordKeepingTournamentTrait().setUpCurrentRanking(ranking);
    }

    public TournamentType getType() {
        return TournamentType.ROUND_ROBIN;
    }
}