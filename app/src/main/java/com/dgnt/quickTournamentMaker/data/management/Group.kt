package com.dgnt.quickTournamentMaker.data.management

import com.dgnt.quickTournamentMaker.data.IKeyable

data class Group(var name:String, var note:String, var favourite:Boolean): IKeyable, Comparable<Group> {
    override val key: String = name
    override fun compareTo(other: Group): Int {
        if (favourite && !other.favourite) return -1
        if (!favourite && other.favourite) return 1
        return name.compareTo(other.name)
    }
}