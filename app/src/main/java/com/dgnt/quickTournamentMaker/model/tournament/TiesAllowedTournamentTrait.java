package com.dgnt.quickTournamentMaker.model.tournament;

/**
 * Created by Owner on 9/13/2016.
 */
public class TiesAllowedTournamentTrait {

    private Tournament tournament;
    public TiesAllowedTournamentTrait(final Tournament tournament) {
        this.tournament = tournament;
    }

    public MatchUp.MatchUpStatus getNewMatchUpStatus(final MatchUp.MatchUpStatus currentStatus, final int participantIndex) {

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
                return MatchUp.MatchUpStatus.TIE;
            }
        } else if (currentStatus == MatchUp.MatchUpStatus.P2_WINNER) {
            if (participantIndex == 0) {
                return MatchUp.MatchUpStatus.TIE;
            } else {
                return MatchUp.MatchUpStatus.DEFAULT;
            }
        } else if (currentStatus == MatchUp.MatchUpStatus.TIE) {
            if (participantIndex == 0) {
                return MatchUp.MatchUpStatus.P2_WINNER;
            } else {
                return MatchUp.MatchUpStatus.P1_WINNER;
            }
        }

        return null;

    }
}
