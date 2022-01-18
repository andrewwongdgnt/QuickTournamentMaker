package com.dgnt.quickTournamentMaker.data.tournament

import com.dgnt.quickTournamentMaker.data.base.BaseRepository

class TournamentRepository(dao: TournamentDAO) : BaseRepository<TournamentEntity>(dao), ITournamentRepository