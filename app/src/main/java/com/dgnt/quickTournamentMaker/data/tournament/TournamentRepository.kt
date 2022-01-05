package com.dgnt.quickTournamentMaker.data.tournament

import com.dgnt.quickTournamentMaker.data.base.BaseRepository
import org.joda.time.LocalDateTime

class TournamentRepository(private val dao: TournamentDAO) : BaseRepository<TournamentEntity>(dao), ITournamentRepository {
    override fun getAll(epoch: LocalDateTime) = dao.getAll(epoch)
}