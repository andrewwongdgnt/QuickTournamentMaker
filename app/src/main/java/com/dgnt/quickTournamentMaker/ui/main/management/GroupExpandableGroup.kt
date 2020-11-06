package com.dgnt.quickTournamentMaker.ui.main.management

import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup

class GroupExpandableGroup(title: String, persons: List<Person>) : MultiCheckExpandableGroup(title, persons)
