package com.dgnt.quickTournamentMaker.data.tournament

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roundTable")
data class RoundEntity(
    @PrimaryKey val epoch: Long,
    val roundGroupIndex: Int,
    val roundIndex: Int,
    val name: String,
    val note: String,
    val color: Int
)