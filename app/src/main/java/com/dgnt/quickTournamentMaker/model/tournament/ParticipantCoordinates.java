package com.dgnt.quickTournamentMaker.model.tournament;

import com.dgnt.quickTournamentMaker.model.IKeyable;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Owner on 5/28/2016.
 */
public class ParticipantCoordinates implements IKeyable {

    private int roundGroupIndex;
    private int roundIndex;
    private int matchUpIndex;
    private int participantIndex;

    public ParticipantCoordinates(final int roundGroupIndex, final int roundIndex, final int matchUpIndex, final int participantIndex) {
        this.roundGroupIndex = roundGroupIndex;
        this.roundIndex = roundIndex;
        this.matchUpIndex = matchUpIndex;
        this.participantIndex = participantIndex;
    }

    public int getRoundGroupIndex() {
        return roundGroupIndex;
    }

    public int getRoundIndex() {
        return roundIndex;
    }

    public int getMatchUpIndex() {
        return matchUpIndex;
    }

    public int getParticipantIndex() {
        return participantIndex;
    }

    public String getKey() {
        return generateKeyFromTournamentIndices(String.valueOf(roundGroupIndex), String.valueOf(roundIndex), String.valueOf(matchUpIndex),String.valueOf(participantIndex));
    }

    public static String generateKeyFromTournamentIndices(String... indices){
        return StringUtils.join(indices, ";");

    }
}
