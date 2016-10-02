package com.dgnt.quickTournamentMaker.model.tournament;

/**
 * Created by Owner on 9/13/2016.
 */
public interface RecordKeepingTournament {

    public void setRankingConfigFromPriority(final RecordKeepingTournamentTrait.RankingFromPriority rankingFromPriority);

    public void setRankingConfigFromScore(final RecordKeepingTournamentTrait.RankingFromScore rankingFromScore);
}
