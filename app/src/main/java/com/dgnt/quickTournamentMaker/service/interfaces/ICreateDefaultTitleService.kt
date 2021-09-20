package com.dgnt.quickTournamentMaker.service.interfaces

import android.content.res.Resources
import com.dgnt.quickTournamentMaker.model.tournament.*

interface ICreateDefaultTitleService {

    /**
     * create default round group title
     *
     * @param resources the android context
     * @param tournamentType the tournament type
     * @param roundGroupIndex the round group index
     * @return the title of the round group
     */
    fun forRoundGroup(resources: Resources, tournamentType: TournamentType, roundGroupIndex: Int): String

    /**
     * create default round title
     *
     * @param resources the android context
     * @param roundIndex the round index in a given round group
     * @return the title of the round
     */
    fun forRound(resources: Resources, roundIndex: Int): String

    /**
     * create default match up title
     *
     * @param resources the android context
     * @param matchUpIndex the match up index in a given round
     * @param participant1 the first participant
     * @param participant2 the second participant
     * @return the title of the round
     */
    fun forMatchUp(resources: Resources, matchUpIndex: Int, participant1: Participant, participant2: Participant): String

    /**
     * naming the participant during quick start
     *
     * @param resources the android context
     * @param participantNumber the number that was assigned to the participant. This only happens in quick start
     */
    fun forParticipant(resources: Resources, participantNumber: Int): String


}