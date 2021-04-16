package com.dgnt.quickTournamentMaker.ui.tournament

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class RoundExpandableGroup(val round: Round, matchUps: List<MatchUp>) : ExpandableGroup<MatchUp>(round.title, matchUps)

