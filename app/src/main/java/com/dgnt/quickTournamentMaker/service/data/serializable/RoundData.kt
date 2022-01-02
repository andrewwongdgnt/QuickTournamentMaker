package com.dgnt.quickTournamentMaker.service.data.serializable

import kotlinx.serialization.Serializable

@Serializable
data class RoundData(
    val roundGroupIndex: Int,
    val roundIndex: Int,
    val originalName: String,
    val name: String,
    val note: String,
    val color: Int,
)
