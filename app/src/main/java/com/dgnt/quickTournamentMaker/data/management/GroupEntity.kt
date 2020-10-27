package com.dgnt.quickTournamentMaker.data.management

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groupTable")
data class GroupEntity(
    @PrimaryKey val name: String,
    val note: String,
    val favourite: Boolean
)