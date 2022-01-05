package com.dgnt.quickTournamentMaker.data.tournament

import com.dgnt.quickTournamentMaker.data.base.BaseRepository
import org.joda.time.LocalDateTime

class RoundRepository(private val dao: RoundDAO) :BaseRepository<RoundEntity>(dao), IRoundRepository {
    override fun getAll(epoch: LocalDateTime) = dao.getAll(epoch)
}