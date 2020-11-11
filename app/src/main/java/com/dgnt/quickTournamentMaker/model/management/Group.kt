package com.dgnt.quickTournamentMaker.model.management

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.data.management.GroupEntity
import com.dgnt.quickTournamentMaker.model.IKeyable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Group(var id: String, var name: String, var note: String, var favourite: Boolean) : IKeyable, Comparable<Group>, Parcelable {
    override val key: String = name
    override fun compareTo(other: Group): Int {
        if (favourite && !other.favourite) return -1
        if (!favourite && other.favourite) return 1
        return name.compareTo(other.name)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Group) {
            val otherGroup = other as Group
            return otherGroup.id == id
        }
        return super.equals(other)
    }

    override fun hashCode(): Int = id.hashCode()
    fun toEntity(): GroupEntity = GroupEntity(id, name, note, favourite)

    companion object {
        fun fromEntity(groupEntity: GroupEntity): Group = Group(groupEntity.id, groupEntity.name, groupEntity.note, groupEntity.favourite)
    }
}