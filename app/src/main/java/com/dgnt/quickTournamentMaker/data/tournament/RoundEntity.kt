package com.dgnt.quickTournamentMaker.data.tournament

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import org.joda.time.LocalDateTime

const val ROUND_TABLE = "roundTable"

@Parcelize
@Entity(tableName = ROUND_TABLE, primaryKeys = ["epoch", "roundGroupIndex", "roundIndex"])
data class RoundEntity(
    val epoch: LocalDateTime,
    val roundGroupIndex: Int,
    val roundIndex: Int,
    val name: String,
    val note: String,
    val color: Int
) : Parcelable