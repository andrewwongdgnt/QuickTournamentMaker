package com.dgnt.quickTournamentMaker.data.management

import androidx.room.Entity
import androidx.room.PrimaryKey

const val PERSON_TABLE = "personTable"

@Entity(tableName = PERSON_TABLE)
data class PersonEntity(
    @PrimaryKey val name: String,
    val note: String,
    val groupName: String
)
