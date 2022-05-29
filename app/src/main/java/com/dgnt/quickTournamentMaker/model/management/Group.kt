package com.dgnt.quickTournamentMaker.model.management

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.data.management.GroupEntity
import com.dgnt.quickTournamentMaker.model.IKeyable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(var id: String = "", var name: String = "", var note: String = "", var favourite: Boolean = false) : IKeyable<String>, Comparable<Group>, Parcelable {

    companion object {
        fun fromEntity(entity: GroupEntity) = Group(entity.id, entity.name, entity.note, entity.favourite)
    }

    override val key = id + name
    override fun compareTo(other: Group): Int {
        if (favourite && !other.favourite) return -1
        if (!favourite && other.favourite) return 1
        return name.compareTo(other.name)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Group) {
            return other.key == key
        }
        return super.equals(other)
    }

    override fun toString() = name

    override fun hashCode() = id.hashCode()
    fun toEntity() = GroupEntity(id, name, note, favourite)


}