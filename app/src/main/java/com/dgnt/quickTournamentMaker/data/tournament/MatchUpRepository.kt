package com.dgnt.quickTournamentMaker.data.tournament

import org.joda.time.LocalDateTime

class MatchUpRepository(private val dao: MatchUpDAO) : IMatchUpRepository {

    override fun getAll(epoch: LocalDateTime) = dao.getAll(epoch)
    override suspend fun insert(vararg entity: MatchUpEntity) = dao.insert(*entity)
    override suspend fun insert(entities: List<MatchUpEntity>) = dao.insert(entities)
    override suspend fun update(vararg entity: MatchUpEntity) = dao.update(*entity)
    override suspend fun update(entities: List<MatchUpEntity>) = dao.update(entities)
    override suspend fun delete(vararg entity: MatchUpEntity) = dao.delete(*entity)
    override suspend fun delete(entities: List<MatchUpEntity>) = dao.delete(entities)
}