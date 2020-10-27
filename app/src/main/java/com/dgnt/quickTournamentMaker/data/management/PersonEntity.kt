package com.dgnt.quickTournamentMaker.data.management

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personTable")
data class PersonEntity(
    @PrimaryKey val name: String,
    val note: String,
    val groupName: String
)
