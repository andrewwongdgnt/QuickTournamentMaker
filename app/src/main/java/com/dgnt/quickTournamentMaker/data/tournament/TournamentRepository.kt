package com.dgnt.quickTournamentMaker.data.tournament

import com.dgnt.quickTournamentMaker.data.base.BaseRepository

class TournamentRepository(private val dao: TournamentDAO) : BaseRepository<TournamentEntity>(dao), ITournamentRepository