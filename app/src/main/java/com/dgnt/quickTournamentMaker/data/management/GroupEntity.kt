package com.dgnt.quickTournamentMaker.data.management

import androidx.room.Entity
import androidx.room.PrimaryKey


const val GROUP_TABLE = "groupTable"

@Entity(tableName = GROUP_TABLE)
data class GroupEntity(
    @PrimaryKey val name: String,
    val note: String,
    val favourite: Boolean
)