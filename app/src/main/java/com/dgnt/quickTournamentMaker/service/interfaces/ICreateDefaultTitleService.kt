package com.dgnt.quickTournamentMaker.service.interfaces

import android.content.res.Resources
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Round

interface ICreateDefaultTitleService {

    /**
     * create default round title
     *
     * @param resources the android context
     * @param round the round
     * @return the title of the round
     */
    fun forRound(resources: Resources, round: Round): String

    /**
     * create default round title
     *
     * @param resources the android context
     * @param matchUp the match up
     * @return the title of the round
     */
    fun forMatchUp(resources: Resources, matchUp: MatchUp): String

    /**
     * naming the participant during quick start
     *
     * @param resources the android context
     * @param participantNumber the number that was assigned to the participant. This only happens in quick start
     */
    fun forParticipant(resources: Resources, participantNumber:Int): String
}