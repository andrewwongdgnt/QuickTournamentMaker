package com.dgnt.quickTournamentMaker.ui.tournament

import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class RoundGroupExpandableGroup(val roundGroup: RoundGroup, rounds: List<Round>) : ExpandableGroup<Round>(roundGroup.title, rounds)

