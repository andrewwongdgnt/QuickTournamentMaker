package com.dgnt.quickTournamentMaker.model.tournament

data class ColorInfo(val name: String, val hex: Int) {
    override fun toString() = name

    override fun equals(other: Any?): Boolean {
        if (other is ColorInfo) {
            return other.hex == hex
        }
        return super.equals(other)
    }
}
