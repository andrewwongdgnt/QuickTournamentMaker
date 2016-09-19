package com.dgnt.quickTournamentMaker.eventListener;

/**
 * Created by Owner on 4/2/2016.
 */
public interface OnTournamentUpdateListener {
    public void onTournamentTitleChange(final String title);
    public void onTournamentDescriptionChange(final String description);
    public void onTournamentCreationTimeInEpochChange(final long epoch);
    public void onTournamentLastModifiedTimeInEpochChange(final long epoch);
}
