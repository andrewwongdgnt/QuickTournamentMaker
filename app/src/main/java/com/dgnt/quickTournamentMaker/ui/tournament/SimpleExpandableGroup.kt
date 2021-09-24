package com.dgnt.quickTournamentMaker.ui.tournament

import com.dgnt.quickTournamentMaker.util.SimpleParcelable
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class SimpleExpandableGroup(val groupTitle: String, childTitle: List<SimpleParcelable>) : ExpandableGroup<SimpleParcelable>(groupTitle, childTitle)

