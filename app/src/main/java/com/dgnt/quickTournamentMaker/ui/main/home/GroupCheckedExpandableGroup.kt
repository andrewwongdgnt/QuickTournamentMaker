package com.dgnt.quickTournamentMaker.ui.main.home

import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup
import java.util.*

class GroupCheckedExpandableGroup(title: String, persons: List<Person>) : MultiCheckExpandableGroup(title, persons), Comparable<GroupCheckedExpandableGroup> {
    override fun compareTo(other: GroupCheckedExpandableGroup): Int = title.compareTo(other.title)

    fun checkSelections() {
        if (selectedChildren != null) {
            Arrays.fill(selectedChildren, true)
        }
    }
}
