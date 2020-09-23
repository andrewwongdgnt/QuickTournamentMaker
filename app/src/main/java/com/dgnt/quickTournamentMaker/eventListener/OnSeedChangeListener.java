package com.dgnt.quickTournamentMaker.eventListener;

import com.dgnt.quickTournamentMaker.model.tournament.Participant;

import java.util.List;

/**
 * Created by Owner on 3/26/2016.
 */
public interface OnSeedChangeListener {

    public void onParticipantListChange(final List<Participant> participants);
    public void onFirstPickChange(final int index);

}
