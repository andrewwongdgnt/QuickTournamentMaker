package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.MatchUpStatus

interface IRecordUpdateService {

    /**
     * update the records of participants for the particular match up
     *
     * @param matchUp the current match up
     * @param previousMatchUpStatus the previous match up status
     */
    fun update(matchUp: MatchUp, previousMatchUpStatus: MatchUpStatus)
}