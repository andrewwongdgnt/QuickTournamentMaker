package com.dgnt.quickTournamentMaker.ui.main.management

import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class GroupExpandableGroup(title: String, persons: List<Person>) : ExpandableGroup<Person>(title, persons)
