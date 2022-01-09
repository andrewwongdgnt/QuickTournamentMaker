package com.dgnt.quickTournamentMaker.data.tournament

import com.dgnt.quickTournamentMaker.data.base.BaseRepository

class RoundRepository(dao: RoundDAO) : BaseRepository<RoundEntity>(dao), IRoundRepository