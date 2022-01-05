package com.dgnt.quickTournamentMaker.data.tournament

import com.dgnt.quickTournamentMaker.data.base.BaseRepository
import org.joda.time.LocalDateTime

class ParticipantRepository(private val dao: ParticipantDAO) : BaseRepository<ParticipantEntity>(dao), IParticipantRepository {
    override fun getAll(epoch: LocalDateTime) = dao.getAll(epoch)
}