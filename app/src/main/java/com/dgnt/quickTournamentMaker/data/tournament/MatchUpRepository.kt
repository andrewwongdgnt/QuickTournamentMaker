package com.dgnt.quickTournamentMaker.data.tournament

import com.dgnt.quickTournamentMaker.data.base.BaseRepository
import org.joda.time.LocalDateTime

class MatchUpRepository(private val dao: MatchUpDAO) : BaseRepository<MatchUpEntity>(dao), IMatchUpRepository {

    override fun getAll(epoch: LocalDateTime) = dao.getAll(epoch)

}