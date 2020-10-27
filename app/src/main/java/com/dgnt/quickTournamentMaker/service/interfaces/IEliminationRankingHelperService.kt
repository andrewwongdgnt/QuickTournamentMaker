package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.Rank
import com.dgnt.quickTournamentMaker.model.tournament.Round

interface IEliminationRankingHelperService {
    /**
     * calculate the rank of each participant for elimination type tournaments. Both in known rankings and unknown
     *
     * @param roundsFirst the first rounds
     * @param roundsLoserBracketNonFinalist the rounds that determine the losers excluding the finalist rounds
     * @param roundsWinnerBracket the rounds that determine the winner and second place
     * @return the rank
     */
    fun calculate(roundsFirst:List<Round>, roundsLoserBracketNonFinalist:List<Round>, roundsWinnerBracket:List<Round>): Rank
}