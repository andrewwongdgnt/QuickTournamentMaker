package com.dgnt.quickTournamentMaker.ui.main.home

import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup

class GroupCheckedExpandableGroup(title: String, persons: List<Person>) : MultiCheckExpandableGroup(title, persons), Comparable<GroupCheckedExpandableGroup> {
    override fun compareTo(other: GroupCheckedExpandableGroup): Int = title.compareTo(other.title)


}
