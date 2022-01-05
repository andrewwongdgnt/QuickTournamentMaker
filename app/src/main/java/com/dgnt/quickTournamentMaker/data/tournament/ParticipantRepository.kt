package com.dgnt.quickTournamentMaker.data.tournament

import org.joda.time.LocalDateTime

class ParticipantRepository(private val dao: ParticipantDAO) : IParticipantRepository {

    override fun getAll(epoch: LocalDateTime) = dao.getAll(epoch)
    override suspend fun insert(vararg entity: ParticipantEntity) = dao.insert(*entity)
    override suspend fun insert(entities: List<ParticipantEntity>) = dao.insert(entities)
    override suspend fun update(vararg entity: ParticipantEntity) = dao.update(*entity)
    override suspend fun update(entities: List<ParticipantEntity>) = dao.update(entities)
    override suspend fun delete(vararg entity: ParticipantEntity) = dao.delete(*entity)
    override suspend fun delete(entities: List<ParticipantEntity>) = dao.delete(entities)
}