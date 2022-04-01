package com.dgnt.quickTournamentMaker.data.search

import androidx.room.Entity
import androidx.room.PrimaryKey

const val SEARCH_TERM_TABLE = "searchTermTable"

@Entity(tableName = SEARCH_TERM_TABLE)
data class SearchTermEntity(
    @PrimaryKey
    val term: String,
    val count: Int,
)