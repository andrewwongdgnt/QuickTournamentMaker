package com.dgnt.quickTournamentMaker.data.tournament

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.LocalDateTime

const val ROUND_TABLE = "roundTable"

@Entity(tableName = ROUND_TABLE, primaryKeys = ["epoch", "roundGroupIndex", "roundIndex"])
data class RoundEntity(
    val epoch: LocalDateTime,
    val roundGroupIndex: Int,
    val roundIndex: Int,
    val originalName: String,
    val name: String,
    val note: String,
    val color: Int
)