package com.dgnt.quickTournamentMaker.model.tournament;

import com.dgnt.quickTournamentMaker.eventListener.OnMatchUpUpdateListener;
import com.dgnt.quickTournamentMaker.model.IKeyable;
import com.dgnt.quickTournamentMaker.util.TournamentUtil;

/**
 * Created by Owner on 3/13/2016.
 */
public class MatchUp implements IKeyable {


    public enum MatchUpStatus {
        DEFAULT, TIE, P1_WINNER, P2_WINNER,
    }

    private int roundGroupIndex;
    private int roundIndex;
    private int matchUpIndex;

    private MatchUpStatus status;

    private Participant participant1;
    private Participant participant2;

    private String note;
    private int color;

    public MatchUp(final int roundGroupIndex, final int roundIndex, final int matchUpIndex, final Participant participant1, final Participant participant2) {
        this.roundGroupIndex = roundGroupIndex;
        this.roundIndex = roundIndex;
        this.matchUpIndex = matchUpIndex;
        this.participant1 = participant1;
        this.participant2 = participant2;
        status = MatchUpStatus.DEFAULT;
        note = "";
        color = TournamentUtil.DEFAULT_DISPLAY_COLOR;
    }

    private OnMatchUpUpdateListener onMatchUpUpdateListener;

    public void setOnMatchUpUpdateListener(OnMatchUpUpdateListener onMatchUpUpdateListener) {
        this.onMatchUpUpdateListener = onMatchUpUpdateListener;
    }

    public void dispatchMatchUpUpdate() {
        if (onMatchUpUpdateListener != null) {
            onMatchUpUpdateListener.onMatchUpUpdate(this);
        }
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

    public Participant getParticipant1() {
        return participant1;
    }

    public void setParticipant1(Participant participant1) {
        this.participant1 = participant1;
        dispatchMatchUpUpdate();
    }

    public Participant getParticipant2() {
        return participant2;
    }

    public void setParticipant2(Participant participant2) {
        this.participant2 = participant2;
        dispatchMatchUpUpdate();
    }

    public MatchUpStatus getStatus() {
        return status;
    }

    public boolean isDefaultStatus() {
        return status == MatchUpStatus.DEFAULT;
    }

    public void setStatus(final MatchUpStatus status) {
        this.status = status;
        dispatchMatchUpUpdate();
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
        dispatchMatchUpUpdate();
    }

    public int getColor() {
        return color == 0 ? TournamentUtil.DEFAULT_DISPLAY_COLOR : color;
    }

    public void setColor(int color) {
        this.color = color;
        dispatchMatchUpUpdate();
    }

    public String getKey() {
        return ParticipantCoordinates.generateKeyFromTournamentIndices(String.valueOf(roundGroupIndex), String.valueOf(roundIndex), String.valueOf(matchUpIndex));

    }

    public String getKey(final int participantIndex) {
        return ParticipantCoordinates.generateKeyFromTournamentIndices(String.valueOf(roundGroupIndex), String.valueOf(roundIndex), String.valueOf(matchUpIndex), String.valueOf(participantIndex));
    }

    public String toString() {
        return getParticipant1() + " vs " + getParticipant2();
    }
}
