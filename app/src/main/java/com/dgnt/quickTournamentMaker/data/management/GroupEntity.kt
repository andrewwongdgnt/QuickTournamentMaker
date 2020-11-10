package com.dgnt.quickTournamentMaker.data.management

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*


const val GROUP_TABLE = "groupTable"

@Entity(tableName = GROUP_TABLE, indices = [Index(value = ["name"], unique = true)])
data class GroupEntity(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    val name: String,
    val note: String,
    val favourite: Boolean
)