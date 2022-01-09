package com.dgnt.quickTournamentMaker.data.tournament

import com.dgnt.quickTournamentMaker.data.base.BaseRepository

class MatchUpRepository(dao: MatchUpDAO) : BaseRepository<MatchUpEntity>(dao), IMatchUpRepository