package com.dgnt.quickTournamentMaker.model.tournament

interface IRankConfig {

    /**
     * Get the string triple representation of this rank config
     *
     * @return the string triple
     */
    val stringTripleRepresentation:Triple<String,String,String>
}