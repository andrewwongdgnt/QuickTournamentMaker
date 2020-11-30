package com.dgnt.quickTournamentMaker.model.tournament

enum class TournamentType {
    ELIMINATION, DOUBLE_ELIMINATION, ROUND_ROBIN, SWISS, SURVIVAL
}

data class Tournament(val name: String, val description: String) {
}