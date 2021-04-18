package com.dgnt.quickTournamentMaker.service.interfaces

import android.content.res.Resources
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType

interface ICreateDefaultTitleService {

    /**
     * create default round group title
     *
     * @param resources the android context
     * @param tournamentType the tournament type
     * @param roundGroup the round group
     * @return the title of the round group
     */
    fun forRoundGroup(resources: Resources, tournamentType: TournamentType, roundGroup: RoundGroup): String

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
    fun forParticipant(resources: Resources, participantNumber: Int): String


}