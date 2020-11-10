package com.dgnt.quickTournamentMaker.data.management

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

const val PERSON_TABLE = "personTable"

@Entity(tableName = PERSON_TABLE, indices = [Index(value = ["name"], unique = true)])
data class PersonEntity(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    val name: String,
    val note: String,
    val groupName: String
)
